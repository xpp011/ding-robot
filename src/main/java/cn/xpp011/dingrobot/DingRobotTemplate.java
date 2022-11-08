package cn.xpp011.dingrobot;

import cn.xpp011.dingrobot.config.DingRobotProperties.RobotProperties;
import cn.xpp011.dingrobot.excepation.DingRobotSendMsgFailException;
import cn.xpp011.dingrobot.message.FailMessage;
import cn.xpp011.dingrobot.message.Message;
import cn.xpp011.dingrobot.ratelimiter.RateLimiter;
import cn.xpp011.dingrobot.storage.FailMessageQueue;
import com.google.common.collect.Maps;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * ding-robot执行类
 *
 * @author: xpp011 2022-07-19 11:17
 **/
public class DingRobotTemplate {

    private static final Logger log = LoggerFactory.getLogger(DingRobotTemplate.class);

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;

    private final String webhook;

    private final String secret;

    private final ExecutorService executor;

    private final RateLimiter rateLimiter;

    private final int retry;

    /**
     * 失败策略
     */
    private final Function<Message, Boolean> FAIL;

    /**
     * @param client          httpClient
     * @param robotProperties 参数信息
     * @param retry           重试次数
     * @param executor        异步处理线程池
     * @param rateLimiter     限流器
     * @param queue           失败消息队列
     * @param robotName       机器人名称
     */
    public DingRobotTemplate(OkHttpClient client, RobotProperties robotProperties, int retry, ExecutorService executor, RateLimiter rateLimiter, FailMessageQueue queue, String robotName) {
        this.client = client;
        this.retry = retry;
        this.executor = executor;
        this.rateLimiter = rateLimiter;
        Assert.notNull(robotProperties, "robot参数信息缺失");
        this.webhook = robotProperties.getWebhook();
        this.secret = robotProperties.getSecret();
        this.FAIL = (message) -> {
            FailMessage failMessage = new FailMessage(robotName, message);
            return queue.push(failMessage);
        };
    }

    public Future<Boolean> sendAsync(Message message) {
        return sendAsync(message, FAIL);
    }

    public Future<Boolean> sendAsync(Message message, Function<Message, Boolean> fail) {
        return executor.submit(() -> send(message, fail));
    }

    public boolean send(Message message) {
        return send(message, FAIL);
    }

    public boolean send(Message message, Function<Message, Boolean> fail) {
        try {
            //尝试获取令牌
            if (!rateLimiter.tryAcquire()) {
                log.warn("获取令牌失败");
                return fail.apply(message);
            }
            return doSend(message, retry);
        } catch (Exception e) {
            log.error("消息发送失败： {}", e.getMessage(), e);
            return fail.apply(message);
        }
    }

    protected boolean doSend(Message message, int retry) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        boolean res = true;
        try {
            RequestBody requestBody = RequestBody.create(JsonUtil.toJson(message), MEDIA_TYPE_JSON);
            Request request = new Request.Builder()
                    .url(signatureUrl())
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            Map<String, Object> responseBody = Maps.newHashMap();
            if (!response.isSuccessful() || !"ok".equals((responseBody = JsonUtil.toMap(response.body().string())).get("errmsg"))) {
                throw new DingRobotSendMsgFailException((String) responseBody.get("errmsg"));
            }
        } catch (Exception e) {
            //网络io异常重试
            if (ExceptionUtil.isNetworkException(e) && retry > 0) return doSend(message, --retry);
            log.error("sending failed message:{}", message);
            throw e;
        }
        return res;
    }

    /**
     * 获取签名后的url
     *
     * @return 返回签名完成的url
     */
    private String signatureUrl() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        if (StringUtils.isBlank(secret)) {
            return webhook;
        }
        long timeMillis = System.currentTimeMillis();
        String stringToSign = timeMillis + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = URLEncoder.encode(new String(Base64Utils.encode(signData)), StandardCharsets.UTF_8.toString());
        String urlToSign = String.format("%s&timestamp=%s&sign=%s", webhook, timeMillis, sign);
        return urlToSign;
    }

}

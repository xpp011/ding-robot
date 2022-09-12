local key = KEYS[1]
local limit = tonumber(ARGV[1])
local windowSize = tonumber(ARGV[2])
local tryAcquireRemain = tonumber(ARGV[3])
local now = tonumber(redis.call('time')[1])
local floor = now - windowSize;
local time = tonumber(redis.call('lindex', key, 0))

while (time ~= nil and time < floor)
do
    redis.call('lpop', key)
    time = tonumber(redis.call('lindex', key, 0))
end

local size = tonumber(redis.call('llen', key))

if size < limit and limit - size > tryAcquireRemain then
    return tonumber(redis.call('rpush', key, now))
end
return 0
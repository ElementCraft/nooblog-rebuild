package com.noobug.NooblogRebuild.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisBase<K extends Serializable, V extends Serializable> {
    @Autowired
    protected RedisTemplate<K, V> redisTemplate;

    @Autowired
    protected RedisUtils redisUtils;

    // ====================== 服务器端 相关操作 ===============================


    public void select(Integer n) {
        Jedis conn = redisUtils.getConnection();
        conn.select(n);
        log.debug("【Redis】切换到了数据库：{}", n);
    }

    public String info() {
        Jedis conn = redisUtils.getConnection();
        return conn.info();
    }

    public void bgsave() {
        Jedis conn = redisUtils.getConnection();
        conn.bgsave();
        log.debug("【Redis】执行了bgsave命令");
    }

    // 建议使用bgsave
    public void save() {
        Jedis conn = redisUtils.getConnection();
        conn.save();
        log.debug("【Redis】执行了save命令");
    }

    public long dbsize() {
        Jedis conn = redisUtils.getConnection();
        return conn.dbSize();
    }

    public void flushdb() {
        Jedis conn = redisUtils.getConnection();
        conn.flushDB();
        log.debug("【Redis】清空了当前数据库");
    }

    public void flushall() {
        Jedis conn = redisUtils.getConnection();
        conn.flushAll();
        log.debug("【Redis】清空了所有数据库");
    }

    public void shutdown() {
        Jedis conn = redisUtils.getConnection();
        conn.shutdown();
        log.debug("【Redis】停止服务");
    }

    public long lastsave() {
        Jedis conn = redisUtils.getConnection();
        return conn.lastsave();
    }

    // ====================== Key 相关操作 ===============================

    // 随机获取一个存在的key
    public K randomkey() {
        return redisTemplate.randomKey();
    }

    // key重命名
    public void rename(K oldKey, K newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    // key重命名 不会覆盖
    public boolean renamenx(K oldKey, K newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    // 序列化指定key
    public byte[] dump(K k) {
        return redisTemplate.dump(k);
    }

    // 反序列化
    public void restore(K k, byte[] bytes, long timeout) {
        redisTemplate.restore(k, bytes, timeout, TimeUnit.SECONDS);
    }

    // 判断指定key是否存在
    public boolean exist(K k) {
        return redisTemplate.hasKey(k);
    }

    // 删除指定key
    public void del(K k) {
        redisTemplate.delete(k);
    }

    // 获取指定key剩余生命周期
    public long ttl(K k) {
        return redisTemplate.getExpire(k, TimeUnit.SECONDS);
    }

    // 获取指定key剩余生命周期(毫秒)
    public long pttl(K k) {
        return redisTemplate.getExpire(k, TimeUnit.MILLISECONDS);
    }

    // 获取指定key的值类型
    public DataType type(K k) {
        return redisTemplate.type(k);
    }

    // 为指定key设置生命周期
    public boolean expire(K k, long timeout) {
        return redisTemplate.expire(k, timeout, TimeUnit.SECONDS);
    }

    // 为指定key设置生命周期(毫秒)
    public boolean pexpire(K k, long mills) {
        return redisTemplate.expire(k, mills, TimeUnit.MILLISECONDS);
    }

    // 为指定key设置生命周期(时间戳)
    public boolean expireat(K k, Date date) {
        return redisTemplate.expireAt(k, date);
    }

    // 移除指定key的生命周期
    public boolean persist(K k) {
        return redisTemplate.persist(k);
    }

    // 查找所有符合给定模式 pattern 的 key
    public Set<K> keys(K k) {
        return redisTemplate.keys(k);
    }

    // ====================== 事务Transaction 相关操作 ===============================

    public void multi() {
        redisTemplate.multi();
    }

    public void discard() {
        redisTemplate.discard();
    }

    public void watch(K k) {
        redisTemplate.watch(k);
    }

    public void unwatch() {
        redisTemplate.unwatch();
    }

    public List<Object> exec() {
        return redisTemplate.exec();
    }

    // ====================== String 相关操作 ===========================

    public long strlen(K k) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.size(k);
    }

    public void set(K k, V v) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        valueOper.set(k, v);
    }

    public boolean setnx(K k, V v) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.setIfAbsent(k, v);
    }

    public void setex(K k, V v, long timeout) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        valueOper.set(k, v, timeout, TimeUnit.SECONDS);
    }

    public void psetex(K k, V v, long timeout) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        valueOper.set(k, v, timeout, TimeUnit.MILLISECONDS);
    }

    public V getset(K k, V v) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.getAndSet(k, v);
    }

    public V get(K k) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.get(k);
    }

    public String getrange(K k, long begin, long end) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.get(k, begin, end);
    }

    // 追加值到末尾 返回追加后长度
    public int append(K k, V v) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.append(k, v.toString());
    }

    // 递增值
    public long incr(K k) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.increment(k, 1);
    }

    public long incrby(K k, long delta) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.increment(k, delta);
    }

    public double incrbyfloat(K k, double delta) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.increment(k, delta);
    }

    // 递减值
    public long decr(K k) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.increment(k, -1);
    }

    public long decrby(K k, long delta) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.increment(k, delta);
    }

    public double decrbyfloat(K k, double delta) {
        ValueOperations<K, V> valueOper = redisTemplate.opsForValue();
        return valueOper.increment(k, delta);
    }

    // ====================== 哈希表 相关操作 ===============================

    public void hdel(K k, Object... args) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        hashOper.delete(k, args);
    }

    public boolean hexist(K k, String field) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.hasKey(k, field);
    }

    public V hget(K k, String field) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.get(k, field);
    }

    public Map<String, V> hgetall(K k) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.entries(k);
    }

    public List<V> hvals(K k) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.values(k);
    }

    public long hincrby(K k, String field, long delta) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.increment(k, field, delta);
    }

    public double hincrbyfloat(K k, String field, double delta) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.increment(k, field, delta);
    }

    public Set<String> hkeys(K k) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.keys(k);
    }

    public long hlen(K k) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.size(k);
    }

    public List<V> hmget(K k, Collection<String> fields) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.multiGet(k, fields);
    }

    public void hmset(K k, Map<? extends String, ? extends V> pairs) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        hashOper.putAll(k, pairs);
    }

    public void hset(K k, String field, V v) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        hashOper.put(k, field, v);
    }

    public boolean hsetnx(K k, String field, V v) {
        HashOperations<K, String, V> hashOper = redisTemplate.opsForHash();
        return hashOper.putIfAbsent(k, field, v);
    }

    // ====================== 列表 相关操作 ===============================

    public V lindex(K k, long index) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.index(k, index);
    }

    public long llen(K k) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.size(k);
    }

    public V lpop(K k) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.leftPop(k);
    }

    public V rpop(K k) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.rightPop(k);
    }

    public long lpush(K k, V v) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.leftPush(k, v);
    }

    public long lpushx(K k, V v) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.leftPushIfPresent(k, v);
    }

    public long rpush(K k, V v) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.rightPush(k, v);
    }

    public long rpushx(K k, V v) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.rightPushIfPresent(k, v);
    }

    public List<V> lrange(K k, long start, long end) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.range(k, start, end);
    }

    public void lset(K k, long pos, V v) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        listOper.set(k, pos, v);
    }

    // 返回值是被移除的元素数量
    public long lrem(K k, long count, V v) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.remove(k, count, v);
    }

    // 截取 包括end
    public void ltrim(K k, long start, long end) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        listOper.trim(k, start, end);
    }

    public void linsert(K k, V pivot, V v) {

    }

    public V rpoplpush(K originKey, K destKey) {
        ListOperations<K, V> listOper = redisTemplate.opsForList();
        return listOper.rightPopAndLeftPush(originKey, destKey);
    }

    // ======================  普通集合 相关操作 ===============================

    public long sadd(K k, V... v) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.add(k, v);
    }

    public long scard(K k) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.size(k);
    }

    public boolean sismember(K k, V v) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.isMember(k, v);
    }

    public Set<V> smembers(K k) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.members(k);
    }

    public boolean smove(K k, V v, K destKey) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.move(k, v, destKey);
    }

    // 随机获取一个元素并移除
    public V spop(K k) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.pop(k);
    }

    // 随机获取一个元素但不移除
    public V srandmember(K k) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.randomMember(k);
    }

    public long srem(K k, Object... objs) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.remove(k, objs);
    }

    // 差集
    public Set<V> sdiff(K k, K... ks) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.difference(k, Arrays.asList(ks));
    }

    public long sdiffstore(K k, K destKey, K... ks) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.differenceAndStore(k, Arrays.asList(ks), destKey);//返回结果集元素数量
    }

    // 交集
    public Set<V> sinter(K k, K... ks) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.intersect(k, Arrays.asList(ks));
    }

    public long sinterstore(K k, K destKey, K... ks) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.intersectAndStore(k, Arrays.asList(ks), destKey);//返回结果集元素数量
    }

    // 并集
    public Set<V> sunion(K k, K... ks) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.union(k, Arrays.asList(ks));
    }

    public long sunionstore(K k, K destKey, K... ks) {
        SetOperations<K, V> setOper = redisTemplate.opsForSet();
        return setOper.unionAndStore(k, Arrays.asList(ks), destKey);//返回结果集元素数量
    }

    // ====================== 有序集合 相关操作 ===============================

    // 获取指定值的排名（从小到大）
    public long zrank(K k, V v) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.rank(k, v);
    }

    // 获取指定值的排名（从大到小）
    public long zrevrank(K k, V v) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.reverseRank(k, v);
    }

    // 有序集合删除值
    public long zrem(K k, Object... objs) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.remove(k, objs);
    }

    public long zremrangebyrank(K k, long start, long stop) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.removeRange(k, start, stop);
    }

    public long zremrangebyscore(K k, long min, long max) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.removeRangeByScore(k, min, max);
    }

    // 有序集合获取指定key的score 值在 min 和 max 之间的值数量
    public long zcount(K k, double min, double max) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.count(k, min, max);
    }

    // 有序集合获取指定key的值的数量
    public long zcard(K k) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.size(k);
    }

    // 有序集合新增
    public void zadd(K k, V v, int score) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        zsetOper.add(k, v, score);
    }

    // 有序集合获取指定值的权值
    public Double zscore(K k, V v) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        return zsetOper.score(k, v);
    }

    // 有序集合改变指定值的权值（在原来基础上增加或减少）
    public void zincrby(K k, V v, int delta) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();
        zsetOper.incrementScore(k, v, delta);
    }

    // 有序集合获取指定区间的所有值（可选按权值排序）
    public Set<V> zrange(K k, long begin, long end, boolean isByScore) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();

        if (isByScore) return zsetOper.rangeByScore(k, begin, end);

        return zsetOper.range(k, begin, end);
    }

    // 有序集合获取指定区间的所有值（逆序）（可选按权值排序）
    public Set<V> zrevrange(K k, long begin, long end, boolean isByScore) {
        ZSetOperations<K, V> zsetOper = redisTemplate.opsForZSet();

        if (isByScore) return zsetOper.reverseRangeByScore(k, begin, end);

        return zsetOper.reverseRange(k, begin, end);
    }
}

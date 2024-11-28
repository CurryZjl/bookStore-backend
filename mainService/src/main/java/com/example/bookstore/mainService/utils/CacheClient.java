package com.example.bookstore.mainService.utils;

import cn.hutool.core.util.StrUtil;
import com.example.bookstore.mainService.constants.RedisConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public boolean isConnected(){
        try{
            if(stringRedisTemplate.getConnectionFactory() != null){
                RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection();
                return !connection.isClosed();
            }
        }catch (Exception e){
            log.warn(e.getMessage());
            return false;
        }
        return false;
    }

    public Boolean safeDelete(String key){
        try{
            boolean connected = isConnected();
            if(!connected) return null;
            return stringRedisTemplate.delete(key);
        }catch (Exception e){
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     *
     * @param prefix 要删除的记录的key的前缀
     * @return 成功删除的数据的数量
     */
    public Long safeDeleteByKeyPrefix(String prefix){
        try{
            boolean connected = isConnected();
            if(!connected) return null;

            Set<String> keys = stringRedisTemplate.keys(prefix);
            if(keys != null){
                return stringRedisTemplate.delete(keys);
            }
            else {
                return 0L;
            }
        }catch (Exception e){
            log.warn(e.getMessage());
            return null;
        }
    }

    public void setRedis(String key, Object value, Long time, TimeUnit unit){
        try{
            stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), time, unit);
        }catch (Exception e){
            log.warn(e.getMessage());
        }
    }

    public <R, ID> Optional<R> queryRedis(String keyPrefix, ID id, Class<R> type) {
        try{
            boolean connected = isConnected();
            String key = keyPrefix + id;
            // 1.从redis查询缓存
            String json = null;
            if (connected) {
                try {
                    json = stringRedisTemplate.opsForValue().get(key);
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
            else{
                log.info("Redis未连接");
            }
            // 2.判断是否存在
            if (StrUtil.isNotBlank(json)) {
                // 3.存在，直接返回
                log.info("后续读写，成功从Redis缓存中得到数据：");
                return Optional.of(objectMapper.readValue(json, type));
            }
            return Optional.empty();
        }catch (Exception e){
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }

}

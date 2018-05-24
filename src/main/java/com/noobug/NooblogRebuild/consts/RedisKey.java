package com.noobug.NooblogRebuild.consts;

/**
 * Redis生成key用
 *
 * @author noobug.com
 */
public class RedisKey {
    public static final String USER_REG_LOCK = "UserRegLock:::%s";
    public static final String USER = "Users";
    public static final String BOOK = "Books";
    public static final String BOOK_CHILD = "%s:%s";

    public static String of(String key, Object... args) {
        return String.format(key, args);
    }
}

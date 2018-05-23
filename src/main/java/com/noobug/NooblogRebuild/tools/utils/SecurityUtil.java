package com.noobug.NooblogRebuild.tools.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

/**
 * 安全相关工具类（包括加密和鉴权相关）
 *
 * @author noobug.com
 */
@Component
public class SecurityUtil {

    /**
     * MD5加密(大写）
     *
     * @param s 原字符串
     * @return 加密后字符串
     */
    public String md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }
}

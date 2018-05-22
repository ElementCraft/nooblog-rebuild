package com.noobug.NooblogRebuild.consts;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface UserConst {

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "USER";

    /**
     * 性别枚举
     */
    interface Sex {
        /**
         * 未知
         */
        int UNKNOWN = 0;

        /**
         * 男性
         */
        int MALE = 1;

        /**
         * 女性
         */
        int FEMALE = 2;

        /**
         * 保密
         */
        int SECRET = 3;

        List<Integer> ALL = Stream.of(UNKNOWN, FEMALE, MALE, SECRET)
                .collect(Collectors.toList());
    }

    interface Limit {
        int LEN_ACCOUNT_MIN = 5;
        int LEN_ACCOUNT_MAX = 18;
        int LEN_NICKNAME_MIN = 1;
        int LEN_NICKNAME_MAX = 18;
        int LEN_PASSWORD_MIN = 5;
        int LEN_PASSWORD_MAX = 18;
        int LEN_SIGNATURE_MAX = 64;
        int LEN_COLUMN_TITLE_MAX = 12;
    }
}

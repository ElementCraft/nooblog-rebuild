package com.noobug.NooblogRebuild.consts;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ArticleConst {

    interface Like {
        /**
         * 点赞
         */
        int GOOD = 1;

        /**
         * 差评
         */
        int BAD = 2;
    }

    /**
     * 文章状态
     */
    interface Status {
        /**
         * 正常
         */
        int NORMAL = 1;

        /**
         * 封禁
         */
        int BANNED = 2;

        /**
         * 删除
         */
        int DELETED = 3;
    }

    /**
     * 文章类型
     */
    interface Type {
        /**
         * 原创
         */
        int OWN = 1;

        /**
         * 转载
         */
        int REPRINT = 2;

        /**
         * 翻译
         */
        int TRANSLATE = 3;

        List<Integer> ALL = Stream.of(OWN, REPRINT, TRANSLATE)
                .collect(Collectors.toList());
    }

    interface Limit {
        int LEN_TITLE_MIN = 1;
        int LEN_TITLE_MAX = 128;
        int LEN_LABEL_MAX = 128;
        int LEN_REPRINT_URL_MAX = 128;
        int LEN_TRANSLATE_URL_MAX = 128;
    }
}

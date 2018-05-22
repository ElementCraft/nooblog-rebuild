package com.noobug.NooblogRebuild.consts.error;

import com.noobug.NooblogRebuild.tools.entity.ErrorCode;

/**
 * Article接口相关错误枚举
 *
 * @author noobug.com
 */
public interface ArticleError {

    ErrorCode NON_EXIST_ID = ErrorCode.of(-1, "不存在该文章");
    ErrorCode ALREADY_DELETED = ErrorCode.of(-2, "文章已删除");

    /**
     * 新增文章相关错误
     */
    interface Add {
        ErrorCode TITLE_LENGTH = ErrorCode.of(1, "标题长度不符合限制");
        ErrorCode NOT_EXIST_TYPEFLAG = ErrorCode.of(2, "无效的文章类型");
        ErrorCode REPRINT_NO_URL = ErrorCode.of(3, "转载文章需注明详细出处");
        ErrorCode TRANSLATE_NO_URL = ErrorCode.of(4, "翻译文章需注明详细出处");
        ErrorCode LABEL_LENGTH = ErrorCode.of(5, "标签长度超出限制");
        ErrorCode REPRINT_URL_LENGTH = ErrorCode.of(6, "转载出处长度超出限制");
        ErrorCode TRANSLATE_URL_LENGTH = ErrorCode.of(7, "翻译出处长度超出限制");
        ErrorCode NOT_EXIST_COLUMN = ErrorCode.of(8, "无效的栏目");
    }

}

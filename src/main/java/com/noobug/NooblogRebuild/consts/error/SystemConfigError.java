package com.noobug.NooblogRebuild.consts.error;

import com.noobug.NooblogRebuild.tools.entity.ErrorCode;

/**
 * SystemConfig数据库配置项接口相关错误枚举
 *
 * @author noobug.com
 */
public interface SystemConfigError {

    ErrorCode NON_EXIST_ID = ErrorCode.of(-1, "不存在的配置项ID");

    ErrorCode NON_EXIST_KEY = ErrorCode.of(1, "不存在该Key对应的配置项");
    ErrorCode EXIST_KEY = ErrorCode.of(2, "重复的Key");
    ErrorCode KEY_LENGTH = ErrorCode.of(3, "Key长度不符合限制");
    ErrorCode KEY_CONTAIN_SPACE = ErrorCode.of(4, "Key不能包含空格");
    ErrorCode KEY_CONTAIN_CHINESE = ErrorCode.of(5, "Key不能包含中文");
    ErrorCode DATA_LENGTH = ErrorCode.of(6, "配置值长度不符合限制");

}

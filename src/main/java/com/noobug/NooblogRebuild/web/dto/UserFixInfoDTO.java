package com.noobug.NooblogRebuild.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 用户修改个人资料DTO
 *
 * @author noobug.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFixInfoDTO {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     *
     * @see com.noobug.NooblogRebuild.consts.UserConst.Sex
     */
    private Integer sex;

    /**
     * 签名
     */
    private String signature;

    /**
     * 头像路径
     */
    private String iconPath;

    /**
     * 是否公开博客
     */
    private Boolean isPublic;
}

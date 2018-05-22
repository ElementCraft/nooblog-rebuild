package com.noobug.NooblogRebuild.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * 用户个人资料DTO
 *
 * @author noobug.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDTO {

    private Long id;

    private String account;

    private String nickName;

    private Integer sex;

    private String email;

    private String signature;

    private Integer score;

    private String iconPath;

    private Boolean authenticated;

    private Boolean banned;

    private Boolean isPublic;

    private ZonedDateTime gmtCreate;
}

package com.noobug.NooblogRebuild.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 用户注册DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegDTO {

    private String account;

    private String nickName;

    private String password;

    private String email;

    private String phone;
}

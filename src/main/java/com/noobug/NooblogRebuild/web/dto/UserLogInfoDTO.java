package com.noobug.NooblogRebuild.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * 用户登录日志DTO
 *
 * @author noobug.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLogInfoDTO {

    private Long id;

    private UserInfoDTO user;

    /**
     * 登录IP
     */
    private String logIp;

    /**
     * 登录时间
     */
    private ZonedDateTime gmtCreate;
}

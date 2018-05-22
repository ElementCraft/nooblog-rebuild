package com.noobug.NooblogRebuild.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * 用户栏目信息DTO
 *
 * @author noobug.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserColumnInfoDTO {

    private Long id;

    private UserInfoDTO user;

    /**
     * 父级栏目ID
     */
    private Long parentId;

    /**
     * 栏目名称
     */
    private String title;

    /**
     * 排序
     */
    private Integer sortLevel;

    /**
     * 是否默认栏目
     */
    private Boolean isDefault;

    private ZonedDateTime gmtCreate;
}

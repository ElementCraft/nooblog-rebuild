package com.noobug.NooblogRebuild.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 新增用户栏目DTO
 *
 * @author noobug.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddUserColumnDTO {

    /**
     * 父级栏目ID
     */
    private Long parentId;

    /**
     * 栏目名称
     */
    private String title;

}

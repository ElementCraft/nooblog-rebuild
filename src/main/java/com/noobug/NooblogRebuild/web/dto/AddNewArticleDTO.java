package com.noobug.NooblogRebuild.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 新增文章DTO
 *
 * @author noobug.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddNewArticleDTO {

    /**
     * 所属栏目
     */
    private Long columnId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章类型
     *
     * @see com.noobug.nooblog.consts.ArticleConst.Type
     */
    private Integer typeFlag;

    /**
     * 转载来源
     */
    private String reprintUrl;

    /**
     * 翻译来源
     */
    private String translateUrl;

    /**
     * 文章标签
     */
    private String label;

    /**
     * 是否私密
     */
    private Boolean isPrivate;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 是否markdown
     */
    private Boolean isMarkdown;

    /**
     * 文章内容
     */
    private String content;

}

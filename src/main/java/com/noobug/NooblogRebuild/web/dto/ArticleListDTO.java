package com.noobug.NooblogRebuild.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * 文章列表DTO
 *
 * @author noobug.com
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleListDTO {

    private Long id;

    /**
     * 所属栏目
     */
    private UserColumnInfoDTO userColumn;

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
     * 文章状态
     *
     * @see com.noobug.nooblog.consts.ArticleConst.Status
     */
    private Integer status;

    /**
     * 总点击数
     */
    private Long clickNumber;

    /**
     * 点赞数
     */
    private Integer goodNumber;

    /**
     * 反对数
     */
    private Integer badNumber;

    /**
     * 收藏数
     */
    private Integer favoriteNumber;

    /**
     * 评论数
     */
    private Integer commentNumber;

    /**
     * 是否markdown
     */
    private Boolean isMarkdown;

    /**
     * 最后编辑时间
     */
    private ZonedDateTime lastEditTime;

    private ZonedDateTime gmtCreate;
}

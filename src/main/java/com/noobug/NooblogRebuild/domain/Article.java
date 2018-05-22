package com.noobug.NooblogRebuild.domain;

import com.noobug.NooblogRebuild.tools.entity.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class Article extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private UserColumn userColumn;

    private String title;

    /**
     * 文章类型
     *
     * @see com.noobug.NooblogRebuild.consts.ArticleConst.Type
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

    /**
     * 正文
     */
    private String content;
}

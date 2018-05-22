package com.noobug.NooblogRebuild.domain;

import com.noobug.NooblogRebuild.tools.entity.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 评论表
 *
 * @author noobug.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 父级评论ID
     */
    private Long parentId;

    /**
     * 点赞数
     */
    private Integer goodNumber;

    /**
     * 反对数
     */
    private Integer badNumber;

    /**
     * 回复数
     */
    private Integer commentNumber;

    /**
     * 是否markdown
     */
    private Boolean isMarkdown;

    /**
     * 是否封禁
     */
    @Column(name = "is_ban")
    private Boolean banned;

    /**
     * 逻辑删除
     */
    @Column(name = "is_deleted")
    private Boolean deleted;

    /**
     * 正文
     */
    private String content;
}

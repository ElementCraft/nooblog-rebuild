package com.noobug.NooblogRebuild.domain;

import com.noobug.NooblogRebuild.tools.entity.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 评论点赞表
 *
 * @author noobug.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_like")
public class CommentLike extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment article;

    private Integer status;
}

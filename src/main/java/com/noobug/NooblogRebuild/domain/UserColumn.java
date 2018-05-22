package com.noobug.NooblogRebuild.domain;

import com.noobug.NooblogRebuild.tools.entity.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_column")
public class UserColumn extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

}

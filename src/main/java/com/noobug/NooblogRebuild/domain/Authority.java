package com.noobug.NooblogRebuild.domain;

import com.noobug.NooblogRebuild.tools.entity.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 接口权限表
 */
@Entity
@Table(name = "authority")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority extends BasePojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private String url;

    private Integer method;

    private Integer sortLevel;

    @Column(name = "is_deleted")
    private Boolean deleted;
}

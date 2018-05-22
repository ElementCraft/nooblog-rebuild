package com.noobug.NooblogRebuild.domain;

import com.noobug.NooblogRebuild.tools.entity.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 角色跟权限的关系表
 */
@Entity
@Table(name = "role_authority")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAuthority extends BasePojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roleId;

    private Long authorityId;
}

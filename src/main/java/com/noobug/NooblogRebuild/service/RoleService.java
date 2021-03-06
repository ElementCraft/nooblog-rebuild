package com.noobug.NooblogRebuild.service;

import com.noobug.NooblogRebuild.domain.Role;
import com.noobug.NooblogRebuild.domain.UserRole;
import com.noobug.NooblogRebuild.repository.RoleRepository;
import com.noobug.NooblogRebuild.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 小王子
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * 查询用户所有角色
     *
     * @param id 用户ID
     * @return 角色列表
     */
    public List<Role> findAllByUserId(Long id) {
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(id);

        return userRoles.stream()
                .map(userRole -> roleRepository.findOne(userRole.getRoleId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 通过CODE查找角色实体
     *
     * @param code code
     * @return
     */
    public Optional<Role> findOneByCode(String code) {
        return roleRepository.findOneByCodeAndDeleted(code, Boolean.FALSE);
    }

    /**
     * 保存用户角色关联
     *
     * @param userRole
     * @return
     */
    public UserRole saveUserRole(UserRole userRole) {

        return userRoleRepository.save(userRole);
    }
}

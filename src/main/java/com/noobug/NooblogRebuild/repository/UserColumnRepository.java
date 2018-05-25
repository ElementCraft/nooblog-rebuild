package com.noobug.NooblogRebuild.repository;

import com.noobug.NooblogRebuild.domain.UserColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserColumnRepository extends JpaRepository<UserColumn, Long> {

    UserColumn findOneByUserIdAndIsDefault(Long id, Boolean isDefault);

    UserColumn findOneByUserAccountAndIsDefault(String account, Boolean isDefault);

    List<UserColumn> findAllByUserAccountAndIsDefaultAndParentIdIsNullOrParentId(String account, Boolean isDefault, long parentId);

    List<UserColumn> findAllByParentIdAndIsDefault(Long id, Boolean isDefault);

    Long countByParentIdAndTitle(Long parentId, String title);

    Long countByUserIdAndTitle(Long id, String title);
}

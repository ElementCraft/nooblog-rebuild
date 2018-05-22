package com.noobug.NooblogRebuild.web.mapper;

import com.noobug.NooblogRebuild.domain.UserColumn;
import com.noobug.NooblogRebuild.web.dto.AddUserColumnDTO;
import com.noobug.NooblogRebuild.web.dto.UserColumnInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

/**
 * 用户栏目 相关 Mapper
 *
 * @author noobug.com
 */
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {UserMapper.class})
public interface UserColumnMapper extends EntityMapper<AddUserColumnDTO, UserColumn> {

    List<UserColumnInfoDTO> userColumns2UserColumnInfoDTOs(List<UserColumn> columns);

    UserColumnInfoDTO userColumn2UserColumnInfoDTO(UserColumn column);
}

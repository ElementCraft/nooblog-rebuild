package com.noobug.NooblogRebuild.web.mapper;

import com.noobug.NooblogRebuild.domain.User;
import com.noobug.NooblogRebuild.web.dto.UserInfoDTO;
import com.noobug.NooblogRebuild.web.dto.UserRegDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    User regDTO2User(UserRegDTO regDTO);

    UserInfoDTO user2InfoDTO(User user);


}

package com.ttsw.task.mapper.user;

import com.ttsw.task.domain.user.AppUserRegisterDTO;
import com.ttsw.task.domain.user.AppUserToSendDTO;
import com.ttsw.task.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    @Mapping(target = "role", defaultValue = "ROLE_UNCONFIRMED")
    @Mapping(target = "enabled", defaultValue = "false")
    AppUser mapToAppUser(AppUserRegisterDTO appUserRegisterDTO);

    AppUserToSendDTO mapToAppUserToSendDTO(AppUser appUser);

    List<AppUserToSendDTO> mapToAppUserSendDTOList(List<AppUser> appUsers);
}

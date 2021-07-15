package com.ttsw.task.mapper.user;

import com.ttsw.task.entity.AppUser;
import com.ttsw.task.domain.user.AppUserRegisterDTO;
import com.ttsw.task.domain.user.AppUserToSendDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUser mapToAppUser(AppUserRegisterDTO appUserRegisterDTO);
    AppUserToSendDTO mapToAppUserToSendDTO(AppUser appUser);
    List<AppUserToSendDTO> mapToAppUserSendDTOList(List<AppUser> appUsers);
}

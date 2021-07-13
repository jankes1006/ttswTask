package com.ttsw.task.mapper;

import com.ttsw.task.entity.AppUser;
import com.ttsw.task.domain.AppUserRegisterDTO;
import com.ttsw.task.domain.AppUserToSendDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUser mapToAppUser(AppUserRegisterDTO appUserRegisterDTO);
    AppUserToSendDTO mapToAppUserToSendDTO(AppUser appUser);
    List<AppUserToSendDTO> mapToAppUserSendDTOList(List<AppUser> appUsers);
}

package com.ttsw.task.mapper;

import com.ttsw.task.domain.AppUser;
import com.ttsw.task.domain.AppUserRegisterDTO;
import com.ttsw.task.domain.AppUserToSendDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppUserMapper {
    AppUserMapper INSTANCE = Mappers.getMapper( AppUserMapper.class );

    AppUserRegisterDTO mapToAppUserRegisterDTO(AppUser appUser);
    AppUser mapToAppUser(AppUserRegisterDTO appUserRegisterDTO);

    AppUserToSendDTO mapToAppUserToSendDTO(AppUser appUser);
}

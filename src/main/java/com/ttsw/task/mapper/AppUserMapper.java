package com.ttsw.task.mapper;

import com.ttsw.task.domain.AppUser;
import com.ttsw.task.domain.AppUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
public interface AppUserMapper {
    AppUserMapper INSTANCE = Mappers.getMapper(AppUserMapper.class);

    AppUserDTO appUserToAppUser(AppUser appUser);
}

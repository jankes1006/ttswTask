package com.ttsw.task.mapper.log;

import com.ttsw.task.domain.log.LogReservedDTO;
import com.ttsw.task.entity.log.LogReservedOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LogReservedMapper {

    @Mapping(source = "appUser.username", target = "usernameClient")
    @Mapping(source = "offer.owner.username", target = "usernameSeller")
    @Mapping(source = "offer.id", target = "offerId")
    @Mapping(source = "offer.title", target = "titleOffer")
    LogReservedDTO mapToLogReservedDTO(LogReservedOffer logReservedOffer);

    List<LogReservedDTO> mapToLogReservedDTOList(List<LogReservedOffer> logReservedOffers);
}

package ru.kpekepsalt.ruvik.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.kpekepsalt.ruvik.dto.MessageDto;
import ru.kpekepsalt.ruvik.model.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDto messageToDto(Message message);

}

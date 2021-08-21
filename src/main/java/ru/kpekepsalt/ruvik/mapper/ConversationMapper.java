package ru.kpekepsalt.ruvik.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.kpekepsalt.ruvik.dto.ConversationDto;
import ru.kpekepsalt.ruvik.model.Conversation;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    ConversationMapper INSTANCE = Mappers.getMapper(ConversationMapper.class);

    ConversationDto conversationToDto(Conversation conversation);

}

package ru.kpekepsalt.ruvik.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;
import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.User;

@Mapper(mappingControl = DeepClone.class, componentModel = "spring")
public interface CloneMapper {

    CloneMapper INSTANCE = Mappers.getMapper(CloneMapper.class);

    User cloneUser(User user);
    Conversation cloneConversation(Conversation conversation);

}

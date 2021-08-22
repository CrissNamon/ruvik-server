package ru.kpekepsalt.ruvik.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings(value = {
            @Mapping(source = "publicIdentityA", target = "publicIdentityKeyA"),
            @Mapping(source = "publicIdentityB", target = "publicIdentityKeyB")
    })
    User dtoToUser(UserDto userDto);
    UserDto userToDto(User user);

}

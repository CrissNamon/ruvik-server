package ru.kpekepsalt.ruvik.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpekepsalt.ruvik.utils.StringUtils;
import ru.kpekepsalt.ruvik.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kpekepsalt.ruvik.Urls.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authWrongToken() throws Exception
    {
        mockMvc.perform(
          get(API_PATH + GATE.END_POINT + GATE.AUTH + "/WRONG_TOKEN")
        )
        .andExpect(status().isNotFound());
    }

    @Test
    public void authEmptyToken() throws Exception
    {
        mockMvc.perform(
                get(API_PATH + GATE.END_POINT + GATE.AUTH + "/    ")
        )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    public void authWithoutAAuthorization() throws Exception
    {
        mockMvc.perform(
                get(API_PATH + GATE.END_POINT + GATE.AUTH)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void registerWithInvalidBody() throws Exception
    {
        UserDto userDto = new UserDto();
        userDto.setLogin("");
        String json = StringUtils.toJson(userDto);
        mockMvc.perform(
                post(API_PATH + GATE.END_POINT + GATE.REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(
                status().isBadRequest()
        );
    }

}

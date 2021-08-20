package ru.kpekepsalt.ruvik.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpekepsalt.ruvik.dto.SessionInitialInformationDto;
import ru.kpekepsalt.ruvik.utils.StringUtils;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kpekepsalt.ruvik.Urls.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ConversationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findUserUnauthorized() throws Exception
    {
        mockMvc.perform(
                get(API_PATH + CONVERSATION.END_POINT + CONVERSATION.INITIATE + "/login")
        )
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    public void findUserByEmptyLogin() throws Exception
    {
        mockMvc.perform(
                get(API_PATH + CONVERSATION.END_POINT + CONVERSATION.INITIATE + "/    ")
                .with(
                        user("user").password("password")
                )
        )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    public void createPendingSessionWithEmptyLogin() throws Exception
    {
        mockMvc.perform(
                post(API_PATH + CONVERSATION.END_POINT + CONVERSATION.INITIATE + "/    ")
                .with(
                        user("user").password("password")
                )
        )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    public void createPendingSessionWithInvalidSessionDto() throws Exception
    {
        SessionInitialInformationDto sessionInitialInformationDto = new SessionInitialInformationDto();
        sessionInitialInformationDto.setPublicIdentityKey("");
        sessionInitialInformationDto.setEncryptedSessionKey("");
        sessionInitialInformationDto.setOneTImeKey("");

        String json = StringUtils.toJson(sessionInitialInformationDto);

        mockMvc.perform(
                post(API_PATH + CONVERSATION.END_POINT + CONVERSATION.INITIATE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(
                        user("user").password("passwordd")
                )

        )
                .andExpect(
                        status().isBadRequest()
                );
    }

}

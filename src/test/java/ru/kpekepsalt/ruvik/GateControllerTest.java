package ru.kpekepsalt.ruvik;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpekepsalt.ruvik.Utils.StringUtils;
import ru.kpekepsalt.ruvik.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kpekepsalt.ruvik.Urls.API_PATH;
import static ru.kpekepsalt.ruvik.Urls.GATE.END_POINT;

@SpringBootTest
@AutoConfigureMockMvc
public class GateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authWrongToken() throws Exception
    {
        mockMvc.perform(
          get(API_PATH + END_POINT + "/auth-token/WRONG_TOKEN")
        )
        .andExpect(status().isNotFound());
    }

    @Test
    public void authWithoutAAuthorization() throws Exception
    {
        mockMvc.perform(
                get(API_PATH + END_POINT + "/auth")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void registerWithEmptyBody() throws Exception
    {
        UserDto userDto = new UserDto();
        String json = StringUtils.toJson(userDto);
        mockMvc.perform(
                post(API_PATH + END_POINT + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest());
    }

}

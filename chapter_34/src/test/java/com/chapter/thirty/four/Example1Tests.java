package com.chapter.thirty.four;

import com.chapter.thirty.four.security.WithCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class Example1Tests {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("When calling the /demo endpoint without authentication we should get 401 UNAUTHORIZED")
    @Test
    void testUnAuthenticatedDemoEndpointWithoutUser() throws Exception {
        mockMvc
                .perform(get("/demo"))
                .andExpect(status().isUnauthorized()); // Will fail if http.httpBasic() is not enabled
    }

    @Test
    @WithCustomUser(authorities = "write")
    void testAuthenticatedWithoutProperAuthDemoEndpoint() throws Exception {
        mockMvc
                .perform(get("/demo"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomUser(authorities = "read")
    void testAuthenticatedWithProperAuthDemoEndpoint() throws Exception {
        mockMvc
                .perform(get("/demo"))
                .andExpect(status().isOk());
    }

}

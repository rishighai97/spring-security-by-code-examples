package com.chapter.thirty.three;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class Example1Tests {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("When calling the /demo endpoint without authentication we should get 403 FORBIDDEN")
    @Test
    void testAuthenticatedDemoEndpointWithoutUser() throws Exception {
        mockMvc
                .perform(get("/demo"))
                .andExpect(status().isForbidden()); // Unauthorized if no authentication
    }

    @Test
    @WithMockUser(username = "mary")
    void testAuthenticatedWithoutProperAuthDemoEndpoint() throws Exception {
        mockMvc
                .perform(get("/demo"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "mary", authorities = "read")
    void testAuthenticatedWithProperAuthDemoEndpoint() throws Exception {
        mockMvc
                .perform(get("/demo"))
                .andExpect(status().isOk());
    }

}

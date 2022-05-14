package com.chapter.thirty.three;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class Example2Tests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("bill")
        // has write authority in project config
    void testAuthenticatedWithoutProperAuthDemoEndpoint() throws Exception {
        mockMvc
                .perform(get("/demo"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("john")
        // has read authority in project config
    void testAuthenticatedWithProperAuthDemoEndpoint() throws Exception {
        mockMvc
                .perform(get("/demo"))
                .andExpect(status().isOk());
    }

}

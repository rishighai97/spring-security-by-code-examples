package com.chapter.thirty.four;

import com.chapter.thirty.four.security.WithCustomUser;
import com.chapter.thirty.four.service.NameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class Example2Tests {

    @Autowired
    private NameService nameService;

    @Test
    void testUnAuthenticatedGetNameWithoutUser() throws Exception {
        assertThrows(AuthenticationCredentialsNotFoundException.class, ()->nameService.getName());
    }

    @Test
    @WithMockUser(authorities = "write")
    void testAuthenticatedGetNameWithoutProperAuth() {
        assertThrows(AccessDeniedException.class, ()->nameService.getName());
    }

    @Test
    @WithMockUser(authorities = "read")
    void testAuthenticatedGetNameWithProperAuth() {
        assertEquals("Mary", nameService.getName());
    }

}

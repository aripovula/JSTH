package com.jsthf;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {
	
    @Autowired
    private MockMvc mockMvc;
    
    @Test (expected = java.lang.AssertionError.class)
    public void testWhetherUnauthorizedUserCanOpenRestrictedPage() throws Exception {        
    	mockMvc.perform(get("/flashcards"))
	        .andExpect(content().string(org.hamcrest.Matchers.containsString("Flashcards:")))
            .andExpect(model().attributeExists("heading"))
            .andExpect(view().name("itemindex"));
    }
    	    
    @Test
    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
    public void testWhetherAuthorizedUserCanOpenRestrictedPage() throws Exception {
    	mockMvc.perform(get("/welcome"))
            .andExpect(status().isOk())
	        .andExpect(content().string(org.hamcrest.Matchers.containsString("Welcome")))
            .andExpect(view().name("welcome"));
    }

    @Test (expected = java.lang.AssertionError.class)
    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
    public void testWhetherAuthorizedUserCanPostWithIncorrectCSRFToken() throws Exception {
        mockMvc.perform(post("/addfc")
        .with(user("a@b.com").roles("USER"))
        .with(csrf().useInvalidToken()))		// invalid token

        .andExpect(view().name("redirect:/addfc"));
        
    }

    @Test
    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
    public void testWhetherAuthorizedUserCanPostWithValidCSRFToken() throws Exception {
        mockMvc.perform(post("/addfc")
        .with(user("a@b.com").roles("USER"))
        .with(csrf()))

        .andExpect(view().name("redirect:/addfc"));
        
    }

}

package com.jsthf.cardstest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
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
import org.springframework.web.context.WebApplicationContext;

	@RunWith(SpringRunner.class)
	@SpringBootTest
	@AutoConfigureMockMvc
	public class FCExItemControllerTest {

		@Autowired
	    WebApplicationContext context;
		
	    @Autowired
	    private MockMvc mockMvc;


	    @Test
	    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
	    public void testInitCreationViews() throws Exception {
	        
	    	mockMvc.perform(get("/flashcards"))
	            .andExpect(status().isOk())
		        .andExpect(content().string(org.hamcrest.Matchers.containsString("Flashcards:")))
	            .andExpect(model().attributeExists("fcexitems"))
	            .andExpect(model().attributeExists("heading"))
	            .andExpect(view().name("itemindex"));
	    }
	    
	    @Test
	    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
	    public void testInitCreationExamplesView() throws Exception {
	        
	    	mockMvc.perform(get("/examples"))
	            .andExpect(status().isOk())
		        .andExpect(content().string(org.hamcrest.Matchers.containsString("Example code cards:")))
	            .andExpect(model().attributeExists("fcexitems"))
		        .andExpect(model().attributeExists("heading"))
	            .andExpect(view().name("itemindex"));
	    }

	    @Test
	    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
	    public void testInitCreationViewWithKeyword() throws Exception {
	        
	    	mockMvc.perform(get("/search?qword=collection"))
	            .andExpect(status().isOk())
		        .andExpect(content().string(org.hamcrest.Matchers.containsString("All cards containing the keyword: collection")))
	            .andExpect(model().attributeExists("fcexitems"))
		        .andExpect(model().attributeExists("heading"))
	            .andExpect(view().name("itemindex"));
	    }

	    @Test
	    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
	    public void testInitCreationViewSelectedFramework() throws Exception {
	        
	    	mockMvc.perform(get("/fcexitems/4/framework"))
	            .andExpect(status().isOk())
		        .andExpect(content().string(org.hamcrest.Matchers.containsString("All cards for framework: Hibernate")))
	            .andExpect(model().attributeExists("fcexitems"))
		        .andExpect(model().attributeExists("heading"))
	            .andExpect(view().name("itemindex"));
	    }


	    @Test
	    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
	    public void testInitAddFCGet() throws Exception {
	        mockMvc.perform(get("/addfc"))
	            .andExpect(status().isOk())
	            .andExpect(model().attributeExists("fcexitem"))
	            .andExpect(model().attributeExists("submit"))
	            .andExpect(view().name("itemform"));
	    }
	    
	    @Test
	    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
	    public void testInitAddFCPost_SinceNoParamsArePassedToFormFlashWarningShouldAppearAndItShouldRedirectBackToAddFC() throws Exception {
	        mockMvc.perform(post("/addfc")
	        .with(user("a@b.com").roles("USER"))
	        .with(csrf()))

	        .andExpect(model().errorCount(0))
    			.andExpect(model().hasNoErrors())
	        .andExpect(status().is3xxRedirection())
	        .andExpect(flash().attributeCount(2))
	        .andExpect(view().name("redirect:/addfc"));
	        
	    }

	    @Test
	    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
	    public void testInitUseCardsCreationForm() throws Exception {
	        mockMvc.perform(get("/usecards"))
	            .andExpect(status().isOk())
	            .andExpect(model().attributeExists("cardsjsn"))
	            .andExpect(view().name("itemuse"));
	    }

	}

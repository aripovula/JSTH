package com.jsthf.usertest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.jsthf.service.UserAuthService;

		@RunWith(SpringRunner.class)
		@SpringBootTest
		@AutoConfigureMockMvc
		//@Transactional
		public class UserControllerTest {

		    @Autowired
		    private MockMvc mockMvc;

		    @MockBean
		    private UserAuthService UAService;

		    @Test
		    public void testInitCreationForm() throws Exception {
		        mockMvc.perform(get("/registration"))
		        .andExpect(model().errorCount(0))
        			.andExpect(model().hasNoErrors())
		        .andExpect(status().isOk())
		            .andExpect(model().attributeExists("user"))
		            .andExpect(view().name("registration"));
		    }
		    
		    @Test
		    public void testInitLoginCreationForm() throws Exception {
		        mockMvc.perform(get("/login"))
		            .andExpect(status().isOk())
		            .andExpect(view().name("login"));
		    }

		    @Test
		    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
		    public void testCardSelectionPost_SinceNoParamsArePassedToFormFlashWarningShouldAppearAndItShouldRedirectBackToSelection() throws Exception {
		        mockMvc.perform(post("/customload")
			        .with(user("a@b.com").roles("USER"))
			        .with(csrf())
		        )
		        		.andExpect(model().errorCount(0))
		        		.andExpect(model().hasNoErrors())
		            .andExpect(status().is3xxRedirection())
		            .andExpect(flash().attributeCount(2))
		            .andExpect(view().name("redirect:/customload"));
		    }

		    @Test
		    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
		    public void testCardStatusChangePost_SinceNoParamsArePassedToFormItShouldRedirectBackToSelection() throws Exception {
		        mockMvc.perform(post("/frequency")
			        .with(user("a@b.com").roles("USER"))
			        .with(csrf())
		        )
		        		.andExpect(model().errorCount(0))
		        		.andExpect(model().hasNoErrors())
		            .andExpect(status().is3xxRedirection())
		            .andExpect(view().name("redirect:/usecards"));
		    }


}

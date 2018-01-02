package com.jsthf.riskstest;

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

import com.jsthf.model.Risk;
import com.jsthf.service.RiskService;

//@WebMvcTest(RiskController.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RiskControllerTest {


			    @Autowired
			    private MockMvc mockMvc;

			    @MockBean
			    private RiskService riskService;

			    private Risk risk;

			    @Test
			    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
			    public void testInitCreationForm() throws Exception {
			        mockMvc.perform(get("/risks"))
			            .andExpect(status().isOk())
			            .andExpect(model().attributeExists("currentuser"))
			            .andExpect(model().attributeExists("risks"))
			            .andExpect(view().name("riskindex"));
			    }
			    

			    @Test
			    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
			    public void testInitAddRiskGet() throws Exception {
			        mockMvc.perform(get("/risks/add"))
			            .andExpect(status().isOk())
			            .andExpect(model().attributeExists("risk"))
			            .andExpect(model().attributeExists("currentuser"))
			            .andExpect(model().attributeExists("submit"))
			            .andExpect(view().name("riskform"));
			    }
			    
			    @Test
			    @WithMockUser(username = "a@b.com", roles = "user")//mock security.
			    public void testInitAddRiskPost_SinceNoParamsArePassedToFormFlashWarningShouldAppearAndItShouldRedirectBackToAddRisk() throws Exception {
			        mockMvc.perform(post("/risks")
			        .with(user("a@b.com").roles("USER"))
			        .with(csrf()))

			        .andExpect(model().errorCount(0))
		    			.andExpect(model().hasNoErrors())
			        .andExpect(status().is3xxRedirection())
			        .andExpect(flash().attributeCount(2))
			        .andExpect(view().name("redirect:/risks/add"));   
			    }
			}


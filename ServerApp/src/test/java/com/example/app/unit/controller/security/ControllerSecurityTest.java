package com.example.app.unit.controller.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.RequestPostProcessor.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class ControllerSecurityTest {

	@Autowired
    private WebApplicationContext wac;
 
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
 
    private MockMvc mockMvc;
 
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
          .addFilter(springSecurityFilterChain).build();
    }
    
    private String obtainAccessToken(String username, String password) throws Exception {
    	  
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "oauth2-jwt-client");
        params.add("username", username);
        params.add("password", password);
     
        ResultActions result 
          = mockMvc.perform(post("/oauth/token")
            .params(params)
            .accept("application/json;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));
     
        String resultString = result.andReturn().getResponse().getContentAsString();
     
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
    
    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
    	mockMvc.perform(get("/balance")
    		      .param("id", "1"))
    		      .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
        String accessToken = obtainAccessToken("user1", "pass");
        mockMvc.perform(get("/employee")
          .header("Authorization", "Bearer " + accessToken)
          .param("email", "jim@yahoo.com"))
          .andExpect(status().isForbidden());
    }
    
    
}

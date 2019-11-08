package com.example.app.model.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CustomGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	private String authority;
	
	public CustomGrantedAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return this.authority;
	}

}

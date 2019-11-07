package com.example.app.serviceImpl;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.app.model.dto.CustomGrantedAuthority;
import com.example.app.model.dto.CustomUserDetails;
import com.example.app.model.security.User;
import com.example.app.model.security.UserAuthority;
import com.example.app.repository.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository  repository;
	
	@Autowired
	private CustomUserDetails userDetails;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user;
	
		try {
			user = repository.findByUserName(username).get();
			userDetails.setUsername(user.getUserName());
			userDetails.setPassword(user.getPassword());
			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			
			for (UserAuthority authority : user.getUserAuthorities()) {
				authorities.add(new CustomGrantedAuthority(authority.getAuthority().getName()));
			}
			userDetails.setGrantedAuthorities(authorities);
			return userDetails;
		} catch(NoSuchElementException e) {
			throw new UsernameNotFoundException(username);
		}
	}

}

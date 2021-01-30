package com.example.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.User;
import com.example.repository.UserRepository;

@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
			throw new UsernameNotFoundException(email + " is not found.");
		}

    return new UserDetailsImpl(user);
  }
}
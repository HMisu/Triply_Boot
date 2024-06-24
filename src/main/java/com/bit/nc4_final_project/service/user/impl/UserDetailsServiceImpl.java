package com.bit.nc4_final_project.service.user.impl;

import com.bit.nc4_final_project.dto.user.UserDTO;
import com.bit.nc4_final_project.entity.CustomUserDetails;
import com.bit.nc4_final_project.entity.User;
import com.bit.nc4_final_project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUserId(username);

        if (userOptional.isEmpty()) {
            return null;
        } else if (!userOptional.get().isActive()) {
            throw new RuntimeException("inActive");
        }
        
        UserDTO signinUser = userOptional.get().toDTO();
        signinUser.setLastLoginDate(LocalDateTime.now().toString());

        User user = signinUser.toEntity();
        userRepository.save(user);

        userRepository.flush();

        return CustomUserDetails.builder()
                .user(user)
                .build();
    }
}

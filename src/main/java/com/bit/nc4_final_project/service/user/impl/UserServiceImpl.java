package com.bit.nc4_final_project.service.user.impl;

import ch.qos.logback.classic.Logger;
import com.bit.nc4_final_project.dto.user.UserDTO;
import com.bit.nc4_final_project.entity.User;
import com.bit.nc4_final_project.entity.UserTag;
import com.bit.nc4_final_project.jwt.JwtTokenProvider;
import com.bit.nc4_final_project.repository.user.UserRepository;
import com.bit.nc4_final_project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.bit.nc4_final_project.entity.UserTag;


import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    // private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO signup(UserDTO userDTO) {
        System.out.println(userDTO);
        User user = userRepository.save(userDTO.toEntity());

        List<String> tags = userDTO.getTags();

        tags.forEach(tagContent -> {
            UserTag userTag = new UserTag();
            userTag.setContent(tagContent);
            user.addUserTag(userTag);
        });

        return user.toDTO();
    }

    @Override
    public UserDTO signin(UserDTO userDTO) {
        return null;
        Optional<User> signinUser = userRepository.findById(userDTO.getId());

        if (signinUser.isEmpty()) {
            throw new RuntimeException("not exist userid");
        }

        if (!passwordEncoder.matches(userDTO.getPw(), signinUser.get().getPw())) {
            throw new RuntimeException("wrong pw");
        }

        UserDTO signinDTO = signinUser.get().toDTO();

        signinDTO.setLastLoginDate(LocalDateTime.now().toString());
        signinDTO.setToken(jwtTokenProvider.create(signinUser.get()));

        userRepository.save(signinDTO.toEntity());
        userRepository.flush();

        return signinDTO;
    }

    @Override
    public long idCheck(UserDTO userDTO) {
        return userRepository.countById(userDTO.getId());
    }

   @Override
   public UserDTO join(UserDTO userDTO) {
        return null;
    }

   @Override
    public UserDTO getUserDTO(Integer userSeq) {
        Optional<User> user = userRepository.findById(userSeq);
        if (user.isEmpty()) {
            log.warn("Travel with ID {} not found", userSeq);
            return null;
        }
        return user.get().toDTO();
    }
}

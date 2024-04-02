package com.bit.nc4_final_project.service.user;

import com.bit.nc4_final_project.dto.user.UserDTO;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    UserDTO join(UserDTO userDTO);

    UserDTO getUserDTO(Integer userSeq);

    UserDTO signup(UserDTO userDTO);

    UserDTO signin(UserDTO userDTO);

    boolean isIdAvailable(String id);

    boolean isNicknameAvailable(String nickname);

    void deleteProfileImage(String username);

    String uploadProfileImage(MultipartFile file, String id);

    String updateProfileImage(MultipartFile file, String id);
}

package com.bit.nc4_final_project.controller;

import com.bit.nc4_final_project.document.user.AreaCode;
import com.bit.nc4_final_project.dto.ResponseDTO;
import com.bit.nc4_final_project.dto.user.UserDTO;
import com.bit.nc4_final_project.jwt.JwtTokenProvider;
import com.bit.nc4_final_project.repository.user.area.UserAreaCodeRepository;
import com.bit.nc4_final_project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserAreaCodeRepository userAreaCodeRepository;

    @GetMapping("/areas")
    public List<AreaCode> getAreas() {
        return userAreaCodeRepository.findAll();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();
        System.out.println(userDTO);
        try {
            userDTO.setActive(true);
            userDTO.setLastLoginDate(LocalDateTime.now().toString());
            userDTO.setUserRegDate(LocalDateTime.now().toString());
            userDTO.setRole("ROLE_USER");
            userDTO.setUserPw(passwordEncoder.encode(userDTO.getUserPw()));

            UserDTO signupUserDTO = userService.signup(userDTO);

            signupUserDTO.setUserPw("");

            responseDTO.setItem(signupUserDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("id is required")) {
                responseDTO.setErrorCode(100);
                responseDTO.setErrorMessage(e.getMessage());
            } else if (e.getMessage().equalsIgnoreCase("password is required")) {
                responseDTO.setErrorCode(101);
                responseDTO.setErrorMessage(e.getMessage());
            } else if (e.getMessage().equalsIgnoreCase("name is required")) {
                responseDTO.setErrorCode(102);
                responseDTO.setErrorMessage(e.getMessage());
            } else {
                responseDTO.setErrorCode(103);
                responseDTO.setErrorMessage(e.getMessage());
            }
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();
        try {
            UserDTO signinUserDTO = userService.signin(userDTO);
//            System.out.println(signinUserDTO.getToken());
//            signinUserDTO.setPw("");
//            log.info("===========token: {} ==========", signinUserDTO.getToken());
            responseDTO.setItem(signinUserDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("not exist userid")) {
                responseDTO.setErrorCode(200);
                responseDTO.setErrorMessage(e.getMessage());
            } else if (e.getMessage().equalsIgnoreCase("wrong password")) {
                responseDTO.setErrorCode(201);
                responseDTO.setErrorMessage(e.getMessage());
            } else {
                responseDTO.setErrorCode(202);
                responseDTO.setErrorMessage(e.getMessage());
            }

            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/sign-out")
    public ResponseEntity<?> signout() {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();

        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(null);
            SecurityContextHolder.setContext(securityContext);

            Map<String, String> msgMap = new HashMap<>();

            msgMap.put("signoutMsg", "signout success");

            responseDTO.setItem(msgMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setErrorCode(300);
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/check-userid")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> checkuserid(@RequestParam("userid") String userid) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try {
            boolean available = userService.isUserIdAvailable(userid);
            Map<String, Object> response = new HashMap<>();
            response.put("available", available);


            responseDTO.setItem(response);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setErrorCode(102);
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> checkusername(@RequestParam("username") String username) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try {
            boolean available = userService.isUserNameAvailable(username);
            Map<String, Object> response = new HashMap<>();
            response.put("available", available);

            responseDTO.setItem(response);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setErrorCode(103);
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfileImage(@RequestParam(value = "file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // 사용자의 아이디를 가져옵니다.
        String profileImageUrl = userService.uploadProfileImage(file, userId);
        return ResponseEntity.ok(profileImageUrl);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProfileImage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        userService.deleteProfileImage(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfileImage(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        String profileImageUrl = userService.uploadProfileImage(file, userId);
        return ResponseEntity.ok(profileImageUrl);
    }

    @GetMapping("/modifyuser/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable("userId") String userId, @AuthenticationPrincipal UserDetails userDetails) {
        // 사용자 인증 및 권한 확인
        if (!userDetails.getUsername().equals(userId)) {
            // 권한 없음 처리
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO userDTO = userService.getUserInfo(userId);
        return ResponseEntity.ok(userDTO);
    }


    @PutMapping("/modifyuser/{userId}")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> modifyUser(
            @PathVariable("userId") String userid,
            @RequestBody UserDTO userDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();

        try {
            // 사용자 인증 및 권한 확인
            if (!userDetails.getUsername().equals(userid)) {
            }

            UserDTO updateUserDTO = userService.modifyUser(userid, userDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("user", updateUserDTO);

            responseDTO.setItem(response);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setErrorCode(104);
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
package com.bit.nc4_final_project.controller.community;

import com.bit.nc4_final_project.common.FileUtils;
import com.bit.nc4_final_project.dto.ResponseDTO;
import com.bit.nc4_final_project.dto.community.CommunityDTO;
import com.bit.nc4_final_project.dto.community.CommunitySubscriberDTO;
import com.bit.nc4_final_project.dto.community.CommunityTagDTO;
import com.bit.nc4_final_project.entity.CustomUserDetails;
import com.bit.nc4_final_project.service.community.CommunityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;
    private final FileUtils fileUtils;

    @PostMapping("/reg")
    public ResponseEntity<?> postBoard(@RequestPart("community") CommunityDTO communityDTO,
                                       @RequestPart("tags") List<CommunityTagDTO> communityTagDTOList,
                                       @RequestPart(value = "picture", required = false) MultipartFile picture,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<CommunityDTO> responseDTO = new ResponseDTO<>();

        try {
            communityDTO.setTags(communityTagDTOList);
            communityDTO.setUser(customUserDetails.getUser().toDTO());
            if (picture != null && !picture.isEmpty()) {
                String picturePath = fileUtils.saveFile(picture);
                communityDTO.setPicture(picturePath);
            }

            communityService.post(communityDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorCode(402);
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/community/{seq}")
    public ResponseEntity<?> getCommunity(@PathVariable("seq") Integer seq) {
        ResponseDTO<CommunityDTO> responseDTO = new ResponseDTO<>();

        try {
            CommunityDTO communityDTO = communityService.findBySeq(seq);

            responseDTO.setItem(communityDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            responseDTO.setErrorCode(404);
            responseDTO.setErrorMessage("Community not found with id: " + seq);
            responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorCode(500);
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifyBoard(@RequestPart("community") CommunityDTO communityDTO,
                                         @RequestPart("tags") List<CommunityTagDTO> communityTagDTOList,
                                         @RequestPart("picture") MultipartFile picture,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<CommunityDTO> responseDTO = new ResponseDTO<>();
        try {
            communityDTO.setTags(communityTagDTOList);

            if (picture != null && !picture.isEmpty()) {
                String picturePath = fileUtils.saveFile(picture);
                communityDTO.setPicture(picturePath);
            }

            CommunityDTO updatedCommunityDTO = communityService.modify(communityDTO, customUserDetails.getUser());

            responseDTO.setItem(updatedCommunityDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorCode(500);
            responseDTO.setErrorMessage("Failed to modify the community post: " + e.getMessage());
            responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @PostMapping("subscribe/{seq}")
    public ResponseEntity<?> subscribe(@PathVariable("seq") int seq,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<CommunitySubscriberDTO> responseDTO = new ResponseDTO<>();

        try {
            CommunitySubscriberDTO communitySubscriberDTO = communityService.subscribe(seq, customUserDetails.getUser());
            responseDTO.setItem(communitySubscriberDTO);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorCode(500);
            responseDTO.setErrorMessage("Failed to modify the community post: " + e.getMessage());
            responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @DeleteMapping("/subscribe/{seq}")
    public ResponseEntity<?> cancelSubscribe(@PathVariable("seq") int seq,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();

        try {
            communityService.cancelSubscribe(seq, customUserDetails.getUser());
            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("msg", "cancel success");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorCode(500);
            responseDTO.setErrorMessage("Failed to modify the community post: " + e.getMessage());
            responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}

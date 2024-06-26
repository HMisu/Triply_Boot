package com.bit.nc4_final_project.entity.community;

import com.bit.nc4_final_project.dto.community.CommunityDTO;
import com.bit.nc4_final_project.dto.community.CommunityTagDTO;
import com.bit.nc4_final_project.dto.user.UserDTO;
import com.bit.nc4_final_project.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "T_COMMUNITY")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_seq")
    private Integer seq;
    private String name;
    private int member;
    private LocalDateTime regDate;
    private String picture;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CommunityTag> communityTags;

    public void setCommunityTags(List<CommunityTag> communityTags) {
        this.communityTags = communityTags;
        communityTags.forEach(tag -> tag.setCommunity(this));
    }

    public CommunityDTO toDTO(UserDTO userDTO) {
        return CommunityDTO.builder()
                .seq(this.seq)
                .name(this.name)
                .member(this.member)
                .regDate(this.regDate.toString())
                .picture(this.picture)
                .description(this.description)
                .user(userDTO)
                .tags(this.communityTags != null ? this.communityTags.stream()
                        .map(tag -> new CommunityTagDTO(tag.getSeq(), tag.getContent()))
                        .collect(Collectors.toList()) : null)
                .build();
    }
}

package com.zylos.backend.features.projectgroup.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

import com.zylos.backend.features.projectgroup.ProjectGroup;

@Entity
@Table(name = "project_group_members")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectGroupMember {

    public ProjectGroupMember(ProjectGroup projectGroup, Long userId, ProjectGroupRole role) {
        this.projectGroup = projectGroup;
        this.userId = userId;
        this.role = role;
        this.joinedAt = LocalDateTime.now(); 
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @NotNull
    private ProjectGroup projectGroup;

    @NotNull
    private Long userId;

    @NotNull @Enumerated(EnumType.STRING)
    private ProjectGroupRole role;

    @NotNull 
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();
}
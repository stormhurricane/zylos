package com.zylos.backend.features.projectgroup;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.projectgroup.member.ProjectGroupMember;
import com.zylos.backend.features.projectgroup.member.ProjectGroupRole;
import com.zylos.backend.features.projectgroup.todo.ProjectGroupTodo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_groups")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(unique = true)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull
    private Long createdBy;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "projectGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectGroupMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "projectGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProjectGroupTodo> todos = new ArrayList<>();

    public void addMember(Long userId, ProjectGroupRole role) {
        ProjectGroupMember member = new ProjectGroupMember(this, userId, role);
        this.members.add(member);
    }
}
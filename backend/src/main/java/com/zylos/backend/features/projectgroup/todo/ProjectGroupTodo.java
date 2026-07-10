package com.zylos.backend.features.projectgroup.todo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

import com.zylos.backend.features.projectgroup.ProjectGroup;

@Entity
@Table(name = "project_group_todos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectGroupTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @NotNull
    private ProjectGroup projectGroup;

    @NotBlank
    private String title;

    private Long assignedToUserId;

    @NotNull
    @Builder.Default
    private boolean isCompleted = false;

    @NotNull
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
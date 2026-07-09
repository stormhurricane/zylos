package com.zylos.backend.features.projectgroup;

import com.zylos.backend.config.web.CurrentUserId;
import com.zylos.backend.features.projectgroup.dto.ProjectGroupRequest;
import com.zylos.backend.features.projectgroup.dto.ProjectGroupResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-groups")
@RequiredArgsConstructor
public class ProjectGroupController {

    private final ProjectGroupService projectGroupService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    public ResponseEntity<ProjectGroupResponse> create(
            @Valid @RequestBody ProjectGroupRequest request, 
            @CurrentUserId long currentUserId) {
        
        ProjectGroupResponse response = projectGroupService.createProjectGroup(request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    public ResponseEntity<List<ProjectGroupResponse>> getAll() {
        return ResponseEntity.ok(projectGroupService.getAllGroups());
    }

    @PostMapping("/{id}/members")
    @PreAuthorize("hasRole('INSTRUCTOR')") 
    public ResponseEntity<Void> addMember(
            @PathVariable Long id, 
            @RequestParam Long userId) {
        
        projectGroupService.addMemberManually(id, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    public ResponseEntity<List<ProjectGroupResponse>> search(@RequestParam String title) {
        return ResponseEntity.ok(projectGroupService.searchGroupsByTitle(title));
    }
}
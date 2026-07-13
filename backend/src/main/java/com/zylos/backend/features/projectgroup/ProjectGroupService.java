package com.zylos.backend.features.projectgroup;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.projectgroup.exception.ProjectGroupAlreadyExistsException;
import com.zylos.backend.features.projectgroup.exception.ProjectGroupNotFoundException;
import com.zylos.backend.features.projectgroup.exception.ProjectGroupAccessDeniedException;
import com.zylos.backend.features.projectgroup.member.ProjectGroupMemberService;
import com.zylos.backend.features.projectgroup.member.ProjectGroupRole;
import com.zylos.backend.features.projectgroup.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectGroupService {

    private final ProjectGroupRepository projectGroupRepository;
    private final CourseRepository courseRepository;
    private final ProjectGroupMemberService projectGroupMemberService;

    @Transactional
    public ProjectGroupResponse createProjectGroup(ProjectGroupRequest request, long currentUserId) {
        if (projectGroupRepository.findByTitle(request.title()).isPresent()) {
            throw new ProjectGroupAlreadyExistsException(request.title());
        }

        Course course = null;
        if (request.courseId() != null) {
            course = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new ProjectGroupNotFoundException(request.courseId()));
        }

        ProjectGroup projectGroup = ProjectGroup.builder()
                .title(request.title())
                .course(course)
                .createdBy(currentUserId)
                .build();

        projectGroup.addMember(currentUserId, ProjectGroupRole.ADMIN);

        ProjectGroup savedGroup = projectGroupRepository.save(projectGroup);
        return mapToResponse(savedGroup);
    }

    @Transactional
    public void addMemberManually(Long groupId, Long targetUserId, long currentUserId) {
        projectGroupMemberService.verifyUserIsAdmin(groupId, currentUserId);

        ProjectGroup group = projectGroupRepository.findById(groupId)
                .orElseThrow(() -> new ProjectGroupNotFoundException(groupId));
        
        boolean alreadyMember = group.getMembers().stream()
                .anyMatch(m -> m.getUserId().equals(targetUserId));
                
        if (alreadyMember) {
            throw new ProjectGroupAlreadyExistsException(group.getTitle());
        }

        group.addMember(targetUserId, ProjectGroupRole.MEMBER);
        projectGroupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public List<ProjectGroupResponse> searchGroupsByTitle(String title, long currentUserId) {
        return projectGroupRepository.findByTitleContainingIgnoreCase(title).stream()
                .filter(group -> group.getMembers().stream().anyMatch(m -> m.getUserId().equals(currentUserId)))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectGroupResponse> getAllGroupsForUser(long currentUserId) {
        return projectGroupRepository.findAll().stream()
                .filter(group -> group.getMembers().stream().anyMatch(m -> m.getUserId().equals(currentUserId)))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProjectGroupResponse mapToResponse(ProjectGroup group) {
        return new ProjectGroupResponse(
                group.getId(),
                group.getTitle(),
                group.getCourse() != null ? group.getCourse().getId() : null,
                group.getCreatedBy(),
                group.getCreatedAt()
        );
    }
}
package com.zylos.backend.features.projectgroup.member;

import com.zylos.backend.features.projectgroup.ProjectGroup;
import com.zylos.backend.features.projectgroup.exception.ProjectGroupAccessDeniedException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectGroupMemberService {

    private final ProjectGroupMemberRepository projectGroupMemberRepository;

    @Transactional(readOnly = true)
    public boolean isUserMember(Long groupId, Long userId) {
        return projectGroupMemberRepository.existsByProjectGroupIdAndUserId(groupId, userId);
    } 

    @Transactional(readOnly = true)
    public void verifyMemberAccess(Long groupId, Long userId) {
        if (!isUserMember(groupId, userId)) {
            throw new ProjectGroupAccessDeniedException(groupId, userId);
        }
    }

    @Transactional
    public void addMemberToGroup(ProjectGroup group, Long userId, ProjectGroupRole role) {
        ProjectGroupMember member = new ProjectGroupMember(group, userId, role);
        projectGroupMemberRepository.save(member);
    }

    public void verifyUserIsAdmin(Long groupId, Long userId) {
        verifyMemberAccess(groupId, userId);
        
        boolean isAdmin = projectGroupMemberRepository.existsByProjectGroupIdAndUserId(groupId, userId) 
            && projectGroupMemberRepository.findByProjectGroupIdAndUserId(groupId, userId)
                .map(member -> member.getRole() == ProjectGroupRole.ADMIN)
                .orElse(false);

        if (!isAdmin) {
            throw new ProjectGroupAccessDeniedException(groupId, userId); 
        }
    }
}
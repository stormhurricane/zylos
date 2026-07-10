package com.zylos.backend.features.projectgroup.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectGroupMemberRepository extends JpaRepository<ProjectGroupMember, Long> {
    
    boolean existsByProjectGroupIdAndUserId(Long projectGroup_id, Long user_id);
}
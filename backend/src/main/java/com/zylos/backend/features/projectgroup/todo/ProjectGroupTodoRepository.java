package com.zylos.backend.features.projectgroup.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectGroupTodoRepository extends JpaRepository<ProjectGroupTodo, Long> {
    List<ProjectGroupTodo> findByProjectGroupId(Long groupId);
}
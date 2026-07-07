package com.zylos.backend.features.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
       Optional<User> findByEmail(String email);

       @Query("SELECT u FROM User u " +
           "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
       List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

       @Query("SELECT u FROM User u " +
           "LEFT JOIN FETCH Student s ON u.id = s.id " +
           "LEFT JOIN FETCH Teacher t ON u.id = t.id " +
           "WHERE u.id IN :ids")
       List<User> findAllByIdWithSubtypes(@Param("ids") List<Long> ids);

       @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.id = :id")
       boolean isStudent(@Param("id") long id);

       @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Teacher t WHERE t.id = :id")
       boolean isInstructor(@Param("id") long id);
}
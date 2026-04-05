package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.database.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    Chat findChatByNutzerId1AndNutzerId2(int nutzerId1, int nutzerId2);
    List<Chat> findAllByNutzerId1(int nutzerId);
    List<Chat> findAllByNutzerId2(int nutzerId);
}

package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.database.ChatNachricht;

import java.util.ArrayList;

@Deprecated(since = "2024-06", forRemoval = true)
public interface ChatNachrichtRepository extends JpaRepository<ChatNachricht, Long> {

    ArrayList<ChatNachricht> findAllByChatId(int chatId);

}

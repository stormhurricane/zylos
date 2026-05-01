package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.Reminder;

import java.util.List;

@Repository
@Deprecated(since = "2024-06", forRemoval = true)
public interface ReminderRepository extends JpaRepository<Reminder, Integer> {

    List<Reminder> findAllByNutzerId (int nutzerId);

    List<Reminder> findAllByNutzerIdAndForm(int nutzerId, Reminder.FormEnum form);

    List<Reminder> findAllByForm(Reminder.FormEnum form);

    List<Reminder> findAllByTerminId(int terminId);

}

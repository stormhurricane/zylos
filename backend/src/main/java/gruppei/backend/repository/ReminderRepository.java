package gruppei.backend.repository;

import gruppei.backend.database.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Integer> {

    List<Reminder> findAllByNutzerId (int nutzerId);

    List<Reminder> findAllByNutzerIdAndForm(int nutzerId, Reminder.FormEnum form);

    List<Reminder> findAllByForm(Reminder.FormEnum form);

    List<Reminder> findAllByTerminId(int terminId);

}

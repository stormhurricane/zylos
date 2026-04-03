package gruppei.backend.repository;

import gruppei.backend.database.ChatNachricht;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;

public interface ChatNachrichtRepository extends JpaRepository<ChatNachricht, Long> {

    ArrayList<ChatNachricht> findAllByChatId(int chatId);

}

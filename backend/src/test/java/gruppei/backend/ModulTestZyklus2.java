package gruppei.backend;

import gruppei.backend.controller.LehrveranstaltungsController;
import gruppei.backend.controller.PrivateChatController;
import gruppei.backend.controller.communication.ChatWrapper;
import gruppei.backend.database.Chat;
import gruppei.backend.database.ChatNachricht;
import gruppei.backend.database.Lehrender;
import gruppei.backend.database.ProjektgruppenNachricht;
import gruppei.backend.repository.ChatNachrichtRepository;
import gruppei.backend.repository.ChatRepository;
import gruppei.backend.repository.ProjektgruppenNachrichtRepository;
import gruppei.backend.service.ChatNachrichtService;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//Quelle
//https://www.baeldung.com/java-spring-mockito-mock-mockbean

//@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModulTestZyklus2 {

    @MockBean
    ChatNachrichtRepository chatNachrichtRepository;

    @MockBean
    ChatRepository chatRepository;

    @MockBean
    ProjektgruppenNachrichtRepository projektgruppenNachrichtRepository;

    @Autowired
    ChatNachrichtService chatNachrichtService;

    @Autowired
    PrivateChatController privateChatController;

    @Autowired
    LehrveranstaltungsController lehrveranstaltungsController;

    @Test
    public void modulTest1() {
        //Given
        ChatNachricht chatNachricht = new ChatNachricht(2, "o o", "test");

        ArrayList<ChatNachricht> ergebnisse = new ArrayList<>();
        ergebnisse.add(chatNachricht);
        Mockito.when(chatNachrichtRepository.findAllByChatId(chatNachricht.getChatId())).thenReturn(ergebnisse);

        //When
        //Testanfang PrivaterChatController
        List<ChatNachricht> nachrichten = privateChatController.sendePrivateNachricht(chatNachricht);

        //Then
        Mockito.verify(chatNachrichtRepository, times(1)).save(any(ChatNachricht.class));
        assertEquals(1, nachrichten.size());
        assertEquals(chatNachricht, nachrichten.get(0));
    }


    @Test
    public void modulTest2() {
        //Given
        Chat chat = new Chat(1, 2);
        chat.setId(2);
        Mockito.when(chatRepository.findChatByNutzerId1AndNutzerId2(chat.getNutzerId1(), chat.getNutzerId2())).thenReturn(chat);

        ChatNachricht chatNachricht = new ChatNachricht(2, "o o", "test");
        ArrayList<ChatNachricht> ergebnisse = new ArrayList<>();
        ergebnisse.add(chatNachricht);
        Mockito.when(chatNachrichtRepository.findAllByChatId(chatNachricht.getChatId())).thenReturn(ergebnisse);

        //When
        ChatWrapper chatwrapper = privateChatController.zeigePrivatenChatAn(1, 2);

        //Then
        assertEquals(1, chatwrapper.getChatNachrichten().size());
        assertEquals(chatNachricht, chatwrapper.getChatNachrichten().get(0));

    }

    @Test
    public void modulTest3() {
        //Given
        ProjektgruppenNachricht projektgruppenNachricht = new ProjektgruppenNachricht(1, "Max Mustermann", "test");
        ArrayList<ProjektgruppenNachricht> ergebnisse = new ArrayList<>();
        ergebnisse.add(projektgruppenNachricht);
        Mockito.when(projektgruppenNachrichtRepository.findAllByProjektgruppenId(2)).thenReturn(ergebnisse);

        //When
        List<ProjektgruppenNachricht> projektgruppenNachrichtList = lehrveranstaltungsController.zeigeGruppenChat(2);

        //Then
        assertEquals(1, projektgruppenNachrichtList.size());
        assertEquals(projektgruppenNachricht, projektgruppenNachrichtList.get(0));
    }

}

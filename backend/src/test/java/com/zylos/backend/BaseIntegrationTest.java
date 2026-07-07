package com.zylos.backend;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // Aktiviert MockMvc zentral für alle Subklassen
@ActiveProfiles("test") // FIX: Erzwingt dein Test-Profil (H2 mit Flyway)
@Transactional // Rollt Änderungen nach jedem Testfall automatisch zurück
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc; // Steht jetzt automatisch in allen Controllertests bereit
}
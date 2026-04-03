package gruppei.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private void sendSimpleMail(SimpleMailMessage message) {
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }

    public void generiereLogin2FAMail(int code, String vorname, String nachname, String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject("Login-Verifizierung");
        mailMessage.setText("Hallo " + vorname + " " + nachname + ",\n\n"
        + "Es wurde ein Loginversuch in ihrem Doodle-Account an " + this.getCurrentTime() + " festgestellt.\n"
        + "Falls sie dies nicht waren, ignorieren sie diese Mail.\n\n" + "Falls doch, geben sie folgenden Code ein, um ihren Login zu bestätigen: \n\n"
        + code + "\n\n" + "Mit Freundlichen Grüßen\nIhr Doodle-Team");

        this.sendSimpleMail(mailMessage);
    }

    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dtf.format(LocalDateTime.now());
    }

    public void generiereReminderEmail(String vorname, String nachname, String email, String betreff, LocalDate datum,
                                       String uhrzeit) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject("Reminder für "  + betreff);
        mailMessage.setText("Hallo " + vorname + " " + nachname + ", \n\nwir möchten sie hiermit daran erinnern, "
        + "dass sie einen Termin am " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(datum) + " um " + uhrzeit
        + " haben.\n\nBitte loggen sie sich spätestens zu diesem Zeitpunkt in Doodle wieder ein.\n\n"
        + "Mit freundlichen Grüßen\nIhr Doodle-Team\n\n\n\n\nDies ist eine automatisch generierte Email. " +
                "Bitte antworten sie nicht darauf.");

        this.sendSimpleMail(mailMessage);
    }

    public void generiereBestehensMail(String vorname, String nachname, String email, String titel, boolean bestanden) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject("Ihre Teilnahme an " + titel);

        String bestehensNachricht = bestanden ? "bestanden" : "nicht bestanden";

        mailMessage.setText("Hallo " + vorname + " " + nachname + ",\n\nwir möchten sie hiermit informieren, dass sie  "
        +  titel + " " + bestehensNachricht + " haben.\n\nWir wünschen ihnen weiterhin viel Erfolg.\n\n"
        + "Mit freundlichen Grüßen\nIhr Doodle-Team\n\n\n\n\nDies ist eine automatisch generierte Email. " +
                "Bitte antworten sie nicht darauf." );

        this.sendSimpleMail(mailMessage);
    }
}

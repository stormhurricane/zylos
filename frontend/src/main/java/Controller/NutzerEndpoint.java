package Controller;


import Controller.Communication.ChatWrapper;
import Controller.Communication.FreundschaftsAnfrage;
import Controller.Communication.NutzerWrapper;
import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface NutzerEndpoint {

    @POST("api/v1/nutzer/register?nutzer=student")
    Call<Boolean> profilErstellenStudent(@Body Student studentToRegister);

    @POST("api/v1/nutzer/register?nutzer=lehrender")
    Call<Boolean> profilErstellenLehrender(@Body Lehrender lehrenderToRegister);

    @GET("api/v1/nutzer/view/{id}")
    Call<NutzerWrapper> profilAufrufen(@Path("id") Integer id);

    @POST("api/v1/teilnehmerliste/check/{id}")
    Call<Boolean> pruefeTeilnahme(@Path("id") int nutzerid, @Body int pgid);

    //calls für LVs
    @GET("api/v1/lehrveranstaltung/view{id}")
    Call<VeranstaltungsWrapper> ladeLehrveranstaltung(@Path("id") Integer id);

    @GET("api/v1/lehrveranstaltung/all")
    Call<ArrayList<VeranstaltungsWrapper>> ladeLVListe();

    @POST("api/v1/lehrveranstaltung/find")
    Call<VeranstaltungsWrapper> sucheLV(@Body Map<String, String> map);

    @GET("api/v1/lehrveranstaltung/find/{id}")
    Call<VeranstaltungsWrapper> sucheLVPerID(@Path("id") Integer id);

    @GET("api/v1/lehrveranstaltungmaterial/find/{id}")
    Call<ArrayList<LehrveranstaltungsMaterial>> ladeLVMaterial(@Path("id") Integer id);

    @POST("api/v1/teilnehmerliste/join")
    Call<Boolean> treteVeranstaltungBei(@Body Map<String, Integer> beitreteMap);

    @GET("api/v1/teilnehmerliste/lv/{id}")
    Call<ArrayList<NutzerWrapper>> ladeTeilnehmerliste(@Path("id") Integer id);

    @GET("api/v1/teilnehmerliste/nutzer/{id}")
    Call<ArrayList<VeranstaltungsWrapper>> ladeMeineLVs(@Path("id") Integer id);

    //Calls for frens
    @GET("api/v2/friends/show/{id}")
    Call<ArrayList<NutzerWrapper>> meineFreunde(@Path("id") int id);

    @POST("api/v2/friends/sendRequest/{id}")
    Call<Boolean> sendeAnfrage(@Path("id") int id1, @Body int id2);

    @POST("api/v2/friends/respond")
    Call<Boolean> behandleAnfrage(@Body FreundschaftsAnfrage anfrage);

    @GET("api/v2/friends/openRequests/{id}")
    Call<ArrayList<NutzerWrapper>> offeneAnfragen(@Path("id") int id);

    //Calls for tako
    @POST("api/v2/privatechat/send")
    Call<List<ChatNachricht>> sendePN(@Body ChatNachricht nachricht);

    @POST("api/v2/privatechat/with/{id}")
    Call<ChatWrapper> ladeNachrichten(@Path("id") int id1, @Body int id2);

    @GET("api/v2/privatechat/show/{id}")
    Call<ArrayList<Chat>> ladeMeineChats(@Path("id") int id);

    //calls für PG
    @GET("api/v1/lehrveranstaltung/chat/{id}")
    Call<ArrayList<ProjektgruppenNachricht>> zeigeGruppenChat(@Path("id") int id);

    @POST("api/v1/lehrveranstaltung/chat/post")
    Call<ArrayList<ProjektgruppenNachricht>> sendePGNachricht(@Body ProjektgruppenNachricht nachricht);

    @GET("api/v1/lehrveranstaltung/todo/{id}")
    Call<ArrayList<ToDos>> zeigeToDos(@Path("id") int id);

    @POST("api/v1/lehrveranstaltung/todo/add")
    Call<Boolean> fuegeToDosZu(@Body ToDos todo);

    @PUT("api/v1/lehrveranstaltung/todo/done")
    Call<Boolean> hakeToDoAb(@Body int id);

    //Calls für Quiz
    @GET("api/v2/quiz/showFrom/{id}")
    Call<ArrayList<Test>>  zeigeAlleQuizEinerLV(@Path("id") int id);

    //Calls für Kalender
    @POST("api/v2/calender/termineEinesDatums/{id}")
    Call<ArrayList<Termin>> ladeTermine(@Path("id") int nutzerid, @Body Map<String, Integer> map);

    @POST("api/v2/calender/reminderFuerPopUpSchicken/{id}")
    Call<ArrayList<Reminder>> ladeReminder(@Path("id") int nutzerid, @Body Map dateTimeMap);

    @GET("api/v2/calender/terminVonReminder/{id}")
    Call<Termin> terminDesReminders(@Path("id") int teriminID);

    //Calls für Lernkarten
    @POST("api/v1/lehrveranstaltung/lernkartenThema/{id}")
    Call<Integer> lernkartenThemaErstellen(@Path("id") int lvid, @Body String thema);

    @GET("api/v1/lehrveranstaltung/lernkartenThemaListe/{id}")
    Call<ArrayList<LernkartenThema>> lernkartenThemaListe(@Path("id") int lvid);

    @POST("api/v1/lehrveranstaltung/lernkarte")
    Call<Boolean> lernkarteErstellen(@Body Lernkarte lk);

    @GET("api/v1/lehrveranstaltung/lernkarten/{id}")
    Call<ArrayList<Lernkarte>> lernkartenListe(@Path("id") int lktid);

    //Calls für Themenangebote
    @GET("api/v3/topic/showAllTopics/{id}")
    Call<ArrayList<ArbeitsThema>> zeigeArbeitsThemen(@Path("id") int lehrendenid);

    @POST("api/v1/teilnehmerliste/studentOf/{id}")
    Call<Boolean> gemeinsameLV(@Path("id") int studentid, @Body int lehrerid);

    //Calls für Bewertung
    @GET("api/v3/lvBewertung/showQuestions/{id}")
    Call<ArrayList<Frage>> ladeBewertung(@Path("id") int lvid);

    @POST("api/v3/lvBewertung/check/{id}")
    Call<Integer> erstelleBewertungsversuch(@Path("id") int nutzerid, @Body int lvid);

    @POST("api/v3/lvBewertung/createFeedback")
    Call<Boolean> erstelleFeedbackFuerVersuch(@Body List<BewertungsFeedback> bewertungsFeedbacks);

    @POST("api/v3/lvBewertung/createFeedbackStatistics/{id}")
    Call<ArrayList<Integer[]>> erstelleBewertungsStatistik(@Path("id") int testid, @Body int bestanden);

    @POST("api/v2/quiz/forceSemester")
    Call<Boolean> erzwingeBestehenspruefung(@Body Map<String, String> semesterDaten);
}

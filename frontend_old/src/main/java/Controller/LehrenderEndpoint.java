package Controller;

import Controller.Communication.NutzerWrapper;
import Controller.Communication.QuizWrapper;
import Controller.Communication.StatistikWrapper;
import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.Map;

public interface LehrenderEndpoint {

    @PUT("api/v1/nutzer/update/{id}")
    Call<Boolean> updateProfil(@Path("id") int id, @Body Map<String, String> updatelehrender);

    @POST("api/v1/lehrveranstaltung/create/{id}")
    Call<Boolean> erstelleLehrveranstaltung(@Path("id") int id, @Body VeranstaltungsWrapper lehrveranstaltung);

    @POST("api/v1/nutzer/findStudent")
    Call<ArrayList<Integer>> sucheStudent(@Body Map<String, String> map);

    @POST("api/v1/teilnehmerliste/addStudent")
    Call<Boolean> fuegeStudentzuLVHinzu(@Body Map<String, Integer> zufuegeMap);


    @POST("api/v1/lehrveranstaltungmaterial/hinzufuegen")
    Call<Boolean> fuegeLVMaterialHinzu(@Body LehrveranstaltungsMaterial zufuegeMaterial);

    //Calls für Quiz
    @POST("api/v2/quiz/new")
    Call<Boolean> legeQuizAn(@Body QuizWrapper quizWrapper);

    @GET("api/v2/quiz/statistik/{id}")
    Call<StatistikWrapper> zeigeStatistikEinesTests(@Path("id") int id);

    //Calls für Kalender
    @POST("api/v2/calender/erstelleTermin")
    Call<Integer> legeTerminAn(@Body Termin t);

    @POST("api/v2/calender/reminderEinesTermins")
    Call<Boolean> reminderTermin(@Body Reminder reminder);

    //Call für Themenangebote
    @POST("api/v3/topic/addTopic")
    Call<Boolean> erstelleThema(@Body ArbeitsThema thema);

    //Calls für Bewertung
    @POST("api/v3/lvBewertung/create")
    Call<Boolean> erstelleBewertung(@Body QuizWrapper quizWrapper);


}

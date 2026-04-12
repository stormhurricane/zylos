package Controller;

import datenklassen.Feedback;
import datenklassen.Frage;
import datenklassen.Student;
import jdk.nashorn.internal.codegen.types.BooleanType;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface StudentEndpoint {

    @POST("/api/v1/nutzer/login")
    Call<Integer[]> loginPerEmail(@Body Map<String, String> loginMap);

    @POST ("api/v1/nutzer/verify/{id}")
    Call<Boolean> verifiziereLogin(@Path("id") int id, @Body int code);

    @POST("/api/v1/nutzer/login") //s.o.
    Call<Integer[]> loginPerMatrikelNummer(@Body Map<String, String> loginMap);

    @PUT("api/v1/nutzer/update/{id}")
    Call<Boolean> updateProfil(@Path("id") int id, @Body Map<String, String> updatestudent);

    //Calls für Quiz
    @GET("api/v2/quiz/showQuestions/{id}")
    Call<ArrayList<Frage>> zeigeFragenEinesTests(@Path("id") int testid);

    @GET("api/v2/quiz/findeFrageMitId/{id}")
    Call<Frage> findeFrageMitId(@Path("id") int frageid);

    @POST("api/v2/quiz/feedback")
    Call<Boolean> legeFeedbackAn(@Body Feedback feedback);

    @GET("api/v2/quiz/showFeedback/{id}")
    Call<ArrayList<Feedback>> zeigeFeedbackEinesVersuchs(@Path("id") int versuchsid);

    @POST("api/v2/quiz/versuch")
    Call<Integer> legeVersuchAn(@Body Map<String, Integer> versuch);

    @PUT("api/v2/quiz/pruefeVersuch/")
    Call<Boolean> pruefeVersuch(@Body int versuchsid);

    @POST("api/v3/lvBewertung/checkParticipation/{id}")
    Call<Boolean> pruefeBearbeitung(@Path("id") int studentenid, @Body int lvid);
}

package ss18.mc.positime.network;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import ss18.mc.positime.model.Response;
import ss18.mc.positime.model.User;

public interface RetrofitInterface {

    @POST("register")
    Observable<Response> register(@Body User user);

    @POST("authenticate")
    Observable<Response> login();

    @POST("token/{email}")
    Observable<Response> checkToken(@Path("email") String email);
}



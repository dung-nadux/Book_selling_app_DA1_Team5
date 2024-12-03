package fpoly.dungnm.assignment_resapi.service;

import static fpoly.dungnm.assignment_resapi.service.ApiService.BASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private ApiService requestInterface;
    public HttpRequest() {
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }
    public ApiService callAPI() {
        return requestInterface;
    }
}

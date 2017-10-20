package biome.fresnotes;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Drew on 12/3/2016.
 */

public class RetrofitShit {

    public static final String BASE_URL = "https://us-central1-com.fresboard.fresnotes-f3f74.cloudfunctions.net";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }




    public static void createProject(String authorid, String projectName, String teamId, String description, Integer visible){

        APIService service = getClient(BASE_URL).create(APIService.class);
        service.createProject(authorid,projectName,teamId,description, visible).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //todo it's lit
            }
        });
    }

    public static void createNote(String authorId, String name, String teamId, String desc, int visibility, int responseType, String[] labels, String projectId, String[] attachments){
        APIService service = getClient(BASE_URL).create(APIService.class);
        service.saveNote(authorId, name, teamId, desc, visibility, responseType, labels, projectId, attachments).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}

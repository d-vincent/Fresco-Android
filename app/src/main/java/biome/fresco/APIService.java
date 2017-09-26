package biome.fresco;

import biome.fresco.Objects.ProjectObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Drew on 8/13/2017.
 */

public interface APIService {

    @POST("/createProject")
    @FormUrlEncoded
    Call<ResponseBody> createProject(@Field("author") String authorId,
                                @Field("name") String name,
                                @Field("team") String teamId,
                                @Field("desc") String description,
                                @Field("visibility") Integer visibility);

    @POST("/createProject")
    @FormUrlEncoded
    Call<ResponseBody> deleteProject(@Field("project") String projectId);

    @POST("/createProject")
    @FormUrlEncoded
    Call<ResponseBody> updateProject(@Field("project") String projectId,
                                     @Field("name") String name,
                                     @Field("desc") String description,
                                     @Field("visibility") Integer visibility);

    @POST("/saveNote")
    @FormUrlEncoded
    Call<ResponseBody> saveNote(@Field("key") String authorId,
                                     @Field("content") String name,
                                     @Field("title") String teamId,
                                     @Field("labels") String description,
                                     @Field("public") Integer visibility,
                                     @Field("responseType") Integer resoinseType,
                                     @Field("startingLabels") String[] startingLabels,
                                     @Field("projectKey") String projectKey,
                                     @Field("attachments") String[] attachments);

    @POST("/createProject")
    @FormUrlEncoded
    Call<ResponseBody> createLabel(@Field("projectId") String projectId,
                                     @Field("labelId") String labelId,
                                     @Field("label") String labelName,
                                     @Field("color") String colorForLabel);
}

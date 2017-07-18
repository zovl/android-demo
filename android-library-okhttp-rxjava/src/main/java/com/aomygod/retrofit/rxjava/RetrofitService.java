package com.aomygod.retrofit.rxjava;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Retrofit网络访问服务
 */
public interface RetrofitService {

    /** Observable 回调 */

    @GET("{lastUrl}")
    Observable<Object> doGet(@Path("lastUrl") String lastUrl, @QueryMap Map<String, Object> queryParams, @HeaderMap Map<String, Object> headers);

    @GET
    Observable<Object> doGetNewUrl(@Url String newUrl, @QueryMap Map<String, Object> queryParams, @HeaderMap Map<String, Object> headers);

    @FormUrlEncoded
    @POST("{lastUrl}")
    Observable<Object> doPost(@Path("lastUrl") String lastUrl, @QueryMap Map<String, Object> queryParams, @FieldMap Map<String, Object> formParams, @HeaderMap Map<String, Object> headers);

    @FormUrlEncoded
    @POST
    Observable<Object> doPostNewUrl(@Url String newUrl, @QueryMap Map<String, Object> queryParams, @FieldMap Map<String, Object> formParams, @HeaderMap Map<String, Object> headers);

    @POST
    Observable<Object> doPostNewUrl(@Url String newUrl, @QueryMap Map<String, Object> queryParams, @Body Object body, @HeaderMap Map<String, Object> headers);

    /** ResponseBody 回调 */

    @Multipart
    @POST
    Call<ResponseBody> uploadFiles(@Url String newUrl, @PartMap() Map<String, RequestBody> params, @HeaderMap Map<String, Object> headers);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String newUrl);
}

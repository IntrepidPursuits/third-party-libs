package com.intrepid.thirdpartylibs.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intrepid.thirdpartylibs.models.GitHubRepo;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import timber.log.Timber;

public class ServiceManager {
    public static void init() {
        GitHub.initService();
    }

    public static class GitHub {
        private static final String BASE_URL = "https://api.github.com/";
        private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

        public static GitHubService gitHubService;

        private static void initService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(getConverter())
                    .client(getClient())
                    .build();
            gitHubService = retrofit.create(GitHubService.class);
        }

        private static Converter.Factory getConverter() {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setDateFormat(DATE_FORMAT)
                    .setPrettyPrinting()
                    .create();
            return GsonConverterFactory.create(gson);
        }

        private static OkHttpClient getClient() {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Timber.tag("OkHttp").d(message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            return new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
        }

        public interface GitHubService {
            @GET("users/{username}/repos")
            Call<List<GitHubRepo>> getRepos(@Path("username") String username);
        }
    }
}

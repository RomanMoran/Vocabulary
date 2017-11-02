package com.example.roman.vocabulary.utilities;

import com.example.roman.vocabulary.api_utility.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by roman on 28.08.2017.
 */

public class RetrofitUtils {
    private static final String TAG = RetrofitUtils.class.getName();
    private static final String BASE_URL = "http://api.lingualeo.com/";
    public static final String SERVER_DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static final String[] PARSE_DATE_FORMATS = new String[]{
            SERVER_DATE_FORMAT_1
    };

    private static ApiService apiService;
    private static RetrofitUtils instance;
    private static Gson gson;


    public static RetrofitUtils getInstance() {
        if (instance == null) instance = new RetrofitUtils();
        return instance;
    }

    private ApiService initApiService() {
        return apiService = provideRetrofit(BASE_URL)
                .create(ApiService.class);
    }

    private HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    private Retrofit provideRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().addInterceptor(provideHttpLoggingInterceptor()).build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Gson getGson() {
        if (gson == null) gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                //.registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Double.class, new TypeAdapter<Double>() {

                    @Override
                    public void write(JsonWriter writer, Double value) throws IOException {
                        if (value == null) {
                            writer.nullValue();
                            return;
                        }
                        writer.value(value);
                    }

                    @Override
                    public Double read(JsonReader reader) throws IOException {
                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull();
                            return null;
                        }
                        String stringValue = reader.nextString();
                        try {
                            Double value = Double.valueOf(stringValue);
                            return value;
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }
                })

               /* .registerTypeAdapter(Gender.class, (JsonDeserializer<Gender>)
                        (json, typeOfT, context) -> Gender.parse(json.getAsString()))*/
                .create();
        return gson;
    }

    private static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
          /*  try {
               long timestamp=jsonElement.getAsJsonPrimitive().getAsLong();
               // DebugUtility.logTest(TAG,"timestamp "+timestamp);
                return new Date(timestamp*1000);
            } catch (Exception e) {
            }*/
            for (String format : PARSE_DATE_FORMATS) {

                try {
                    return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
                } catch (ParseException e) {
                }
            }
            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + Arrays.toString(PARSE_DATE_FORMATS));
        }

    }


    public ApiService getApiService() {
        return apiService != null ? apiService : initApiService();
    }

}

package api;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.net.URL;

public class APIService {
    private static String apiURL = "http://openapi.seoul.go.kr:8088/696474764564616e39307548544a75/json/TbPublicWifiInfo/";
    private static OkHttpClient client = new OkHttpClient();

    public static int getWifiCount() throws IOException {
        int count = 0;
        URL url = new URL(apiURL + "1/1");

        Request.Builder builder = new Request.Builder().url(url).get();
        Response response = client.newCall(builder.build()).execute();

        try {
            if (response.isSuccessful()) {
                ResponseBody respBody = response.body();

                if (respBody != null) {
                    String jsonString = respBody.string();

                    // JsonElement로 파싱하기 전에 원본 json 문자열을 출력
                    System.out.println("JSON Response: " + jsonString);

                    JsonElement element = JsonParser.parseString(respBody.string());

                    count = element.getAsJsonObject().get("TbPublicWifiInfo")
                            .getAsJsonObject().get("list_total_count")
                            .getAsInt();

                    System.out.println("Total count: " + count);
                }
            }

            else {
                System.out.println("API request failed : " + response.code());
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return count;
    }

    public static void main(String[] args) {
        try {
            getWifiCount();
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}

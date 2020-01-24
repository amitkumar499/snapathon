package helpers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

public class PinPointCaller {

    private static final String PINPOINT_URL = "http://pinpoint.ops.snapdeal.io";
    private static final String BASIC_AUTH_HEADER_VALUE = "dmluZWV0LmNoYXVyYXNpYTpkZWNANDM1MQ==";


    public static void getInfoForApp(String appName, DockerConfig config) {

        String url = buildUrl(appName);
        String response = null;
        try {
            response = getResponse(url, null);
            // log("response =" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonObject el = new Gson().fromJson(response, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : el.entrySet()) {
            JsonArray vmArgs = entry.getValue().getAsJsonArray().get(0).getAsJsonObject().get("serverMetaData").getAsJsonObject().get("vmArgs").getAsJsonArray();
            for (JsonElement element : vmArgs) {
                String elStr = element.getAsString();
                if (element.getAsString().startsWith("-X")) {
                    log("arg = " + element.getAsString());
                    if (elStr.startsWith("-Xms")) {

                    }
                }
            }
            break;
        }
    }

    private static String buildUrl(String appName) {
        // end product
        // http://pinpoint.ops.snapdeal.io/getAgentList.pinpoint?application=sts-api&from=1579852270000&to=1579852570000;
        Date start = new Date();
        return String.format("%s/getAgentList.pinpoint?application=%s&from=%s&to=%s", PINPOINT_URL, appName, String.valueOf(start.getTime()), String.valueOf(start.getTime()));
    }


    private static String getResponse(String url, String body) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("Authorization", "Basic "+BASIC_AUTH_HEADER_VALUE);
        //HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type","application/json");
        //request.setEntity(new ByteArrayEntity(body.getBytes()));
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader rd = null;
        try {
            rd = new BufferedReader
                    (new InputStreamReader(
                            response.getEntity().getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = "";
        StringBuilder textView = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            textView.append(line);
        }
        return textView.toString();
    }


    private static void log(String txt) {
        System.out.println(txt);
    }

    public static void main(String[] args) {
        // aggregator-crmweb
        // sts-api
        // poms-web
        getInfoForApp("sts-api", new DockerConfig(0.5f));
    }
}


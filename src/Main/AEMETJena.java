package Main;

import Model.Grafo;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AEMETJena {

    private static final String clave = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuZnJhcGNAZ21haWwuY29tIiwianRpIjoiMzg1ZTA5YjctYjYzZi00YWRiLTgxZDMtYmEwOWM4NTk1MWQxIiwiaXNzIjoiQUVNRVQiLCJpYXQiOjE1MjE0NTY1MDMsInVzZXJJZCI6IjM4NWUwOWI3LWI2M2YtNGFkYi04MWQzLWJhMDljODU5NTFkMSIsInJvbGUiOiIifQ.L9mPIfI4xTvizQm3m27Gqbg_zSIVYEuui9ZVY749kGk";

    public static void main(String[] args) throws IOException, JSONException {
        System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://opendata.aemet.es/opendata/api/valores/climatologicos/inventarioestaciones/todasestaciones/?api_key=" + clave)
                .get()
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject json = new JSONObject(response.body().string());
        System.out.println(json.get("datos"));
        
        request = new Request.Builder()
                .url(json.get("datos").toString())
                .get()
                .addHeader("cache-control", "no-cache")
                .build();
        
        response = client.newCall(request).execute();
        JSONArray jsonArray = new JSONArray(response.body().string());
        System.out.println(jsonArray.getJSONObject(0).toString(4));
        Grafo grafo = new Grafo();
        grafo.addStation(jsonArray.getJSONObject(0));
        grafo.print();
    }

}

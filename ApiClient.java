import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

// Cliente para consumir la API de Pokemon
public class ApiClient {
    private static final String API_URL = "https://pokeapi.co/api/v2/pokemon?limit=151";

    public static List<Character> fetchCharacters() {
        List<Character> characters = new ArrayList<>();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();

            JSONObject json = new JSONObject(sb.toString());
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject obj = results.getJSONObject(i);
                String name = obj.optString("name", "Sin nombre");
                String urlDetail = obj.optString("url", "");
                String imageUrl = "";
                String description = "";
                // Obtener imagen y descripción (desde species) usando el endpoint de detalle
                try {
                    if (urlDetail != null && !urlDetail.isEmpty()) {
                        URL detailUrl = new URL(urlDetail);
                        HttpURLConnection detailConn = (HttpURLConnection) detailUrl.openConnection();
                        detailConn.setRequestMethod("GET");
                        detailConn.setRequestProperty("Accept", "application/json");
                        int detailStatus = detailConn.getResponseCode();
                        if (detailStatus >= 200 && detailStatus < 300) {
                            StringBuilder detailSb = new StringBuilder();
                            try (BufferedReader detailBr = new BufferedReader(new InputStreamReader(detailConn.getInputStream(), StandardCharsets.UTF_8))) {
                                String l;
                                while ((l = detailBr.readLine()) != null) detailSb.append(l);
                            }
                            JSONObject detailJson = new JSONObject(detailSb.toString());
                            // Obtener imagen del sprite oficial
                            if (detailJson.has("sprites") && !detailJson.isNull("sprites")) {
                                imageUrl = detailJson.getJSONObject("sprites").optString("front_default", "");
                            }
                            // Intentar obtener descripción (flavor text) desde el endpoint de species
                            if (detailJson.has("species") && !detailJson.isNull("species")) {
                                String speciesUrl = detailJson.getJSONObject("species").optString("url", "");
                                if (!speciesUrl.isEmpty()) {
                                    try {
                                        URL sUrl = new URL(speciesUrl);
                                        HttpURLConnection sConn = (HttpURLConnection) sUrl.openConnection();
                                        sConn.setRequestMethod("GET");
                                        sConn.setRequestProperty("Accept", "application/json");
                                        int sStatus = sConn.getResponseCode();
                                        if (sStatus >= 200 && sStatus < 300) {
                                            StringBuilder sSb = new StringBuilder();
                                            try (BufferedReader sBr = new BufferedReader(new InputStreamReader(sConn.getInputStream(), StandardCharsets.UTF_8))) {
                                                String m;
                                                while ((m = sBr.readLine()) != null) sSb.append(m);
                                            }
                                            JSONObject speciesJson = new JSONObject(sSb.toString());
                                            if (speciesJson.has("flavor_text_entries")) {
                                                for (Object o : speciesJson.getJSONArray("flavor_text_entries")) {
                                                    JSONObject f = (JSONObject) o;
                                                    JSONObject lang = f.optJSONObject("language");
                                                    if (lang != null && "en".equals(lang.optString("name"))) {
                                                        String text = f.optString("flavor_text", "");
                                                        // limpiar saltos raros
                                                        description = text.replaceAll("\n|\f", " ").trim();
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        sConn.disconnect();
                                    } catch (Exception ex2) {
                                        // no bloquee la carga de la imagen si falla la species
                                    }
                                }
                            }
                        }
                        detailConn.disconnect();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                characters.add(new Character(i, name, imageUrl, description));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return characters;
    }
}

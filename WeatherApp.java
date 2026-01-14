import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherApp {

    public static void main(String[] args) {
        String url = "https://api.open-meteo.com/v1/forecast"
                   + "?latitude=52.52&longitude=13.41&current_weather=true";

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            // Simple JSON parsing (manual)
            String temperature = extractValue(json, "\"temperature\":", ",");
            String windspeed = extractValue(json, "\"windspeed\":", ",");
            String winddirection = extractValue(json, "\"winddirection\":", ",");
            String time = extractValue(json, "\"time\":\"", "\"");

            // Display structured output
            System.out.println("===== Current Weather Report =====");
            System.out.println("Location        : Berlin, Germany");
            System.out.println("Observation Time: " + time);
            System.out.println("---------------------------------");
            System.out.println("Temperature     : " + temperature + " °C");
            System.out.println("Wind Speed      : " + windspeed + " km/h");
            System.out.println("Wind Direction  : " + winddirection + "°");
            System.out.println("=================================");

        } catch (Exception e) {
            System.err.println("Failed to fetch weather data");
            e.printStackTrace();
        }
    }

    // Helper method to extract JSON values
    private static String extractValue(String json, String key, String endChar) {
        int start = json.indexOf(key);
        if (start == -1) return "N/A";
        start += key.length();
        int end = json.indexOf(endChar, start);
        return json.substring(start, end);
    }
}

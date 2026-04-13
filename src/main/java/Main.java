import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        // ✅ Placeholder API Key (replace later)
        String apiKey = System.getenv("OPENAIKEY");

        // ✅ Request body
        String requestBody = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {
              "role": "user",
              "content": "Hii, Give me a joke"
            }
          ]
        }
        """;

        // ✅ Create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // ✅ Create request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            // ✅ Send request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response:");
            System.out.println(response.body());

            // ✅ Save response to file
            Files.writeString(Paths.get("output.txt"), response.body());

            System.out.println("Saved response to output.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Force clean exit (important for GitHub Actions)
        System.exit(0);
    }
}

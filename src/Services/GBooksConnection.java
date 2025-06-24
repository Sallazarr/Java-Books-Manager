package Services;

import Utils.ConfigApi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GBooksConnection {

    private final String apiKey = ConfigApi.get("apiKey");
    private final HttpClient client = HttpClient.newHttpClient();

    public String searchBook(String search) {
        try {
          String url = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + search + "&maxResults=10&key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}

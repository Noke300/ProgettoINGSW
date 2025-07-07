package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class NotificationClient {

    private final String userId;
    private final CopyOnWriteArrayList<Consumer<String>> listeners = new CopyOnWriteArrayList<>();
    private final HttpClient client;

    public NotificationClient(String userId) {
        this.userId = userId;
        this.client = HttpClient.newBuilder()
                .build();
    }

    public void start() {
        new Thread(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:8081/notifiche/stream?userId=" + userId))
                        .header("Accept", "text/event-stream")
                        .GET()
                        .build();

                HttpResponse<java.io.InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data:")) {
                            String message = line.substring("data:".length()).trim();
                            notifyListeners(message);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    private void notifyListeners(String message) {
        for (Consumer<String> listener : listeners) {
            listener.accept(message);
        }
    }
}



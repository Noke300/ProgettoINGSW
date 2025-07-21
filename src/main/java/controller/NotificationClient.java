package controller;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class NotificationClient extends Controller {

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
                        .uri(new URI(URL_SERVER+":8081/notifiche/stream?userId=" + userId))
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

            }catch (Exception e) {}
        }).start();
    }

    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    public static void  notificheWindow(String messaggio){
        JWindow notificationWindow = new JWindow();

        JLabel label = new JLabel("Notifica: " + messaggio);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setOpaque(true);
        label.setBackground(new Color(255, 255, 204)); // giallo tenue
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        notificationWindow.getContentPane().add(label);
        notificationWindow.pack();

        // Posizionamento in alto a destra dello schermo
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - notificationWindow.getWidth() - 20;
        int y = 20; // margine dall'alto
        notificationWindow.setLocation(x, y);

        notificationWindow.setFocusableWindowState(false);

        notificationWindow.setVisible(true);

        // Chiude la finestra dopo 7 secondi
        new Timer(7000, e -> notificationWindow.dispose()).start();

    }
    private void notifyListeners(String message) {
        for (Consumer<String> listener : listeners) {
            listener.accept(message);
        }
    }
}



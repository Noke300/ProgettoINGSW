package gui.costum_components;

import controller.Controller;
import controller.ControllerClienti;
import gui.pagine_clienti.LogIn;
import model.Immobile;
import model.Visita;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VisitaPanel {
    private static final int GIORNI_METEO = 14;
    public static void createPanel(String token, JFrame frame, Immobile immobile, Controller control) {
        if (!"".equals(token)) {
            JDialog visitaDialog = new JDialog(frame, "Visita", true);
            visitaDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            visitaDialog.setSize(700, 550);
            visitaDialog.setResizable(false);
            visitaDialog.setLayout(new BorderLayout());

            Locale locale = new Locale("it", "IT");
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.X_AXIS));
            dayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            ButtonGroup dayGroup = new ButtonGroup();
            Map<JToggleButton, LocalDate> bottoniDataMap = new HashMap<>();

            String[] temperatureMax = new String[GIORNI_METEO];
            String[] temperatureMin = new String[GIORNI_METEO];
            int[] weatherCodes = new int[GIORNI_METEO];

            try {
                String endpoint = "https://api.open-meteo.com/v1/forecast?latitude=" + immobile.getLanIm() +
                        "&longitude=" + immobile.getLongIm() + "&daily=temperature_2m_max,temperature_2m_min,weathercode&timezone=auto&forecast_days=" + GIORNI_METEO;

                HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseStr = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseStr.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(responseStr.toString());
                JSONArray tempsMax = json.getJSONObject("daily").getJSONArray("temperature_2m_max");
                JSONArray tempsMin = json.getJSONObject("daily").getJSONArray("temperature_2m_min");
                JSONArray meteoCodes = json.getJSONObject("daily").getJSONArray("weathercode");

                for (int i = 0; i < GIORNI_METEO; i++) {
                    temperatureMax[i] = tempsMax.getDouble(i) + "°C";
                    temperatureMin[i] = tempsMin.getDouble(i) + "°C";
                    weatherCodes[i] = meteoCodes.getInt(i);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                for (int i = 0; i < GIORNI_METEO; i++) {
                    temperatureMax[i] = "?°C";
                    temperatureMin[i] = "?°C";
                    weatherCodes[i] = -1;
                }
            }
            // Bottoni con icona meteo
            for (int i = 0; i < GIORNI_METEO; i++) {
                LocalDate day = LocalDate.now().plusDays(i);
                String giorno = String.valueOf(day.getDayOfMonth());
                String mese = day.getMonth().getDisplayName(TextStyle.FULL, locale);

                JToggleButton dayButton = new JToggleButton("<html><center><b>" + giorno + "</b><br>" + mese + "</center></html>");
                dayButton.setFont(new Font("Arial", Font.PLAIN, 24));
                dayGroup.add(dayButton);
                bottoniDataMap.put(dayButton, day);

                // Icona meteo
                JLabel weatherIcon = new JLabel();
                weatherIcon.setFont(new Font("Arial", Font.PLAIN, 32));
                weatherIcon.setIcon(getWeatherEmoji(weatherCodes[i]));
                weatherIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel meteoLabel = new JLabel(
                        "<html><center><span style='font-size:14px;'>Min: " + temperatureMin[i] + "<br>Max: " + temperatureMax[i] + "</span></center></html>"
                );
                meteoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                dayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                meteoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JPanel giornoPanel = new JPanel();
                giornoPanel.setLayout(new BoxLayout(giornoPanel, BoxLayout.Y_AXIS));
                giornoPanel.setOpaque(false);
                giornoPanel.add(weatherIcon);
                giornoPanel.add(dayButton);
                giornoPanel.add(Box.createVerticalStrut(5));
                giornoPanel.add(meteoLabel);

                dayPanel.add(giornoPanel);
                dayPanel.add(Box.createHorizontalStrut(10));
            }

            JScrollPane scrollPane = new JScrollPane(dayPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setBorder(null);

            String[] orari = {"08:00", "10:00", "12:00", "14:00", "16:00", "18:00"};
            JComboBox<String> oraComboBox = new JComboBox<>(orari);
            oraComboBox.setFont(new Font("Arial", Font.PLAIN, 28));

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
            bottomPanel.add(oraComboBox, BorderLayout.WEST);

            JButton inviaButton = new JButton("Invia");
            inviaButton.setFont(new Font("Arial", Font.BOLD, 28));
            bottomPanel.add(inviaButton, BorderLayout.EAST);

            inviaButton.addActionListener(h -> {
                ButtonModel selectedModel = dayGroup.getSelection();
                LocalDate dataSelezionata = null;

                for (Map.Entry<JToggleButton, LocalDate> entry : bottoniDataMap.entrySet()) {
                    if (entry.getKey().getModel() == selectedModel) {
                        dataSelezionata = entry.getValue();
                        break;
                    }
                }

                if (dataSelezionata != null) {
                    String selected = (String) oraComboBox.getSelectedItem();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    assert selected != null;
                    LocalTime orario = LocalTime.parse(selected, formatter);

                    LocalDate dataSelezionataMinusOne = dataSelezionata.minusDays(1);
                    Date dataVisita = java.sql.Date.valueOf(dataSelezionataMinusOne);
                    Visita visita = new Visita(immobile, orario, dataVisita);

                    if (control.getNameLogIn().equals("[CLIENT]")) {
                        ControllerClienti controllerClienti = new ControllerClienti();
                        if (controllerClienti.inviaVisita(visita, token)) {
                            BottoniOpzioni.getMapLog().put(LocalDateTime.now(), "Visita Richiesta il : " + visita.getDataVisita() + " alle " + visita.getOraVisita() + "\n Per " + immobile.getIndirizzo().toString());
                            JOptionPane.showMessageDialog(visitaDialog, "Richiesta inviata!");
                            visitaDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(visitaDialog, "Errore durante l'invio della richiesta.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(visitaDialog, "Per prenotare una visita bisogna essere un Cliente ");
                    }
                } else {
                    JOptionPane.showMessageDialog(visitaDialog, "Seleziona una data prima di inviare.");
                }
            });

            visitaDialog.add(scrollPane, BorderLayout.CENTER);
            visitaDialog.add(bottomPanel, BorderLayout.SOUTH);
            visitaDialog.setLocationRelativeTo(null);
            visitaDialog.setVisible(true);

        } else {
            frame.setContentPane(new LogIn(frame, control, token));
            frame.revalidate();
            frame.repaint();
        }
    }

    private static FontIcon getWeatherEmoji(int code) {
        if (code >= 0 && code <= 3) return IconClass.getSoleIcon();
        else if (code >= 45 && code <= 48) return IconClass.getNuvoleIcon();
        else if (code >= 51 && code <= 67) return IconClass.getPioggiaIcon();
        else if (code >= 71 && code <= 77) return IconClass.getNeveIcon();
        else if (code >= 80 && code <= 82) return IconClass.getSoleNuvoleIcon();
        else return IconClass.getNuvoleIcon();
    }

}

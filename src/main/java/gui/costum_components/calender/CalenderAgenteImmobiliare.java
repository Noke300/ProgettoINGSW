package gui.costum_components.calender;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
import controller.Controller;
import controller.ControllerAgente;
import gui.costum_components.IconClass;
import model.Cliente;
import model.Immobile;
import model.Visita;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
public class CalenderAgenteImmobiliare {
    private static final Map<String, List<Visita>> visitePerGiorno = new HashMap<>();
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static final JCalendar calendar = new JCalendar();
    // Sposta queste variabili a livello della classe (come static)
    private static DateColorManager dateColorManager;
    private static JDialog calendarPopup;

    private static boolean isPopupOpen = false;

    public static JPanel caricaCalendario(JButton buttonCalendario, ControllerAgente control, String token, JFrame frame) {
        calenderStarter(buttonCalendario, control, token, frame);

        JPanel controlPanel = new JPanel(new BorderLayout());
        JButton addDateButton = new JButton("Info Giorno");

        addDateButton.addActionListener(h ->
                mostraDettagliVisite(calendar.getDate(), format, frame, control, token, dateColorManager)
        );
        controlPanel.add(addDateButton, BorderLayout.SOUTH);

        return controlPanel;
    }

    private static void calenderStarter(JButton buttonCalendario, ControllerAgente control, String token, JFrame frame) {
        buttonCalendario.setIcon(IconClass.getCalendarioIcon());
        buttonCalendario.setText(LocalDate.now().toString());

        buttonCalendario.addActionListener(e -> {
            if (isPopupOpen) return; // 🔒 Evita doppie aperture
            isPopupOpen = true;

            calendar.setLocale(Locale.ITALY);
            JDayChooser dayChooser = calendar.getDayChooser();
             dateColorManager = new DateColorManager(dayChooser);

            visitePerGiorno.clear();
            List<Visita> visite = control.getVisite(token);
            List<ColoredDate> coloredDates = new ArrayList<>();

            for (Visita visita : visite) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(visita.getDataVisita());
                cal.add(Calendar.DAY_OF_MONTH, 1);
                Date adjustedDate = cal.getTime();
                visita.setDataVisita(adjustedDate);

                String key = format.format(adjustedDate);
                visitePerGiorno.computeIfAbsent(key, k -> new ArrayList<>()).add(visita);

                ColoredDate colored = new ColoredDate(adjustedDate, Boolean.FALSE.equals(visita.getConferma()));
                if (!coloredDates.contains(colored)) {
                    coloredDates.add(colored);
                }
            }

            dateColorManager.setColoredDates(coloredDates);
            dateColorManager.updateDayColors(true);

            calendar.addPropertyChangeListener(evt -> {
                if ("calendar".equals(evt.getPropertyName())) {
                    dateColorManager.updateDayColors(true);
                }
            });

            calendarPopup = new JDialog(frame, "Calendario", true);
            calendarPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            calendarPopup.setSize(700, 450);
            calendarPopup.setResizable(false);

            JPanel controlPanel = new JPanel(new BorderLayout());
            JButton addDateButton = new JButton("Info Giorno");
            addDateButton.addActionListener(h ->
                    mostraDettagliVisite(calendar.getDate(), format, frame, control, token, dateColorManager)
            );            controlPanel.add(addDateButton, BorderLayout.SOUTH);

            calendarPopup.add(calendar, BorderLayout.CENTER);
            calendarPopup.add(controlPanel, BorderLayout.SOUTH);
            calendarPopup.setLocationRelativeTo(frame);

            calendarPopup.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    visitePerGiorno.clear();
                    coloredDates.clear();
                    isPopupOpen = false; // 🔓 Riabilita apertura
                }
            });

            calendarPopup.setVisible(true);
        });
    }


    private static void mostraDettagliVisite(Date selectedDate, DateFormat format, JFrame frame,
                                             ControllerAgente controller, String token,
                                             DateColorManager dateColorManager) {

        String giornoChiave = format.format(selectedDate);
        List<Visita> visiteDelGiorno = visitePerGiorno.get(giornoChiave);

        if (visiteDelGiorno != null && !visiteDelGiorno.isEmpty()) {
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

            JDialog dettaglioDialog = new JDialog(frame, "Visite del " + giornoChiave, true);
            dettaglioDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dettaglioDialog.setSize(650, 450);
            dettaglioDialog.setResizable(false);
            dettaglioDialog.setLocationRelativeTo(frame);

            for (Visita visita : visiteDelGiorno) {
                Immobile imm = visita.getImmobile();
                Cliente cl = visita.getCliente();

                JPanel visitaPanel = new JPanel();
                visitaPanel.setLayout(new BoxLayout(visitaPanel, BoxLayout.Y_AXIS));
                visitaPanel.setBorder(BorderFactory.createTitledBorder("🕒 Visita alle " + visita.getOraVisita()));

                JTextArea info = new JTextArea(
                        "🏠 Tipo immobile: " + imm.getTipoImmobile() + "\n" +
                                "💸 Prezzo: €" + imm.getPrezzo() + "\n" +
                                "📍 Indirizzo: " + imm.getIndirizzo() + "\n" +
                                "👤 Cliente: " + cl.getNome() + " " + cl.getCognome() + "\n" +
                                "📌 Stato: " + (visita.getConferma() == null ? "⏳ In Attesa" : "✅ Confermata")
                );
                info.setFont(new Font("SansSerif", Font.PLAIN, 16));
                info.setEditable(false);
                visitaPanel.add(info);

                JPanel buttonPanel = new JPanel();

                if (visita.getConferma() == null) {
                    JButton accetta = new JButton("✅ Accetta");
                    JButton rifiuta = new JButton("❌ Rifiuta");

                    accetta.addActionListener(ev -> {
                        visita.setConferma(true);
                        if (controller.esitoVisita(token, visita)) {
                            JOptionPane.showMessageDialog(frame, "✅ Visita confermata.");
                        } else {
                            JOptionPane.showMessageDialog(frame, "❌ Qualcosa è andato storto.");
                        }
                        dateColorManager.updateDayColors(true);
                        dettaglioDialog.dispose();
                    });

                    rifiuta.addActionListener(ev -> {
                        visita.setConferma(false);
                        if (controller.esitoVisita(token, visita)) {
                            JOptionPane.showMessageDialog(frame, "❌ Visita rifiutata.");
                        } else {
                            JOptionPane.showMessageDialog(frame, "⚠️ Qualcosa è andato storto.");
                        }
                        dateColorManager.updateDayColors(true);
                        dettaglioDialog.dispose();
                    });

                    buttonPanel.add(accetta);
                    buttonPanel.add(rifiuta);

                } else if (visita.getConferma()) {
                    JButton annulla = new JButton("🔄 Annulla");

                    annulla.addActionListener(ev -> {
                        visita.setConferma(false); // Torna in stato "In Attesa"
                        if (controller.esitoVisita(token, visita)) {
                            JOptionPane.showMessageDialog(frame, "⏳ Visita annullata. Ora in attesa.");
                        } else {
                            JOptionPane.showMessageDialog(frame, "⚠️ Qualcosa è andato storto.");
                        }
                        dateColorManager.updateDayColors(true);
                        dettaglioDialog.dispose();
                    });

                    buttonPanel.add(annulla);
                }

                visitaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                visitaPanel.add(buttonPanel);
                containerPanel.add(visitaPanel);
            }

            JScrollPane scrollPane = new JScrollPane(containerPanel);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            dettaglioDialog.add(scrollPane);
            dettaglioDialog.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(frame, "📭 Nessuna visita per questo giorno.", "Informazioni", JOptionPane.INFORMATION_MESSAGE);
        }
    }




    public static JCalendar getCalendar() {
        return calendar;
    }

    public static DateFormat getFormat() {
        return format;
    }

    public static Map<String, List<Visita>> getVisitePerGiorno() {
        return visitePerGiorno;
    }
}

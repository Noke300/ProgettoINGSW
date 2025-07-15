package gui.costum_components.calender;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
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
    private static boolean isPopupOpen = false;

    public static JPanel caricaCalendario(JButton buttonCalendario, ControllerAgente control, String token, JFrame frame) {
        calenderStarter(buttonCalendario, control, token, frame);

        JPanel controlPanel = new JPanel(new BorderLayout());
        JButton addDateButton = new JButton("Info Giorno");

        addDateButton.addActionListener(h -> mostraDettagliVisite(calendar.getDate(), format, frame));
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
            DateColorManager dateColorManager = new DateColorManager(dayChooser);

            visitePerGiorno.clear();
            List<Visita> visite = control.getVisite(token);
            List<ColoredDate> coloredDates = new ArrayList<>();

            for (Visita visita : visite) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(visita.getDataVisita());
                cal.add(Calendar.DAY_OF_MONTH, 1); // ⚠️ Se non necessario, rimuovi questo offset
                Date adjustedDate = cal.getTime();
                visita.setDataVisita(adjustedDate);

                String key = format.format(adjustedDate);
                visitePerGiorno.computeIfAbsent(key, k -> new ArrayList<>()).add(visita);

                ColoredDate colored = new ColoredDate(adjustedDate, visita.getConferma());
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

            final JDialog calendarPopup = new JDialog(frame, "Calendario", true);
            calendarPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            calendarPopup.setSize(700, 450);
            calendarPopup.setResizable(false);

            JPanel controlPanel = new JPanel(new BorderLayout());
            JButton addDateButton = new JButton("Info Giorno");
            addDateButton.addActionListener(h -> mostraDettagliVisite(calendar.getDate(), format, frame));
            controlPanel.add(addDateButton, BorderLayout.SOUTH);

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

    private static void mostraDettagliVisite(Date selectedDate, DateFormat format, JFrame frame) {
        String giornoChiave = format.format(selectedDate);
        List<Visita> visiteDelGiorno = visitePerGiorno.get(giornoChiave);

        if (visiteDelGiorno != null && !visiteDelGiorno.isEmpty()) {
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

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
                                "✅ Confermata: " + (visita.getConferma() ? "✅ Sì" : "❌ No")
                );
                info.setFont(new Font("SansSerif", Font.PLAIN, 16));
                info.setEditable(false);
                visitaPanel.add(info);

                JPanel buttonPanel = new JPanel();
                if (!visita.getConferma()) {
                    JButton accetta = new JButton("✅ Accetta");
                    JButton rifiuta = new JButton("❌ Rifiuta");

                    accetta.addActionListener(ev -> {
                        visita.setConferma(true);
                        JOptionPane.showMessageDialog(frame, "Visita confermata.");
                    });
                    rifiuta.addActionListener(ev -> JOptionPane.showMessageDialog(frame, "Visita rifiutata."));
                    buttonPanel.add(accetta);
                    buttonPanel.add(rifiuta);
                } else {
                    JButton annulla = new JButton("🔄 Annulla");
                    annulla.addActionListener(ev -> {
                        visita.setConferma(false);
                        JOptionPane.showMessageDialog(frame, "Visita annullata.");
                    });
                    buttonPanel.add(annulla);
                }

                visitaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                visitaPanel.add(buttonPanel);
                containerPanel.add(visitaPanel);
            }

            JScrollPane scrollPane = new JScrollPane(containerPanel);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(frame, scrollPane, "Visite del " + giornoChiave, JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Nessuna visita per questo giorno.", "Informazioni", JOptionPane.INFORMATION_MESSAGE);
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

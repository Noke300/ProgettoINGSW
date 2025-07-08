package gui.costum_components;

import com.toedter.calendar.JDayChooser;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateColorManager {
    private final JDayChooser dayChooser;
    private List<ColoredDate> coloredDates = new ArrayList<>();
    private JButton[] dayButtons;

    public DateColorManager(JDayChooser dayChooser) {
        this.dayChooser = dayChooser;
        try {
            Field daysField = JDayChooser.class.getDeclaredField("days");
            daysField.setAccessible(true);
            dayButtons = (JButton[]) daysField.get(dayChooser);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERRORE CRITICO: Accesso ai bottoni dei giorni fallito: " + e.getMessage());
            dayButtons = null;
        }
    }

    public void setColoredDates(List<ColoredDate> dates) {
        this.coloredDates = new ArrayList<>(dates);
        updateDayColors();
    }

    public void updateDayColors() {
        if (dayButtons == null) return;

        // Reset all buttons to default
        for (JButton button : dayButtons) {
            if (button != null) {
                button.setBackground(null);
            }
        }

        try {
            // Get calendar from JDayChooser
            Field calendarField = JDayChooser.class.getDeclaredField("calendar");
            calendarField.setAccessible(true);
            Calendar currentCalendar = (Calendar) calendarField.get(dayChooser);

            if (currentCalendar == null) {
                System.err.println("Calendar is null");
                return;
            }

            int currentMonth = currentCalendar.get(Calendar.MONTH);
            int currentYear = currentCalendar.get(Calendar.YEAR);

            // Direct matching by button text
            for (int i = 0; i < dayButtons.length; i++) {
                JButton button = dayButtons[i];
                if (button != null && button.getText() != null && !button.getText().isEmpty()) {
                    try {
                        int day = Integer.parseInt(button.getText());

                        // Create a date for this button
                        Calendar buttonCalendar = Calendar.getInstance();
                        buttonCalendar.set(currentYear, currentMonth, day, 0, 0, 0);
                        buttonCalendar.set(Calendar.MILLISECOND, 0);
                        Date buttonDate = buttonCalendar.getTime();

                        // Check if this button's date matches any colored date
                        for (ColoredDate coloredDate : coloredDates) {
                            if (coloredDate.isSameDay(buttonDate)) {
                                button.setBackground(coloredDate.getColor());
                                break;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Not a number, probably an empty or special day cell
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in updateDayColors: " + e.getMessage());
        }

        dayChooser.repaint();
    }
}
package gui.costum_components.calender;

import gui.costum_components.IconClass;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class ColoredDate {
    private final IconClass iconSet = new IconClass();
    private final Date date;
    private final boolean confermato;
    private final Color color = iconSet.getMainColor();
    private final Color colorNonConfermati = Color.CYAN;


    public ColoredDate(Date date, boolean confermato) {
        // Normalize the date to avoid time-related issues
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.date = cal.getTime();

        this.confermato = confermato;
    }

    public Color getColor() {
        return color;
    }

    public Color getColorNonConfermati() {
        return colorNonConfermati;
    }

    public boolean isSameDay(Date otherDate) {
        if (otherDate == null) return false;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(this.date);
        cal2.setTime(otherDate);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}
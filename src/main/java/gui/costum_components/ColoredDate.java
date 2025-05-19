package gui.costum_components;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class ColoredDate {
    private final IconClass iconSet = new IconClass();
    private final Date date;
    private final Color color = iconSet.getMainColor();

    public ColoredDate(Date date) {
        // Normalize the date to avoid time-related issues
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.date = cal.getTime();
    }

    public Color getColor() {
        return color;
    }

    public Date getDate() {
        return (Date) date.clone();
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
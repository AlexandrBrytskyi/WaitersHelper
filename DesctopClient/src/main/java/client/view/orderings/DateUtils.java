package client.view.orderings;


import net.sourceforge.jdatepicker.impl.DateComponentFormatter;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import server.model.order.Ordering;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class DateUtils {


    public static SqlDateModel initDateWhenPeopleComePanel(JPanel datePanel, JComboBox hoursComboBox, JComboBox minutesComboBox) {

        SqlDateModel model = new SqlDateModel(Date.valueOf(LocalDate.now()));
        JDatePanelImpl panel = new JDatePanelImpl(model);
        JDatePickerImpl picker = new JDatePickerImpl(panel, new DateComponentFormatter());
        datePanel.add(picker);
        addItemsToHoursMinutesComboBox(hoursComboBox, minutesComboBox);
        return model;
    }

    public static void addItemsToHoursMinutesComboBox(JComboBox hoursComboBox, JComboBox minutesComboBox) {
        int[] hours = new int[24];
        for (int i = 0; i < hours.length; i++) {
            hours[i] = i;
        }
        int[] minutes = new int[60];
        for (int i = 0; i < minutes.length; i++) {
            minutes[i] = i;
        }
        for (int hour : hours) {
            hoursComboBox.addItem(hour);
        }
        for (int minute : minutes) {
            minutesComboBox.addItem(minute);
        }
        hoursComboBox.setSelectedIndex(LocalTime.now().getHour());
        minutesComboBox.setSelectedIndex(LocalTime.now().getMinute());
    }

    public static SqlDateModel initDateWhenPeopleComePanel(JPanel datePanel, JComboBox hoursComboBox, JComboBox minutesComboBox, Ordering ordering) {

        SqlDateModel model = new SqlDateModel(Date.valueOf(ordering.getDateClientsCome().toLocalDate()));
        JDatePanelImpl panel = new JDatePanelImpl(model);
        JDatePickerImpl picker = new JDatePickerImpl(panel, new DateComponentFormatter());
        datePanel.add(picker);
        addItemsToHoursMinutesComboBox(hoursComboBox, minutesComboBox,
                ordering.getDateClientsCome().toLocalTime().getHour(),
                ordering.getDateClientsCome().toLocalTime().getMinute());
        return model;
    }

    public static void addItemsToHoursMinutesComboBox(JComboBox hoursComboBox, JComboBox minutesComboBox, int hour, int minute) {
        int[] hours = new int[24];
        for (int i = 0; i < hours.length; i++) {
            hours[i] = i;
        }
        int[] minutes = new int[60];
        for (int i = 0; i < minutes.length; i++) {
            minutes[i] = i;
        }
        for (int hourr : hours) {
            hoursComboBox.addItem(hourr);
        }
        for (int minut : minutes) {
            minutesComboBox.addItem(minut);
        }
        hoursComboBox.setSelectedIndex(hour);
        minutesComboBox.setSelectedIndex(minute);
    }
}

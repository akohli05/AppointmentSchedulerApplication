package com.example.c195pa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for the Months of the year
 *
 * @author Aimy Kohli
 */
public class Months {
    //Variable
    private String monthName;
    private int noAppointmentTotal;

    /**
     * Constructor
     * @param monthName The month name
     * @param noAppointmentTotal  The var for holding the appointment total
     */
    public Months(String monthName, int noAppointmentTotal) {
        this.monthName = monthName;
        this.noAppointmentTotal = noAppointmentTotal;
    }

    /**
     * Setter
     * @param monthName The month name
     */
    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    /**
     * Setter
     * @param noAppointmentTotal The var for holding the appointment total
     */
    public void setNoAppointmentTotal(int noAppointmentTotal) {
        this.noAppointmentTotal = noAppointmentTotal;
    }

    /**
     * Getter
     * @return The month name
     */
    public String getMonthName() {
        return monthName;
    }

    /**
     * Getter
     * @return The var for holding the appointment total
     */
    public int getNoAppointmentTotal() {
        return noAppointmentTotal;
    }

}

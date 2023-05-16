package com.example.c195pa.model;

/**
 * Model class for the reports
 * Class that will help fill out the reports tableview
 * Relating to appointment by month total
 *
 * @author Aimy Kohli
 */

public class ReportsMonthTotal {
    //Variables
    private String appointmentMonth;
    private int appointmentMonthTotal;

    /**
     * Constructor
     * @param appointmentMonth The appointment month
     * @param appointmentMonthTotal The total number of appointments per month
     */
    public ReportsMonthTotal(String appointmentMonth, int appointmentMonthTotal) {
        this.appointmentMonth = appointmentMonth;
        this.appointmentMonthTotal = appointmentMonthTotal;
    }

    /**
     * Setter
     * @param appointmentMonth The appointment month
     */
    public void setAppointmentMonth(String appointmentMonth) {
        this.appointmentMonth = appointmentMonth;
    }

    /**
     * Setter
     * @param appointmentMonthTotal The total number of appointments per month
     */
    public void setAppointmentMonthTotal(int appointmentMonthTotal) {
        this.appointmentMonthTotal = appointmentMonthTotal;
    }

    /**
     * Getter
     * @return The appointment month
     */
    public String getAppointmentMonth() {
        return appointmentMonth;
    }

    /**
     * Getter
     * @return The total number of appointments per month
     */
    public int getAppointmentMonthTotal() {
        return appointmentMonthTotal;
    }

}

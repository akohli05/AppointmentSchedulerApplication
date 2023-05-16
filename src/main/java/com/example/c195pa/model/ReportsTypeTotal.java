package com.example.c195pa.model;

/**
 * Model class for the reports
 * Class that will help fill out the reports tableview
 * Relating to appointment by month total
 *
 * @author Aimy Kohli
 */

public class ReportsTypeTotal {
    //Variables
    private String appointmentType;
    private int appointmentTypeTotal;

    /**
     * Constructor
     * @param appointmentType The appointment type
     * @param appointmentTypeTotal The total number of appointments by type
     */
    public ReportsTypeTotal(String appointmentType, int appointmentTypeTotal) {
        this.appointmentType = appointmentType;
        this.appointmentTypeTotal = appointmentTypeTotal;
    }

    /**
     * Setter
     * @param appointmentType The appointment type
     */
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /**
     * Setter
     * @param appointmentTypeTotal The total number of appointments by type
     */
    public void setAppointmentTypeTotal(int appointmentTypeTotal) {
        this.appointmentTypeTotal = appointmentTypeTotal;
    }

    /**
     * Getter
     * @return The appointment type
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * Getter
     * @return The total number of appointments by type
     */
    public int getAppointmentTypeTotal() {
        return appointmentTypeTotal;
    }

}

package com.example.c195pa.model;

/**
 * Model class for reports
 * Contains information relating to reports
 * @author Aimy Kohli
 */
public class ReportsCustomerAppTotal {
    //Variables
    private int customerID;
    private String customerName;
    public int appointmentTotal;

    /**
     * Constructor
     * @param customerID The customer id
     * @param customerName The customer name
     * @param appointmentTotal The total number of appointments by customer
     */
    public ReportsCustomerAppTotal(int customerID, String customerName, int appointmentTotal) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.appointmentTotal = appointmentTotal;
    }

    /**
     * Setter
     * @param customerID The total of each country
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Setter
     * @param customerName The country name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Setter
     * @param appointmentTotal The total number of appointments by customer
     */
    public void setAppointmentTotal(int appointmentTotal) {
        this.appointmentTotal = appointmentTotal;
    }

    /**
     * Getter
     * @return The total of each country
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Getter
     * @return The country name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Getter
     * @return The total number of appointments by customer
     */
    public int getAppointmentTotal() {
        return appointmentTotal;
    }
}

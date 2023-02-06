/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 *
 * @author daviddaillere
 */
public class booking {

    private String bookingType;
    private String propertyType;
    private String rentalName;
    private String checkIn;
    private String timeIn;
    private String checkOut;
    private String timeOut;
    private String comment;
    private String propertyAdress;

    private int id;
    private int clientID;
    private int adult;
    private int child;
    private int singleBed;
    private int doubleBed;
    private int numberOfStay;
    private int option;
//total of the booking without option or tax
    private int initialAmount;
    private int totalAmount;
    private double paid;
    private double toPay;
//total including all option and tax
    private double grandTotal;
    private int paiementId;

    private boolean contractStatus;
    private boolean advanceStatus;
    private double tax;
    private double baseTax;

    private String contractName;
    private String factureName;

    private int advanceAmount;
    private boolean boundStatus;
    private String boundAmount;

    /**
     *
     */
    public booking() {
    }

    /**
     *
     * @return id booking Id use to call or create a booking
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return clientId the client link to this booking
     *
     */
    public int getClientID() {
        return clientID;
    }

    /**
     *
     * @return bookingType of booking( direct or from other website)
     */
    public String getBookingType() {
        return bookingType;
    }

    /**
     *
     * @return property Type: (house, room or dorm)
     */
    public String getPropertyType() {
        return propertyType;
    }

    /**
     *
     * @return the name of the rental place
     */
    public String getRentalName() {
        return rentalName;
    }

    /**
     *
     * @return checkIn date in a string format
     */
    public String getCheckIn() {
        return checkIn;
    }

    /**
     *
     * @return min time of arrival in a string format
     */
    public String getTimeIn() {
        return timeIn;
    }

    /**
     *
     * @return max time of departure in a string format
     */
    public String getTimeOut() {
        return timeOut;
    }

    /**
     *
     * @return checkOut date in a string format
     */
    public String getCheckOut() {
        return checkOut;
    }

    public String getComment() {
        return comment;
    }

    /**
     *
     * @return the property adress
     */
    public String getPropertyAdress() {
        return propertyAdress;
    }

    /**
     *
     * @return number of adults
     */
    public int getAdults() {
        return adult;
    }

    /**
     *
     * @return number of children
     */
    public int getChildren() {
        return child;
    }

    /**
     *
     * @return number of single bed
     */
    public int getSingleBed() {
        return singleBed;
    }

    /**
     *
     * @return number of double bed
     */
    public int getDoubleBed() {
        return doubleBed;
    }

    /**
     *
     * @return number of stay
     */
    public int getNumberOfStay() {
        return numberOfStay;
    }

    /**
     *
     * @return total option amount
     */
    public int getTotalOption() {
        return option;
    }

    /**
     *
     * @return the paiement method ID: link to paiement method list
     */
    public int getPaiementId() {
        return paiementId;
    }
    
    /**
     *
     * @return price of booking without tax or options
     */
    public int getInitalAmount() {
        return initialAmount;
    }

    /**
     *
     * @return price of booking without tax or options
     */
    public int getTotalAmount() {
        return totalAmount;
    }

    /**
     *
     * @return the amount paid so far
     */
    public double getPaid() {
        return paid;
    }

    /**
     *
     * @return te amount no paid yet
     */
    public double getToPay() {
        return toPay;
    }

    /**
     *
     * @return booking amount all included
     */
    public double getGrandTotal() {
        return grandTotal;
    }

    public void setBookingType(String bookingOrigin) {
        bookingType = bookingOrigin;
    }

    public void setPropertyType(String bookingType) {
        propertyType = bookingType;

    }

    public void setRentalName(String propertyName) {
        rentalName = propertyName;

    }

    public void setCheckIn(String bookingCheckIn) {
        checkIn = bookingCheckIn;
    }

    public void setTimeIn(String bookingTimeIn) {
        timeIn = bookingTimeIn;
    }

    public void setCheckOut(String bookingCheckOut) {
        checkOut = bookingCheckOut;
    }

    public void setTimeOut(String bookingTimeOut) {
        timeOut = bookingTimeOut;
    }

    public void setComment(String bookingComment) {
        comment = bookingComment;
    }

    public void setPropertyAdress(String bookingAdress) {
        propertyAdress = bookingAdress;
    }
    
    

    public void setId(int bookingId) {
        id = bookingId;
    }

    public void setClientId(int bookingClientId) {
        clientID = bookingClientId;
    }

    public void setAdult(int adults) {
        adult = adults;
    }

    public void setChildren(int children) {
        child = children;
    }

    public void setSingleBed(int single) {
        singleBed = single;
    }

    public void setDoubleBed(int Double) {
        doubleBed = Double;
    }

    public void setNumberOfStay(int NumberOfDays) {
        numberOfStay = NumberOfDays;
    }
    


    public void setTotalOption(int totalOption) {
        option = totalOption;
    }
    
    public void setInitialAmount(int amount) {
        initialAmount = amount;
    }

    public void setTotalAmount(int totalBooking) {
        totalAmount = totalBooking;
    }

    public void setPaiementId(int paiementID) {
        paiementId = paiementID;
    }

    public void setPaid(double amountPaid) {
        paid = amountPaid;
    }

    public void setToPay(double amountToPay) {
        toPay = amountToPay;
    }

    public void setGrandTotal(double GrandTotal) {
        grandTotal = GrandTotal;
    }

    public boolean getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(boolean b) {
        contractStatus = b;
    }

    public boolean getAdvanceStatus() {
        return advanceStatus;
    }

    public void setAdvanceStatus(boolean b) {
        advanceStatus = b;
    }

    public double getTax() {
        return tax;
    }

    public double getTaxBase() {
        return baseTax;
    }

    public void setTaxBase(double taxBase) {
        baseTax = taxBase;
    }
    
    public void setTax(double Tax) {
        tax = Tax;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String fileName) {
        contractName = fileName;
    }

    public String getBillName() {
        return factureName;
    }

    public void setBillName(String fileName) {
        factureName = fileName;
    }

    public int getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(int amount) {
        advanceAmount = amount;
    }

    public boolean getBoundStatus() {
        return boundStatus;
    }

    public void setBoundStatus(boolean b) {
        boundStatus = b;
    }

    public String getBoundAmount() {
        return boundAmount;
    }

    public void setBoundAmount(String amount) {
        boundAmount = amount;
    }

}

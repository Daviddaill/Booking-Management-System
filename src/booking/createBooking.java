/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package booking;

import static java.lang.Math.floor;
import project.InsertUpdateDelete;

/**
 *
 * @author daviddaillere
 */
public class createBooking extends booking {
  
private String db; 
private double pricePerDay;
    
    public createBooking(String dataBase,
                        int clientId,
                        String bookingType, 
                        String propertyType, 
                        String checkIn,
                        String timeIn,
                        String checkOut,
                        String timeOut,
                        int numberOfStay,
                        int adult,
                        int children,
                        int SingleBed,
                        int doubleBed,
                        String rentalName,
                        int amount,
                        double tax, 
                        String bound,
                        int optionAmount){
        db= dataBase;
        this.setClientId(clientId);
        this.setBookingType(bookingType);
        this.setPropertyType(propertyType);
        this.setCheckIn(checkIn);
        this.setTimeIn(timeIn);
        this.setCheckOut(checkOut);
        this.setTimeOut(timeOut);
        this.setNumberOfStay(numberOfStay);
        this.setAdult(adult);
        this.setChildren(children);
        this.setSingleBed(SingleBed);
        this.setDoubleBed(doubleBed);
        this.setRentalName(rentalName);
        this.setInitialAmount(amount);
        this.setTaxBase(tax);
        this.setBoundAmount(bound);
        this.setTotalOption(optionAmount);
        
    }
    
    public void processBooking(){
//calculate price of booking                
                    double amountStay = 0;
                    String propertyType= this.getPropertyType();
                    int initialAmount= this.getInitalAmount();
                    int numberOfStay= this.getNumberOfStay();
                    pricePerDay= 0;
//if property is a house total amount is same as initial amount,                  
                    if (propertyType.equals("Maison")) {
                        this.setTotalAmount(initialAmount);
                        pricePerDay= Math.round(amountStay / numberOfStay);
                    }
// if property is a room, total amount is initial price * number of days                    
                    if (propertyType.equals("Chambre")) {
                        this.setTotalAmount(initialAmount * numberOfStay);
                        pricePerDay = initialAmount;
                        
                    }
// if property is a dorm total amount is initial price * number of days * number of people                    
                    int totalPeople = this.getAdults() + this.getChildren();
                    if (propertyType.equals("Dortoir")) {
                        this.setTotalAmount((initialAmount * totalPeople) * numberOfStay);
                        pricePerDay = initialAmount;
                    }
                    

                    this.setTax(Math.round(this.getTaxBase() * 100.0) / 100.0);
                    
                    double totalWithTax = amountStay + this.getTax();

                    //if advance smaller than 20 and total price smaller than 30 advance is total price, if total price is between 30 and 70 advance is 20
                    //set arres value to 30% of the booking amountStay (tax and options excluded) round down to tenths.    
                    double advance = amountStay * 0.30;
                    advance = (10 * floor(advance / 10));
                    if (advance < 20) {
                        if (totalWithTax < 30) {
                            advance = totalWithTax;
                        }
                        if (totalWithTax > 30 && amountStay < 70) {
                            advance = 20;
                        }
                    }
                    
                    
                    //add option to TOTAL
                    totalWithTax += this.getTotalOption();
                    this.setGrandTotal(totalWithTax);     
                    saveBooking();
                    
    }
 
    
public void saveBooking(){
    
    String propertyType= this.getPropertyType();
    int clientId= this.getClientID();
    String bookingType= this.getBookingType();
    String checkIn= this.getCheckIn();
    String timeIn= this.getTimeIn();
    String checkOut= this.getCheckOut();
    String timeOut= this.getTimeOut();
    
            
    
//save in db the new booking;                    
                    String Query="";
                    if (propertyType.equals("Maison")) {
                        Query = "insert into booking ( clientID, checkIn, roomName,adult , child, singleBed, doubleBed, bookingType, pricePerDay, checkOut,timeIn, timeOut, numberOfStay,myOption, totalAmount, bound, status, contratStatus, boundStatus, advanceStatus, paid, toPay, tax, baseTax, totalWithTax, addressProperty, advanceAmount) values('" + this.getClientID() + "','" + this.getCheckIn() + "','" + this.getRentalName().replace("\'", "\'\'") + "','" + this.getAdults() + "','" + this.getChildren() + "','" + this.getSingleBed() + "','" + this.getDoubleBed() + "','" + bookingType + "','" + pricePerDay + "','" + this.getCheckOut() + "','" + timeIn + "','" + timeOut + "','" + this.getNumberOfStay() + "','" + this.getTotalOption() + "','" + this.getTotalAmount() + "','" + this.getBoundAmount() + "','en cours', 'false','false','false','0','" + this.getGrandTotal() + "','" + this.getTax() + "','" + this.getTaxBase() + "','" + this.getGrandTotal() + "','" + this.getPropertyAdress() + "','" + this.getAdvanceAmount() + "')";
                        InsertUpdateDelete.setData(db, Query, "Félicitaion! , Nouvelle réservation enregistrée avec succès :) ");
                    } else {
                        Query = "insert into booking ( clientID, checkIn, roomName, adult, child, singleBed, doubleBed,  bookingType, pricePerDay, checkOut, timeIn, timeOut, numberOfStay, status, contratStatus, advanceStatus, myOption, totalAmount,tax,baseTax, totalWithTax, paid, toPay, addressProperty, advanceAmount) values('" + this.getClientID()+ "','" + bookingDate.getCheckIn() + "','" + this.getRentalName() + "','" + this.getAdults() + "','" + this.getChildren()+ "','" + this.getSingleBed() + "','" + this.getDoubleBed()+ "','" + bookingType + "','" + pricePerDay + "','" + this.getCheckOut() + "','" + timeIn + "','" + timeOut + "','" + this.getNumberOfStay() + "','en cours','false','false','" + this.getTotalOption() + "','" + this.getTotalAmount() + "','" + this.getTax() + "','" + this.getTaxBase() + "','" + this.getGrandTotal() + "','0','" + this.getGrandTotal() + "','" + this.getPropertyAdress() + "','" + this.getAdvanceAmount() + "')";
                        InsertUpdateDelete.setData(db, Query, "Félicitaion! , Nouvelle réservation enregistrée avec succès :) ");
                    }
    }
    
 
    
}

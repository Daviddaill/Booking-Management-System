/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package booking;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import project.select;
import externalBooking.*;

/**
 *
 * @author daviddaillere
 */
public class bookingType {

    //type of booking: direct or externalBooking
    String bookingType;
    //type of property: house, room or dorm
    int type;
    String db;
    String name;
    String amount;
    String bound;
    String tax;
    String bookingAdress;

    /**
     *
     * @param dataBase
     * @param BookingType
     * @param propertyType
     * @param unitName
     * 
     * first, create a booking type object giving all necessary parameter, then setInitialPrice to calculate the pricing of a booking
     */
    public bookingType(String dataBase, String BookingType, int propertyType, String unitName) {
        db = dataBase;
        type = propertyType;
        name = unitName;
        bookingType = BookingType;
    }

    /**
     * set the initial booking prices and property address according to the
     * bookingType, propertyType and property name given
     *
     * @param numberOfStay
     */
    public void setInitialPrice(int numberOfStay) {
        try {
//search in database the property with the corresponding type and name
            
//if property is a house, amount is the price for the entire stay           
            if (type == 0) {
                ResultSet rs = select.getData(db, "select * from Room where building= '" + name + "' and bookingType='Maison'");
                while (rs.next()) {
                    //get the room information from data base,  
                    int price = Integer.parseInt(rs.getString("price"));
                    int totalAmount = price * numberOfStay;
                    amount = String.valueOf(totalAmount);
                    bound = rs.getString("bound");

                    tax = rs.getString("tax");

                    bookingAdress = rs.getString("adress");

                }
                rs.close();
            }
//if porperty type is a room amount is price per day
            if (type == 1) {
                if (name != null) {
                    int index = name.indexOf("-") + 1;
                    name = name.substring(index);
                    name = name.replace("\'", "\'\'");
                }
                ResultSet rs = select.getData(db, "select * from Room where roomName= '" + name + "'");
                while (rs.next()) {
                    //get the room information from data base,  
                    int price = Integer.parseInt(rs.getString("price"));
                    amount = String.valueOf(price);
                    tax = rs.getString("tax");

                    bookingAdress = rs.getString("adress");

                }
                rs.close();
            }
// if property type is a dorm, amount value is the price per day           
            if (type == 2) {
                if (name != null) {
                    int index = name.indexOf("-") + 1;
                    name = name.substring(index);
                    name = name.replace("\'", "\'\'");
                }
                ResultSet rs = select.getData(db, "select * from Room where roomName= '" + name + "'");
                while (rs.next()) {
                    //get the room information from data base,  
                    int price = Integer.parseInt(rs.getString("price"));
                    amount = String.valueOf(price);

                    tax = rs.getString("tax");

                    bookingAdress = rs.getString("adress");

                }

                rs.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
// if booking isn't a direct booking tax = 0        
        if (bookingType != null) {
            if (!bookingType.equals("Direct")) {
                tax = "0";
            }
        }

    }
    
    

    /**
     *
     * @return the bound amount 
     */
    public String getBound() {
        return bound;
    }

    /**
     *
     * @return tax of stay. there is no tax for all non direct booking
     */
    public String getTax() {
        return tax;
    }

    /**
     *
     * @return the property address of the booking
     */
    public String getAdress() {
        return bookingAdress;
    }

    /**
     *
     * @return the initial price of the booking,
     * initial amount for a house is the entire stay, for room and dorm it is the price per night.
     * it makes it easier for users to think that way. 
     */
    public String getAmount() {
        return amount;
    }

}

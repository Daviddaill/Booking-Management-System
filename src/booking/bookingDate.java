/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package booking;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 *
 * @author daviddaillere
 */
public class bookingDate {
    
    private static String checkIn;
    private static String checkOut;
    
    public static int getNumberOfStay(Date dateIn, Date dateOut){
        
// if check in and check out are set to same date. number of stay =1
        int numberOfStay=0;
        checkIn = new SimpleDateFormat("dd-MM-yy").format(dateIn);
        if (dateOut != null) {
            
            checkOut = new SimpleDateFormat("dd-MM-yy").format(dateOut);
            if (checkIn.equals(checkOut)) {
                return 1;
                
            } else {
//else, number of stay is difference between checkout and checkIn return numberOfStay
                try {
                    long diffInMillies = Math.abs(dateIn.getTime() - dateOut.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    numberOfStay = (int) diff;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
        return numberOfStay;
    }
    
    public static String getCheckIn(){
        return checkIn;
    }
    public static String getCheckOut(){
        return checkOut;
    }
}

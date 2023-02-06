/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package email;

import java.io.File;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import email.GmailOperations.*;
import project.select;
import document.document;
import email.standardOperation.*;

/**
 *
 * @author daviddaillere
 */
public class sendEmail {

    private static String from;
    private static String to;
    private static String billPath;
    private static String contractPath;
    private static boolean gmail;

    /* public newBooking(String db, int bookingID){
        build(db, bookingID);
   }*/
    public static void send(String db, int bookingID, String object, String msge) {
        getInfo(db, bookingID);

        msge = document.replaceInfoFromString(db, bookingID, msge);
        if (gmail) {
            try {
                GmailOperations.send(to, from, object, msge);
                JOptionPane success = new JOptionPane();
                success.showMessageDialog(null, "Email envoyé");
            } catch (Exception ex) {
                //Logger.getLogger(bookings.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Email non reçu!");
            }
        } else {
            standardOperation.sendMessage(db, to, object, msge);
        }

    }

    public static void sendWithContract(String db, int bookingID, String object, String msge) {
        getInfo(db, bookingID);

        msge = document.replaceInfoFromString(db, bookingID, msge);
        if (gmail) {
            try {
                GmailOperations.send(to, from, object, msge, new File(contractPath), new File(document.getGeneralConditions(db)));
                JOptionPane success = new JOptionPane();
                success.showMessageDialog(null, "Email envoyé");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Email non reçu!");
            }
        } else {
            standardOperation.sendMessage(db, to, object, msge, new File(contractPath), new File(document.getGeneralConditions(db)));
        }

    }

    public static void sendWithBill(String db, int bookingID, String object, String msge) {
        getInfo(db, bookingID);

        msge = document.replaceInfoFromString(db, bookingID, msge);
        if (gmail) {
            try {
                GmailOperations.send(to, from, object, msge, new File(billPath));
                JOptionPane success = new JOptionPane();
                success.showMessageDialog(null, "Email envoyé");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Email non reçu!");
            }
        } else {
            standardOperation.sendMessage(db, to, object, msge, new File(billPath));
        }

    }

    private static void getInfo(String db, int bookingID) {
        from = "";
        to = "";

        billPath = "";
        contractPath = "";
        ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID where booking.ID = '" + bookingID + "' ");
        ResultSet rs2 = select.getData(db, "select * from myInfo  where ID = '1' ");

        try {
            while (rs.next()) {
                billPath = document.getBillPath(db) + rs.getString("booking.factureName");
                contractPath = document.getContratPath(db) + rs.getString("booking.contractName");
                to = rs.getString("client.email");
            }
            rs.close();

            while (rs2.next()) {
                from = rs2.getString("email");
                if (from.contains("@gmail")) {
                    gmail = true;
                } else {
                    gmail = false;
                }

            }
            rs2.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static String getFrom() {
        return from;
    }

    public static String getTo() {
        return to;
    }

    public static String getbillPath() {
        return billPath;
    }

    public static String getcontractPath() {
        return contractPath;
    }

    public static File getBill() {

        return new File(billPath);

    }

    public static File getcontract() {
        return new File(contractPath);
    }
}

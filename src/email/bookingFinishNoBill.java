/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package email;

import document.document;
import java.io.File;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import project.select;

/**
 *
 * @author daviddaillere
 */
public class bookingFinishNoBill {

    private static String subject;
    private static String msge;


    public bookingFinishNoBill(String db, int bookingID) {
        document.initFields(db, bookingID);
        build(db, bookingID, 7);
    }

    public void build(String db, int bookingID, int type) {

        subject = "";
        msge = "";
        ResultSet rs3 = select.getData(db, "select * from documents  where name = '" + type + "' ");
        try {
            while (rs3.next()) {
                subject = rs3.getString("object");
            }
            rs3.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        var typeStrg = String.valueOf(type);
        
        msge = document.replaceInfoFromDoc(db, bookingID, typeStrg);

    }

    public static String getSubject() {
        return subject;
    }

    public static String getMsge() {
        return msge;
    }
}

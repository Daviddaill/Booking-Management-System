
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import java.util.HashMap;
import javax.swing.text.DefaultEditorKit;
import java.awt.Image;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import document.bill;
import document.contract;
import document.envelop;
import java.awt.Color;
import java.awt.Cursor;
import java.util.Locale;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import project.select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import project.InsertUpdateDelete;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.StringFormatter;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.io.IOException;
import static java.lang.Math.floor;
import java.nio.file.Files;
import java.nio.file.Paths;
import project.backup;
import project.excelFile;
import person.manageClient;
import person.client;
//import person.person;
import person.searchClient;
import person.owner;
import email.*;
import externalBooking.*;
import booking.*;
import document.document;
import static email.GmailOauth.GmailService;
import java.security.GeneralSecurityException;
import email.GmailOauth;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author daviddaillere
 */
public class mainPage extends javax.swing.JFrame {

    //db is set at Simply page and will be use everytime sql is called to determine wich database to operate on
    private String db;
    //bookingID is used to refer to the current booking in use (initiated every time we call a new booking)
    private int bookingID;
    //variable used in mes "reservations/en cours/ reservations" to refer to the clientID linked to the current bookingID in use
    private int clientBookingID;
    //storedRoom map is used in mon espace/ mes location/ maison. to save all rooms set by the user and then later used to insert into the corresponding database according to users decision
    private HashMap<String, ArrayList<String>> storedRooms;
    private int rooms;
    private int currentRoom;
    private int singleBedHouse = 0;
    private int doubleBedHouse = 0;
    private String unitName;
    private String clientName;
    private String clientFirstName;
    private int clientID;
    //check new client is use in reservation/ nouveau. to determine if a client's name is new (0), existing (1), or multiple client with same name(2)
    private int checkNewClient;
    private String bookingAddress;
    private String path = null;
    private Process pr = null;
    private person.searchClient sc;
    // 1= registered, 2= registered(no contract), 3= end booking( no bill), 4= end booking (with bill)
    private int emailType;
    //0= new booking, 1= current, 2= terminated;
    private int bookingStatus;

    /**
     * Creates new form Home
     *
     * @param database
     */
    public mainPage(String database) {
        initComponents();
        allPanelNotVisible();
        db = database;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height - 30);
        labelNotVisible();

    }

    public void initHome() {
        System.out.println("init mon espace");
        homePanel.setVisible(true);
        booking11.setVisible(false);
        homePanelsNotVisible();
        homeLabelNotVisible();

    }

    public void initBooking() {
        System.out.println("init mes reservations");
        booking11.setVisible(true);
        homePanel.setVisible(false);
        bookingPanelsNotVisible();
        bookingLabelNotVisible();
    }

    public void allPanelNotVisible() {

        homePanel.setVisible(false);
        booking11.setVisible(false);
    }

    public mainPage() {
        System.out.println("init mainPage");
        initComponents();
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    //set the main page's labels selector to not visible
    public void labelNotVisible() {
        jLabelHomeSelector.setVisible(false);
        jLabelBookingSelector.setVisible(false);
    }

    public void currClient(String name, String firstName) {
        searchClient sc = new searchClient(db, name, firstName);

    }

    public void homeInitMyRentals(String database) {
        System.out.println("init mon espace/ mes locations");
        myRentalPanel.setVisible(true);
        storedRooms = new HashMap<String, ArrayList<String>>();
        housePanel.setVisible(false);
        dormPanel.setVisible(false);
        roomHousePanel.setVisible(false);
        helpPanel.setVisible(false);
        selectTypeBox.requestFocus();
    }

    public void homeInitEmailPanel() {
        System.out.println("init home/ email panel");
        homePanelsNotVisible();
        createEmailPanel.setVisible(true);
        jComboBox2.setSelectedIndex(0);

    }

    public void homeInitMyInfo() {
         ResultSet rs = select.getData(db, "select * from myInfo where ID= 1");
            try {
                if (! rs.next()) {
                 String query = "insert into myInfo ( ID,company,name,firstName, tel,tel2, email,password, street,street2, district, cp, city, idCompany,APEcode, website, facebook, instagram, google, tripAdvisor, otherReview) values('1','','','','','','','','','','','','','','','','','','','','')";
                 InsertUpdateDelete.setData(db, query, "");   

                }
                rs.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        owner o = new owner(db);
        myCompanyField.setText(o.getCompany());
        myNameField.setText(o.getName());
        myFirstNameField.setText(o.getFirstName());
        myPhoneField.setText(o.getTel1());
        myPhone2Field.setText(o.getTel2());
        myEmailField.setText(o.getEmail());
        myStreet1Field.setText(o.getStreet1());
        myDcField.setText(o.getDc());
        myPostCodeField.setText(o.getPostCode());
        myCityField.setText(o.getCity());
        myStreet2Field.setText(o.getStreet2());
        mySiretField.setText(o.getSiret());
        myApeField.setText(o.getApe());
        websiteField.setText(o.getWebsite());
        facebookField.setText(o.getFacebook());
        instaField.setText(o.getInsta());
        googletField.setText(o.getGoogle());
        tripAdvisorField.setText(o.getTripAdvisor());
        otherReviewField.setText(o.getOtherReview());
        myLogoField.setText(o.getLogo());
        jPasswordField1.setText(o.getEmailPassword());
        //set "importé checkbox to selected if there is a name set in the logo name textfield; set to not selected if the textfield is empty
        if (!myLogoField.getText().isEmpty()) {
            jCheckBox2.setSelected(true);
        } else {
            jCheckBox2.setSelected(false);
        }
    }

    private void homeInitOption() {
        System.out.println("init mon espace/ option");
        //build the option table
        DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
        model.setRowCount(0);
        jTable6.getColumnModel().getColumn(0).setPreferredWidth(15);
        jTable6.getColumnModel().getColumn(2).setPreferredWidth(20);
        jTable6.getColumnModel().getColumn(2).setPreferredWidth(30);
        jScrollPane8.getViewport().setBackground(Color.white);

        //search for all option stored in myOption table and set every option in the jTable
        ResultSet rs = select.getData(db, "select * from myOption");
        try {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("ID"), rs.getString("name"), rs.getString("amount"), rs.getBoolean("dailyRate")});

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //set all textfields and checkbox to null in myOption panel.
        homeResetOption();

    }

    private void homeInitExternalBooking() {
        System.out.println("init mon espace/ option");
        //build the option table
        DefaultTableModel model = (DefaultTableModel) jTable7.getModel();
        model.setRowCount(0);
        jScrollPane10.getViewport().setBackground(Color.white);

        //search for all option stored in myOption table and set every option in the jTable
        ResultSet rs = select.getData(db, "select * from externalBooking");
        try {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("name")});
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //set all textfields and checkbox to null in myOption panel.
        homeResetExternalBooking();

    }

    public void homeInitYearStats() {

//put all existing property in the select property box
        ResultSet rs1 = select.getData(db, "select * from room ");
        String building = "";
        ArrayList<String> list1 = new ArrayList<String>();
        int count = jComboBox1.getItemCount();
        for (int i = 0; i < count; i++) {
            list1.add(jComboBox1.getItemAt(i));
        }
        try {
            while (rs1.next()) {
                building = rs1.getString("building");
                if (!list1.contains(building)) {
                    jComboBox1.addItem(building);
                    list1.add(building);
                }
            }
            rs1.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

//fill the table with all inforation from finsihed booking according to years       
        String in = "";
        float night = 0;
        float people = 0;
        float adult = 0;
        float children = 0;
        float tax = 0;
        float total = 0;

        HashMap<String, ArrayList<Float>> map = new HashMap<String, ArrayList<Float>>();
        ArrayList<Float> list = new ArrayList<Float>();
        ResultSet rs;
        //get all  booking stored in sql booking table if comboBox1 is set to "tout voir".
        if (jComboBox1.getSelectedItem().equals("tout voir")) {
            rs = select.getData(db, "select * from booking");
        } else {
            //get all booking of the selected property in sql booking table .
            rs = select.getData(db, "select * from booking where roomName LIKE '" + jComboBox1.getSelectedItem() + "%'");
        }

        try {

            while (rs.next()) {
                //add in the statistiques table only the booking with a tax            
                double checkTax = Double.parseDouble(rs.getString("baseTax"));
                if (checkTax != 0) {
                    if (rs.getString("status").equals("terminé") || rs.getString("status").equals("en cours")) {
                        in = new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("booking.checkIn"));
                        int length = in.length();
                        if (length != -1) {
                            in = in.substring(length - 2, length);
                        }

                        night = rs.getInt("numberOfStay");
                        people = rs.getInt("adult") + rs.getInt("child");
                        adult = rs.getInt("adult") * night;
                        children = rs.getInt("child") * night;
                        tax = rs.getFloat("tax");
                        total = rs.getFloat("totalWithTax");
                        if (map.containsKey(in)) {
                            list = map.get(in);
                            list.set(0, list.get(0) + night);
                            list.set(1, list.get(1) + people);
                            list.set(2, list.get(2) + adult);
                            list.set(3, list.get(3) + children);
                            list.set(4, list.get(4) + tax);
                            list.set(5, list.get(5) + total);
                            map.put(in, list);
                        } else {
                            list = new ArrayList<Float>();
                            list.add(night);
                            list.add(people);
                            list.add(adult);
                            list.add(children);
                            list.add(tax);
                            list.add(total);
                            map.put(in, list);
                        }
                    }
                }
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0);
        jScrollPane6.getViewport().setBackground(Color.white);

        for (String s : map.keySet()) {
            list = map.get(s);
            double stayTax = list.get(4);
            stayTax = Math.round(stayTax * 100.0) / 100.0;
            double grandTotal = list.get(5);
            grandTotal = Math.round(grandTotal * 100.0) / 100.0;
            model.addRow(new Object[]{"20" + s, Math.round((list.get(0))), Math.round((list.get(1))), Math.round((list.get(2))), Math.round((list.get(3))), stayTax, grandTotal});
        }
    }

    public void homeInitMonthStats() {
        ResultSet rs;
        String booking = "";
        String in = "";
        float night = 0;
        float people = 0;
        float adult = 0;
        float children = 0;
        float tax = 0;
        float total = 0;
//create a map with key: month of the year and value list (float): 0=property, 1= night, 2= people, 3=adult,4= children, 5= tax, 6= total)
        HashMap<String, ArrayList<Float>> map = new HashMap<String, ArrayList<Float>>();
        ArrayList<Float> list = new ArrayList<Float>();
        //get the selected booking
        booking = (String) jComboBox3.getSelectedItem();
        //get the selcted year
        if (jComboBox4.getItemCount() > 0) {
            String year = "20" + (String) jComboBox4.getSelectedItem();
            year = StringFormatter.simpleClean(year);

            //if selected booking is set to all, search all booking within the selected year
            if (booking.equals("tout voir")) {
                rs = select.getData(db, "select * from booking where year(checkIn)='" + year + "'");

            } else {
                //if not, search all booking with the selected property within the selected year 
                rs = select.getData(db, "select * from booking where year(checkIn)='" + year + "' and roomName like'" + booking + "%' ");
            }
            try {
                while (rs.next()) {

                    //add in the statistiques table only the booking with a tax           
                    double checkTax = Double.parseDouble(rs.getString("baseTax"));
                    if (checkTax != 0) {
                        if (rs.getString("status").equals("en cours") || rs.getString("status").equals("terminé")) {

                            in = new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("booking.checkIn"));

                            int length = in.length();
                            if (length != 0) {
                                in = in.substring(3, 5);

                                booking = rs.getString("roomName");
                                night = rs.getInt("numberOfStay");
                                people = rs.getInt("adult") + rs.getInt("child");
                                adult = rs.getInt("adult") * night;
                                children = rs.getInt("child") * night;
                                tax = rs.getFloat("tax");
                                total = rs.getFloat("totalWithTax");
                                //sort booking per month
                                if (in.indexOf("01") != -1) {
                                    if (map.containsKey("Janvier")) {
                                        list = map.get("Janvier");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Janvier", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Janvier", list);
                                    }
                                }

                                if (in.indexOf("02") != -1) {
                                    if (map.containsKey("Fevrier")) {
                                        list = map.get("Fevrier");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Fevrier", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Fevrier", list);
                                    }
                                }

                                if (in.indexOf("03") != -1) {
                                    if (map.containsKey("Mars")) {
                                        list = map.get("Mars");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Mars", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Mars", list);
                                    }
                                }

                                if (in.indexOf("04") != -1) {
                                    if (map.containsKey("Avril")) {
                                        list = map.get("Avril");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Avril", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Avril", list);
                                    }
                                }

                                if (in.indexOf("05") != -1) {
                                    if (map.containsKey("Mai")) {
                                        list = map.get("Mai");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Mai", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Mai", list);
                                    }
                                }

                                if (in.indexOf("06") != -1) {
                                    if (map.containsKey("Juin")) {
                                        list = map.get("Juin");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Juin", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Juin", list);
                                    }
                                }

                                if (in.indexOf("07") != -1) {
                                    if (map.containsKey("Juillet")) {
                                        list = map.get("Juillet");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Juillet", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Juillet", list);
                                    }
                                }

                                if (in.indexOf("08") != -1) {
                                    if (map.containsKey("Aout")) {
                                        list = map.get("Aout");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Aout", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Aout", list);
                                    }
                                }

                                if (in.indexOf("09") != -1) {
                                    if (map.containsKey("Septembre")) {
                                        list = map.get("Septembre");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Septembre", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Septembre", list);
                                    }
                                }

                                if (in.indexOf("10") != -1) {
                                    if (map.containsKey("Octobre")) {
                                        list = map.get("Octobre");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Octobre", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Octobre", list);
                                    }
                                }

                                if (in.indexOf("11") != -1) {
                                    if (map.containsKey("Novembre")) {
                                        list = map.get("Novembre");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Novembre", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Novembre", list);
                                    }
                                }

                                if (in.indexOf("12") != -1) {
                                    if (map.containsKey("Decembre")) {
                                        list = map.get("Decembre");
                                        list.set(0, list.get(0) + night);
                                        list.set(1, list.get(1) + people);
                                        list.set(2, list.get(2) + adult);
                                        list.set(3, list.get(3) + children);
                                        list.set(4, list.get(4) + tax);
                                        list.set(5, list.get(5) + total);
                                        map.put("Decembre", list);
                                    } else {
                                        list = new ArrayList<Float>();
                                        list.add(night);
                                        list.add(people);
                                        list.add(adult);
                                        list.add(children);
                                        list.add(tax);
                                        list.add(total);
                                        map.put("Decembre", list);
                                    }
                                }

                            }

                        }
                    }
                }
                rs.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

            DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
            model.setRowCount(0);

            ArrayList<String> month = new ArrayList<String>();
            month.add("Janvier");
            month.add("Fevrier");
            month.add("Mars");
            month.add("Avril");
            month.add("Mai");
            month.add("Juin");
            month.add("Juillet");
            month.add("Aout");
            month.add("Septembre");
            month.add("Octobre");
            month.add("Novembre");
            month.add("Decembre");

            for (int i = 0; i < 12; i++) {
                String s = month.get(i);
                if (map.containsKey(s)) {
                    list = map.get(s);

                    double stayTax = list.get(4);
                    stayTax = Math.round(stayTax * 100.0) / 100.0;
                    double grandTotal = list.get(5);
                    grandTotal = Math.round(grandTotal * 100.0) / 100.0;
                    model.addRow(new Object[]{s, Math.round((list.get(0))), Math.round((list.get(1))), Math.round((list.get(2))), Math.round((list.get(3))), stayTax, grandTotal});
                }
            }

            map.clear();
        }
    }

    public void homePanelsNotVisible() {
        //set every panel to not visible in "mon espace".
        statPanel.setVisible(false);
        statMPanel.setVisible(false);
        createDocPanel.setVisible(false);
        myPanel.setVisible(false);
        myRentalPanel.setVisible(false);
        createEmailPanel.setVisible(false);
        optionPanel.setVisible(false);
        client.setVisible(false);
        externalBooking.setVisible(false);
    }

    private void homeSubPanelNotVisible() {
        //set every panel to not visible in "mon espace/ mes locations".
        roomPanel.setVisible(false);
        housePanel.setVisible(false);
        dormPanel.setVisible(false);
        roomHousePanel.setVisible(false);
        roomStoredLabel.setText("");
        singleBedStoredLabel.setText("");
        doubleBedStoredLabel.setText("");
    }

    public void homeLabelNotVisible() {
        // set all menu selector label in "mon espace" to not visible.
        jLabel126.setVisible(false);
        jLabel127.setVisible(false);
        jLabel128.setVisible(false);
        jLabel130.setVisible(false);
        jLabel136.setVisible(false);
        jLabel185.setVisible(false);
        jLabel204.setVisible(false);
    }

    private void homeResetMyrentalTable() {
        System.out.println("reset myRental table");
        //empty and set "mon espace/ mes location" jTable
        DefaultTableModel model = (DefaultTableModel) myRentalsTable.getModel();
        model.setRowCount(0);
        myRentalsTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        myRentalsTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        myRentalsTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        myRentalsTable.getColumnModel().getColumn(4).setPreferredWidth(35);
        myRentalsTable.getColumnModel().getColumn(5).setPreferredWidth(35);
        myRentalsTable.getColumnModel().getColumn(6).setPreferredWidth(50);
        myRentalsTable.getColumnModel().getColumn(7).setPreferredWidth(40);
        myRentalsTable.getColumnModel().getColumn(8).setPreferredWidth(40);
        jScrollPane1.getViewport().setBackground(Color.white);

        //search every property stored in room table and add them in "mon espace/ mes location" jTable
        ResultSet rs = select.getData(db, "select * from Room");
        try {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(12)});

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //reset all textfield in "mon espace/ mes location" to null( except for property, adress and tax in dorm and room)
        nameRoomTextField.setText("");
        amountRoomTextField.setText("");
        doubleBedRoomTextField.setText("0");
        singleBedRoomTextField.setText("0");
        nameHouseTextField.setText("");
        qtyRoomHouseTextField.setText("0");
        amountHouseTextField.setText("");
        nameRoomHouseTextField.setText("");
        singleBedRoomHouseTextField.setText("0");
        doublebedRoomHouseTextField.setText("0");
        nameDormTextField.setText("");
        singleBedDormTextField.setText("0");
        doubleBedDormTextField.setText("0");
        amountDormTextField.setText("");
        boundHouseTextField.setText("");
    }

    private void homeResetOption() {
        System.out.println("reset option");
        //set all textfields and checkbox to null in "mon espace/ mes options"  panel.
        jTextField10.setText("");
        jTextField11.setText("");
        jCheckBox1.setSelected(false);
    }

    private void homeResetExternalBooking() {
        nameTextField.setText("");
    }

    //return a new id for the rentals property
    private int homeNewID() {
        System.out.println("generate a property id");
        int id = 1;
        String Query = "select max(id) from Room";

        try {
            ResultSet rs = select.getData(db, Query);
            while (rs.next()) {
                id = rs.getInt(1);
                id += 1;

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return id;
    }

    //set every panels in "mes réservation" to not visible.
    private void bookingPanelsNotVisible() {
        newBooking.setVisible(false);
        bookingsPanel.setVisible(false);
        previousBookingPanel.setVisible(false);
        bookingNewBookingInfoPanel.setVisible(false);
        allBookingPanel.setVisible(false);
        endBookingPanel.setVisible(false);
        emailBookingPanel.setVisible(false);
        bookingOption.setVisible(false);
        bookingOption2.setVisible(false);
        sendEmailPanel.setVisible(false);
        sendEmailPanel2.setVisible(false);
        sendEmailPanel3.setVisible(false);
        bookingExternalPanel.setVisible(false);
        statPanel.setVisible(false);
        statMPanel.setVisible(false);

    }

    //set all booking selector label to not visible.
    public void bookingLabelNotVisible() {
        jLabelb130.setVisible(false);
        jLabelb131.setVisible(false);
        jLabelb132.setVisible(false);
        jLabelb135.setVisible(false);
    }

    //set all available house according to the details given
    public void bookingHouseDetails() {
        System.out.println("house details");

        choosePropertyBox.removeAllItems();
        amountField.setText("");
        boundField.setText("");
        taxField.setText("");
        //get property type, single  & double bed info
        int singleBed = Integer.parseInt(singleField.getText());
        int doubleBed = Integer.parseInt(doubleField.getText());
        String bookingType = (String) propertyTypeBox.getSelectedItem();
        try {
            //in sql, get all house  where single & double beds qte in room table are higher or equal to the numbers of bed selected and set them in the house panel.
            ResultSet rs = select.getData(db, "select * from Room where singleBed >=" + singleBed + " and doubleBed >=" + doubleBed + " and bookingType = '" + bookingType + "'");
            while (rs.next()) {
                if (propertyTypeBox.getSelectedIndex() == 0) {
                    choosePropertyBox.addItem(rs.getString(2));
                } else {
                    choosePropertyBox.addItem(rs.getString(2) + "-" + rs.getString(4));
                }
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //reset every field of "mes réservations/ nouveau/ Maison" to there initial value;
    private void bookingResetBookingInfo() {
        firstNameField.setText("");
        nameField.setText("");
        taxField.setText("");
        tel1Field.setText("");
        emailField.setText("");
        cityField.setText("");
        address1Field.setText("");
        postCodeField.setText("");
        districtField.setText("");
        idField.setText("");
        jComboBoxb2.setSelectedIndex(0);
        adultField.setText("0");
        kidField.setText("0");
        singleField.setText("0");
        doubleField.setText("0");
        timeInField.setText("17h");
        timeOutField.setText("10h");
        boundField.setText("");
        amountField.setText("");
        jTextAreab1.setText("");
        jComboBoxb8.removeAllItems();
        choosePropertyBox.removeAllItems();
        calendarIn.setDate(new Date());
        calendarOut.setDate(new Date());

    }

    private void bookingNewResetClient() {
        //reset all client info fields to default
        tel1Field.setText("");
        tel2Field.setText("");
        emailField.setText("");
        address1Field.setText("");
        address2Field.setText("");
        districtField.setText("");
        postCodeField.setText("");
        cityField.setText("");
        idField.setText("");
        blackListCheckBox.setSelected(false);
        jComboBoxb8.removeAll();
        jComboBoxb2.setSelectedIndex(0);
        jTextAreab1.setText("");
        commentsPanelb.setVisible(false);
    }

    private void homeClientReset() {
        //reset all client info fields to default
        clientTel1Field.setText("");
        clientEmailField.setText("");
        clientStreet1Field.setText("");
        clientTel2Field.setText("");
        clientStreet2Field.setText("");
        clientDcField.setText("");
        clientCityField.setText("");
        clientIdProofField.setText("");
        clientPostCodeField.setText("");
        jTextAreab3.setText("");
        jComboBoxb14.removeAllItems();
        commentsPanelb1.setVisible(false);
        jTextAreab3.setText("");
        jComboBoxb15.setSelectedIndex(0);
    }
    //fill booking type box with all external booking services and set calendar to today's date  

    private void bookingLoadTypeBox() {
        calendarIn.setDate(new Date());
        calendarIn.getMonthView().setLowerBound(new Date());
        calendarIn.setDate(new Date());
        calendarOut.setDate(new Date());
        bookingTypeBox.removeAllItems();
        bookingTypeBox.addItem("Direct");
        ResultSet rs = select.getData(db, "SELECT * FROM  externalBooking");
        try {
            while (rs.next()) {
                bookingTypeBox.addItem(rs.getString("name"));
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void bookingNewClient() {
        bookingNewResetClient();

//create bookingTypeBox items 
        bookingLoadTypeBox();

        clientPanel2.setVisible(true);
        bookingNewBookingInfoPanel.setVisible(true);
        propertyTypeBox.setSelectedIndex(0);
        bookingTypeBox.setSelectedIndex(0);

        if (firstNameField.getText().isEmpty()) {
            tel1Field.requestFocus();
        } else {
            jComboBoxb2.requestFocus();
        }

    }

    private void homeClientNew() {
        homeClientReset();
        jComboBoxb15.requestFocus();
        jComboBoxb15.setSelectedIndex(0);
        commentsPanelb1.setVisible(false);
        clientPanel3.setVisible(true);
    }

    private void bookingNewLoadClient(int checkFirstName, int clientId) {
        client c = new client(sc.getClientDetails(clientId));

        tel1Field.setText(c.getTel1());
        tel2Field.setText(c.getTel2());
        emailField.setText(c.getEmail());
        address1Field.setText(c.getStreet1());
        address2Field.setText(c.getStreet2());
        districtField.setText(c.getDc());
        postCodeField.setText(c.getPostCode());
        cityField.setText(c.getCity());
        idField.setText(c.getIdProof());
        if (c.getBlackList().equals("true")) {
            blackListCheckBox.setSelected(true);
        } else {
            blackListCheckBox.setSelected(false);
        }
        if (checkFirstName == 0) {
            jComboBoxb2.setSelectedItem(c.getGender());
        }
        //set all necessary panels and component 

        commentsPanelb.setVisible(true);
        searchClientPanel.setVisible(true);
        jComboBoxb8.removeAllItems();
        clientPanel2.setVisible(true);
        bookingNewBookingInfoPanel.setVisible(true);
        bookingLoadTypeBox();
        multipleClients.setVisible(false);
        propertyTypeBox.setSelectedIndex(0);
        bookingTypeBox.requestFocus();

        //add each client booking date into previous comment comboBox.            
        ArrayList<String> list = sc.getDates(sc.getId());
        for (String s : list) {
            jComboBoxb8.addItem(s);
        }
    }

    private void homeClientLoad(int checkFirstName, int clientId) {
        client c = new client(sc.getClientDetails(clientId));
        clientTel1Field.setText(c.getTel1());
        clientTel2Field.setText(c.getTel2());
        clientEmailField.setText(c.getEmail());
        clientStreet1Field.setText(c.getStreet1());
        clientStreet2Field.setText(c.getStreet2());
        clientDcField.setText(c.getDc());
        clientPostCodeField.setText(c.getPostCode());
        clientCityField.setText(c.getCity());
        clientIdProofField.setText(c.getIdProof());
        if (c.getBlackList().equals("true")) {
            clientBlackListCheckBox.setSelected(true);
        } else {
            blackListCheckBox.setSelected(false);
        }
        if (checkFirstName == 0) {
            jComboBoxb15.setSelectedItem(c.getGender());
        }
        //set all necessary panels and component 
        commentsPanelb1.setVisible(true);
        searchClientPanel1.setVisible(true);
        jComboBoxb14.removeAllItems();
        clientPanel3.setVisible(true);
        multipleClients1.setVisible(false);
        //add each client booking date into previous comment comboBox.            
        ArrayList<String> list = sc.getDates(sc.getId());
        for (String s : list) {
            jComboBoxb14.addItem(s);
        }
    }

    //reset every field of "mes réservations/ encours/ terminer-payer" to there initial value;
    private void bookingResetEndBooking() {
        System.out.println("reset Endbooking");
        jTextFieldb49.setText("");
        jTextFieldb50.setText("");
        jTextFieldb51.setText("");
        jTextFieldb52.setText("");
        jTextFieldb53.setText("");
        jTextFieldb54.setText("");
        jTextFieldb61.setText("");
        //set terminer & envoi courrier as unselected set send email as selected
        jCheckBoxb6.setSelected(true);
        jCheckBoxb8.setSelected(false);
        jCheckBoxb5.setSelected(false);

    }

    //init the email panel
    private void bookingInitEmailPanel(int booking) {
        System.out.println("init booking emailPanel");
        bookingPanelsNotVisible();
        emailBookingPanel.setVisible(true);
        //set "envoyé email" to selected and "envoyé courrier" to not selected
        jCheckBoxb9.setSelected(true);
        jCheckBoxb11.setSelected(false);
        //set bookingID field to the current booking id in use
        bookingID = booking;

    }

    public void bookingInitNewPanel() {
        System.out.println("init booking newPanel");
        newBooking.setVisible(true);
        searchClientPanel.setVisible(true);
        multipleClients.setVisible(false);
        commentsPanelb.setVisible(false);
        clientPanel2.setVisible(false);
        bookingNewBookingInfoPanel.setVisible(false);
        bookingInitBookingOption();
        nameField.requestFocus();
    }

    //init "mes réservation/nouveau/ ajouter option" panel
    private void bookingInitBookingOption() {
        System.out.println("init bookingOption");
        //set the jTable
        DefaultTableModel model = (DefaultTableModel) jTableb7.getModel();
        model.setRowCount(0);
        jTableb7.getColumnModel().getColumn(0).setPreferredWidth(15);
        jTableb7.getColumnModel().getColumn(2).setPreferredWidth(15);
        jTableb7.getColumnModel().getColumn(3).setPreferredWidth(15);
        jScrollPaneb9.getViewport().setBackground(Color.white);
        //get all option in sql "myOption" table
        ResultSet rs = select.getData(db, "select * from myOption");
        try {
            //import all option found in the jTable
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("ID"), rs.getString("name"), rs.getString("amount"), false});
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //init "mes réservation/ encours / réservation /gérer option" panel
    public void bookingInitBookingOption2() {
        System.out.println("init bookingOption2");
        //initialize the jTable
        DefaultTableModel model = (DefaultTableModel) jTableb8.getModel();
        model.setRowCount(0);
        jTableb8.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTableb8.getColumnModel().getColumn(1).setPreferredWidth(120);
        jScrollPaneb10.getViewport().setBackground(Color.white);

        ResultSet rs = select.getData(db, "select * from bookingOption WHERE bookingID='" + bookingID + "'");
        ResultSet rs1 = select.getData(db, "select * from myOption");
        ResultSet rs2 = select.getData(db, "select * from booking  WHERE ID ='" + bookingID + "'");
        ArrayList<Integer> list = new ArrayList<Integer>();
        int night = 0;
        String totalOption = "";
        String add = "";
        boolean select = false;

        try {
            //get the number of night and the total amountStay of option in sql booking table 
            while (rs2.next()) {
                night = rs2.getInt("numberOfStay");
                totalOption = rs2.getString("myOption");
            }
            rs2.close();
            //look at all options stored in bookingOption for the current bookingID 
            while (rs.next()) {
                //look if option is already selected
                select = rs.getBoolean("selected");
                int optionID = rs.getInt("optionID");
                //add the option in list( to check later if the option was already found)
                list.add(optionID);
                String name = rs.getString("name");
                int amount = Integer.parseInt(rs.getString("amount"));
                //look if Option is a daily Rate
                String dailyRate = rs.getString("dailyRate");
                //if option is not a daily rate, set amountStay to the column "montant" in the jTable
                if (dailyRate.equals("false")) {
                    model.addRow(new Object[]{optionID, name, "", amount, select});
                } else {
                    //if it is a daily rate,set  amountStay to "prix-jour" colomn and montant is the amountStay * the number of night
                    amount = night * amount;
                    model.addRow(new Object[]{optionID, name, rs.getInt("amount"), amount, select});
                }
            }
            rs.close();
            //look at all option in sql myOption table
            while (rs1.next()) {
                //for each option look at the id
                int id = rs1.getInt("ID");

                //if the option ID was not already found in bookingOption (not added to list)
                if (!list.contains(id)) {
                    //then get all the option info
                    String name = rs1.getString("name");
                    int amount = Integer.parseInt(rs1.getString("amount"));
                    String dailyRate = rs1.getString("dailyRate");
                    // and insert the option in the bookingOption table under the current booking ID in use
                    String Query3 = " INSERT INTO bookingOption (bookingID, name, dailyRate, amount, optionID, selected) values ('" + bookingID + "','" + name + "','" + dailyRate + "','" + amount + "','" + id + "', 'false')";
                    InsertUpdateDelete.setData(db, Query3, "");
                    //if option is not a daily rate, set amountStay to the column "montant" in the jTable
                    if (dailyRate.equals("false")) {
                        model.addRow(new Object[]{id, name, "", amount, false});
                    } else {
                        //if it is a daily rate,set  amountStay to "prix-jour" colomn and montant is the amountStay * the number of night
                        amount = night * amount;
                        model.addRow(new Object[]{id, name, rs1.getInt("amount"), amount, false});
                    }
                }
            }
            rs1.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //set the total option field in "mes reservation/ en cours/ reservation" panel
        jTextFieldb48.setText(totalOption);

    }

    // apply daily Rate change of all bookingOption avalaible and if update the total option amountStay of the booking, when a date is modify in reservation en cours
    private void bookingSetChangeDate() {

        DefaultTableModel model = (DefaultTableModel) jTableb8.getModel();
        int amount = 0;

        try {
            for (int i = 0; i < model.getRowCount(); i++) {

                ResultSet rs = select.getData(db, " SELECT * FROM bookingOption  WHERE bookingID= " + bookingID + " AND name= '" + model.getValueAt(i, 1) + "'");
                while (rs.next()) {
                    int optionRate = 0;
                    if (rs.getString("dailyRate").equals("true")) {
                        optionRate = Integer.parseInt(jTextFieldb38.getText()) * (int) model.getValueAt(i, 2);
                        model.setValueAt(optionRate, i, 3);
                    }
                }
                if ((boolean) model.getValueAt(i, 4) == true) {
                    amount = (int) model.getValueAt(i, 3) + amount;
                }
                rs.close();

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        jTextFieldb48.setText(String.valueOf(amount));
        //bookingSetTotal();

    }

    //set TOTAL field in "mes reservation/ en cours/ reservation" panel
    private void bookingSetTotal() {
        System.out.println("set total");
        //get the base tax value of the booking in use in sql booking
        ResultSet rs = select.getData(db, "select * from booking WHERE ID= " + bookingID);
        double baseTax = 0;
        String type = "";
        double price = 0;
        String origin = "";

        try {
            while (rs.next()) {
                baseTax = Double.parseDouble(rs.getString("baseTax"));
                type = rs.getString("bookingType");
                price = rs.getFloat("pricePerDay");
                price = Math.round(price * 100.0) / 100.0;
                origin = rs.getString("origin");
                System.out.println(origin);

                System.out.println(price);
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        //get adult, night, total option, booking amountStay and paid amountStay values set in "mes reservation/ en cours/ reservation" panel
        int adult = Integer.parseInt(jTextFieldb40.getText());
        int kid = Integer.parseInt(jTextFieldb41.getText());
        int people = adult + kid;
        int night = Integer.parseInt(jTextFieldb38.getText());
        int option = Integer.parseInt(jTextFieldb48.getText());
        double totalAmount = Double.parseDouble(jTextFieldb29.getText());
        if (type.equals("Dortoir") && origin.equals("Direct")) {
            totalAmount = (price * people) * night;
            totalAmount = Math.round(totalAmount * 100.0) / 100.0;
            jTextFieldb29.setText(String.valueOf(totalAmount));
        }
        double paid = Double.parseDouble(paidField.getText());
        //recalculate tax, TOTAL and toPay and set the new value in their respective fields( each value to be rounded at 2 decimal)
        double tax = (baseTax * adult) * night;
        tax = Math.round(tax * 100.0) / 100.0;
        double total = totalAmount + tax + option;

        total = Math.round(total * 100.0) / 100.0;

        double toPay = total - paid;
        toPay = Math.round(toPay * 100.0) / 100.0;
        jTextFieldb35.setText(String.valueOf(tax));
        grandTotalField.setText(String.valueOf(total));
        toPayField.setText(String.valueOf(toPay));
    }

    private void bookingSaveCurrBooking() {
        System.out.println("reservation/ en cours/ booking: save");
        bookingSetTotal();

//Process and Update clientInfo
        client c = new client();
        c.setName(StringFormatter.clean(bookingNameField.getText()));
        c.setFirstName(StringFormatter.clean(bookingFirstNameField.getText()));
        String gender = "";
        if (!bookingFirstNameField.getText().isEmpty()) {
            gender = (String) bookingGenderBox.getSelectedItem();
        } else {
            gender = "Madame/Monsieur";
        }
//get all information from client panel and set them to client class            
        c.setGender(gender);
        c.setTel1(StringFormatter.simpleClean(bookingTel1Field.getText()));
        c.setTel2(StringFormatter.simpleClean(bookingTel2Field.getText()));
        c.setEmail(StringFormatter.simpleClean(bookingEmailField.getText()));
        c.setStreet1(StringFormatter.clean(bookingStreet1Field.getText()));
        c.setStreet2(StringFormatter.clean(bookingStreet2Field.getText()));
        c.setDc(StringFormatter.clean(bookingDcField.getText()));
        c.setPostCode(StringFormatter.simpleClean(bookingPostCodeField.getText()));
        c.setCity(StringFormatter.clean(bookingCityField.getText()));
        c.setIdProof(StringFormatter.simpleClean(bookingIdProofField.getText()));
        String blackList = "false";
        if (bookingBlackListCheckBox.isSelected()) {
            blackList = "true";
        }
        c.setBlackList(blackList);
        c.update(db, clientID);

        String paiement = (String) jComboBoxb9.getSelectedItem();
        //check if district is given, if yes get their value

        String comment = "";
        comment = bookingCommentText.getText().trim();
        comment = comment.replace("\'", "\'\'");
        String type = jTextFieldb44.getText();
        String timeIn = jTextFieldb86.getText();
        String timeOut = jTextFieldb87.getText();
        int adult = Integer.parseInt(jTextFieldb40.getText());
        int kid = Integer.parseInt(jTextFieldb41.getText());
        int people = kid + adult;
        int singleBed = Integer.parseInt(jTextFieldb42.getText());
        int doubleBed = Integer.parseInt(jTextFieldb43.getText());
        String advance = jTextFieldb27.getText();
        int bound = 0;
        Date dateIn = jXDatePicker1.getDate();
        String checkIn = new SimpleDateFormat("yyyy-MM-dd").format(dateIn);
        Date dateOut = jXDatePicker2.getDate();
        String checkOut = new SimpleDateFormat("yyyy-MM-dd").format(dateOut);
        int night = Integer.parseInt(jTextFieldb38.getText());
        int option = Integer.parseInt(jTextFieldb48.getText());
        double tax = Double.parseDouble(jTextFieldb35.getText());
        //if a value is given to bound get the value
        if (!jTextFieldb26.getText().isBlank()) {
            bound = Integer.parseInt(jTextFieldb26.getText());
        }
        //totalAmount is the price of the booking (excluding tax and option)
        double totalAmount = Double.parseDouble(jTextFieldb29.getText());
        double toPay = Double.parseDouble(toPayField.getText());
        double paid = Double.parseDouble(paidField.getText());
        double price = 0;
        if (type.equals("Dortoir")) {
            price = (totalAmount / night) / people;
        } else {
            price = totalAmount / night;
        }
        //totalWIthTax is the grand total( totalAmount+ options+tax)
        double totalWithTax = Double.parseDouble(grandTotalField.getText());

        String boundStatus = "false";
        String contractStatus = "false";
        String advanceStatus = "false";
        //if booking is a house look at the bound status
        if (type.equals("Maison")) {
            if (jCheckBoxb1.isSelected()) {
                boundStatus = "true";

            } else {
                boundStatus = "false";
            }

        }
        if (jCheckBoxb3.isSelected()) {
            contractStatus = "true";

        } else {
            contractStatus = "false";
        }
        if (jCheckBoxb2.isSelected()) {
            advanceStatus = "true";
            jCheckBoxb2.setEnabled(false);
        } else {
            advanceStatus = "false";
            jCheckBoxb2.setEnabled(true);
        }

        //if booking type is a house update all booking info inclunding bound value and bound status
        String query = "Update booking set checkIn='" + checkIn + "', checkOut='" + checkOut + "' ,timeIn='" + timeIn + "', timeOut='" + timeOut + "' , numberOfStay='" + night + "' , adult='" + adult + "' , child= '" + kid + "' , singleBed= '" + singleBed + "' , doubleBed= '" + doubleBed + "' , pricePerDay= '" + price + "' , bound= '" + bound + "' , totalAmount= '" + totalAmount + "' ,paiementMethod= '" + paiement + "', comment= '" + comment + "' ,contratStatus= '" + contractStatus + "' , boundStatus='" + boundStatus + "', advanceStatus='" + advanceStatus + "',myOption ='" + option + "',tax='" + tax + "',totalWithTax='" + totalWithTax + "', paid='" + paid + "', toPay='" + toPay + "',advanceAmount='" + advance + "' WHERE ID= " + bookingID;
        InsertUpdateDelete.setData(db, query, "");

        try {
            //look at gerer option information
            String optionID;
            TableModel model = jTableb8.getModel();
            //for each option, check if it is selected and update the result in bookingOption database
            for (int i = 0; i < jTableb8.getRowCount(); i++) {
                optionID = model.getValueAt(i, 0).toString();
                String selected = "false";
                if ((boolean) model.getValueAt(i, 4) == true) {
                    selected = "true";
                }
                String Query3 = " UPDATE bookingOption SET selected = '" + selected + "' WHERE bookingID = '" + bookingID + "' AND optionID='" + optionID + "'";
                InsertUpdateDelete.setData(db, Query3, "");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    //initialize "mes reseravtions/ en cours/ reservation" panel according to the bookingID selected from" mes reservation/ en cours" panel
    private void bookingInitBooking() {
        System.out.println("init booking panel");
        bookingStatus = 1;
        addAmountPanel.setVisible(false);
        bookingMenuPanel.setVisible(false);
        //get the current booking information from booking table and the linked client from client table and set all variable accordingly
        ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID where booking.ID='" + bookingID + "'");
        int night = 0;
        try {
            while (rs.next()) {
                //set the client informations
                clientID = rs.getInt("client.ID");
                String gender = rs.getString("client.gender");
                String name = rs.getString("client.name");
                String firstName = rs.getString("client.firstName");
                String tel = rs.getString("client.mobileNumber");
                String tel2 = rs.getString("client.tel2");
                String street = rs.getString("client.street");
                String street2 = rs.getString("client.street2");
                String district = rs.getString("client.district");
                String cp = rs.getString("client.cp");
                String city = rs.getString("client.city");
                String country = rs.getString("client.country");
                String email = rs.getString("client.email");
                String idNumber = rs.getString("client.IDproof");
                String comment = rs.getString("booking.comment");
                String bookingType = rs.getString("booking.origin");
                int externalDue = rs.getInt("booking.externalDue");

                //if there is no firstName given set gender variable to monsieur et madame
                if (firstName.isEmpty()) {
                    gender = "Madame et Monsieur";
                    bookingGenderBox.setVisible(false);
                    jLabelb61.setVisible(false);
                } else {
                    //else, set the gender field according to the combobox selection
                    bookingGenderBox.setVisible(true);
                    jLabelb61.setVisible(true);
                    if (gender.equals("Homme")) {
                        bookingGenderBox.setSelectedIndex(0);
                    }
                    if (gender.equals("Femme")) {
                        bookingGenderBox.setSelectedIndex(1);
                    }
                    if (gender.equals("Autre")) {
                        bookingGenderBox.setSelectedIndex(2);
                    }
                }
                //set all field in reservation panel
                bookingNameField.setText(name);
                bookingFirstNameField.setText(firstName);
                bookingTel1Field.setText(tel);
                bookingTel2Field.setText(tel2);
                bookingEmailField.setText(email);
                bookingIdProofField.setText(idNumber);
                bookingStreet1Field.setText(street);
                bookingStreet2Field.setText(street2);
                bookingDcField.setText(district);
                bookingPostCodeField.setText(cp);
                bookingCityField.setText(city);
                bookingCommentText.setText(comment);
                bookingTypeLabel.setText(bookingType);
                if (!bookingType.equals("Direct")) {

                    if (externalDue == 0.0) {
                        bookingTypeLabel.setForeground(new Color(55, 185, 55));
                    } else {

                        bookingTypeLabel.setForeground(Color.red);
                    }
                } else {
                    bookingTypeLabel.setForeground(new Color(102, 102, 102));
                }

                //set the booking informations
                //get all information from the sql table
                String rentalName = rs.getString("booking.roomName");
                String type = rs.getString("booking.bookingType");
                String in = new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn"));
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
                //Parsing the given String to Date object
                Date dateIn = formatter.parse(in);
                jXDatePicker1.setDate(dateIn);
                String out = new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut"));
                Date dateOut = formatter.parse(out);
                jXDatePicker2.setDate(dateOut);
                jXDatePicker2.getMonthView().setLowerBound(dateIn);
                jTextFieldb86.setText(rs.getString("booking.timeIn"));
                jTextFieldb87.setText(rs.getString("booking.timeOut"));
                //while in "mes reservation/ en cours/ reservation/ clientBookingID will be use to get the client IDinstead of searching the client id in sql booking table. 
                clientBookingID = rs.getInt("booking.clientID");
                int adult = rs.getInt("booking.adult");
                int kid = rs.getInt("booking.child");
                int singleBed = rs.getInt("booking.singleBed");
                int doubleBed = rs.getInt("booking.doubleBed");
                int bound = rs.getInt("booking.bound");
                int option = rs.getInt("booking.myOption");
                night = rs.getInt("booking.numberOfStay");
                double tax = rs.getFloat("booking.tax");
                tax = Math.round(tax * 100.0) / 100.0;
                double totalAmount = rs.getFloat("booking.totalAmount");
                totalAmount = Math.round(totalAmount * 100.0) / 100.0;
                double totalWithTax = rs.getFloat("booking.totalWithTax");
                totalWithTax = Math.round(totalWithTax * 100.0) / 100.0;
                double paid = rs.getFloat("booking.paid");
                paid = Math.round(paid * 100.0) / 100.0;
                double toPay = rs.getFloat("booking.toPay");
                toPay = Math.round(toPay * 100.0) / 100.0;
                String advance = rs.getString("booking.advanceAmount");
                String boundStatus = rs.getString("booking.boundStatus");
                String contractStatus = rs.getString("booking.contratStatus");
                String advanceStatus = rs.getString("booking.advanceStatus");
                //set all field accordingly
                jTextFieldb45.setText(rentalName);
                jTextFieldb44.setText(type);
                jTextFieldb38.setText(String.valueOf(night));
                jTextFieldb40.setText(String.valueOf(adult));
                jTextFieldb41.setText(String.valueOf(kid));
                jTextFieldb43.setText(String.valueOf(doubleBed));
                jTextFieldb42.setText(String.valueOf(singleBed));
                jTextFieldb27.setText(String.valueOf(advance));
                jTextFieldb29.setText(String.valueOf(totalAmount));
                jTextFieldb35.setText(String.valueOf(tax));
                jTextFieldb48.setText(String.valueOf(option));
                grandTotalField.setText(String.valueOf(totalWithTax));
                paidField.setText(String.valueOf(paid));
                toPayField.setText(String.valueOf(toPay));
                //set color of reste a payer field
                if (toPay == 0.0) {
                    toPayField.setForeground(new Color(55, 185, 55));
                } else {
                    toPayField.setForeground(Color.red);
                }
                // if type is a house set the bounds field as available
                if (type.equals("Maison")) {
                    jTextFieldb26.setText(String.valueOf(bound));
                    jCheckBoxb1.setVisible(true);
                    jLabelb133.setVisible(true);
                    //if "caution" set as not received:
                    if (boundStatus.equals("false")) {
                        jCheckBoxb1.setSelected(false);
                        jCheckBoxb1.setEnabled(true);
                        jCheckBoxb1.setForeground(new Color(255, 50, 50));
                        jCheckBoxb1.setText("Non Reçu");
                    } else {
                        //if "caution" set as not received:
                        jCheckBoxb1.setSelected(true);
                        jCheckBoxb1.setEnabled(false);
                        jCheckBoxb1.setText("Reçu");
                        jCheckBoxb1.setForeground(new Color(55, 185, 55));
                    }

                } else {
                    // if type is not a house set the bounds field as not available
                    jCheckBoxb1.setVisible(false);
                    jLabelb133.setVisible(false);
                    jTextFieldb26.setText("");
                    jTextFieldb26.setEditable(false);
                    jCheckBoxb1.setEnabled(false);
                    jCheckBoxb1.setText("");
                }

                //set arrhes section
                if (advanceStatus.equals("false")) {
                    jCheckBoxb2.setEnabled(true);
                    jTextFieldb27.setEditable(true);
                    jCheckBoxb2.setSelected(false);
                    jCheckBoxb2.setForeground(new Color(255, 50, 50));
                    jCheckBoxb2.setText("Non Reçu");
                } else {
                    jCheckBoxb2.setEnabled(false);
                    jTextFieldb27.setEditable(false);
                    jCheckBoxb2.setSelected(true);
                    jCheckBoxb2.setForeground(new Color(55, 185, 55));
                    jCheckBoxb2.setText("Reçu");
                }
                //set conract section
                if (contractStatus.equals("false")) {
                    jCheckBoxb3.setEnabled(true);
                    jCheckBoxb3.setSelected(false);
                    jCheckBoxb3.setForeground(new Color(255, 50, 50));
                    jCheckBoxb3.setText("Non Signé");
                    // jTextFieldb29.setEditable(true);
                } else {
                    jCheckBoxb3.setEnabled(false);
                    jCheckBoxb3.setSelected(true);
                    jCheckBoxb3.setForeground(new Color(55, 185, 55));
                    jCheckBoxb3.setText("Signé");
                    //jTextFieldb29.setEditable(false);
                }
                if (rs.getString("blackList").equals("true")) {
                    bookingBlackListCheckBox.setSelected(true);
                } else {
                    bookingBlackListCheckBox.setSelected(false);
                }

            }
            rs.close();
            //initialize "mes reservations/ en cours/ reservation/ gerer option" panel
            bookingInitBookingOption2();
            document.initFields(db, bookingID);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 =  new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    mainPage.class.getResource("/images/background1_1.jpg"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };  ;
        jLabelHomeSelector = new javax.swing.JLabel();
        jLabelBookingSelector = new javax.swing.JLabel();
        jLabelSignOut = new javax.swing.JLabel();
        jLabelHome = new javax.swing.JLabel();
        jLabelBooking = new javax.swing.JLabel();
        jLabelClose = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        homePanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    mainPage.class.getResource("/images/background1_1.jpg"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }; ;
        createDocPanel = new javax.swing.JLayeredPane();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        contratTxt = new javax.swing.JTextArea();
        autoLabel1 = new javax.swing.JLabel();
        autoLabel4 = new javax.swing.JLabel();
        autoLabel8 = new javax.swing.JLabel();
        autoLabel17 = new javax.swing.JLabel();
        autoLabel2 = new javax.swing.JLabel();
        autoLabel3 = new javax.swing.JLabel();
        autoLabel11 = new javax.swing.JLabel();
        autoLabel18 = new javax.swing.JLabel();
        autoLabel12 = new javax.swing.JLabel();
        autoLabel6 = new javax.swing.JLabel();
        autoLabel5 = new javax.swing.JLabel();
        autoLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        autoLabel15 = new javax.swing.JLabel();
        autoLabel16 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        autoLabel10 = new javax.swing.JLabel();
        autoLabel9 = new javax.swing.JLabel();
        contratTitle = new javax.swing.JTextField();
        jComboBox6 = new javax.swing.JComboBox<>();
        autoLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        autoLabel14 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        createEmailPanel = new javax.swing.JLayeredPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        autoLabelb1 = new javax.swing.JLabel();
        autoLabelb4 = new javax.swing.JLabel();
        autoLabelb8 = new javax.swing.JLabel();
        autoLabelb17 = new javax.swing.JLabel();
        autoLabelb2 = new javax.swing.JLabel();
        autoLabelb3 = new javax.swing.JLabel();
        autoLabelb11 = new javax.swing.JLabel();
        autoLabelb18 = new javax.swing.JLabel();
        autoLabelb12 = new javax.swing.JLabel();
        autoLabelb6 = new javax.swing.JLabel();
        autoLabelb5 = new javax.swing.JLabel();
        autoLabelb15 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel89 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        autoLabelb13 = new javax.swing.JLabel();
        autoLabelb14 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        jLabel152 = new javax.swing.JLabel();
        autoLabelb9 = new javax.swing.JLabel();
        autoLabelb10 = new javax.swing.JLabel();
        autoLabelb7 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        autoLabelb16 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        myPanel = new javax.swing.JLayeredPane();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        smtpWindow = new javax.swing.JInternalFrame();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel7 = new javax.swing.JPanel();
        jLabelh2 = new javax.swing.JLabel();
        myCompanyField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        myEmailField = new javax.swing.JTextField();
        jLabelh7 = new javax.swing.JLabel();
        myNameField = new javax.swing.JTextField();
        jLabelh8 = new javax.swing.JLabel();
        myFirstNameField = new javax.swing.JTextField();
        jLabel199 = new javax.swing.JLabel();
        myPhone2Field = new javax.swing.JTextField();
        myPhoneField = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        mySiretField = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        myCityField = new javax.swing.JTextField();
        jLabelh5 = new javax.swing.JLabel();
        myPostCodeField = new javax.swing.JTextField();
        jLabelh4 = new javax.swing.JLabel();
        myStreet1Field = new javax.swing.JTextField();
        jLabelh3 = new javax.swing.JLabel();
        jLabelh6 = new javax.swing.JLabel();
        myDcField = new javax.swing.JTextField();
        jLabel167 = new javax.swing.JLabel();
        myApeField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        myStreet2Field = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel98 = new javax.swing.JLabel();
        websiteField = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        facebookField = new javax.swing.JTextField();
        instaField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        googletField = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        tripAdvisorField = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        otherReviewField = new javax.swing.JTextField();
        jLabel183 = new javax.swing.JLabel();
        jLabel182 = new javax.swing.JLabel();
        jLabel181 = new javax.swing.JLabel();
        jLabel180 = new javax.swing.JLabel();
        jLabel179 = new javax.swing.JLabel();
        jLabel178 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jLabel187 = new javax.swing.JLabel();
        jLabel188 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        myLogoField = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        myRentalPanel = new javax.swing.JLayeredPane();
        newRentalPanel = new javax.swing.JPanel();
        rentalTypeLabel = new javax.swing.JLabel();
        selectTypeBox = new javax.swing.JComboBox<>();
        roomPanel = new javax.swing.JPanel();
        nameRoomLabel = new javax.swing.JLabel();
        nameRoomTextField = new javax.swing.JTextField();
        singleRoomBedLabel = new javax.swing.JLabel();
        singleBedRoomTextField = new javax.swing.JTextField();
        doubleBedRoomLabel = new javax.swing.JLabel();
        doubleBedRoomTextField = new javax.swing.JTextField();
        amountRoomLabel = new javax.swing.JLabel();
        amountRoomTextField = new javax.swing.JTextField();
        addRoomButton = new javax.swing.JButton();
        buildingTextField = new javax.swing.JTextField();
        typeRoomLabel = new javax.swing.JLabel();
        cancelRoomButton = new javax.swing.JButton();
        typeRoomLabel1 = new javax.swing.JLabel();
        adressRoomTextField = new javax.swing.JTextField();
        taxRoomLabel = new javax.swing.JLabel();
        taxRoomTextField = new javax.swing.JTextField();
        housePanel = new javax.swing.JPanel();
        qtyRoomHouseLabel = new javax.swing.JLabel();
        qtyRoomHouseTextField = new javax.swing.JTextField();
        nameHouseTextField = new javax.swing.JTextField();
        nameHouseLabel = new javax.swing.JLabel();
        addHouseButton = new javax.swing.JButton();
        cancelHouseButton = new javax.swing.JButton();
        enterHouseButton = new javax.swing.JButton();
        amountHouseLabel = new javax.swing.JLabel();
        amountHouseTextField = new javax.swing.JTextField();
        boundHouseTextField = new javax.swing.JTextField();
        boundHouseLabel = new javax.swing.JLabel();
        singleBedStoredLabel = new javax.swing.JLabel();
        doubleBedStoredLabel = new javax.swing.JLabel();
        roomStoredLabel = new javax.swing.JLabel();
        boundHouseLabel1 = new javax.swing.JLabel();
        taxHouseTextField = new javax.swing.JTextField();
        AdressHouseTextField = new javax.swing.JTextField();
        AdressHouseLabel = new javax.swing.JLabel();
        roomHousePanel = new javax.swing.JPanel();
        singleBedRoomHouseLabel = new javax.swing.JLabel();
        singleBedRoomHouseTextField = new javax.swing.JTextField();
        doublebedRoomHouseTextField = new javax.swing.JTextField();
        nameRoomHouseTextField = new javax.swing.JTextField();
        saveRoomHouseButton = new javax.swing.JButton();
        doubleBedRToomHouseLabel1 = new javax.swing.JLabel();
        dormPanel = new javax.swing.JPanel();
        typeDormLabel = new javax.swing.JLabel();
        typeDormTextField = new javax.swing.JTextField();
        nameDormLabel = new javax.swing.JLabel();
        nameDormTextField = new javax.swing.JTextField();
        singleBedDormLabel = new javax.swing.JLabel();
        singleBedDormTextField = new javax.swing.JTextField();
        doubleBedDormLabel = new javax.swing.JLabel();
        doubleBedDormTextField = new javax.swing.JTextField();
        amountDormLabel = new javax.swing.JLabel();
        amountDormTextField = new javax.swing.JTextField();
        addDormButton = new javax.swing.JButton();
        cancelDormButton = new javax.swing.JButton();
        adressDormTextField1 = new javax.swing.JLabel();
        adressDormTextField = new javax.swing.JTextField();
        taxDormLabel = new javax.swing.JLabel();
        taxDormTextField = new javax.swing.JTextField();
        jLabel120 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cancelMyrentalsButton = new javax.swing.JButton();
        deleteMyrentalsButton = new javax.swing.JButton();
        helpPanel = new javax.swing.JPanel();
        jLabel125 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        jLabel163 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        jLabel165 = new javax.swing.JLabel();
        jLabel189 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        myRentalsTable = new javax.swing.JTable();
        modifyMyrentalsTable = new javax.swing.JButton();
        jLabel119 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        optionPanel = new javax.swing.JLayeredPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jButton21 = new javax.swing.JButton();
        jLabel132 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel135 = new javax.swing.JLabel();
        jButton22 = new javax.swing.JButton();
        jLabel137 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel138 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        client = new javax.swing.JPanel();
        searchClientPanel1 = new javax.swing.JPanel();
        commentsPanelb1 = new javax.swing.JLayeredPane();
        jLabelb149 = new javax.swing.JLabel();
        jComboBoxb14 = new javax.swing.JComboBox<>();
        jScrollPaneb4 = new javax.swing.JScrollPane();
        jTextAreab3 = new javax.swing.JTextArea();
        clientBlackListCheckBox = new javax.swing.JCheckBox();
        jLabelb150 = new javax.swing.JLabel();
        clientfirstNameField = new javax.swing.JTextField();
        jLabelb151 = new javax.swing.JLabel();
        clientNameField = new javax.swing.JTextField();
        jButtonb16 = new javax.swing.JButton();
        jButtonb17 = new javax.swing.JButton();
        jLabelb152 = new javax.swing.JLabel();
        clientPanel3 = new javax.swing.JPanel();
        clientIdProofField = new javax.swing.JTextField();
        clientEmailField = new javax.swing.JTextField();
        jLabelb153 = new javax.swing.JLabel();
        clientDcField = new javax.swing.JTextField();
        clientCityField = new javax.swing.JTextField();
        jLabelb154 = new javax.swing.JLabel();
        clientTel1Field = new javax.swing.JTextField();
        jLabelb155 = new javax.swing.JLabel();
        jLabelb156 = new javax.swing.JLabel();
        jLabelb157 = new javax.swing.JLabel();
        jLabelb158 = new javax.swing.JLabel();
        jComboBoxb15 = new javax.swing.JComboBox<>();
        jLabelb160 = new javax.swing.JLabel();
        clientStreet1Field = new javax.swing.JTextField();
        jLabelb161 = new javax.swing.JLabel();
        clientPostCodeField = new javax.swing.JTextField();
        jLabelb162 = new javax.swing.JLabel();
        jLabelb163 = new javax.swing.JLabel();
        clientTel2Field = new javax.swing.JTextField();
        jLabelb164 = new javax.swing.JLabel();
        jLabelb165 = new javax.swing.JLabel();
        clientStreet2Field = new javax.swing.JTextField();
        jButtonb18 = new javax.swing.JButton();
        jLabelb195 = new javax.swing.JLabel();
        multipleClients1 = new javax.swing.JLayeredPane();
        jLabelb38 = new javax.swing.JLabel();
        jComboBoxb12 = new javax.swing.JComboBox<>();
        jLabelb39 = new javax.swing.JLabel();
        jLabelb41 = new javax.swing.JLabel();
        jButtonb13 = new javax.swing.JButton();
        jButtonb14 = new javax.swing.JButton();
        jLabelb42 = new javax.swing.JLabel();
        jLabelb44 = new javax.swing.JLabel();
        jLabelb145 = new javax.swing.JLabel();
        jLabelb146 = new javax.swing.JLabel();
        jLabelb147 = new javax.swing.JLabel();
        jLabelb148 = new javax.swing.JLabel();
        jLabel198 = new javax.swing.JLabel();
        externalBooking = new javax.swing.JLayeredPane();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jButton23 = new javax.swing.JButton();
        jLabel203 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel205 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jLabel206 = new javax.swing.JLabel();
        jLabel209 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jLabel185 = new javax.swing.JLabel();
        jLabel184 = new javax.swing.JLabel();
        jLabel204 = new javax.swing.JLabel();
        jLabel207 = new javax.swing.JLabel();
        booking11 = new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(
                    mainPage.class.getResource("/images/background1_1.jpg"));
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }; ;
        newBooking = new javax.swing.JPanel();
        multipleClients = new javax.swing.JLayeredPane();
        jLabelb32 = new javax.swing.JLabel();
        jComboBoxb7 = new javax.swing.JComboBox<>();
        jLabelb33 = new javax.swing.JLabel();
        jLabelb34 = new javax.swing.JLabel();
        jButtonb11 = new javax.swing.JButton();
        jButtonb12 = new javax.swing.JButton();
        jLabelb36 = new javax.swing.JLabel();
        jLabelb37 = new javax.swing.JLabel();
        jLabelb125 = new javax.swing.JLabel();
        jLabelb124 = new javax.swing.JLabel();
        jLabelb126 = new javax.swing.JLabel();
        jLabelb134 = new javax.swing.JLabel();
        searchClientPanel = new javax.swing.JPanel();
        commentsPanelb = new javax.swing.JLayeredPane();
        jLabelb35 = new javax.swing.JLabel();
        jComboBoxb8 = new javax.swing.JComboBox<>();
        jScrollPaneb1 = new javax.swing.JScrollPane();
        jTextAreab1 = new javax.swing.JTextArea();
        blackListCheckBox = new javax.swing.JCheckBox();
        jLabelb15 = new javax.swing.JLabel();
        firstNameField = new javax.swing.JTextField();
        jLabelb2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jButtonb5 = new javax.swing.JButton();
        jButtonb15 = new javax.swing.JButton();
        jLabelb109 = new javax.swing.JLabel();
        clientPanel2 = new javax.swing.JPanel();
        idField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        jLabelb16 = new javax.swing.JLabel();
        districtField = new javax.swing.JTextField();
        cityField = new javax.swing.JTextField();
        jLabelb13 = new javax.swing.JLabel();
        tel1Field = new javax.swing.JTextField();
        jLabelb3 = new javax.swing.JLabel();
        jLabelb7 = new javax.swing.JLabel();
        jLabelb8 = new javax.swing.JLabel();
        jLabelb5 = new javax.swing.JLabel();
        jComboBoxb2 = new javax.swing.JComboBox<>();
        jLabelb84 = new javax.swing.JLabel();
        address1Field = new javax.swing.JTextField();
        jLabelb90 = new javax.swing.JLabel();
        postCodeField = new javax.swing.JTextField();
        jLabelb110 = new javax.swing.JLabel();
        jLabelb112 = new javax.swing.JLabel();
        jLabelb45 = new javax.swing.JLabel();
        tel2Field = new javax.swing.JTextField();
        jLabelb166 = new javax.swing.JLabel();
        address2Field = new javax.swing.JTextField();
        jLabelb108 = new javax.swing.JLabel();
        bookingNewBookingInfoPanel = new javax.swing.JPanel();
        jLabelb111 = new javax.swing.JLabel();
        jLabelb17 = new javax.swing.JLabel();
        propertyTypeBox = new javax.swing.JComboBox<>();
        jLabelb19 = new javax.swing.JLabel();
        jLabelb10 = new javax.swing.JLabel();
        jLabelb20 = new javax.swing.JLabel();
        jLabelb21 = new javax.swing.JLabel();
        singleField = new javax.swing.JTextField();
        doubleField = new javax.swing.JTextField();
        adultField = new javax.swing.JTextField();
        jLabelb1 = new javax.swing.JLabel();
        kidField = new javax.swing.JTextField();
        jLabelb47 = new javax.swing.JLabel();
        calendarIn = new org.jdesktop.swingx.JXDatePicker();
        calendarOut = new org.jdesktop.swingx.JXDatePicker();
        jLabelb171 = new javax.swing.JLabel();
        timeInField = new javax.swing.JTextField();
        jLabelb172 = new javax.swing.JLabel();
        timeOutField = new javax.swing.JTextField();
        jLabelb22 = new javax.swing.JLabel();
        choosePropertyBox = new javax.swing.JComboBox<>();
        amountLabel = new javax.swing.JLabel();
        amountField = new javax.swing.JTextField();
        advanceLabel = new javax.swing.JLabel();
        boundField = new javax.swing.JTextField();
        jLabelb85 = new javax.swing.JLabel();
        taxField = new javax.swing.JTextField();
        jLabelb56 = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jLabelb18 = new javax.swing.JLabel();
        bookingTypeBox = new javax.swing.JComboBox<>();
        emailBookingPanel = new javax.swing.JLayeredPane();
        jCheckBoxb9 = new javax.swing.JCheckBox();
        jCheckBoxb11 = new javax.swing.JCheckBox();
        jButtonb30 = new javax.swing.JButton();
        jButtonb31 = new javax.swing.JButton();
        jLabelb99 = new javax.swing.JLabel();
        jLabelb122 = new javax.swing.JLabel();
        jLabelb123 = new javax.swing.JLabel();
        previousBookingPanel = new javax.swing.JLayeredPane();
        jScrollPaneb5 = new javax.swing.JScrollPane();
        jTableb3 = new javax.swing.JTable();
        jButtonb33 = new javax.swing.JButton();
        jTextFieldb62 = new javax.swing.JTextField();
        jLabelb4 = new javax.swing.JLabel();
        jComboBoxb11 = new javax.swing.JComboBox<>();
        jLabelb76 = new javax.swing.JLabel();
        jLabelb103 = new javax.swing.JLabel();
        jLabelb137 = new javax.swing.JLabel();
        jLabelb138 = new javax.swing.JLabel();
        jLabelb142 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        bookingsPanel = new javax.swing.JPanel();
        bookingMenuPanel = new javax.swing.JPanel();
        cancelBookingLabel = new javax.swing.JLabel();
        buildEnvelopLabel = new javax.swing.JLabel();
        NewContractLabel = new javax.swing.JLabel();
        sendEmailLabel = new javax.swing.JLabel();
        jLabel202 = new javax.swing.JLabel();
        jLabelb75 = new javax.swing.JLabel();
        jLabelb80 = new javax.swing.JLabel();
        buildbill = new javax.swing.JLabel();
        addAmountPanel = new javax.swing.JPanel();
        jTextField21 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel196 = new javax.swing.JLabel();
        jButtonb20 = new javax.swing.JButton();
        jButtonb21 = new javax.swing.JButton();
        jLabelb50 = new javax.swing.JLabel();
        clientPanel = new javax.swing.JLayeredPane();
        jLabelb51 = new javax.swing.JLabel();
        bookingNameField = new javax.swing.JTextField();
        jLabelb59 = new javax.swing.JLabel();
        bookingFirstNameField = new javax.swing.JTextField();
        jLabelb54 = new javax.swing.JLabel();
        bookingTel1Field = new javax.swing.JTextField();
        jLabelb58 = new javax.swing.JLabel();
        bookingEmailField = new javax.swing.JTextField();
        jLabelb60 = new javax.swing.JLabel();
        bookingStreet1Field = new javax.swing.JTextField();
        jLabelb61 = new javax.swing.JLabel();
        jLabelb62 = new javax.swing.JLabel();
        bookingDcField = new javax.swing.JTextField();
        jLabelb63 = new javax.swing.JLabel();
        bookingIdProofField = new javax.swing.JTextField();
        jScrollPaneb3 = new javax.swing.JScrollPane();
        bookingCommentText = new javax.swing.JTextArea();
        jLabelb72 = new javax.swing.JLabel();
        jLabelb91 = new javax.swing.JLabel();
        bookingPostCodeField = new javax.swing.JTextField();
        jLabelb92 = new javax.swing.JLabel();
        bookingCityField = new javax.swing.JTextField();
        bookingGenderBox = new javax.swing.JComboBox<>();
        jLabelb114 = new javax.swing.JLabel();
        jLabelb167 = new javax.swing.JLabel();
        bookingTel2Field = new javax.swing.JTextField();
        jLabelb168 = new javax.swing.JLabel();
        bookingStreet2Field = new javax.swing.JTextField();
        bookingBlackListCheckBox = new javax.swing.JCheckBox();
        bookingPanel = new javax.swing.JLayeredPane();
        jLabelb55 = new javax.swing.JLabel();
        jTextFieldb29 = new javax.swing.JTextField();
        jLabelb53 = new javax.swing.JLabel();
        jLabelb57 = new javax.swing.JLabel();
        toPayField = new javax.swing.JTextField();
        jLabelb52 = new javax.swing.JLabel();
        jLabelb64 = new javax.swing.JLabel();
        jTextFieldb38 = new javax.swing.JTextField();
        jLabelb65 = new javax.swing.JLabel();
        grandTotalField = new javax.swing.JTextField();
        jLabelb66 = new javax.swing.JLabel();
        jTextFieldb40 = new javax.swing.JTextField();
        jLabelb67 = new javax.swing.JLabel();
        jTextFieldb41 = new javax.swing.JTextField();
        jLabelb68 = new javax.swing.JLabel();
        jTextFieldb42 = new javax.swing.JTextField();
        jLabelb69 = new javax.swing.JLabel();
        jTextFieldb43 = new javax.swing.JTextField();
        jLabelb70 = new javax.swing.JLabel();
        jTextFieldb44 = new javax.swing.JTextField();
        jLabelb71 = new javax.swing.JLabel();
        jTextFieldb45 = new javax.swing.JTextField();
        jLabelb74 = new javax.swing.JLabel();
        paidField = new javax.swing.JTextField();
        jCheckBoxb1 = new javax.swing.JCheckBox();
        jLabelb77 = new javax.swing.JLabel();
        jTextFieldb26 = new javax.swing.JTextField();
        jLabelb78 = new javax.swing.JLabel();
        jTextFieldb27 = new javax.swing.JTextField();
        jCheckBoxb2 = new javax.swing.JCheckBox();
        jCheckBoxb3 = new javax.swing.JCheckBox();
        jLabelb81 = new javax.swing.JLabel();
        jTextFieldb35 = new javax.swing.JTextField();
        jLabelb94 = new javax.swing.JLabel();
        jLabelb115 = new javax.swing.JLabel();
        jLabelb79 = new javax.swing.JLabel();
        jTextFieldb48 = new javax.swing.JTextField();
        jLabelb30 = new javax.swing.JLabel();
        jLabelb133 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
        jTextFieldb86 = new javax.swing.JTextField();
        jLabelb175 = new javax.swing.JLabel();
        jTextFieldb87 = new javax.swing.JTextField();
        jLabelb176 = new javax.swing.JLabel();
        jLabelb31 = new javax.swing.JLabel();
        jButtonb22 = new javax.swing.JButton();
        jLabel200 = new javax.swing.JLabel();
        bookingTypeLabel = new javax.swing.JLabel();
        endBookingPanel = new javax.swing.JLayeredPane();
        jButtonb24 = new javax.swing.JButton();
        jButtonb25 = new javax.swing.JButton();
        jCheckBoxb5 = new javax.swing.JCheckBox();
        jComboBoxb9 = new javax.swing.JComboBox<>();
        jLabelb95 = new javax.swing.JLabel();
        jTextFieldb61 = new javax.swing.JTextField();
        jLabelb96 = new javax.swing.JLabel();
        jLabelb118 = new javax.swing.JLabel();
        jLabelb98 = new javax.swing.JLabel();
        jLabelb87 = new javax.swing.JLabel();
        jTextFieldb53 = new javax.swing.JTextField();
        jLabelb89 = new javax.swing.JLabel();
        jTextFieldb54 = new javax.swing.JTextField();
        jLabelb83 = new javax.swing.JLabel();
        jTextFieldb49 = new javax.swing.JTextField();
        jLabelb86 = new javax.swing.JLabel();
        jLabelb88 = new javax.swing.JLabel();
        jTextFieldb50 = new javax.swing.JTextField();
        jTextFieldb51 = new javax.swing.JTextField();
        jTextFieldb52 = new javax.swing.JTextField();
        jLabelb97 = new javax.swing.JLabel();
        jLabelb116 = new javax.swing.JLabel();
        jLabelb120 = new javax.swing.JLabel();
        jLabelb121 = new javax.swing.JLabel();
        jLabelb128 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxb8 = new javax.swing.JCheckBox();
        jCheckBoxb6 = new javax.swing.JCheckBox();
        jLabelb119 = new javax.swing.JLabel();
        bookingExternalPanel = new javax.swing.JLayeredPane();
        jButtonb26 = new javax.swing.JButton();
        jButtonb28 = new javax.swing.JButton();
        jTextFieldb64 = new javax.swing.JTextField();
        jLabelb107 = new javax.swing.JLabel();
        externalBookingTitleLabel = new javax.swing.JLabel();
        jLabelb93 = new javax.swing.JLabel();
        jTextFieldb55 = new javax.swing.JTextField();
        jLabelb136 = new javax.swing.JLabel();
        jTextFieldb56 = new javax.swing.JTextField();
        jLabelb159 = new javax.swing.JLabel();
        jTextFieldb57 = new javax.swing.JTextField();
        jLabelb173 = new javax.swing.JLabel();
        jLabelb174 = new javax.swing.JLabel();
        jLabelb181 = new javax.swing.JLabel();
        allBookingPanel = new javax.swing.JLayeredPane();
        jScrollPaneb2 = new javax.swing.JScrollPane();
        jTableb1 = new javax.swing.JTable();
        jButtonb34 = new javax.swing.JButton();
        jTextFieldb63 = new javax.swing.JTextField();
        jLabelb101 = new javax.swing.JLabel();
        jLabelb102 = new javax.swing.JLabel();
        jComboBoxb10 = new javax.swing.JComboBox<>();
        jLabelb117 = new javax.swing.JLabel();
        jLabelb127 = new javax.swing.JLabel();
        sendEmailPanel = new javax.swing.JLayeredPane();
        jButtonb32 = new javax.swing.JButton();
        jLabelb141 = new javax.swing.JLabel();
        jButtonb35 = new javax.swing.JButton();
        jTextField14 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel159 = new javax.swing.JLabel();
        jLabel160 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jCheckBox3 = new javax.swing.JCheckBox();
        sendEmailPanel3 = new javax.swing.JLayeredPane();
        endEmailCancelButton1 = new javax.swing.JButton();
        jLabelb144 = new javax.swing.JLabel();
        endEmailOkButton1 = new javax.swing.JButton();
        endEmailObjectField1 = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        endEmailTextField1 = new javax.swing.JTextArea();
        jLabel177 = new javax.swing.JLabel();
        jLabel186 = new javax.swing.JLabel();
        jLabel201 = new javax.swing.JLabel();
        linkBillBox1 = new javax.swing.JCheckBox();
        linkContractBox = new javax.swing.JCheckBox();
        sendEmailPanel2 = new javax.swing.JLayeredPane();
        endEmailCancelButton = new javax.swing.JButton();
        jLabelb143 = new javax.swing.JLabel();
        endEmailOkButton = new javax.swing.JButton();
        endEmailObjectField = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        endEmailTextField = new javax.swing.JTextArea();
        jLabel174 = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        jLabel176 = new javax.swing.JLabel();
        linkBillBox = new javax.swing.JCheckBox();
        bookingOption2 = new javax.swing.JLayeredPane();
        jScrollPaneb10 = new javax.swing.JScrollPane();
        jTableb8 = new javax.swing.JTable();
        jLabelb140 = new javax.swing.JLabel();
        jButtonb29 = new javax.swing.JButton();
        bookingOption = new javax.swing.JLayeredPane();
        jScrollPaneb9 = new javax.swing.JScrollPane();
        jTableb7 = new javax.swing.JTable();
        jButtonb23 = new javax.swing.JButton();
        jLabelb139 = new javax.swing.JLabel();
        jButtonb27 = new javax.swing.JButton();
        statPanel = new javax.swing.JLayeredPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton19 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        statMPanel = new javax.swing.JLayeredPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton20 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabelb130 = new javax.swing.JLabel();
        jLabelb131 = new javax.swing.JLabel();
        jLabelb132 = new javax.swing.JLabel();
        jLabelb113 = new javax.swing.JLabel();
        jLabelb104 = new javax.swing.JLabel();
        jLabelb105 = new javax.swing.JLabel();
        jLabelb106 = new javax.swing.JLabel();
        jLabelb135 = new javax.swing.JLabel();
        jLabelb129 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setUndecorated(true);
        setSize(new java.awt.Dimension(0, 0));

        jPanel1.setPreferredSize(new java.awt.Dimension(0, 0));

        jLabelHomeSelector.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelHomeSelector.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHomeSelector.setText("__________");
        jLabelHomeSelector.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabelHomeSelectorFocusGained(evt);
            }
        });

        jLabelBookingSelector.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelBookingSelector.setForeground(new java.awt.Color(255, 255, 255));
        jLabelBookingSelector.setText("__________");
        jLabelBookingSelector.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabelBookingSelectorFocusGained(evt);
            }
        });

        jLabelSignOut.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelSignOut.setForeground(new java.awt.Color(0, 255, 255));
        jLabelSignOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sign-out_3.png"))); // NOI18N
        jLabelSignOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSignOutMouseClicked(evt);
            }
        });

        jLabelHome.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelHome.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/home_2.png"))); // NOI18N
        jLabelHome.setText("Mon Espace");
        jLabelHome.setIgnoreRepaint(true);
        jLabelHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelHomeMouseClicked(evt);
            }
        });

        jLabelBooking.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelBooking.setForeground(new java.awt.Color(255, 255, 255));
        jLabelBooking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/beds_1.png"))); // NOI18N
        jLabelBooking.setText("Réservation");
        jLabelBooking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelBookingMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelBookingMouseEntered(evt);
            }
        });

        jLabelClose.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelClose.setForeground(new java.awt.Color(102, 102, 102));
        jLabelClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close_2.png"))); // NOI18N
        jLabelClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelCloseMouseClicked(evt);
            }
        });

        jPanel2.setLocation(new java.awt.Point(0, 10));
        jPanel2.setOpaque(false);

        homePanel.setBackground(new java.awt.Color(204, 204, 204));
        homePanel.setPreferredSize(new java.awt.Dimension(0, 0));
        homePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                homePanelComponentShown(evt);
            }
        });

        createDocPanel.setBackground(new java.awt.Color(255, 255, 255));
        createDocPanel.setForeground(new java.awt.Color(204, 204, 204));
        createDocPanel.setOpaque(true);

        jLabel10.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 153, 204));
        jLabel10.setText("Mon Contrat");
        createDocPanel.add(jLabel10);
        jLabel10.setBounds(20, 10, 190, 30);

        contratTxt.setColumns(20);
        contratTxt.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        contratTxt.setLineWrap(true);
        contratTxt.setRows(5);
        contratTxt.setToolTipText("Entête, date et numero de contrat seront créés automatiquement.");
        contratTxt.setWrapStyleWord(true);
        contratTxt.setBorder(null);
        contratTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contratTxtMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(contratTxt);

        createDocPanel.add(jScrollPane3);
        jScrollPane3.setBounds(20, 210, 750, 340);

        autoLabel1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel1.setText("<adulte>");
        autoLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel1MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel1);
        autoLabel1.setBounds(790, 150, 130, 15);

        autoLabel4.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel4.setText("<lit simple>");
        autoLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel4MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel4);
        autoLabel4.setBounds(940, 170, 130, 15);

        autoLabel8.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel8.setText("<nuit>");
        autoLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel8MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel8);
        autoLabel8.setBounds(940, 210, 130, 15);

        autoLabel17.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel17.setText("<taxe de séjour>");
        autoLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel17MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel17);
        autoLabel17.setBounds(790, 310, 120, 15);

        autoLabel2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel2.setText("<enfant>");
        autoLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel2MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel2);
        autoLabel2.setBounds(940, 150, 130, 15);

        autoLabel3.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel3.setText("<lit double>");
        autoLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel3MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel3);
        autoLabel3.setBounds(790, 170, 130, 15);

        autoLabel11.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel11.setText("<prix jour>");
        autoLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel11MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel11);
        autoLabel11.setBounds(790, 250, 130, 15);

        autoLabel18.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel18.setText("<TOTAL>");
        autoLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel18MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel18);
        autoLabel18.setBounds(940, 310, 120, 15);

        autoLabel12.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel12.setText("<montant>");
        autoLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel12MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel12);
        autoLabel12.setBounds(940, 250, 130, 15);

        autoLabel6.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel6.setText("<nom location>");
        autoLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel6MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel6);
        autoLabel6.setBounds(940, 190, 120, 15);

        autoLabel5.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel5.setText("<type de location>");
        autoLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel5MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel5);
        autoLabel5.setBounds(790, 190, 130, 15);

        autoLabel13.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel13.setText("<arrhes>");
        autoLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel13MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel13);
        autoLabel13.setBounds(790, 270, 120, 15);

        jLabel11.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 15)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Selectionner un Paragraphe");
        jLabel11.setToolTipText("Élement remplacé par les informations lié à la réservation concernée.");
        createDocPanel.add(jLabel11);
        jLabel11.setBounds(20, 60, 240, 20);

        jLabel50.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 11)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 153, 204));
        jLabel50.setText("*Clique sur un élément pour l'ajouter au texte.");
        createDocPanel.add(jLabel50);
        jLabel50.setBounds(790, 120, 280, 20);

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(102, 102, 102));
        jButton5.setText("Enregistrer");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        createDocPanel.add(jButton5);
        jButton5.setBounds(30, 580, 190, 35);

        autoLabel15.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel15.setText("<option>");
        autoLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel15MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel15);
        autoLabel15.setBounds(790, 290, 120, 15);

        autoLabel16.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel16.setText("<total option>");
        autoLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel16MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel16);
        autoLabel16.setBounds(940, 290, 120, 15);

        jLabel147.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel147.setForeground(new java.awt.Color(102, 102, 102));
        jLabel147.setText("Coller");
        jLabel147.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel147MouseClicked(evt);
            }
        });
        createDocPanel.add(jLabel147);
        jLabel147.setBounds(720, 190, 50, 17);

        jLabel148.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(102, 102, 102));
        jLabel148.setText("Copier");
        jLabel148.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel148MouseClicked(evt);
            }
        });
        createDocPanel.add(jLabel148);
        jLabel148.setBounds(650, 190, 60, 17);

        autoLabel10.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel10.setText("<départ>");
        autoLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel10MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel10);
        autoLabel10.setBounds(940, 230, 130, 15);

        autoLabel9.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel9.setText("<arrivée>");
        autoLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel9MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel9);
        autoLabel9.setBounds(790, 230, 130, 15);

        contratTitle.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        contratTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contratTitleActionPerformed(evt);
            }
        });
        createDocPanel.add(contratTitle);
        contratTitle.setBounds(20, 150, 750, 30);

        jComboBox6.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox6.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Description", "Propriétaire", "Propriété", "Client", "Réservation", "Informations Importantes", "Signature" }));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });
        createDocPanel.add(jComboBox6);
        jComboBox6.setBounds(20, 90, 350, 27);

        autoLabel7.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel7.setText("<adresse loc>");
        autoLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel7MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel7);
        autoLabel7.setBounds(790, 210, 120, 15);

        jLabel13.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Éléments Remplaçables");
        jLabel13.setToolTipText("Élement remplacé par les informations lié à la réservation concernée.");
        createDocPanel.add(jLabel13);
        jLabel13.setBounds(790, 60, 260, 20);

        autoLabel14.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabel14.setText("<caution>");
        autoLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabel14MouseClicked(evt);
            }
        });
        createDocPanel.add(autoLabel14);
        autoLabel14.setBounds(940, 270, 130, 15);

        jComboBox7.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox7.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Client", "Réservation", "Mes Informations", "Mes Liens Internet" }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });
        createDocPanel.add(jComboBox7);
        jComboBox7.setBounds(790, 90, 220, 27);

        createEmailPanel.setBackground(new java.awt.Color(255, 255, 255));
        createEmailPanel.setOpaque(true);

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setWrapStyleWord(true);
        jScrollPane4.setViewportView(jTextArea2);

        createEmailPanel.add(jScrollPane4);
        jScrollPane4.setBounds(20, 200, 710, 320);

        autoLabelb1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb1.setText("<adulte>");
        autoLabelb1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb1MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb1);
        autoLabelb1.setBounds(780, 140, 130, 15);

        autoLabelb4.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb4.setText("<lit simple>");
        autoLabelb4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb4MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb4);
        autoLabelb4.setBounds(930, 160, 130, 15);

        autoLabelb8.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb8.setText("<nuit>");
        autoLabelb8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb8MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb8);
        autoLabelb8.setBounds(930, 200, 130, 15);

        autoLabelb17.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb17.setText("<taxe de séjour>");
        autoLabelb17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb17MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb17);
        autoLabelb17.setBounds(780, 300, 130, 15);

        autoLabelb2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb2.setText("<enfant>");
        autoLabelb2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb2MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb2);
        autoLabelb2.setBounds(930, 140, 130, 15);

        autoLabelb3.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb3.setText("<lit double>");
        autoLabelb3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb3MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb3);
        autoLabelb3.setBounds(780, 160, 130, 15);

        autoLabelb11.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb11.setText("<prix jour>");
        autoLabelb11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb11MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb11);
        autoLabelb11.setBounds(780, 240, 130, 15);

        autoLabelb18.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb18.setText("<TOTAL>");
        autoLabelb18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb18MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb18);
        autoLabelb18.setBounds(930, 300, 130, 15);

        autoLabelb12.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb12.setText("<montant>");
        autoLabelb12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb12MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb12);
        autoLabelb12.setBounds(930, 240, 130, 15);

        autoLabelb6.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb6.setText("<nom location>");
        autoLabelb6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb6MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb6);
        autoLabelb6.setBounds(930, 180, 130, 15);

        autoLabelb5.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb5.setText("<type de location>");
        autoLabelb5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb5MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb5);
        autoLabelb5.setBounds(780, 180, 130, 15);

        autoLabelb15.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb15.setText("<arrhes>");
        autoLabelb15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb15MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb15);
        autoLabelb15.setBounds(780, 280, 130, 15);

        jLabel87.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 15)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(102, 102, 102));
        jLabel87.setText("Selectioner un Email");
        createEmailPanel.add(jLabel87);
        jLabel87.setBounds(20, 50, 190, 20);

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(102, 102, 102));
        jButton6.setText("Enregistrer");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        createEmailPanel.add(jButton6);
        jButton6.setBounds(20, 560, 190, 35);

        jButton10.setBackground(new java.awt.Color(255, 255, 255));
        jButton10.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(102, 102, 102));
        jButton10.setText("Anuler");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        createEmailPanel.add(jButton10);
        jButton10.setBounds(280, 560, 190, 35);

        jLabel89.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 11)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(0, 153, 204));
        jLabel89.setText("*Click sur un élément pour l'ajouter au texte.");
        createEmailPanel.add(jLabel89);
        jLabel89.setBounds(780, 110, 280, 20);

        jTextField12.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        createEmailPanel.add(jTextField12);
        jTextField12.setBounds(20, 140, 710, 30);

        jComboBox2.setBackground(new java.awt.Color(200, 200, 200));
        jComboBox2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nouvelle Réservation ", "Nouvelle Réservation (sans contrat)", "Réservation Confirmée", "Réservation Non Confirmée", "Fin de Séjour (avec facture)", "Fin de Séjour " }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        createEmailPanel.add(jComboBox2);
        jComboBox2.setBounds(20, 80, 317, 27);

        jLabel1.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 204));
        jLabel1.setText("Objet:");
        createEmailPanel.add(jLabel1);
        jLabel1.setBounds(20, 120, 120, 17);

        jLabel51.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 153, 204));
        jLabel51.setText("Message:");
        createEmailPanel.add(jLabel51);
        jLabel51.setBounds(20, 180, 100, 17);

        jLabel116.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel116.setForeground(new java.awt.Color(0, 153, 204));
        jLabel116.setText("Mes Emails");
        createEmailPanel.add(jLabel116);
        jLabel116.setBounds(20, 10, 200, 22);

        autoLabelb13.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb13.setText("<option>");
        autoLabelb13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb13MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb13);
        autoLabelb13.setBounds(780, 260, 130, 15);

        autoLabelb14.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb14.setText("<total option>");
        autoLabelb14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb14MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb14);
        autoLabelb14.setBounds(930, 260, 130, 15);

        jLabel151.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel151.setForeground(new java.awt.Color(102, 102, 102));
        jLabel151.setText("Copier");
        jLabel151.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel151MouseClicked(evt);
            }
        });
        createEmailPanel.add(jLabel151);
        jLabel151.setBounds(580, 180, 61, 16);

        jLabel152.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel152.setForeground(new java.awt.Color(102, 102, 102));
        jLabel152.setText("Coller");
        jLabel152.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel152MouseClicked(evt);
            }
        });
        createEmailPanel.add(jLabel152);
        jLabel152.setBounds(660, 180, 61, 16);

        autoLabelb9.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb9.setText("<arrivée>");
        autoLabelb9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb9MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb9);
        autoLabelb9.setBounds(780, 220, 130, 15);

        autoLabelb10.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb10.setText("<départ>");
        autoLabelb10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb10MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb10);
        autoLabelb10.setBounds(930, 220, 130, 15);

        autoLabelb7.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb7.setText("<adresse loc>");
        autoLabelb7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb7MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb7);
        autoLabelb7.setBounds(780, 200, 130, 15);

        jLabel88.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(102, 102, 102));
        jLabel88.setText("Éléments Remplaçables");
        createEmailPanel.add(jLabel88);
        jLabel88.setBounds(780, 50, 260, 20);

        autoLabelb16.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        autoLabelb16.setText("<caution>");
        autoLabelb16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                autoLabelb16MouseClicked(evt);
            }
        });
        createEmailPanel.add(autoLabelb16);
        autoLabelb16.setBounds(930, 280, 130, 15);

        jComboBox8.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox8.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Client", "Réservation", "Mes Informations", "Mes Liens Internet", " " }));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });
        createEmailPanel.add(jComboBox8);
        jComboBox8.setBounds(780, 80, 240, 27);

        myPanel.setBackground(new java.awt.Color(255, 255, 255));
        myPanel.setOpaque(true);
        myPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                myPanelComponentShown(evt);
            }
        });

        jLabel72.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(0, 153, 204));
        jLabel72.setText("Mes Informations");
        myPanel.add(jLabel72);
        jLabel72.setBounds(20, 20, 250, 30);

        jLabel73.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(102, 102, 102));
        jLabel73.setText("Les informations renseignées apparaîtront dans vos contrats, vos factures et vos emails.");
        myPanel.add(jLabel73);
        jLabel73.setBounds(20, 50, 690, 17);
        myPanel.add(jLabel71);
        jLabel71.setBounds(10, 10, 0, 0);

        smtpWindow.setBackground(new java.awt.Color(255, 255, 255));
        smtpWindow.setBorder(null);
        smtpWindow.setClosable(true);
        smtpWindow.setVisible(false);

        jButton3.setText("Ok");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton7.setText("Anuler");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel15.setText("Host:");

        jLabel16.setText("Port:");

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel17.setText("Mot de passe:");

        jLabel18.setText("Serveur SMTP sortant:");

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 102, 204));
        jButton8.setText("Tester ");
        jButton8.setBorder(null);
        jButton8.setBorderPainted(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jPasswordField1.setText("jPasswordField1");

        javax.swing.GroupLayout smtpWindowLayout = new javax.swing.GroupLayout(smtpWindow.getContentPane());
        smtpWindow.getContentPane().setLayout(smtpWindowLayout);
        smtpWindowLayout.setHorizontalGroup(
            smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(smtpWindowLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(smtpWindowLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(smtpWindowLayout.createSequentialGroup()
                        .addGroup(smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(67, 67, 67)
                        .addGroup(smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, smtpWindowLayout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(156, Short.MAX_VALUE))
        );
        smtpWindowLayout.setVerticalGroup(
            smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, smtpWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addGap(10, 10, 10)
                .addGroup(smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(smtpWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton7))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        myPanel.add(smtpWindow);
        smtpWindow.setBounds(100, 290, 337, 300);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setOpaque(false);
        jPanel7.setLayout(null);

        jLabelh2.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelh2.setForeground(new java.awt.Color(0, 153, 204));
        jLabelh2.setText("Votre Compagnie*");
        jPanel7.add(jLabelh2);
        jLabelh2.setBounds(10, 10, 200, 17);

        myCompanyField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myCompanyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myCompanyFieldActionPerformed(evt);
            }
        });
        jPanel7.add(myCompanyField);
        myCompanyField.setBounds(10, 30, 260, 30);

        jLabel7.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 153, 204));
        jLabel7.setText("Votre Téléphone 2");
        jPanel7.add(jLabel7);
        jLabel7.setBounds(10, 250, 200, 17);

        jLabel8.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 153, 204));
        jLabel8.setText("Votre E-mail ");
        jPanel7.add(jLabel8);
        jLabel8.setBounds(10, 310, 270, 17);

        myEmailField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myEmailField.setToolTipText("Votre Email adresse est utilisé pour l'envoi de vos emails .");
        myEmailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myEmailFieldFocusLost(evt);
            }
        });
        jPanel7.add(myEmailField);
        myEmailField.setBounds(10, 330, 260, 30);

        jLabelh7.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelh7.setForeground(new java.awt.Color(0, 153, 204));
        jLabelh7.setText("Votre Nom*");
        jPanel7.add(jLabelh7);
        jLabelh7.setBounds(10, 70, 200, 17);

        myNameField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myNameFieldActionPerformed(evt);
            }
        });
        jPanel7.add(myNameField);
        myNameField.setBounds(10, 90, 260, 30);

        jLabelh8.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelh8.setForeground(new java.awt.Color(0, 153, 204));
        jLabelh8.setText("Votre Prénom*");
        jPanel7.add(jLabelh8);
        jLabelh8.setBounds(10, 130, 200, 17);

        myFirstNameField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myFirstNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myFirstNameFieldActionPerformed(evt);
            }
        });
        jPanel7.add(myFirstNameField);
        myFirstNameField.setBounds(10, 150, 260, 30);

        jLabel199.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel199.setForeground(new java.awt.Color(0, 153, 204));
        jLabel199.setText("Votre Téléphone*");
        jPanel7.add(jLabel199);
        jLabel199.setBounds(10, 190, 200, 17);

        myPhone2Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myPhone2Field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myPhone2FieldFocusLost(evt);
            }
        });
        jPanel7.add(myPhone2Field);
        myPhone2Field.setBounds(10, 270, 260, 30);

        myPhoneField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myPhoneField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myPhoneFieldFocusLost(evt);
            }
        });
        jPanel7.add(myPhoneField);
        myPhoneField.setBounds(10, 210, 260, 30);

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setForeground(new java.awt.Color(102, 102, 102));
        jButton2.setText("Configurer votre adresse e-mail");
        jButton2.setBorderPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton2);
        jButton2.setBounds(10, 390, 250, 29);

        myPanel.add(jPanel7);
        jPanel7.setBounds(10, 80, 310, 460);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setOpaque(false);
        jPanel8.setLayout(null);

        mySiretField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        mySiretField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mySiretFieldFocusLost(evt);
            }
        });
        mySiretField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mySiretFieldKeyPressed(evt);
            }
        });
        jPanel8.add(mySiretField);
        mySiretField.setBounds(20, 330, 290, 30);

        jLabel97.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(0, 153, 204));
        jLabel97.setText("Votre Numéro De Siret ");
        jPanel8.add(jLabel97);
        jLabel97.setBounds(20, 310, 300, 17);

        myCityField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myCityField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myCityFieldFocusLost(evt);
            }
        });
        jPanel8.add(myCityField);
        myCityField.setBounds(20, 270, 290, 30);

        jLabelh5.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelh5.setForeground(new java.awt.Color(0, 153, 204));
        jLabelh5.setText("Votre Ville*");
        jPanel8.add(jLabelh5);
        jLabelh5.setBounds(20, 250, 230, 17);

        myPostCodeField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myPostCodeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myPostCodeFieldFocusLost(evt);
            }
        });
        jPanel8.add(myPostCodeField);
        myPostCodeField.setBounds(20, 210, 290, 30);

        jLabelh4.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelh4.setForeground(new java.awt.Color(0, 153, 204));
        jLabelh4.setText("Votre Code Postal*");
        jPanel8.add(jLabelh4);
        jLabelh4.setBounds(20, 190, 230, 17);

        myStreet1Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myStreet1Field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myStreet1FieldFocusLost(evt);
            }
        });
        jPanel8.add(myStreet1Field);
        myStreet1Field.setBounds(20, 30, 290, 30);

        jLabelh3.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelh3.setForeground(new java.awt.Color(0, 153, 204));
        jLabelh3.setText("Votre Adresse (Siège Social)*");
        jPanel8.add(jLabelh3);
        jLabelh3.setBounds(20, 10, 230, 17);

        jLabelh6.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelh6.setForeground(new java.awt.Color(0, 153, 204));
        jLabelh6.setText("Votre Commune Déléguée");
        jPanel8.add(jLabelh6);
        jLabelh6.setBounds(20, 130, 230, 17);

        myDcField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myDcField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myDcFieldFocusLost(evt);
            }
        });
        myDcField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myDcFieldActionPerformed(evt);
            }
        });
        jPanel8.add(myDcField);
        myDcField.setBounds(20, 150, 290, 30);

        jLabel167.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel167.setForeground(new java.awt.Color(0, 153, 204));
        jLabel167.setText("Votre Code APE");
        jPanel8.add(jLabel167);
        jLabel167.setBounds(20, 370, 190, 17);

        myApeField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myApeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myApeFieldFocusLost(evt);
            }
        });
        myApeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                myApeFieldKeyPressed(evt);
            }
        });
        jPanel8.add(myApeField);
        myApeField.setBounds(20, 390, 290, 30);

        jLabel6.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 153, 204));
        jLabel6.setText("Votre Adresse 2");
        jPanel8.add(jLabel6);
        jLabel6.setBounds(20, 70, 230, 17);

        myStreet2Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myStreet2Field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                myStreet2FieldFocusLost(evt);
            }
        });
        jPanel8.add(myStreet2Field);
        myStreet2Field.setBounds(20, 90, 290, 30);

        myPanel.add(jPanel8);
        jPanel8.setBounds(330, 80, 330, 430);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setOpaque(false);
        jPanel9.setLayout(null);

        jLabel98.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(0, 153, 204));
        jLabel98.setText("Votre Site Internet");
        jPanel9.add(jLabel98);
        jLabel98.setBounds(16, 6, 270, 17);

        websiteField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        websiteField.setToolTipText("Sera automatiquement ajouté a l'entête de vos document.");
        websiteField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                websiteFieldFocusLost(evt);
            }
        });
        jPanel9.add(websiteField);
        websiteField.setBounds(86, 26, 300, 30);

        jLabel99.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(0, 153, 204));
        jLabel99.setText("Facebook");
        jPanel9.add(jLabel99);
        jLabel99.setBounds(16, 66, 270, 17);

        facebookField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        facebookField.setToolTipText("Sera automatiquement ajouté a l'entête de vos document.");
        facebookField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                facebookFieldFocusLost(evt);
            }
        });
        jPanel9.add(facebookField);
        facebookField.setBounds(86, 86, 300, 30);

        instaField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        instaField.setToolTipText("Sera automatiquement ajouté a l'entête de vos document.");
        instaField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                instaFieldFocusLost(evt);
            }
        });
        jPanel9.add(instaField);
        instaField.setBounds(86, 146, 300, 30);

        jLabel9.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 153, 204));
        jLabel9.setText("Instagram");
        jPanel9.add(jLabel9);
        jLabel9.setBounds(16, 126, 270, 17);

        jLabel96.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(0, 153, 204));
        jLabel96.setText("Avis Google");
        jPanel9.add(jLabel96);
        jLabel96.setBounds(16, 186, 270, 17);

        googletField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        googletField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                googletFieldFocusLost(evt);
            }
        });
        jPanel9.add(googletField);
        googletField.setBounds(86, 206, 300, 30);

        jLabel100.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(0, 153, 204));
        jLabel100.setText("Avis Trip Advisor");
        jPanel9.add(jLabel100);
        jLabel100.setBounds(16, 246, 270, 17);

        tripAdvisorField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        tripAdvisorField.setToolTipText("peut être ajouter à votre email de fin se séjour.");
        tripAdvisorField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tripAdvisorFieldFocusLost(evt);
            }
        });
        jPanel9.add(tripAdvisorField);
        tripAdvisorField.setBounds(86, 266, 300, 30);

        jLabel101.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(0, 153, 204));
        jLabel101.setText("Autre Lien Internet");
        jPanel9.add(jLabel101);
        jLabel101.setBounds(16, 306, 270, 17);

        otherReviewField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        otherReviewField.setToolTipText("peut être ajouter à votre email de fin se séjour.");
        otherReviewField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                otherReviewFieldFocusLost(evt);
            }
        });
        otherReviewField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                otherReviewFieldKeyPressed(evt);
            }
        });
        jPanel9.add(otherReviewField);
        otherReviewField.setBounds(86, 326, 300, 30);

        jLabel183.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel183.setForeground(new java.awt.Color(102, 102, 102));
        jLabel183.setText("Coller");
        jLabel183.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel183MouseClicked(evt);
            }
        });
        jPanel9.add(jLabel183);
        jLabel183.setBounds(20, 30, 50, 17);

        jLabel182.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel182.setForeground(new java.awt.Color(102, 102, 102));
        jLabel182.setText("Coller");
        jLabel182.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel182MouseClicked(evt);
            }
        });
        jPanel9.add(jLabel182);
        jLabel182.setBounds(20, 90, 50, 17);

        jLabel181.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel181.setForeground(new java.awt.Color(102, 102, 102));
        jLabel181.setText("Coller");
        jLabel181.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel181MouseClicked(evt);
            }
        });
        jPanel9.add(jLabel181);
        jLabel181.setBounds(20, 150, 50, 17);

        jLabel180.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel180.setForeground(new java.awt.Color(102, 102, 102));
        jLabel180.setText("Coller");
        jLabel180.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel180MouseClicked(evt);
            }
        });
        jPanel9.add(jLabel180);
        jLabel180.setBounds(20, 210, 50, 17);

        jLabel179.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel179.setForeground(new java.awt.Color(102, 102, 102));
        jLabel179.setText("Coller");
        jLabel179.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel179MouseClicked(evt);
            }
        });
        jPanel9.add(jLabel179);
        jLabel179.setBounds(20, 270, 50, 17);

        jLabel178.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel178.setForeground(new java.awt.Color(102, 102, 102));
        jLabel178.setText("Coller");
        jLabel178.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel178MouseClicked(evt);
            }
        });
        jPanel9.add(jLabel178);
        jLabel178.setBounds(20, 330, 50, 17);

        jLabel117.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel117.setForeground(new java.awt.Color(0, 153, 204));
        jLabel117.setText("Mon Logo (jpg, gif, png)");
        jPanel9.add(jLabel117);
        jLabel117.setBounds(20, 370, 210, 17);

        jLabel187.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel187.setForeground(new java.awt.Color(102, 102, 102));
        jLabel187.setText("Ajouter");
        jLabel187.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel187MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel187MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel187MouseExited(evt);
            }
        });
        jPanel9.add(jLabel187);
        jLabel187.setBounds(20, 420, 60, 17);

        jLabel188.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel188.setForeground(new java.awt.Color(102, 102, 102));
        jLabel188.setText("Supprimer");
        jLabel188.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel188MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel188MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel188MouseExited(evt);
            }
        });
        jPanel9.add(jLabel188);
        jLabel188.setBounds(100, 420, 80, 20);

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 13)); // NOI18N
        jCheckBox2.setText("Importé");
        jCheckBox2.setBorder(null);
        jCheckBox2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCheckBox2FocusLost(evt);
            }
        });
        jPanel9.add(jCheckBox2);
        jCheckBox2.setBounds(200, 390, 80, 20);

        myLogoField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        myLogoField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                myLogoFieldKeyPressed(evt);
            }
        });
        jPanel9.add(myLogoField);
        myLogoField.setBounds(20, 390, 160, 30);

        myPanel.add(jPanel9);
        jPanel9.setBounds(680, 80, 390, 450);

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(102, 102, 102));
        jButton4.setText("Enregistrer");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        myPanel.add(jButton4);
        jButton4.setBounds(20, 550, 200, 35);

        myRentalPanel.setBackground(new java.awt.Color(255, 255, 255));
        myRentalPanel.setOpaque(true);
        myRentalPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                myRentalPanelComponentShown(evt);
            }
        });
        myRentalPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        newRentalPanel.setOpaque(false);
        newRentalPanel.setLayout(null);

        rentalTypeLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        rentalTypeLabel.setForeground(new java.awt.Color(0, 153, 204));
        rentalTypeLabel.setText("Type De Location");
        newRentalPanel.add(rentalTypeLabel);
        rentalTypeLabel.setBounds(30, 35, 125, 17);

        selectTypeBox.setBackground(new java.awt.Color(255, 255, 255));
        selectTypeBox.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        selectTypeBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chambre", "Maison", "Dortoir" }));
        selectTypeBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectTypeBoxMouseClicked(evt);
            }
        });
        selectTypeBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectTypeBoxActionPerformed(evt);
            }
        });
        newRentalPanel.add(selectTypeBox);
        selectTypeBox.setBounds(30, 55, 330, 35);

        roomPanel.setOpaque(false);
        roomPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        roomPanel.setLayout(null);

        nameRoomLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        nameRoomLabel.setForeground(new java.awt.Color(0, 153, 204));
        nameRoomLabel.setText("Nom / Numéro");
        roomPanel.add(nameRoomLabel);
        nameRoomLabel.setBounds(10, 130, 109, 17);

        nameRoomTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        nameRoomTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameRoomTextFieldActionPerformed(evt);
            }
        });
        roomPanel.add(nameRoomTextField);
        nameRoomTextField.setBounds(10, 150, 330, 30);

        singleRoomBedLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        singleRoomBedLabel.setForeground(new java.awt.Color(0, 153, 204));
        singleRoomBedLabel.setText("Lit Simple");
        roomPanel.add(singleRoomBedLabel);
        singleRoomBedLabel.setBounds(10, 190, 73, 17);

        singleBedRoomTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        singleBedRoomTextField.setText("0");
        singleBedRoomTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                singleBedRoomTextFieldFocusGained(evt);
            }
        });
        singleBedRoomTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                singleBedRoomTextFieldMouseClicked(evt);
            }
        });
        singleBedRoomTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleBedRoomTextFieldActionPerformed(evt);
            }
        });
        roomPanel.add(singleBedRoomTextField);
        singleBedRoomTextField.setBounds(10, 210, 330, 30);

        doubleBedRoomLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        doubleBedRoomLabel.setForeground(new java.awt.Color(0, 153, 204));
        doubleBedRoomLabel.setText("Lit Double");
        roomPanel.add(doubleBedRoomLabel);
        doubleBedRoomLabel.setBounds(10, 250, 75, 17);

        doubleBedRoomTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        doubleBedRoomTextField.setText("0");
        doubleBedRoomTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doubleBedRoomTextFieldFocusGained(evt);
            }
        });
        doubleBedRoomTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doubleBedRoomTextFieldMouseClicked(evt);
            }
        });
        doubleBedRoomTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleBedRoomTextFieldActionPerformed(evt);
            }
        });
        roomPanel.add(doubleBedRoomTextField);
        doubleBedRoomTextField.setBounds(10, 270, 330, 30);

        amountRoomLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        amountRoomLabel.setForeground(new java.awt.Color(0, 153, 204));
        amountRoomLabel.setText("Montant");
        roomPanel.add(amountRoomLabel);
        amountRoomLabel.setBounds(10, 310, 60, 17);

        amountRoomTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        amountRoomTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                amountRoomTextFieldKeyPressed(evt);
            }
        });
        roomPanel.add(amountRoomTextField);
        amountRoomTextField.setBounds(10, 330, 330, 30);

        addRoomButton.setBackground(new java.awt.Color(255, 255, 255));
        addRoomButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        addRoomButton.setForeground(new java.awt.Color(102, 102, 102));
        addRoomButton.setText("Ajouter");
        addRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRoomButtonActionPerformed(evt);
            }
        });
        roomPanel.add(addRoomButton);
        addRoomButton.setBounds(10, 430, 100, 30);

        buildingTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        roomPanel.add(buildingTextField);
        buildingTextField.setBounds(8, 31, 330, 30);

        typeRoomLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        typeRoomLabel.setForeground(new java.awt.Color(0, 153, 204));
        typeRoomLabel.setText("Propriété");
        roomPanel.add(typeRoomLabel);
        typeRoomLabel.setBounds(10, 10, 66, 17);

        cancelRoomButton.setBackground(new java.awt.Color(255, 255, 255));
        cancelRoomButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        cancelRoomButton.setForeground(new java.awt.Color(102, 102, 102));
        cancelRoomButton.setText("Anuler");
        cancelRoomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelRoomButtonActionPerformed(evt);
            }
        });
        roomPanel.add(cancelRoomButton);
        cancelRoomButton.setBounds(180, 430, 94, 30);

        typeRoomLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        typeRoomLabel1.setForeground(new java.awt.Color(0, 153, 204));
        typeRoomLabel1.setText("Adresse de la Propriété");
        roomPanel.add(typeRoomLabel1);
        typeRoomLabel1.setBounds(10, 70, 190, 17);

        adressRoomTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        roomPanel.add(adressRoomTextField);
        adressRoomTextField.setBounds(10, 90, 330, 30);

        taxRoomLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        taxRoomLabel.setForeground(new java.awt.Color(0, 153, 204));
        taxRoomLabel.setText("Taxe de Séjour");
        roomPanel.add(taxRoomLabel);
        taxRoomLabel.setBounds(10, 370, 140, 17);

        taxRoomTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        taxRoomTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                taxRoomTextFieldKeyPressed(evt);
            }
        });
        roomPanel.add(taxRoomTextField);
        taxRoomTextField.setBounds(10, 390, 330, 30);

        newRentalPanel.add(roomPanel);
        roomPanel.setBounds(25, 85, 370, 470);

        housePanel.setOpaque(false);
        housePanel.setPreferredSize(new java.awt.Dimension(360, 360));
        housePanel.setLayout(null);

        qtyRoomHouseLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        qtyRoomHouseLabel.setForeground(new java.awt.Color(0, 153, 204));
        qtyRoomHouseLabel.setText("Qté Chambre(s)");
        housePanel.add(qtyRoomHouseLabel);
        qtyRoomHouseLabel.setBounds(10, 70, 160, 17);

        qtyRoomHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        qtyRoomHouseTextField.setText("0");
        qtyRoomHouseTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                qtyRoomHouseTextFieldFocusGained(evt);
            }
        });
        qtyRoomHouseTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qtyRoomHouseTextFieldMouseClicked(evt);
            }
        });
        qtyRoomHouseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qtyRoomHouseTextFieldActionPerformed(evt);
            }
        });
        qtyRoomHouseTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtyRoomHouseTextFieldKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                qtyRoomHouseTextFieldKeyTyped(evt);
            }
        });
        housePanel.add(qtyRoomHouseTextField);
        qtyRoomHouseTextField.setBounds(8, 93, 330, 30);

        nameHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        nameHouseTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameHouseTextFieldFocusGained(evt);
            }
        });
        nameHouseTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nameHouseTextFieldMouseClicked(evt);
            }
        });
        housePanel.add(nameHouseTextField);
        nameHouseTextField.setBounds(8, 31, 330, 30);

        nameHouseLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        nameHouseLabel.setForeground(new java.awt.Color(0, 153, 204));
        nameHouseLabel.setText("Propriété");
        housePanel.add(nameHouseLabel);
        nameHouseLabel.setBounds(8, 8, 120, 17);

        addHouseButton.setBackground(new java.awt.Color(255, 255, 255));
        addHouseButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        addHouseButton.setForeground(new java.awt.Color(102, 102, 102));
        addHouseButton.setText("Ajouter");
        addHouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addHouseButtonActionPerformed(evt);
            }
        });
        housePanel.add(addHouseButton);
        addHouseButton.setBounds(20, 390, 110, 30);

        cancelHouseButton.setBackground(new java.awt.Color(255, 255, 255));
        cancelHouseButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        cancelHouseButton.setForeground(new java.awt.Color(102, 102, 102));
        cancelHouseButton.setText("Anuler");
        cancelHouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelHouseButtonActionPerformed(evt);
            }
        });
        housePanel.add(cancelHouseButton);
        cancelHouseButton.setBounds(180, 390, 100, 30);

        enterHouseButton.setBackground(new java.awt.Color(255, 255, 255));
        enterHouseButton.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        enterHouseButton.setText("Entrer");
        enterHouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterHouseButtonActionPerformed(evt);
            }
        });
        housePanel.add(enterHouseButton);
        enterHouseButton.setBounds(10, 130, 97, 29);

        amountHouseLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        amountHouseLabel.setForeground(new java.awt.Color(0, 153, 204));
        amountHouseLabel.setText("Adresse de la Propriété");
        housePanel.add(amountHouseLabel);
        amountHouseLabel.setBounds(10, 170, 280, 17);

        amountHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        amountHouseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amountHouseTextFieldActionPerformed(evt);
            }
        });
        amountHouseTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                amountHouseTextFieldKeyPressed(evt);
            }
        });
        housePanel.add(amountHouseTextField);
        amountHouseTextField.setBounds(10, 240, 320, 30);

        boundHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        boundHouseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boundHouseTextFieldActionPerformed(evt);
            }
        });
        housePanel.add(boundHouseTextField);
        boundHouseTextField.setBounds(10, 290, 320, 30);

        boundHouseLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        boundHouseLabel.setForeground(new java.awt.Color(0, 153, 204));
        boundHouseLabel.setText("Dépot de Garantie (Prix Indicatif)");
        housePanel.add(boundHouseLabel);
        boundHouseLabel.setBounds(10, 270, 300, 17);
        housePanel.add(singleBedStoredLabel);
        singleBedStoredLabel.setBounds(50, 90, 270, 20);
        housePanel.add(doubleBedStoredLabel);
        doubleBedStoredLabel.setBounds(50, 130, 250, 20);
        housePanel.add(roomStoredLabel);
        roomStoredLabel.setBounds(20, 66, 290, 20);

        boundHouseLabel1.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        boundHouseLabel1.setForeground(new java.awt.Color(0, 153, 204));
        boundHouseLabel1.setText("Tax de Séjour");
        housePanel.add(boundHouseLabel1);
        boundHouseLabel1.setBounds(10, 330, 130, 17);

        taxHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        taxHouseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taxHouseTextFieldActionPerformed(evt);
            }
        });
        housePanel.add(taxHouseTextField);
        taxHouseTextField.setBounds(10, 350, 320, 30);

        AdressHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        AdressHouseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdressHouseTextFieldActionPerformed(evt);
            }
        });
        AdressHouseTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AdressHouseTextFieldKeyPressed(evt);
            }
        });
        housePanel.add(AdressHouseTextField);
        AdressHouseTextField.setBounds(10, 190, 320, 30);

        AdressHouseLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        AdressHouseLabel.setForeground(new java.awt.Color(0, 153, 204));
        AdressHouseLabel.setText("Montant par Jour (Prix Indicatif)");
        housePanel.add(AdressHouseLabel);
        AdressHouseLabel.setBounds(10, 220, 280, 17);

        newRentalPanel.add(housePanel);
        housePanel.setBounds(25, 85, 360, 470);

        roomHousePanel.setBackground(new java.awt.Color(255, 255, 255));
        roomHousePanel.setPreferredSize(new java.awt.Dimension(370, 370));
        roomHousePanel.setLayout(null);

        singleBedRoomHouseLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        singleBedRoomHouseLabel.setForeground(new java.awt.Color(0, 153, 204));
        singleBedRoomHouseLabel.setText("Lit Simple");
        roomHousePanel.add(singleBedRoomHouseLabel);
        singleBedRoomHouseLabel.setBounds(10, 40, 73, 17);

        singleBedRoomHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        singleBedRoomHouseTextField.setText("0");
        singleBedRoomHouseTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                singleBedRoomHouseTextFieldFocusGained(evt);
            }
        });
        singleBedRoomHouseTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                singleBedRoomHouseTextFieldMouseClicked(evt);
            }
        });
        singleBedRoomHouseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleBedRoomHouseTextFieldActionPerformed(evt);
            }
        });
        roomHousePanel.add(singleBedRoomHouseTextField);
        singleBedRoomHouseTextField.setBounds(10, 60, 330, 30);

        doublebedRoomHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        doublebedRoomHouseTextField.setText("0");
        doublebedRoomHouseTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doublebedRoomHouseTextFieldFocusGained(evt);
            }
        });
        doublebedRoomHouseTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doublebedRoomHouseTextFieldMouseClicked(evt);
            }
        });
        doublebedRoomHouseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doublebedRoomHouseTextFieldActionPerformed(evt);
            }
        });
        roomHousePanel.add(doublebedRoomHouseTextField);
        doublebedRoomHouseTextField.setBounds(10, 110, 330, 30);

        nameRoomHouseTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        roomHousePanel.add(nameRoomHouseTextField);
        nameRoomHouseTextField.setBounds(10, 10, 320, 30);

        saveRoomHouseButton.setBackground(new java.awt.Color(255, 255, 255));
        saveRoomHouseButton.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        saveRoomHouseButton.setText("Valider Chambre");
        saveRoomHouseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveRoomHouseButtonMouseClicked(evt);
            }
        });
        saveRoomHouseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveRoomHouseButtonActionPerformed(evt);
            }
        });
        roomHousePanel.add(saveRoomHouseButton);
        saveRoomHouseButton.setBounds(10, 320, 170, 30);

        doubleBedRToomHouseLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        doubleBedRToomHouseLabel1.setForeground(new java.awt.Color(0, 153, 204));
        doubleBedRToomHouseLabel1.setText("Lit Double");
        roomHousePanel.add(doubleBedRToomHouseLabel1);
        doubleBedRToomHouseLabel1.setBounds(10, 90, 75, 17);

        newRentalPanel.add(roomHousePanel);
        roomHousePanel.setBounds(25, 85, 360, 370);

        dormPanel.setForeground(new java.awt.Color(0, 153, 204));
        dormPanel.setOpaque(false);
        dormPanel.setPreferredSize(new java.awt.Dimension(360, 370));
        dormPanel.setLayout(null);

        typeDormLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        typeDormLabel.setForeground(new java.awt.Color(0, 153, 204));
        typeDormLabel.setText("Propriété");
        dormPanel.add(typeDormLabel);
        typeDormLabel.setBounds(10, 10, 66, 17);

        typeDormTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        dormPanel.add(typeDormTextField);
        typeDormTextField.setBounds(8, 31, 330, 30);

        nameDormLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        nameDormLabel.setForeground(new java.awt.Color(0, 153, 204));
        nameDormLabel.setText("Nom / Numéro");
        dormPanel.add(nameDormLabel);
        nameDormLabel.setBounds(10, 130, 109, 17);

        nameDormTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        nameDormTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameDormTextFieldActionPerformed(evt);
            }
        });
        dormPanel.add(nameDormTextField);
        nameDormTextField.setBounds(10, 150, 330, 30);

        singleBedDormLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        singleBedDormLabel.setForeground(new java.awt.Color(0, 153, 204));
        singleBedDormLabel.setText("Lit Simple");
        dormPanel.add(singleBedDormLabel);
        singleBedDormLabel.setBounds(10, 190, 73, 17);

        singleBedDormTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        singleBedDormTextField.setText("0");
        singleBedDormTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                singleBedDormTextFieldFocusGained(evt);
            }
        });
        singleBedDormTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                singleBedDormTextFieldMouseClicked(evt);
            }
        });
        singleBedDormTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleBedDormTextFieldActionPerformed(evt);
            }
        });
        dormPanel.add(singleBedDormTextField);
        singleBedDormTextField.setBounds(10, 210, 330, 30);

        doubleBedDormLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        doubleBedDormLabel.setForeground(new java.awt.Color(0, 153, 204));
        doubleBedDormLabel.setText("Lit Double");
        dormPanel.add(doubleBedDormLabel);
        doubleBedDormLabel.setBounds(10, 250, 75, 17);

        doubleBedDormTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        doubleBedDormTextField.setText("0");
        doubleBedDormTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doubleBedDormTextFieldFocusGained(evt);
            }
        });
        doubleBedDormTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doubleBedDormTextFieldMouseClicked(evt);
            }
        });
        doubleBedDormTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleBedDormTextFieldActionPerformed(evt);
            }
        });
        dormPanel.add(doubleBedDormTextField);
        doubleBedDormTextField.setBounds(10, 270, 330, 30);

        amountDormLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        amountDormLabel.setForeground(new java.awt.Color(0, 153, 204));
        amountDormLabel.setText("Montant");
        dormPanel.add(amountDormLabel);
        amountDormLabel.setBounds(10, 310, 60, 17);

        amountDormTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        amountDormTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                amountDormTextFieldKeyPressed(evt);
            }
        });
        dormPanel.add(amountDormTextField);
        amountDormTextField.setBounds(10, 330, 330, 30);

        addDormButton.setBackground(new java.awt.Color(255, 255, 255));
        addDormButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        addDormButton.setForeground(new java.awt.Color(102, 102, 102));
        addDormButton.setText("Ajouter");
        addDormButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDormButtonActionPerformed(evt);
            }
        });
        dormPanel.add(addDormButton);
        addDormButton.setBounds(10, 430, 100, 30);

        cancelDormButton.setBackground(new java.awt.Color(255, 255, 255));
        cancelDormButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        cancelDormButton.setForeground(new java.awt.Color(102, 102, 102));
        cancelDormButton.setText("Anuler");
        cancelDormButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDormButtonActionPerformed(evt);
            }
        });
        dormPanel.add(cancelDormButton);
        cancelDormButton.setBounds(180, 430, 94, 30);

        adressDormTextField1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        adressDormTextField1.setForeground(new java.awt.Color(0, 153, 204));
        adressDormTextField1.setText("Adresse de la Propriété");
        dormPanel.add(adressDormTextField1);
        adressDormTextField1.setBounds(10, 70, 220, 17);

        adressDormTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        dormPanel.add(adressDormTextField);
        adressDormTextField.setBounds(10, 90, 330, 30);

        taxDormLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        taxDormLabel.setForeground(new java.awt.Color(0, 153, 204));
        taxDormLabel.setText("Taxe de Séjour");
        dormPanel.add(taxDormLabel);
        taxDormLabel.setBounds(10, 370, 130, 17);

        taxDormTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        taxDormTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                taxDormTextFieldKeyPressed(evt);
            }
        });
        dormPanel.add(taxDormTextField);
        taxDormTextField.setBounds(10, 390, 330, 30);

        newRentalPanel.add(dormPanel);
        dormPanel.setBounds(25, 85, 360, 470);

        jLabel120.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(102, 102, 102));
        jLabel120.setText("Créer Une Nouvelle Location");
        newRentalPanel.add(jLabel120);
        jLabel120.setBounds(30, 10, 230, 17);

        myRentalPanel.add(newRentalPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 400, 560));

        jPanel3.setOpaque(false);
        jPanel3.setLayout(null);

        cancelMyrentalsButton.setBackground(new java.awt.Color(255, 255, 255));
        cancelMyrentalsButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        cancelMyrentalsButton.setForeground(new java.awt.Color(102, 102, 102));
        cancelMyrentalsButton.setText("Anuler");
        cancelMyrentalsButton.setToolTipText("Anuler les nouveaux changements.");
        cancelMyrentalsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelMyrentalsButtonActionPerformed(evt);
            }
        });
        jPanel3.add(cancelMyrentalsButton);
        cancelMyrentalsButton.setBounds(300, 490, 140, 29);

        deleteMyrentalsButton.setBackground(new java.awt.Color(255, 255, 255));
        deleteMyrentalsButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        deleteMyrentalsButton.setForeground(new java.awt.Color(102, 102, 102));
        deleteMyrentalsButton.setText("Suprimer");
        deleteMyrentalsButton.setToolTipText("Suprimer une ou plusieurs location.\n\n");
        deleteMyrentalsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMyrentalsButtonActionPerformed(evt);
            }
        });
        jPanel3.add(deleteMyrentalsButton);
        deleteMyrentalsButton.setBounds(150, 490, 140, 29);

        helpPanel.setBackground(new java.awt.Color(51, 177, 226));
        helpPanel.setForeground(new java.awt.Color(255, 255, 255));
        helpPanel.setLayout(null);

        jLabel125.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(255, 255, 255));
        jLabel125.setText("Modifier une location");
        helpPanel.add(jLabel125);
        jLabel125.setBounds(10, 0, 170, 17);

        jLabel162.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        jLabel162.setForeground(new java.awt.Color(255, 255, 255));
        jLabel162.setText("ANULER pour anuler les changements non sauvegarder");
        helpPanel.add(jLabel162);
        jLabel162.setBounds(30, 110, 360, 16);

        jLabel163.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        jLabel163.setForeground(new java.awt.Color(255, 255, 255));
        jLabel163.setText("1. double click sur une cellule pour la modifier");
        helpPanel.add(jLabel163);
        jLabel163.setBounds(10, 30, 310, 16);

        jLabel164.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        jLabel164.setForeground(new java.awt.Color(255, 255, 255));
        jLabel164.setText("2. Presse ENTRER ou TAB pour valider");
        helpPanel.add(jLabel164);
        jLabel164.setBounds(10, 50, 310, 16);

        jLabel165.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        jLabel165.setForeground(new java.awt.Color(255, 255, 255));
        jLabel165.setText("3. clique sur:");
        helpPanel.add(jLabel165);
        jLabel165.setBounds(10, 70, 300, 16);

        jLabel189.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        jLabel189.setForeground(new java.awt.Color(255, 255, 255));
        jLabel189.setText("ENREGISTRER pour sauvegarder dans la base de donnée");
        helpPanel.add(jLabel189);
        jLabel189.setBounds(30, 90, 360, 16);

        jPanel3.add(helpPanel);
        helpPanel.setBounds(210, 30, 400, 150);

        jScrollPane1.setOpaque(false);

        myRentalsTable.setAutoCreateRowSorter(true);
        myRentalsTable.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        myRentalsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Propriété", "Type ", "Nom", "Lit 1p", "Lit 2p", "Montant", "Caution", "Taxe", "Adresse"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        myRentalsTable.setToolTipText("<html>\nDouble-Click sur une cellule pour la modifier. <br>\nPresse Entrer ou Tab pour confirmer.\n</html>\n");
        myRentalsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        myRentalsTable.setOpaque(false);
        myRentalsTable.setShowGrid(false);
        myRentalsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myRentalsTableMouseClicked(evt);
            }
        });
        myRentalsTable.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                myRentalsTableComponentShown(evt);
            }
        });
        jScrollPane1.setViewportView(myRentalsTable);

        jPanel3.add(jScrollPane1);
        jScrollPane1.setBounds(10, 60, 630, 410);

        modifyMyrentalsTable.setBackground(new java.awt.Color(255, 255, 255));
        modifyMyrentalsTable.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        modifyMyrentalsTable.setForeground(new java.awt.Color(102, 102, 102));
        modifyMyrentalsTable.setText("Enregistrer");
        modifyMyrentalsTable.setToolTipText("Enregister dans la base de données.");
        modifyMyrentalsTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyMyrentalsTableActionPerformed(evt);
            }
        });
        jPanel3.add(modifyMyrentalsTable);
        modifyMyrentalsTable.setBounds(10, 490, 130, 29);

        jLabel119.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(102, 102, 102));
        jLabel119.setText("Voir/ Modifier Mes Locations ");
        jPanel3.add(jLabel119);
        jLabel119.setBounds(20, 10, 300, 17);

        jLabel121.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(0, 153, 204));
        jLabel121.setText("Aide");
        jLabel121.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel121MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel121MouseExited(evt);
            }
        });
        jPanel3.add(jLabel121);
        jLabel121.setBounds(600, 10, 40, 20);

        myRentalPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 650, 560));

        jLabel118.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel118.setForeground(new java.awt.Color(0, 153, 204));
        jLabel118.setText("Mes Locations");
        myRentalPanel.add(jLabel118, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 210, -1));

        optionPanel.setBackground(new java.awt.Color(255, 255, 255));
        optionPanel.setOpaque(true);
        optionPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                optionPanelComponentShown(evt);
            }
        });

        jTable6.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 13)); // NOI18N
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Description", "Montant", "Taux Journalier"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable6MouseClicked(evt);
            }
        });
        jTable6.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTable6ComponentShown(evt);
            }
        });
        jScrollPane8.setViewportView(jTable6);

        optionPanel.add(jScrollPane8);
        jScrollPane8.setBounds(90, 130, 440, 230);

        jButton21.setBackground(new java.awt.Color(255, 255, 255));
        jButton21.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton21.setForeground(new java.awt.Color(102, 102, 102));
        jButton21.setText("Effacer");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        optionPanel.add(jButton21);
        jButton21.setBounds(800, 370, 140, 30);

        jLabel132.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(0, 153, 204));
        jLabel132.setText("Mes Options");
        optionPanel.add(jLabel132);
        jLabel132.setBounds(20, 10, 400, 30);

        jLabel133.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel133.setText("€");
        optionPanel.add(jLabel133);
        jLabel133.setBounds(810, 230, 20, 30);

        jTextField10.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        optionPanel.add(jTextField10);
        jTextField10.setBounds(640, 170, 290, 30);

        jTextField11.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });
        optionPanel.add(jTextField11);
        jTextField11.setBounds(640, 230, 160, 30);

        jLabel135.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(102, 102, 102));
        jLabel135.setText(" Options Disponibles");
        optionPanel.add(jLabel135);
        jLabel135.setBounds(80, 80, 210, 25);

        jButton22.setBackground(new java.awt.Color(255, 255, 255));
        jButton22.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton22.setForeground(new java.awt.Color(102, 102, 102));
        jButton22.setText("Enregistrer");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        optionPanel.add(jButton22);
        jButton22.setBounds(635, 370, 140, 30);

        jLabel137.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(0, 153, 204));
        jLabel137.setText("Description");
        optionPanel.add(jLabel137);
        jLabel137.setBounds(640, 150, 100, 25);

        jLabel134.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(0, 153, 204));
        jLabel134.setText("Montant");
        optionPanel.add(jLabel134);
        jLabel134.setBounds(640, 210, 100, 25);

        jCheckBox1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jCheckBox1.setText("Taux Journalier");
        optionPanel.add(jCheckBox1);
        jCheckBox1.setBounds(640, 270, 180, 23);

        jLabel138.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(102, 102, 102));
        jLabel138.setText("Suprimer Option");
        jLabel138.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel138MouseClicked(evt);
            }
        });
        jLabel138.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel138KeyPressed(evt);
            }
        });
        optionPanel.add(jLabel138);
        jLabel138.setBounds(410, 380, 140, 17);

        jLabel143.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(102, 102, 102));
        jLabel143.setText("Ajouter Option");
        optionPanel.add(jLabel143);
        jLabel143.setBounds(640, 80, 140, 25);

        client.setBackground(new java.awt.Color(255, 255, 255));
        client.setRequestFocusEnabled(false);
        client.setLayout(null);

        searchClientPanel1.setBackground(new java.awt.Color(255, 255, 255));
        searchClientPanel1.setLayout(null);

        jLabelb149.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb149.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb149.setText("Commentaires Précédents");
        commentsPanelb1.add(jLabelb149);
        jLabelb149.setBounds(20, 0, 220, 17);

        jComboBoxb14.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb14.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBoxb14.setOpaque(true);
        jComboBoxb14.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBoxb14FocusLost(evt);
            }
        });
        jComboBoxb14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxb14ActionPerformed(evt);
            }
        });
        commentsPanelb1.add(jComboBoxb14);
        jComboBoxb14.setBounds(20, 20, 260, 27);

        jTextAreab3.setColumns(20);
        jTextAreab3.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextAreab3.setLineWrap(true);
        jTextAreab3.setRows(5);
        jTextAreab3.setWrapStyleWord(true);
        jScrollPaneb4.setViewportView(jTextAreab3);

        commentsPanelb1.add(jScrollPaneb4);
        jScrollPaneb4.setBounds(20, 60, 270, 180);

        clientBlackListCheckBox.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientBlackListCheckBox.setText("Marquer comme client indésirable");
        clientBlackListCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                clientBlackListCheckBoxStateChanged(evt);
            }
        });
        clientBlackListCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientBlackListCheckBoxActionPerformed(evt);
            }
        });
        commentsPanelb1.add(clientBlackListCheckBox);
        clientBlackListCheckBox.setBounds(20, 250, 280, 23);

        searchClientPanel1.add(commentsPanelb1);
        commentsPanelb1.setBounds(0, 240, 310, 290);

        jLabelb150.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb150.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb150.setText("Prénom");
        searchClientPanel1.add(jLabelb150);
        jLabelb150.setBounds(20, 90, 200, 17);

        clientfirstNameField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientfirstNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientfirstNameFieldActionPerformed(evt);
            }
        });
        clientfirstNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clientfirstNameFieldKeyPressed(evt);
            }
        });
        searchClientPanel1.add(clientfirstNameField);
        clientfirstNameField.setBounds(20, 110, 260, 30);

        jLabelb151.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb151.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb151.setText("Nom*");
        searchClientPanel1.add(jLabelb151);
        jLabelb151.setBounds(20, 30, 80, 17);

        clientNameField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientNameField.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                clientNameFieldInputMethodTextChanged(evt);
            }
        });
        clientNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientNameFieldActionPerformed(evt);
            }
        });
        clientNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clientNameFieldKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                clientNameFieldKeyTyped(evt);
            }
        });
        searchClientPanel1.add(clientNameField);
        clientNameField.setBounds(20, 50, 260, 30);

        jButtonb16.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb16.setText("Rechercher");
        jButtonb16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonb16MouseClicked(evt);
            }
        });
        jButtonb16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb16ActionPerformed(evt);
            }
        });
        searchClientPanel1.add(jButtonb16);
        jButtonb16.setBounds(20, 160, 113, 30);

        jButtonb17.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb17.setText("Effacer");
        jButtonb17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb17ActionPerformed(evt);
            }
        });
        searchClientPanel1.add(jButtonb17);
        jButtonb17.setBounds(160, 160, 120, 30);

        jLabelb152.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb152.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb152.setText("Rechercher Client");
        searchClientPanel1.add(jLabelb152);
        jLabelb152.setBounds(20, 10, 180, 20);

        client.add(searchClientPanel1);
        searchClientPanel1.setBounds(20, 40, 340, 560);

        clientPanel3.setBackground(new java.awt.Color(255, 255, 255));
        clientPanel3.setOpaque(false);
        clientPanel3.setLayout(null);

        clientIdProofField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientIdProofField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clientIdProofFieldMouseEntered(evt);
            }
        });
        clientIdProofField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientIdProofFieldActionPerformed(evt);
            }
        });
        clientIdProofField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                clientIdProofFieldKeyPressed(evt);
            }
        });
        clientPanel3.add(clientIdProofField);
        clientIdProofField.setBounds(30, 500, 290, 30);

        clientEmailField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel3.add(clientEmailField);
        clientEmailField.setBounds(30, 200, 290, 30);

        jLabelb153.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb153.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb153.setText("Commune Déléguée");
        clientPanel3.add(jLabelb153);
        jLabelb153.setBounds(30, 330, 280, 20);

        clientDcField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel3.add(clientDcField);
        clientDcField.setBounds(30, 350, 290, 30);

        clientCityField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel3.add(clientCityField);
        clientCityField.setBounds(30, 450, 290, 30);

        jLabelb154.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb154.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb154.setText("Email");
        clientPanel3.add(jLabelb154);
        jLabelb154.setBounds(30, 180, 70, 20);

        clientTel1Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel3.add(clientTel1Field);
        clientTel1Field.setBounds(30, 100, 290, 30);

        jLabelb155.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb155.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb155.setText("Téléphone 1");
        clientPanel3.add(jLabelb155);
        jLabelb155.setBounds(30, 80, 160, 20);

        jLabelb156.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb156.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb156.setText("Ville - pays");
        clientPanel3.add(jLabelb156);
        jLabelb156.setBounds(30, 430, 130, 20);

        jLabelb157.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb157.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb157.setText("Numéro d'Identité");
        clientPanel3.add(jLabelb157);
        jLabelb157.setBounds(30, 480, 240, 20);

        jLabelb158.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb158.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb158.setText("Genre");
        clientPanel3.add(jLabelb158);
        jLabelb158.setBounds(30, 30, 70, 20);

        jComboBoxb15.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb15.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBoxb15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Homme", "Femme", "Autre" }));
        clientPanel3.add(jComboBoxb15);
        jComboBoxb15.setBounds(30, 50, 290, 30);

        jLabelb160.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb160.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb160.setText("Adresse 2");
        clientPanel3.add(jLabelb160);
        jLabelb160.setBounds(30, 280, 90, 20);

        clientStreet1Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel3.add(clientStreet1Field);
        clientStreet1Field.setBounds(30, 250, 290, 30);

        jLabelb161.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb161.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb161.setText("Code Postal");
        clientPanel3.add(jLabelb161);
        jLabelb161.setBounds(30, 380, 110, 20);

        clientPostCodeField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel3.add(clientPostCodeField);
        clientPostCodeField.setBounds(30, 400, 290, 30);

        jLabelb162.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb162.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb162.setText("Info Client");
        clientPanel3.add(jLabelb162);
        jLabelb162.setBounds(30, 10, 160, 20);

        jLabelb163.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        jLabelb163.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb163.setText("Créer Homonyme");
        jLabelb163.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb163MouseClicked(evt);
            }
        });
        clientPanel3.add(jLabelb163);
        jLabelb163.setBounds(210, 30, 110, 20);

        clientTel2Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel3.add(clientTel2Field);
        clientTel2Field.setBounds(30, 150, 290, 30);

        jLabelb164.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb164.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb164.setText("Téléphone 2");
        clientPanel3.add(jLabelb164);
        jLabelb164.setBounds(30, 130, 130, 20);

        jLabelb165.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb165.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb165.setText("Adresse 1");
        clientPanel3.add(jLabelb165);
        jLabelb165.setBounds(30, 230, 120, 20);

        clientStreet2Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel3.add(clientStreet2Field);
        clientStreet2Field.setBounds(30, 300, 290, 30);

        jButtonb18.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb18.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb18.setForeground(new java.awt.Color(102, 102, 102));
        jButtonb18.setText("Enregistrer");
        jButtonb18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb18ActionPerformed(evt);
            }
        });
        clientPanel3.add(jButtonb18);
        jButtonb18.setBounds(30, 540, 130, 30);

        client.add(clientPanel3);
        clientPanel3.setBounds(340, 40, 340, 650);

        jLabelb195.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb195.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb195.setText("Mes Clients");
        client.add(jLabelb195);
        jLabelb195.setBounds(40, 10, 300, 30);

        multipleClients1.setOpaque(true);

        jLabelb38.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients1.add(jLabelb38);
        jLabelb38.setBounds(20, 230, 290, 20);

        jComboBoxb12.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxb12MouseClicked(evt);
            }
        });
        jComboBoxb12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxb12ActionPerformed(evt);
            }
        });
        multipleClients1.add(jComboBoxb12);
        jComboBoxb12.setBounds(20, 70, 290, 30);

        jLabelb39.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb39.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb39.setText("Choisir Client");
        multipleClients1.add(jLabelb39);
        jLabelb39.setBounds(20, 40, 120, 30);

        jLabelb41.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb41.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jLabelb41ComponentShown(evt);
            }
        });
        multipleClients1.add(jLabelb41);
        jLabelb41.setBounds(20, 110, 290, 20);

        jButtonb13.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb13.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButtonb13.setText("Valider");
        jButtonb13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonb13MouseClicked(evt);
            }
        });
        jButtonb13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb13ActionPerformed(evt);
            }
        });
        multipleClients1.add(jButtonb13);
        jButtonb13.setBounds(20, 330, 94, 30);

        jButtonb14.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb14.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButtonb14.setText("Anuler");
        jButtonb14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb14ActionPerformed(evt);
            }
        });
        multipleClients1.add(jButtonb14);
        jButtonb14.setBounds(170, 330, 91, 30);

        jLabelb42.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients1.add(jLabelb42);
        jLabelb42.setBounds(20, 140, 290, 20);

        jLabelb44.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients1.add(jLabelb44);
        jLabelb44.setBounds(20, 170, 290, 20);

        jLabelb145.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb145.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb145.setText("Plusieurs Clients Trouvés");
        multipleClients1.add(jLabelb145);
        jLabelb145.setBounds(20, 10, 250, 20);

        jLabelb146.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients1.add(jLabelb146);
        jLabelb146.setBounds(20, 250, 290, 20);

        jLabelb147.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients1.add(jLabelb147);
        jLabelb147.setBounds(20, 190, 290, 20);

        jLabelb148.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients1.add(jLabelb148);
        jLabelb148.setBounds(20, 210, 290, 20);

        client.add(multipleClients1);
        multipleClients1.setBounds(20, 50, 330, 550);

        jLabel198.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel198.setForeground(new java.awt.Color(102, 102, 102));
        jLabel198.setText("Voir tous mes clients avec excel");
        jLabel198.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel198MouseClicked(evt);
            }
        });
        client.add(jLabel198);
        jLabel198.setBounds(810, 10, 250, 17);

        externalBooking.setBackground(new java.awt.Color(255, 255, 255));
        externalBooking.setOpaque(true);
        externalBooking.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                externalBookingComponentShown(evt);
            }
        });

        jTable7.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nom"
            }
        ));
        jTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable7MouseClicked(evt);
            }
        });
        jTable7.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTable7ComponentShown(evt);
            }
        });
        jScrollPane10.setViewportView(jTable7);

        externalBooking.add(jScrollPane10);
        jScrollPane10.setBounds(50, 270, 360, 230);

        jButton23.setBackground(new java.awt.Color(255, 255, 255));
        jButton23.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton23.setForeground(new java.awt.Color(102, 102, 102));
        jButton23.setText("Suprimer");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        externalBooking.add(jButton23);
        jButton23.setBounds(50, 510, 140, 30);

        jLabel203.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel203.setForeground(new java.awt.Color(0, 153, 204));
        jLabel203.setText("Gestion Externe");
        externalBooking.add(jLabel203);
        jLabel203.setBounds(20, 10, 400, 30);

        nameTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });
        externalBooking.add(nameTextField);
        nameTextField.setBounds(50, 130, 360, 30);

        jLabel205.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabel205.setForeground(new java.awt.Color(102, 102, 102));
        jLabel205.setText("Liste Des Sites De Réservation");
        externalBooking.add(jLabel205);
        jLabel205.setBounds(50, 230, 400, 25);

        jButton24.setBackground(new java.awt.Color(255, 255, 255));
        jButton24.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton24.setForeground(new java.awt.Color(102, 102, 102));
        jButton24.setText("Enregistrer");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        externalBooking.add(jButton24);
        jButton24.setBounds(50, 170, 140, 30);

        jLabel206.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel206.setForeground(new java.awt.Color(0, 153, 204));
        jLabel206.setText("Nom");
        externalBooking.add(jLabel206);
        jLabel206.setBounds(50, 100, 100, 25);

        jLabel209.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabel209.setForeground(new java.awt.Color(102, 102, 102));
        jLabel209.setText("Ajouter Un Site De Réservation");
        externalBooking.add(jLabel209);
        jLabel209.setBounds(50, 70, 330, 25);

        jButton26.setBackground(new java.awt.Color(255, 255, 255));
        jButton26.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButton26.setForeground(new java.awt.Color(102, 102, 102));
        jButton26.setText("Effacer");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        externalBooking.add(jButton26);
        jButton26.setBounds(210, 170, 140, 30);

        jLabel126.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel126.setForeground(new java.awt.Color(255, 255, 255));
        jLabel126.setText("_____________");
        jLabel126.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel126FocusGained(evt);
            }
        });

        jLabel127.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel127.setForeground(new java.awt.Color(255, 255, 255));
        jLabel127.setText("___________");
        jLabel127.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel127FocusGained(evt);
            }
        });

        jLabel128.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel128.setForeground(new java.awt.Color(255, 255, 255));
        jLabel128.setText("__________");
        jLabel128.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel128FocusGained(evt);
            }
        });

        jLabel130.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(255, 255, 255));
        jLabel130.setText("_________");
        jLabel130.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel130FocusGained(evt);
            }
        });

        jLabel93.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(255, 255, 255));
        jLabel93.setText("Mes Locations");
        jLabel93.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel93MouseClicked(evt);
            }
        });

        jLabel94.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(255, 255, 255));
        jLabel94.setText("Mon Contrat");
        jLabel94.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel94MouseClicked(evt);
            }
        });

        jLabel95.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(255, 255, 255));
        jLabel95.setText("Mes Emails");
        jLabel95.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel95MouseClicked(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Mes Infos");
        jLabel49.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel49MouseClicked(evt);
            }
        });

        jLabel124.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel124.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/home_2.png"))); // NOI18N

        jLabel136.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(255, 255, 255));
        jLabel136.setText("___________");
        jLabel136.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel136FocusGained(evt);
            }
        });

        jLabel131.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(255, 255, 255));
        jLabel131.setText("Mes Options");
        jLabel131.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel131MouseClicked(evt);
            }
        });

        jLabel185.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel185.setForeground(new java.awt.Color(255, 255, 255));
        jLabel185.setText("__________");
        jLabel185.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel185FocusGained(evt);
            }
        });

        jLabel184.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel184.setForeground(new java.awt.Color(255, 255, 255));
        jLabel184.setText("Mes Clients");
        jLabel184.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel184MouseClicked(evt);
            }
        });

        jLabel204.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel204.setForeground(new java.awt.Color(255, 255, 255));
        jLabel204.setText("______________");
        jLabel204.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel204FocusGained(evt);
            }
        });

        jLabel207.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel207.setForeground(new java.awt.Color(255, 255, 255));
        jLabel207.setText("Gestion Externe");
        jLabel207.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel207MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout homePanelLayout = new javax.swing.GroupLayout(homePanel);
        homePanel.setLayout(homePanelLayout);
        homePanelLayout.setHorizontalGroup(
            homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel124)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel130, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel126, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel128, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel131, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel184, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel185, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel204, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel207, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homePanelLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(externalBooking))
                    .addComponent(myPanel)
                    .addComponent(optionPanel)
                    .addComponent(myRentalPanel)
                    .addComponent(createEmailPanel)
                    .addComponent(createDocPanel)
                    .addComponent(client, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        homePanelLayout.setVerticalGroup(
            homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homePanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel124)
                .addGap(30, 30, 30)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(homePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel130)))
                .addGap(20, 20, 20)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel126))
                    .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(homePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel127)))
                .addGap(20, 20, 20)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel128))
                    .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel131, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(homePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel136)))
                .addGap(19, 19, 19)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel184, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(homePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel185)))
                .addGap(21, 21, 21)
                .addGroup(homePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homePanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel204))
                    .addComponent(jLabel207, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(homePanelLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(externalBooking))
            .addComponent(myPanel)
            .addComponent(optionPanel)
            .addComponent(myRentalPanel)
            .addComponent(createEmailPanel)
            .addComponent(createDocPanel)
            .addComponent(client, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        booking11.setBackground(new java.awt.Color(204, 204, 204));
        booking11.setPreferredSize(new java.awt.Dimension(0, 0));

        newBooking.setBackground(new java.awt.Color(255, 255, 255));
        newBooking.setRequestFocusEnabled(false);
        newBooking.setLayout(null);

        multipleClients.setOpaque(true);

        jLabelb32.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients.add(jLabelb32);
        jLabelb32.setBounds(20, 250, 290, 20);

        jComboBoxb7.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxb7MouseClicked(evt);
            }
        });
        jComboBoxb7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxb7ActionPerformed(evt);
            }
        });
        multipleClients.add(jComboBoxb7);
        jComboBoxb7.setBounds(20, 70, 290, 30);

        jLabelb33.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb33.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb33.setText("Choisir Client");
        multipleClients.add(jLabelb33);
        jLabelb33.setBounds(20, 40, 120, 30);

        jLabelb34.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb34.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jLabelb34ComponentShown(evt);
            }
        });
        multipleClients.add(jLabelb34);
        jLabelb34.setBounds(20, 110, 290, 20);

        jButtonb11.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb11.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButtonb11.setText("Valider");
        jButtonb11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonb11MouseClicked(evt);
            }
        });
        jButtonb11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb11ActionPerformed(evt);
            }
        });
        multipleClients.add(jButtonb11);
        jButtonb11.setBounds(20, 320, 94, 30);

        jButtonb12.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb12.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButtonb12.setText("Anuler");
        jButtonb12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb12ActionPerformed(evt);
            }
        });
        multipleClients.add(jButtonb12);
        jButtonb12.setBounds(170, 320, 91, 30);

        jLabelb36.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients.add(jLabelb36);
        jLabelb36.setBounds(20, 140, 290, 20);

        jLabelb37.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients.add(jLabelb37);
        jLabelb37.setBounds(20, 170, 290, 20);

        jLabelb125.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb125.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb125.setText("Plusieurs Clients Trouvés !");
        multipleClients.add(jLabelb125);
        jLabelb125.setBounds(20, 10, 260, 20);

        jLabelb124.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients.add(jLabelb124);
        jLabelb124.setBounds(20, 210, 290, 20);

        jLabelb126.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients.add(jLabelb126);
        jLabelb126.setBounds(20, 190, 290, 20);

        jLabelb134.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        multipleClients.add(jLabelb134);
        jLabelb134.setBounds(20, 230, 290, 20);

        newBooking.add(multipleClients);
        multipleClients.setBounds(20, 50, 330, 600);

        searchClientPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchClientPanel.setLayout(null);

        jLabelb35.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb35.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb35.setText("Commentaires Précédents ");
        commentsPanelb.add(jLabelb35);
        jLabelb35.setBounds(20, 0, 250, 17);

        jComboBoxb8.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb8.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBoxb8.setOpaque(true);
        jComboBoxb8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBoxb8FocusLost(evt);
            }
        });
        jComboBoxb8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxb8ActionPerformed(evt);
            }
        });
        commentsPanelb.add(jComboBoxb8);
        jComboBoxb8.setBounds(20, 20, 260, 27);

        jTextAreab1.setColumns(20);
        jTextAreab1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextAreab1.setLineWrap(true);
        jTextAreab1.setRows(5);
        jTextAreab1.setWrapStyleWord(true);
        jScrollPaneb1.setViewportView(jTextAreab1);

        commentsPanelb.add(jScrollPaneb1);
        jScrollPaneb1.setBounds(20, 60, 270, 240);

        blackListCheckBox.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        blackListCheckBox.setText("Marquer comme client indésirable");
        blackListCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                blackListCheckBoxStateChanged(evt);
            }
        });
        commentsPanelb.add(blackListCheckBox);
        blackListCheckBox.setBounds(20, 300, 260, 23);

        searchClientPanel.add(commentsPanelb);
        commentsPanelb.setBounds(0, 200, 310, 370);

        jLabelb15.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb15.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb15.setText("Prénom");
        searchClientPanel.add(jLabelb15);
        jLabelb15.setBounds(20, 90, 200, 17);

        firstNameField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        firstNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameFieldActionPerformed(evt);
            }
        });
        firstNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                firstNameFieldKeyPressed(evt);
            }
        });
        searchClientPanel.add(firstNameField);
        firstNameField.setBounds(20, 110, 260, 30);

        jLabelb2.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb2.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb2.setText("Nom*");
        searchClientPanel.add(jLabelb2);
        jLabelb2.setBounds(20, 30, 80, 17);

        nameField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });
        nameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameFieldKeyPressed(evt);
            }
        });
        searchClientPanel.add(nameField);
        nameField.setBounds(20, 50, 260, 30);

        jButtonb5.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb5.setText("Rechercher");
        jButtonb5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonb5MouseClicked(evt);
            }
        });
        jButtonb5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb5ActionPerformed(evt);
            }
        });
        searchClientPanel.add(jButtonb5);
        jButtonb5.setBounds(20, 160, 113, 30);

        jButtonb15.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb15.setText("Effacer");
        jButtonb15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb15ActionPerformed(evt);
            }
        });
        searchClientPanel.add(jButtonb15);
        jButtonb15.setBounds(160, 160, 120, 30);

        jLabelb109.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb109.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb109.setText("Rechercher Client");
        searchClientPanel.add(jLabelb109);
        jLabelb109.setBounds(20, 10, 180, 20);

        newBooking.add(searchClientPanel);
        searchClientPanel.setBounds(20, 45, 340, 560);

        clientPanel2.setBackground(new java.awt.Color(255, 255, 255));
        clientPanel2.setOpaque(false);
        clientPanel2.setLayout(null);

        idField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        idField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                idFieldMouseEntered(evt);
            }
        });
        idField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idFieldActionPerformed(evt);
            }
        });
        idField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idFieldKeyPressed(evt);
            }
        });
        clientPanel2.add(idField);
        idField.setBounds(30, 500, 290, 30);

        emailField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel2.add(emailField);
        emailField.setBounds(30, 200, 290, 30);

        jLabelb16.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb16.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb16.setText("Commune Déléguée");
        clientPanel2.add(jLabelb16);
        jLabelb16.setBounds(30, 330, 280, 20);

        districtField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel2.add(districtField);
        districtField.setBounds(30, 350, 290, 30);

        cityField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel2.add(cityField);
        cityField.setBounds(30, 450, 290, 30);

        jLabelb13.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb13.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb13.setText("Email");
        clientPanel2.add(jLabelb13);
        jLabelb13.setBounds(30, 180, 70, 20);

        tel1Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel2.add(tel1Field);
        tel1Field.setBounds(30, 100, 290, 30);

        jLabelb3.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb3.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb3.setText("Téléphone 1");
        clientPanel2.add(jLabelb3);
        jLabelb3.setBounds(30, 80, 120, 20);

        jLabelb7.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb7.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb7.setText("Ville - Pays");
        clientPanel2.add(jLabelb7);
        jLabelb7.setBounds(30, 430, 130, 20);

        jLabelb8.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb8.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb8.setText("Numéro d'Identité");
        clientPanel2.add(jLabelb8);
        jLabelb8.setBounds(30, 480, 240, 20);

        jLabelb5.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb5.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb5.setText("Genre");
        clientPanel2.add(jLabelb5);
        jLabelb5.setBounds(30, 30, 70, 20);

        jComboBoxb2.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBoxb2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Homme", "Femme", "Autre" }));
        clientPanel2.add(jComboBoxb2);
        jComboBoxb2.setBounds(30, 50, 290, 30);

        jLabelb84.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb84.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb84.setText("Adresse 1");
        clientPanel2.add(jLabelb84);
        jLabelb84.setBounds(30, 230, 110, 20);

        address1Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        address1Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                address1FieldActionPerformed(evt);
            }
        });
        address1Field.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                address1FieldPropertyChange(evt);
            }
        });
        address1Field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                address1FieldKeyReleased(evt);
            }
        });
        clientPanel2.add(address1Field);
        address1Field.setBounds(30, 250, 290, 30);

        jLabelb90.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb90.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb90.setText("Code Postal");
        clientPanel2.add(jLabelb90);
        jLabelb90.setBounds(30, 380, 110, 20);

        postCodeField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel2.add(postCodeField);
        postCodeField.setBounds(30, 400, 290, 30);

        jLabelb110.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb110.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb110.setText("Info Client");
        clientPanel2.add(jLabelb110);
        jLabelb110.setBounds(30, 10, 160, 20);

        jLabelb112.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        jLabelb112.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb112.setText("Créer Homonyme");
        jLabelb112.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb112MouseClicked(evt);
            }
        });
        clientPanel2.add(jLabelb112);
        jLabelb112.setBounds(210, 30, 110, 20);

        jLabelb45.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb45.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb45.setText("Téléphone 2");
        clientPanel2.add(jLabelb45);
        jLabelb45.setBounds(30, 130, 110, 20);

        tel2Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        clientPanel2.add(tel2Field);
        tel2Field.setBounds(30, 150, 290, 30);

        jLabelb166.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb166.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb166.setText("Adresse 2");
        clientPanel2.add(jLabelb166);
        jLabelb166.setBounds(30, 280, 100, 20);

        address2Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        address2Field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                address2FieldActionPerformed(evt);
            }
        });
        clientPanel2.add(address2Field);
        address2Field.setBounds(30, 300, 290, 30);

        newBooking.add(clientPanel2);
        clientPanel2.setBounds(340, 20, 340, 590);

        jLabelb108.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb108.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb108.setText("Nouvelle Réservation");
        newBooking.add(jLabelb108);
        jLabelb108.setBounds(40, 10, 300, 30);

        bookingNewBookingInfoPanel.setBackground(new java.awt.Color(255, 255, 255));
        bookingNewBookingInfoPanel.setLayout(null);

        jLabelb111.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb111.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb111.setText("Réservation");
        bookingNewBookingInfoPanel.add(jLabelb111);
        jLabelb111.setBounds(20, 10, 140, 20);

        jLabelb17.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb17.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb17.setText("Type de Location");
        bookingNewBookingInfoPanel.add(jLabelb17);
        jLabelb17.setBounds(20, 80, 130, 20);

        propertyTypeBox.setBackground(new java.awt.Color(255, 255, 255));
        propertyTypeBox.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        propertyTypeBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Maison", "Chambre", "Dortoir" }));
        propertyTypeBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                propertyTypeBoxFocusLost(evt);
            }
        });
        propertyTypeBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                propertyTypeBoxActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(propertyTypeBox);
        propertyTypeBox.setBounds(20, 100, 250, 30);

        jLabelb19.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb19.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb19.setText("Arrivée");
        bookingNewBookingInfoPanel.add(jLabelb19);
        jLabelb19.setBounds(20, 130, 60, 20);

        jLabelb10.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb10.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb10.setText("Départ");
        bookingNewBookingInfoPanel.add(jLabelb10);
        jLabelb10.setBounds(20, 180, 60, 20);

        jLabelb20.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb20.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb20.setText(" Lit Simple");
        bookingNewBookingInfoPanel.add(jLabelb20);
        jLabelb20.setBounds(160, 280, 100, 20);

        jLabelb21.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb21.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb21.setText("Lit Double");
        bookingNewBookingInfoPanel.add(jLabelb21);
        jLabelb21.setBounds(20, 280, 77, 20);

        singleField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        singleField.setText("0");
        singleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                singleFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                singleFieldFocusLost(evt);
            }
        });
        singleField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                singleFieldMouseClicked(evt);
            }
        });
        singleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleFieldActionPerformed(evt);
            }
        });
        singleField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                singleFieldKeyTyped(evt);
            }
        });
        bookingNewBookingInfoPanel.add(singleField);
        singleField.setBounds(160, 300, 110, 30);

        doubleField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        doubleField.setText("0");
        doubleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doubleFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doubleFieldFocusLost(evt);
            }
        });
        doubleField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                doubleFieldMouseClicked(evt);
            }
        });
        doubleField.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                doubleFieldInputMethodTextChanged(evt);
            }
        });
        doubleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleFieldActionPerformed(evt);
            }
        });
        doubleField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                doubleFieldKeyTyped(evt);
            }
        });
        bookingNewBookingInfoPanel.add(doubleField);
        doubleField.setBounds(20, 300, 110, 30);

        adultField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        adultField.setText("0");
        adultField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                adultFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                adultFieldFocusLost(evt);
            }
        });
        adultField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adultFieldActionPerformed(evt);
            }
        });
        adultField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                adultFieldKeyTyped(evt);
            }
        });
        bookingNewBookingInfoPanel.add(adultField);
        adultField.setBounds(20, 250, 110, 30);

        jLabelb1.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb1.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb1.setText("Adulte");
        bookingNewBookingInfoPanel.add(jLabelb1);
        jLabelb1.setBounds(20, 230, 100, 17);

        kidField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        kidField.setText("0");
        kidField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                kidFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                kidFieldFocusLost(evt);
            }
        });
        kidField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kidFieldActionPerformed(evt);
            }
        });
        kidField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                kidFieldKeyTyped(evt);
            }
        });
        bookingNewBookingInfoPanel.add(kidField);
        kidField.setBounds(160, 250, 110, 30);

        jLabelb47.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb47.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb47.setText("Enfant");
        bookingNewBookingInfoPanel.add(jLabelb47);
        jLabelb47.setBounds(160, 230, 100, 17);

        calendarIn.setForeground(new java.awt.Color(102, 102, 102));
        calendarIn.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        calendarIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calendarInActionPerformed(evt);
            }
        });
        calendarIn.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calendarInPropertyChange(evt);
            }
        });
        bookingNewBookingInfoPanel.add(calendarIn);
        calendarIn.setBounds(20, 150, 180, 30);
        calendarIn.setLocale(Locale.FRENCH);

        calendarOut.setForeground(new java.awt.Color(102, 102, 102));
        calendarOut.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        calendarOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calendarOutActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(calendarOut);
        calendarOut.setBounds(20, 200, 180, 30);
        calendarOut.setLocale(Locale.FRENCH);

        jLabelb171.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb171.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb171.setText("Heure");
        bookingNewBookingInfoPanel.add(jLabelb171);
        jLabelb171.setBounds(210, 130, 75, 17);

        timeInField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        timeInField.setText("17h");
        timeInField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                timeInFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                timeInFieldFocusLost(evt);
            }
        });
        timeInField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeInFieldActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(timeInField);
        timeInField.setBounds(210, 150, 60, 30);

        jLabelb172.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb172.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb172.setText("Heure");
        bookingNewBookingInfoPanel.add(jLabelb172);
        jLabelb172.setBounds(210, 180, 50, 20);

        timeOutField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        timeOutField.setText("10h");
        timeOutField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                timeOutFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                timeOutFieldFocusLost(evt);
            }
        });
        timeOutField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeOutFieldActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(timeOutField);
        timeOutField.setBounds(210, 200, 60, 30);

        jLabelb22.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb22.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb22.setText("Choisir Location");
        bookingNewBookingInfoPanel.add(jLabelb22);
        jLabelb22.setBounds(20, 330, 170, 17);

        choosePropertyBox.setBackground(new java.awt.Color(255, 255, 255));
        choosePropertyBox.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        choosePropertyBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePropertyBoxActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(choosePropertyBox);
        choosePropertyBox.setBounds(20, 350, 250, 27);

        amountLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        amountLabel.setForeground(new java.awt.Color(0, 153, 204));
        amountLabel.setText(" Montant Total du Séjour");
        bookingNewBookingInfoPanel.add(amountLabel);
        amountLabel.setBounds(20, 380, 280, 17);

        amountField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        amountField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                amountFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                amountFieldFocusLost(evt);
            }
        });
        amountField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amountFieldActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(amountField);
        amountField.setBounds(20, 400, 250, 30);

        advanceLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        advanceLabel.setForeground(new java.awt.Color(0, 153, 204));
        advanceLabel.setText("Dépôt de Garantie");
        bookingNewBookingInfoPanel.add(advanceLabel);
        advanceLabel.setBounds(20, 480, 190, 20);

        boundField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        boundField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                boundFieldFocusGained(evt);
            }
        });
        boundField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boundFieldActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(boundField);
        boundField.setBounds(20, 500, 250, 30);

        jLabelb85.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb85.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb85.setText("Taxe de Séjour (Unitaire)");
        bookingNewBookingInfoPanel.add(jLabelb85);
        jLabelb85.setBounds(20, 430, 220, 20);

        taxField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        taxField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                taxFieldFocusGained(evt);
            }
        });
        taxField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taxFieldActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(taxField);
        taxField.setBounds(20, 450, 250, 30);

        jLabelb56.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb56.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb56.setText("Ajouter Option");
        jLabelb56.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb56MouseClicked(evt);
            }
        });
        bookingNewBookingInfoPanel.add(jLabelb56);
        jLabelb56.setBounds(230, 10, 110, 18);

        saveButton.setBackground(new java.awt.Color(255, 255, 255));
        saveButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        saveButton.setForeground(new java.awt.Color(102, 102, 102));
        saveButton.setText("Enregistrer");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(saveButton);
        saveButton.setBounds(20, 540, 130, 30);

        deleteButton.setBackground(new java.awt.Color(255, 255, 255));
        deleteButton.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(102, 102, 102));
        deleteButton.setText("Effacer");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(deleteButton);
        deleteButton.setBounds(170, 540, 99, 30);

        jLabelb18.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb18.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb18.setText("Type de Réservation");
        bookingNewBookingInfoPanel.add(jLabelb18);
        jLabelb18.setBounds(20, 30, 190, 20);

        bookingTypeBox.setBackground(new java.awt.Color(255, 255, 255));
        bookingTypeBox.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingTypeBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Direct" }));
        bookingTypeBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                bookingTypeBoxFocusLost(evt);
            }
        });
        bookingTypeBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookingTypeBoxMouseClicked(evt);
            }
        });
        bookingTypeBox.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                bookingTypeBoxComponentShown(evt);
            }
        });
        bookingTypeBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookingTypeBoxActionPerformed(evt);
            }
        });
        bookingNewBookingInfoPanel.add(bookingTypeBox);
        bookingTypeBox.setBounds(20, 50, 250, 30);

        newBooking.add(bookingNewBookingInfoPanel);
        bookingNewBookingInfoPanel.setBounds(690, 20, 350, 610);

        emailBookingPanel.setBackground(new java.awt.Color(255, 255, 255));
        emailBookingPanel.setOpaque(true);

        jCheckBoxb9.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jCheckBoxb9.setForeground(new java.awt.Color(0, 153, 204));
        jCheckBoxb9.setText("Email");
        jCheckBoxb9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxb9ActionPerformed(evt);
            }
        });
        emailBookingPanel.add(jCheckBoxb9);
        jCheckBoxb9.setBounds(370, 270, 400, 23);

        jCheckBoxb11.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jCheckBoxb11.setForeground(new java.awt.Color(0, 153, 204));
        jCheckBoxb11.setText("Courrier");
        emailBookingPanel.add(jCheckBoxb11);
        jCheckBoxb11.setBounds(370, 320, 340, 23);

        jButtonb30.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb30.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb30.setText("Oui ");
        jButtonb30.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jButtonb30FocusLost(evt);
            }
        });
        jButtonb30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonb30MouseClicked(evt);
            }
        });
        jButtonb30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb30ActionPerformed(evt);
            }
        });
        emailBookingPanel.add(jButtonb30);
        jButtonb30.setBounds(370, 370, 130, 40);

        jButtonb31.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb31.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb31.setText("Non ");
        jButtonb31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb31ActionPerformed(evt);
            }
        });
        emailBookingPanel.add(jButtonb31);
        jButtonb31.setBounds(560, 370, 120, 40);

        jLabelb99.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb99.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb99.setText("Envoyer  Contrat");
        emailBookingPanel.add(jLabelb99);
        jLabelb99.setBounds(40, 20, 420, 40);

        jLabelb122.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb122.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb122.setText("Voir Contrat");
        jLabelb122.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb122MouseClicked(evt);
            }
        });
        emailBookingPanel.add(jLabelb122);
        jLabelb122.setBounds(370, 220, 120, 30);

        jLabelb123.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb123.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb123.setText("Envoyer Par Email et/ou Courrier?");
        emailBookingPanel.add(jLabelb123);
        jLabelb123.setBounds(370, 176, 320, 30);

        previousBookingPanel.setBackground(new java.awt.Color(255, 255, 255));
        previousBookingPanel.setMinimumSize(new java.awt.Dimension(550, 350));
        previousBookingPanel.setOpaque(true);
        previousBookingPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        previousBookingPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                previousBookingPanelComponentShown(evt);
            }
        });

        jTableb3.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jTableb3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ", "Nom", "Prénom", "Type", "Location", "Arrivée", "Départ", "Nuit(s)", "Total Client", "Reçu Client", "Site de Rés.", "Reçu "
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableb3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableb3MouseClicked(evt);
            }
        });
        jTableb3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTableb3ComponentShown(evt);
            }
        });
        jScrollPaneb5.setViewportView(jTableb3);

        previousBookingPanel.add(jScrollPaneb5);
        jScrollPaneb5.setBounds(40, 120, 1030, 390);

        jButtonb33.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb33.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButtonb33.setText("Rechercher");
        jButtonb33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb33ActionPerformed(evt);
            }
        });
        previousBookingPanel.add(jButtonb33);
        jButtonb33.setBounds(690, 60, 127, 30);

        jTextFieldb62.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb62.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldb62KeyPressed(evt);
            }
        });
        previousBookingPanel.add(jTextFieldb62);
        jTextFieldb62.setBounds(400, 60, 270, 30);

        jLabelb4.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 18)); // NOI18N
        jLabelb4.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb4.setText("Réservations Terminées");
        previousBookingPanel.add(jLabelb4);
        jLabelb4.setBounds(40, 10, 250, 22);

        jComboBoxb11.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb11.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jComboBoxb11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toutes mes propriétés" }));
        jComboBoxb11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxb11ActionPerformed(evt);
            }
        });
        previousBookingPanel.add(jComboBoxb11);
        jComboBoxb11.setBounds(40, 60, 250, 30);

        jLabelb76.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb76.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb76.setText("Rechercher par Propriété");
        previousBookingPanel.add(jLabelb76);
        jLabelb76.setBounds(40, 40, 250, 17);

        jLabelb103.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb103.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb103.setText("Envoyer Email");
        jLabelb103.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb103MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelb103MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelb103MouseExited(evt);
            }
        });
        previousBookingPanel.add(jLabelb103);
        jLabelb103.setBounds(340, 550, 120, 17);

        jLabelb137.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb137.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb137.setText("Rechercher par Nom de Famille");
        previousBookingPanel.add(jLabelb137);
        jLabelb137.setBounds(400, 40, 250, 17);

        jLabelb138.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb138.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb138.setText("Voir Contrat");
        jLabelb138.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb138MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelb138MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelb138MouseExited(evt);
            }
        });
        previousBookingPanel.add(jLabelb138);
        jLabelb138.setBounds(50, 550, 120, 17);

        jLabelb142.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb142.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb142.setText("Voir Facture");
        jLabelb142.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb142MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelb142MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelb142MouseExited(evt);
            }
        });
        previousBookingPanel.add(jLabelb142);
        jLabelb142.setBounds(200, 550, 120, 17);

        jLabel3.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Paiement Site De Réservation");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel3MouseExited(evt);
            }
        });
        previousBookingPanel.add(jLabel3);
        jLabel3.setBounds(690, 550, 240, 17);

        jLabel4.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Paiement Client");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited(evt);
            }
        });
        previousBookingPanel.add(jLabel4);
        jLabel4.setBounds(500, 550, 140, 17);

        jLabel5.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 204));
        jLabel5.setText("*Selectioner une reservation dans le tableau puis cliquer sur l'une des actions ci-dessous.");
        previousBookingPanel.add(jLabel5);
        jLabel5.setBounds(40, 520, 770, 16);

        bookingsPanel.setBackground(new java.awt.Color(255, 255, 255));
        bookingsPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                bookingsPanelComponentShown(evt);
            }
        });
        bookingsPanel.setLayout(null);

        bookingMenuPanel.setBackground(new java.awt.Color(255, 255, 255));
        bookingMenuPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 204), 2, true));
        bookingMenuPanel.setForeground(new java.awt.Color(255, 255, 255));
        bookingMenuPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bookingMenuPanelMouseExited(evt);
            }
        });
        bookingMenuPanel.setLayout(null);

        cancelBookingLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        cancelBookingLabel.setForeground(new java.awt.Color(153, 153, 153));
        cancelBookingLabel.setText("Anuler Réservation");
        cancelBookingLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelBookingLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelBookingLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelBookingLabelMouseExited(evt);
            }
        });
        bookingMenuPanel.add(cancelBookingLabel);
        cancelBookingLabel.setBounds(10, 210, 140, 30);

        buildEnvelopLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        buildEnvelopLabel.setForeground(new java.awt.Color(102, 102, 102));
        buildEnvelopLabel.setText("Créer Enveloppe");
        buildEnvelopLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buildEnvelopLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buildEnvelopLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buildEnvelopLabelMouseExited(evt);
            }
        });
        bookingMenuPanel.add(buildEnvelopLabel);
        buildEnvelopLabel.setBounds(10, 180, 130, 30);

        NewContractLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        NewContractLabel.setForeground(new java.awt.Color(102, 102, 102));
        NewContractLabel.setText("Nouveau Contrat");
        NewContractLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NewContractLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                NewContractLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                NewContractLabelMouseExited(evt);
            }
        });
        bookingMenuPanel.add(NewContractLabel);
        NewContractLabel.setBounds(10, 120, 130, 30);

        sendEmailLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        sendEmailLabel.setForeground(new java.awt.Color(102, 102, 102));
        sendEmailLabel.setText("Envoyer Email ");
        sendEmailLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendEmailLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sendEmailLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sendEmailLabelMouseExited(evt);
            }
        });
        bookingMenuPanel.add(sendEmailLabel);
        sendEmailLabel.setBounds(10, 30, 130, 30);

        jLabel202.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        jLabel202.setForeground(new java.awt.Color(102, 102, 102));
        jLabel202.setText("Menu");
        jLabel202.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel202MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel202MouseEntered(evt);
            }
        });
        bookingMenuPanel.add(jLabel202);
        jLabel202.setBounds(100, 0, 50, 20);

        jLabelb75.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb75.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb75.setText("Voir contrat ");
        jLabelb75.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb75MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelb75MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelb75MouseExited(evt);
            }
        });
        bookingMenuPanel.add(jLabelb75);
        jLabelb75.setBounds(10, 90, 130, 30);

        jLabelb80.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb80.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb80.setText("Gérer Option");
        jLabelb80.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb80MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelb80MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelb80MouseExited(evt);
            }
        });
        bookingMenuPanel.add(jLabelb80);
        jLabelb80.setBounds(10, 60, 130, 30);

        buildbill.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        buildbill.setForeground(new java.awt.Color(102, 102, 102));
        buildbill.setText("Créer Facture");
        buildbill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buildbillMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buildbillMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buildbillMouseExited(evt);
            }
        });
        bookingMenuPanel.add(buildbill);
        buildbill.setBounds(10, 150, 130, 30);

        bookingsPanel.add(bookingMenuPanel);
        bookingMenuPanel.setBounds(930, 10, 150, 260);

        addAmountPanel.setBackground(new java.awt.Color(255, 255, 255));
        addAmountPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 153, 204), 2, true));

        jTextField21.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextField21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField21KeyPressed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButton1.setText("ok");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel196.setBackground(new java.awt.Color(255, 255, 255));
        jLabel196.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel196.setForeground(new java.awt.Color(0, 153, 204));
        jLabel196.setText("Ajouter montant pour les jours en plus");

        javax.swing.GroupLayout addAmountPanelLayout = new javax.swing.GroupLayout(addAmountPanel);
        addAmountPanel.setLayout(addAmountPanelLayout);
        addAmountPanelLayout.setHorizontalGroup(
            addAmountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addAmountPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123))
            .addGroup(addAmountPanelLayout.createSequentialGroup()
                .addGroup(addAmountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addAmountPanelLayout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addAmountPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel196, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        addAmountPanelLayout.setVerticalGroup(
            addAmountPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addAmountPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel196)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bookingsPanel.add(addAmountPanel);
        addAmountPanel.setBounds(570, 120, 320, 100);

        jButtonb20.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb20.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb20.setText("Enregistrer");
        jButtonb20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb20ActionPerformed(evt);
            }
        });
        bookingsPanel.add(jButtonb20);
        jButtonb20.setBounds(520, 560, 160, 29);

        jButtonb21.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb21.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb21.setText("Effacer");
        jButtonb21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb21ActionPerformed(evt);
            }
        });
        bookingsPanel.add(jButtonb21);
        jButtonb21.setBounds(710, 560, 160, 29);

        jLabelb50.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb50.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb50.setText("Réservation ");
        bookingsPanel.add(jLabelb50);
        jLabelb50.setBounds(30, 0, 162, 30);

        jLabelb51.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb51.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb51.setText("Nom ");
        clientPanel.add(jLabelb51);
        jLabelb51.setBounds(20, 40, 60, 17);

        bookingNameField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingNameField.setBorder(null);
        clientPanel.add(bookingNameField);
        bookingNameField.setBounds(20, 60, 130, 17);

        jLabelb59.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb59.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb59.setText("Prénom");
        clientPanel.add(jLabelb59);
        jLabelb59.setBounds(20, 90, 60, 17);

        bookingFirstNameField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingFirstNameField.setBorder(null);
        bookingFirstNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                bookingFirstNameFieldFocusLost(evt);
            }
        });
        bookingFirstNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookingFirstNameFieldActionPerformed(evt);
            }
        });
        bookingFirstNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bookingFirstNameFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bookingFirstNameFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bookingFirstNameFieldKeyTyped(evt);
            }
        });
        clientPanel.add(bookingFirstNameField);
        bookingFirstNameField.setBounds(20, 110, 130, 17);

        jLabelb54.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb54.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb54.setText("Telephone 1");
        clientPanel.add(jLabelb54);
        jLabelb54.setBounds(20, 190, 180, 17);

        bookingTel1Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingTel1Field.setBorder(null);
        clientPanel.add(bookingTel1Field);
        bookingTel1Field.setBounds(20, 210, 180, 17);

        jLabelb58.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb58.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb58.setText("Email");
        clientPanel.add(jLabelb58);
        jLabelb58.setBounds(20, 290, 150, 17);

        bookingEmailField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingEmailField.setBorder(null);
        clientPanel.add(bookingEmailField);
        bookingEmailField.setBounds(20, 310, 210, 17);

        jLabelb60.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb60.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb60.setText("Adresse 1");
        clientPanel.add(jLabelb60);
        jLabelb60.setBounds(240, 40, 110, 17);

        bookingStreet1Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingStreet1Field.setBorder(null);
        clientPanel.add(bookingStreet1Field);
        bookingStreet1Field.setBounds(240, 60, 260, 17);

        jLabelb61.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb61.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb61.setText("Genre");
        clientPanel.add(jLabelb61);
        jLabelb61.setBounds(20, 140, 60, 17);

        jLabelb62.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb62.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb62.setText("Commune Délégée");
        clientPanel.add(jLabelb62);
        jLabelb62.setBounds(240, 140, 170, 17);

        bookingDcField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingDcField.setBorder(null);
        bookingDcField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookingDcFieldActionPerformed(evt);
            }
        });
        clientPanel.add(bookingDcField);
        bookingDcField.setBounds(240, 160, 210, 17);

        jLabelb63.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb63.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb63.setText("Numéro d'Itentité");
        clientPanel.add(jLabelb63);
        jLabelb63.setBounds(240, 290, 200, 17);

        bookingIdProofField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingIdProofField.setBorder(null);
        bookingIdProofField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookingIdProofFieldActionPerformed(evt);
            }
        });
        clientPanel.add(bookingIdProofField);
        bookingIdProofField.setBounds(240, 310, 210, 17);

        bookingCommentText.setColumns(20);
        bookingCommentText.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingCommentText.setLineWrap(true);
        bookingCommentText.setRows(5);
        bookingCommentText.setWrapStyleWord(true);
        jScrollPaneb3.setViewportView(bookingCommentText);

        clientPanel.add(jScrollPaneb3);
        jScrollPaneb3.setBounds(20, 360, 290, 130);

        jLabelb72.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb72.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb72.setText("Commentaire");
        clientPanel.add(jLabelb72);
        jLabelb72.setBounds(20, 330, 110, 20);

        jLabelb91.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb91.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb91.setText("Code Postal");
        clientPanel.add(jLabelb91);
        jLabelb91.setBounds(240, 190, 140, 17);

        bookingPostCodeField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingPostCodeField.setBorder(null);
        clientPanel.add(bookingPostCodeField);
        bookingPostCodeField.setBounds(240, 210, 240, 17);

        jLabelb92.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb92.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb92.setText("Ville - Pays");
        clientPanel.add(jLabelb92);
        jLabelb92.setBounds(240, 240, 120, 17);

        bookingCityField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingCityField.setBorder(null);
        clientPanel.add(bookingCityField);
        bookingCityField.setBounds(240, 260, 240, 17);

        bookingGenderBox.setBackground(new java.awt.Color(255, 255, 255));
        bookingGenderBox.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingGenderBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Homme", "Femme", "Autre" }));
        clientPanel.add(bookingGenderBox);
        bookingGenderBox.setBounds(20, 160, 120, 27);

        jLabelb114.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb114.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb114.setText("Info Client");
        clientPanel.add(jLabelb114);
        jLabelb114.setBounds(20, 10, 120, 20);

        jLabelb167.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb167.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb167.setText("Telephone 2");
        clientPanel.add(jLabelb167);
        jLabelb167.setBounds(20, 240, 180, 17);

        bookingTel2Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingTel2Field.setBorder(null);
        clientPanel.add(bookingTel2Field);
        bookingTel2Field.setBounds(20, 260, 180, 17);

        jLabelb168.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb168.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb168.setText("Adresse 2");
        clientPanel.add(jLabelb168);
        jLabelb168.setBounds(240, 90, 110, 17);

        bookingStreet2Field.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingStreet2Field.setBorder(null);
        clientPanel.add(bookingStreet2Field);
        bookingStreet2Field.setBounds(240, 110, 260, 17);

        bookingBlackListCheckBox.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 12)); // NOI18N
        bookingBlackListCheckBox.setText("Marquer comme client indésirable");
        bookingBlackListCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bookingBlackListCheckBoxStateChanged(evt);
            }
        });
        clientPanel.add(bookingBlackListCheckBox);
        bookingBlackListCheckBox.setBounds(20, 500, 250, 23);

        bookingsPanel.add(clientPanel);
        clientPanel.setBounds(20, 30, 510, 550);

        bookingPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bookingPanelMouseEntered(evt);
            }
        });

        jLabelb55.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb55.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb55.setText("Prix du Séjour");
        bookingPanel.add(jLabelb55);
        jLabelb55.setBounds(20, 350, 130, 17);

        jTextFieldb29.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb29.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldb29FocusLost(evt);
            }
        });
        jTextFieldb29.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldb29KeyPressed(evt);
            }
        });
        bookingPanel.add(jTextFieldb29);
        jTextFieldb29.setBounds(20, 370, 100, 30);

        jLabelb53.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb53.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb53.setText("Départ");
        bookingPanel.add(jLabelb53);
        jLabelb53.setBounds(20, 130, 80, 20);

        jLabelb57.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb57.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb57.setText("RESTE À PAYER");
        bookingPanel.add(jLabelb57);
        jLabelb57.setBounds(320, 420, 150, 30);

        toPayField.setEditable(false);
        toPayField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 16)); // NOI18N
        toPayField.setBorder(null);
        toPayField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toPayFieldActionPerformed(evt);
            }
        });
        bookingPanel.add(toPayField);
        toPayField.setBounds(340, 450, 80, 30);

        jLabelb52.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb52.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb52.setText("Arrivée");
        bookingPanel.add(jLabelb52);
        jLabelb52.setBounds(20, 80, 57, 17);

        jLabelb64.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb64.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb64.setText("Nuit(s)");
        bookingPanel.add(jLabelb64);
        jLabelb64.setBounds(390, 40, 70, 17);

        jTextFieldb38.setEditable(false);
        jTextFieldb38.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb38.setBorder(null);
        jTextFieldb38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb38ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb38);
        jTextFieldb38.setBounds(390, 60, 60, 17);

        jLabelb65.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb65.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb65.setText("TOTAL");
        bookingPanel.add(jLabelb65);
        jLabelb65.setBounds(40, 420, 70, 30);

        grandTotalField.setEditable(false);
        grandTotalField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 16)); // NOI18N
        grandTotalField.setBorder(null);
        grandTotalField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grandTotalFieldActionPerformed(evt);
            }
        });
        bookingPanel.add(grandTotalField);
        grandTotalField.setBounds(50, 450, 70, 30);

        jLabelb66.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb66.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb66.setText("Adulte");
        bookingPanel.add(jLabelb66);
        jLabelb66.setBounds(20, 190, 80, 17);

        jTextFieldb40.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb40.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldb40FocusLost(evt);
            }
        });
        jTextFieldb40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb40ActionPerformed(evt);
            }
        });
        jTextFieldb40.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldb40KeyPressed(evt);
            }
        });
        bookingPanel.add(jTextFieldb40);
        jTextFieldb40.setBounds(20, 210, 70, 30);

        jLabelb67.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb67.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb67.setText("Enfant");
        bookingPanel.add(jLabelb67);
        jLabelb67.setBounds(150, 190, 70, 17);

        jTextFieldb41.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb41ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb41);
        jTextFieldb41.setBounds(150, 210, 70, 30);

        jLabelb68.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb68.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb68.setText("Lit Simple");
        bookingPanel.add(jLabelb68);
        jLabelb68.setBounds(390, 190, 90, 17);

        jTextFieldb42.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingPanel.add(jTextFieldb42);
        jTextFieldb42.setBounds(390, 210, 70, 30);

        jLabelb69.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb69.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb69.setText("Lit Double");
        bookingPanel.add(jLabelb69);
        jLabelb69.setBounds(270, 190, 80, 17);

        jTextFieldb43.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        bookingPanel.add(jTextFieldb43);
        jTextFieldb43.setBounds(270, 210, 70, 30);

        jLabelb70.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb70.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb70.setText("Type");
        bookingPanel.add(jLabelb70);
        jLabelb70.setBounds(270, 40, 80, 17);

        jTextFieldb44.setEditable(false);
        jTextFieldb44.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb44.setBorder(null);
        jTextFieldb44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb44ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb44);
        jTextFieldb44.setBounds(270, 60, 120, 17);

        jLabelb71.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb71.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb71.setText("Nom Location");
        bookingPanel.add(jLabelb71);
        jLabelb71.setBounds(20, 40, 140, 17);

        jTextFieldb45.setEditable(false);
        jTextFieldb45.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb45.setBorder(null);
        jTextFieldb45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb45ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb45);
        jTextFieldb45.setBounds(20, 60, 250, 17);

        jLabelb74.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb74.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb74.setText("REÇU");
        bookingPanel.add(jLabelb74);
        jLabelb74.setBounds(200, 420, 60, 30);

        paidField.setEditable(false);
        paidField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 16)); // NOI18N
        paidField.setBorder(null);
        bookingPanel.add(paidField);
        paidField.setBounds(220, 450, 60, 30);

        jCheckBoxb1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jCheckBoxb1.setText("Recu");
        jCheckBoxb1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxb1MouseClicked(evt);
            }
        });
        jCheckBoxb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxb1ActionPerformed(evt);
            }
        });
        bookingPanel.add(jCheckBoxb1);
        jCheckBoxb1.setBounds(330, 270, 150, 23);

        jLabelb77.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb77.setForeground(new java.awt.Color(0, 153, 204));
        bookingPanel.add(jLabelb77);
        jLabelb77.setBounds(350, 370, 70, 30);

        jTextFieldb26.setEditable(false);
        jTextFieldb26.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb26.setBorder(null);
        jTextFieldb26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb26ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb26);
        jTextFieldb26.setBounds(340, 300, 50, 17);

        jLabelb78.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb78.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb78.setText("Arrhes");
        bookingPanel.add(jLabelb78);
        jLabelb78.setBounds(20, 250, 60, 17);

        jTextFieldb27.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb27.setBorder(null);
        jTextFieldb27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb27ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb27);
        jTextFieldb27.setBounds(30, 300, 50, 17);

        jCheckBoxb2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jCheckBoxb2.setText("Recu");
        jCheckBoxb2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxb2MouseClicked(evt);
            }
        });
        jCheckBoxb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxb2ActionPerformed(evt);
            }
        });
        bookingPanel.add(jCheckBoxb2);
        jCheckBoxb2.setBounds(20, 270, 160, 23);

        jCheckBoxb3.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jCheckBoxb3.setText("Signé");
        jCheckBoxb3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBoxb3MouseClicked(evt);
            }
        });
        jCheckBoxb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxb3ActionPerformed(evt);
            }
        });
        bookingPanel.add(jCheckBoxb3);
        jCheckBoxb3.setBounds(180, 270, 150, 23);

        jLabelb81.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb81.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb81.setText("Contrat");
        bookingPanel.add(jLabelb81);
        jLabelb81.setBounds(180, 250, 90, 17);

        jTextFieldb35.setEditable(false);
        jTextFieldb35.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb35.setBorder(null);
        jTextFieldb35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb35ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb35);
        jTextFieldb35.setBounds(190, 370, 70, 30);

        jLabelb94.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb94.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb94.setText("Taxe de Séjour");
        bookingPanel.add(jLabelb94);
        jLabelb94.setBounds(180, 350, 120, 17);

        jLabelb115.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb115.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb115.setText("Info Réservation");
        bookingPanel.add(jLabelb115);
        jLabelb115.setBounds(20, 10, 190, 20);

        jLabelb79.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb79.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb79.setText("Total Option");
        bookingPanel.add(jLabelb79);
        jLabelb79.setBounds(330, 350, 110, 17);

        jTextFieldb48.setEditable(false);
        jTextFieldb48.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb48.setBorder(null);
        jTextFieldb48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb48ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb48);
        jTextFieldb48.setBounds(340, 370, 90, 30);

        jLabelb30.setForeground(new java.awt.Color(153, 153, 153));
        jLabelb30.setText("________________________________________________________________________");
        bookingPanel.add(jLabelb30);
        jLabelb30.setBounds(20, 480, 504, 20);

        jLabelb133.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb133.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb133.setText("Caution");
        bookingPanel.add(jLabelb133);
        jLabelb133.setBounds(330, 250, 70, 17);

        jXDatePicker1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jXDatePicker1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jXDatePicker1ComponentShown(evt);
            }
        });
        jXDatePicker1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker1ActionPerformed(evt);
            }
        });
        jXDatePicker1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jXDatePicker1PropertyChange(evt);
            }
        });
        bookingPanel.add(jXDatePicker1);
        jXDatePicker1.setBounds(20, 100, 220, 30);
        jXDatePicker1.setLocale(Locale.FRENCH);

        jXDatePicker2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jXDatePicker2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jXDatePicker2ComponentShown(evt);
            }
        });
        jXDatePicker2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker2ActionPerformed(evt);
            }
        });
        bookingPanel.add(jXDatePicker2);
        jXDatePicker2.setBounds(20, 150, 220, 30);
        jXDatePicker2.setLocale(Locale.FRENCH);

        jTextFieldb86.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb86ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb86);
        jTextFieldb86.setBounds(270, 100, 60, 30);

        jLabelb175.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb175.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb175.setText("Heure");
        bookingPanel.add(jLabelb175);
        jLabelb175.setBounds(270, 80, 70, 17);

        jTextFieldb87.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb87ActionPerformed(evt);
            }
        });
        bookingPanel.add(jTextFieldb87);
        jTextFieldb87.setBounds(270, 150, 60, 30);

        jLabelb176.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb176.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb176.setText("Heure");
        bookingPanel.add(jLabelb176);
        jLabelb176.setBounds(270, 130, 50, 17);

        jLabelb31.setForeground(new java.awt.Color(153, 153, 153));
        jLabelb31.setText("________________________________________________________________________");
        bookingPanel.add(jLabelb31);
        jLabelb31.setBounds(20, 320, 504, 20);

        bookingsPanel.add(bookingPanel);
        bookingPanel.setBounds(510, 30, 520, 520);

        jButtonb22.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb22.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jButtonb22.setText("Terminer/Payer");
        jButtonb22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb22ActionPerformed(evt);
            }
        });
        bookingsPanel.add(jButtonb22);
        jButtonb22.setBounds(890, 560, 160, 29);

        jLabel200.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        jLabel200.setForeground(new java.awt.Color(102, 102, 102));
        jLabel200.setText("Menu");
        jLabel200.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel200MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel200MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel200MouseExited(evt);
            }
        });
        bookingsPanel.add(jLabel200);
        jLabel200.setBounds(1030, 10, 50, 20);

        bookingTypeLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
        bookingTypeLabel.setForeground(new java.awt.Color(102, 102, 102));
        bookingTypeLabel.setText("booking type");
        bookingTypeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookingTypeLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bookingTypeLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bookingTypeLabelMouseExited(evt);
            }
        });
        bookingsPanel.add(bookingTypeLabel);
        bookingTypeLabel.setBounds(200, 0, 161, 30);

        endBookingPanel.setBackground(new java.awt.Color(255, 255, 255));
        endBookingPanel.setForeground(new java.awt.Color(204, 255, 255));
        endBookingPanel.setOpaque(true);
        endBookingPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                endBookingPanelComponentShown(evt);
            }
        });

        jButtonb24.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb24.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb24.setText("Confirmer");
        jButtonb24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb24ActionPerformed(evt);
            }
        });
        endBookingPanel.add(jButtonb24);
        jButtonb24.setBounds(370, 450, 140, 40);

        jButtonb25.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb25.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb25.setText("Anuler");
        jButtonb25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb25ActionPerformed(evt);
            }
        });
        endBookingPanel.add(jButtonb25);
        jButtonb25.setBounds(570, 450, 140, 40);

        jCheckBoxb5.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBoxb5.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jCheckBoxb5.setForeground(new java.awt.Color(102, 102, 102));
        jCheckBoxb5.setText("Terminer la Réservation");
        jCheckBoxb5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxb5ActionPerformed(evt);
            }
        });
        endBookingPanel.add(jCheckBoxb5);
        jCheckBoxb5.setBounds(370, 280, 260, 30);

        jComboBoxb9.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb9.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 13)); // NOI18N
        jComboBoxb9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Espèce", "Virement", "Chèque", "Chèque-Vacances", "CB", "Autre" }));
        jComboBoxb9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxb9ActionPerformed(evt);
            }
        });
        endBookingPanel.add(jComboBoxb9);
        jComboBoxb9.setBounds(910, 100, 130, 30);

        jLabelb95.setBackground(new java.awt.Color(255, 255, 255));
        jLabelb95.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb95.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb95.setText("Methode de Paiement");
        endBookingPanel.add(jLabelb95);
        jLabelb95.setBounds(720, 100, 160, 30);

        jTextFieldb61.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 13)); // NOI18N
        jTextFieldb61.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldb61FocusLost(evt);
            }
        });
        jTextFieldb61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb61ActionPerformed(evt);
            }
        });
        jTextFieldb61.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldb61KeyPressed(evt);
            }
        });
        endBookingPanel.add(jTextFieldb61);
        jTextFieldb61.setBounds(910, 140, 130, 30);

        jLabelb96.setBackground(new java.awt.Color(255, 255, 255));
        jLabelb96.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb96.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb96.setText("Montant Payé");
        endBookingPanel.add(jLabelb96);
        jLabelb96.setBounds(720, 140, 130, 30);

        jLabelb118.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb118.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb118.setText("Terminer / Payer");
        endBookingPanel.add(jLabelb118);
        jLabelb118.setBounds(30, 10, 340, 40);

        jLabelb98.setBackground(new java.awt.Color(255, 255, 255));
        jLabelb98.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb98.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb98.setText("* l'envoi par email prendra une dizaine de seconde...");
        endBookingPanel.add(jLabelb98);
        jLabelb98.setBounds(370, 500, 410, 30);

        jLabelb87.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb87.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb87.setText("Montant Total ");
        endBookingPanel.add(jLabelb87);
        jLabelb87.setBounds(380, 110, 110, 30);

        jTextFieldb53.setEditable(false);
        jTextFieldb53.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb53.setBorder(null);
        endBookingPanel.add(jTextFieldb53);
        jTextFieldb53.setBounds(530, 110, 130, 30);

        jLabelb89.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb89.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb89.setText("Montant Reçu");
        endBookingPanel.add(jLabelb89);
        jLabelb89.setBounds(380, 140, 130, 30);

        jTextFieldb54.setEditable(false);
        jTextFieldb54.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb54.setBorder(null);
        endBookingPanel.add(jTextFieldb54);
        jTextFieldb54.setBounds(530, 140, 130, 30);

        jLabelb83.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb83.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb83.setText("Reste à Payer");
        endBookingPanel.add(jLabelb83);
        jLabelb83.setBounds(380, 170, 120, 30);

        jTextFieldb49.setEditable(false);
        jTextFieldb49.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb49.setBorder(null);
        jTextFieldb49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb49ActionPerformed(evt);
            }
        });
        endBookingPanel.add(jTextFieldb49);
        jTextFieldb49.setBounds(530, 170, 130, 30);

        jLabelb86.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb86.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb86.setText("Nombre de Nuit");
        endBookingPanel.add(jLabelb86);
        jLabelb86.setBounds(30, 110, 140, 30);

        jLabelb88.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb88.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb88.setText("Option");
        endBookingPanel.add(jLabelb88);
        jLabelb88.setBounds(30, 170, 120, 30);

        jTextFieldb50.setEditable(false);
        jTextFieldb50.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb50.setBorder(null);
        jTextFieldb50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb50ActionPerformed(evt);
            }
        });
        endBookingPanel.add(jTextFieldb50);
        jTextFieldb50.setBounds(220, 140, 100, 30);

        jTextFieldb51.setEditable(false);
        jTextFieldb51.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb51.setBorder(null);
        endBookingPanel.add(jTextFieldb51);
        jTextFieldb51.setBounds(220, 110, 100, 30);

        jTextFieldb52.setEditable(false);
        jTextFieldb52.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb52.setBorder(null);
        endBookingPanel.add(jTextFieldb52);
        jTextFieldb52.setBounds(220, 170, 100, 30);

        jLabelb97.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb97.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb97.setText(" Régler la Totalité");
        jLabelb97.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb97MouseClicked(evt);
            }
        });
        endBookingPanel.add(jLabelb97);
        jLabelb97.setBounds(910, 180, 130, 30);

        jLabelb116.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb116.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb116.setText("Paiement");
        endBookingPanel.add(jLabelb116);
        jLabelb116.setBounds(380, 80, 140, 20);

        jLabelb120.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb120.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb120.setText("Récapitulatif");
        endBookingPanel.add(jLabelb120);
        jLabelb120.setBounds(30, 80, 140, 20);

        jLabelb121.setText("_____________________________________________");
        endBookingPanel.add(jLabelb121);
        jLabelb121.setBounds(380, 240, 320, 20);

        jLabelb128.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb128.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb128.setText("Taxe de Séjour Adulte");
        endBookingPanel.add(jLabelb128);
        jLabelb128.setBounds(30, 140, 160, 30);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(null);

        jCheckBoxb8.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBoxb8.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jCheckBoxb8.setForeground(new java.awt.Color(0, 153, 204));
        jCheckBoxb8.setText(" Courrier");
        jCheckBoxb8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxb8ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBoxb8);
        jCheckBoxb8.setBounds(20, 80, 110, 23);

        jCheckBoxb6.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBoxb6.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jCheckBoxb6.setForeground(new java.awt.Color(0, 153, 204));
        jCheckBoxb6.setText("Email");
        jCheckBoxb6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxb6ActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBoxb6);
        jCheckBoxb6.setBounds(20, 40, 100, 23);

        jLabelb119.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb119.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb119.setText("Envoi Email - Courrier");
        jPanel4.add(jLabelb119);
        jLabelb119.setBounds(20, 10, 210, 20);

        endBookingPanel.add(jPanel4);
        jPanel4.setBounds(370, 310, 350, 130);

        bookingExternalPanel.setBackground(new java.awt.Color(255, 255, 255));
        bookingExternalPanel.setForeground(new java.awt.Color(204, 255, 255));
        bookingExternalPanel.setOpaque(true);
        bookingExternalPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                bookingExternalPanelComponentShown(evt);
            }
        });

        jButtonb26.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb26.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb26.setText("Confirmer");
        jButtonb26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb26ActionPerformed(evt);
            }
        });
        bookingExternalPanel.add(jButtonb26);
        jButtonb26.setBounds(330, 440, 140, 40);

        jButtonb28.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb28.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jButtonb28.setText("Anuler");
        jButtonb28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb28ActionPerformed(evt);
            }
        });
        bookingExternalPanel.add(jButtonb28);
        jButtonb28.setBounds(560, 440, 140, 40);

        jTextFieldb64.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 13)); // NOI18N
        jTextFieldb64.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldb64FocusLost(evt);
            }
        });
        jTextFieldb64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb64ActionPerformed(evt);
            }
        });
        jTextFieldb64.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldb64KeyPressed(evt);
            }
        });
        bookingExternalPanel.add(jTextFieldb64);
        jTextFieldb64.setBounds(470, 140, 130, 30);

        jLabelb107.setBackground(new java.awt.Color(255, 255, 255));
        jLabelb107.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb107.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb107.setText("Montant Payé");
        bookingExternalPanel.add(jLabelb107);
        jLabelb107.setBounds(330, 140, 130, 30);

        externalBookingTitleLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        externalBookingTitleLabel.setForeground(new java.awt.Color(0, 153, 204));
        externalBookingTitleLabel.setText("externalBookingTitleLabel");
        bookingExternalPanel.add(externalBookingTitleLabel);
        externalBookingTitleLabel.setBounds(30, 10, 540, 40);

        jLabelb93.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb93.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb93.setText("Montant Total ");
        bookingExternalPanel.add(jLabelb93);
        jLabelb93.setBounds(330, 290, 110, 30);

        jTextFieldb55.setEditable(false);
        jTextFieldb55.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb55.setBorder(null);
        bookingExternalPanel.add(jTextFieldb55);
        jTextFieldb55.setBounds(480, 290, 130, 30);

        jLabelb136.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb136.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb136.setText("Montant Reçu");
        bookingExternalPanel.add(jLabelb136);
        jLabelb136.setBounds(330, 320, 130, 30);

        jTextFieldb56.setEditable(false);
        jTextFieldb56.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb56.setBorder(null);
        bookingExternalPanel.add(jTextFieldb56);
        jTextFieldb56.setBounds(480, 320, 130, 30);

        jLabelb159.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb159.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb159.setText("Reste à Payer");
        bookingExternalPanel.add(jLabelb159);
        jLabelb159.setBounds(330, 350, 120, 30);

        jTextFieldb57.setEditable(false);
        jTextFieldb57.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb57.setBorder(null);
        jTextFieldb57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb57ActionPerformed(evt);
            }
        });
        bookingExternalPanel.add(jTextFieldb57);
        jTextFieldb57.setBounds(480, 350, 130, 30);

        jLabelb173.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabelb173.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb173.setText(" Régler la Totalité");
        jLabelb173.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb173MouseClicked(evt);
            }
        });
        bookingExternalPanel.add(jLabelb173);
        jLabelb173.setBounds(610, 140, 130, 30);

        jLabelb174.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb174.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb174.setText("Aperçu");
        bookingExternalPanel.add(jLabelb174);
        jLabelb174.setBounds(330, 250, 200, 20);

        jLabelb181.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 16)); // NOI18N
        jLabelb181.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb181.setText("Paiement");
        bookingExternalPanel.add(jLabelb181);
        jLabelb181.setBounds(330, 110, 140, 20);

        allBookingPanel.setBackground(new java.awt.Color(255, 255, 255));
        allBookingPanel.setMinimumSize(new java.awt.Dimension(550, 350));
        allBookingPanel.setOpaque(true);
        allBookingPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        allBookingPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                allBookingPanelComponentShown(evt);
            }
        });

        jTableb1.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jTableb1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ", "Nom", "Prénom", "Type", "Location", "Arrivée", "Départ", "Nuit(s)", "Total", "Reçu", "Arrhes", "Contrat", "Caution"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableb1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        jTableb1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableb1MouseClicked(evt);
            }
        });
        jTableb1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTableb1ComponentShown(evt);
            }
        });
        jScrollPaneb2.setViewportView(jTableb1);
        if (jTableb1.getColumnModel().getColumnCount() > 0) {
            jTableb1.getColumnModel().getColumn(10).setResizable(false);
            jTableb1.getColumnModel().getColumn(11).setResizable(false);
            jTableb1.getColumnModel().getColumn(12).setResizable(false);
        }

        allBookingPanel.add(jScrollPaneb2);
        jScrollPaneb2.setBounds(30, 120, 1040, 490);

        jButtonb34.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb34.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButtonb34.setText("Rechercher");
        jButtonb34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb34ActionPerformed(evt);
            }
        });
        allBookingPanel.add(jButtonb34);
        jButtonb34.setBounds(650, 80, 113, 30);

        jTextFieldb63.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextFieldb63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldb63ActionPerformed(evt);
            }
        });
        jTextFieldb63.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldb63KeyPressed(evt);
            }
        });
        allBookingPanel.add(jTextFieldb63);
        jTextFieldb63.setBounds(360, 80, 260, 30);

        jLabelb101.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb101.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb101.setText("Rechercher Par Propriété");
        allBookingPanel.add(jLabelb101);
        jLabelb101.setBounds(30, 60, 260, 17);

        jLabelb102.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 16)); // NOI18N
        jLabelb102.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb102.setText("*Cliquer sur une réservation pour voir le detail");
        allBookingPanel.add(jLabelb102);
        jLabelb102.setBounds(30, 630, 630, 20);

        jComboBoxb10.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxb10.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jComboBoxb10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toutes mes propriétés" }));
        jComboBoxb10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxb10ActionPerformed(evt);
            }
        });
        allBookingPanel.add(jComboBoxb10);
        jComboBoxb10.setBounds(30, 80, 250, 30);

        jLabelb117.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb117.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb117.setText("Réservation En Cours");
        allBookingPanel.add(jLabelb117);
        jLabelb117.setBounds(30, 10, 290, 30);

        jLabelb127.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabelb127.setForeground(new java.awt.Color(102, 102, 102));
        jLabelb127.setText("Rechercher Par Nom De Famille");
        allBookingPanel.add(jLabelb127);
        jLabelb127.setBounds(360, 60, 260, 17);

        sendEmailPanel.setBackground(new java.awt.Color(255, 255, 255));
        sendEmailPanel.setOpaque(true);
        sendEmailPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                sendEmailPanelComponentShown(evt);
            }
        });

        jButtonb32.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb32.setText("Anuler");
        jButtonb32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb32ActionPerformed(evt);
            }
        });
        sendEmailPanel.add(jButtonb32);
        jButtonb32.setBounds(530, 560, 140, 30);

        jLabelb141.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb141.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb141.setText("Envoyer Email");
        sendEmailPanel.add(jLabelb141);
        jLabelb141.setBounds(20, 10, 400, 30);

        jButtonb35.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb35.setText("Valider");
        jButtonb35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb35ActionPerformed(evt);
            }
        });
        sendEmailPanel.add(jButtonb35);
        jButtonb35.setBounds(300, 560, 140, 30);

        jTextField14.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });
        sendEmailPanel.add(jTextField14);
        jTextField14.setBounds(200, 140, 610, 30);

        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jTextArea3.setLineWrap(true);
        jTextArea3.setRows(5);
        jTextArea3.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextArea3);

        sendEmailPanel.add(jScrollPane2);
        jScrollPane2.setBounds(200, 210, 620, 260);

        jLabel159.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel159.setForeground(new java.awt.Color(0, 153, 204));
        jLabel159.setText("Objet:");
        sendEmailPanel.add(jLabel159);
        jLabel159.setBounds(200, 120, 110, 17);

        jLabel160.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel160.setForeground(new java.awt.Color(0, 153, 204));
        jLabel160.setText("Message:");
        sendEmailPanel.add(jLabel160);
        jLabel160.setBounds(200, 180, 190, 17);

        jLabel161.setForeground(new java.awt.Color(0, 153, 204));
        jLabel161.setText("L'envoi de l'email peu prendre plusieurs secondes");
        sendEmailPanel.add(jLabel161);
        jLabel161.setBounds(310, 610, 490, 16);

        jComboBox5.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox5.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nouvelle Réservation", "Réservation Confirmée", "Réservation Non Confirmée", "Nouveau" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });
        sendEmailPanel.add(jComboBox5);
        jComboBox5.setBounds(340, 70, 360, 27);

        jCheckBox3.setText("Joindre Contrat");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        sendEmailPanel.add(jCheckBox3);
        jCheckBox3.setBounds(210, 480, 480, 23);

        sendEmailPanel3.setBackground(new java.awt.Color(255, 255, 255));
        sendEmailPanel3.setOpaque(true);
        sendEmailPanel3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                sendEmailPanel3ComponentShown(evt);
            }
        });

        endEmailCancelButton1.setBackground(new java.awt.Color(255, 255, 255));
        endEmailCancelButton1.setText("Anuler");
        endEmailCancelButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endEmailCancelButton1ActionPerformed(evt);
            }
        });
        sendEmailPanel3.add(endEmailCancelButton1);
        endEmailCancelButton1.setBounds(530, 560, 140, 30);

        jLabelb144.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb144.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb144.setText("Envoyer Email");
        sendEmailPanel3.add(jLabelb144);
        jLabelb144.setBounds(20, 10, 400, 30);

        endEmailOkButton1.setBackground(new java.awt.Color(255, 255, 255));
        endEmailOkButton1.setText("Valider");
        endEmailOkButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endEmailOkButton1ActionPerformed(evt);
            }
        });
        sendEmailPanel3.add(endEmailOkButton1);
        endEmailOkButton1.setBounds(300, 560, 140, 30);

        endEmailObjectField1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        endEmailObjectField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endEmailObjectField1ActionPerformed(evt);
            }
        });
        sendEmailPanel3.add(endEmailObjectField1);
        endEmailObjectField1.setBounds(200, 140, 610, 30);

        endEmailTextField1.setColumns(20);
        endEmailTextField1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        endEmailTextField1.setLineWrap(true);
        endEmailTextField1.setRows(5);
        endEmailTextField1.setWrapStyleWord(true);
        jScrollPane9.setViewportView(endEmailTextField1);

        sendEmailPanel3.add(jScrollPane9);
        jScrollPane9.setBounds(200, 210, 620, 260);

        jLabel177.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel177.setForeground(new java.awt.Color(0, 153, 204));
        jLabel177.setText("Objet");
        sendEmailPanel3.add(jLabel177);
        jLabel177.setBounds(200, 120, 110, 17);

        jLabel186.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel186.setForeground(new java.awt.Color(0, 153, 204));
        jLabel186.setText("Message:");
        sendEmailPanel3.add(jLabel186);
        jLabel186.setBounds(200, 180, 190, 17);

        jLabel201.setForeground(new java.awt.Color(0, 153, 204));
        jLabel201.setText("L'envoi de l'email peu prendre plusieurs secondes");
        sendEmailPanel3.add(jLabel201);
        jLabel201.setBounds(310, 610, 490, 16);

        linkBillBox1.setText("Joindre Facture");
        linkBillBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkBillBox1ActionPerformed(evt);
            }
        });
        sendEmailPanel3.add(linkBillBox1);
        linkBillBox1.setBounds(200, 500, 150, 23);

        linkContractBox.setText("Joindre Contrat");
        linkContractBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkContractBoxActionPerformed(evt);
            }
        });
        sendEmailPanel3.add(linkContractBox);
        linkContractBox.setBounds(200, 470, 150, 23);

        sendEmailPanel2.setBackground(new java.awt.Color(255, 255, 255));
        sendEmailPanel2.setOpaque(true);
        sendEmailPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                sendEmailPanel2ComponentShown(evt);
            }
        });

        endEmailCancelButton.setBackground(new java.awt.Color(255, 255, 255));
        endEmailCancelButton.setText("Anuler");
        endEmailCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endEmailCancelButtonActionPerformed(evt);
            }
        });
        sendEmailPanel2.add(endEmailCancelButton);
        endEmailCancelButton.setBounds(530, 560, 140, 30);

        jLabelb143.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb143.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb143.setText("Envoyer Email");
        sendEmailPanel2.add(jLabelb143);
        jLabelb143.setBounds(20, 10, 400, 30);

        endEmailOkButton.setBackground(new java.awt.Color(255, 255, 255));
        endEmailOkButton.setText("Valider");
        endEmailOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endEmailOkButtonActionPerformed(evt);
            }
        });
        sendEmailPanel2.add(endEmailOkButton);
        endEmailOkButton.setBounds(300, 560, 140, 30);

        endEmailObjectField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        endEmailObjectField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endEmailObjectFieldActionPerformed(evt);
            }
        });
        sendEmailPanel2.add(endEmailObjectField);
        endEmailObjectField.setBounds(200, 140, 610, 30);

        endEmailTextField.setColumns(20);
        endEmailTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        endEmailTextField.setLineWrap(true);
        endEmailTextField.setRows(5);
        endEmailTextField.setWrapStyleWord(true);
        jScrollPane5.setViewportView(endEmailTextField);

        sendEmailPanel2.add(jScrollPane5);
        jScrollPane5.setBounds(200, 210, 620, 260);

        jLabel174.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel174.setForeground(new java.awt.Color(0, 153, 204));
        jLabel174.setText("Objet");
        sendEmailPanel2.add(jLabel174);
        jLabel174.setBounds(200, 120, 110, 17);

        jLabel175.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel175.setForeground(new java.awt.Color(0, 153, 204));
        jLabel175.setText("Message:");
        sendEmailPanel2.add(jLabel175);
        jLabel175.setBounds(200, 180, 190, 17);

        jLabel176.setForeground(new java.awt.Color(0, 153, 204));
        jLabel176.setText("L'envoi d' email peu prendre plusieurs secondes.");
        sendEmailPanel2.add(jLabel176);
        jLabel176.setBounds(310, 610, 490, 16);

        linkBillBox.setText("Joindre Facture");
        linkBillBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkBillBoxActionPerformed(evt);
            }
        });
        sendEmailPanel2.add(linkBillBox);
        linkBillBox.setBounds(200, 490, 480, 23);

        bookingOption2.setBackground(new java.awt.Color(255, 255, 255));
        bookingOption2.setMinimumSize(new java.awt.Dimension(545, 375));
        bookingOption2.setOpaque(true);
        bookingOption2.setPreferredSize(new java.awt.Dimension(0, 0));
        bookingOption2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                bookingOption2ComponentShown(evt);
            }
        });

        jTableb8.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 13)); // NOI18N
        jTableb8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nom", "Prix Jour", "Montant", "Ajouter"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableb8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableb8MouseClicked(evt);
            }
        });
        jTableb8.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTableb8ComponentShown(evt);
            }
        });
        jScrollPaneb10.setViewportView(jTableb8);

        bookingOption2.add(jScrollPaneb10);
        jScrollPaneb10.setBounds(330, 180, 440, 230);

        jLabelb140.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb140.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb140.setText("Gérer Options");
        bookingOption2.add(jLabelb140);
        jLabelb140.setBounds(20, 20, 400, 30);

        jButtonb29.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb29.setText("OK");
        jButtonb29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb29ActionPerformed(evt);
            }
        });
        bookingOption2.add(jButtonb29);
        jButtonb29.setBounds(630, 430, 140, 30);

        bookingOption.setBackground(new java.awt.Color(255, 255, 255));
        bookingOption.setOpaque(true);
        bookingOption.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                bookingOptionComponentShown(evt);
            }
        });

        jTableb7.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 13)); // NOI18N
        jTableb7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Description", "Montant", "Ajouter"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableb7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableb7MouseClicked(evt);
            }
        });
        jTableb7.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTableb7ComponentShown(evt);
            }
        });
        jScrollPaneb9.setViewportView(jTableb7);

        bookingOption.add(jScrollPaneb9);
        jScrollPaneb9.setBounds(370, 180, 440, 230);

        jButtonb23.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb23.setText("Anuler");
        jButtonb23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb23ActionPerformed(evt);
            }
        });
        bookingOption.add(jButtonb23);
        jButtonb23.setBounds(640, 480, 140, 30);

        jLabelb139.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabelb139.setForeground(new java.awt.Color(0, 153, 204));
        jLabelb139.setText("Ajouter Options");
        bookingOption.add(jLabelb139);
        jLabelb139.setBounds(20, 20, 400, 30);

        jButtonb27.setBackground(new java.awt.Color(255, 255, 255));
        jButtonb27.setText("Valider");
        jButtonb27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonb27ActionPerformed(evt);
            }
        });
        bookingOption.add(jButtonb27);
        jButtonb27.setBounds(410, 480, 140, 30);

        statPanel.setBackground(new java.awt.Color(255, 255, 255));
        statPanel.setOpaque(true);
        statPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                statPanelComponentShown(evt);
            }
        });

        jTable4.setAutoCreateRowSorter(true);
        jTable4.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 13)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Année", "Jours Occupés", "Nbre De Personnes", "Nuitées Adultes", "Nuitées Enfants", "Taxe de Séjour", "Montant"
            }
        ));
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jTable4.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTable4ComponentShown(evt);
            }
        });
        jScrollPane6.setViewportView(jTable4);

        statPanel.add(jScrollPane6);
        jScrollPane6.setBounds(20, 130, 1000, 380);

        jButton19.setBackground(new java.awt.Color(255, 255, 255));
        jButton19.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButton19.setText("Mois");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        statPanel.add(jButton19);
        jButton19.setBounds(20, 540, 140, 30);

        jComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "tout voir" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        statPanel.add(jComboBox1);
        jComboBox1.setBounds(20, 80, 290, 30);

        jLabel46.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(102, 102, 102));
        jLabel46.setText("Selectionner Une Propriété");
        statPanel.add(jLabel46);
        jLabel46.setBounds(20, 60, 270, 17);

        jLabel123.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(0, 153, 204));
        jLabel123.setText("Mes Statistiques (Par Année)");
        statPanel.add(jLabel123);
        jLabel123.setBounds(20, 10, 400, 30);

        statMPanel.setBackground(new java.awt.Color(255, 255, 255));
        statMPanel.setOpaque(true);
        statMPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                statMPanelComponentShown(evt);
            }
        });

        jTable5.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 13)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mois", "Jours Occupés", "Nbre De Personnes", "Nuitées Adultes", "Nuitées Enfants", "Taxe de Séjour", "Montant"
            }
        ));
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jTable5.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jTable5ComponentShown(evt);
            }
        });
        jScrollPane7.setViewportView(jTable5);

        statMPanel.add(jScrollPane7);
        jScrollPane7.setBounds(20, 130, 1000, 380);

        jButton20.setBackground(new java.awt.Color(255, 255, 255));
        jButton20.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jButton20.setText("Année");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        statMPanel.add(jButton20);
        jButton20.setBounds(20, 540, 140, 30);

        jComboBox3.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "tout voir" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        statMPanel.add(jComboBox3);
        jComboBox3.setBounds(20, 80, 290, 30);

        jComboBox4.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });
        statMPanel.add(jComboBox4);
        jComboBox4.setBounds(440, 80, 60, 30);

        jLabel12.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Propriété");
        statMPanel.add(jLabel12);
        jLabel12.setBounds(20, 60, 140, 17);

        jLabel45.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(102, 102, 102));
        jLabel45.setText("Année");
        statMPanel.add(jLabel45);
        jLabel45.setBounds(420, 60, 90, 17);

        jLabel122.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(0, 153, 204));
        jLabel122.setText("Mes Statistiques (Par Mois)");
        statMPanel.add(jLabel122);
        jLabel122.setBounds(20, 10, 400, 30);

        jLabel2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 14)); // NOI18N
        jLabel2.setText("20");
        statMPanel.add(jLabel2);
        jLabel2.setBounds(420, 80, 20, 30);

        jLabelb130.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb130.setForeground(new java.awt.Color(255, 255, 255));
        jLabelb130.setText("_______");
        jLabelb130.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabelb130FocusGained(evt);
            }
        });

        jLabelb131.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb131.setForeground(new java.awt.Color(255, 255, 255));
        jLabelb131.setText("______");
        jLabelb131.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabelb131FocusGained(evt);
            }
        });

        jLabelb132.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb132.setForeground(new java.awt.Color(255, 255, 255));
        jLabelb132.setText("_______");
        jLabelb132.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabelb132FocusGained(evt);
            }
        });

        jLabelb113.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb113.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/beds_1.png"))); // NOI18N

        jLabelb104.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb104.setForeground(new java.awt.Color(255, 255, 255));
        jLabelb104.setText("En Cours");
        jLabelb104.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb104MouseClicked(evt);
            }
        });

        jLabelb105.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb105.setForeground(new java.awt.Color(255, 255, 255));
        jLabelb105.setText("Passé");
        jLabelb105.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb105MouseClicked(evt);
            }
        });

        jLabelb106.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb106.setForeground(new java.awt.Color(255, 255, 255));
        jLabelb106.setText("Nouveau");
        jLabelb106.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb106MouseClicked(evt);
            }
        });

        jLabelb135.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb135.setForeground(new java.awt.Color(255, 255, 255));
        jLabelb135.setText("________");
        jLabelb135.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabelb135FocusGained(evt);
            }
        });

        jLabelb129.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabelb129.setForeground(new java.awt.Color(255, 255, 255));
        jLabelb129.setText("Statistiques");
        jLabelb129.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelb129MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout booking11Layout = new javax.swing.GroupLayout(booking11);
        booking11.setLayout(booking11Layout);
        booking11Layout.setHorizontalGroup(
            booking11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(booking11Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(booking11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelb113, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb132, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb106, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb130, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb104, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb105, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb131, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb129, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb135, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(booking11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(bookingOption))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(endBookingPanel))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(emailBookingPanel))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(statPanel))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(previousBookingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(bookingOption2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(sendEmailPanel)
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(sendEmailPanel2))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(allBookingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(sendEmailPanel3))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(newBooking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(bookingExternalPanel))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(statMPanel))
                    .addGroup(booking11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(bookingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        booking11Layout.setVerticalGroup(
            booking11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(booking11Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelb113, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(booking11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelb132, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb106))
                .addGap(30, 30, 30)
                .addGroup(booking11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelb130, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb104, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(booking11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelb105, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb131, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(booking11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelb129, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelb135, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(bookingOption)
            .addComponent(endBookingPanel)
            .addComponent(emailBookingPanel)
            .addComponent(statPanel)
            .addComponent(previousBookingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bookingOption2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(sendEmailPanel)
            .addComponent(sendEmailPanel2)
            .addComponent(allBookingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(booking11Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(sendEmailPanel3))
            .addComponent(newBooking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bookingExternalPanel)
            .addComponent(statMPanel)
            .addComponent(bookingsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(homePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1555, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(booking11, javax.swing.GroupLayout.DEFAULT_SIZE, 1555, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(homePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(booking11, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 36)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 255, 204));
        jLabel14.setText("Gérer Mon Gîte");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelHome, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabelHomeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabelBookingSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(jLabelSignOut)
                .addGap(26, 26, 26)
                .addComponent(jLabelClose)
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelSignOut)
                            .addComponent(jLabelClose))
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelHome)
                                .addComponent(jLabelBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelHomeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelBookingSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(7, 7, 7)))
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabelSignOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSignOutMouseClicked
        backup.create(db);
        excelFile.insertData(db);
        setVisible(false);
        new Simply().setVisible(true);
    }//GEN-LAST:event_jLabelSignOutMouseClicked

    private void jLabelHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelHomeMouseClicked
        initHome();
        labelNotVisible();
        jLabelHomeSelector.setVisible(true);
    }//GEN-LAST:event_jLabelHomeMouseClicked

    private void jLabelBookingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelBookingMouseClicked
        initBooking();
        labelNotVisible();
        jLabelBookingSelector.setVisible(true);
    }//GEN-LAST:event_jLabelBookingMouseClicked

    private void jLabelCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelCloseMouseClicked
        System.out.println("exit app and backup sql");
        backup.create(db);

        excelFile.insertData(db);
        System.exit(0);


    }//GEN-LAST:event_jLabelCloseMouseClicked

    private void jLabelBookingMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelBookingMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelBookingMouseEntered

    private void jLabelHomeSelectorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelHomeSelectorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelHomeSelectorFocusGained

    private void jLabelBookingSelectorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelBookingSelectorFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelBookingSelectorFocusGained

    private void contratTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contratTxtMouseClicked
        new DefaultEditorKit.CutAction();
        new DefaultEditorKit.CopyAction();
        new DefaultEditorKit.PasteAction();
    }//GEN-LAST:event_contratTxtMouseClicked

    private void autoLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel1MouseClicked
        String s = autoLabel1.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel1MouseClicked

    private void autoLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel4MouseClicked
        String s = autoLabel4.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel4MouseClicked

    private void autoLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel8MouseClicked
        String s = autoLabel8.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel8MouseClicked

    private void autoLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel17MouseClicked
        String s = autoLabel17.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel17MouseClicked

    private void autoLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel2MouseClicked
        String s = autoLabel2.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel2MouseClicked

    private void autoLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel3MouseClicked
        String s = autoLabel3.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel3MouseClicked

    private void autoLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel11MouseClicked
        String s = autoLabel11.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel11MouseClicked

    private void autoLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel18MouseClicked
        String s = autoLabel18.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel18MouseClicked

    private void autoLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel12MouseClicked
        String s = autoLabel12.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel12MouseClicked

    private void autoLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel6MouseClicked
        String s = autoLabel6.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel6MouseClicked

    private void autoLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel5MouseClicked
        String s = autoLabel5.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel5MouseClicked

    private void autoLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel13MouseClicked
        String s = autoLabel13.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel13MouseClicked

//save the selected paragraph in "mon espace/ mon contrat" panel   
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        System.out.println("mon espace/ mon contrat : save selected paragraph");
        //get title and text if they are not empty
        String text = "";
        if (!contratTxt.getText().isEmpty()) {
            text = contratTxt.getText();
            text = StringFormatter.simpleClean(text);
        }
        String title = "";
        if (!contratTitle.getText().isEmpty()) {
            title = contratTitle.getText();
            title = StringFormatter.simpleClean(title);
        }
        String query = "";
        int index = jComboBox6.getSelectedIndex();

        try {
            //update the selected text
            if (index == 0) {
                query = "Update documents set text='" + title + "' WHERE name= 'contratInfoTitle'";
                InsertUpdateDelete.setData(db, query, "");

                query = "Update documents set text='" + text + "'  WHERE name= 'contratIntro'";
                InsertUpdateDelete.setData(db, query, "Modification enregistré avec succès");
            }
            if (index == 1) {
                //ResultSet rs1 = select.getData(db, "select * from documents where name= 'contratOwnerTitle'");
                //while (rs1.next()) {
                query = "Update documents set text='" + title + "'  WHERE name= 'contratOwnerTitle'";
                InsertUpdateDelete.setData(db, query, "");

                query = "Update documents set text='" + text + "'  WHERE name= 'contratOwner'";
                InsertUpdateDelete.setData(db, query, "Modification enregistré avec succès");
                // }
                //rs1.close();
            }
            if (index == 2) {

                query = "Update documents set text='" + title + "'  WHERE name= 'contratPropertyTitle'";
                InsertUpdateDelete.setData(db, query, "");

                query = "Update documents set text='" + text + "'  WHERE name= 'contratProperty'";
                InsertUpdateDelete.setData(db, query, "Modification enregistré avec succès");

            }
            if (index == 3) {

                query = "Update documents set text='" + title + "'  WHERE name= 'contratClientTitle'";
                InsertUpdateDelete.setData(db, query, "");

                query = "Update documents set text='" + text + "'  WHERE name= 'contratClient'";
                InsertUpdateDelete.setData(db, query, "Modification enregistré avec succès");

            }
            if (index == 4) {

                query = "Update documents set text='" + title + "'  WHERE name= 'contratBookingTitle' ";
                InsertUpdateDelete.setData(db, query, "");

                query = "Update documents set text='" + text + "'  WHERE name= 'contratBooking'";
                InsertUpdateDelete.setData(db, query, "Modification enregistré avec succès");

            }
            if (index == 5) {

                query = "Update documents set text='" + title + "'  WHERE name= 'contratConditionTitle' ";
                InsertUpdateDelete.setData(db, query, "");

                query = "Update documents set text='" + text + "'  WHERE name= 'contratCondition'";
                InsertUpdateDelete.setData(db, query, "Modification enregistré avec succès");

            }

            if (index == 6) {

                query = "Update documents set text='" + title + "'  WHERE name= 'contratSignatureTitle' ";
                InsertUpdateDelete.setData(db, query, "");

                query = "Update documents set text='" + text + "'  WHERE name= 'contratSignature'";
                InsertUpdateDelete.setData(db, query, "Modification enregistré avec succès");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        //refresh table and keep selected paragraph
        jComboBox6.setSelectedIndex(index);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void autoLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel15MouseClicked
        String s = autoLabel15.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel15MouseClicked

    private void autoLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel16MouseClicked
        String s = autoLabel16.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel16MouseClicked

    private void autoLabelb1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb1MouseClicked
        String element = autoLabelb1.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb1MouseClicked

    private void autoLabelb4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb4MouseClicked
        String element = autoLabelb4.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb4MouseClicked

    private void autoLabelb8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb8MouseClicked
        String element = autoLabelb8.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb8MouseClicked

    private void autoLabelb17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb17MouseClicked
        String element = autoLabelb17.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb17MouseClicked

    private void autoLabelb2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb2MouseClicked
        String element = autoLabelb2.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb2MouseClicked

    private void autoLabelb3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb3MouseClicked
        String element = autoLabelb3.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb3MouseClicked

    private void autoLabelb11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb11MouseClicked
        String element = autoLabelb11.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb11MouseClicked

    private void autoLabelb18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb18MouseClicked
        String element = autoLabelb18.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb18MouseClicked

    private void autoLabelb12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb12MouseClicked
        String element = autoLabelb12.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb12MouseClicked

    private void autoLabelb6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb6MouseClicked
        String element = autoLabelb6.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb6MouseClicked

    private void autoLabelb5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb5MouseClicked
        String element = autoLabelb5.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb5MouseClicked

    private void autoLabelb15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb15MouseClicked
        String element = autoLabelb15.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb15MouseClicked

//save text area in "mon espace/ mes emails"
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        System.out.println("mon espace/ mes emails: save change of the selected email");
        int index = jComboBox2.getSelectedIndex();
        String object = jTextField12.getText();
        object = StringFormatter.simpleClean(object);
        String msge = jTextArea2.getText();
        msge = StringFormatter.simpleClean(msge);
        //update the documents table's row according to the selected email

        if (index == 0) {
            String Query = " UPDATE documents SET bookingType = 'all',property = 'all',object = '" + object + "', text = '" + msge + "' WHERE name= '2'";
            InsertUpdateDelete.setData(db, Query, "Enregistré avec succès");
        }
        if (index == 1) {
            String Query = " UPDATE documents SET bookingType = 'all',property = 'all',object = '" + object + "', text = '" + msge + "' WHERE name= '8'";
            InsertUpdateDelete.setData(db, Query, "Enregistré avec succès");
        }
        if (index == 2) {
            String Query = " UPDATE documents SET bookingType = 'all',property = 'all',object = '" + object + "', text = '" + msge + "' WHERE name= '4'";
            InsertUpdateDelete.setData(db, Query, "Enregistré avec succès");
        }
        if (index == 3) {
            String Query = " UPDATE documents SET bookingType = 'all',property = 'all',object = '" + object + "', text = '" + msge + "' WHERE name= '5'";
            InsertUpdateDelete.setData(db, Query, "Enregistré avec succès");
        }
        if (index == 4) {
            String Query = " UPDATE documents SET bookingType = 'all',property = 'all',object = '" + object + "', text = '" + msge + "' WHERE name= '3'";
            InsertUpdateDelete.setData(db, Query, "Enregistré avec succès");
        }
        if (index == 5) {
            String Query = " UPDATE documents SET bookingType = 'all',property = 'all',object = '" + object + "', text = '" + msge + "' WHERE name= '7'";
            InsertUpdateDelete.setData(db, Query, "Enregistré avec succès");
        }

        homeInitEmailPanel();
        // keep the selected email
        jComboBox2.setSelectedIndex(index);
    }//GEN-LAST:event_jButton6ActionPerformed

//cancel unsaved change in "mon espace/mes email" text Area
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        homeInitEmailPanel();
    }//GEN-LAST:event_jButton10ActionPerformed


    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        System.out.println("mon espace/ mes emails: set selected email");
        //get the selected email and set text fields with the corresponding subject and text stored in sql documents table
        int index = jComboBox2.getSelectedIndex();
        ResultSet rs;
        if (index == 0) {
            //if selected email is "nouvelle reservation"
            rs = select.getData(db, "SELECT * FROM documents WHERE name='2'");
            try {
                while (rs.next()) {
                    jTextField12.setText(rs.getString("object"));
                    jTextArea2.setText(rs.getString("text"));
                }
                rs.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        if (index == 1) {
            //if selected email is "reservation confirmée"
            rs = select.getData(db, "SELECT * FROM documents WHERE name='8'");
            try {
                while (rs.next()) {
                    jTextField12.setText(rs.getString("object"));
                    jTextArea2.setText(rs.getString("text"));
                }
                rs.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
        if (index == 2) {
            //if selected email is "reservation non confirmée"
            rs = select.getData(db, "SELECT * FROM documents WHERE name='4'");
            try {
                while (rs.next()) {
                    jTextField12.setText(rs.getString("object"));
                    jTextArea2.setText(rs.getString("text"));
                }
                rs.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

        if (index == 3) {
            //if selected email is "fin de sejour"
            rs = select.getData(db, "SELECT * FROM documents WHERE name='5'");
            try {
                while (rs.next()) {
                    jTextField12.setText(rs.getString("object"));
                    jTextArea2.setText(rs.getString("text"));
                }
                rs.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        if (index == 4) {
            //if selected email is "fin de sejour"
            rs = select.getData(db, "SELECT * FROM documents WHERE name='3'");
            try {
                while (rs.next()) {
                    jTextField12.setText(rs.getString("object"));
                    jTextArea2.setText(rs.getString("text"));
                }
                rs.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        if (index == 5) {
            //if selected email is "fin de sejour"
            rs = select.getData(db, "SELECT * FROM documents WHERE name='7'");
            try {
                while (rs.next()) {
                    jTextField12.setText(rs.getString("object"));
                    jTextArea2.setText(rs.getString("text"));
                }
                rs.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void autoLabelb13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb13MouseClicked
        String s = autoLabelb13.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb13MouseClicked

    private void autoLabelb14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb14MouseClicked
        String s = autoLabelb14.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb14MouseClicked

    private void myCompanyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myCompanyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myCompanyFieldActionPerformed

    private void myStreet1FieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myStreet1FieldFocusLost

    }//GEN-LAST:event_myStreet1FieldFocusLost

    private void myPostCodeFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myPostCodeFieldFocusLost

    }//GEN-LAST:event_myPostCodeFieldFocusLost

    private void myCityFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myCityFieldFocusLost

    }//GEN-LAST:event_myCityFieldFocusLost

    private void myStreet2FieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myStreet2FieldFocusLost

    }//GEN-LAST:event_myStreet2FieldFocusLost

    private void myPhoneFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myPhoneFieldFocusLost

    }//GEN-LAST:event_myPhoneFieldFocusLost

    private void myEmailFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myEmailFieldFocusLost

    }//GEN-LAST:event_myEmailFieldFocusLost

    private void instaFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_instaFieldFocusLost

    }//GEN-LAST:event_instaFieldFocusLost

//save all changes made by the user in "mon espace/ mes infos" panel  
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        System.out.println("mon espace/mes infos: save change");
        //search if the user informations are alredy stored (if id 1 exist) in myInfo table (0= not exist; 1= is exist)
        int check = 0;
        //check if company, name, firstname, tel, email, street, cp, and city are given
        if (myCompanyField.getText().isBlank() || myNameField.getText().isBlank() || myFirstNameField.getText().isBlank() || myStreet1Field.getText().isBlank() || myPostCodeField.getText().isBlank() || myCityField.getText().isBlank() || myPhoneField.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Aïe... Informations manquantes");
        } else {
            owner o = new owner();
            o.setCompany(StringFormatter.clean(myCompanyField.getText()));
            o.setName(StringFormatter.clean(myNameField.getText()));
            o.setFirstName(StringFormatter.clean(myFirstNameField.getText()));
            o.setStreet1(StringFormatter.clean(myStreet1Field.getText()));
            o.setPostCode(StringFormatter.simpleClean(myPostCodeField.getText()));
            o.setCity(StringFormatter.clean(myCityField.getText()));
            o.setTel1(StringFormatter.simpleClean(myPhoneField.getText()));
            //non mandatory data( check first if the data were given by the user) and if yes initialize the corresponding variable
            //then insert or update myInfo table 

            if (!myStreet2Field.getText().isEmpty()) {
                o.setStreet2(StringFormatter.clean(myStreet2Field.getText()));
            } else {
                o.setStreet2("");
            }
            if (!myDcField.getText().isEmpty()) {
                o.setDc(StringFormatter.clean(myDcField.getText()));
            } else {
                o.setDc("");
            }
            if (!myPhone2Field.getText().isEmpty()) {
                o.setTel2(StringFormatter.clean(myPhone2Field.getText()));
            } else {
                o.setTel2("");
            }
            if (!myEmailField.getText().isEmpty()) {
                o.setEmail(StringFormatter.simpleClean(myEmailField.getText()));
            }
            if (!jPasswordField1.getText().isEmpty()) {
                o.setEmailPassword(StringFormatter.simpleClean(jPasswordField1.getText()));
            }
            if(jPasswordField1.getText().isEmpty()){
                 o.setEmailPassword(jPasswordField1.getText());
            }

            if (!mySiretField.getText().isEmpty()) {
                o.setSiret(StringFormatter.simpleClean(mySiretField.getText()));
            } else {
                o.setSiret("");
            }
            if (!myApeField.getText().isEmpty()) {
                o.setApe(StringFormatter.simpleClean(myApeField.getText()));
            } else {
                o.setApe("");
            }
            if (!websiteField.getText().isEmpty()) {
                o.setWebsite(StringFormatter.simpleClean(websiteField.getText()));
            } else {
                o.setWebsite("");
            }
            if (!facebookField.getText().isEmpty()) {
                o.setFacebook(StringFormatter.simpleClean(facebookField.getText()));
            } else {
                o.setFacebook("");
            }
            if (!instaField.getText().isEmpty()) {
                o.setInsta(StringFormatter.simpleClean(instaField.getText()));
            } else {
                o.setInsta("");
            }
            if (!googletField.getText().isEmpty()) {
                o.setGoogle(StringFormatter.simpleClean(googletField.getText()));
            } else {
                o.setGoogle("");
            }
            if (!tripAdvisorField.getText().isEmpty()) {
                o.setTripAdvisor(StringFormatter.simpleClean(tripAdvisorField.getText()));
            } else {
                o.setTripAdvisor("");
            }
            if (!otherReviewField.getText().isEmpty()) {
                o.setOtherReview(StringFormatter.simpleClean(otherReviewField.getText()));
            } else {
                o.setOtherReview("");
            }
            o.saveOwner(db);
            document.createGeneralCondtions(db);
            homeInitMyInfo();

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void mySiretFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mySiretFieldFocusLost

    }//GEN-LAST:event_mySiretFieldFocusLost

    private void websiteFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_websiteFieldFocusLost

    }//GEN-LAST:event_websiteFieldFocusLost

    private void facebookFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_facebookFieldFocusLost

    }//GEN-LAST:event_facebookFieldFocusLost

    private void tripAdvisorFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tripAdvisorFieldFocusLost

    }//GEN-LAST:event_tripAdvisorFieldFocusLost

    private void otherReviewFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_otherReviewFieldFocusLost

    }//GEN-LAST:event_otherReviewFieldFocusLost

    private void googletFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_googletFieldFocusLost

    }//GEN-LAST:event_googletFieldFocusLost


    private void selectTypeBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectTypeBoxMouseClicked

    }//GEN-LAST:event_selectTypeBoxMouseClicked

    //mon espace/ mes locations: select property type and set coresponding panel and fields 
    private void selectTypeBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectTypeBoxActionPerformed

        String type = (String) selectTypeBox.getSelectedItem();

        if (type.equals("Maison")) {
            System.out.println("mon espace/ mes locations : select Maison");
            homeSubPanelNotVisible();

            if (!housePanel.isVisible()) {
                housePanel.setVisible(true);
                addHouseButton.setVisible(false);
            }
            qtyRoomHouseLabel.setVisible(true);
            qtyRoomHouseTextField.setVisible(true);
            enterHouseButton.setVisible(true);
            amountHouseLabel.setVisible(false);
            amountHouseTextField.setVisible(false);
            AdressHouseTextField.setVisible(false);
            AdressHouseLabel.setVisible(false);
            nameHouseTextField.requestFocus();
            boundHouseTextField.setVisible(false);
            boundHouseLabel.setVisible(false);
            boundHouseLabel1.setVisible(false);
            taxHouseTextField.setVisible(false);
            AdressHouseTextField.setText("");
            taxHouseTextField.setText("");
        }

        if (type.equals("Chambre")) {
            System.out.println("mon espace/ mes location : select chambre");
            homeSubPanelNotVisible();
            roomPanel.setVisible(true);
            buildingTextField.requestFocus();
        }

        if (type.equals("Dortoir")) {
            System.out.println("mon espace/ mes location : select dortoir");
            homeSubPanelNotVisible();
            dormPanel.setVisible(true);
            typeDormTextField.requestFocus();
        }
    }//GEN-LAST:event_selectTypeBoxActionPerformed

    private void nameRoomTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameRoomTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameRoomTextFieldActionPerformed

    private void singleBedRoomTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_singleBedRoomTextFieldFocusGained
        singleBedRoomTextField.selectAll();
    }//GEN-LAST:event_singleBedRoomTextFieldFocusGained

    private void singleBedRoomTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_singleBedRoomTextFieldMouseClicked
        singleBedRoomTextField.selectAll();
    }//GEN-LAST:event_singleBedRoomTextFieldMouseClicked

    private void singleBedRoomTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleBedRoomTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_singleBedRoomTextFieldActionPerformed

    private void doubleBedRoomTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doubleBedRoomTextFieldFocusGained
        doubleBedRoomTextField.selectAll();
    }//GEN-LAST:event_doubleBedRoomTextFieldFocusGained

    private void doubleBedRoomTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doubleBedRoomTextFieldMouseClicked
        doubleBedRoomTextField.selectAll();
    }//GEN-LAST:event_doubleBedRoomTextFieldMouseClicked

    private void doubleBedRoomTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doubleBedRoomTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_doubleBedRoomTextFieldActionPerformed

    private void amountRoomTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountRoomTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addRoomButton.doClick();
        }
    }//GEN-LAST:event_amountRoomTextFieldKeyPressed

    //mon espace/ mes location: add a room property in my database
    private void addRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRoomButtonActionPerformed
        System.out.println("mon espace/ mes location: add room");
        //make sure that all fields are filled by the user
        if (buildingTextField.getText().isEmpty() || adressRoomTextField.getText().isEmpty() || nameRoomTextField.getText().isEmpty() || singleBedRoomTextField.getText().isEmpty() || doubleBedRoomTextField.getText().isEmpty() || amountRoomTextField.getText().isEmpty() || taxRoomTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aïe, Toutes les informations sont nécéssaire.");
        } else {
            String type = (String) selectTypeBox.getSelectedItem();
            String building = buildingTextField.getText();
            building = StringFormatter.clean(building);
            String adress = StringFormatter.clean(adressRoomTextField.getText());
            String room = nameRoomTextField.getText();
            room = StringFormatter.clean(room);
            String singleBed = singleBedRoomTextField.getText();
            String doubleBed = doubleBedRoomTextField.getText();
            String price = amountRoomTextField.getText();
            String tax = taxRoomTextField.getText().replaceAll(",", ".");
            //generate a new and unic property ID
            int ID = homeNewID();
            if (singleBed.equals("") || doubleBed.equals("") || building.equals("") || room.equals("") || type.equals("") || price.equals("") || adress.equals("") || tax.equals("")) {
                JOptionPane.showMessageDialog(null, "Merci de renseigner toutes les informations");
            } else {
                //save room in sql room table
                String Query = "insert into Room values('" + ID + "','" + building + "','" + type + "','" + room + "','-','" + singleBed + "','" + doubleBed + "','" + price + "','-','" + tax + "','true','" + adress + "')";
                InsertUpdateDelete.setData(db, Query, "Félicitation! Nouvelle Chambre Enregistré :) ");
                homeResetMyrentalTable();
            }
        }
    }//GEN-LAST:event_addRoomButtonActionPerformed

    //set all room field to null
    private void cancelRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelRoomButtonActionPerformed
        nameRoomTextField.setText("");
        amountRoomTextField.setText("");
        buildingTextField.setText("");
        singleBedRoomTextField.setText("0");
        doubleBedRoomTextField.setText("0");
    }//GEN-LAST:event_cancelRoomButtonActionPerformed

    private void qtyRoomHouseTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_qtyRoomHouseTextFieldFocusGained
        qtyRoomHouseTextField.selectAll();
    }//GEN-LAST:event_qtyRoomHouseTextFieldFocusGained

    private void qtyRoomHouseTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtyRoomHouseTextFieldMouseClicked
        qtyRoomHouseTextField.selectAll();
    }//GEN-LAST:event_qtyRoomHouseTextFieldMouseClicked

    private void qtyRoomHouseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qtyRoomHouseTextFieldActionPerformed

    }//GEN-LAST:event_qtyRoomHouseTextFieldActionPerformed

    //mon espace/ mes locations: get the number of room in the current property and set first room in the next panel
    private void qtyRoomHouseTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtyRoomHouseTextFieldKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            rooms = 0;
            currentRoom = 0;
            rooms = Integer.parseInt(qtyRoomHouseTextField.getText());

            if (rooms >= 1) {
                currentRoom = 1;
                roomHousePanel.setVisible(true);
                housePanel.setVisible(false);
                nameRoomHouseTextField.setText("Chambre " + currentRoom);
                nameRoomHouseTextField.requestFocus();
                nameRoomHouseTextField.selectAll();

            } else {
                JOptionPane.showMessageDialog(null, "Oups, Aucune chambre indiqué");
            }
        }
    }//GEN-LAST:event_qtyRoomHouseTextFieldKeyPressed

    private void qtyRoomHouseTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtyRoomHouseTextFieldKeyTyped

    }//GEN-LAST:event_qtyRoomHouseTextFieldKeyTyped

    private void nameHouseTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameHouseTextFieldFocusGained
        nameRoomHouseTextField.selectAll();
    }//GEN-LAST:event_nameHouseTextFieldFocusGained

    private void nameHouseTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameHouseTextFieldMouseClicked
        nameRoomHouseTextField.selectAll();
    }//GEN-LAST:event_nameHouseTextFieldMouseClicked

    //mon espace/ mes locations: save new house in sql room table
    private void addHouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addHouseButtonActionPerformed
        System.out.println("mon espace/ mes location: add house");
        String type = (String) selectTypeBox.getSelectedItem();
        String building = nameHouseTextField.getText();
        building = StringFormatter.clean(building);
        String roomQte = qtyRoomHouseTextField.getText();
        String price = amountHouseTextField.getText();
        String adress = StringFormatter.clean(AdressHouseTextField.getText());
        String bound = boundHouseTextField.getText();
        String tax = taxHouseTextField.getText();
        //get a new & unic property ID
        int ID = homeNewID();
        if (building.equals("") || roomQte.equals("") || type.equals("") || price.equals("") || adress.equals("") || bound.equals("") || tax.equals("")) {
            JOptionPane.showMessageDialog(null, "Merci de renseigner toutes les informations");
        } else {
            String Query = "insert into Room values('" + ID + "','" + building + "','" + type + "',' - ','" + roomQte + "','" + singleBedHouse + "','" + doubleBedHouse + "','" + price + "','" + bound + "','" + tax + "','true','" + adress + "')";
            InsertUpdateDelete.setData(db, Query, "Félicitation! Nouvelle maison enregistrée :) ");
            //check (in the map stored room) if the property also has room to save in "chambre"
            homeResetMyrentalTable();
            amountHouseLabel.setVisible(false);
            amountHouseTextField.setVisible(false);
            roomStoredLabel.setVisible(false);
            singleBedStoredLabel.setVisible(false);
            doubleBedStoredLabel.setVisible(false);

            qtyRoomHouseLabel.setVisible(true);
            qtyRoomHouseTextField.setVisible(true);
            enterHouseButton.setVisible(true);
            addHouseButton.setVisible(false);
            nameHouseTextField.requestFocus();
            singleBedHouse = 0;
            doubleBedHouse = 0;
            storedRooms.clear();
        }
        selectTypeBox.setSelectedIndex(1);
    }//GEN-LAST:event_addHouseButtonActionPerformed

    //mon espace/ mes location/ maison: clear all field.
    private void cancelHouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelHouseButtonActionPerformed
        storedRooms.clear();
        singleBedHouse = 0;
        doubleBedHouse = 0;
        nameHouseTextField.setText("");
        qtyRoomHouseTextField.setText("0");
        singleBedRoomHouseTextField.setText("0");
        doublebedRoomHouseTextField.setText("0");
        AdressHouseTextField.setText("");
        taxHouseTextField.setText("");
        amountHouseTextField.setText("");
        boundHouseTextField.setText("");

        if (!qtyRoomHouseLabel.isVisible()) {
            qtyRoomHouseLabel.setVisible(true);
            qtyRoomHouseTextField.setVisible(true);
            enterHouseButton.setVisible(true);
        }
        selectTypeBox.setSelectedIndex(1);
    }//GEN-LAST:event_cancelHouseButtonActionPerformed

    //"mon espace/ mes locations/ maison: set and open next panel
    private void enterHouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterHouseButtonActionPerformed

        rooms = 0;
        currentRoom = 0;
        rooms = Integer.parseInt(qtyRoomHouseTextField.getText());

        if (rooms >= 1 && !nameHouseTextField.getText().isEmpty()) {
            currentRoom = 1;
            roomHousePanel.setVisible(true);
            housePanel.setVisible(false);
            nameRoomHouseTextField.setText("Chambre " + currentRoom);
            nameRoomHouseTextField.requestFocus();
            nameRoomHouseTextField.selectAll();

        } else {
            JOptionPane.showMessageDialog(null, "Oups, Aucune chambre ou propriété indiquée");
        }
    }//GEN-LAST:event_enterHouseButtonActionPerformed

    private void amountHouseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountHouseTextFieldActionPerformed
        addHouseButton.requestFocus();
    }//GEN-LAST:event_amountHouseTextFieldActionPerformed

    private void amountHouseTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountHouseTextFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            addHouseButton.doClick();
        }
    }//GEN-LAST:event_amountHouseTextFieldKeyPressed

    private void boundHouseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boundHouseTextFieldActionPerformed

    }//GEN-LAST:event_boundHouseTextFieldActionPerformed

    private void singleBedRoomHouseTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_singleBedRoomHouseTextFieldFocusGained
        singleBedRoomHouseTextField.selectAll();
    }//GEN-LAST:event_singleBedRoomHouseTextFieldFocusGained

    private void singleBedRoomHouseTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_singleBedRoomHouseTextFieldMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_singleBedRoomHouseTextFieldMouseClicked

    private void singleBedRoomHouseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleBedRoomHouseTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_singleBedRoomHouseTextFieldActionPerformed

    private void doublebedRoomHouseTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doublebedRoomHouseTextFieldFocusGained
        doublebedRoomHouseTextField.selectAll();
    }//GEN-LAST:event_doublebedRoomHouseTextFieldFocusGained

    private void doublebedRoomHouseTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doublebedRoomHouseTextFieldMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_doublebedRoomHouseTextFieldMouseClicked

    private void doublebedRoomHouseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doublebedRoomHouseTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_doublebedRoomHouseTextFieldActionPerformed

    private void saveRoomHouseButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveRoomHouseButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_saveRoomHouseButtonMouseClicked

    //mon espace/ mes location/ maison: store all rooms infomations in a the map storedRooms
    private void saveRoomHouseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveRoomHouseButtonActionPerformed
        // check if it is the last room to save
        if (rooms == currentRoom) {
            //if yes store the room info in stored room and go to next panel and provide a recap of the saved information.
            ArrayList<String> list = new ArrayList<String>();
            list.add(singleBedRoomHouseTextField.getText());
            list.add(doublebedRoomHouseTextField.getText());

            String roomName = nameRoomHouseTextField.getText();
            roomName = StringFormatter.clean(roomName);
            storedRooms.put(roomName, list);
            roomHousePanel.setVisible(false);
            housePanel.setVisible(true);
            enterHouseButton.setVisible(false);
            addHouseButton.setVisible(true);
            qtyRoomHouseTextField.setVisible(false);
            qtyRoomHouseLabel.setVisible(false);
            int roomStored = storedRooms.size();

            for (String s : storedRooms.keySet()) {
                singleBedHouse += Integer.parseInt(storedRooms.get(s).get(0));
                doubleBedHouse += Integer.parseInt(storedRooms.get(s).get(1));
            }
            roomStoredLabel.setText("- " + roomStored + " Chambre(s) : ");
            singleBedStoredLabel.setText("- " + singleBedHouse + " lit simple.");
            doubleBedStoredLabel.setText("- " + doubleBedHouse + " lit double.");
            amountHouseLabel.setVisible(true);
            amountHouseTextField.setVisible(true);
            AdressHouseTextField.setVisible(true);
            AdressHouseLabel.setVisible(true);
            AdressHouseTextField.requestFocus();
            boundHouseTextField.setVisible(true);
            boundHouseLabel.setVisible(true);
            boundHouseLabel1.setVisible(true);
            taxHouseTextField.setVisible(true);

        } else {
            // if it is not the last room, add 1 to curr room, store the current info in the map and display same panel
            currentRoom += 1;
            ArrayList<String> list = new ArrayList<String>();
            list.add(singleBedRoomHouseTextField.getText());
            list.add(doublebedRoomHouseTextField.getText());

            String roomName = nameRoomHouseTextField.getText();
            roomName = StringFormatter.clean(roomName);
            storedRooms.put(roomName, list);
            nameRoomHouseTextField.setText("Chambre " + currentRoom);
            singleBedRoomHouseTextField.setText("0");
            doublebedRoomHouseTextField.setText("0");
            nameRoomHouseTextField.requestFocus();
            
        }
    }//GEN-LAST:event_saveRoomHouseButtonActionPerformed

    private void nameDormTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameDormTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameDormTextFieldActionPerformed

    private void singleBedDormTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_singleBedDormTextFieldFocusGained
        singleBedDormTextField.selectAll();
    }//GEN-LAST:event_singleBedDormTextFieldFocusGained

    private void singleBedDormTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_singleBedDormTextFieldMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_singleBedDormTextFieldMouseClicked

    private void singleBedDormTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleBedDormTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_singleBedDormTextFieldActionPerformed

    private void doubleBedDormTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doubleBedDormTextFieldFocusGained
        doubleBedDormTextField.selectAll();
    }//GEN-LAST:event_doubleBedDormTextFieldFocusGained

    private void doubleBedDormTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doubleBedDormTextFieldMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_doubleBedDormTextFieldMouseClicked

    private void doubleBedDormTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doubleBedDormTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_doubleBedDormTextFieldActionPerformed

    private void amountDormTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountDormTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountDormTextFieldKeyPressed

    //mon espace/ mes location/ dortoir: save dortoir in sql room table
    private void addDormButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDormButtonActionPerformed
        System.out.println("mon espace/ mes location/ dortoir: save dortoir");
        if (typeDormTextField.getText().isEmpty() || adressDormTextField.getText().isEmpty() || nameDormTextField.getText().isEmpty() || singleBedDormTextField.getText().isEmpty() || doubleBedDormTextField.getText().isEmpty() || amountDormTextField.getText().isEmpty() || taxDormTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aïe, Toutes les informations sont nécéssaires.");
        } else {
            String type = (String) selectTypeBox.getSelectedItem();
            String building = typeDormTextField.getText();
            building = StringFormatter.clean(building);
            String adress = StringFormatter.clean(adressDormTextField.getText());
            String room = nameDormTextField.getText();
            room = StringFormatter.clean(room);
            String singleBed = singleBedDormTextField.getText();
            String doubleBed = doubleBedDormTextField.getText();
            String price = amountDormTextField.getText();
            String tax = taxDormTextField.getText();
            int bedQte = Integer.parseInt(singleBed) + Integer.parseInt(doubleBed);
            int ID = homeNewID();
            if (singleBed.equals("") || doubleBed.equals("") || building.equals("") || room.equals("") || type.equals("") || price.equals("") || adress.equals("") || tax.equals("")) {
                JOptionPane.showMessageDialog(null, "Merci de renseigner toutes les informations");
            } else {
                String Query = "insert into Room values('" + ID + "','" + building + "','" + type + "','" + room + "','" + bedQte + "','" + singleBed + "','" + doubleBed + "','" + price + "', '-','" + tax + "','true','" + adress + "')";
                InsertUpdateDelete.setData(db, Query, "Félicitation! Nouveau Dortoir Enregistré :) ");
                homeResetMyrentalTable();
            }
        }
    }//GEN-LAST:event_addDormButtonActionPerformed

    //mon espace/ mes location/ dortoir: clear dortoir
    private void cancelDormButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDormButtonActionPerformed
        typeDormTextField.setText("");
        nameDormTextField.setText("");
        amountDormTextField.setText("");
        singleBedDormTextField.setText("0");
        doubleBedDormTextField.setText("0");
    }//GEN-LAST:event_cancelDormButtonActionPerformed

    private void cancelMyrentalsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelMyrentalsButtonActionPerformed
        homeResetMyrentalTable();
    }//GEN-LAST:event_cancelMyrentalsButtonActionPerformed

    ////mon espace/ mes location: delete all selected properties in the jTable
    private void deleteMyrentalsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMyrentalsButtonActionPerformed
        int index[] = myRentalsTable.getSelectedRows();

        int a = JOptionPane.showConfirmDialog(null, "SUPRIMER, Est-Vous Sûre ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            for (int i = 0; i < index.length; i++) {
                String ID = myRentalsTable.getValueAt(index[i], 0).toString();
                String Query = "DELETE FROM room WHERE ID=" + ID;
                InsertUpdateDelete.setData(db, Query, "");
            }
        }
        homeResetMyrentalTable();

    }//GEN-LAST:event_deleteMyrentalsButtonActionPerformed

    private void myRentalsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myRentalsTableMouseClicked

    }//GEN-LAST:event_myRentalsTableMouseClicked

    private void myRentalsTableComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_myRentalsTableComponentShown

    }//GEN-LAST:event_myRentalsTableComponentShown

    //mon espace/ mes location/ update sql roomtable properties information from jTable
    private void modifyMyrentalsTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyMyrentalsTableActionPerformed

        int a = JOptionPane.showConfirmDialog(null, "Modifier, Est-vous sûre ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            for (int i = 0; i < myRentalsTable.getRowCount(); i++) {
                String ID = myRentalsTable.getValueAt(i, 0).toString();
                String building = myRentalsTable.getValueAt(i, 1).toString();
                building = StringFormatter.clean(building);
                String type = myRentalsTable.getValueAt(i, 2).toString();
                String roomName = myRentalsTable.getValueAt(i, 3).toString();
                roomName = StringFormatter.clean(roomName);
                String singleBed = myRentalsTable.getValueAt(i, 4).toString();
                String doubleBed = myRentalsTable.getValueAt(i, 5).toString();
                String price = myRentalsTable.getValueAt(i, 6).toString();
                String bound = myRentalsTable.getValueAt(i, 7).toString();
                String tax = myRentalsTable.getValueAt(i, 8).toString();
                String adress = myRentalsTable.getValueAt(i, 9).toString();
                adress = StringFormatter.clean(adress);

                if (type.equals("Maison")) {

                    String Query = " UPDATE Room set building = '" + building + "',singleBed = '" + singleBed + "',doubleBed = '" + doubleBed + "',price = '" + price + "',bound= '" + bound + "', tax= '" + tax + "', adress='" + adress + "' WHERE ID= " + ID;
                    InsertUpdateDelete.setData(db, Query, "");
                }

                if (type.equals("Chambre")) {
                    String Query = " UPDATE Room set building = '" + building + "',roomName = '" + roomName + "',singleBed = '" + singleBed + "',doubleBed = '" + doubleBed + "',price = '" + price + "',bound= '" + bound + "', tax= '" + tax + "', adress='" + adress + "' WHERE ID= " + ID;
                    InsertUpdateDelete.setData(db, Query, "");
                }
                if (type.equals("Dortoir")) {
                    String Query = " UPDATE Room set building = '" + building + "',roomName = '" + roomName + "', singleBed = '" + singleBed + "',doubleBed = '" + doubleBed + "',price = '" + price + "',bound= '" + bound + "', tax= '" + tax + "', adress='" + adress + "' WHERE ID= " + ID;
                    InsertUpdateDelete.setData(db, Query, "");
                }
            }
        }

        homeResetMyrentalTable();
    }//GEN-LAST:event_modifyMyrentalsTableActionPerformed

    private void jLabel121MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel121MouseEntered
        helpPanel.setVisible(true);
    }//GEN-LAST:event_jLabel121MouseEntered

    private void jLabel121MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel121MouseExited
        helpPanel.setVisible(false);
    }//GEN-LAST:event_jLabel121MouseExited

    private void myRentalPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_myRentalPanelComponentShown
        homeResetMyrentalTable();
        myRentalsTable.getRowSorter().toggleSortOrder(2);
    }//GEN-LAST:event_myRentalPanelComponentShown

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable5MouseClicked

    private void jTable5ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable5ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable5ComponentShown

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        homePanelsNotVisible();
        statMPanel.setVisible(false);
        statPanel.setVisible(true);
        jComboBox1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton20ActionPerformed

    //"mon espace/ statistiques:  set the statistic table according to the selected booking
    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        homeInitMonthStats();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    //"mon espace/ statistiques":  set the statistic table according to the selected year
    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        homeInitMonthStats();
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void statMPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_statMPanelComponentShown
        jScrollPane7.getViewport().setBackground(Color.white);
    }//GEN-LAST:event_statMPanelComponentShown

    private void jTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable6MouseClicked

    private void jTable6ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable6ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable6ComponentShown

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        homeResetOption();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    //mon espace/ mes options: save new option.
    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        //get new option information
        String name = jTextField10.getText();
        String amount = jTextField11.getText();
        String rate = "";
        //look if the option is a daily rate or a fix rate
        if (jCheckBox1.isSelected()) {
            rate = "true";
        } else {
            rate = "false";
        }
        //check if all information are filled
        if (name.isEmpty() || amount.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Merci de renseigner toutes les informations");
        } else {
            //if yes, insert option into sql myOption table
            name = StringFormatter.clean(name);
            String Query = "insert into myOption (name, amount,dailyRate) values('" + name + "','" + amount + "','" + rate + "')";
            InsertUpdateDelete.setData(db, Query, "");
        }
        homeInitOption();
    }//GEN-LAST:event_jButton22ActionPerformed

    //mon espaces/ mes options: delete all selected option form sql myOption table
    private void jLabel138MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel138MouseClicked
        int index[] = jTable6.getSelectedRows();
        int a = JOptionPane.showConfirmDialog(null, "Supprimer options, Est-Vous Sûre ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            for (int i = 0; i < index.length; i++) {
                int ID = (int) jTable6.getValueAt(index[i], 0);
                String Query = "DELETE FROM myOption WHERE ID='" + ID + "'";
                InsertUpdateDelete.setData(db, Query, "");
                String query2 = "DELETE FROM bookingOption WHERE optionID='" + ID + "' AND selected= 'false'";
                InsertUpdateDelete.setData(db, query2, "");
            }
        }
        homeInitOption();
    }//GEN-LAST:event_jLabel138MouseClicked

    private void jLabel138KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel138KeyPressed

    }//GEN-LAST:event_jLabel138KeyPressed

    private void optionPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_optionPanelComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_optionPanelComponentShown

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable4MouseClicked

    private void jTable4ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable4ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable4ComponentShown

    //mon espace/ mes statistiques par annee: show mes statistiques par mois
    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed

        homePanelsNotVisible();
        statMPanel.setVisible(true);
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> Rentallist = new ArrayList<String>();

        //add all existing property in select property box (months stats)    
        ResultSet rs1 = select.getData(db, "select * from room ");
        String building = "";
        ArrayList<String> list1 = new ArrayList<String>();
        int count1 = jComboBox3.getItemCount();
        for (int i = 0; i < count1; i++) {
            list1.add(jComboBox3.getItemAt(i));
        }
        try {
            while (rs1.next()) {
                building = rs1.getString("building");
                if (!list1.contains(building)) {
                    jComboBox3.addItem(building);
                    list1.add(building);
                }
            }

            rs1.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        jComboBox3.setSelectedIndex(0);

        ResultSet rs = select.getData(db, "select * from booking");
        //check how many different years are in the comboBox4 (année)
        int count = jComboBox4.getItemCount();
        for (int i = 0; i < count; i++) {
            //store each year of the comboBox 4 in list
            list.add(jComboBox4.getItemAt(i));
        }

        String in = "";

        try {
            //look at each booking from sql booking table
            while (rs.next()) {
                //add in the statistiques table only the booking with a tax            
                double checkTax = Double.parseDouble(rs.getString("baseTax"));
                if (checkTax != 0) {
                    if (rs.getString("status").equals("terminé") || rs.getString("status").equals("en cours")) {
                        in = new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("booking.checkIn"));
                        int length = in.length();
                        if (length != -1) {
                            in = in.substring(length - 2, length);
                        }
                        //if List does not contain a checkIn year stored in database, add this year to the comboBox4
                        if (!list.contains(in)) {
                            jComboBox4.addItem(in);
                            //add year to list
                            list.add(in);
                        }

                    }
                }
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }


    }//GEN-LAST:event_jButton19ActionPerformed

    //mon espace/ mes statistique par année: set jtable according to the selected item in comboBox1 (année).
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        homeInitYearStats();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    //mon espace/ mes statistiques par année: init jTable
    private void statPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_statPanelComponentShown
        homeInitYearStats();

    }//GEN-LAST:event_statPanelComponentShown

    private void jLabel126FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel126FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel126FocusGained

    private void jLabel127FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel127FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel127FocusGained

    private void jLabel128FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel128FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel128FocusGained

    private void jLabel130FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel130FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel130FocusGained

    private void jLabel93MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel93MouseClicked
        System.out.println("open: mon espace/ mes locations");
        homePanelsNotVisible();
        homeInitMyRentals(db);
        selectTypeBox.setSelectedIndex(0);
        homeLabelNotVisible();
        jLabel126.setVisible(true);
    }//GEN-LAST:event_jLabel93MouseClicked

    private void jLabel94MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel94MouseClicked
        System.out.println("open: mon espace/ mon contrat");
        homePanelsNotVisible();
        createDocPanel.setVisible(true);
        jComboBox6.setSelectedIndex(0);
        jComboBox7.setSelectedIndex(0);
        homeLabelNotVisible();
        jLabel127.setVisible(true);
    }//GEN-LAST:event_jLabel94MouseClicked

    private void jLabel95MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel95MouseClicked
        System.out.println("open: mon espace/ mes emails");
        homePanelsNotVisible();
        homeInitEmailPanel();
        homeLabelNotVisible();
        jComboBox8.setSelectedIndex(0);
        jLabel128.setVisible(true);
    }//GEN-LAST:event_jLabel95MouseClicked

    //open: mon espace/ mes statistiques
    private void jLabel49MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel49MouseClicked
        System.out.println("open: mon espace/ mes infos");
        homePanelsNotVisible();
        homeLabelNotVisible();
        jLabel130.setVisible(true);
        myPanel.setVisible(true);
        homeInitMyInfo();


    }//GEN-LAST:event_jLabel49MouseClicked

    private void jLabel136FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel136FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel136FocusGained

    //open: mon espace/ mes Options 
    private void jLabel131MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel131MouseClicked
        System.out.println("open: mon espace/ mes Options");
        homePanelsNotVisible();
        homeLabelNotVisible();
        jLabel136.setVisible(true);
        optionPanel.setVisible(true);
        homeInitOption();
    }//GEN-LAST:event_jLabel131MouseClicked

    private void homePanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_homePanelComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_homePanelComponentShown

    private void propertyTypeBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_propertyTypeBoxFocusLost

    }//GEN-LAST:event_propertyTypeBoxFocusLost

    //reservation/ nouveau: set property type.
    private void propertyTypeBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_propertyTypeBoxActionPerformed
        //get the property type selected
        String type = (String) propertyTypeBox.getSelectedItem();

        //set the subPanel according to the selected type
        if (type.equals("Maison")) {
            amountLabel.setText("Montant Total du Séjour");
            advanceLabel.setVisible(true);
            boundField.setVisible(true);
            bookingHouseDetails();
        }
        if (type.equals("Chambre")) {
            amountLabel.setText("Montant Par Jour");
            advanceLabel.setVisible(false);
            boundField.setVisible(false);
            bookingHouseDetails();
        }
        if (type.equals("Dortoir")) {
            amountLabel.setText("Montant Jour/ Personne");
            advanceLabel.setVisible(false);
            boundField.setVisible(false);
            bookingHouseDetails();
        }

    }//GEN-LAST:event_propertyTypeBoxActionPerformed

    private void jComboBoxb7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxb7MouseClicked

    }//GEN-LAST:event_jComboBoxb7MouseClicked

    //reservation/nouveu/ plusieurs clients trouvé: select client & display client details 
    private void jComboBoxb7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxb7ActionPerformed
        jLabelb34.setText("");
        //get comboBox item ( items are clients IDs)
        String IDstrg = (String) jComboBoxb7.getSelectedItem();
        if (IDstrg != null) {
            int clientId = Integer.parseInt(IDstrg);
            client c = new client(sc.getClientDetails(clientId));
            jLabelb34.setText("tel: " + c.getTel1() + " - " + c.getTel2());
            jLabelb36.setText("email: " + c.getEmail());
            jLabelb37.setText("adresse: ");
            jLabelb126.setText(c.getStreet1());
            jLabelb124.setText(c.getStreet2());
            jLabelb134.setText(c.getDc());
            jLabelb32.setText(c.getPostCode() + ", " + c.getCity());
        }
    }//GEN-LAST:event_jComboBoxb7ActionPerformed

    private void jLabelb34ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabelb34ComponentShown

    }//GEN-LAST:event_jLabelb34ComponentShown

    private void jButtonb11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonb11MouseClicked
        propertyTypeBox.requestFocus();
    }//GEN-LAST:event_jButtonb11MouseClicked

    //reservation/nouveau / plusieurs clients trouvé: comfirm selected client, set client information in client details fields 
    private void jButtonb11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb11ActionPerformed
        int checkFirstName = 0;
        int clientId = Integer.parseInt((String) jComboBoxb7.getSelectedItem());
//if there is no firstname set, then checkFirstName = 1 (booking for a couple: monsieur et madame ...);
        if (clientFirstName.equals("")) {
            checkFirstName = 1;
        }
// call bookingNewLoadClient from this class to load all client details in client info panel
        sc.setID(clientId);
        bookingNewLoadClient(checkFirstName, clientId);
    }//GEN-LAST:event_jButtonb11ActionPerformed

    //cancel multiple client panel
    private void jButtonb12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb12ActionPerformed
        nameField.setText("");
        firstNameField.setText("");
        searchClientPanel.setVisible(true);
        multipleClients.setVisible(false);
    }//GEN-LAST:event_jButtonb12ActionPerformed

    private void jComboBoxb8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxb8FocusLost
        propertyTypeBox.requestFocus();
    }//GEN-LAST:event_jComboBoxb8FocusLost

    //reservation/ nouveau/ commentaire precedents: set comment text of the selected client and checkIn date
    private void jComboBoxb8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxb8ActionPerformed
        //check if there are bookings under client's name
        if (jComboBoxb8.getItemCount() != 0) {
            //if yes, get the selected checkIn date
            int index = jComboBoxb8.getSelectedIndex();
            String checkIn = jComboBoxb8.getItemAt(index);
            //call the method getComment in searchClient class to get the corresponding comment
            jTextAreab1.setText(sc.getComment(sc.getId(), checkIn));

        }
    }//GEN-LAST:event_jComboBoxb8ActionPerformed

    private void firstNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameFieldActionPerformed

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        jButtonb5.requestFocus();
    }//GEN-LAST:event_nameFieldActionPerformed

    private void nameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonb5.doClick();
        }
    }//GEN-LAST:event_nameFieldKeyPressed

    //reservation/ nouveau/ chercher client/ valider: set next focus
    private void jButtonb5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonb5MouseClicked

    }//GEN-LAST:event_jButtonb5MouseClicked

    //reservation/ nouveau/ chercher client/ valider
    private void jButtonb5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb5ActionPerformed
//reset everything first
        if (sc != null) {
            sc.clearAll();
        }
        clientPanel2.setVisible(false);
        bookingNewBookingInfoPanel.setVisible(false);
        jComboBoxb8.removeAllItems();
//check that there is a name
        if (nameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Merci de Renseigner le nom du client");
        } else {
//create checkfirstName to check if booking is for a couple or an individual. 
            int checkFirstName = 0;
            clientName = nameField.getText();
            //clientName = StringFormatter.clean(clientName);
//if there is no firstname set, then checkFirstName is 1 (booking for a couple: monsieur et madame ...);
            if (firstNameField.getText().isEmpty()) {
                checkFirstName = 1;
//do ot display gender comboBox and label
                jComboBoxb2.setVisible(false);
                jLabelb5.setVisible(false);
                clientFirstName = "";
            } else {
// if there is a firstName, check first name value is still 0. display gender comboBox and label, set focus to gender box
                jComboBoxb2.setVisible(true);
                jLabelb5.setVisible(true);
                clientFirstName = firstNameField.getText();
                // clientFirstName = StringFormatter.clean(clientFirstName);
            }
//call search client class and call searchStatus method : 0= new, 1= one client found, 2= several clients found.       
            sc = new searchClient(db, clientName, clientFirstName);
            int searchStatus = sc.getSearchStatus();
            System.out.println(searchStatus);

//show message new client, set all field to empty, show panel client info and booking info        
            if (searchStatus == 0) {
                bookingNewClient();
                //show message new client. set all panels        
                JOptionPane.showMessageDialog(null, "Nouveau Client");
            }
//call method bookingNewLoadClient in this class to set every field with corresponding client info, set booking date in comment combobox       
            if (searchStatus == 1) {
                sc.setId();
                bookingNewLoadClient(checkFirstName, sc.getId());
            }
// set every client id in multiple client combobox         
            if (searchStatus == 2) {
                jComboBoxb7.removeAllItems();
                var list = sc.getIds();
                for (int i : list) {
                    System.out.println(i);
                    jComboBoxb7.addItem(String.valueOf(i));
                }
                multipleClients.setVisible(true);
                searchClientPanel.setVisible(false);
                jComboBoxb7.requestFocus();
            }
        }
    }//GEN-LAST:event_jButtonb5ActionPerformed

    //reservation/nouveau/ rechercher client/ effacer: clear all data
    private void jButtonb15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb15ActionPerformed
        nameField.setText("");
        firstNameField.setText("");
        bookingNewResetClient();
        clientPanel2.setVisible(false);
        bookingNewBookingInfoPanel.setVisible(false);
    }//GEN-LAST:event_jButtonb15ActionPerformed

    private void idFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_idFieldMouseEntered

    }//GEN-LAST:event_idFieldMouseEntered

    private void idFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idFieldActionPerformed
        propertyTypeBox.requestFocus();
    }//GEN-LAST:event_idFieldActionPerformed

    private void idFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            propertyTypeBox.requestFocus();
        }
    }//GEN-LAST:event_idFieldKeyPressed

//reservation/ nouveau/ info client/ créer homonyme: create a new client of an already existing name and firstname
    private void jLabelb112MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb112MouseClicked
//reset all client info fields to default
        bookingNewClient();
        sc.resetSearchStatus();
    }//GEN-LAST:event_jLabelb112MouseClicked

    private void singleFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_singleFieldFocusGained
        singleField.selectAll();
    }//GEN-LAST:event_singleFieldFocusGained

    private void singleFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_singleFieldMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_singleFieldMouseClicked

    private void singleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleFieldActionPerformed

    }//GEN-LAST:event_singleFieldActionPerformed

    private void singleFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_singleFieldKeyTyped
        if (!singleField.getText().isEmpty()) {
            bookingHouseDetails();
        }
    }//GEN-LAST:event_singleFieldKeyTyped

    private void doubleFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doubleFieldFocusGained
        doubleField.selectAll();
    }//GEN-LAST:event_doubleFieldFocusGained

    private void doubleFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_doubleFieldMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_doubleFieldMouseClicked

    private void doubleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doubleFieldActionPerformed

    }//GEN-LAST:event_doubleFieldActionPerformed

    private void doubleFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_doubleFieldKeyTyped

    }//GEN-LAST:event_doubleFieldKeyTyped
    //reservation/ nouveau/ reservation/ maison/ choisir location
    private void choosePropertyBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosePropertyBoxActionPerformed
        String bookingType = (String) bookingTypeBox.getSelectedItem();
        int propertyType = propertyTypeBox.getSelectedIndex();
        unitName = (String) choosePropertyBox.getSelectedItem();

        if (unitName != null) {
            unitName = unitName.replace("\'", "\'\'");
        }
        if (calendarIn.getDate() != null) {
            // calculate number of stay and return an int answer
            Date dateIn = calendarIn.getDate();
            int numberOfStay = bookingDate.getNumberOfStay(dateIn, calendarOut.getDate());

            bookingType bt = new bookingType(db, bookingType, propertyType, unitName);
            bt.setInitialPrice(numberOfStay);
            amountField.setText(bt.getAmount());
            boundField.setText(bt.getBound());
            taxField.setText(bt.getTax());
            bookingAddress = bt.getAdress();

        }
    }//GEN-LAST:event_choosePropertyBoxActionPerformed

    private void amountFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountFieldFocusGained
        amountField.selectAll();
    }//GEN-LAST:event_amountFieldFocusGained

    private void amountFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountFieldFocusLost

    }//GEN-LAST:event_amountFieldFocusLost

    private void amountFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountFieldActionPerformed

    //reservation/ nouveau/ reservation/ maison/ enregistrer: save booking
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        System.out.println("save new booking");
//check if all field are filled
        if (singleField.getText().isBlank() || doubleField.getText().isBlank() || amountField.getText().isBlank() || nameField.getText().isBlank() || calendarIn.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Aïe! informations manquantes");
        } else {
            client c = new client();
            c.setName(StringFormatter.clean(nameField.getText()));
            c.setFirstName(StringFormatter.clean(firstNameField.getText()));
            String gender = "";
            if (!firstNameField.getText().isEmpty()) {
                gender = (String) jComboBoxb2.getSelectedItem();
            } else {
                gender = "Madame/Monsieur";
            }
//get all information from client panel and set them to client class            
            c.setGender(gender);
            c.setTel1(StringFormatter.simpleClean(tel1Field.getText()));
            c.setTel2(StringFormatter.simpleClean(tel2Field.getText()));
            c.setEmail(StringFormatter.simpleClean(emailField.getText()));
            c.setStreet1(StringFormatter.clean(address1Field.getText()));
            c.setStreet2(StringFormatter.clean(address2Field.getText()));
            c.setDc(StringFormatter.clean(districtField.getText()));
            c.setPostCode(StringFormatter.simpleClean(postCodeField.getText()));
            c.setCity(StringFormatter.simpleClean(cityField.getText()));
            c.setIdProof(StringFormatter.simpleClean(idField.getText()));
            String blackList = "false";
            if (blackListCheckBox.isSelected()) {
                blackList = "true";
            }
            c.setBlackList(blackList);

            //get booking values
            int booking = 0;
            int adult = Integer.valueOf(adultField.getText());
            int child = Integer.valueOf(kidField.getText());
            int singleBed = Integer.valueOf(singleField.getText());
            int doubleBed = Integer.valueOf(doubleField.getText());
            String propertyType = (String) propertyTypeBox.getSelectedItem();
            String bookingType = (String) bookingTypeBox.getSelectedItem();
            unitName = (String) choosePropertyBox.getSelectedItem();

            int numberOfStay = 0;
            //set option and get selected option
            String optionID = "";
            String dailyRate = "";
            String bound = "";
            if (!boundField.getText().isEmpty()) {
                bound = boundField.getText();
            }
            String timeIn = timeInField.getText();
            String timeOut = timeOutField.getText();
            String Query = "select max(ID) from client";
            String Query2 = "select max(ID) from booking";
            //get  number of stay (if stay is less than 1 day stay= 1)
            Date dateIn = calendarIn.getDate();
            //String checkIn = new SimpleDateFormat("yyyy-MM-dd").format(dateIn);
            String checkIn = new SimpleDateFormat("yyyy-MM-dd").format(dateIn);
            if (calendarOut.getDate() != null) {
                Date dateOut = calendarOut.getDate();
                String checkOut = new SimpleDateFormat("yyyy-MM-dd").format(dateOut);
                if (checkIn.equals(checkOut)) {
                    numberOfStay = 1;
                } else {
                    try {
                        long diffInMillies = Math.abs(dateIn.getTime() - dateOut.getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                        numberOfStay = (int) diff;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
                double amountStay = 0;
                if (propertyTypeBox.getSelectedIndex() == 0) {
                    amountStay = Double.parseDouble(amountField.getText());
                }
                if (propertyTypeBox.getSelectedIndex() == 1) {
                    amountStay = Double.parseDouble(amountField.getText()) * numberOfStay;
                }
                int totalPeople = adult + child;
                if (propertyTypeBox.getSelectedIndex() == 2) {
                    amountStay = (Double.parseDouble(amountField.getText()) * totalPeople) * numberOfStay;
                }
                double priceHouse = amountStay / numberOfStay;
                double priceRoom = Double.parseDouble(amountField.getText());

                double baseTax = Float.parseFloat(taxField.getText().replaceAll(",", "."));
                baseTax = Math.round(baseTax * 100.0) / 100.0;
                double tax = (baseTax * adult) * numberOfStay;
                tax = Math.round(tax * 100.0) / 100.0;
                double totalWithTax = amountStay + tax;

                int totalOption = 0;
                int amount = 0;
                int optionAmount = 0;
                String optionName = "";
                try {
                    //find the next bookingID generated, ( bookingOption will get this id later)
                    ResultSet rs2 = select.getData(db, Query2);
                    while (rs2.next()) {
                        booking = rs2.getInt(1) + 1;
                        bookingID = booking;
                    }
                    rs2.close();
                    //look at all option on the table
                    TableModel model = jTableb7.getModel();
                    for (int i = 0; i < jTableb7.getRowCount(); i++) {
                        optionID = model.getValueAt(i, 0).toString();
                        optionName = model.getValueAt(i, 1).toString();
                        String Query4 = "select * from myOption WHERE ID='" + optionID + "'";
                        ResultSet rs3 = select.getData(db, Query4);
                        while (rs3.next()) {
                            //calculate rate( fix rate or daily rate)
                            amount = Integer.parseInt(rs3.getString("amount"));
                            if (rs3.getString("dailyRate").equals("false")) {
                                optionAmount = Integer.parseInt(rs3.getString("amount"));
                                dailyRate = "false";
                            } else {
                                dailyRate = "true";
                                optionAmount = Integer.parseInt(rs3.getString("amount")) * numberOfStay;
                            }

                            if (model.getValueAt(i, 3) != null) {
                                boolean isSelected = (boolean) model.getValueAt(i, 3);
                                String selected = "";
                                // save the option as selected or not in the database 
                                if (isSelected) {
                                    selected = "true";
                                    totalOption += optionAmount;
                                } else {
                                    selected = "false";
                                }
                                String Query3 = " INSERT INTO bookingOption (bookingID, name, dailyRate, amount, optionID, selected) values ('" + bookingID + "','" + optionName + "','" + dailyRate + "','" + amount + "','" + optionID + "', '" + selected + "')";
                                InsertUpdateDelete.setData(db, Query3, "");

                            } else {
                                String Query3 = " INSERT INTO bookingOption (bookingID, name, dailyRate, amount, optionID, selected) values ('" + bookingID + "','" + optionName + "','" + dailyRate + "','" + amount + "','" + optionID + "', 'false')";
                                InsertUpdateDelete.setData(db, Query3, "");
                            }

                        }
                        rs3.close();
                    }

                    //add option to TOTAL
                    totalWithTax += totalOption;
                    double totalNonDirect = totalOption + tax;
                    //if advance smaller than 20 and total price smaller than 30 advance is total price, if total price is between 30 and 70 advance is 20
                    //set arres value to 30% of the booking amountStay (tax and options excluded) round down to tenths.

                    double advance = amountStay * 0.30;
                    advance = (10 * floor(advance / 10));
                    if (advance < 20) {
                        if (totalWithTax <= 30) {
                            advance = totalWithTax;
                        }
                        if (totalWithTax > 30 && amountStay < 70) {
                            advance = 20;
                        }
                    }

                    int searchStatus = sc.getSearchStatus();
//if client doesn't exist, get a new client ID
                    if (searchStatus == 0) {
                        c.save(db);
                        clientID = c.getID();

                    } else {
//else, update client information
                        c.update(db, sc.getId());
                        clientID = sc.getId();
                    }
//data on data base are save diffenretlymaccording to booking type and property type
//if booking type is direct, total amount is as set above, if not direct separate amount to be paid by external service and amount to be paid by client
                    if (bookingType.equals("Direct")) {
                        if (propertyTypeBox.getSelectedIndex() == 0) {
                            Query = "insert into booking ( clientID,origin, checkIn, roomName,adult , child, singleBed, doubleBed, bookingType, pricePerDay, checkOut,timeIn, timeOut, numberOfStay,myOption, totalAmount, bound, status, contratStatus, boundStatus, advanceStatus, paid, toPay, tax, baseTax, totalWithTax, addressProperty, advanceAmount) values('" + clientID + "','" + bookingType + "','" + checkIn + "','" + unitName.replace("\'", "\'\'") + "','" + adult + "','" + child + "','" + singleBed + "','" + doubleBed + "','" + propertyType + "','" + priceHouse + "','" + checkOut + "','" + timeIn + "','" + timeOut + "','" + numberOfStay + "','" + totalOption + "','" + amountStay + "','" + bound + "','en cours', 'false','false','false','0','" + totalWithTax + "','" + tax + "','" + baseTax + "','" + totalWithTax + "','" + StringFormatter.simpleClean(bookingAddress) + "','" + advance + "')";
                            InsertUpdateDelete.setData(db, Query, "Félicitaion! , Nouvelle réservation enregistrée avec succès :) ");
                        } else {
                            Query = "insert into booking ( clientID,origin, checkIn, roomName, adult, child, singleBed, doubleBed,  bookingType, pricePerDay, checkOut, timeIn, timeOut, numberOfStay, status, contratStatus, advanceStatus, myOption, totalAmount,tax,baseTax, totalWithTax, paid, toPay, addressProperty, advanceAmount) values('" + clientID + "','" + bookingType + "','" + checkIn + "','" + unitName + "','" + adult + "','" + child + "','" + singleBed + "','" + doubleBed + "','" + propertyType + "','" + priceRoom + "','" + checkOut + "','" + timeIn + "','" + timeOut + "','" + numberOfStay + "','en cours','false','false','" + totalOption + "','" + amountStay + "','" + tax + "','" + baseTax + "','" + totalWithTax + "','0','" + totalWithTax + "','" + StringFormatter.simpleClean(bookingAddress) + "','" + advance + "')";
                            InsertUpdateDelete.setData(db, Query, "Félicitaion! , Nouvelle réservation enregistrée avec succès :) ");
                        }
                    } else {
                        if (propertyTypeBox.getSelectedIndex() == 0) {
                            Query = "insert into booking ( clientID,origin, checkIn, roomName,adult , child, singleBed, doubleBed, bookingType, pricePerDay, checkOut,timeIn, timeOut, numberOfStay,myOption, totalAmount, bound, status, contratStatus, boundStatus, advanceStatus, paid, toPay, tax, baseTax, totalWithTax, addressProperty, advanceAmount, amountExternal, externalDue, externalPaid) values('" + clientID + "','" + bookingType + "','" + checkIn + "','" + unitName.replace("\'", "\'\'") + "','" + adult + "','" + child + "','" + singleBed + "','" + doubleBed + "','" + propertyType + "','" + priceHouse + "','" + checkOut + "','" + timeIn + "','" + timeOut + "','" + numberOfStay + "','" + totalOption + "','" + totalOption + "','" + bound + "','en cours', 'false','false','false','0','" + totalNonDirect + "','" + tax + "','" + baseTax + "','" + totalNonDirect + "','" + StringFormatter.simpleClean(bookingAddress) + "','0','" + amountStay + "','" + amountStay + "','0')";
                            InsertUpdateDelete.setData(db, Query, "Félicitaion! , Nouvelle réservation enregistrée avec succès :) ");
                        } else {
                            Query = "insert into booking ( clientID,origin, checkIn, roomName, adult, child, singleBed, doubleBed,  bookingType, pricePerDay, checkOut, timeIn, timeOut, numberOfStay, status, contratStatus, advanceStatus, myOption, totalAmount,tax,baseTax, totalWithTax, paid, toPay, addressProperty, advanceAmount, amountExternal, externalDue, externalPaid) values('" + clientID + "','" + bookingType + "','" + checkIn + "','" + unitName + "','" + adult + "','" + child + "','" + singleBed + "','" + doubleBed + "','" + propertyType + "','" + priceRoom + "','" + checkOut + "','" + timeIn + "','" + timeOut + "','" + numberOfStay + "','en cours','false','false','" + totalOption + "','" + totalOption + "','" + tax + "','" + baseTax + "','" + totalNonDirect + "','0','" + totalNonDirect + "','" + StringFormatter.simpleClean(bookingAddress) + "','0','" + amountStay + "','" + amountStay + "','0')";
                            InsertUpdateDelete.setData(db, Query, "Félicitaion! , Nouvelle réservation enregistrée avec succès :) ");
                        }
                    }

                    bookingResetBookingInfo();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Oups... Date De Départ Manquante");
            }
            //check if client is set as blacklisted or not and save status in db
            if (blackListCheckBox.isSelected()) {
                manageClient.BlackList(db, clientID, true);
            } else {
                manageClient.BlackList(db, clientID, false);
            }

//check if booking is direct or not (if yes create contract and open email panel, if not close panel)
            if (bookingType.equals("Direct")) {

                contract.build(db, booking);

                bookingInitEmailPanel(booking);
            } else {
                emailType = 2;
                contract.build(db, booking);
                var email = new bookingNewNoContract(db, bookingID);
                //document.initFields(db, bookingID);
                endEmailObjectField1.setText(email.getSubject());
                endEmailTextField1.setText(email.getMsge());
                bookingPanelsNotVisible();
                sendEmailPanel3.setVisible(true);
            }

            sc.clearAll();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    //reservation, nouveau/ reservation/ maison/ effacer: clear all house fields
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        bookingResetBookingInfo();
        bookingInitNewPanel();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void boundFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_boundFieldFocusGained
        boundField.selectAll();
    }//GEN-LAST:event_boundFieldFocusGained

    private void boundFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boundFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boundFieldActionPerformed

    private void adultFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_adultFieldFocusGained
        adultField.selectAll();
    }//GEN-LAST:event_adultFieldFocusGained

    private void adultFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adultFieldActionPerformed
        adultField.selectAll();
        bookingHouseDetails();
    }//GEN-LAST:event_adultFieldActionPerformed

    private void kidFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_kidFieldFocusGained
        kidField.selectAll();
    }//GEN-LAST:event_kidFieldFocusGained

    private void kidFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kidFieldActionPerformed
        bookingHouseDetails();
    }//GEN-LAST:event_kidFieldActionPerformed

    private void jLabelb56MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb56MouseClicked
        newBooking.setVisible(false);
        bookingOption.setVisible(true);
    }//GEN-LAST:event_jLabelb56MouseClicked

    private void calendarInPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_calendarInPropertyChange
        calendarOut.getMonthView().setLowerBound(calendarIn.getDate());
        calendarOut.setDate(calendarIn.getDate());
    }//GEN-LAST:event_calendarInPropertyChange

    private void jCheckBoxb9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxb9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxb9ActionPerformed

    private void jButtonb30FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButtonb30FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonb30FocusLost

    private void jButtonb30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonb30MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonb30MouseClicked

    //reservation// nouveau/ envoyer contrat/ oui: send selected option
    private void jButtonb30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb30ActionPerformed

        if (jCheckBoxb9.isSelected()) {

            emailType = 1;
            var email = new newBooking(db, bookingID);
            endEmailObjectField1.setText(email.getSubject());
            endEmailTextField1.setText(email.getMsge());
        }
        //send contrat by post mail (create envelop and open the 3 pdf in a pdg viewer
        if (jCheckBoxb11.isSelected()) {
            envelop.build(db, bookingID);
            contract.open(db, bookingID);
        }

        bookingPanelsNotVisible();
        //bookingInitNewPanel();
        sendEmailPanel3.setVisible(true);
    }//GEN-LAST:event_jButtonb30ActionPerformed

    //reservation/ nouveau/ envoyer contrat/ non: close send contrat panel and open nouveau panel
    private void jButtonb31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb31ActionPerformed
        bookingPanelsNotVisible();
        bookingInitNewPanel();
    }//GEN-LAST:event_jButtonb31ActionPerformed

    //reservation/ nouveau/ envoyer contrat/ voir contrat: see the newly created contrat
    private void jLabelb122MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb122MouseClicked
        contract.open(db, bookingID);
    }//GEN-LAST:event_jLabelb122MouseClicked

    private void jTableb3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableb3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableb3MouseClicked

    private void jTableb3ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTableb3ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableb3ComponentShown

    //reservatiom/ reservation terminée/ rechercher: search previous booking by familly name and property type
    private void jButtonb33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb33ActionPerformed
        //if name field is not empty, get the name and the property type selected
        if (!jTextFieldb62.getText().isEmpty()) {
            String name = jTextFieldb62.getText();
            String building = (String) jComboBoxb11.getSelectedItem();
            //if all property is not selected search selected property type, by checkout date order ( from nearest to furthest)
            if (!building.equals("Toutes mes propriétés")) {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'terminé' AND roomName LIKE '%" + building + "%' ORDER BY checkout DESC");
                DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
                //reset jTable
                model.setRowCount(0);
                try {
                    //get all booking with corresponding name and add it to jTable
                    while (rs.next()) {
                        if (rs.getString("client.name").toLowerCase().equals(name)) {
                            model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.bookingType"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.paiementMethod"), rs.getString("booking.status")});
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                //else, search all booking by checkout date order (from nearest to furthest)
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'terminé' ORDER BY checkOut DESC");
                DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
                //reset jTable
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        //insert all booking that include client name in jTable
                        if (rs.getString("client.name").toLowerCase().equals(name)) {
                            model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.bookingType"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), rs.getString("booking.checkOut"), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.paiementMethod"), rs.getString("booking.status")});
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } else {
            //if no name is provided, search booking only by property type
            String building = (String) jComboBoxb11.getSelectedItem();
            //if one sepcific property type is selected, search all booking of this property by check out date order( nearest to furthest)
            if (!building.equals("Toutes mes propriétés")) {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'terminé' AND roomName LIKE '%" + building + "%' ORDER BY checkOut DESC");
                DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
                //reset jTable
                model.setRowCount(0);
                try {
                    //insert all bokking found
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.bookingType"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.paiementMethod"), rs.getString("booking.status")});

                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'terminé' ORDER BY checkOut DESC");
                DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.bookingType"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.paiementMethod"), rs.getString("booking.status")});
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }//GEN-LAST:event_jButtonb33ActionPerformed

    private void jTextFieldb62KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldb62KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonb33.doClick();
        }
    }//GEN-LAST:event_jTextFieldb62KeyPressed

    //reservation/ terminé/ rechercher par propriété: search by property
    private void jComboBoxb11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxb11ActionPerformed
        //if name field is not empty, get the name and the property type selected
        if (!jTextFieldb62.getText().isEmpty()) {
            String name = jTextFieldb62.getText();
            String building = (String) jComboBoxb11.getSelectedItem();
            //if all property is not selected search selected property type, by checkout date order ( from nearest to furthest)
            if (!building.equals("Toutes mes propriétés")) {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'terminé' AND roomName LIKE '" + building + "%' ORDER BY checkOut DESC ");
                DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
                //reset jTable
                model.setRowCount(0);
                try {
                    //get all booking with corresponding name and add it to jTable
                    while (rs.next()) {
                        if (rs.getString("client.name").toLowerCase().equals(name)) {
                            model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.bookingType"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.paiementMethod"), rs.getString("booking.status")});
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                //else, search all booking by checkout date order (from nearest to furthest)
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'terminé' ORDER BY checkOut DESC");
                DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
                //reset jTable
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        //insert all booking that include client name in jTable
                        if (rs.getString("client.name").toLowerCase().equals(name)) {
                            model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.bookingType"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.paiementMethod"), rs.getString("booking.status")});
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } else {
            //if no name is provided, search booking only by property type
            String building = (String) jComboBoxb11.getSelectedItem();
            //if one sepcific property type is selected, search all booking of this property by check out date order( nearest to furthest)
            if (!building.equals("Toutes mes propriétés")) {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'terminé' AND roomName LIKE '%" + building + "%' ORDER BY checkOut DESC");
                DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
                //reset jTable
                model.setRowCount(0);
                try {
                    //insert all bokking found
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.bookingType"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.paiementMethod"), rs.getString("booking.status")});

                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'terminé' ORDER BY checkOut DESC ");
                DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.bookingType"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.paiementMethod"), rs.getString("booking.status")});
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }

    }//GEN-LAST:event_jComboBoxb11ActionPerformed

    //reservation/ passé: init jTable
    private void previousBookingPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_previousBookingPanelComponentShown
        ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID where status = 'terminé' ORDER BY checkOut DESC");
        DefaultTableModel model = (DefaultTableModel) jTableb3.getModel();
        model.setRowCount(0);
        jTableb3.getColumnModel().getColumn(0).setPreferredWidth(25);
        jTableb3.getColumnModel().getColumn(3).setPreferredWidth(45);
        jTableb3.getColumnModel().getColumn(5).setPreferredWidth(60);
        jTableb3.getColumnModel().getColumn(6).setPreferredWidth(60);
        jTableb3.getColumnModel().getColumn(7).setPreferredWidth(25);
        jTableb3.getColumnModel().getColumn(8).setPreferredWidth(40);
        jTableb3.getColumnModel().getColumn(9).setPreferredWidth(50);
        jTableb3.getColumnModel().getColumn(10).setPreferredWidth(60);
        jTableb3.getColumnModel().getColumn(11).setPreferredWidth(30);
        jScrollPaneb5.getViewport().setBackground(Color.white);
        try {
            while (rs.next()) {

                model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("client.firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getString("booking.amountExternal"), rs.getString("booking.externalPaid")});

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_previousBookingPanelComponentShown

    //reservation/ en cours/ reservation/ enregistrer
    private void jButtonb20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb20ActionPerformed
        bookingSaveCurrBooking();
        JOptionPane.showMessageDialog(null, "Enregistré avec succès :)");
        bookingInitBooking();
    }//GEN-LAST:event_jButtonb20ActionPerformed

    //reservation/ en cours/ reservation/ effacer: reset page
    private void jButtonb21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb21ActionPerformed
        System.out.println("reservation/ en cours/ booking: clear page");
        bookingInitBookingOption2();
        bookingInitBooking();

    }//GEN-LAST:event_jButtonb21ActionPerformed

    private void jTextFieldb29FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldb29FocusLost
        bookingSetTotal();
    }//GEN-LAST:event_jTextFieldb29FocusLost

    private void jTextFieldb29KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldb29KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonb20.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldb29KeyPressed

    private void toPayFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toPayFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toPayFieldActionPerformed

    private void grandTotalFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grandTotalFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grandTotalFieldActionPerformed

    private void jTextFieldb40FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldb40FocusLost
        bookingSetTotal();
    }//GEN-LAST:event_jTextFieldb40FocusLost

    private void jTextFieldb40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb40ActionPerformed

    private void jTextFieldb40KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldb40KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldb41.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldb40KeyPressed

    private void jTextFieldb41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb41ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb41ActionPerformed

    private void jTextFieldb44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb44ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb44ActionPerformed

    private void jCheckBoxb1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxb1MouseClicked
        if (jCheckBoxb1.isSelected()) {
            jCheckBoxb1.setForeground(new Color(55, 185, 55));
            jCheckBoxb1.setText("Reçu");
        } else {
            jTextFieldb26.setForeground(Color.black);
            jCheckBoxb1.setText("Non Reçu");
        }
    }//GEN-LAST:event_jCheckBoxb1MouseClicked

    private void jCheckBoxb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxb1ActionPerformed

    }//GEN-LAST:event_jCheckBoxb1ActionPerformed

    private void jTextFieldb26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb26ActionPerformed

    private void jTextFieldb27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb27ActionPerformed

    //reservation/ en cours/ reservation/ arrhes
    private void jCheckBoxb2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxb2MouseClicked
        //if advance is paid and checkbox for advance is enable:
        if (jCheckBoxb2.isSelected() && jCheckBoxb2.isEnabled()) {
            double advance = Double.parseDouble(jTextFieldb27.getText());
            double paid = Double.parseDouble(paidField.getText()) + advance;
            paid = Math.round(paid * 100.0) / 100.0;
            double total = Double.parseDouble(grandTotalField.getText());
            total = Math.round(total * 100.0) / 100.0;
            double toPay = total - advance;
            toPay = Math.round(toPay * 100.0) / 100.0;
            toPayField.setText(String.valueOf(toPay));
            paidField.setText(String.valueOf(paid));
            //make priceHouse per day and bound not editable anymore and make bound status green
            jCheckBoxb2.setForeground(new Color(55, 185, 55));
            jCheckBoxb2.setEnabled(false);
            jCheckBoxb2.setText("Reçu");
            jTextFieldb27.setEditable(false);
        }

    }//GEN-LAST:event_jCheckBoxb2MouseClicked

    private void jCheckBoxb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxb2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxb2ActionPerformed

    private void jCheckBoxb3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBoxb3MouseClicked
        if (jCheckBoxb3.isSelected()) {
            //jTextFieldb29.setEditable(false);
            jCheckBoxb3.setForeground(new Color(55, 185, 55));
            jCheckBoxb3.setText("Signé");
        } else {
            //jTextFieldb29.setEditable(true);
            jCheckBoxb3.setForeground(new Color(255, 50, 50));
            jCheckBoxb3.setText("Non Signé");
        }
    }//GEN-LAST:event_jCheckBoxb3MouseClicked

    private void jCheckBoxb3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxb3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxb3ActionPerformed

    private void jTextFieldb35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb35ActionPerformed

    }//GEN-LAST:event_jTextFieldb35ActionPerformed

    private void jLabelb80MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb80MouseClicked
        bookingsPanel.setVisible(false);
        bookingOption2.setVisible(true);
        bookingMenuPanel.setVisible(false);
    }//GEN-LAST:event_jLabelb80MouseClicked

    private void jLabelb75MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb75MouseClicked
        contract.open(db, bookingID);
    }//GEN-LAST:event_jLabelb75MouseClicked

    private void jButtonb22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb22ActionPerformed
        //check if booking is fully paid, if yes, disable paiments fields
        if (toPayField.getText().equals("0.0")) {
            jTextFieldb61.setEditable(false);
            jTextFieldb49.setForeground(new Color(24, 185, 24));
        } else {
            //if not, enable paiement fields
            jTextFieldb61.setEditable(true);
            jTextFieldb61.setEnabled(true);
            jTextFieldb49.setForeground(new Color(255, 100, 100));
        }
        //transfer values to next panel
        //tax:
        jTextFieldb50.setText(jTextFieldb35.getText());
        //night:
        jTextFieldb51.setText(jTextFieldb38.getText());
        //TOTAL:
        jTextFieldb53.setText(grandTotalField.getText());
        //paid:
        jTextFieldb54.setText(paidField.getText());
        // topay:
        jTextFieldb49.setText(toPayField.getText());
        //option:
        jTextFieldb52.setText(jTextFieldb48.getText());
        endBookingPanel.setVisible(true);
        bookingsPanel.setVisible(false);
        jCheckBoxb6.setSelected(true);
        jCheckBoxb8.setSelected(false);
        jCheckBoxb5.setSelected(false);
    }//GEN-LAST:event_jButtonb22ActionPerformed

    private void bookingsPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_bookingsPanelComponentShown

    }//GEN-LAST:event_bookingsPanelComponentShown

    private void jButtonb24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb24ActionPerformed
        double toPay = Double.parseDouble(jTextFieldb49.getText());
        if (toPay < 0.0) {
            JOptionPane.showMessageDialog(null, "montant a rembourser: " + toPay);
        }
        paidField.setText(jTextFieldb54.getText());
        grandTotalField.setText(jTextFieldb53.getText());
        toPayField.setText(jTextFieldb49.getText());
        bookingSaveCurrBooking();
//check if finishbooking is selected:
        if (jCheckBoxb5.isSelected()) {
            int check = 0;
            if (toPay > 0) {
                int a = JOptionPane.showConfirmDialog(null, "Réservation non payée integralement. Terminer, êtes-vous sure?", "Select", JOptionPane.YES_NO_OPTION);
                if (a == 0) {
                    check = 1;
                }
            } else {
                check = 1;
            }

            if (check == 1) {

                bill.build(db, bookingID);
//if sendEmail is selected open sendEmailPanel3( endOfBooking Email)
//all other action to terminate the booking will be done when sending the email in that panel
                if (jCheckBoxb6.isSelected()) {
// create a endEmail object to store all the relevant information for the email and send it to the emailPanel3              

// if total is 0 send email with no bill                   
                    emailType = 3;
                    var bfnb = new bookingFinishNoBill(db, bookingID);
                    endEmailObjectField1.setText(bfnb.getSubject());
                    endEmailTextField1.setText(bfnb.getMsge());

                    sendEmailPanel3.setVisible(true);
                    endBookingPanel.setVisible(false);
                } else {
                    //if there is no email to send terminate the booking here.   
                    //if send by postmail is selected open the relevant pdf files
                    if (jCheckBoxb8.isSelected()) {
                        envelop.build(db, bookingID);
                        bill.open(db, bookingID);
                    }
                    //set the booking status to terminated       
                    String query4 = "Update booking set status='terminé' WHERE ID= " + bookingID;
                    InsertUpdateDelete.setData(db, query4, "");
                    // and finally reset all panel 
                    bookingPanelsNotVisible();
                    bookingResetEndBooking();
                    allBookingPanel.setVisible(true);
                }
            }
        } else {

            JOptionPane.showMessageDialog(null, "Enregistré avec succès :)");
            bookingInitBooking();
            bookingResetEndBooking();
            endBookingPanel.setVisible(false);
            bookingsPanel.setVisible(true);
        }

    }//GEN-LAST:event_jButtonb24ActionPerformed

    private void jButtonb25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb25ActionPerformed
        endBookingPanel.setVisible(false);
        bookingResetEndBooking();
        bookingsPanel.setVisible(true);
    }//GEN-LAST:event_jButtonb25ActionPerformed

    private void jCheckBoxb5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxb5ActionPerformed
        if (jCheckBoxb5.isSelected()) {
            jPanel4.setVisible(true);
        } else {
            jPanel4.setVisible(false);
        }


    }//GEN-LAST:event_jCheckBoxb5ActionPerformed

    private void jComboBoxb9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxb9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxb9ActionPerformed

    private void jTextFieldb61FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldb61FocusLost
        if (!jTextFieldb61.getText().isBlank()) {
            double totalWithTax = Double.parseDouble(jTextFieldb53.getText());
            String paiement = jTextFieldb61.getText().replace(",", ".");
            double justPaid = Double.parseDouble(paiement);
            justPaid = Math.round(justPaid * 100.0) / 100.0;

            double paid = Double.parseDouble(paidField.getText()) + justPaid;
            paid = Math.round(paid * 100.0) / 100.0;
            double toPay = Double.parseDouble(toPayField.getText()) - justPaid;
            toPay = Math.round(toPay * 100.0) / 100.0;

            if (toPay == 0) {

                jTextFieldb61.setEnabled(false);
                jTextFieldb49.setForeground(new Color(25, 185, 25));

                paid = totalWithTax;
                paid = Math.round(paid * 100.0) / 100.0;
                toPay = 0.0;
                jTextFieldb49.setText(String.valueOf(toPay));
                jTextFieldb54.setText(String.valueOf(paid));
            } else {
                jTextFieldb49.setText(String.valueOf(toPay));
                jTextFieldb54.setText(String.valueOf(paid));
                jTextFieldb49.setForeground(new Color(255, 100, 100));
            }

        } else {
            double justPaid = 0;
            justPaid = Math.round(justPaid * 100.0) / 100.0;

            double paid = Double.parseDouble(paidField.getText()) + justPaid;
            paid = Math.round(paid * 100.0) / 100.0;
            double toPay = Double.parseDouble(toPayField.getText()) - justPaid;
            toPay = Math.round(toPay * 100.0) / 100.0;

            jTextFieldb49.setText(String.valueOf(toPay));
            jTextFieldb54.setText(String.valueOf(paid));
        }
    }//GEN-LAST:event_jTextFieldb61FocusLost

    private void jTextFieldb61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb61ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb61ActionPerformed

    private void jTextFieldb61KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldb61KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jCheckBoxb5.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldb61KeyPressed

    private void jCheckBoxb6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxb6ActionPerformed

    }//GEN-LAST:event_jCheckBoxb6ActionPerformed

    private void jCheckBoxb8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxb8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxb8ActionPerformed

    private void jTextFieldb49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb49ActionPerformed
        double total = Double.parseDouble(jTextFieldb49.getText());
        if (total > 0.0) {
            jTextFieldb49.setForeground(Color.red);
        } else {
            jTextFieldb49.setForeground(Color.green);
        }
    }//GEN-LAST:event_jTextFieldb49ActionPerformed

    private void jTextFieldb50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb50ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb50ActionPerformed

    //reservation/ en cours/ reservation// terminer payer/ regler la totalité 
    private void jLabelb97MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb97MouseClicked
        if (Double.parseDouble(jTextFieldb49.getText()) != 0) {
            jTextFieldb61.setEnabled(false);
            jTextFieldb49.setForeground(new Color(25, 185, 25));
            double totalWithTax = Double.parseDouble(jTextFieldb53.getText());
            jTextFieldb49.setText("0");
            jTextFieldb54.setText(String.valueOf(totalWithTax));
        }
    }//GEN-LAST:event_jLabelb97MouseClicked

    //reservation/ en cours: select a booking
    private void jTableb1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableb1MouseClicked
        int index = jTableb1.getSelectedRow();
        String IDstrg = jTableb1.getValueAt(index, 0).toString();
        bookingID = Integer.parseInt(IDstrg);
        bookingsPanel.setVisible(true);
        allBookingPanel.setVisible(false);
        bookingInitBooking();

    }//GEN-LAST:event_jTableb1MouseClicked

    private void jTableb1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTableb1ComponentShown

    }//GEN-LAST:event_jTableb1ComponentShown

    private void jButtonb34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb34ActionPerformed
        if (!jTextFieldb63.getText().isEmpty()) {
            String name = jTextFieldb63.getText();
            String building = (String) jComboBoxb10.getSelectedItem();
            if (!building.equals("Toutes mes propriétés")) {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'en cours' AND roomName LIKE '%" + building + "%'  ORDER BY checkIn ");
                DefaultTableModel model = (DefaultTableModel) jTableb1.getModel();
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        if (rs.getString("client.name").toLowerCase().equals(name)) {
                            model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'en cours' ORDER BY  checkIn  ");
                DefaultTableModel model = (DefaultTableModel) jTableb1.getModel();
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        if (rs.getString("client.name").toLowerCase().equals(name)) {
                            model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }

        } else {

            String building = (String) jComboBoxb10.getSelectedItem();
            if (!building.equals("Toutes mes propriétés")) {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'en cours' AND roomName LIKE '%" + building + "%' ORDER BY checkIn ");
                DefaultTableModel model = (DefaultTableModel) jTableb1.getModel();
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});

                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'en cours'  ORDER BY checkIn   ");
                DefaultTableModel model = (DefaultTableModel) jTableb1.getModel();
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }//GEN-LAST:event_jButtonb34ActionPerformed

    private void jTextFieldb63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb63ActionPerformed

    }//GEN-LAST:event_jTextFieldb63ActionPerformed

    private void jTextFieldb63KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldb63KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonb34.doClick();
        }
    }//GEN-LAST:event_jTextFieldb63KeyPressed

    private void jComboBoxb10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxb10ActionPerformed
        String building = (String) jComboBoxb10.getSelectedItem();
        if (!building.equals("Toutes mes propriétés") && jTextFieldb63.getText().isEmpty()) {
            ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'en cours' AND roomName LIKE '%" + building + "%' ORDER BY checkIn ");
            DefaultTableModel model = (DefaultTableModel) jTableb1.getModel();
            model.setRowCount(0);
            try {
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
                }
                rs.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            if (!building.equals("Toutes mes propriétés")) {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'en cours' AND roomName LIKE '%" + building + "%' ORDER BY checkIn");
                DefaultTableModel model = (DefaultTableModel) jTableb1.getModel();
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        if (jTextFieldb63.getText().isEmpty()) {
                            model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
                        } else {
                            String name = rs.getString("client.name").toLowerCase();
                            if (name.equals(jTextFieldb63.getText())) {
                                model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
                            }
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'en cours'  ORDER BY checkIn ");
                DefaultTableModel model = (DefaultTableModel) jTableb1.getModel();
                model.setRowCount(0);
                try {
                    while (rs.next()) {
                        if (jTextFieldb63.getText().isEmpty()) {
                            model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
                        } else {
                            String name = rs.getString("client.name").toLowerCase();
                            if (name.equals(jTextFieldb63.getText())) {
                                model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
                            }
                        }
                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
    }//GEN-LAST:event_jComboBoxb10ActionPerformed

    private void allBookingPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_allBookingPanelComponentShown

        ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID WHERE status = 'en cours'  ORDER BY checkIn  ");

        DefaultTableModel model = (DefaultTableModel) jTableb1.getModel();
        model.setRowCount(0);
        jTableb1.getColumnModel().getColumn(0).setPreferredWidth(15);
        jTableb1.getColumnModel().getColumn(3).setPreferredWidth(45);
        jTableb1.getColumnModel().getColumn(7).setPreferredWidth(20);
        jTableb1.getColumnModel().getColumn(8).setPreferredWidth(30);
        jTableb1.getColumnModel().getColumn(9).setPreferredWidth(30);
        jTableb1.getColumnModel().getColumn(5).setPreferredWidth(40);
        jTableb1.getColumnModel().getColumn(6).setPreferredWidth(40);
        jTableb1.getColumnModel().getColumn(9).setPreferredWidth(35);
        jTableb1.getColumnModel().getColumn(10).setPreferredWidth(20);
        jTableb1.getColumnModel().getColumn(11).setPreferredWidth(20);
        jTableb1.getColumnModel().getColumn(12).setPreferredWidth(20);
        jScrollPaneb2.getViewport().setBackground(Color.white);

        try {
            while (rs.next()) {

                model.addRow(new Object[]{rs.getString("booking.ID"), rs.getString("client.name"), rs.getString("firstName"), rs.getString("booking.origin"), rs.getString("booking.roomName"), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn")), new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut")), rs.getString("booking.numberOfStay"), rs.getString("booking.totalWithTax"), rs.getString("booking.paid"), rs.getBoolean("booking.advanceStatus"), rs.getBoolean("booking.contratStatus"), rs.getBoolean("booking.boundStatus")});
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }


    }//GEN-LAST:event_allBookingPanelComponentShown

    private void jTableb8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableb8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableb8MouseClicked

    private void jTableb8ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTableb8ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableb8ComponentShown

    //reservation/ en cours/ gerer option/ anuler: leave bookingOption
    private void jButtonb29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb29ActionPerformed
        System.out.println("reservation/ en cours/ booking: save option");
        DefaultTableModel model = (DefaultTableModel) jTableb8.getModel();
        int amount = 0;
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 4) != null) {
                    if (model.getValueAt(i, 4).equals(true)) {
                        amount = (int) model.getValueAt(i, 3) + amount;
                        /* String Query3 = " update bookingOption SET selected ='true' WHERE bookingID= "+bookingID+" AND name= '"+model.getValueAt(i, 1)+"'";
                        InsertUpdateDelete.setData(db, Query3, "");*/
                    }
                    /*else{
                        String Query3 = " update bookingOption SET selected  ='false' WHERE bookingID= "+bookingID+" AND name= '"+model.getValueAt(i, 1)+"'";
                        InsertUpdateDelete.setData(db, Query3, "");
                    }*/
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        jTextFieldb48.setText(String.valueOf(amount));

        bookingSetTotal();

        bookingsPanel.setVisible(true);
        bookingOption2.setVisible(false);

    }//GEN-LAST:event_jButtonb29ActionPerformed

    private void bookingOption2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_bookingOption2ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_bookingOption2ComponentShown

    private void jTableb7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableb7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableb7MouseClicked

    private void jTableb7ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTableb7ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableb7ComponentShown


    private void jButtonb23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb23ActionPerformed
        newBooking.setVisible(true);
        bookingOption.setVisible(false);
    }//GEN-LAST:event_jButtonb23ActionPerformed

    private void jButtonb27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb27ActionPerformed
        bookingOption.setVisible(false);
        newBooking.setVisible(true);
    }//GEN-LAST:event_jButtonb27ActionPerformed

    private void bookingOptionComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_bookingOptionComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_bookingOptionComponentShown

    private void jLabelb130FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelb130FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelb130FocusGained

    private void jLabelb131FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelb131FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelb131FocusGained

    private void jLabelb132FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelb132FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelb132FocusGained

    private void jLabelb104MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb104MouseClicked
        bookingLabelNotVisible();
        jLabelb130.setVisible(true);
        bookingPanelsNotVisible();
        allBookingPanel.setVisible(true);

        ResultSet rs = select.getData(db, "select * from room");
        String building = "";
        ArrayList<String> list = new ArrayList<String>();
        int count = jComboBoxb10.getItemCount();
        for (int i = 0; i < count; i++) {
            list.add(jComboBoxb10.getItemAt(i));
        }
        try {
            while (rs.next()) {
                building = rs.getString("building");
                if (!list.contains(building)) {
                    jComboBoxb10.addItem(building);
                    list.add(building);
                }
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        jComboBoxb10.setSelectedIndex(0);
    }//GEN-LAST:event_jLabelb104MouseClicked

    private void jLabelb105MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb105MouseClicked
        bookingPanelsNotVisible();
        bookingLabelNotVisible();
        jLabelb131.setVisible(true);
        previousBookingPanel.setVisible(true);
        ResultSet rs = select.getData(db, "select * from room ");
        String building = "";
        ArrayList<String> list = new ArrayList<String>();
        int count = jComboBoxb11.getItemCount();
        for (int i = 0; i < count; i++) {
            list.add(jComboBoxb11.getItemAt(i));
        }
        try {
            while (rs.next()) {
                building = rs.getString("building");
                if (!list.contains(building)) {
                    jComboBoxb11.addItem(building);
                    list.add(building);
                }
            }

            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        jComboBoxb11.setSelectedIndex(0);
    }//GEN-LAST:event_jLabelb105MouseClicked

    private void jLabelb106MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb106MouseClicked
        bookingPanelsNotVisible();
        bookingLabelNotVisible();
        jLabelb132.setVisible(true);
        bookingInitNewPanel();
    }//GEN-LAST:event_jLabelb106MouseClicked

    private void bookingIdProofFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookingIdProofFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bookingIdProofFieldActionPerformed

    private void endBookingPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_endBookingPanelComponentShown
        jPanel4.setVisible(false);
    }//GEN-LAST:event_endBookingPanelComponentShown

    private void myPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_myPanelComponentShown

    }//GEN-LAST:event_myPanelComponentShown

    private void jLabel148MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel148MouseClicked
        contratTxt.copy();
    }//GEN-LAST:event_jLabel148MouseClicked

    private void jLabel147MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel147MouseClicked
        contratTxt.paste();
    }//GEN-LAST:event_jLabel147MouseClicked

    private void autoLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel10MouseClicked
        String s = autoLabel10.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel10MouseClicked

    private void autoLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel9MouseClicked
        String s = autoLabel9.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel9MouseClicked

    private void jLabel151MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel151MouseClicked
        jTextArea2.copy();
    }//GEN-LAST:event_jLabel151MouseClicked

    private void jLabel152MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel152MouseClicked
        jTextArea2.paste();
    }//GEN-LAST:event_jLabel152MouseClicked

    private void autoLabelb9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb9MouseClicked
        String s = autoLabelb9.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb9MouseClicked

    private void autoLabelb10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb10MouseClicked
        String s = autoLabelb10.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb10MouseClicked

    private void bookingDcFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookingDcFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bookingDcFieldActionPerformed

    private void jTextFieldb48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb48ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb48ActionPerformed

    private void jCheckBox2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCheckBox2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2FocusLost

    private void myLogoFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_myLogoFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            myStreet1Field.requestFocus();
        }
    }//GEN-LAST:event_myLogoFieldKeyPressed

    private void mySiretFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mySiretFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            websiteField.requestFocus();
        }
    }//GEN-LAST:event_mySiretFieldKeyPressed

    private void otherReviewFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_otherReviewFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            jButton4.requestFocus();
        }
    }//GEN-LAST:event_otherReviewFieldKeyPressed

    private void taxHouseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taxHouseTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_taxHouseTextFieldActionPerformed

    private void taxFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_taxFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_taxFieldFocusGained

    private void taxFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taxFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_taxFieldActionPerformed

    private void myDcFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myDcFieldFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_myDcFieldFocusLost

    private void sendEmailLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendEmailLabelMouseClicked
        bookingSaveCurrBooking();
        jComboBox5.setSelectedIndex(1);
        bookingsPanel.setVisible(false);
        sendEmailPanel.setVisible(true);
        bookingMenuPanel.setVisible(false);
    }//GEN-LAST:event_sendEmailLabelMouseClicked

    private void jButtonb32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb32ActionPerformed
        bookingsPanel.setVisible(true);
        sendEmailPanel.setVisible(false);
    }//GEN-LAST:event_jButtonb32ActionPerformed

    //reservation/ en cours/ reservation/ envoyer email/ valider
    private void jButtonb35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb35ActionPerformed
        if (jTextField14.getText().isEmpty() || jTextArea3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Objet ou message manquant");
        } else {
            /*
            String from = "";
            String to = "";
            String password = "";
            String subject = jTextField14.getText();
            String msge = jTextArea3.getText();
            String fileName = "";
            
            ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID where booking.ID = '" + bookingID + "' ");
            ResultSet rs2 = select.getData(db, "select * from myInfo  where ID = '1' ");

            try {
                while (rs.next()) {
                    fileName = documents.getContratPath(db)+rs.getString("booking.contractName");
                    to = rs.getString("client.email");
                }
                rs.close();
                while (rs2.next()) {
                    from = rs2.getString("email");
                    password = rs2.getString("password");
                }
                rs2.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            File file = new File(fileName);
            File file2 = new File(documents.getGeneralConditions(db));
            //if contrat is selected
            if (jCheckBox3.isSelected()) {
                try {
                    msge = documents.replaceInfoFromString(db, bookingID, msge);
                    JavaMailUtil.sendEmail(from, password, to, subject, msge, file, file2);
                    JOptionPane success = new JOptionPane();
                    success.showMessageDialog(null, "Email envoyé");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Email non reçu!");
                }
            } else {
                //if contrat is not selected
                try {
                    msge = documents.replaceInfoFromString(db, bookingID, msge);
                    JavaMailUtil.sendEmail(from, password, to, subject, msge);
                    JOptionPane success = new JOptionPane();
                    success.showMessageDialog(null, "Email envoyé");
                } catch (Exception ex) {
                    //Logger.getLogger(bookings.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "Email non reçu!");
                }
            }
             */
            String object = jTextField14.getText();
            String msge = jTextArea3.getText();
            //if contrat is selected
            if (jCheckBox3.isSelected()) {
                sendEmail.sendWithContract(db, bookingID, object, msge);
            } else {
                //if contrat is not selected
                sendEmail.send(db, bookingID, object, msge);

            }
            bookingsPanel.setVisible(true);
            sendEmailPanel.setVisible(false);

        }

    }//GEN-LAST:event_jButtonb35ActionPerformed

    private void sendEmailPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_sendEmailPanelComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_sendEmailPanelComponentShown

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    //reservation/ en cours/ reservation/ envoyer email/ selection email
    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        jCheckBox3.setSelected((false));
        //String email = (String) jComboBox5.getSelectedItem();
        int index = jComboBox5.getSelectedIndex();
        //look at what email is being called,
        // and create the corresponding email object: the object will get all necessary information (object, recipent, msge(with details about booking,client and owner )    
        if (index == 0) {

            if (jCheckBox3.isSelected()) {
                var nb = new newBooking(db, bookingID);
                jTextField14.setText(nb.getSubject());
                jTextArea3.setText(nb.getMsge());
            } else {
                var nb = new bookingNewNoContract(db, bookingID);
                jTextField14.setText(nb.getSubject());
                jTextArea3.setText(nb.getMsge());
            }
        }

        if (index == 1) {
            var bc = new bookingConfirmed(db, bookingID);
            jTextField14.setText(bc.getSubject());
            jTextArea3.setText(bc.getMsge());
        }
        if (index == 2) {
            var bnc = new bookingNotConfirmed(db, bookingID);
            jTextField14.setText(bnc.getSubject());
            jTextArea3.setText(bnc.getMsge());

        }
        if (index == 3) {
            var be = new blankEmail(db, bookingID);
            jTextField14.setText(be.getSubject());
            jTextArea3.setText(be.getMsge());
        }
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        int index = jComboBox5.getSelectedIndex();
        //look at what email is being called,
        // and create the corresponding email object: the object will get all necessary information (object, recipent, msge(with details about booking,client and owner )    
        if (index == 0) {

            if (jCheckBox3.isSelected()) {
                var nb = new newBooking(db, bookingID);
                jTextField14.setText(nb.getSubject());
                jTextArea3.setText(nb.getMsge());
            } else {
                var nb = new bookingNewNoContract(db, bookingID);
                jTextField14.setText(nb.getSubject());
                jTextArea3.setText(nb.getMsge());
            }
        }
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void bookingFirstNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookingFirstNameFieldActionPerformed

    }//GEN-LAST:event_bookingFirstNameFieldActionPerformed

    private void bookingFirstNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bookingFirstNameFieldKeyPressed

    }//GEN-LAST:event_bookingFirstNameFieldKeyPressed

    private void bookingFirstNameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bookingFirstNameFieldFocusLost

    }//GEN-LAST:event_bookingFirstNameFieldFocusLost

    private void bookingFirstNameFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bookingFirstNameFieldKeyTyped

    }//GEN-LAST:event_bookingFirstNameFieldKeyTyped

    private void bookingFirstNameFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bookingFirstNameFieldKeyReleased
        if (!bookingFirstNameField.getText().isEmpty()) {
            jLabelb61.setVisible(true);
            bookingGenderBox.setVisible(true);
        } else {
            jLabelb61.setVisible(false);
            bookingGenderBox.setVisible(false);
        }
    }//GEN-LAST:event_bookingFirstNameFieldKeyReleased

    private void AdressHouseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdressHouseTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AdressHouseTextFieldActionPerformed

    private void AdressHouseTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AdressHouseTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AdressHouseTextFieldKeyPressed

    private void taxDormTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_taxDormTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_taxDormTextFieldKeyPressed

    private void taxRoomTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_taxRoomTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_taxRoomTextFieldKeyPressed

    private void contratTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contratTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contratTitleActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        int index = jComboBox6.getSelectedIndex();
        try {
            if (index == 0) {
                ResultSet rs = select.getData(db, "select * from documents where name= 'contratInfoTitle'");
                while (rs.next()) {

                    contratTitle.setText(rs.getString("text"));
                }
                rs.close();
                rs = select.getData(db, "select * from documents where name= 'contratIntro'");
                while (rs.next()) {
                    contratTxt.setText(rs.getString("text"));
                }
                rs.close();
            }
            if (index == 1) {
                ResultSet rs1 = select.getData(db, "select * from documents where name= 'contratOwnerTitle'");
                while (rs1.next()) {
                    contratTitle.setText(rs1.getString("text"));
                }
                rs1.close();
                ResultSet rs2 = select.getData(db, "select * from documents where name= 'contratOwner'");
                while (rs2.next()) {
                    contratTxt.setText(rs2.getString("text"));
                }
                rs2.close();
            }
            if (index == 2) {
                ResultSet rs3 = select.getData(db, "select * from documents where name= 'contratPropertyTitle'");
                while (rs3.next()) {
                    contratTitle.setText(rs3.getString("text"));
                }
                rs3.close();

                ResultSet rs4 = select.getData(db, "select * from documents where name= 'contratProperty'");
                while (rs4.next()) {
                    contratTxt.setText(rs4.getString("text"));
                }
                rs4.close();
            }
            if (index == 3) {
                ResultSet rs5 = select.getData(db, "select * from documents where name= 'contratClientTitle'");
                while (rs5.next()) {
                    contratTitle.setText(rs5.getString("text"));
                }
                rs5.close();
                ResultSet rs6 = select.getData(db, "select * from documents where name= 'contratClient'");
                while (rs6.next()) {
                    contratTxt.setText(rs6.getString("text"));
                }
                rs6.close();

            }
            if (index == 4) {
                ResultSet rs7 = select.getData(db, "select * from documents where name= 'contratBookingTitle'");
                while (rs7.next()) {
                    contratTitle.setText(rs7.getString("text"));
                }
                rs7.close();

                ResultSet rs8 = select.getData(db, "select * from documents where name= 'contratBooking'");
                while (rs8.next()) {
                    contratTxt.setText(rs8.getString("text"));
                }
                rs8.close();

            }
            if (index == 5) {
                ResultSet rs7 = select.getData(db, "select * from documents where name= 'contratConditionTitle'");
                while (rs7.next()) {
                    contratTitle.setText(rs7.getString("text"));
                }
                rs7.close();

                ResultSet rs8 = select.getData(db, "select * from documents where name= 'contratCondition'");
                while (rs8.next()) {
                    contratTxt.setText(rs8.getString("text"));
                }
                rs8.close();
            }

            if (index == 6) {
                ResultSet rs7 = select.getData(db, "select * from documents where name= 'contratSignatureTitle'");
                while (rs7.next()) {
                    contratTitle.setText(rs7.getString("text"));
                }
                rs7.close();

                ResultSet rs8 = select.getData(db, "select * from documents where name= 'contratSignature'");
                while (rs8.next()) {
                    contratTxt.setText(rs8.getString("text"));
                }
                rs8.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }


    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void myNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myNameFieldActionPerformed

    private void myFirstNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myFirstNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myFirstNameFieldActionPerformed

    private void autoLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel7MouseClicked
        String s = autoLabel7.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel7MouseClicked

    private void autoLabelb7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb7MouseClicked
        String s = autoLabelb7.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb7MouseClicked

    private void autoLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabel14MouseClicked
        String s = autoLabel14.getText();
        int currentCaretPosition = contratTxt.getCaretPosition();
        contratTxt.insert(s, currentCaretPosition);
    }//GEN-LAST:event_autoLabel14MouseClicked

    private void autoLabelb16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoLabelb16MouseClicked
        String element = autoLabelb16.getText();
        int currentCaretPosition = jTextArea2.getCaretPosition();
        jTextArea2.insert(element, currentCaretPosition);
    }//GEN-LAST:event_autoLabelb16MouseClicked

    private void jLabelb138MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb138MouseClicked
        int index = jTableb3.getSelectedRow();
        String IDstrg = jTableb3.getValueAt(index, 0).toString();
        int booking = Integer.parseInt(IDstrg);
        contract.open(db, booking);
    }//GEN-LAST:event_jLabelb138MouseClicked

    private void jLabelb142MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb142MouseClicked
        int index = jTableb3.getSelectedRow();
        String IDstrg = jTableb3.getValueAt(index, 0).toString();
        int booking = Integer.parseInt(IDstrg);
        bill.open(db, booking);
    }//GEN-LAST:event_jLabelb142MouseClicked

    private void endEmailCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endEmailCancelButtonActionPerformed
        previousBookingPanel.setVisible(true);
        sendEmailPanel2.setVisible(false);
    }//GEN-LAST:event_endEmailCancelButtonActionPerformed

    private void endEmailOkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endEmailOkButtonActionPerformed

        if (linkBillBox.isSelected()) {
            sendEmail.sendWithBill(db, bookingID, endEmailObjectField.getText(), endEmailTextField.getText());

        } else {
            sendEmail.send(db, bookingID, endEmailObjectField.getText(), endEmailTextField.getText());
        }
        previousBookingPanel.setVisible(true);
        sendEmailPanel2.setVisible(false);

    }//GEN-LAST:event_endEmailOkButtonActionPerformed

    private void endEmailObjectFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endEmailObjectFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endEmailObjectFieldActionPerformed

    private void sendEmailPanel2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_sendEmailPanel2ComponentShown

    }//GEN-LAST:event_sendEmailPanel2ComponentShown

    private void linkBillBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkBillBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_linkBillBoxActionPerformed

    private void jLabelb103MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb103MouseClicked
        int index = jTableb3.getSelectedRow();
        String IDstrg = jTableb3.getValueAt(index, 0).toString();

        previousBookingPanel.setVisible(false);
        bookingID = Integer.parseInt(IDstrg);
        document.initFields(db, bookingID);
        var be = new blankEmail(db, bookingID);
        endEmailObjectField.setText(be.getSubject());
        endEmailTextField.setText(be.getMsge());
        sendEmailPanel2.setVisible(true);
    }//GEN-LAST:event_jLabelb103MouseClicked

    private void firstNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_firstNameFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonb5.doClick();
        }
    }//GEN-LAST:event_firstNameFieldKeyPressed

    private void NewContractLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NewContractLabelMouseClicked

        bookingSaveCurrBooking();
        bookingInitBooking();

        contract.build(db, bookingID);

        int a = JOptionPane.showConfirmDialog(null, "Nouveau contrat créé avec succès. Voulez-vous voir le nouveau contrat?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            contract.open(db, bookingID);
        }
        bookingMenuPanel.setVisible(false);
    }//GEN-LAST:event_NewContractLabelMouseClicked

    private void jLabel183MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel183MouseClicked
        websiteField.paste();
    }//GEN-LAST:event_jLabel183MouseClicked

    private void jLabel182MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel182MouseClicked
        facebookField.paste();
    }//GEN-LAST:event_jLabel182MouseClicked

    private void jLabel181MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel181MouseClicked
        instaField.paste();
    }//GEN-LAST:event_jLabel181MouseClicked

    private void jLabel180MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel180MouseClicked
        googletField.paste();
    }//GEN-LAST:event_jLabel180MouseClicked

    private void jLabel179MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel179MouseClicked
        tripAdvisorField.paste();
    }//GEN-LAST:event_jLabel179MouseClicked

    private void jLabel178MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel178MouseClicked
        otherReviewField.paste();
    }//GEN-LAST:event_jLabel178MouseClicked

    private void calendarInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calendarInActionPerformed
        bookingHouseDetails();
    }//GEN-LAST:event_calendarInActionPerformed

    private void calendarOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calendarOutActionPerformed
        bookingHouseDetails();
    }//GEN-LAST:event_calendarOutActionPerformed

    private void doubleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doubleFieldFocusLost
        if (doubleField.getText().isEmpty()) {
            doubleField.setText("0");
            bookingHouseDetails();
        } else {
            bookingHouseDetails();
        }
    }//GEN-LAST:event_doubleFieldFocusLost

    private void adultFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_adultFieldKeyTyped

    }//GEN-LAST:event_adultFieldKeyTyped

    private void kidFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kidFieldKeyTyped

    }//GEN-LAST:event_kidFieldKeyTyped

    private void doubleFieldInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_doubleFieldInputMethodTextChanged

    }//GEN-LAST:event_doubleFieldInputMethodTextChanged

    private void singleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_singleFieldFocusLost
        if (singleField.getText().isEmpty()) {
            singleField.setText("0");
            bookingHouseDetails();
        } else {
            bookingHouseDetails();
        }
    }//GEN-LAST:event_singleFieldFocusLost

    private void kidFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_kidFieldFocusLost
        if (kidField.getText().isEmpty()) {
            kidField.setText("0");
            bookingHouseDetails();
        } else {
            bookingHouseDetails();
        }
    }//GEN-LAST:event_kidFieldFocusLost

    private void adultFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_adultFieldFocusLost
        if (adultField.getText().isEmpty()) {
            adultField.setText("0");
            bookingHouseDetails();
        } else {
            bookingHouseDetails();
        }
    }//GEN-LAST:event_adultFieldFocusLost

    private void jComboBoxb14FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxb14FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxb14FocusLost

    private void jComboBoxb14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxb14ActionPerformed

        //check if there are bookings under client's name
        if (jComboBoxb14.getItemCount() != 0) {
            //if yes, get the selected checkIn date
            int index = jComboBoxb14.getSelectedIndex();
            String checkIn = jComboBoxb14.getItemAt(index);
            //call the method getComment in searchClient class to get the corresponding comment
            jTextAreab3.setText(sc.getComment(sc.getId(), checkIn));

        }
    }//GEN-LAST:event_jComboBoxb14ActionPerformed

    private void clientfirstNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientfirstNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientfirstNameFieldActionPerformed

    private void clientfirstNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clientfirstNameFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonb16.doClick();
        }
    }//GEN-LAST:event_clientfirstNameFieldKeyPressed

    private void clientNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientNameFieldActionPerformed

    }//GEN-LAST:event_clientNameFieldActionPerformed

    private void clientNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clientNameFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonb16.doClick();
        }
    }//GEN-LAST:event_clientNameFieldKeyPressed

    private void jButtonb16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonb16MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonb16MouseClicked

    private void jButtonb16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb16ActionPerformed
//reset everything first
        if (sc != null) {
            sc.clearAll();
        }
        clientPanel3.setVisible(false);
        jComboBoxb14.removeAllItems();
//check that there is a name
        if (clientNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Merci de Renseigner le nom du client");
        } else {
//create checkfirstName to check if booking is for a couple or an individual. 
            int checkFirstName = 0;
            clientName = clientNameField.getText();
            //clientName = StringFormatter.clean(clientName);
//if there is no firstname set, then checkFirstName is 1 (booking for a couple: monsieur et madame ...);
            if (clientfirstNameField.getText().isEmpty()) {
                checkFirstName = 1;
                jComboBoxb15.setVisible(false);
                jLabelb158.setVisible(false);
                clientFirstName = "";

            } else {
                jComboBoxb15.setVisible(true);
                jLabelb158.setVisible(true);
                clientFirstName = clientfirstNameField.getText();

            }
//call search client class and call searchStatus method : 0= new, 1= one client found, 2= several clients found.       
            sc = new searchClient(db, clientName, clientFirstName);
            int searchStatus = sc.getSearchStatus();
            System.out.println(searchStatus);

//show message new client, set all field to empty, show panel client info and booking info        
            if (searchStatus == 0) {
                //show message new client. set all panels  
                JOptionPane.showMessageDialog(null, "Nouveau Client");
                homeClientNew();
            }
//call method bookingNewLoadClient in this class to set every field with corresponding client info, set booking date in comment combobox       
            if (searchStatus == 1) {
                sc.setId();
                homeClientLoad(checkFirstName, sc.getId());
            }
// set every client id in multiple client combobox         
            if (searchStatus == 2) {
                jComboBoxb12.removeAllItems();
                var list = sc.getIds();
                for (int i : list) {
                    System.out.println(i);
                    jComboBoxb12.addItem(String.valueOf(i));
                }
                multipleClients1.setVisible(true);
                searchClientPanel1.setVisible(false);
                jComboBoxb12.requestFocus();
            }
        }
    }//GEN-LAST:event_jButtonb16ActionPerformed

    private void jButtonb17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb17ActionPerformed
        clientNameField.setText("");
        clientfirstNameField.setText("");
        homeClientReset();
        clientPanel3.setVisible(false);
    }//GEN-LAST:event_jButtonb17ActionPerformed

    private void clientIdProofFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientIdProofFieldMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_clientIdProofFieldMouseEntered

    private void clientIdProofFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientIdProofFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientIdProofFieldActionPerformed

    private void clientIdProofFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clientIdProofFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientIdProofFieldKeyPressed

    private void jLabelb163MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb163MouseClicked
        homeClientReset();
        sc.resetSearchStatus();
    }//GEN-LAST:event_jLabelb163MouseClicked

    private void jButtonb18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb18ActionPerformed
//check if all field are filled
        client c = new client();
        c.setName(StringFormatter.clean(clientNameField.getText()));
        c.setFirstName(StringFormatter.clean(clientfirstNameField.getText()));
        String gender = "";
        if (!clientfirstNameField.getText().isEmpty()) {
            gender = (String) jComboBoxb15.getSelectedItem();
        } else {
            gender = "Madame/Monsieur";
        }
//get all information from client panel and set them to client class            
        c.setGender(gender);
        c.setTel1(clientTel1Field.getText());
        c.setTel2(clientTel2Field.getText());
        c.setEmail(clientEmailField.getText());
        c.setStreet1(StringFormatter.clean(clientStreet1Field.getText()));
        c.setStreet2(StringFormatter.clean(clientStreet2Field.getText()));
        c.setDc(StringFormatter.clean(clientDcField.getText()));
        c.setPostCode(StringFormatter.simpleClean(clientPostCodeField.getText()));
        c.setCity(StringFormatter.clean(clientCityField.getText()));
        c.setIdProof(StringFormatter.clean(clientIdProofField.getText()));
        String blackList = "false";
        if (clientBlackListCheckBox.isSelected()) {
            blackList = "true";
        }
        c.setBlackList(blackList);

        int searchStatus = sc.getSearchStatus();
        System.out.println(sc.getSearchStatus());
//if client doesn't exist, get a new client ID
        if (searchStatus == 0) {
            c.save(db);
            JOptionPane.showMessageDialog(null, "Client enregistré");
        } else {
//else, update client information
            System.out.println(sc.getId());
            c.update(db, sc.getId());
            JOptionPane.showMessageDialog(null, "Client modifié");
        }

        clientNameField.setText("");
        clientfirstNameField.setText("");
        homeClientReset();
        clientPanel3.setVisible(false);
        sc.clearAll();
    }//GEN-LAST:event_jButtonb18ActionPerformed

    private void jLabel184MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel184MouseClicked
        homePanelsNotVisible();
        client.setVisible(true);
        multipleClients1.setVisible(false);
        commentsPanelb1.setVisible(false);
        clientPanel3.setVisible(false);
        homeLabelNotVisible();
        jLabel185.setVisible(true);
    }//GEN-LAST:event_jLabel184MouseClicked

    private void jLabel185FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel185FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel185FocusGained

    private void jComboBoxb12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxb12MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxb12MouseClicked

    private void jComboBoxb12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxb12ActionPerformed
        //get client's id form conbobox'
        String IDstrg = (String) jComboBoxb12.getSelectedItem();
        if (IDstrg != null) {
            int clientId = Integer.parseInt(IDstrg);
            client c = new client(sc.getClientDetails(clientId));
            jLabelb41.setText("tel: " + c.getTel1() + " - " + c.getTel2());
            jLabelb42.setText("email: " + c.getEmail());
            jLabelb44.setText("adresse: ");
            jLabelb147.setText(c.getStreet1());
            jLabelb148.setText(c.getStreet2());
            jLabelb38.setText(c.getDc());
            jLabelb146.setText(c.getPostCode() + ", " + c.getCity());
        }
    }//GEN-LAST:event_jComboBoxb12ActionPerformed

    private void jLabelb41ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabelb41ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelb41ComponentShown

    private void jButtonb13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonb13MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonb13MouseClicked

    private void jButtonb13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb13ActionPerformed
        int checkFirstName = 0;
        int clientId = Integer.parseInt((String) jComboBoxb12.getSelectedItem());
//if there is no firstname set, then checkFirstName = 1 (booking for a couple: monsieur et madame ...);
        if (clientFirstName.equals("")) {
            checkFirstName = 1;
        }
// call bookingNewLoadClient from this class to load all client details in client info panel
        sc.setID(clientId);
        homeClientLoad(checkFirstName, clientId);
    }//GEN-LAST:event_jButtonb13ActionPerformed

    private void jButtonb14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb14ActionPerformed
        clientfirstNameField.setText("");
        clientNameField.setText("");
        homeClientReset();
        multipleClients1.setVisible(false);
        searchClientPanel1.setVisible(true);
    }//GEN-LAST:event_jButtonb14ActionPerformed

    private void clientNameFieldInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_clientNameFieldInputMethodTextChanged

    }//GEN-LAST:event_clientNameFieldInputMethodTextChanged

    private void clientNameFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_clientNameFieldKeyTyped
        //clientPanel3.setVisible(false);

    }//GEN-LAST:event_clientNameFieldKeyTyped

    private void myDcFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myDcFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_myDcFieldActionPerformed

    private void cancelBookingLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelBookingLabelMouseClicked
        int a = JOptionPane.showConfirmDialog(null, "Anuler la réservation, Est-Vous Sûre ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            String Query = "UPDATE booking set status= 'annulé' WHERE ID= '" + bookingID + "'";
            InsertUpdateDelete.setData(db, Query, "Réservation " + bookingID + " anulée");
            bookingsPanel.setVisible(false);
            allBookingPanel.setVisible(true);
        }
        bookingMenuPanel.setVisible(false);
    }//GEN-LAST:event_cancelBookingLabelMouseClicked

    private void jLabel187MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel187MouseClicked
        System.out.println("import and save logo");
        String logoPath = "";
        String logoName = "";
        String nameOut = "";
        String Pathout = "";
        //search for a logo
        FileDialog fd = new FileDialog(this, "Open", FileDialog.LOAD);
        fd.setVisible(true);
        logoPath = fd.getDirectory() + fd.getFile();
        logoName = fd.getFile();

        if (!logoName.equals("")) {
            if (logoName.contains("PNG") || logoName.contains("JPG") || logoName.contains("GIF") || logoName.contains("png") || logoName.contains("jpg") || logoName.contains("gif")) {
                try {
                    // retrieve image
                    File inputFile = new File(logoPath);
                    //create the logo path
                    String home = System.getProperty("user.home");

                    Pathout = home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + "/image/";
                    //create the logo name
                    nameOut = Pathout + logoName;
                    BufferedImage bi = ImageIO.read(inputFile);
                    File outputfile = new File(nameOut);
                    ImageIO.write(bi, "png", outputfile);
                    //display logo path and name in the dedicated text field, set imported check box to selected
                    myLogoField.setText(logoName);
                    jCheckBox2.setSelected(true);
                    owner.saveLogo(db, logoName);
                    JOptionPane.showMessageDialog(null, "image importré avec succès");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                document.createGeneralCondtions(db);
            }
        }

    }//GEN-LAST:event_jLabel187MouseClicked

    private void jLabel187MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel187MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel187MouseEntered

    private void jLabel187MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel187MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel187MouseExited

    private void jLabel188MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel188MouseClicked

        int a = JOptionPane.showConfirmDialog(null, "Suprimer logo ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            owner.deleteLogo(db);
            myLogoField.setText("");
            jCheckBox2.setSelected(false);
            document.createGeneralCondtions(db);
        }
    }//GEN-LAST:event_jLabel188MouseClicked

    private void jLabel188MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel188MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel188MouseEntered

    private void jLabel188MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel188MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel188MouseExited

    private void jXDatePicker1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker1ActionPerformed
        int numberOfStay = 0;
        Date dateIn = jXDatePicker1.getDate();
        String checkIn = new SimpleDateFormat("dd-MM-yy").format(dateIn);
        Date dateOut = jXDatePicker2.getDate();
        String checkOut = new SimpleDateFormat("dd-MM-yy").format(dateOut);
        if (checkIn.equals(checkOut)) {
            numberOfStay = 1;
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                long diffInMillies = Math.abs(dateIn.getTime() - dateOut.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                numberOfStay = (int) diff;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        jTextFieldb38.setText(String.valueOf(numberOfStay));

        bookingSetChangeDate();
        bookingSetTotal();
        if (!jTextFieldb44.equals("Dortoir")) {
            addAmountPanel.setVisible(true);
            jTextField21.requestFocus();
        }

    }//GEN-LAST:event_jXDatePicker1ActionPerformed

    private void jTextFieldb38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb38ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb38ActionPerformed

    private void jXDatePicker2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker2ActionPerformed
        int numberOfStay = 0;
        Date dateIn = jXDatePicker1.getDate();
        String checkIn = new SimpleDateFormat("dd-MM-yy").format(dateIn);
        Date dateOut = jXDatePicker2.getDate();
        String checkOut = new SimpleDateFormat("dd-MM-yy").format(dateOut);
        if (checkIn.equals(checkOut)) {
            numberOfStay = 1;
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                long diffInMillies = Math.abs(dateIn.getTime() - dateOut.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                numberOfStay = (int) diff;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        jTextFieldb38.setText(String.valueOf(numberOfStay));
        bookingSetChangeDate();
        bookingSetTotal();
        if (!jTextFieldb44.getText().equals("Dortoir")) {
            //JOptionPane.showMessageDialog(null, "Pensez à ajouter le montant additionnel souhaité dans \"Prix Du Séjour\"");
            addAmountPanel.setVisible(true);
            jTextField21.requestFocus();
        }
    }//GEN-LAST:event_jXDatePicker2ActionPerformed

    private void jXDatePicker1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jXDatePicker1ComponentShown

    }//GEN-LAST:event_jXDatePicker1ComponentShown

    private void jXDatePicker1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jXDatePicker1PropertyChange
        jXDatePicker2.getMonthView().setLowerBound(jXDatePicker1.getDate());
    }//GEN-LAST:event_jXDatePicker1PropertyChange

    private void jXDatePicker2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jXDatePicker2ComponentShown

    }//GEN-LAST:event_jXDatePicker2ComponentShown

    private void myApeFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myApeFieldFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_myApeFieldFocusLost

    private void myApeFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_myApeFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_myApeFieldKeyPressed

    private void address2FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_address2FieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_address2FieldActionPerformed

    private void timeInFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_timeInFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_timeInFieldFocusGained

    private void timeInFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_timeInFieldFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_timeInFieldFocusLost

    private void timeInFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeInFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeInFieldActionPerformed

    private void timeOutFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_timeOutFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_timeOutFieldFocusGained

    private void timeOutFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_timeOutFieldFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_timeOutFieldFocusLost

    private void timeOutFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeOutFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeOutFieldActionPerformed

    private void jTextFieldb86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb86ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb86ActionPerformed

    private void jTextFieldb87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb87ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb87ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        double amount = 0;
        if (!jTextField21.getText().isEmpty()) {
            amount = Double.parseDouble(jTextField21.getText());
            double oldAmount = Double.parseDouble(jTextFieldb29.getText());
            double newAmount = amount + oldAmount;
            jTextFieldb29.setText(String.valueOf(newAmount));
        }
        bookingSetTotal();
        addAmountPanel.setVisible(false);
        jTextField21.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField21KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField21KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButton1.doClick();
        }
    }//GEN-LAST:event_jTextField21KeyPressed

    private void clientBlackListCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientBlackListCheckBoxActionPerformed

    }//GEN-LAST:event_clientBlackListCheckBoxActionPerformed

    private void clientBlackListCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_clientBlackListCheckBoxStateChanged
        if (clientBlackListCheckBox.isSelected()) {
            clientBlackListCheckBox.setForeground(Color.red);
        } else {
            clientBlackListCheckBox.setForeground(Color.black);
        }
    }//GEN-LAST:event_clientBlackListCheckBoxStateChanged

    private void blackListCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_blackListCheckBoxStateChanged
        if (blackListCheckBox.isSelected()) {
            blackListCheckBox.setForeground(Color.red);
        } else {
            blackListCheckBox.setForeground(Color.black);
        }
    }//GEN-LAST:event_blackListCheckBoxStateChanged

    private void bookingBlackListCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bookingBlackListCheckBoxStateChanged
        if (bookingBlackListCheckBox.isSelected()) {
            bookingBlackListCheckBox.setForeground(Color.red);
        } else {
            bookingBlackListCheckBox.setForeground(Color.black);
        }
    }//GEN-LAST:event_bookingBlackListCheckBoxStateChanged

    private void jLabel198MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel198MouseClicked
        try {
            excelFile.insertData(db);
            Desktop.getDesktop().open(new File(document.getPath(db, "clients") + "mes clients.xls"));
        } catch (IOException ex) {
            Logger.getLogger(mainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel198MouseClicked

    private void myPhone2FieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_myPhone2FieldFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_myPhone2FieldFocusLost

    private void address1FieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_address1FieldActionPerformed

    }//GEN-LAST:event_address1FieldActionPerformed

    private void address1FieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_address1FieldPropertyChange

    }//GEN-LAST:event_address1FieldPropertyChange

    private void address1FieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_address1FieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_address1FieldKeyReleased

    private void buildEnvelopLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buildEnvelopLabelMouseClicked
        envelop.build(db, bookingID);
        bookingMenuPanel.setVisible(false);
    }//GEN-LAST:event_buildEnvelopLabelMouseClicked

    private void bookingMenuPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingMenuPanelMouseExited
        bookingMenuPanel.setVisible(false);
    }//GEN-LAST:event_bookingMenuPanelMouseExited

    private void jLabel200MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel200MouseClicked
        bookingMenuPanel.setVisible(true);

    }//GEN-LAST:event_jLabel200MouseClicked

    private void buildEnvelopLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buildEnvelopLabelMouseEntered
        bookingMenuPanel.setVisible(true);
        buildEnvelopLabel.setForeground(new Color(0, 153, 204));
        buildEnvelopLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_buildEnvelopLabelMouseEntered

    private void cancelBookingLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelBookingLabelMouseEntered
        bookingMenuPanel.setVisible(true);
        cancelBookingLabel.setForeground(new Color(0, 153, 204));
        cancelBookingLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_cancelBookingLabelMouseEntered

    private void NewContractLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NewContractLabelMouseEntered
        bookingMenuPanel.setVisible(true);
        NewContractLabel.setForeground(new Color(0, 153, 204));
        NewContractLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_NewContractLabelMouseEntered

    private void sendEmailLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendEmailLabelMouseEntered
        bookingMenuPanel.setVisible(true);
        sendEmailLabel.setForeground(new Color(0, 153, 204));
        sendEmailLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_sendEmailLabelMouseEntered

    private void jLabel202MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel202MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel202MouseClicked

    private void jLabel202MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel202MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel202MouseEntered

    private void sendEmailLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendEmailLabelMouseExited
        sendEmailLabel.setForeground(new Color(102, 102, 102));
        sendEmailLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_sendEmailLabelMouseExited

    private void jLabel200MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel200MouseEntered
        jLabel200.setForeground(new Color(0, 153, 204));
        jLabel200.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabel200MouseEntered

    private void jLabel200MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel200MouseExited
        jLabel200.setForeground(new Color(102, 102, 102));
        jLabel200.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jLabel200MouseExited

    private void endEmailCancelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endEmailCancelButton1ActionPerformed
//if we are sending a new booking emailType is 1
        if (emailType == 1) {
            emailBookingPanel.setVisible(true);
        }
        if (emailType == 2) {
            bookingInitNewPanel();

        }

//if we sending a end of booking email with bill emailType is 4        
        if (emailType == 4 || emailType == 3) {
            endBookingPanel.setVisible(true);
            jCheckBoxb5.setSelected(false);
        }
//for other emails, just close the email panel        

        sendEmailPanel3.setVisible(false);
    }//GEN-LAST:event_endEmailCancelButton1ActionPerformed

    private void endEmailOkButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endEmailOkButton1ActionPerformed

        //send email with bill        
//if we are creating a booking:
        //if it's a direct booking
        if (emailType == 1 || emailType == 2) {
            if (linkContractBox.isSelected()) {
                sendEmail.sendWithContract(db, bookingID, endEmailObjectField1.getText(), endEmailTextField1.getText());
            } else {
                //send email without bill           
                sendEmail.send(db, bookingID, endEmailObjectField1.getText(), endEmailTextField1.getText());
            }
            sendEmailPanel3.setVisible(false);
            bookingInitNewPanel();
        }

//if we were ending a booking do the next step       
        if (emailType == 4 || emailType == 3) {
            if (linkBillBox1.isSelected()) {
                sendEmail.sendWithBill(db, bookingID, endEmailObjectField1.getText(), endEmailTextField1.getText());
            } else {
                //send email without bill           
                sendEmail.send(db, bookingID, endEmailObjectField1.getText(), endEmailTextField1.getText());
            }
//if send by postmail was selected then open bill and envelop pdf  
            if (jCheckBoxb8.isSelected()) {
                envelop.build(db, bookingID);
                bill.open(db, bookingID);
            }
            //set the booking status to terminated       
            String query4 = "Update booking set  status='terminé' WHERE ID= " + bookingID;
            InsertUpdateDelete.setData(db, query4, "");
            //and finally reset all panel      
            bookingPanelsNotVisible();
            bookingResetEndBooking();
            allBookingPanel.setVisible(true);
        }
    }//GEN-LAST:event_endEmailOkButton1ActionPerformed

    private void endEmailObjectField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endEmailObjectField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endEmailObjectField1ActionPerformed

    private void linkBillBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkBillBox1ActionPerformed
        if (linkBillBox1.isSelected()) {
// create a endEmail object to store all the relevant information for the email and send it to the emailPanel3              

// if total is 0 send email with no bill                   
            emailType = 4;
            var bfnb = new bookingFinished(db, bookingID);
            endEmailObjectField1.setText(bfnb.getSubject());
            endEmailTextField1.setText(bfnb.getMsge());
        } else {
            emailType = 3;
            var bfnb = new bookingFinishNoBill(db, bookingID);
            endEmailObjectField1.setText(bfnb.getSubject());
            endEmailTextField1.setText(bfnb.getMsge());
        }
    }//GEN-LAST:event_linkBillBox1ActionPerformed

    private void sendEmailPanel3ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_sendEmailPanel3ComponentShown
        if (emailType == 1) {
            linkContractBox.setVisible(true);
            linkContractBox.setSelected(true);
            linkBillBox1.setVisible(false);
            linkBillBox1.setSelected(false);
        }
        if (emailType == 2) {
            linkContractBox.setVisible(false);
            linkContractBox.setSelected(false);
            linkBillBox1.setVisible(false);
            linkBillBox1.setSelected(false);
        }
        if (emailType == 3) {
            linkContractBox.setVisible(false);
            linkContractBox.setSelected(false);
            linkBillBox1.setVisible(true);
            linkBillBox1.setSelected(false);
        }
        if (emailType == 4) {
            linkContractBox.setVisible(false);
            linkContractBox.setSelected(false);
            linkBillBox1.setVisible(true);
            linkBillBox1.setSelected(true);
        }
    }//GEN-LAST:event_sendEmailPanel3ComponentShown

    private void jLabelb80MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb80MouseEntered
        jLabelb80.setForeground(new Color(0, 153, 204));
        jLabelb80.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabelb80MouseEntered

    private void jLabelb75MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb75MouseEntered
        jLabelb75.setForeground(new Color(0, 153, 204));
        jLabelb75.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabelb75MouseEntered

    private void jLabelb80MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb80MouseExited
        jLabelb80.setForeground(new Color(102, 102, 102));
        jLabelb80.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jLabelb80MouseExited

    private void jLabelb75MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb75MouseExited
        jLabelb75.setForeground(new Color(102, 102, 102));
        jLabelb75.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jLabelb75MouseExited

    private void NewContractLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NewContractLabelMouseExited
        NewContractLabel.setForeground(new Color(102, 102, 102));
        NewContractLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_NewContractLabelMouseExited

    private void buildEnvelopLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buildEnvelopLabelMouseExited
        buildEnvelopLabel.setForeground(new Color(102, 102, 102));
        buildEnvelopLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_buildEnvelopLabelMouseExited

    private void cancelBookingLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelBookingLabelMouseExited
        cancelBookingLabel.setForeground(new Color(102, 102, 102));
        cancelBookingLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_cancelBookingLabelMouseExited

    private void jTextFieldb45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb45ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb45ActionPerformed

    private void jTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable7MouseClicked

    private void jTable7ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable7ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable7ComponentShown

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        int index[] = jTable7.getSelectedRows();

        int a = JOptionPane.showConfirmDialog(null, "Supprimer options, Est-Vous Sûre ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {
            for (int i = 0; i < index.length; i++) {
                String name = (String) jTable7.getValueAt(index[i], 0);
                String Query = "DELETE FROM externalBooking WHERE name='" + name + "'";
                InsertUpdateDelete.setData(db, Query, "");
            }

        }
        homeInitExternalBooking();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextFieldActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        var ceb = new externalBooking();
        ceb.setName(nameTextField.getText());

        ceb.saveInDb(db);
        homeInitExternalBooking();

    }//GEN-LAST:event_jButton24ActionPerformed

    private void externalBookingComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_externalBookingComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_externalBookingComponentShown

    private void jLabel204FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel204FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel204FocusGained

    private void jLabel207MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel207MouseClicked
        homePanelsNotVisible();
        externalBooking.setVisible(true);
        homeInitExternalBooking();
        homeLabelNotVisible();
        jLabel204.setVisible(true);
    }//GEN-LAST:event_jLabel207MouseClicked

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        homeResetExternalBooking();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void bookingTypeBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bookingTypeBoxFocusLost

    }//GEN-LAST:event_bookingTypeBoxFocusLost

    private void bookingTypeBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookingTypeBoxActionPerformed
        bookingHouseDetails();

    }//GEN-LAST:event_bookingTypeBoxActionPerformed

    private void bookingTypeBoxComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_bookingTypeBoxComponentShown

    }//GEN-LAST:event_bookingTypeBoxComponentShown

    private void bookingTypeBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingTypeBoxMouseClicked

    }//GEN-LAST:event_bookingTypeBoxMouseClicked

    private void jButtonb26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb26ActionPerformed
        if (bookingStatus == 3) {
            String Query = "UPDATE Booking set toPay = '" + jTextFieldb57.getText() + "',paid ='" + jTextFieldb56.getText() + "'  WHERE ID= " + bookingID;
            InsertUpdateDelete.setData(db, Query, "");

            if (jTextFieldb57.getText().equals("0.0") || jTextFieldb57.getText().equals("0")) {
                bookingTypeLabel.setForeground(new Color(55, 185, 55));
            } else {
                bookingTypeLabel.setForeground(Color.red);
            }

            bookingExternalPanel.setVisible(false);
            previousBookingPanel.setVisible(true);

        } else {
            String Query = "UPDATE Booking set  externalDue= '" + jTextFieldb57.getText() + "',externalPaid ='" + jTextFieldb56.getText() + "'  WHERE ID= " + bookingID;
            InsertUpdateDelete.setData(db, Query, "");

            if (jTextFieldb57.getText().equals("0.0") || jTextFieldb57.getText().equals("0")) {
                bookingTypeLabel.setForeground(new Color(55, 185, 55));
            } else {
                bookingTypeLabel.setForeground(Color.red);
            }

            bookingExternalPanel.setVisible(false);
            if (bookingStatus == 1) {
                bookingsPanel.setVisible(true);
            }
            if (bookingStatus == 2) {
                previousBookingPanel.setVisible(true);
            }
        }
    }//GEN-LAST:event_jButtonb26ActionPerformed

    private void jButtonb28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonb28ActionPerformed
        bookingExternalPanel.setVisible(false);

        if (bookingStatus == 1) {
            bookingsPanel.setVisible(true);
        }
        if (bookingStatus >= 2) {
            previousBookingPanel.setVisible(true);
        }
    }//GEN-LAST:event_jButtonb28ActionPerformed

    private void jTextFieldb64FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldb64FocusLost
        if (!jTextFieldb64.getText().isBlank()) {
            double amountStay = Double.parseDouble(jTextFieldb55.getText());
            String paiement = jTextFieldb64.getText().replace(",", ".");
            double justPaid = Double.parseDouble(paiement);
            justPaid = Math.round(justPaid * 100.0) / 100.0;

            double paid = Double.parseDouble(jTextFieldb56.getText()) + justPaid;
            paid = Math.round(paid * 100.0) / 100.0;
            double toPay = Double.parseDouble(jTextFieldb57.getText()) - justPaid;
            toPay = Math.round(toPay * 100.0) / 100.0;

            if (toPay == 0) {

                jTextFieldb64.setEnabled(false);
                jTextFieldb57.setForeground(new Color(25, 185, 25));

                paid = amountStay;
                paid = Math.round(paid * 100.0) / 100.0;
                toPay = 0.0;
                jTextFieldb57.setText(String.valueOf(toPay));
                jTextFieldb56.setText(String.valueOf(paid));

            } else {
                jTextFieldb57.setText(String.valueOf(toPay));
                jTextFieldb56.setText(String.valueOf(paid));
                jTextFieldb57.setForeground(new Color(255, 100, 100));

            }

        } else {
            double justPaid = 0;
            justPaid = Math.round(justPaid * 100.0) / 100.0;

            double paid = Double.parseDouble(jTextFieldb56.getText()) + justPaid;
            paid = Math.round(paid * 100.0) / 100.0;
            double toPay = Double.parseDouble(jTextFieldb57.getText()) - justPaid;
            toPay = Math.round(toPay * 100.0) / 100.0;

            jTextFieldb57.setText(String.valueOf(toPay));
            jTextFieldb56.setText(String.valueOf(paid));

        }
        jTextFieldb64.setText("");
        jButtonb26.requestFocus();

    }//GEN-LAST:event_jTextFieldb64FocusLost

    private void jTextFieldb64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb64ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb64ActionPerformed

    private void jTextFieldb64KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldb64KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonb26.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldb64KeyPressed

    private void jTextFieldb57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldb57ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldb57ActionPerformed

    private void jLabelb173MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb173MouseClicked
        if (Double.parseDouble(jTextFieldb57.getText()) != 0) {
            jTextFieldb64.setEnabled(false);
            jTextFieldb57.setForeground(new Color(25, 185, 25));
            double totalWithTax = Double.parseDouble(jTextFieldb55.getText());
            jTextFieldb57.setText("0");
            jTextFieldb56.setText(String.valueOf(totalWithTax));
        }
    }//GEN-LAST:event_jLabelb173MouseClicked

    private void bookingExternalPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_bookingExternalPanelComponentShown
        if (jTextFieldb57.getText().equals("0.0")) {
            jTextFieldb64.setEnabled(false);
        } else {
            jTextFieldb64.setEnabled(true);
        }
    }//GEN-LAST:event_bookingExternalPanelComponentShown

    private void bookingTypeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingTypeLabelMouseClicked
        if (!bookingTypeLabel.getText().equals("Direct")) {
            bookingExternalPanel.setVisible(true);
            bookingsPanel.setVisible(false);
            externalBookingTitleLabel.setText("Paiement " + bookingTypeLabel.getText());
            ResultSet rs = select.getData(db, "SELECT * FROM  booking where ID='" + bookingID + "'");

            try {
                while (rs.next()) {
                    jTextFieldb55.setText(String.valueOf(rs.getString("amountExternal")));
                    jTextFieldb57.setText(String.valueOf(rs.getString("externalDue")));
                    jTextFieldb56.setText(String.valueOf(rs.getString("externalPaid")));
                }
                rs.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
            if (jTextFieldb57.getText().equals("0") || jTextFieldb57.getText().equals("0.0")) {
                jTextFieldb57.setForeground(new Color(55, 185, 55));

            } else {
                jTextFieldb64.setEditable(true);
                jTextFieldb57.setForeground(Color.red);
            }

        }
    }//GEN-LAST:event_bookingTypeLabelMouseClicked

    private void bookingTypeLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingTypeLabelMouseEntered
        if (!bookingTypeLabel.getText().equals("Direct")) {

            bookingTypeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }//GEN-LAST:event_bookingTypeLabelMouseEntered

    private void bookingTypeLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingTypeLabelMouseExited
        if (!bookingTypeLabel.getText().equals("Direct")) {

            bookingTypeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_bookingTypeLabelMouseExited

    private void linkContractBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkContractBoxActionPerformed
        if (linkContractBox.isSelected()) {

            emailType = 1;
            var email = new newBooking(db, bookingID);
            endEmailObjectField1.setText(email.getSubject());
            endEmailTextField1.setText(email.getMsge());
        } else {
            emailType = 2;
            var email = new bookingNewNoContract(db, bookingID);
            endEmailObjectField1.setText(email.getSubject());
            endEmailTextField1.setText(email.getMsge());
        }
    }//GEN-LAST:event_linkContractBoxActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed

        if (jComboBox7.getSelectedIndex() == 0) {
            autoLabel1.setText("<genre>");
            autoLabel2.setText("<ville>");
            autoLabel3.setText("<prénom>");
            autoLabel4.setText("<commune dlg>");
            autoLabel5.setText("<nom>");
            autoLabel6.setText("<cp>");
            autoLabel7.setText("<tel>");
            autoLabel8.setText("<adresse1>");
            autoLabel9.setText("<tel2>");
            autoLabel10.setText("<adresse2>");
            autoLabel11.setText("<email>");
            autoLabel12.setText("<pays>");
            autoLabel13.setText("");
            autoLabel14.setText("");
            autoLabel15.setText("");
            autoLabel16.setText("");
            autoLabel17.setText("");
            autoLabel18.setText("");
        }
        if (jComboBox7.getSelectedIndex() == 1) {
            autoLabel1.setText("<adulte");
            autoLabel2.setText("<enfant>");
            autoLabel3.setText("<lit double>");
            autoLabel4.setText("<lit simple>");
            autoLabel5.setText("<type de location>");
            autoLabel6.setText("<nom de location>");
            autoLabel7.setText("<adresse loc>");
            autoLabel8.setText("<nuit>");
            autoLabel9.setText("<arrivée>");
            autoLabel10.setText("<départ>");
            autoLabel11.setText("<prix jour>");
            autoLabel12.setText("<montant>");
            autoLabel13.setText("<arrhes>");
            autoLabel14.setText("<caution>");
            autoLabel15.setText("<option>");
            autoLabel16.setText("<total option>");
            autoLabel17.setText("<taxe de séjour>");
            autoLabel18.setText("<TOTAL>");
        }

        if (jComboBox7.getSelectedIndex() == 2) {
            autoLabel1.setText("<ma compagnie>");
            autoLabel2.setText("<ma rue>");
            autoLabel3.setText("<mon prénom>");
            autoLabel4.setText("<ma commune déléguée>");
            autoLabel5.setText("<mon nom>");
            autoLabel6.setText("<mon cp>");
            autoLabel7.setText("<mon tel>");
            autoLabel8.setText("<ma ville>");
            autoLabel9.setText("<mon email>");
            autoLabel10.setText("<mon pays>");
            autoLabel11.setText("<mon siret>");
            autoLabel12.setText("<mon APE>");
            autoLabel13.setText("");
            autoLabel14.setText("");
            autoLabel15.setText("");
            autoLabel16.setText("");
            autoLabel17.setText("");
            autoLabel18.setText("");

        }
        if (jComboBox7.getSelectedIndex() == 3) {
            autoLabel1.setText("<mon site>");
            autoLabel2.setText("<avis google>");
            autoLabel3.setText("<mon facebook>");
            autoLabel4.setText("<avis trip advisor>");
            autoLabel5.setText("<mon instagram>");
            autoLabel6.setText("<autre lien>");
            autoLabel7.setText("");
            autoLabel8.setText("");
            autoLabel9.setText("");
            autoLabel10.setText("");
            autoLabel11.setText("");
            autoLabel12.setText("");
            autoLabel13.setText("");
            autoLabel14.setText("");
            autoLabel15.setText("");
            autoLabel16.setText("");
            autoLabel17.setText("");
            autoLabel18.setText("");
        }

    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        if (jComboBox8.getSelectedIndex() == 0) {
            autoLabelb1.setText("<genre>");
            autoLabelb2.setText("<ville>");
            autoLabelb3.setText("<prénom>");
            autoLabelb4.setText("<commune dlg>");
            autoLabelb5.setText("<nom>");
            autoLabelb6.setText("<cp>");
            autoLabelb7.setText("<tel>");
            autoLabelb8.setText("<adresse1>");
            autoLabelb9.setText("<tel2>");
            autoLabelb10.setText("<adresse2>");
            autoLabelb11.setText("<email>");
            autoLabelb12.setText("<pays>");
            autoLabelb13.setText("");
            autoLabelb14.setText("");
            autoLabelb15.setText("");
            autoLabelb16.setText("");
            autoLabelb17.setText("");
            autoLabelb18.setText("");
        }
        if (jComboBox8.getSelectedIndex() == 1) {
            autoLabelb1.setText("<adulte");
            autoLabelb2.setText("<enfant>");
            autoLabelb3.setText("<lit double>");
            autoLabelb4.setText("<lit simple>");
            autoLabelb5.setText("<type de location>");
            autoLabelb6.setText("<nom de location>");
            autoLabelb7.setText("<adresse loc>");
            autoLabelb8.setText("<nuit>");
            autoLabelb9.setText("<arrivée>");
            autoLabelb10.setText("<départ>");
            autoLabelb11.setText("<prix jour>");
            autoLabelb12.setText("<montant>");
            autoLabelb13.setText("<arrhes>");
            autoLabelb14.setText("<caution>");
            autoLabelb15.setText("<option>");
            autoLabelb16.setText("<total option>");
            autoLabelb17.setText("<taxe de séjour>");
            autoLabelb18.setText("<TOTAL>");
        }

        if (jComboBox8.getSelectedIndex() == 2) {
            autoLabelb1.setText("<ma compagnie>");
            autoLabelb2.setText("<ma rue>");
            autoLabelb3.setText("<mon prénom>");
            autoLabelb4.setText("<ma commune déléguée>");
            autoLabelb5.setText("<mon nom>");
            autoLabelb6.setText("<mon cp>");
            autoLabelb7.setText("<mon tel>");
            autoLabelb8.setText("<ma ville>");
            autoLabelb9.setText("<mon email>");
            autoLabelb10.setText("<mon pays>");
            autoLabelb11.setText("<mon siret>");
            autoLabelb12.setText("<mon APE>");
            autoLabelb13.setText("");
            autoLabelb14.setText("");
            autoLabelb15.setText("");
            autoLabelb16.setText("");
            autoLabelb17.setText("");
            autoLabelb18.setText("");

        }
        if (jComboBox8.getSelectedIndex() == 3) {
            autoLabelb1.setText("<mon site>");
            autoLabelb2.setText("<avis google>");
            autoLabelb3.setText("<mon facebook>");
            autoLabelb4.setText("<avis trip advisor>");
            autoLabelb5.setText("<mon instagram>");
            autoLabelb6.setText("<autre lien>");
            autoLabelb7.setText("");
            autoLabelb8.setText("");
            autoLabelb9.setText("");
            autoLabelb10.setText("");
            autoLabelb11.setText("");
            autoLabelb12.setText("");
            autoLabelb13.setText("");
            autoLabelb14.setText("");
            autoLabelb15.setText("");
            autoLabelb16.setText("");
            autoLabelb17.setText("");
            autoLabelb18.setText("");
        }
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
//booking status is set to finished
        String bkgType = "";

        int index = jTableb3.getSelectedRow();
        String IDstrg = jTableb3.getValueAt(index, 0).toString();
        bookingID = Integer.parseInt(IDstrg);

        ResultSet rs = select.getData(db, "SELECT * FROM  booking where ID='" + bookingID + "'");

        try {
            while (rs.next()) {
                bkgType = rs.getString("origin");
                jTextFieldb55.setText(String.valueOf(rs.getString("amountExternal")));
                jTextFieldb57.setText(String.valueOf(rs.getString("externalDue")));
                jTextFieldb56.setText(String.valueOf(rs.getString("externalPaid")));
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        if (!bkgType.equals("Direct")) {
            bookingStatus = 2;
            externalBookingTitleLabel.setText("Paiement " + bkgType);
            bookingExternalPanel.setVisible(true);
            previousBookingPanel.setVisible(false);
            if (!jTextFieldb57.equals("0")) {
                jTextFieldb64.setEditable(true);
            }
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        //booking status is set to 3: booing finish, paiment for a client
        bookingStatus = 3;
        bookingExternalPanel.setVisible(true);
        previousBookingPanel.setVisible(false);
        int index = jTableb3.getSelectedRow();
        String IDstrg = jTableb3.getValueAt(index, 0).toString();
        bookingID = Integer.parseInt(IDstrg);
        externalBookingTitleLabel.setText("Paiement Client");
        ResultSet rs = select.getData(db, "SELECT * FROM  booking where ID='" + bookingID + "'");

        try {
            while (rs.next()) {
                jTextFieldb55.setText(String.valueOf(rs.getString("totalWithTax")));
                jTextFieldb57.setText(String.valueOf(rs.getString("toPay")));
                jTextFieldb56.setText(String.valueOf(rs.getString("paid")));
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        if (!jTextFieldb57.equals("0")) {
            jTextFieldb64.setEditable(true);
        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabelb135FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabelb135FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelb135FocusGained

    private void jLabelb129MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb129MouseClicked
        bookingPanelsNotVisible();
        bookingLabelNotVisible();
        jLabelb135.setVisible(true);
        statPanel.setVisible(true);
        jComboBox1.setSelectedIndex(0);
    }//GEN-LAST:event_jLabelb129MouseClicked

    private void jLabelb138MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb138MouseEntered
        jLabelb138.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabelb138.setForeground(new Color(0, 153, 204));
    }//GEN-LAST:event_jLabelb138MouseEntered

    private void jLabelb142MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb142MouseEntered
        jLabelb142.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabelb142.setForeground(new Color(0, 153, 204));
    }//GEN-LAST:event_jLabelb142MouseEntered

    private void jLabelb103MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb103MouseEntered
        jLabelb103.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabelb103.setForeground(new Color(0, 153, 204));
    }//GEN-LAST:event_jLabelb103MouseEntered

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        jLabel4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel4.setForeground(new Color(0, 153, 204));
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseEntered
        jLabel3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel3.setForeground(new Color(0, 153, 204));
    }//GEN-LAST:event_jLabel3MouseEntered

    private void jLabelb138MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb138MouseExited
        jLabelb138.setForeground(new Color(102, 102, 102));
        jLabelb138.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jLabelb138MouseExited

    private void jLabelb142MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb142MouseExited
        jLabelb142.setForeground(new Color(102, 102, 102));
        jLabelb142.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jLabelb142MouseExited

    private void jLabelb103MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelb103MouseExited
        jLabelb103.setForeground(new Color(102, 102, 102));
        jLabelb103.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jLabelb103MouseExited

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        jLabel4.setForeground(new Color(102, 102, 102));
        jLabel4.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jLabel4MouseExited

    private void jLabel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseExited
        jLabel3.setForeground(new Color(102, 102, 102));
        jLabel3.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jLabel3MouseExited

    private void bookingPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookingPanelMouseEntered
        bookingMenuPanel.setVisible(false);
    }//GEN-LAST:event_bookingPanelMouseEntered

    private void buildbillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buildbillMouseClicked
        bill.build(db, bookingID);
        bill.open(db, bookingID);
        bookingMenuPanel.setVisible(false);
    }//GEN-LAST:event_buildbillMouseClicked

    private void buildbillMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buildbillMouseEntered
        bookingMenuPanel.setVisible(true);
        buildbill.setForeground(new Color(0, 153, 204));
        buildbill.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_buildbillMouseEntered

    private void buildbillMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buildbillMouseExited
        buildbill.setForeground(new Color(102, 102, 102));
        buildbill.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_buildbillMouseExited

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //if email address is a gmail account handle gmail service else open smtp serveur seting window
        
        if(!myEmailField.getText().isEmpty()){
        if (myEmailField.getText().contains("@gmail")) {
            GmailOauth.deleteToken();

            try {
                GmailService();
            } catch (IOException ex) {
                Logger.getLogger(mainPage.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GeneralSecurityException ex) {
                Logger.getLogger(mainPage.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //handle case where email is not a gmail account
            //first add host and port column in myInfo table in the database
            InsertUpdateDelete.setData(db, "ALTER TABLE myInfo ADD COLUMN IF NOT EXISTS host VARCHAR(50)", "");
            InsertUpdateDelete.setData(db, "ALTER TABLE myInfo ADD COLUMN IF NOT EXISTS port VARCHAR(25)", "");
            ResultSet rs = select.getData(db, "select * from myInfo where ID= 1");
            try {
                if (rs.next()) {
                    String port= "";
                    String host= "";
                    String password= "";
                    //check if emailfield is same as the email in database  
                    if(myEmailField.getText().equals(rs.getString("email"))  ){
                        port= rs.getString("port");
                        host= rs.getString("host");
                        //handle edgecase where password in database is null andput a emptystring instead;
                        if(rs.getString("password")==null){
                            password="";
                        }else{
                        password= rs.getString("password");
                        }
                        
                    }
                    else{
                        //read email and gess setings
                        setSMTP smtp= new setSMTP(myEmailField.getText());
                        host= smtp.getHost();
                        port= smtp.getPort();
                        
                        
                    }
                    
                    jTextField1.setText(host);
                    jTextField2.setText(port);
                    jPasswordField1.setText(password);


                }else{
                  //if Id 1 not yet set in myInfo table  
                   setSMTP smtp= new setSMTP(myEmailField.getText());
                        String host= smtp.getHost();
                        String port= smtp.getPort();
                        jTextField1.setText(host);
                        jTextField2.setText(port);
                }
                rs.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            smtpWindow.setVisible(true);
        }
        }else{
            JOptionPane.showMessageDialog(null, "Adresse e-mail non renseigné");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String host = jTextField1.getText();
        String port = jTextField2.getText();
        String password = StringFormatter.simpleClean(jPasswordField1.getText());

        InsertUpdateDelete.setData(db, "UPDATE myInfo SET host='" + host + "', port='" + port + "', password='" + password + "' WHERE ID=1", "Parametre Enregistré avec succès");
        smtpWindow.setVisible(false);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        smtpWindow.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    String host= jTextField1.getText();
    String port= jTextField2.getText();
    String password= jPasswordField1.getText();
    String email= myEmailField.getText();
        standardOperation.runTest(host, port, password, email);
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AdressHouseLabel;
    private javax.swing.JTextField AdressHouseTextField;
    private javax.swing.JLabel NewContractLabel;
    private javax.swing.JPanel addAmountPanel;
    private javax.swing.JButton addDormButton;
    private javax.swing.JButton addHouseButton;
    private javax.swing.JButton addRoomButton;
    private javax.swing.JTextField address1Field;
    private javax.swing.JTextField address2Field;
    private javax.swing.JTextField adressDormTextField;
    private javax.swing.JLabel adressDormTextField1;
    private javax.swing.JTextField adressRoomTextField;
    private javax.swing.JTextField adultField;
    private javax.swing.JLabel advanceLabel;
    private javax.swing.JLayeredPane allBookingPanel;
    private javax.swing.JLabel amountDormLabel;
    private javax.swing.JTextField amountDormTextField;
    private javax.swing.JTextField amountField;
    private javax.swing.JLabel amountHouseLabel;
    private javax.swing.JTextField amountHouseTextField;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JLabel amountRoomLabel;
    private javax.swing.JTextField amountRoomTextField;
    private javax.swing.JLabel autoLabel1;
    private javax.swing.JLabel autoLabel10;
    private javax.swing.JLabel autoLabel11;
    private javax.swing.JLabel autoLabel12;
    private javax.swing.JLabel autoLabel13;
    private javax.swing.JLabel autoLabel14;
    private javax.swing.JLabel autoLabel15;
    private javax.swing.JLabel autoLabel16;
    private javax.swing.JLabel autoLabel17;
    private javax.swing.JLabel autoLabel18;
    private javax.swing.JLabel autoLabel2;
    private javax.swing.JLabel autoLabel3;
    private javax.swing.JLabel autoLabel4;
    private javax.swing.JLabel autoLabel5;
    private javax.swing.JLabel autoLabel6;
    private javax.swing.JLabel autoLabel7;
    private javax.swing.JLabel autoLabel8;
    private javax.swing.JLabel autoLabel9;
    private javax.swing.JLabel autoLabelb1;
    private javax.swing.JLabel autoLabelb10;
    private javax.swing.JLabel autoLabelb11;
    private javax.swing.JLabel autoLabelb12;
    private javax.swing.JLabel autoLabelb13;
    private javax.swing.JLabel autoLabelb14;
    private javax.swing.JLabel autoLabelb15;
    private javax.swing.JLabel autoLabelb16;
    private javax.swing.JLabel autoLabelb17;
    private javax.swing.JLabel autoLabelb18;
    private javax.swing.JLabel autoLabelb2;
    private javax.swing.JLabel autoLabelb3;
    private javax.swing.JLabel autoLabelb4;
    private javax.swing.JLabel autoLabelb5;
    private javax.swing.JLabel autoLabelb6;
    private javax.swing.JLabel autoLabelb7;
    private javax.swing.JLabel autoLabelb8;
    private javax.swing.JLabel autoLabelb9;
    private javax.swing.JCheckBox blackListCheckBox;
    private javax.swing.JPanel booking11;
    private javax.swing.JCheckBox bookingBlackListCheckBox;
    private javax.swing.JTextField bookingCityField;
    private javax.swing.JTextArea bookingCommentText;
    private javax.swing.JTextField bookingDcField;
    private javax.swing.JTextField bookingEmailField;
    private javax.swing.JLayeredPane bookingExternalPanel;
    private javax.swing.JTextField bookingFirstNameField;
    private javax.swing.JComboBox<String> bookingGenderBox;
    private javax.swing.JTextField bookingIdProofField;
    private javax.swing.JPanel bookingMenuPanel;
    private javax.swing.JTextField bookingNameField;
    private javax.swing.JPanel bookingNewBookingInfoPanel;
    private javax.swing.JLayeredPane bookingOption;
    private javax.swing.JLayeredPane bookingOption2;
    private javax.swing.JLayeredPane bookingPanel;
    private javax.swing.JTextField bookingPostCodeField;
    private javax.swing.JTextField bookingStreet1Field;
    private javax.swing.JTextField bookingStreet2Field;
    private javax.swing.JTextField bookingTel1Field;
    private javax.swing.JTextField bookingTel2Field;
    private javax.swing.JComboBox<String> bookingTypeBox;
    private javax.swing.JLabel bookingTypeLabel;
    private javax.swing.JPanel bookingsPanel;
    private javax.swing.JTextField boundField;
    private javax.swing.JLabel boundHouseLabel;
    private javax.swing.JLabel boundHouseLabel1;
    private javax.swing.JTextField boundHouseTextField;
    private javax.swing.JLabel buildEnvelopLabel;
    private javax.swing.JLabel buildbill;
    private javax.swing.JTextField buildingTextField;
    private org.jdesktop.swingx.JXDatePicker calendarIn;
    private org.jdesktop.swingx.JXDatePicker calendarOut;
    private javax.swing.JLabel cancelBookingLabel;
    private javax.swing.JButton cancelDormButton;
    private javax.swing.JButton cancelHouseButton;
    private javax.swing.JButton cancelMyrentalsButton;
    private javax.swing.JButton cancelRoomButton;
    private javax.swing.JComboBox<String> choosePropertyBox;
    private javax.swing.JTextField cityField;
    private javax.swing.JPanel client;
    private javax.swing.JCheckBox clientBlackListCheckBox;
    private javax.swing.JTextField clientCityField;
    private javax.swing.JTextField clientDcField;
    private javax.swing.JTextField clientEmailField;
    private javax.swing.JTextField clientIdProofField;
    private javax.swing.JTextField clientNameField;
    private javax.swing.JLayeredPane clientPanel;
    private javax.swing.JPanel clientPanel2;
    private javax.swing.JPanel clientPanel3;
    private javax.swing.JTextField clientPostCodeField;
    private javax.swing.JTextField clientStreet1Field;
    private javax.swing.JTextField clientStreet2Field;
    private javax.swing.JTextField clientTel1Field;
    private javax.swing.JTextField clientTel2Field;
    private javax.swing.JTextField clientfirstNameField;
    private javax.swing.JLayeredPane commentsPanelb;
    private javax.swing.JLayeredPane commentsPanelb1;
    private javax.swing.JTextField contratTitle;
    private javax.swing.JTextArea contratTxt;
    private javax.swing.JLayeredPane createDocPanel;
    private javax.swing.JLayeredPane createEmailPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteMyrentalsButton;
    private javax.swing.JTextField districtField;
    private javax.swing.JPanel dormPanel;
    private javax.swing.JLabel doubleBedDormLabel;
    private javax.swing.JTextField doubleBedDormTextField;
    private javax.swing.JLabel doubleBedRToomHouseLabel1;
    private javax.swing.JLabel doubleBedRoomLabel;
    private javax.swing.JTextField doubleBedRoomTextField;
    private javax.swing.JLabel doubleBedStoredLabel;
    private javax.swing.JTextField doubleField;
    private javax.swing.JTextField doublebedRoomHouseTextField;
    private javax.swing.JLayeredPane emailBookingPanel;
    private javax.swing.JTextField emailField;
    private javax.swing.JLayeredPane endBookingPanel;
    private javax.swing.JButton endEmailCancelButton;
    private javax.swing.JButton endEmailCancelButton1;
    private javax.swing.JTextField endEmailObjectField;
    private javax.swing.JTextField endEmailObjectField1;
    private javax.swing.JButton endEmailOkButton;
    private javax.swing.JButton endEmailOkButton1;
    private javax.swing.JTextArea endEmailTextField;
    private javax.swing.JTextArea endEmailTextField1;
    private javax.swing.JButton enterHouseButton;
    private javax.swing.JLayeredPane externalBooking;
    private javax.swing.JLabel externalBookingTitleLabel;
    private javax.swing.JTextField facebookField;
    private javax.swing.JTextField firstNameField;
    private javax.swing.JTextField googletField;
    private javax.swing.JTextField grandTotalField;
    private javax.swing.JPanel helpPanel;
    private javax.swing.JPanel homePanel;
    private javax.swing.JPanel housePanel;
    private javax.swing.JTextField idField;
    private javax.swing.JTextField instaField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButtonb11;
    private javax.swing.JButton jButtonb12;
    private javax.swing.JButton jButtonb13;
    private javax.swing.JButton jButtonb14;
    private javax.swing.JButton jButtonb15;
    private javax.swing.JButton jButtonb16;
    private javax.swing.JButton jButtonb17;
    private javax.swing.JButton jButtonb18;
    private javax.swing.JButton jButtonb20;
    private javax.swing.JButton jButtonb21;
    private javax.swing.JButton jButtonb22;
    private javax.swing.JButton jButtonb23;
    private javax.swing.JButton jButtonb24;
    private javax.swing.JButton jButtonb25;
    private javax.swing.JButton jButtonb26;
    private javax.swing.JButton jButtonb27;
    private javax.swing.JButton jButtonb28;
    private javax.swing.JButton jButtonb29;
    private javax.swing.JButton jButtonb30;
    private javax.swing.JButton jButtonb31;
    private javax.swing.JButton jButtonb32;
    private javax.swing.JButton jButtonb33;
    private javax.swing.JButton jButtonb34;
    private javax.swing.JButton jButtonb35;
    private javax.swing.JButton jButtonb5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBoxb1;
    private javax.swing.JCheckBox jCheckBoxb11;
    private javax.swing.JCheckBox jCheckBoxb2;
    private javax.swing.JCheckBox jCheckBoxb3;
    private javax.swing.JCheckBox jCheckBoxb5;
    private javax.swing.JCheckBox jCheckBoxb6;
    private javax.swing.JCheckBox jCheckBoxb8;
    private javax.swing.JCheckBox jCheckBoxb9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBoxb10;
    private javax.swing.JComboBox<String> jComboBoxb11;
    private javax.swing.JComboBox<String> jComboBoxb12;
    private javax.swing.JComboBox<String> jComboBoxb14;
    private javax.swing.JComboBox<String> jComboBoxb15;
    private javax.swing.JComboBox<String> jComboBoxb2;
    private javax.swing.JComboBox<String> jComboBoxb7;
    private javax.swing.JComboBox<String> jComboBoxb8;
    private javax.swing.JComboBox<String> jComboBoxb9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel180;
    private javax.swing.JLabel jLabel181;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel184;
    private javax.swing.JLabel jLabel185;
    private javax.swing.JLabel jLabel186;
    private javax.swing.JLabel jLabel187;
    private javax.swing.JLabel jLabel188;
    private javax.swing.JLabel jLabel189;
    private javax.swing.JLabel jLabel196;
    private javax.swing.JLabel jLabel198;
    private javax.swing.JLabel jLabel199;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel200;
    private javax.swing.JLabel jLabel201;
    private javax.swing.JLabel jLabel202;
    private javax.swing.JLabel jLabel203;
    private javax.swing.JLabel jLabel204;
    private javax.swing.JLabel jLabel205;
    private javax.swing.JLabel jLabel206;
    private javax.swing.JLabel jLabel207;
    private javax.swing.JLabel jLabel209;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JLabel jLabelBooking;
    private javax.swing.JLabel jLabelBookingSelector;
    private javax.swing.JLabel jLabelClose;
    private javax.swing.JLabel jLabelHome;
    private javax.swing.JLabel jLabelHomeSelector;
    private javax.swing.JLabel jLabelSignOut;
    private javax.swing.JLabel jLabelb1;
    private javax.swing.JLabel jLabelb10;
    private javax.swing.JLabel jLabelb101;
    private javax.swing.JLabel jLabelb102;
    private javax.swing.JLabel jLabelb103;
    private javax.swing.JLabel jLabelb104;
    private javax.swing.JLabel jLabelb105;
    private javax.swing.JLabel jLabelb106;
    private javax.swing.JLabel jLabelb107;
    private javax.swing.JLabel jLabelb108;
    private javax.swing.JLabel jLabelb109;
    private javax.swing.JLabel jLabelb110;
    private javax.swing.JLabel jLabelb111;
    private javax.swing.JLabel jLabelb112;
    private javax.swing.JLabel jLabelb113;
    private javax.swing.JLabel jLabelb114;
    private javax.swing.JLabel jLabelb115;
    private javax.swing.JLabel jLabelb116;
    private javax.swing.JLabel jLabelb117;
    private javax.swing.JLabel jLabelb118;
    private javax.swing.JLabel jLabelb119;
    private javax.swing.JLabel jLabelb120;
    private javax.swing.JLabel jLabelb121;
    private javax.swing.JLabel jLabelb122;
    private javax.swing.JLabel jLabelb123;
    private javax.swing.JLabel jLabelb124;
    private javax.swing.JLabel jLabelb125;
    private javax.swing.JLabel jLabelb126;
    private javax.swing.JLabel jLabelb127;
    private javax.swing.JLabel jLabelb128;
    private javax.swing.JLabel jLabelb129;
    private javax.swing.JLabel jLabelb13;
    private javax.swing.JLabel jLabelb130;
    private javax.swing.JLabel jLabelb131;
    private javax.swing.JLabel jLabelb132;
    private javax.swing.JLabel jLabelb133;
    private javax.swing.JLabel jLabelb134;
    private javax.swing.JLabel jLabelb135;
    private javax.swing.JLabel jLabelb136;
    private javax.swing.JLabel jLabelb137;
    private javax.swing.JLabel jLabelb138;
    private javax.swing.JLabel jLabelb139;
    private javax.swing.JLabel jLabelb140;
    private javax.swing.JLabel jLabelb141;
    private javax.swing.JLabel jLabelb142;
    private javax.swing.JLabel jLabelb143;
    private javax.swing.JLabel jLabelb144;
    private javax.swing.JLabel jLabelb145;
    private javax.swing.JLabel jLabelb146;
    private javax.swing.JLabel jLabelb147;
    private javax.swing.JLabel jLabelb148;
    private javax.swing.JLabel jLabelb149;
    private javax.swing.JLabel jLabelb15;
    private javax.swing.JLabel jLabelb150;
    private javax.swing.JLabel jLabelb151;
    private javax.swing.JLabel jLabelb152;
    private javax.swing.JLabel jLabelb153;
    private javax.swing.JLabel jLabelb154;
    private javax.swing.JLabel jLabelb155;
    private javax.swing.JLabel jLabelb156;
    private javax.swing.JLabel jLabelb157;
    private javax.swing.JLabel jLabelb158;
    private javax.swing.JLabel jLabelb159;
    private javax.swing.JLabel jLabelb16;
    private javax.swing.JLabel jLabelb160;
    private javax.swing.JLabel jLabelb161;
    private javax.swing.JLabel jLabelb162;
    private javax.swing.JLabel jLabelb163;
    private javax.swing.JLabel jLabelb164;
    private javax.swing.JLabel jLabelb165;
    private javax.swing.JLabel jLabelb166;
    private javax.swing.JLabel jLabelb167;
    private javax.swing.JLabel jLabelb168;
    private javax.swing.JLabel jLabelb17;
    private javax.swing.JLabel jLabelb171;
    private javax.swing.JLabel jLabelb172;
    private javax.swing.JLabel jLabelb173;
    private javax.swing.JLabel jLabelb174;
    private javax.swing.JLabel jLabelb175;
    private javax.swing.JLabel jLabelb176;
    private javax.swing.JLabel jLabelb18;
    private javax.swing.JLabel jLabelb181;
    private javax.swing.JLabel jLabelb19;
    private javax.swing.JLabel jLabelb195;
    private javax.swing.JLabel jLabelb2;
    private javax.swing.JLabel jLabelb20;
    private javax.swing.JLabel jLabelb21;
    private javax.swing.JLabel jLabelb22;
    private javax.swing.JLabel jLabelb3;
    private javax.swing.JLabel jLabelb30;
    private javax.swing.JLabel jLabelb31;
    private javax.swing.JLabel jLabelb32;
    private javax.swing.JLabel jLabelb33;
    private javax.swing.JLabel jLabelb34;
    private javax.swing.JLabel jLabelb35;
    private javax.swing.JLabel jLabelb36;
    private javax.swing.JLabel jLabelb37;
    private javax.swing.JLabel jLabelb38;
    private javax.swing.JLabel jLabelb39;
    private javax.swing.JLabel jLabelb4;
    private javax.swing.JLabel jLabelb41;
    private javax.swing.JLabel jLabelb42;
    private javax.swing.JLabel jLabelb44;
    private javax.swing.JLabel jLabelb45;
    private javax.swing.JLabel jLabelb47;
    private javax.swing.JLabel jLabelb5;
    private javax.swing.JLabel jLabelb50;
    private javax.swing.JLabel jLabelb51;
    private javax.swing.JLabel jLabelb52;
    private javax.swing.JLabel jLabelb53;
    private javax.swing.JLabel jLabelb54;
    private javax.swing.JLabel jLabelb55;
    private javax.swing.JLabel jLabelb56;
    private javax.swing.JLabel jLabelb57;
    private javax.swing.JLabel jLabelb58;
    private javax.swing.JLabel jLabelb59;
    private javax.swing.JLabel jLabelb60;
    private javax.swing.JLabel jLabelb61;
    private javax.swing.JLabel jLabelb62;
    private javax.swing.JLabel jLabelb63;
    private javax.swing.JLabel jLabelb64;
    private javax.swing.JLabel jLabelb65;
    private javax.swing.JLabel jLabelb66;
    private javax.swing.JLabel jLabelb67;
    private javax.swing.JLabel jLabelb68;
    private javax.swing.JLabel jLabelb69;
    private javax.swing.JLabel jLabelb7;
    private javax.swing.JLabel jLabelb70;
    private javax.swing.JLabel jLabelb71;
    private javax.swing.JLabel jLabelb72;
    private javax.swing.JLabel jLabelb74;
    private javax.swing.JLabel jLabelb75;
    private javax.swing.JLabel jLabelb76;
    private javax.swing.JLabel jLabelb77;
    private javax.swing.JLabel jLabelb78;
    private javax.swing.JLabel jLabelb79;
    private javax.swing.JLabel jLabelb8;
    private javax.swing.JLabel jLabelb80;
    private javax.swing.JLabel jLabelb81;
    private javax.swing.JLabel jLabelb83;
    private javax.swing.JLabel jLabelb84;
    private javax.swing.JLabel jLabelb85;
    private javax.swing.JLabel jLabelb86;
    private javax.swing.JLabel jLabelb87;
    private javax.swing.JLabel jLabelb88;
    private javax.swing.JLabel jLabelb89;
    private javax.swing.JLabel jLabelb90;
    private javax.swing.JLabel jLabelb91;
    private javax.swing.JLabel jLabelb92;
    private javax.swing.JLabel jLabelb93;
    private javax.swing.JLabel jLabelb94;
    private javax.swing.JLabel jLabelb95;
    private javax.swing.JLabel jLabelb96;
    private javax.swing.JLabel jLabelb97;
    private javax.swing.JLabel jLabelb98;
    private javax.swing.JLabel jLabelb99;
    private javax.swing.JLabel jLabelh2;
    private javax.swing.JLabel jLabelh3;
    private javax.swing.JLabel jLabelh4;
    private javax.swing.JLabel jLabelh5;
    private javax.swing.JLabel jLabelh6;
    private javax.swing.JLabel jLabelh7;
    private javax.swing.JLabel jLabelh8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPaneb1;
    private javax.swing.JScrollPane jScrollPaneb10;
    private javax.swing.JScrollPane jScrollPaneb2;
    private javax.swing.JScrollPane jScrollPaneb3;
    private javax.swing.JScrollPane jScrollPaneb4;
    private javax.swing.JScrollPane jScrollPaneb5;
    private javax.swing.JScrollPane jScrollPaneb9;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTableb1;
    private javax.swing.JTable jTableb3;
    private javax.swing.JTable jTableb7;
    private javax.swing.JTable jTableb8;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextAreab1;
    private javax.swing.JTextArea jTextAreab3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextFieldb26;
    private javax.swing.JTextField jTextFieldb27;
    private javax.swing.JTextField jTextFieldb29;
    private javax.swing.JTextField jTextFieldb35;
    private javax.swing.JTextField jTextFieldb38;
    private javax.swing.JTextField jTextFieldb40;
    private javax.swing.JTextField jTextFieldb41;
    private javax.swing.JTextField jTextFieldb42;
    private javax.swing.JTextField jTextFieldb43;
    private javax.swing.JTextField jTextFieldb44;
    private javax.swing.JTextField jTextFieldb45;
    private javax.swing.JTextField jTextFieldb48;
    private javax.swing.JTextField jTextFieldb49;
    private javax.swing.JTextField jTextFieldb50;
    private javax.swing.JTextField jTextFieldb51;
    private javax.swing.JTextField jTextFieldb52;
    private javax.swing.JTextField jTextFieldb53;
    private javax.swing.JTextField jTextFieldb54;
    private javax.swing.JTextField jTextFieldb55;
    private javax.swing.JTextField jTextFieldb56;
    private javax.swing.JTextField jTextFieldb57;
    private javax.swing.JTextField jTextFieldb61;
    private javax.swing.JTextField jTextFieldb62;
    private javax.swing.JTextField jTextFieldb63;
    private javax.swing.JTextField jTextFieldb64;
    private javax.swing.JTextField jTextFieldb86;
    private javax.swing.JTextField jTextFieldb87;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    private javax.swing.JTextField kidField;
    private javax.swing.JCheckBox linkBillBox;
    private javax.swing.JCheckBox linkBillBox1;
    private javax.swing.JCheckBox linkContractBox;
    private javax.swing.JButton modifyMyrentalsTable;
    private javax.swing.JLayeredPane multipleClients;
    private javax.swing.JLayeredPane multipleClients1;
    private javax.swing.JTextField myApeField;
    private javax.swing.JTextField myCityField;
    private javax.swing.JTextField myCompanyField;
    private javax.swing.JTextField myDcField;
    private javax.swing.JTextField myEmailField;
    private javax.swing.JTextField myFirstNameField;
    private javax.swing.JTextField myLogoField;
    private javax.swing.JTextField myNameField;
    private javax.swing.JLayeredPane myPanel;
    private javax.swing.JTextField myPhone2Field;
    private javax.swing.JTextField myPhoneField;
    private javax.swing.JTextField myPostCodeField;
    private javax.swing.JLayeredPane myRentalPanel;
    private javax.swing.JTable myRentalsTable;
    private javax.swing.JTextField mySiretField;
    private javax.swing.JTextField myStreet1Field;
    private javax.swing.JTextField myStreet2Field;
    private javax.swing.JLabel nameDormLabel;
    private javax.swing.JTextField nameDormTextField;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameHouseLabel;
    private javax.swing.JTextField nameHouseTextField;
    private javax.swing.JTextField nameRoomHouseTextField;
    private javax.swing.JLabel nameRoomLabel;
    private javax.swing.JTextField nameRoomTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPanel newBooking;
    private javax.swing.JPanel newRentalPanel;
    private javax.swing.JLayeredPane optionPanel;
    private javax.swing.JTextField otherReviewField;
    private javax.swing.JTextField paidField;
    private javax.swing.JTextField postCodeField;
    private javax.swing.JLayeredPane previousBookingPanel;
    private javax.swing.JComboBox<String> propertyTypeBox;
    private javax.swing.JLabel qtyRoomHouseLabel;
    private javax.swing.JTextField qtyRoomHouseTextField;
    private javax.swing.JLabel rentalTypeLabel;
    private javax.swing.JPanel roomHousePanel;
    private javax.swing.JPanel roomPanel;
    private javax.swing.JLabel roomStoredLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveRoomHouseButton;
    private javax.swing.JPanel searchClientPanel;
    private javax.swing.JPanel searchClientPanel1;
    private javax.swing.JComboBox<String> selectTypeBox;
    private javax.swing.JLabel sendEmailLabel;
    private javax.swing.JLayeredPane sendEmailPanel;
    private javax.swing.JLayeredPane sendEmailPanel2;
    private javax.swing.JLayeredPane sendEmailPanel3;
    private javax.swing.JLabel singleBedDormLabel;
    private javax.swing.JTextField singleBedDormTextField;
    private javax.swing.JLabel singleBedRoomHouseLabel;
    private javax.swing.JTextField singleBedRoomHouseTextField;
    private javax.swing.JTextField singleBedRoomTextField;
    private javax.swing.JLabel singleBedStoredLabel;
    private javax.swing.JTextField singleField;
    private javax.swing.JLabel singleRoomBedLabel;
    private javax.swing.JInternalFrame smtpWindow;
    private javax.swing.JLayeredPane statMPanel;
    private javax.swing.JLayeredPane statPanel;
    private javax.swing.JLabel taxDormLabel;
    private javax.swing.JTextField taxDormTextField;
    private javax.swing.JTextField taxField;
    private javax.swing.JTextField taxHouseTextField;
    private javax.swing.JLabel taxRoomLabel;
    private javax.swing.JTextField taxRoomTextField;
    private javax.swing.JTextField tel1Field;
    private javax.swing.JTextField tel2Field;
    private javax.swing.JTextField timeInField;
    private javax.swing.JTextField timeOutField;
    private javax.swing.JTextField toPayField;
    private javax.swing.JTextField tripAdvisorField;
    private javax.swing.JLabel typeDormLabel;
    private javax.swing.JTextField typeDormTextField;
    private javax.swing.JLabel typeRoomLabel;
    private javax.swing.JLabel typeRoomLabel1;
    private javax.swing.JTextField websiteField;
    // End of variables declaration//GEN-END:variables
}

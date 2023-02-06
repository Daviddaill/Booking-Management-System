/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package document;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import project.InsertUpdateDelete;
import project.select;

/**
 *
 * @author daviddaillere
 */
public class contract {
    
    //generate contract
    public static void build(String db, int booking) {
        System.out.println("buildcontract");
        document.initFields(db, booking);
        // get rentalName and type of the current booking
        String rentalName = "";
        String type = "";
        ResultSet rs = select.getData(db, "select * from booking where booking.ID='" + booking + "'");
        try {
            while (rs.next()) {
                rentalName = rs.getString("booking.roomName");
                type = rs.getString("bookingType");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        //create the name and path of the pdf contract & create a pdf file
        String home = System.getProperty("user.home");
        String path = home + File.separator + "Documents" + File.separator + "Gérer Mon Gite"+File.separator+db+File.separator+"contrat"+File.separator;
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path + "Contrat " + rentalName + " " + booking + ".pdf"));
            doc.open();
            //create a header table
            PdfPCell cell = new PdfPCell();
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100f);
            String title = "CONTRAT";
            PdfPCell leftCell = new PdfPCell();
            Font f = new Font(Font.FontFamily.HELVETICA, 18.0f, Font.BOLD, BaseColor.LIGHT_GRAY);
            Paragraph addText = new Paragraph(title, f);
            addText.setAlignment(Element.ALIGN_TOP);
            leftCell.addElement(addText);
            Font fmyCompany = new Font(Font.FontFamily.HELVETICA, 14.0f, Font.BOLD, BaseColor.BLACK);
            String myCompany = "<ma compagnie>";
            myCompany = document.replaceInfoFromString(db, booking, myCompany);
            Paragraph company = new Paragraph(myCompany, fmyCompany);
            company.setAlignment(Element.ALIGN_TOP);
            leftCell.addElement(company);
            String myInfo = "<mon site>";
            myInfo = document.replaceInfoFromString(db, booking, myInfo);
            Font fmyInfo = new Font(Font.FontFamily.HELVETICA, 11.0f, Font.NORMAL, BaseColor.BLACK);
            Paragraph myDetail = new Paragraph(myInfo, fmyInfo);
            myDetail.setAlignment(Element.ALIGN_LEFT);
            leftCell.addElement(myDetail);
            leftCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(leftCell);

            PdfPCell rightCell = new PdfPCell();
            if (!document.getLogoName(db).isEmpty()) {
                com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(document.getLogoName(db));
                img.scaleAbsoluteHeight(60f);
                img.scaleAbsoluteWidth(60f);
                img.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(img);
                rightCell.setBorder(Rectangle.NO_BORDER);
            } else {
                rightCell.setBorder(Rectangle.NO_BORDER);
            }

            headerTable.addCell(rightCell);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(headerTable);
            doc.add(headerTable);
            // set all fonts
            Font fline = new Font(Font.FontFamily.HELVETICA, 11.0f, Font.NORMAL, BaseColor.LIGHT_GRAY);
            Font fTitle = new Font(Font.FontFamily.HELVETICA, 9.0f, Font.BOLD, BaseColor.BLACK);
            Font fText = new Font(Font.FontFamily.HELVETICA, 9.0f, Font.NORMAL, BaseColor.BLACK);
            Font fdate = new Font(Font.FontFamily.HELVETICA, 10.0f, Font.NORMAL, BaseColor.BLACK);

            Paragraph line = new Paragraph("_____________________________________________________________________________________\n", fline);
            doc.add(line);

            Paragraph enter = new Paragraph("\n", fText);
            enter.setLeading(11);
            doc.add(enter);
            //set todays date
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            myFormat.format(cal.getTime());
            String date = "Fait le: " + myFormat.format(cal.getTime());
            Paragraph Date = new Paragraph(date, fdate);
            Date.setLeading(11);
            Date.setAlignment(Element.ALIGN_RIGHT);
            doc.add(Date);
            //set contract number
            String contrat = "Numero de Contrat: " + booking;
            Paragraph c = new Paragraph(contrat, fdate);
            c.setLeading(11);
            c.setAlignment(Element.ALIGN_RIGHT);
            doc.add(c);
            //create all contract paragraph
            Paragraph infoTitle = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratInfoTitle"), fTitle);
            infoTitle.setLeading(11);
            doc.add(infoTitle);
            doc.add(enter);

            Paragraph intro = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratIntro"), fText);
            intro.setLeading(11);
            doc.add(intro);
            doc.add(enter);

            Paragraph contratOwnerTitle = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratOwnerTitle"), fTitle);
            contratOwnerTitle.setLeading(11);
            doc.add(contratOwnerTitle);
            //System.out.println("ownertitle done");  
            Paragraph ContratOwner = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratOwner"), fText);
            ContratOwner.setLeading(11);
            doc.add(ContratOwner);
            doc.add(enter);

            Paragraph contratClientTitle = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratClientTitle"), fTitle);
            contratClientTitle.setLeading(11);
            doc.add(contratClientTitle);

            Paragraph ContratClient = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratClient"), fText);
            ContratClient.setLeading(11);
            doc.add(ContratClient);
            doc.add(enter);

            Paragraph ContratPropertyTitle = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratPropertyTitle"), fTitle);
            ContratPropertyTitle.setLeading(11);
            doc.add(ContratPropertyTitle);

            Paragraph ContratProperty = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratProperty"), fText);
            ContratProperty.setLeading(11);
            doc.add(ContratProperty);
            doc.add(enter);

            Paragraph ContratBookingTitle = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratBookingTitle"), fTitle);
            ContratBookingTitle.setLeading(11);
            doc.add(ContratBookingTitle);

            Paragraph ContratBooking = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratBooking"), fText);
            ContratBooking.setLeading(11);
            doc.add(ContratBooking);
            doc.add(enter);
            //if current booking is a house add important information paragraph to the contract
            if (type.equals("Maison")) {
                Paragraph ContratConditionTitle = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratConditionTitle"), fTitle);
                ContratConditionTitle.setLeading(11);
                doc.add(ContratConditionTitle);
                Paragraph ContratCondition = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratCondition"), fText);
                ContratCondition.setLeading(11);
                doc.add(ContratCondition);
                doc.add(enter);
            }

            Paragraph contratSignatureTitle = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratSignatureTitle"), fTitle);
            contratSignatureTitle.setLeading(11);
            doc.add(contratSignatureTitle);

            Paragraph contratSignature = new Paragraph(document.replaceInfoFromDoc(db, booking, "contratSignature"), fText);
            contratSignature.setLeading(11);
            doc.add(contratSignature);
            doc.add(enter);

            doc.close();
            //store the pdf file name in sql booking table;
            String Query = "UPDATE booking  SET contractName = 'Contrat " + rentalName + " " + booking + ".pdf' WHERE ID=" + booking;
            InsertUpdateDelete.setData(db, Query, "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public static void open(String db, int booking) {
        //get the booking's contact file name
        String file = "";
        String home = System.getProperty("user.home");
        home= home + File.separator + "Documents" + File.separator + "Gérer Mon Gite"+File.separator+db+File.separator+"contrat"+File.separator;
        ResultSet rs = select.getData(db, "select * from booking where ID=" + booking);
        try {
            while (rs.next()) {
                file = home+rs.getString("contractName");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        try {
            //open pdf contrat
            File pdfFile = new File(file);
            if (pdfFile.exists()) {

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }

            } else {
                System.out.println("File is not exists!");
            }
            //open pdf general conditions
            File conditions = new File(document.getGeneralConditions(db));
            if (conditions.exists()) {

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(conditions);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }

            } else {
                System.out.println("File is not exists!");
            }

            System.out.println("Done");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void delete(String db, int booking){
        String contrat = "";
        ResultSet rs = select.getData(db, "SELECT * FROM booking WHERE ID='" + booking + "'");
        try {
            while (rs.next()) {
                contrat = rs.getString("contractName");

            }
            rs.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        try {
            Files.delete(Paths.get(contrat));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



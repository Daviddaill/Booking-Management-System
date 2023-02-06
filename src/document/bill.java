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
public class bill {

    public static void build(String db, int booking) {
        
        document.initFields(db, booking);
        String rentalName = "";
        String in = "";
        String out = "";
        int optionAmount = 0;
        int night = 0;
        double price = 0;
        double total = 0;
        double paid = 0;
        double toPay = 0;
        double tax = 0;
        double totalWithTax = 0;
        String optionName = "";
        String daily = "";
        int optionPrice = 0;
        int adult=0;
        int baseTax=0;
        //get current booking data from sql booking table
        ResultSet rs = select.getData(db, "select * from booking where booking.ID='" + booking + "'");
        try {
            while (rs.next()) {
                rentalName = rs.getString("booking.roomName");
                in = new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkIn"));
                out = new SimpleDateFormat("dd-MM-yy").format(rs.getDate("booking.checkOut"));
                night = rs.getInt("booking.numberOfStay");
                price = rs.getFloat("booking.pricePerDay");
                price = Math.round(price * 100.0) / 100.0;
                tax = rs.getFloat("booking.tax");
                tax = Math.round(tax * 100.0) / 100.0;
                optionAmount = rs.getInt("myOption");
                total = rs.getFloat("booking.totalAmount");
                total = Math.round(total * 100.0) / 100.0;
                totalWithTax = rs.getFloat("booking.totalWithTax");
                totalWithTax = Math.round(totalWithTax * 100.0) / 100.0;
                paid = rs.getFloat("booking.paid");
                paid = Math.round(paid * 100.0) / 100.0;
                toPay = rs.getFloat("booking.toPay");
                toPay = Math.round(toPay * 100.0) / 100.0;
              
                adult=rs.getInt("booking.adult");
                baseTax=rs.getInt("booking.baseTax");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        //create path, pdf name and pdf documents
        String home = System.getProperty("user.home");

        String path = home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + File.separator +"facture"+ File.separator;

        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path + "Facture " + rentalName + " " + booking + ".pdf"));
            doc.open();
            PdfPCell cell = new PdfPCell();
            //create the heaer table
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100f);
            String title = "FACTURE";
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
            String myInfo = "<mon email>\n<mon tel>\n<mon site>";
            myInfo = document.replaceInfoFromString(db, booking, myInfo);
            Font fmyInfo = new Font(Font.FontFamily.HELVETICA, 11.0f, Font.NORMAL, BaseColor.BLACK);
            Paragraph myDetail = new Paragraph(myInfo, fmyInfo);
            myDetail.setAlignment(Element.ALIGN_LEFT);
            leftCell.addElement(myDetail);

            String siretApe = "";
            if (!document.getSiret().equals("")) {
                siretApe = "siret: <mon siret>  ";
            }
            if (!document.getAPE().equals("")) {
                siretApe = siretApe + "code APE: <mon APE>";
            }

            siretApe = document.replaceInfoFromString(db, booking, siretApe);
            Font fApe = new Font(Font.FontFamily.HELVETICA, 9.0f, Font.NORMAL, BaseColor.BLACK);
            Paragraph APE = new Paragraph(siretApe, fApe);
            APE.setAlignment(Element.ALIGN_LEFT);
            leftCell.addElement(APE);
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

            Font fline = new Font(Font.FontFamily.HELVETICA, 12.0f, Font.NORMAL, BaseColor.LIGHT_GRAY);
            Paragraph line = new Paragraph("_____________________________________________________________________________\n", fline);
            doc.add(line);
            Paragraph enter = new Paragraph("\n");
            doc.add(enter);
            //set todays date
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            myFormat.format(cal.getTime());
            String date = "Fait le: " + myFormat.format(cal.getTime());
            Paragraph p2 = new Paragraph(date);
            doc.add(p2);

            //set bill number
            String bill = "Numero de Facture: " + booking;
            Paragraph p2b = new Paragraph(bill);
            doc.add(p2b);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            //create bill paragraphs
            PdfPTable tb6 = new PdfPTable(2);
            tb6.setWidthPercentage(100f);
            PdfPCell lcell = new PdfPCell();
            lcell.setPaddingRight(20);
            String infoBooking = "Information Réservation";
            Font fbold = new Font(Font.FontFamily.HELVETICA, 12.0f, Font.BOLD, BaseColor.BLACK);
            Font f1 = new Font(Font.FontFamily.HELVETICA, 11.0f, Font.NORMAL, BaseColor.BLACK);
            Paragraph pbooking = new Paragraph(infoBooking, fbold);
            pbooking.setAlignment(Element.ALIGN_LEFT);
            lcell.addElement(pbooking);

            String bookingDetail = "Location: <nom location> \nType de Logement: <type de location>\nadresse:\n<adresse loc>\nadulte: <adulte> - enfant: <enfant>\nOption(s): <option> ";
            bookingDetail = document.replaceInfoFromString(db, booking, bookingDetail);
            Paragraph pBD = new Paragraph(bookingDetail, f1);
            pBD.setAlignment(Element.ALIGN_LEFT);
            pBD.setLeading(12);
            lcell.addElement(pBD);
            lcell.setBorder(Rectangle.NO_BORDER);
            tb6.addCell(lcell);

            PdfPCell rcell = new PdfPCell();
            rcell.setPaddingLeft(20);
            String client = "Information Client";
            Paragraph pclient = new Paragraph(client, fbold);
            pclient.setAlignment(Element.ALIGN_LEFT);
            rcell.addElement(pclient);

            String customerDetail = "<genre> <prénom> <nom>\n<tel>\n<email>\n<adresse1> <adresse2> <commune dlg>,\n<cp>, <ville>\n";
            customerDetail = document.replaceInfoFromString(db, booking, customerDetail);
            Paragraph p1 = new Paragraph(customerDetail, f1);
            p1.setAlignment(Element.ALIGN_LEFT);
            p1.setLeading(12);
            rcell.addElement(p1);
            rcell.setBorder(Rectangle.NO_BORDER);
            tb6.addCell(rcell);

            doc.add(tb6);

            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            
            PdfPTable tb1 = new PdfPTable(9);

            tb1.setWidthPercentage(100f);
            tb1.addCell("Arrivée ");
            tb1.addCell("Départ: ");
            tb1.addCell("Nbre de Nuit ");
            tb1.addCell("Montant séjour ");
            tb1.addCell("Montant option(s)");
            tb1.addCell("Total HT");
            tb1.addCell("TVA 10%");
            tb1.addCell("Montant TTC");
            tb1.addCell("Taxe de séjour");
            
            double sejourHT= total-(total/10);
            double optionHT= optionAmount-(optionAmount/10);
            double ht= sejourHT+optionHT;
            double tva = (total+ optionAmount)/ 10;
            tva= Math.round(tva * 100.0) / 100.0;
            double ttc= ht+tva;
            
            doc.add(tb1);
            PdfPTable tb2 = new PdfPTable(9);
            tb2.setWidthPercentage(100f);
            tb2.addCell(in);
            tb2.addCell(out);
            tb2.addCell(String.valueOf(night));
            tb2.addCell(String.valueOf(sejourHT));
            tb2.addCell(String.valueOf(optionHT));
            tb2.addCell(String.valueOf(ht));
            tb2.addCell(String.valueOf(tva));
            tb2.addCell(String.valueOf(ttc));
            tb2.addCell(String.valueOf(tax));
            
            
            tb2.addCell(String.valueOf(Math.round(tva * 100.0) / 100.0));
            doc.add(tb2);

            PdfPTable tb5 = new PdfPTable(8);
            tb5.setWidthPercentage(100f);
            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(Rectangle.NO_BORDER);
            tb5.addCell(emptyCell);
            tb5.addCell(emptyCell);
            tb5.addCell(emptyCell);
            tb5.addCell(emptyCell);
            tb5.addCell(emptyCell);
            tb5.addCell(emptyCell);
            tb5.addCell(emptyCell);
            tb5.addCell(emptyCell);

            doc.add(tb5);

            PdfPTable tb7 = new PdfPTable(4);
            tb7.setWidthPercentage(100f);

            tb7.addCell(emptyCell);
            tb7.addCell(emptyCell);
            tb7.addCell(emptyCell);



            PdfPCell toPayvcell = new PdfPCell();
            Paragraph pvtoPay = new Paragraph("Net à payer " + String.valueOf(totalWithTax) + " € ");
            pvtoPay.setAlignment(Element.ALIGN_LEFT);
            toPayvcell.addElement(pvtoPay);
            toPayvcell.setBorder(Rectangle.NO_BORDER);
            tb7.addCell(toPayvcell);
            
            doc.add(tb7);
            
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);
            doc.add(enter);


            doc.add(line);
            Paragraph paragraph6 = new Paragraph("\t Merci de Votre Visite,  En Esperant Vous Revoir Bientôt.\n");
            paragraph6.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraph6);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        doc.close();
        //store pdf name in sql booking table database
        String Query = "UPDATE booking  SET factureName = 'Facture " + rentalName + " " + booking + ".pdf' WHERE ID=" + booking;
        InsertUpdateDelete.setData(db, Query, "");
    }

    public static void open(String db, int booking) {
        //search the pdf file name of the current booking in the sql booking table
        String bill = "";
        String home = System.getProperty("user.home");
        home = home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + File.separator + "facture"+ File.separator;
        ResultSet rs = select.getData(db, "select * from booking where ID=" + booking);
        try {
            while (rs.next()) {
                bill = home + rs.getString("factureName");

            }
            System.out.println(bill);
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        //open pdf if exist
        try {
            File pdfFile = new File(bill);
            if (pdfFile.exists()) {

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
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
}

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
import javax.swing.JOptionPane;
import project.select;

/**
 *
 * @author daviddaillere
 */
public class envelop {
    public static void build(String db, int booking) {
        document.initFields(db, booking);
        String name = "";
        String firstName = "";
        ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID where booking.ID='" + booking + "'");
        try {
            while (rs.next()) {
                name = rs.getString("client.name");
                firstName = rs.getString("client.firstName");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        String home = System.getProperty("user.home");
        String path = home + File.separator + "Documents" + File.separator + "Gérer Mon Gite"+File.separator+db+ File.separator+"enveloppe"+ File.separator;
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        doc.setPageSize(new Rectangle(624, 309));
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path + "Enveloppe " + booking + "-" + name + " " + firstName + ".pdf"));
            doc.open();

            PdfPCell cell = new PdfPCell();
            //cell.setFixedHeight(120);
            PdfPTable headerTable = new PdfPTable(new float[]{50, 50});
            headerTable.setWidthPercentage(100f);

            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(leftCell);

            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            //create the paragraph with only non enpty field of client's address
            String myInfo ="";
            String address2= document.getStreet2();
            String dc= document.getDC();
            if(! address2.equals("")&& ! dc.equals("") ){
            myInfo = "\n\n\n\n<genre> <prénom> <nom>\n<adresse1> \n<adresse2>\n<commune dlg>\n<cp> <ville>";
            }
            if( address2.equals("")&& ! dc.equals("") ){
            myInfo = "\n\n\n\n\n<genre> <prénom> <nom>\n<adresse1>\n<commune dlg>\n<cp> <ville>";
            }
            if( address2.equals("")&& dc.equals("") ){
            myInfo = "\n\n\n\n\n<genre> <prénom> <nom>\n<adresse1>\n<cp> <ville>";
            }
            if( ! address2.equals("")&&  dc.equals("") ){
            myInfo = "\n\n\n\n\n<genre> <prénom> <nom>\n<adresse1>\n<adresse2>\n<cp> <ville>";
            }
            myInfo = document.replaceInfoFromString(db, booking, myInfo);
            Font fmyInfo = new Font(Font.FontFamily.HELVETICA, 14.0f, Font.NORMAL, BaseColor.BLACK);
            Paragraph myDetail = new Paragraph(myInfo, fmyInfo);
            myDetail.setAlignment(Element.ALIGN_BOTTOM);
            rightCell.addElement(myDetail);

            headerTable.addCell(rightCell);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.addElement(headerTable);
            doc.add(headerTable);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        doc.close();

        try {
            File pdfFile = new File(path + "Enveloppe " + booking + "-" + name + " " + firstName + ".pdf");
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

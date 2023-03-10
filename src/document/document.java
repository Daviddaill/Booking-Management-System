/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package document;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import static java.lang.Math.floor;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import project.InsertUpdateDelete;
import project.select;

/**
 *
 * @author daviddaillere
 */
public class document {

    private static String text = "";
    private static String gender = "";
    private static String name = "";
    private static String firstName = "";
    private static String tel = "";
    private static String tel2 = "";
    private static String street = "";
    private static String street2 = "";
    private static String district = "";
    private static String cp = "";
    private static String city = "";
    private static String country = "";
    private static String email = "";
    private static String nationality = "";
    private static String idNumber = "";
    private static String rentalName = "";
    private static String type = "";
    private static String in = "";
    private static String out = "";
    private static String timeIn = "";
    private static String timeOut = "";
    private static String bookingStrg = "";
    private static String website = "";
    private static String facebook = "";
    private static String instagram = "";
    private static String google = "";
    private static String tripAdvisor = "";
    private static String otherReview = "";
    private static String addressProperty = "";
    private static ArrayList<String> optionList;
    private static String option = "";
    private static int adult = 0;
    private static int kid = 0;
    private static int singleBed = 0;
    private static int doubleBed = 0;
    private static int bound = 0;
    private static int night = 0;
    private static int totalOption = 0;
    private static double price = 0;
    private static double total = 0;
    private static double totalWithTax = 0;
    private static double paid = 0;
    private static double toPay = 0;
    private static double tax = 0;
    private static double advance = 0;
    private static String myCompany = "";
    private static String myName = "";
    private static String myfirstName = "";
    private static String myTel = "";
    private static String myEmail = "";
    private static String myStreet = "";
    private static String myDistrict = "";
    private static String myCp = "";
    private static String myCity = "";
    private static String myCountry = "";
    private static String myIdCompany = "";
    private static String myAPE = "";

    private static String logoName;

    public static void setLogoName(String db, String thisLogoName) {
        logoName = thisLogoName;
        String query = "insert into storedInfo ( name, info) values('logoName','" + logoName + "')";
        InsertUpdateDelete.setData(db, query, "");

    }

    public static String getLogoName(String db) {
        String lgName = "";
        String home = System.getProperty("user.home");
        home= home + File.separator + "Documents" + File.separator + "G??rer Mon Gite"+File.separator+db+File.separator+"image/";
        System.out.println("check logo name");
        ResultSet rs = select.getData(db, "select * from storedInfo where name= 'logoName' ");
        try {
            while (rs.next()) {
                lgName = home+rs.getString("info");
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        logoName = lgName;
        return logoName;
    }

    public static void initFields(String db, int booking) {
        optionList = new ArrayList<String>();
        

        ResultSet rs = select.getData(db, "select * from booking  INNER JOIN client ON booking.clientID=client.ID where booking.ID='" + booking + "'");
        ResultSet rs4 = select.getData(db, "select * from bookingOption WHERE bookingID='" + booking + "' ");
        try {
            while (rs.next()) {
                name = rs.getString("client.name");
                firstName = rs.getString("client.firstName");
                if (firstName.isEmpty()) {
                    gender = "Madame et Monsieur";
                } else {
                    gender = rs.getString("client.gender");
                    if (gender.toLowerCase().equals("homme")) {
                        gender = "Monsieur";
                    }
                    if (gender.toLowerCase().equals("femme")) {
                        gender = "Madame";
                    }
                    if (gender.toLowerCase().equals("autre")) {
                        gender = "";
                    }
                }
                tel = rs.getString("client.mobileNumber");
                tel2 = rs.getString("client.tel2");
                email = rs.getString("client.email");
                street = rs.getString("client.street");
                street2 = rs.getString("client.street2");
                district = rs.getString("client.district");
                cp = rs.getString("client.cp");
                city = rs.getString("client.city");
                country = rs.getString("client.country");
                addressProperty = rs.getString("booking.addressProperty");
                in =new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("booking.checkIn"));
                
                
                timeIn = rs.getString("booking.timeIn");
                out= new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("booking.checkOut"));
               
                timeOut = rs.getString("booking.timeOut");
                type = rs.getString("booking.bookingType");
                rentalName = rs.getString("booking.roomName");
                adult = rs.getInt("booking.adult");
                kid = rs.getInt("booking.child");
                singleBed = rs.getInt("booking.singleBed");
                doubleBed = rs.getInt("booking.doubleBed");
                night = rs.getInt("booking.numberOfStay");
                bound = rs.getInt("booking.bound");
                price = rs.getFloat("booking.pricePerDay");
                price = Math.round(price * 100.0) / 100.0;
                tax = rs.getFloat("booking.tax");
                tax = Math.round(tax * 100.0) / 100.0;
                totalOption = rs.getInt("myOption");
                total = rs.getFloat("booking.totalAmount");
                total = Math.round(total * 100.0) / 100.0;
                totalWithTax = rs.getFloat("booking.totalWithTax");
                totalWithTax = Math.round(totalWithTax * 100.0) / 100.0;
                paid = rs.getFloat("booking.paid");
                paid = Math.round(paid * 100.0) / 100.0;
                toPay = rs.getFloat("booking.toPay");
                toPay = Math.round(toPay * 100.0) / 100.0;
                advance= rs.getDouble("advanceAmount");
                bookingStrg = String.valueOf(booking);
                option="";
                
               
            }
            rs.close();

            while (rs4.next()) {
                if (rs4.getString("selected").equals("true")) {
                    optionList.add(rs4.getString("name"));
                }
            }
            rs4.close();
            for (int i = 0; i < optionList.size(); i++) {
                option = option + optionList.get(i) + ", ";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        ResultSet rs2 = select.getData(db, "select * from myInfo where id= 1");
        try {
            while (rs2.next()) {
                myCompany = rs2.getString("company");
                myName = rs2.getString("name");
                myfirstName = rs2.getString("firstName");
                myTel = rs2.getString("tel");
                myEmail = rs2.getString("email");
                myStreet = rs2.getString("street");
                myDistrict = rs2.getString("district");
                myCp = rs2.getString("cp");
                myCity = rs2.getString("city");
                myCountry = rs2.getString("country");
                myIdCompany = rs2.getString("idCompany");
                myAPE = rs2.getString("APEcode");
                website = rs2.getString("website");
                facebook = rs2.getString("facebook");
                instagram = rs2.getString("instagram");
                google = rs2.getString("google");
                tripAdvisor = rs2.getString("tripAdvisor");
                otherReview = rs2.getString("otherReview");
            }
            rs2.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static String replaceInfoFromDoc(String db, int booking, String document) {
       // initFields(db,booking);
        ResultSet rs3 = select.getData(db, "select * from documents where name='" + document + "'");
        try {
            while (rs3.next()) {
                text = rs3.getString("text");
            }
            rs3.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        text = text.replaceAll("<genre>", gender);
        text = text.replaceAll("<nom>", name);
        text = text.replaceAll("<pr??nom>", firstName);
        text = text.replaceAll("<tel>", tel);
        text = text.replaceAll("<tel2>", tel2);
        text = text.replaceAll("<email>", email);
        text = text.replaceAll("<adresse1>", street);
        text = text.replaceAll("<adresse2>", street2);
        text = text.replaceAll("<commune dlg>", district);
        text = text.replaceAll("<cp>", cp);
        text = text.replaceAll("<ville>", city);
        text = text.replaceAll("<pays>", country);
        text = text.replaceAll("<mon site>", website);
        text = text.replaceAll("<mon facebook>", facebook);
        text = text.replaceAll("<mon instagram>", instagram);
        text = text.replaceAll("<avis google>", google);
        text = text.replaceAll("<avis trip advisor>", tripAdvisor);
        text = text.replaceAll("<autre lien>", otherReview);
        text = text.replaceAll("<nom location>", rentalName);
        text = text.replaceAll("<type de location>", type);
        text = text.replaceAll("<arriv??e>", in+" - "+timeIn);
        text = text.replaceAll("<d??part>", out+" - "+timeOut);
        text = text.replaceAll("<adulte>", String.valueOf(adult));
        text = text.replaceAll("<enfant>", String.valueOf(kid));
        text = text.replaceAll("<lit simple>", String.valueOf(singleBed));
        text = text.replaceAll("<lit double>", String.valueOf(doubleBed));
        text = text.replaceAll("<nuit>", String.valueOf(night));
        text = text.replaceAll("<prix jour>", String.valueOf(price));
        text = text.replaceAll("<arrhes>", String.valueOf(advance));
        text = text.replaceAll("<caution>", String.valueOf(bound));
        text = text.replaceAll("<montant>", String.valueOf(total));
        text = text.replaceAll("<taxe de s??jour>", String.valueOf(tax));
        text = text.replaceAll("<total option>", String.valueOf(totalOption));
        text = text.replaceAll("<option>", option);
        text = text.replaceAll("<TOTAL>", String.valueOf(totalWithTax));
        text = text.replaceAll("<ma compagnie>", myCompany);
        text = text.replaceAll("<adresse loc>", addressProperty);
        text = text.replaceAll("<mon nom>", myName);
        text = text.replaceAll("<mon pr??nom>", myfirstName);
        text = text.replaceAll("<mon tel>", myTel);
        text = text.replaceAll("<mon email>", myEmail);
        text = text.replaceAll("<ma rue>", myStreet);
        text = text.replaceAll("<ma commune dlg>", myDistrict);
        text = text.replaceAll("<mon cp>", myCp);
        text = text.replaceAll("<ma ville>", myCity);
        text = text.replaceAll("<mon pays>", myCountry);
        text = text.replaceAll("<mon siret>", myIdCompany);
        text = text.replaceAll("<mon APE>", myAPE);

        return text;
    }

    public static String replaceInfoFromString(String db, int booking, String text) {
       // initFields(db,booking);
        text = text.replaceAll("<genre>", gender);
        text = text.replaceAll("<nom>", name);
        text = text.replaceAll("<pr??nom>", firstName);
        text = text.replaceAll("<tel>", tel);
        text = text.replaceAll("<tel2>", tel2);
        text = text.replaceAll("<email>", email);
        text = text.replaceAll("<adresse1>", street);
        text = text.replaceAll("<adresse2>", street2);
        text = text.replaceAll("<commune dlg>", district);
        text = text.replaceAll("<cp>", cp);
        text = text.replaceAll("<ville>", city);
        text = text.replaceAll("<pays>", country);
        text = text.replaceAll("<mon site>", website);
        text = text.replaceAll("<mon facebook>", facebook);
        text = text.replaceAll("<mon instagram>", instagram);
        text = text.replaceAll("<avis google>", google);
        text = text.replaceAll("<avis trip advisor>", tripAdvisor);
        text = text.replaceAll("<autre lien>", otherReview);
        text = text.replaceAll("<nom location>", rentalName);
        text = text.replaceAll("<type de location>", type);
        text = text.replaceAll("<arriv??e>", in+" "+timeIn);
        text = text.replaceAll("<d??part>", out+" "+timeOut);
        text = text.replaceAll("<adulte>", String.valueOf(adult));
        text = text.replaceAll("<enfant>", String.valueOf(kid));
        text = text.replaceAll("<lit simple>", String.valueOf(singleBed));
        text = text.replaceAll("<lit double>", String.valueOf(doubleBed));
        text = text.replaceAll("<nuit>", String.valueOf(night));
        text = text.replaceAll("<prix jour>", String.valueOf(price));
        text = text.replaceAll("<arrhes>", String.valueOf(advance));
        text = text.replaceAll("<montant>", String.valueOf(total));
        text = text.replaceAll("<taxe de s??jour>", String.valueOf(tax));
        text = text.replaceAll("<total avec taxe>", String.valueOf(totalWithTax));
        text = text.replaceAll("<ma compagnie>", myCompany);
        text = text.replaceAll("<mon tel>", myTel);
        text = text.replaceAll("<mon email>", myEmail);
        text = text.replaceAll("<ma rue>", myStreet);
        text = text.replaceAll("<ma commune dlg>", myDistrict);
        text = text.replaceAll("<mon cp>", myCp);
        text = text.replaceAll("<ma ville>", myCity);
        text = text.replaceAll("<mon pays>", myCountry);
        text = text.replaceAll("<mon siret>", myIdCompany);
        text = text.replaceAll("<mon APE>", myAPE);
        text = text.replaceAll("<mon nom>", myName);
        text = text.replaceAll("<mon pr??nom>", myfirstName);
        text = text.replaceAll("<adresse loc>", addressProperty);
        text = text.replaceAll("<total option>", String.valueOf(totalOption));
        text = text.replaceAll("<option>", option);

        return text;
    }

    public static void createGeneralCondtions(String db) {
        
        ResultSet rs = select.getData(db, "select * from myInfo where id= 1");
        String myCompany = "";
        String website = "";
        String fName="";
        String name="";
        try {
            while (rs.next()) {
                myCompany = rs.getString("company");
                website = rs.getString("website");
                fName= rs.getString("firstName");
                name= rs.getString("name");

            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
         String home = System.getProperty("user.home");
        //String filepath = "documents/" + database + "/sauvegarde/"+database+"_"+sdf+".sql"; 
        String path = home + File.separator + "Documents" + File.separator + "G??rer Mon Gite"+File.separator+db+"/contrat/";
        
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path + "Conditions G??r??rales.pdf"));
            doc.open();
            PdfPCell cell = new PdfPCell();
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100f);

            PdfPCell leftCell = new PdfPCell();
            Font fmyCompany = new Font(Font.FontFamily.HELVETICA, 16.0f, Font.BOLD, BaseColor.LIGHT_GRAY);
            Paragraph company = new Paragraph(myCompany, fmyCompany);
            company.setAlignment(Element.ALIGN_TOP);
            leftCell.addElement(company);
            Font fwebsite = new Font(Font.FontFamily.HELVETICA, 11.0f, Font.NORMAL, BaseColor.BLACK);
            Paragraph site = new Paragraph(website, fwebsite);
            company.setAlignment(Element.ALIGN_TOP);
            leftCell.addElement(site);
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

            Font fline = new Font(Font.FontFamily.TIMES_ROMAN, 11.0f, Font.NORMAL, BaseColor.LIGHT_GRAY);
            Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.BOLD, BaseColor.BLACK);
            Font fRed = new Font(Font.FontFamily.TIMES_ROMAN, 9.0f, Font.BOLD, BaseColor.RED);
            Font fBlue = new Font(Font.FontFamily.TIMES_ROMAN, 9.0f, Font.UNDERLINE, BaseColor.BLUE);
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 9.0f, Font.NORMAL, BaseColor.BLACK);
            Font fBold = new Font(Font.FontFamily.TIMES_ROMAN, 9.0f, Font.BOLD, BaseColor.BLACK);

            Paragraph line = new Paragraph("_____________________________________________________________________________________\n", fline);
            doc.add(line);

            Paragraph enter = new Paragraph("\n", f);
            doc.add(enter);

            Chunk chunk0 = new Chunk("\n", f);
            Chunk chunk1 = new Chunk("                                                                      Conditions g??n??rales de location", fTitle);
            Chunk chunk2 = new Chunk("                                                        A Conserver", fRed);
            Chunk chunk3 = new Chunk("Ce contrat est r??serv?? ?? l'usage exclusif des locations de vacances agr??es ", f);
            Chunk chunk4 = new Chunk(myCompany, fBold);
            Chunk chunk5 = new Chunk(" et seule la loi fran??aise est applicable au contrat. ", f);
            Chunk chunk6 = new Chunk("Dispositions g??n??rales ", fBlue);
            Chunk chunk7 = new Chunk("Le locataire ne pourra en aucune circonstance se pr??valoir d'un quelconque droit au maintien dans les lieux ?? l'expiration de la p??riode de location initialement pr??vue sur le pr??sent contrat, sauf accord du propri??taire. "
                    + "Aucune modification (rature, surcharge)ne sera accept??e dans la r??daction  du contrat sans l'accord des deux parties. ", f);
            Chunk chunk8 = new Chunk("Paiement", fBlue);
            Chunk chunk9 = new Chunk("La r??servation deviendra effective d??s lors que le locataire aura ", f);
            Chunk chunk10 = new Chunk("retourn?? un exemplaire du pr??sent contrat accompagn?? du montant des arrhes, ", fBold);
            Chunk chunk11 = new Chunk("avant la date indiqu??e au verso. ", f);
            Chunk chunk13 = new Chunk("le solde de la location sera vers?? le jour de votre arriv??e, Sauf en cas de r??glement par, ch??que vacances, le solde devra ??tre r??gl?? un mois avant l'arriv??e dans les lieux. ", fBold);
            Chunk chunk16 = new Chunk("D??p??t de garantie (ou caution)", fBlue);
            Chunk chunk17 = new Chunk("Au del?? d'une nuit??e, ", f);
            Chunk chunk19 = new Chunk("le locataire verse ?? son arriv??e un  d??p??t de garantie ", fBold);
            Chunk chunk20 = new Chunk("en plus du solde du loyer. "
                    + "Il sera annul?? dans un d??lai  de 15 jours ?? compter du d??part du locataire, d??duction faite, par le propri??taire des montants ?? charge du locataire aux fins de remise en ??tat des lieux, r??parations diverses. \n"
                    + "Le montant de ces retenues devra ??tre d??ment justifi?? par le propri??taire, devis, facture etc... \n"
                    + "Si le d??p??t de garantie s'av??re insuffisant, le locataire s'engage ?? compl??ter la somme sur la base des justificatifs fournis par le propri??taire. ", f);
            Chunk chunk21 = new Chunk("Utilisation des lieux ", fBlue);
            Chunk chunk22 = new Chunk("le locataire jouira de la location d'une mani??re paisible et en fera bon usage, conform??ment ?? la destination des lieux. "
                    + "A son d??part, le locataire s'engage ?? rendre la location aussi propre qu'il aura trouv?? ?? son arriv??e. "
                    + "L'ensemble du mat??riel figurant ?? l'inventaire, devra ??tre  remis ?? la place qu'il occupait lors de l'entr??e dans les lieux. "
                    + "La location ne peut en aucun cas b??n??ficier ?? des tiers, sauf accord pr??alable du propri??taire. \n"
                    + "La sous location est interdit au preneur, sous quelque pr??texte que ce soit, m??me ?? titre gratuit, sous peine de r??siliation de contrat. "
                    + "Les locaux lou??s sont ?? usage d'habitation provisoire ou de vacances, excluant toute activit?? professionnelle, commerciale ou artisanal. "
                    + "Le propri??taire fournira le logement conforme ?? la description qu'il en ?? faite et le maintiendra en ??tat de servir. ", f);
            Chunk chunk23 = new Chunk("Cas particulier ", fBlue);
            Chunk chunk24 = new Chunk("Le nombre de locataire ne peut ??tre sup??rieur ?? la capacit?? d'accueil indiqu?? sur le contrat, "
                    + "Sauf accord du propri??taire, dans ce cas il sera en droit de percevoir une majoration de prix qui devra ??tre pr??alablement communiqu??e sur le contrat de location. ", f);
            Chunk chunk25 = new Chunk("??tat des lieux et inventaire", fBlue);
            Chunk chunk26 = new Chunk("Un inventaire sera indiqu?? au logement, il devra ??tre v??rifi?? ?? votre arriv??e. "
                    + "le locataire aura 24 heures pour signaler au propri??taire toutes anomalies constat??es. "
                    + "Si le propri??taire constate des d??g??ts apr??s votre d??part, il devra en informer le locataire sous 48 heures. "
                    + "Un ??tat des lieux de chaque pi??ce prendrait trop de temps et d'??nergie sur votre s??jour. "
                    + "Dans le cas d'une anomalie constat??e, le locataire aura 24 heures pour le signaler au propri??taire. ", f);
            Chunk chunk27 = new Chunk("Conditions de r??siliation.", fBlue);
            Chunk chunk28 = new Chunk("Toute r??siliation doit ??tre notifi?? par lettre recommand??e ou t??l??gramme : ", f);
            Chunk chunk29 = new Chunk("Lorsque la r??siliation intervient dans un d??lai de 60 jours avant l'entr??e dans les lieux, le propri??taire restitue dans les 30 jours l'int??gralit?? du montant des arrhes vers??es,"
                    + "Si cette r??siliation intervient dans un d??lai de 30 jours avant l'entr??e dans les lieux, le propri??taire conserve l'int??gralit?? du montant des arrhes vers??es, cependant le locataire a la possibilit?? de reporter la location ?? une autre date et b??n??ficier de nouveau des arrhes d??j?? vers??es, ?? condition que ce report s'effectue dans l'ann??e en cours, sauf accord du propri??taire, "
                    + "En cas de r??siliation du contrat par le propri??taire avant l'entr??e dans les lieux, il reversera au locataire les arrhes per??ues, ", fBold);
            Chunk chunk30 = new Chunk("Interruption du s??jour", fBlue);
            Chunk chunk31 = new Chunk("En cas d'interruption du s??jour anticip?? par le locataire, il ne sera  proc??d?? ?? aucun remboursement, hormis le d??p??t de garantie, "
                    + "En cas de force majeur : si le locataire justifie de motifs graves, l'impossibilit?? de continuer son s??jour, le contrat est r??sili?? de plein droit, le montant de loyers vers??s lui sera restitu?? au prorata de la dur??e de l'occupation qu'il restera ?? effectuer, ", f);
            Chunk chunk32 = new Chunk("Assurance", fBlue);
            Chunk chunk33 = new Chunk("Le locataire est tenue d'assurer le local qui lui est confi??, V??rifier si son contrat d'habitation principal pr??voit l'extension vill??giature (location de vacances), "
                    + " Dans l???hypoth??se contraire il doit intervenir aupr??s de sa compagnie d'assurances et lui r??clamer l'extension ou bien souscrire un contrat  particulier, au titre de clause ?? vill??giature ??, "
                    + "\n"
                    + "Signatures: \n"
                    + "                                           Le propri??taire:                                                                            Le locataire:\n"
                    +"\n"
                    + "                                           "+fName+" "+ name , f);

            Paragraph p = new Paragraph();
            p.add(chunk1);

            p.add(chunk2);
            p.add(chunk0);
            p.add(chunk0);
            p.add(chunk3);
            p.add(chunk4);
            p.add(chunk5);
            p.add(chunk0);
            p.add(chunk6);
            p.add(chunk0);
            p.add(chunk7);
            p.add(chunk0);
            p.add(chunk8);
            p.add(chunk0);
            p.add(chunk9);
            p.add(chunk10);
            p.add(chunk11);

            p.add(chunk13);

            p.add(chunk0);
            p.add(chunk16);
            p.add(chunk0);
            p.add(chunk17);
            p.add(chunk19);
            p.add(chunk20);
            p.add(chunk0);
            p.add(chunk21);
            p.add(chunk0);
            p.add(chunk22);
            p.add(chunk0);
            p.add(chunk23);
            p.add(chunk0);
            p.add(chunk24);
            p.add(chunk0);
            p.add(chunk25);
            p.add(chunk0);
            p.add(chunk26);
            p.add(chunk0);
            p.add(chunk27);
            p.add(chunk0);
            p.add(chunk28);
            p.add(chunk29);
            p.add(chunk0);
            p.add(chunk30);
            p.add(chunk0);
            p.add(chunk31);
            p.add(chunk0);
            p.add(chunk32);
            p.add(chunk0);
            p.add(chunk33);
            p.setLeading(12);
            doc.add(p);
            doc.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public static String getGeneralConditions(String db) {
        String home = System.getProperty("user.home");
        //String filepath = "documents/" + database + "/sauvegarde/"+database+"_"+sdf+".sql"; 
        String path = home + File.separator + "Documents" + File.separator + "G??rer Mon Gite"+File.separator+db+"/contrat/Conditions G??r??rales.pdf";
        return path;
    }
    
    public static String getSiret() {       
        return myIdCompany;
    }
    
    public static String getAPE() {       
        return myAPE;
    }
    public static String getStreet2() {       
        return street2;
    }
    public static String getDC() {       
        return district;
    }
    
    public static String getContratPath(String db){
        String home = System.getProperty("user.home");
        home= home + File.separator + "Documents" + File.separator + "G??rer Mon Gite"+File.separator+db+File.separator+"contrat/";
        return home;
    }
    
     public static String getBillPath(String db){
        String home = System.getProperty("user.home");
        home= home + File.separator + "Documents" + File.separator + "G??rer Mon Gite"+File.separator+db+File.separator+"facture/";
        return home;
    }
     
    public static String getPath(String db, String folder){
        String home = System.getProperty("user.home");
        home= home + File.separator + "Documents" + File.separator + "G??rer Mon Gite"+File.separator+db+File.separator+folder+"/";
        return home;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author daviddaillere
 */
public class tables {
    public static void createTables(String database){
        Connection con= null;
        Statement st= null;
        try{
            
           con= ConnectionProvider.getCon(database);
           st= con.createStatement();
          
           st.executeUpdate("create table IF NOT EXISTS room (ID int NOT NULL,building varchar(200),bookingType varchar(200),roomName varchar(200),roomQte varchar(20), singleBed int, doubleBed int, price  int, bound varchar(20),tax varchar(20),open varchar(20), adress varchar(1000))"  );
           st.executeUpdate("create table IF NOT EXISTS client (ID int  ,name varchar(200),firstName varchar(200), gender varchar(20), mobileNumber varchar(20), tel2 varchar(20), email varchar(200), street varchar(200), street2 varchar(200), district varchar(200), cp varchar(20), city varchar(200), country varchar(200), IDproof varchar(200),times int , blackList varchar(20))" );
           st.executeUpdate("create table IF NOT EXISTS booking (ID int NOT NULL AUTO_INCREMENT ,clientID int,origin varchar(50),  bookingType varchar(200), roomName varchar(200),adult int, child int, singleBed int, doubleBed int, pricePerDay number,checkIn DATE, timeIn varchar(20), checkOut DATE, timeOut varchar(20), numberOfStay int, bound int, myOption int, totalAmount float,amountExternal float, externalDue float, externalPaid float, comment varchar(500), status  varchar(20), contratStatus varchar(20), boundStatus varchar(20), advanceStatus varchar(20), paid float, toPay float,tax float,baseTax float, totalWithTax float,paiementMethod varchar(200),contractName varchar(500), factureName varchar(500), addressProperty varchar(500),advanceAmount varchar(20), PRIMARY KEY (ID))");
           st.executeUpdate("create table IF NOT EXISTS myInfo (ID int  ,company varchar(200),name varchar(200),firstName varchar(200),tel varchar(20),tel2 varchar(20),  email varchar(200), password varchar(200), street varchar(200),street2 varchar(200), district varchar(200), cp varchar(20), city varchar(200), country varchar(200), idCompany varchar(200), website varchar(1000), facebook varchar(1000), instagram varchar(1000), google varchar(1000), tripAdvisor varchar(1000), otherReview varchar(1000), APEcode varchar(50) )" );
           st.executeUpdate("create table IF NOT EXISTS documents ( name varchar(200), bookingType varchar(200),property varchar(200),object varchar(200),text varchar(5000))"  );
           st.executeUpdate("create table IF NOT EXISTS storedInfo (name varchar(20),info varchar(1000),info2 varchar(2000))");
           st.executeUpdate("create table IF NOT EXISTS myOption( ID int NOT NULL AUTO_INCREMENT, name varchar(200),amount varchar(200),dailyRate varchar(200), PRIMARY KEY (ID))");
           st.executeUpdate("create table IF NOT EXISTS bookingOption (bookingID int, name varchar(200), dailyRate varchar(20),amount varchar(20), optionID int, selected varchar(20))");
           st.executeUpdate("create table IF NOT EXISTS externalBooking( ID int NOT NULL AUTO_INCREMENT, name varchar(200),contract  varchar(20),tax varchar(20),advance varchar(20),paiement varchar(20), bound varchar(20), PRIMARY KEY (ID))");
           String text= "<genre> <pr??nom> <nom>, \n \nCeci est un contrat de location pour <pr??nom> <nom>, concernant la location: <nom location> (<type de location>), durant la periode :\ndu <arriv??e> au <d??part>, (soit <nuit> nuit(s) ),\npour <adulte> adulte(s) et <enfant> enfant(s).\nle prix de la location est de <prix jour> ??? /jour pour un montant de <montant> ??? .\nLa taxe de sejour est de <taxe de s??jour> (charge additionel pour toutes personnes suppl??mentaire). \nLe montant total estim?? est de <total avec taxe>??? .\nAfin de confirmer la r??servation, merci de payer <arrhe> ??? ?? l???avance et de retourner ce document dat?? et sign??.\nCordialement.\n<ma compagnie>";
           
           String query= "insert into documents ( name, bookingType, property, text) values('1','gite','all','"+text+"')";
           InsertUpdateDelete.setData(database,query,"");
           
           
           String email1=   "<genre> <pr??nom> <nom>,\n \nJ''ai le plaisir de vous informer que votre demande ?? bien ??t?? enregistr??e.\n" +
                            "Veuillez trouver ci-joint les documents li??s ?? votre r??servation.\n" +
                            "Afin de confirmer votre r??servation, merci de renvoyer un exemplaire rev??tu de votre accord dat?? et sign??, accompagn?? des arrhes.\n" +
                            "En esp??rant avoir le plaisir de vous accueillir tr??s prochainement, je vous adresse mes sinc??res salutations.\n" +
                            "\n" +
                            "Cordialement\n"+
                            "<mon pr??nom>\n"+
                            "\n" +
  
                            "<ma compagnie>\n"+
                            "<mon tel>\n"+
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n"+
                            "<mon site>";
           String query2= "insert into documents ( name, bookingType, property,object, text) values(2, 'all', 'all','Votre r??servation ?? ??t?? enregistr??e','"+email1+"')";
           InsertUpdateDelete.setData(database,query2,"");
           String email2=   "<genre> <pr??nom> <nom>,\n \n"+
                            "Merci de votre visite. Nous ??speront que vous avez pass?? un agr??able s??jour. vous trouverez ci-joint le detail de la facturation.\n"+
                            "En esperant vous revoir bient??t.\n"+
                            "Cordialement\n" +
                            "<mon pr??nom>\n" +
                            "\n"+
                            "<ma compagnie>\n" +
                            "<mon tel>\n" +
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n"+
                            "<mon site>";
           String query3= "insert into documents ( name, bookingType, property,object, text) values(3, 'all', 'all','Merci de votre visite','"+email2+"')";
           InsertUpdateDelete.setData(database,query3,"");
           String email4= "<genre> <pr??nom> <nom>,\n \nJ''ai le plaisir de vous informer avoir bien re??u le contrat et les Arrhes. \nVotre r??servation est par cons??quent confirm??e.\n\nCordialement\n" +
                            "<mon pr??nom>\n" +
                            "\n" +
                            "<ma compagnie>\n" +
                            "<mon tel>\n" +
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n" +
                            "<mon site>";
           //email4= StringFormatter.clean(email4);
           String query4= "insert into documents ( name, bookingType, property,object, text) values(4, 'all', 'all','R??servation Confirm??e','"+email4+"')";
           InsertUpdateDelete.setData(database,query4,"");
           String email5= "<genre> <pr??nom> <nom>,\n \nSuite ?? votre demande, nous avons enregistr?? une r??servation ?? votre nom. Cependant, Nous n''avons ?? ce jour pas encore recus le contrat et/ou les Arrhes. Afin d''??viter l''anulation de votres r??servations, merci de nous retourner les ??lements manquants dans les plus brefs d??lais. Sans retour de votre part, les dates enregistr??es seront de nouveau disponibles\nCordialement\n" +
                        "<mon pr??nom>\n" +
                        "\n" +
                        "<ma compagnie>\n" +
                        "<mon tel>\n" +
                        "<ma rue>, <ma commune dlg>\n" +
                        "<mon cp> <ma ville>\n" +
                        "<mon site>";
           //email5= StringFormatter.clean(email5);
           String query5= "insert into documents ( name, bookingType, property,object, text) values(5, 'all', 'all','Contrat et/ou Arrhes non re??us','"+email5+"')";
           InsertUpdateDelete.setData(database,query5,"");
           
           String email6= "<genre> <pr??nom> <nom>,\n\n\n\n" +
                        "<mon pr??nom>\n" +
                        "\n" +
                        "<ma compagnie>\n" +
                        "<mon tel>\n" +
                        "<ma rue>, <ma commune dlg>\n" +
                        "<mon cp> <ma ville>\n" +
                        "<mon site>";
           //email5= StringFormatter.clean(email5);
           query5= "insert into documents ( name, bookingType, property,object, text) values(6, 'all', 'all','','"+email6+"')";
           InsertUpdateDelete.setData(database,query5,"");
           
           String email7=   "<genre> <pr??nom> <nom>,\n \n"+
                            "Merci de votre visite. Nous ??speront que vous avez pass?? un agr??able s??jour.\n"+
                            "En esperant vous revoir bient??t.\n"+
                            "Cordialement\n" +
                            "<mon pr??nom>\n" +
                            "\n"+
                            "<ma compagnie>\n" +
                            "<mon tel>\n" +
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n"+
                            "<mon site>";
           String query8= "insert into documents ( name, bookingType, property,object, text) values(7, 'all', 'all','Merci de votre visite','"+email7+"')";
           InsertUpdateDelete.setData(database,query8,"");
           
            
           String email8=   "<genre> <pr??nom> <nom>,\n \nJ''ai le plaisir de vous informer que votre r??servation ?? bien ??t?? comfirm??e.\n" +
                            "Dans l''attente de vous accueillir tr??s prochainement, je vous adresse mes sinc??res salutations.\n" +
                            "\n" +
                            "Cordialement\n"+
                            "<mon pr??nom>\n"+
                            "\n" +
  
                            "<ma compagnie>\n"+
                            "<mon tel>\n"+
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n"+
                            "<mon site>";
           String query9= "insert into documents ( name, bookingType, property,object, text) values(8, 'all', 'all','Votre r??servation ?? ??t?? enregistr??e','"+email8+"')";
           InsertUpdateDelete.setData(database,query9,"");
           
           
           String contratInfoTitle= "Contrat de Location";
           String query7= "insert into documents ( name, bookingType, property,text) values('contratInfoTitle','gite','all','"+contratInfoTitle+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratIntro= "<genre> <pr??nom> <nom>,\n\n" +
                            "Suite ?? votre demande de r??servation, j''ai le plaisir de vous adresser le pr??sent contrat de location.\n" +
                            "Afin de confirmer la location, veuillez renvoyer un exemplaire dat?? et sign?? rev??tu de votre accord et accompagn?? du r??glement du montant des arrhes.\n" +
                            "Le second ??xemplaire est ?? conserver.";
                             
           
           String query6= "insert into documents ( name, bookingType, property, text) values('contratIntro','gite','all','"+contratIntro+"')";
           InsertUpdateDelete.setData(database,query6,"");   
           
           String contratOwnerTitle= "Entre le propri??taire (adresse de r??glement)";
           query7= "insert into documents ( name, bookingType, property,text) values('contratOwnerTitle','gite','all','"+contratOwnerTitle+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratOwner= "<mon pr??nom> <mon nom>, \n"+
                                "Adresse: <ma rue>, <ma commune dlg>  <mon cp> <ma ville>.\n"+
                                "T??l??phone: <mon tel> - Email: <mon email> - Siret: <mon siret> - APE: <mon APE>\n";
           query7= "insert into documents ( name, bookingType, property, text) values('contratOwner','gite','all','"+contratOwner+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratPropertyTitle= "Pour la location: ";
            query7= "insert into documents ( name, bookingType, property, text) values('contratPropertyTitle','gite','all','"+contratPropertyTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratProperty= "Nom de la location: <nom location>, type: <type de location> \n"+
                                    "Adresse de la propri??t??: <adresse loc>\n";            
           query7= "insert into documents ( name, bookingType, property,text) values('contratProperty','gite','all','"+contratProperty+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratClientTitle= "Et le locataire: ";
           query7= "insert into documents ( name, bookingType, property, text) values('contratClientTitle','gite','all','"+contratClientTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratClient= "<genre> <pr??nom> <nom> \n"+
                                "Adresse: <adresse1> <adresse2>, <commune dlg>  <cp> <ville>.\n"+
                                "T??l??phone: <tel> - <tel2> Email: <email>";
           query7= "insert into documents ( name, bookingType, property, text) values('contratClient','gite','all','"+contratClient+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratBookingTitle= "D??tail de la r??servation";
           query7= "insert into documents ( name, bookingType, property, text) values('contratBookingTitle','gite','all','"+contratBookingTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratBooking= "Arriv??e: <arriv??e>\n"+
                                  "D??part: <d??part>\n"+
                                "Nombre de nuit: <nuit>\n"+
                                "Nombre d''adutle(s): <adulte> - Enfant(s): <enfant>\n"+
                                "Montant de la location: <TOTAL> ???\n"+
                                "(s??jour: <montant> ??? , taxe de s??jour: <taxe de s??jour> ??? , option(s): <total option> ??? ) \n"+
                                "Option(s): <option>\n"+
                                "Arrhes: <arrhes> ???\n";
                                
           query7= "insert into documents ( name, bookingType, property, text) values('contratBooking','gite','all','"+contratBooking+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratConditionTitle= "Informations Importantes";
           query7= "insert into documents ( name, bookingType, property, text) values('contratConditionTitle','gite','all','"+contratConditionTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratCondition= "Un d??p??t de garantie de <caution> ??? vous sera demand?? ?? votre arriv??e.\n" +
                                "Cette caution sera annul??e dans un d??lai de 15 jours ?? compter du d??part des lieux, d??duction faite des ??ventuelles d??t??riorations ou du co??t de remise en ??tat des lieux.\n" +
                                "Le solde de la location devra ??tre vers?? au plus tard le jour de votre arriv??e, en cas de r??glement par ch??ques vacances, le solde devra ??tre r??gl?? un mois avant votre arriv??e. "+
                                "La confirmation de r??servation prendra effet immediatement apr??s reception ?? mon adresse, d''un exemplaire du pr??sent contrat dat?? et sign?? avec la mention lu et approuv??, " +
                                "accompagn?? du montant des arrhes de (30 % de la location) soit: <arrhes> ???. ";
           query7= "insert into documents ( name, bookingType, property, text) values('contratCondition','gite','all','"+contratCondition+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
            String contratSignatureTitle= "Merci de dater et signer";
           query7= "insert into documents ( name, bookingType, property, text) values('contratSignatureTitle','gite','all','"+contratSignatureTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratSignature= "Le pr??sent contrat est ??tabli en deux exemplaires.\n" +
                                    "J''ai pris connaissance des conditions g??n??rales de locations pr??cis??es ci-joint.\n" +
                                    "\n" +
                                     "\n" +
                                    "\n" +
                                    "                           Le propri??taire:                                                              Le locataire:\n"+
                                    "\n"+
                                    "                           <mon pr??nom> <mon nom>";
           query7= "insert into documents ( name, bookingType, property, text) values('contratSignature','gite','all','"+contratSignature+"')";
           InsertUpdateDelete.setData(database,query7,"");

        }
        
        
        catch(Exception e){
       JOptionPane.showMessageDialog(null, e);
        }
        finally{
         try{
           con.close();
           st.close(); 
        }
        catch(Exception e){
        
        }   
        }
    }
    
    public static void createUsersTable(){
        Connection con= null;
        Statement st= null;
        try{
           con= ConnectionProvider.getCon("admin_database");
           st= con.createStatement();
           st.executeUpdate("create table IF NOT EXISTS users (name varchar(200),email varchar(200),password varchar(50), securityQuestion varchar(500), answer  varchar(200), address  varchar(200),license  varchar(200) , startDate  varchar(200))");
           
           
        }
        catch(Exception e){
       JOptionPane.showMessageDialog(null, e);
        }
        finally{
         try{
           con.close();
           st.close(); 
        }
        catch(Exception e){
        
        }   
        }
    }
}



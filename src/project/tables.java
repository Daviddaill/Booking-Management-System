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
           String text= "<genre> <prénom> <nom>, \n \nCeci est un contrat de location pour <prénom> <nom>, concernant la location: <nom location> (<type de location>), durant la periode :\ndu <arrivée> au <départ>, (soit <nuit> nuit(s) ),\npour <adulte> adulte(s) et <enfant> enfant(s).\nle prix de la location est de <prix jour> € /jour pour un montant de <montant> € .\nLa taxe de sejour est de <taxe de séjour> (charge additionel pour toutes personnes supplémentaire). \nLe montant total estimé est de <total avec taxe>€ .\nAfin de confirmer la réservation, merci de payer <arrhe> € à l’avance et de retourner ce document daté et signé.\nCordialement.\n<ma compagnie>";
           
           String query= "insert into documents ( name, bookingType, property, text) values('1','gite','all','"+text+"')";
           InsertUpdateDelete.setData(database,query,"");
           
           
           String email1=   "<genre> <prénom> <nom>,\n \nJ''ai le plaisir de vous informer que votre demande à bien été enregistrée.\n" +
                            "Veuillez trouver ci-joint les documents liés à votre réservation.\n" +
                            "Afin de confirmer votre réservation, merci de renvoyer un exemplaire revêtu de votre accord daté et signé, accompagné des arrhes.\n" +
                            "En espérant avoir le plaisir de vous accueillir très prochainement, je vous adresse mes sincères salutations.\n" +
                            "\n" +
                            "Cordialement\n"+
                            "<mon prénom>\n"+
                            "\n" +
  
                            "<ma compagnie>\n"+
                            "<mon tel>\n"+
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n"+
                            "<mon site>";
           String query2= "insert into documents ( name, bookingType, property,object, text) values(2, 'all', 'all','Votre réservation à été enregistrée','"+email1+"')";
           InsertUpdateDelete.setData(database,query2,"");
           String email2=   "<genre> <prénom> <nom>,\n \n"+
                            "Merci de votre visite. Nous ésperont que vous avez passé un agréable séjour. vous trouverez ci-joint le detail de la facturation.\n"+
                            "En esperant vous revoir bientôt.\n"+
                            "Cordialement\n" +
                            "<mon prénom>\n" +
                            "\n"+
                            "<ma compagnie>\n" +
                            "<mon tel>\n" +
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n"+
                            "<mon site>";
           String query3= "insert into documents ( name, bookingType, property,object, text) values(3, 'all', 'all','Merci de votre visite','"+email2+"')";
           InsertUpdateDelete.setData(database,query3,"");
           String email4= "<genre> <prénom> <nom>,\n \nJ''ai le plaisir de vous informer avoir bien reçu le contrat et les Arrhes. \nVotre réservation est par conséquent confirmée.\n\nCordialement\n" +
                            "<mon prénom>\n" +
                            "\n" +
                            "<ma compagnie>\n" +
                            "<mon tel>\n" +
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n" +
                            "<mon site>";
           //email4= StringFormatter.clean(email4);
           String query4= "insert into documents ( name, bookingType, property,object, text) values(4, 'all', 'all','Réservation Confirmée','"+email4+"')";
           InsertUpdateDelete.setData(database,query4,"");
           String email5= "<genre> <prénom> <nom>,\n \nSuite à votre demande, nous avons enregistré une réservation à votre nom. Cependant, Nous n''avons à ce jour pas encore recus le contrat et/ou les Arrhes. Afin d''éviter l''anulation de votres réservations, merci de nous retourner les élements manquants dans les plus brefs délais. Sans retour de votre part, les dates enregistrées seront de nouveau disponibles\nCordialement\n" +
                        "<mon prénom>\n" +
                        "\n" +
                        "<ma compagnie>\n" +
                        "<mon tel>\n" +
                        "<ma rue>, <ma commune dlg>\n" +
                        "<mon cp> <ma ville>\n" +
                        "<mon site>";
           //email5= StringFormatter.clean(email5);
           String query5= "insert into documents ( name, bookingType, property,object, text) values(5, 'all', 'all','Contrat et/ou Arrhes non reçus','"+email5+"')";
           InsertUpdateDelete.setData(database,query5,"");
           
           String email6= "<genre> <prénom> <nom>,\n\n\n\n" +
                        "<mon prénom>\n" +
                        "\n" +
                        "<ma compagnie>\n" +
                        "<mon tel>\n" +
                        "<ma rue>, <ma commune dlg>\n" +
                        "<mon cp> <ma ville>\n" +
                        "<mon site>";
           //email5= StringFormatter.clean(email5);
           query5= "insert into documents ( name, bookingType, property,object, text) values(6, 'all', 'all','','"+email6+"')";
           InsertUpdateDelete.setData(database,query5,"");
           
           String email7=   "<genre> <prénom> <nom>,\n \n"+
                            "Merci de votre visite. Nous ésperont que vous avez passé un agréable séjour.\n"+
                            "En esperant vous revoir bientôt.\n"+
                            "Cordialement\n" +
                            "<mon prénom>\n" +
                            "\n"+
                            "<ma compagnie>\n" +
                            "<mon tel>\n" +
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n"+
                            "<mon site>";
           String query8= "insert into documents ( name, bookingType, property,object, text) values(7, 'all', 'all','Merci de votre visite','"+email7+"')";
           InsertUpdateDelete.setData(database,query8,"");
           
            
           String email8=   "<genre> <prénom> <nom>,\n \nJ''ai le plaisir de vous informer que votre réservation à bien été comfirmée.\n" +
                            "Dans l''attente de vous accueillir très prochainement, je vous adresse mes sincères salutations.\n" +
                            "\n" +
                            "Cordialement\n"+
                            "<mon prénom>\n"+
                            "\n" +
  
                            "<ma compagnie>\n"+
                            "<mon tel>\n"+
                            "<ma rue>, <ma commune dlg>\n" +
                            "<mon cp> <ma ville>\n"+
                            "<mon site>";
           String query9= "insert into documents ( name, bookingType, property,object, text) values(8, 'all', 'all','Votre réservation à été enregistrée','"+email8+"')";
           InsertUpdateDelete.setData(database,query9,"");
           
           
           String contratInfoTitle= "Contrat de Location";
           String query7= "insert into documents ( name, bookingType, property,text) values('contratInfoTitle','gite','all','"+contratInfoTitle+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratIntro= "<genre> <prénom> <nom>,\n\n" +
                            "Suite à votre demande de réservation, j''ai le plaisir de vous adresser le présent contrat de location.\n" +
                            "Afin de confirmer la location, veuillez renvoyer un exemplaire daté et signé revêtu de votre accord et accompagné du règlement du montant des arrhes.\n" +
                            "Le second éxemplaire est à conserver.";
                             
           
           String query6= "insert into documents ( name, bookingType, property, text) values('contratIntro','gite','all','"+contratIntro+"')";
           InsertUpdateDelete.setData(database,query6,"");   
           
           String contratOwnerTitle= "Entre le propriétaire (adresse de règlement)";
           query7= "insert into documents ( name, bookingType, property,text) values('contratOwnerTitle','gite','all','"+contratOwnerTitle+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratOwner= "<mon prénom> <mon nom>, \n"+
                                "Adresse: <ma rue>, <ma commune dlg>  <mon cp> <ma ville>.\n"+
                                "Téléphone: <mon tel> - Email: <mon email> - Siret: <mon siret> - APE: <mon APE>\n";
           query7= "insert into documents ( name, bookingType, property, text) values('contratOwner','gite','all','"+contratOwner+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratPropertyTitle= "Pour la location: ";
            query7= "insert into documents ( name, bookingType, property, text) values('contratPropertyTitle','gite','all','"+contratPropertyTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratProperty= "Nom de la location: <nom location>, type: <type de location> \n"+
                                    "Adresse de la propriété: <adresse loc>\n";            
           query7= "insert into documents ( name, bookingType, property,text) values('contratProperty','gite','all','"+contratProperty+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratClientTitle= "Et le locataire: ";
           query7= "insert into documents ( name, bookingType, property, text) values('contratClientTitle','gite','all','"+contratClientTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratClient= "<genre> <prénom> <nom> \n"+
                                "Adresse: <adresse1> <adresse2>, <commune dlg>  <cp> <ville>.\n"+
                                "Téléphone: <tel> - <tel2> Email: <email>";
           query7= "insert into documents ( name, bookingType, property, text) values('contratClient','gite','all','"+contratClient+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratBookingTitle= "Détail de la réservation";
           query7= "insert into documents ( name, bookingType, property, text) values('contratBookingTitle','gite','all','"+contratBookingTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratBooking= "Arrivée: <arrivée>\n"+
                                  "Départ: <départ>\n"+
                                "Nombre de nuit: <nuit>\n"+
                                "Nombre d''adutle(s): <adulte> - Enfant(s): <enfant>\n"+
                                "Montant de la location: <TOTAL> €\n"+
                                "(séjour: <montant> € , taxe de séjour: <taxe de séjour> € , option(s): <total option> € ) \n"+
                                "Option(s): <option>\n"+
                                "Arrhes: <arrhes> €\n";
                                
           query7= "insert into documents ( name, bookingType, property, text) values('contratBooking','gite','all','"+contratBooking+"')";
           InsertUpdateDelete.setData(database,query7,""); 
           
           String contratConditionTitle= "Informations Importantes";
           query7= "insert into documents ( name, bookingType, property, text) values('contratConditionTitle','gite','all','"+contratConditionTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratCondition= "Un dépôt de garantie de <caution> € vous sera demandé à votre arrivée.\n" +
                                "Cette caution sera annulée dans un délai de 15 jours à compter du départ des lieux, déduction faite des éventuelles détériorations ou du coût de remise en état des lieux.\n" +
                                "Le solde de la location devra être versé au plus tard le jour de votre arrivée, en cas de règlement par chèques vacances, le solde devra être réglé un mois avant votre arrivée. "+
                                "La confirmation de réservation prendra effet immediatement après reception à mon adresse, d''un exemplaire du présent contrat daté et signé avec la mention lu et approuvé, " +
                                "accompagné du montant des arrhes de (30 % de la location) soit: <arrhes> €. ";
           query7= "insert into documents ( name, bookingType, property, text) values('contratCondition','gite','all','"+contratCondition+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
            String contratSignatureTitle= "Merci de dater et signer";
           query7= "insert into documents ( name, bookingType, property, text) values('contratSignatureTitle','gite','all','"+contratSignatureTitle+"')";
           InsertUpdateDelete.setData(database,query7,"");
           
           String contratSignature= "Le présent contrat est établi en deux exemplaires.\n" +
                                    "J''ai pris connaissance des conditions générales de locations précisées ci-joint.\n" +
                                    "\n" +
                                     "\n" +
                                    "\n" +
                                    "                           Le propriétaire:                                                              Le locataire:\n"+
                                    "\n"+
                                    "                           <mon prénom> <mon nom>";
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



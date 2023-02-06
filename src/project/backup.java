/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.Date;
import javax.swing.JFileChooser;
import org.h2.store.fs.FileUtils;
import org.h2.tools.RunScript;

/**
 *
 * @author daviddaillere
 */
public class backup {
 
    private String path;
    
    private static String createFileName(String database){
        Date currentDate = new Date();
        String sdf = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String home = System.getProperty("user.home");
        //String filepath = "documents/" + database + "/sauvegarde/"+database+"_"+sdf+".sql";
        String filepath = home + File.separator + "Documents" + File.separator + "Gérer mon gite"+File.separator+database+"/sauvegarde/"+database+"_"+sdf+".zip";
        return filepath;
    }
    
    public static void create(String database){

          InsertUpdateDelete.setData(database, "BACKUP TO '"+createFileName(database)+"';", "");
    }
    
    
    
    
}
    


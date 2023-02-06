
import static email.GmailOauth.setTokenPath;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import static java.awt.SystemColor.window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import project.select;
import java.util.Map.*;
import javax.swing.JLabel;
import java.io.File;
import static java.lang.Math.floor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.ImageIcon;
import org.apache.poi.common.usermodel.Hyperlink;
import project.ConnectionProvider;
import project.InsertUpdateDelete;
import project.*;
import static project.database.setDb;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author daviddaillere
 */
public class Simply extends javax.swing.JFrame {

    /**
     * Creates new form Simply
     */
    private String db;
    //ImageIcon logo= new ImageIcon(".//images//home(1).png"); 

    public Simply() {
        initComponents();
        this.setLocationRelativeTo(null);
        pointerSignIn.setVisible(false);
        pointerConection.setVisible(false);
        pointerPsw.setVisible(false);
        rememberMe();
        conectionLabel.requestFocus();// 'start' will be your start button
    }

    Preferences preference;
    boolean rememberPreference;

    public void rememberMe() {
        preference = Preferences.userNodeForPackage(this.getClass());
        rememberPreference = preference.getBoolean("rememberMe", Boolean.valueOf(""));
        if (rememberPreference) {
            jTextField1.setText(preference.get("user", ""));
            jPasswordField1.setText(preference.get("pswrd", ""));
            jCheckBox1.setSelected(rememberPreference);
        }
    }
    
    public void connect(){
        int check = 0;

        String id = jTextField1.getText().replaceAll("\\s", "");
        String password = jPasswordField1.getText();
        db = jTextField1.getText().replaceAll("\\s", "");
//set the database db name in the class database ( it will be use to refer to the database in use.       
        setDb(db);
        setTokenPath(db);
        if (id.equals("") || password.equals("")) {
            check = 1;
            JOptionPane.showMessageDialog(null, "veuillez renseigner toutes les informations");
        } else if (id.equals("admin_page") && password.equals("admin")) {
            check = 1;
            setVisible(false);
            new adminHome().setVisible(true);
        } else {
            ResultSet rs = select.getData("admin_database", "select * from users where name = '" + id + "' and password = '" + password + "'");
            try {
                if (rs.next()) {
                    check = 1;
                    if (jCheckBox1.isSelected() && !rememberPreference) {
                        preference.put("user", jTextField1.getText());
                        preference.put("pswrd", jPasswordField1.getText());
                        preference.putBoolean("rememberMe", true);

                    } else if (!jCheckBox1.isSelected() && rememberPreference) {
                        preference.put("user", "");
                        preference.put("pswrd", "");
                        preference.putBoolean("rememberMe", false);
                    }

//calculate number of trial days:
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        java.util.Date dateAvant = sdf.parse(rs.getString("startDate"));
                        Calendar calendar = Calendar.getInstance();
                        java.util.Date dateObj = calendar.getTime();
                        String currDate = sdf.format(dateObj);
                        java.util.Date dateApres = sdf.parse(currDate);
                        long diff = dateApres.getTime() - dateAvant.getTime();
                        float res = (diff / (1000 * 60 * 60 * 24));
                        float remainingDays = 31 - res;
                        String status = rs.getString("license");
                        

//verify if license is purchased       
                        if (rs.getString("license").equals("purchased")) {
                            setVisible(false);
                            new mainPage(db).setVisible(true);
                        } else {
//if trial version count number of days  (res is number of days since signup                           
                            if (res <= 31 && res > -1) {
                                JOptionPane.showMessageDialog(null, "Jours d'essai restant: " + remainingDays);
                                setVisible(false);
                                new mainPage(db).setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Periode d'essai terminé, Visitez notre site internet: www.gerer-mon-gite.com ou contactez nous à contact@gerer-mon-gite.com"
                                       
                                         );
                            }
                        }
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String home = System.getProperty("user.home");
                    File file = new File(home + File.separator + "Documents" + File.separator + "Gérer Mon Gite");
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            System.out.println("Directory is created!");
                        } else {
                            System.out.println("Failed to create directory!");
                        }
                    }

                    //To create multiple directories/folders
                    File files = new File(home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + File.separator + "contrat");
                    if (!files.exists()) {
                        if (files.mkdirs()) {
                            System.out.println("Multiple directories are created!");
                        } else {
                            System.out.println("Failed to create multiple directories!");
                        }
                    }

                    files = new File(home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + File.separator + "facture");
                    if (!files.exists()) {
                        if (files.mkdirs()) {
                            System.out.println("Multiple directories are created!");
                        } else {
                            System.out.println("Failed to create multiple directories!");
                        }
                    }
                    files = new File(home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + File.separator + "image");
                    if (!files.exists()) {
                        if (files.mkdirs()) {
                            System.out.println("Multiple directories are created!");
                        } else {
                            System.out.println("Failed to create multiple directories!");
                        }
                    }

                    files = new File(home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + File.separator + "enveloppe");
                    if (!files.exists()) {
                        if (files.mkdirs()) {
                            System.out.println("Multiple directories are created!");
                        } else {
                            System.out.println("Failed to create multiple directories!");
                        }
                    }
                    files = new File(home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + File.separator + "sauvegarde");
                    if (!files.exists()) {
                        if (files.mkdirs()) {
                            System.out.println("Multiple directories are created!");
                        } else {
                            System.out.println("Failed to create multiple directories!");
                        }
                    }
                    files = new File(home + File.separator + "Documents" + File.separator + "Gérer Mon Gite" + File.separator + db + File.separator + "clients");
                    if (!files.exists()) {
                        if (files.mkdirs()) {
                            System.out.println("Multiple directories are created!");
                        } else {
                            System.out.println("Failed to create multiple directories!");
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (check == 0) {
            JOptionPane.showMessageDialog(null, "Mot de Passe ou Email Incorrect");
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

        emailTxt = new javax.swing.JLabel();
        passwordTxt = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jTextField1 = new javax.swing.JTextField();
        titleLabel = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        closeLabel = new javax.swing.JLabel();
        pointerPsw = new javax.swing.JLabel();
        pointerSignIn = new javax.swing.JLabel();
        pointerConection = new javax.swing.JLabel();
        pswrLabel = new javax.swing.JLabel();
        signInLabel = new javax.swing.JLabel();
        conectionLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        backGroundLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(0, 0));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(null);

        emailTxt.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 15)); // NOI18N
        emailTxt.setForeground(new java.awt.Color(255, 255, 255));
        emailTxt.setText("Identifiant");
        getContentPane().add(emailTxt);
        emailTxt.setBounds(150, 240, 85, 19);

        passwordTxt.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 15)); // NOI18N
        passwordTxt.setForeground(new java.awt.Color(255, 255, 255));
        passwordTxt.setText("Mot de Passe");
        getContentPane().add(passwordTxt);
        passwordTxt.setBounds(130, 280, 107, 19);

        jPasswordField1.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jPasswordField1.setForeground(new java.awt.Color(102, 102, 102));
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });
        jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField1KeyPressed(evt);
            }
        });
        getContentPane().add(jPasswordField1);
        jPasswordField1.setBounds(280, 270, 300, 30);

        jTextField1.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(102, 102, 102));
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField1PropertyChange(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        getContentPane().add(jTextField1);
        jTextField1.setBounds(280, 230, 300, 30);

        titleLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 24)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("Connexion");
        getContentPane().add(titleLabel);
        titleLabel.setBounds(280, 170, 137, 30);

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font(".AppleSystemUIFont", 1, 14)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Se souvenir de moi");
        jCheckBox1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCheckBox1FocusLost(evt);
            }
        });
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        getContentPane().add(jCheckBox1);
        jCheckBox1.setBounds(280, 310, 175, 23);

        closeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close_2.png"))); // NOI18N
        closeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeLabelMouseClicked(evt);
            }
        });
        closeLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                closeLabelKeyPressed(evt);
            }
        });
        getContentPane().add(closeLabel);
        closeLabel.setBounds(740, 20, 25, 25);

        pointerPsw.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        pointerPsw.setForeground(new java.awt.Color(255, 255, 255));
        pointerPsw.setText("______________________");
        pointerPsw.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pointerPswFocusLost(evt);
            }
        });
        getContentPane().add(pointerPsw);
        pointerPsw.setBounds(620, 570, 170, 30);

        pointerSignIn.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        pointerSignIn.setForeground(new java.awt.Color(255, 255, 255));
        pointerSignIn.setText("___________");
        pointerSignIn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pointerSignInFocusGained(evt);
            }
        });
        getContentPane().add(pointerSignIn);
        pointerSignIn.setBounds(470, 390, 90, 30);

        pointerConection.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        pointerConection.setForeground(new java.awt.Color(255, 255, 255));
        pointerConection.setText("___________");
        pointerConection.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pointerConectionFocusGained(evt);
            }
        });
        getContentPane().add(pointerConection);
        pointerConection.setBounds(290, 390, 90, 30);

        pswrLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        pswrLabel.setForeground(new java.awt.Color(255, 255, 255));
        pswrLabel.setText("Mot de passe oublié?");
        pswrLabel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pswrLabelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pswrLabelFocusLost(evt);
            }
        });
        pswrLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pswrLabelMouseClicked(evt);
            }
        });
        pswrLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pswrLabelKeyPressed(evt);
            }
        });
        getContentPane().add(pswrLabel);
        pswrLabel.setBounds(620, 570, 153, 17);

        signInLabel.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        signInLabel.setForeground(new java.awt.Color(255, 255, 255));
        signInLabel.setText("Inscription");
        signInLabel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                signInLabelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                signInLabelFocusLost(evt);
            }
        });
        signInLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signInLabelMouseClicked(evt);
            }
        });
        signInLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                signInLabelKeyPressed(evt);
            }
        });
        getContentPane().add(signInLabel);
        signInLabel.setBounds(460, 390, 89, 20);

        conectionLabel.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        conectionLabel.setForeground(new java.awt.Color(255, 255, 255));
        conectionLabel.setText("Connexion");
        conectionLabel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                conectionLabelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                conectionLabelFocusLost(evt);
            }
        });
        conectionLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                conectionLabelMouseClicked(evt);
            }
        });
        conectionLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                conectionLabelKeyPressed(evt);
            }
        });
        getContentPane().add(conectionLabel);
        conectionLabel.setBounds(280, 390, 100, 20);

        jLabel1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 255, 204));
        jLabel1.setText("Gérer Mon Gîte");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 10, 590, 80);

        jLabel2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 255, 255));
        jLabel2.setText("Logiciel de gestion locative simple.");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(30, 80, 550, 30);

        backGroundLabel.setBackground(new java.awt.Color(102, 102, 102));
        backGroundLabel.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
        backGroundLabel.setForeground(new java.awt.Color(255, 255, 255));
        backGroundLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/background1_1.jpg"))); // NOI18N
        backGroundLabel.setMaximumSize(new java.awt.Dimension(800, 600));
        backGroundLabel.setMinimumSize(new java.awt.Dimension(800, 600));
        backGroundLabel.setOpaque(true);
        backGroundLabel.setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().add(backGroundLabel);
        backGroundLabel.setBounds(0, -370, 1410, 1340);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        conectionLabel.requestFocus();
    }//GEN-LAST:event_jCheckBox1ActionPerformed


    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int check = 0;

            String id = jTextField1.getText().replaceAll("\\s", "");
            String password = jPasswordField1.getText();
            db = jTextField1.getText().replaceAll("\\s", "");
            if (id.equals("") || password.equals("")) {
                check = 1;
                JOptionPane.showMessageDialog(null, "veuillez renseigner toutes les informations");
            } else if (id.equals("admin_page") && password.equals("admin")) {
                check = 1;
                setVisible(false);
                new adminHome().setVisible(true);
            } else {
                ResultSet rs = select.getData("admin_database", "select * from users where name = '" + id + "' and password = '" + password + "'");
                try {
                    if (rs.next()) {
                        check = 1;
                        if (jCheckBox1.isSelected() && !rememberPreference) {
                            preference.put("user", jTextField1.getText());
                            preference.put("pswrd", jPasswordField1.getText());
                            preference.putBoolean("rememberMe", true);

                        } else if (!jCheckBox1.isSelected() && rememberPreference) {
                            preference.put("user", "");
                            preference.put("pswrd", "");
                            preference.putBoolean("rememberMe", false);
                        }

                        setVisible(false);
                        new mainPage(db).setVisible(true);

                    }
                    rs.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            if (check == 0) {
                JOptionPane.showMessageDialog(null, "Mot de Passe ou Email Incorrect");
            }
        }
    }//GEN-LAST:event_formKeyPressed

    private void closeLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeLabelMouseClicked
        int a = JOptionPane.showConfirmDialog(null, "Êtes-Vous Sûre De Vouloir Fermer l'Application ?", "Select", JOptionPane.YES_NO_OPTION);
        if (a == 0) {

            System.exit(0);
        }
    }//GEN-LAST:event_closeLabelMouseClicked

    private void pswrLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pswrLabelMouseClicked
        preference.putBoolean("rememberMe", false);
        jCheckBox1.setSelected(false);
        setVisible(false);
        new forgotPassword().setVisible(true);
    }//GEN-LAST:event_pswrLabelMouseClicked

    private void signInLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signInLabelMouseClicked
        setVisible(false);
        new SignUp().setVisible(true);
    }//GEN-LAST:event_signInLabelMouseClicked

    private void conectionLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_conectionLabelMouseClicked
        connect();
    }//GEN-LAST:event_conectionLabelMouseClicked

    private void conectionLabelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_conectionLabelKeyPressed
        connect();


    }//GEN-LAST:event_conectionLabelKeyPressed

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        if (!jCheckBox1.isSelected()) {
            preference.put("user", "");
            preference.put("pswrd", "");
            preference.putBoolean("rememberMe", false);
        } else {
            preference.put("user", jTextField1.getText());
            preference.put("pswrd", jPasswordField1.getText());
            preference.putBoolean("rememberMe", true);
        }
    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void jTextField1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField1PropertyChange

    }//GEN-LAST:event_jTextField1PropertyChange

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        jCheckBox1.setSelected(false);
        jTextField1.selectAll();
        jPasswordField1.setText("");

    }//GEN-LAST:event_jTextField1MouseClicked

    private void conectionLabelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_conectionLabelFocusGained
        pointerConection.setVisible(true);

    }//GEN-LAST:event_conectionLabelFocusGained

    private void conectionLabelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_conectionLabelFocusLost
        pointerConection.setVisible(false);

    }//GEN-LAST:event_conectionLabelFocusLost

    private void pointerSignInFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pointerSignInFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_pointerSignInFocusGained

    private void pswrLabelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pswrLabelFocusGained
        pointerPsw.setVisible(true);
    }//GEN-LAST:event_pswrLabelFocusGained

    private void pswrLabelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pswrLabelFocusLost
        pointerPsw.setVisible(false);
    }//GEN-LAST:event_pswrLabelFocusLost

    private void signInLabelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_signInLabelFocusGained
        pointerSignIn.setVisible(true);

    }//GEN-LAST:event_signInLabelFocusGained

    private void signInLabelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_signInLabelFocusLost
        pointerSignIn.setVisible(false);

    }//GEN-LAST:event_signInLabelFocusLost

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        jCheckBox1.requestFocus();
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void pointerConectionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pointerConectionFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_pointerConectionFocusGained

    private void pointerPswFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pointerPswFocusLost
        conectionLabel.requestFocus();
    }//GEN-LAST:event_pointerPswFocusLost

    private void jCheckBox1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCheckBox1FocusLost
        conectionLabel.requestFocus();
    }//GEN-LAST:event_jCheckBox1FocusLost

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            jPasswordField1.requestFocus();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jPasswordField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            jCheckBox1.requestFocus();
        }
    }//GEN-LAST:event_jPasswordField1KeyPressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        if (jCheckBox1.isSelected()) {
            conectionLabel.requestFocus();
        }
    }//GEN-LAST:event_formComponentShown

    private void signInLabelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_signInLabelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            pswrLabel.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            setVisible(false);
            new SignUp().setVisible(true);

        }
    }//GEN-LAST:event_signInLabelKeyPressed

    private void pswrLabelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pswrLabelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            closeLabel.requestFocus();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            preference.putBoolean("rememberMe", false);
            jCheckBox1.setSelected(false);
            setVisible(false);
            new forgotPassword().setVisible(true);
        }

    }//GEN-LAST:event_pswrLabelKeyPressed

    private void closeLabelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_closeLabelKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            jTextField1.requestFocus();
        }
    }//GEN-LAST:event_closeLabelKeyPressed

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
            java.util.logging.Logger.getLogger(Simply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Simply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Simply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Simply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new Simply().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backGroundLabel;
    private javax.swing.JLabel closeLabel;
    private javax.swing.JLabel conectionLabel;
    private javax.swing.JLabel emailTxt;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel passwordTxt;
    private javax.swing.JLabel pointerConection;
    private javax.swing.JLabel pointerPsw;
    private javax.swing.JLabel pointerSignIn;
    private javax.swing.JLabel pswrLabel;
    private javax.swing.JLabel signInLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}

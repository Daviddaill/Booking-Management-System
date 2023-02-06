/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project;
 import java.util.Locale;
/**
 *
 * @author daviddaillere
 */
public class OS {
   

    private static String OS = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

    public static boolean isWindows()
    {
        return OS.contains("win");
    }

    public static boolean isMac()
    {
        return OS.contains("mac");
    }

    public static boolean isUnix()
    {
        return OS.contains("nux");
    }
}


package org.wintrisstech;
/*******************************************************************
 * Crazy Working JSoup
 * Copyright 2022 Dan Farris
 * Version crazy 220810
 * Writes Covers NFL data to a large SportData Excel sheet
 *******************************************************************/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
public class ExcelReader
{
    private final String deskTopPath = System.getProperty("user.home") + "/Desktop/SportDataBackup.xlsx";/* User's desktop path */
    private XSSFWorkbook sportDataWorkbook = null;
    public XSSFWorkbook readSportData()
    {
        FileInputStream inFile = null;
        try {
            inFile = new FileInputStream(deskTopPath);
            sportDataWorkbook = new XSSFWorkbook(inFile);
            inFile.close();
        } catch (Exception ex) {
            System.out.println("Error reading  " + inFile + " in ExcelReader20");
        }
        return sportDataWorkbook;
    }
    public XSSFWorkbook getSportDataWorkbook()
    {
        return sportDataWorkbook;
    }
}





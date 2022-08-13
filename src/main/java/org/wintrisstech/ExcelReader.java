package org.wintrisstech;
/*******************************************************************
 * Crazy Working JSoup
 * Copyright 2022 Dan Farris
 * Version NewCovers 220812
 * Writes Covers NFL data to SportData Excel sheet
 *******************************************************************/
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
public class ExcelReader
{
    private final String deskTopPath = System.getProperty("user.home") + "/Desktop/SportData.xlsx";/* User's desktop path */
    private XSSFWorkbook sportDataWorkbook;
    private XSSFSheet sportDataSheet;
    public XSSFWorkbook readSportData()
    {
        FileInputStream inFile = null;
        try {
            inFile = new FileInputStream(deskTopPath);
            sportDataWorkbook = new XSSFWorkbook(inFile);
            inFile.close();
        } catch (Exception ex) {
            System.out.println("Error reading  " + inFile + " in ExcelReader25");
        }
        sportDataSheet = sportDataWorkbook.getSheet("Data");
        return sportDataWorkbook;
    }
}





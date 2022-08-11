package org.wintrisstech;
/*******************************************************************
 * Crazy Working JSoup
 * Copyright 2022 Dan Farris
 * Version crazy 220810
 * Writes Covers NFL data to a large SportData Excel sheet
 *******************************************************************/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
public class ExcelWriter
{
    private final String deskTopPath = System.getProperty("user.home") + "/Desktop/SportData.xlsx";/* User's desktop path */
    public void writeSportData(XSSFWorkbook sportDataWorkbook)
    {
        FileOutputStream outFile;
        try {
            outFile = new FileOutputStream(deskTopPath);
            sportDataWorkbook.write(outFile);
            outFile.close();
        }
        catch (Exception ex) {System.out.println("Bad error writing SportData in ExcelWriter24");
        }
    }
}


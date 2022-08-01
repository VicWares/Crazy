package org.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version crazy 220801
 *******************************************************************/
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
public class ExcelBuilder
{
    private String thisSeason;//TODO:Fix this
    private String ouUnder;
    private String ouOver;
    private String homeTeam;
    private String awayTeam;
    private String thisMatchupDate;
    private HashMap<String, String> homeTeamsMap = new HashMap<>();
    private HashMap<String, String> awayTeamsMap = new HashMap<>();
    private HashMap<String, String> gameDatesMap = new HashMap<>();
    private HashMap<String, String> atsHomesMap = new HashMap<>();
    private HashMap<String, String> atsAwaysMap = new HashMap<>();
    private HashMap<String, String> ouOversMap;
    private HashMap<String, String> ouUndersMap;
    private final HashMap<String, String> homeMLOddsMap = new HashMap<>();
    private final HashMap<String, String> homeMoneyLineOddsMap = new HashMap<>();
    private final HashMap<String, String> bet365OddsMap = new HashMap<>();
    private String spreadHomeOddsString;
    private String spreadAwayOddsString;
    private Sheet sportDataSheet;
    private final XSSFWorkbook sportDataWorkBook = new XSSFWorkbook();
    private final XSSFSheet sportDataUpdateSheet = null;
    byte[] redColor = new byte[]{(byte) 255, (byte) 0, (byte) 0};
    Color color = new Color(215, 228, 188);
    private String atsHome;
    private String atsAway;
    private String completeHomeTeamName;
    private String completeAwayTeamName;
    private String gameIdentifier;
    private String dataEventId;
    private String bet365AwayOddsString;
    private String HomeOddsString;
    private String homeOddsString;
    private String moneyLineHomeOddsString;
    private String moneyLineAwayOddsString;
    public XSSFWorkbook buildExcel(XSSFWorkbook sportDataWorkbook, String dataEventId, int eventIndex, String gameIdentifier)
    {
        this.dataEventId = dataEventId;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = (dateFormat.format(date));
        sportDataSheet = sportDataWorkbook.getSheet("Data");
        CellStyle leftStyle = sportDataWorkbook.createCellStyle();
        CellStyle centerStyle = sportDataWorkbook.createCellStyle();
        CellStyle myStyle = sportDataWorkbook.createCellStyle();
        XSSFCellStyle redStyle = sportDataWorkbook.createCellStyle();
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        sportDataSheet.setDefaultColumnStyle(0, leftStyle);
        sportDataSheet.setDefaultColumnStyle(1, centerStyle);
        sportDataSheet.createRow(eventIndex);
        sportDataSheet.setColumnWidth(1, 25 * 256);
        homeTeam = homeTeamsMap.get(dataEventId);
        awayTeam = awayTeamsMap.get(dataEventId);
        thisMatchupDate = gameDatesMap.get(dataEventId);
        atsHome = atsHomesMap.get(dataEventId);
        atsAway = atsAwaysMap.get(dataEventId);
        ouOver = ouOversMap.get(dataEventId);
        ouUnder = ouUndersMap.get(dataEventId);
        sportDataSheet.getRow(eventIndex).createCell(0);
        sportDataSheet.getRow(eventIndex).getCell(0).setCellStyle(leftStyle);
        sportDataSheet.getRow(0).getCell(0).setCellValue(time);
        sportDataSheet.getRow(eventIndex).getCell(0).setCellValue(gameIdentifier);//Column A e.g. 2021 - Washington Football Team @ Dallas Cowboys
        sportDataSheet.getRow(eventIndex).createCell(1);
        sportDataSheet.getRow(eventIndex).getCell(1).setCellStyle(centerStyle);
        sportDataSheet.getRow(eventIndex).getCell(1).setCellValue(thisMatchupDate);
        sportDataSheet.getRow(eventIndex).createCell(2);
        sportDataSheet.getRow(eventIndex).getCell(2).setCellStyle(centerStyle);
        sportDataSheet.getRow(eventIndex).getCell(2).setCellValue(thisMatchupDate.split("-")[0]);//Column C, season TDDO:Fix this
        sportDataSheet.getRow(eventIndex).createCell(13);//Spread home odds, column N/14 Close
        sportDataSheet.getRow(eventIndex).getCell(13).setCellStyle(centerStyle);
        sportDataSheet.getRow(eventIndex).getCell(13).setCellValue(spreadHomeOddsString);
        sportDataSheet.getRow(eventIndex).createCell(17);//MoneyLine home odds, column R
        sportDataSheet.getRow(eventIndex).getCell(17).setCellStyle(centerStyle);
        sportDataSheet.getRow(eventIndex).getCell(17).setCellValue(moneyLineHomeOddsString);
        sportDataSheet.getRow(eventIndex).createCell(27);//Spread away odds, column AB/28
        sportDataSheet.getRow(eventIndex).getCell(27).setCellStyle(centerStyle);
        sportDataSheet.getRow(eventIndex).getCell(27).setCellValue(spreadAwayOddsString);
        sportDataSheet.getRow(eventIndex).createCell(31);//MoneyLine away odds, column AF
        sportDataSheet.getRow(eventIndex).getCell(31).setCellStyle(centerStyle);
        sportDataSheet.getRow(eventIndex).getCell(31).setCellValue(moneyLineAwayOddsString);
        sportDataSheet.getRow(eventIndex).createCell(59);
        sportDataSheet.getRow(eventIndex).getCell(59).setCellStyle(myStyle);
        sportDataSheet.getRow(eventIndex).getCell(59).setCellValue(atsHome);
        sportDataSheet.getRow(eventIndex).createCell(61);
        sportDataSheet.getRow(eventIndex).getCell(61).setCellStyle(myStyle);
        sportDataSheet.getRow(eventIndex).getCell(61).setCellValue(atsAway);
        sportDataSheet.getRow(eventIndex).createCell(64);
        sportDataSheet.getRow(eventIndex).getCell(64).setCellStyle(myStyle);
        sportDataSheet.getRow(eventIndex).getCell(64).setCellValue(ouOver);
        sportDataSheet.getRow(eventIndex).createCell(66);
        sportDataSheet.getRow(eventIndex).getCell(66).setCellStyle(myStyle);
        sportDataSheet.getRow(eventIndex).getCell(66).setCellValue(ouUnder);
        return sportDataWorkbook;
    }
    public void setHomeTeamsMap(HashMap<String, String> homeTeamsMap)
    {
        this.homeTeamsMap = homeTeamsMap;
    }
    public void setThisWeekAwayTeamsMap(HashMap<String, String> thisWeekAwayTeamsMap)
    {
        this.awayTeamsMap = thisWeekAwayTeamsMap;
    }
    public void setGameDatesMap(HashMap<String, String> gameDatesMap)
    {
        this.gameDatesMap = gameDatesMap;
    }
    public void setAtsHomesMap(HashMap<String, String> atsHomes)
    {
        this.atsHomesMap = atsHomes;
    }
    public void setAtsAwaysMap(HashMap<String, String> atsAwayMap)
    {
        this.atsAwaysMap = atsAwayMap;
    }
    public void setOuOversMap(HashMap<String, String> ouOversMap)
    {
        this.ouOversMap = ouOversMap;
    }
    public void setOuUndersMap(HashMap<String, String> ouUndersMap)
    {
        this.ouUndersMap = ouUndersMap;
    }
    public void setCompleteHomeTeamName(String completeHomeTeamName)
    {
        this.completeHomeTeamName = completeHomeTeamName;
    }
    public void setCompleteAwayTeamName(String completeAwayTeamName)
    {
        this.completeAwayTeamName = completeAwayTeamName;
    }
    public void setGameIdentifier(String gameIdentifier)
    {
        this.gameIdentifier = gameIdentifier;
    }
    public void setMoneyLineHomeOddsString(String moneyLineHomeOddsString) {this.moneyLineHomeOddsString = moneyLineHomeOddsString;}
    public void setMoneyLineAwayOddsString(String moneyLineAwayOddsString) {this.moneyLineAwayOddsString = moneyLineAwayOddsString;}
    public void setSpreadHomeOddsString(String spreadHomeOddsString) {this.spreadHomeOddsString = spreadHomeOddsString;}
    public void setSpreadAwayOddsString(String spreadAwayOddsString) {this.spreadAwayOddsString = spreadAwayOddsString;}
}


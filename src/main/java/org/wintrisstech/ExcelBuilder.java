package org.wintrisstech;
/*******************************************************************
 * Crazy Working JSoup
 * Copyright 2022 Dan Farris
 * Version crazy 220810
 * Writes Covers NFL data to a large SportData Excel sheet
 *******************************************************************/
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.select.Elements;

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
    private String spreadHomeOddsString;
    private String spreadAwayOddsString;
    private Sheet SPORT_DATABOOK;
    private XSSFWorkbook sportDataWorkBook = new XSSFWorkbook();
    private final XSSFSheet sportDataUpdateSheet = null;
    private String atsHome;
    private String atsAway;
    private String gameIdentifier;
    private String dataEventId;
    private String bet365AwayOddsString;
    private String HomeOddsString;
    private String homeOddsString;
    private String moneyLineHomeOddsString;
    private String moneyLineAwayOddsString;
    private String awayCityPlusNickname;
    private String homeCityPlusNickname;
    public void buildExcel(XSSFSheet sportDataSheet, String dataEventId, int eventIndex)
    {
        this.dataEventId = dataEventId;

        this.SPORT_DATABOOK.setColumnWidth(1, 25 * 256);
//        homeTeam = homeTeamsMap.get(dataEventId);
//        awayTeam = awayTeamsMap.get(dataEventId);
//        thisMatchupDate = gameDatesMap.get(dataEventId);
//        atsHome = atsHomesMap.get(dataEventId);
//        atsAway = atsAwaysMap.get(dataEventId);
//        ouOver = ouOversMap.get(dataEventId);
//        ouUnder = ouUndersMap.get(dataEventId);
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(0).setCellValue(gameIdentifier);//Column A e.g. 2021 - Washington Football Team @ Dallas Cowboys
        this.SPORT_DATABOOK.getRow(eventIndex).createCell(1);
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(1).setCellValue(thisMatchupDate);
        //sportDataSheet.getRow(eventIndex).createCell(2);
        //sportDataSheet.getRow(eventIndex).getCell(2).setCellStyle(centerStyle);
        //sportDataSheet.getRow(eventIndex).getCell(2).setCellValue(thisMatchupDate.split("-")[0]);//Column C, season TDDO:Fix this
        this.SPORT_DATABOOK.getRow(eventIndex).createCell(12);//Home city plus Nickname e.g. Denver Broncos
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(12).setCellValue(homeCityPlusNickname);
        this.SPORT_DATABOOK.getRow(eventIndex).createCell(13);//Spread home odds, column N/14 Close
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(13).setCellValue(spreadHomeOddsString);
        this.SPORT_DATABOOK.getRow(eventIndex).createCell(28);//Spread away odds, column AB/28
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(28).setCellValue(spreadAwayOddsString);
        this.SPORT_DATABOOK.getRow(eventIndex).createCell(59);
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(59).setCellValue(atsHome);
        this.SPORT_DATABOOK.getRow(eventIndex).createCell(61);
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(61).setCellValue(atsAway);
        this.SPORT_DATABOOK.getRow(eventIndex).createCell(64);
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(64).setCellValue(ouOver);
        this.SPORT_DATABOOK.getRow(eventIndex).createCell(66);
        this.SPORT_DATABOOK.getRow(eventIndex).getCell(66).setCellValue(ouUnder);
    }
    public void setHomeTeamsMap(HashMap<String, String> homeTeamsMap)
    {
        this.homeTeamsMap = homeTeamsMap;
    }
    public void setCityPlusNicknameMap(HashMap<String, String> thisWeekAwayTeamsMap)
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
    public void setGameIdentifier(String gameIdentifier)
    {
        this.gameIdentifier = gameIdentifier;
    }
    public void setMoneyLineHomeOddsString(String moneyLineHomeOddsString)
    {
        this.moneyLineHomeOddsString = moneyLineHomeOddsString;
    }
    public void setMoneyLineAwayOddsString(String moneyLineAwayOddsString)
    {
        this.moneyLineAwayOddsString = moneyLineAwayOddsString;
    }
    public void setSpreadHomeOddsString(String spreadHomeOddsString)
    {
        this.spreadHomeOddsString = spreadHomeOddsString;
    }
    public void setSpreadAwayOddsString(String spreadAwayOddsString)
    {
        this.spreadAwayOddsString = spreadAwayOddsString;
    }
    public void buildAwayCityPlusNickname(String dataEventId, Elements weekElements, int rowIndex)//e.g. Kansas City Chiefs
    {
        String awayCity = weekElements.attr("data-away-team-fullname-search");//e.g. Dallas
        String awayNickname = weekElements.attr("data-away-team-nickname-search");//e.g. Cowboys
        String awayCityPlusNickname = awayCity + " " + awayNickname;
           try
           {
               sportDataWorkBook.getSheet("Data").createRow(rowIndex).createCell(25);//Away team combined name e.g Dallas Cowboys
               sportDataWorkBook.getSheet("Data").getRow(rowIndex).getCell(25).setCellValue(awayCityPlusNickname);//Away team combined name e.g Dallas Cowboys
           } catch (Exception e) {
               System.out.println("EB138 can't put awayCityPlusNickname in cell(25)");
           }
    }
    public XSSFWorkbook get(XSSFWorkbook sportDataWorkbook) {return sportDataWorkbook;}
    public void setSportDataWorkbook(XSSFWorkbook sportDataWorkbook) {this.sportDataWorkBook = sportDataWorkbook;}
    public void buildTimeStamp()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = (dateFormat.format(date));
        sportDataWorkBook.getSheet("Data").getRow(0).getCell(0).setCellValue(time);
    }
}


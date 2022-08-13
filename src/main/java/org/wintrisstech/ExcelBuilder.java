package org.wintrisstech;
/*******************************************************************
 * Crazy Working JSoup
 * Copyright 2022 Dan Farris
 * Version NewCovers 220812
 * Writes Covers NFL data to SportData Excel sheet
 *******************************************************************/
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
    private XSSFSheet sportDataSheet;
    private XSSFWorkbook sportDataWorkbook;
    public void buildAwayCityNname(XSSFWorkbook sportDataWorkbook, String dataEventId, Elements nflElements, int rowIndex)//e.g. Kansas City Chiefs
    {
        Elements e = nflElements.select("[data-event-id='" + dataEventId + "']");
        String awayCity = e.attr("data-away-team-fullname-search");//e.g. Tennessee
        sportDataWorkbook.getSheet("Data").createRow(rowIndex).createCell(10).setCellType(CellType.STRING);
        sportDataWorkbook.getSheet("Data").createRow(rowIndex).createCell(10);
        sportDataWorkbook.getSheet("Data").getRow(rowIndex).getCell(10).setCellValue(awayCity);//Cell K...11  Home Team
    }
    public void buildHomeCityNname(XSSFWorkbook sportDataWorkbook, String dataEventId, Elements nflElements, int rowIndex)//Home city name.g. Kansas City Chiefs
    {
        Elements e = nflElements.select("[data-event-id='" + dataEventId + "']");
        String homeCity = e.attr("data-home-team-fullname-search");//e.g. Houston
        sportDataWorkbook.getSheet("Data").createRow(rowIndex).createCell(25).setCellType(CellType.STRING);
        sportDataWorkbook.getSheet("Data").createRow(rowIndex).createCell(25);
        sportDataWorkbook.getSheet("Data").getRow(rowIndex).getCell(25).setCellValue(homeCity);//Cell Z...26  Home Team
    }
    public void buildTimeStamp(XSSFWorkbook sportDataWorkbook)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = (dateFormat.format(date));
        sportDataWorkbook.getSheet("data"). getRow(0).getCell(0).setCellValue(time);
    }
    public void setSportDataWorkbook(XSSFWorkbook sportDataWorkbook) {this.sportDataWorkbook = sportDataWorkbook;}
    public void setSportDataSheet(XSSFSheet sportDataSheet) {this.sportDataSheet = sportDataSheet;}
    public XSSFWorkbook getSportDataWorkbook()
    {
        return sportDataWorkbook;
    }
}


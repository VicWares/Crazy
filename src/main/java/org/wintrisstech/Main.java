package org.wintrisstech;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
/*********************************************************
 * Copyright 2022 Dan Farris
 * Version NewCovers 220812
 * Writes Covers NFL data to SportData Excel sheet
 *********************************************************/
public class Main
{
    public static final String VERSION = "220807";
    private static XSSFWorkbook sportDataWorkbook = new XSSFWorkbook();
    private final HashMap<String, String> weekDateMap = new HashMap<>();
    private final HashMap<String, String> cityNameMap = new HashMap<>();
    private HashMap<String, String> xRefMap = new HashMap<>();
    public ExcelBuilder excelBuilder = new ExcelBuilder();
    public ExcelWriter excelWriter = new ExcelWriter();
    public ExcelReader excelReader = new ExcelReader();
    private int rowIndex = 3;
    public XSSFSheet sportDataSheet;
    private String weekNumber = "1";//For testinbg
    private String weekDate;
    private Elements nflElements;
    public static void main(String[] args) throws IOException
    {
        Main main = new Main();
        main.getGoing();//To get out of static context
    }
    public static XSSFWorkbook getSportDataWorkbook()
    {
        return sportDataWorkbook;
    }
    public static void setSportDataWorkbook(XSSFWorkbook sportDataWorkbook)
    {
        Main.sportDataWorkbook = sportDataWorkbook;
    }
    private void getGoing() throws IOException
    {
        buildCityAndWeekNumberMaps();
        weekNumber = JOptionPane.showInputDialog("Enter NFL week number, e. g. 2");//Get input from user
        weekNumber = "1"; //for testing
        nflElements = WebSiteReader.readWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + weekDate);//Covers.com "Scores and Matchups" page for this week
        org.jsoup.select.Elements soupOddsElements = WebSiteReader.readWebsite("https://www.covers.com/sport/football/nfl/odds");
        xRefMap = buildXrefMap(nflElements);//Key is data-event-ID e.g. 87579, Value is data-game e.g. 265282, two different ways of selecting the same matchup (game)
        System.out.println("Main45 week number => " + weekNumber + ", week date => " + weekDate + ", " + xRefMap.size() + " games this week");
        sportDataWorkbook = excelReader.readSportData();
        excelBuilder.setSportDataWorkbook(sportDataWorkbook);
        System.out.println("Main56.");
        excelBuilder.setSportDataSheet(sportDataSheet);
        System.out.println("Main47>>>>>>>>>>>>>>>>>>>>>>>>>START MAIN LOOP, VERSION #" + VERSION  + ".......... MAIN LOOP, WEEK DATE " + weekDate + "........... MAIN LOOP, Games This Week " + nflElements.size()+ "........... MAIN LOOP, VERSION #" + VERSION + "...................");
        excelBuilder.buildTimeStamp(sportDataWorkbook);//Cell(0), row(0)
        for (Map.Entry<String, String> entry : xRefMap.entrySet())//Loop through all matchups this week
        {
            String dataEventId = entry.getKey();//e.g. 87581
            String dataGame = xRefMap.get(dataEventId);//e.g. 265284 Two ways of specifying the same matchup
            sportDataWorkbook = excelReader.readSportData();
            excelBuilder.buildHomeCityNname(sportDataWorkbook, dataEventId, nflElements, rowIndex);
            excelWriter.writeSportData(sportDataWorkbook);
            sportDataWorkbook = excelReader.readSportData();
            excelBuilder.buildAwayCityNname(sportDataWorkbook, dataEventId, nflElements, rowIndex);
            excelWriter.writeSportData(sportDataWorkbook);

            //org.jsoup.select.Elements consensusElements = WebSiteReader.readWebsite("https://contests.covers.com/consensus/matchupconsensusdetails?externalId=%2fsport%2ffootball%2fcompetition%3a" + dataEventId);
            //excelBuilder.buildHomeCityNname(dataEventId, nflElements, rowIndex);//Column K...11
            //excelBuilder.buildAwayCityNname(dataEventId, nflElements, rowIndex);//Column Z...26
            rowIndex++;
            System.out.println("data-event-id => " + dataEventId + " data-game => " + dataGame);
        }
        excelWriter.writeSportData(excelBuilder.getSportDataWorkbook());//Writes updated SportData.xlsx file to Desktop/SportData.xlsx
        System.out.println("Main59>>>>>>>>>>>>>>>>>>>>>>>>> END MAIN LOOP, VERSION #" + VERSION + " END MAIN LOOP, VERSION #" + VERSION + ".......... END MAIN LOOP, VERSION #" + VERSION + "........... END MAIN LOOP, VERSION #" + VERSION + "...................\n");
        System.out.println("Main61 Proper Finish...HOORAY");
    }
    private void buildCityAndWeekNumberMaps()
    {
        buildCityNameMap();
        buildWeekNumberMap();
        weekDate = weekDateMap.get(weekNumber);//Gets week date e.g. 2022-09-08 from week number e.g. 1,2,3,4,...
    }
    public HashMap<String, String> buildXrefMap(org.jsoup.select.Elements nflElements)//Cross refernces data-event-id (e.g.87585) and data-game (e.g. 265288)...two ways of identifying matchups
    {
        Elements matchupElements = nflElements.select(".cmg_game_data.cmg_matchup_game_box");
        for (Element e : matchupElements)
        {
            String dataEventId = e.attr("data-event-id");
            String dataGame[] = e.attr("data-link").split("/");
            String dataGameString = dataGame[5];
            xRefMap.put(dataEventId, dataGameString);
        }
        return xRefMap;
    }
    private void buildCityNameMap()
    {
        cityNameMap.put("Minneapolis", "Minnesota");//Minnesota Vikings
        cityNameMap.put("Tampa", "Tampa Bay");//Tampa Bay Buccaneers
        cityNameMap.put("Tampa Bay", "Tampa Bay");//Tampa Bay Buccaneers
        cityNameMap.put("Arlington", "Dallas");//Dallas Cowboys
        cityNameMap.put("Dallas", "Dallas");//Dallas Cowboys
        cityNameMap.put("Orchard Park", "Buffalo");//Buffalo Bills
        cityNameMap.put("Buffalo", "Buffalo");//Buffalo Bills
        cityNameMap.put("Charlotte", "Carolina");//Carolina Panthers
        cityNameMap.put("Carolina", "Carolina");//Carolina Panthers
        cityNameMap.put("Arizona", "Arizona");//Arizona Cardinals
        cityNameMap.put("Tempe", "Arizona");//Arizona Cardinals
        cityNameMap.put("Foxborough", "New England");//New England Patriots
        cityNameMap.put("New England", "New England");//New England Patriots
        cityNameMap.put("East Rutherford", "New York");//New York Giants and New York Jets
        cityNameMap.put("New York", "New York");//New York Giants and New York Jets
        cityNameMap.put("Landover", "Washington");//Washington Football Team
        cityNameMap.put("Washington", "Washington");//Washington Football Team
        cityNameMap.put("Nashville", "Tennessee");//Tennessee Titans
        cityNameMap.put("Miami", "Miami");//Miami Dolphins
        cityNameMap.put("Baltimore", "Baltimore");//Baltimore Ravens
        cityNameMap.put("Cincinnati", "Cincinnati");//Cincinnati Bengals
        cityNameMap.put("Cleveland", "Cleveland");//Cleveland Browns
        cityNameMap.put("Pittsburgh", "Pittsburgh");//Pittsburgh Steelers
        cityNameMap.put("Houston", "Houston");//Houston Texans
        cityNameMap.put("Indianapolis", "Indianapolis");//Indianapolis Colts
        cityNameMap.put("Jacksonville", "Jacksonville");//Jacksonville Jaguars
        cityNameMap.put("Tennessee", "Tennessee");//Tennessee Titans
        cityNameMap.put("Denver", "Denver");//Denver Broncos
        cityNameMap.put("Kansas City", "Kansas City");//Kansas City Chiefs
        cityNameMap.put("Las Vegas", "Las Vegas");//Los Angeles Chargers and Los Angeles Rams
        cityNameMap.put("Philadelphia", "Philadelphia");//Philadelphia Eagles
        cityNameMap.put("Chicago", "Chicago");//Chicago Bears
        cityNameMap.put("Detroit", "Detroit");//Detroit Lions
        cityNameMap.put("Green Bay", "Green Bay");//Green Bay Packers
        cityNameMap.put("Minnesota", "Minnesota");
        cityNameMap.put("Atlanta", "Atlanta");//Atlanta Falcons
        cityNameMap.put("New Orleans", "New Orleans");//New Orleans Saints
        cityNameMap.put("Los Angeles", "Los Angeles");//Los Angeles Rams
        cityNameMap.put("San Francisco", "San Francisco");//San Francisco 49ers
        cityNameMap.put("Seattle", "Seattle");//Seattle Seahawks
    }
    private void buildWeekNumberMap()
    {
        weekDateMap.put("1", "2022-09-08");//Season start...Week 1
        weekDateMap.put("2", "2022-09-15");
        weekDateMap.put("3", "2022-09-22");
        weekDateMap.put("4", "2022-09-29");
        weekDateMap.put("5", "2022-10-06");
        weekDateMap.put("6", "2022-10-13");
        weekDateMap.put("7", "2022-10-20");
        weekDateMap.put("8", "2022-10-27");
        weekDateMap.put("9", "2022-11-03");
        weekDateMap.put("10", "2022-11-10");
        weekDateMap.put("11", "2022-11-17");
        weekDateMap.put("12", "2022-11-24");
        weekDateMap.put("13", "2022-12-01");
        weekDateMap.put("14", "2022-12-08");
        weekDateMap.put("15", "2022-12-15");
        weekDateMap.put("16", "2022-12-22");
        weekDateMap.put("17", "2022-12-29");
        weekDateMap.put("18", "2023-01-08");
        weekDateMap.put("19", "2023-02-05");
    }
}

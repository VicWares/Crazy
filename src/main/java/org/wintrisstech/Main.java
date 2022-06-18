package org.wintrisstech;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.lang.model.util.Elements;
import java.util.HashMap;
/****************************************
 * Crazy Working selenium demo
 ****************************************/
public class Main
{
    private static String version = "220616";
    public static String weekNumber;
    private XSSFWorkbook sportDataWorkbook;
    private HashMap<String, String> weekNumberMap = new HashMap<>();
    private HashMap<String, String> cityNameMap = new HashMap<>();
    private HashMap<String, String> xRefMap = new HashMap<>();
    public WebSiteReader webSiteReader = new WebSiteReader();
    public ExcelReader excelReader = new ExcelReader();
    public ExcelBuilder excelBuilder = new ExcelBuilder();
    public ExcelWriter excelWriter = new ExcelWriter();
    public DataCollector dataCollector = new DataCollector();
    private Elements consensusElements;
    private int globalMatchupIndex = 3;
    private Elements oddsElements;
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.setProperty("webdriver.chrome.driver", "/Users/vicwintriss/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://facebook.com");
        System.out.println(driver.getTitle());
        driver.quit();
    }public HashMap<String, String> buildXref(org.jsoup.select.Elements weekElements)
{
    for (Element e : weekElements) {
        String dataLinkString = e.attr("data-link");
        String[] dlsa = dataLinkString.split("/");
        String dataLink = dlsa[5];
        String dataEvent = e.attr("data-event-id");
        xRefMap.put(dataEvent, dataLink);
    }
    return xRefMap;
}
    private void fillCityNameMap()
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
        cityNameMap.put("Las Vegas", "Las Vegas");//Los Angeles Chargers and Los angeles Rams
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

    private void fillWeekNumberMap()
    {
        weekNumberMap.put("1", "2021-09-09");//Season start...Week 1
        weekNumberMap.put("2", "2021-09-16");
        weekNumberMap.put("3", "2021-09-23");
        weekNumberMap.put("4", "2021-09-30");
        weekNumberMap.put("5", "2021-10-07");
        weekNumberMap.put("6", "2021-10-14");
        weekNumberMap.put("7", "2021-10-21");
        weekNumberMap.put("8", "2021-10-28");
        weekNumberMap.put("9", "2021-11-04");
        weekNumberMap.put("10", "2021-11-11");
        weekNumberMap.put("11", "2021-11-18");
        weekNumberMap.put("12", "2021-11-25");
        weekNumberMap.put("13", "2021-12-02");
        weekNumberMap.put("14", "2021-12-09");
        weekNumberMap.put("15", "2021-12-16");
        weekNumberMap.put("16", "2021-12-23");
        weekNumberMap.put("17", "2022-01-02");
        weekNumberMap.put("18", "2022-01-09");
        weekNumberMap.put("19", "2022-02-06");
    }
}
///*******************************************************************
// * Covers NFL Extraction Tool
// * Copyright 2021 Dan Farris
// * version 220617
// * MyProject9   complete working copy
// * Build .dmg with
// * jpackage --verbose --name SmartPack --input target --main-jar Covers.jar --main-class com.wintrisstech.Main.class
// *******************************************************************/
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import javax.swing.*;
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.Map;
//public class Main extends JComponent
//{
//    private static String version = "220617";
//    private XSSFWorkbook sportDataWorkbook;
//    private HashMap<String, String> weekNumberMap = new HashMap<>();
//    private HashMap<String, String> cityNameMap = new HashMap<>();
//    private HashMap<String, String> xRefMap = new HashMap<>();
//    public WebSiteReader webSiteReader = new WebSiteReader();
//    public ExcelReader excelReader = new ExcelReader();
//    public ExcelBuilder excelBuilder = new ExcelBuilder();
//    public ExcelWriter excelWriter = new ExcelWriter();
//    public DataCollector dataCollector = new DataCollector();
//    private Elements consensusElements;
//    private int globalMatchupIndex = 3;
//    private Elements oddsElements;
////    WebDriver drv;
//    public static void main(String[] args) throws IOException, ParseException
//    {
//        System.out.println("SharpMarkets, version " + version + ", Copyright 2021 Dan Farris");
//        Main main = new Main();
//        main.initialize();//Get out of static context
//    }
//    private void initialize() throws IOException
//    {
//        fillCityNameMap();//Builds full city name map to correct for Covers variations in team city names
//        fillWeekNumberMap();
//        dataCollector.setCityNameMap(cityNameMap);
//        String weekNumber = JOptionPane.showInputDialog("Enter NFL week number");
//        weekNumber = "17";
//        String weekDate = weekNumberMap.get(weekNumber);
//        Elements nflElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + weekDate);
//        Elements weekElements = nflElements.select(".cmg_game_data, .cmg_matchup_game_box");
//        xRefMap = buildXref(weekElements);
//        oddsElements = webSiteReader.readCleanWebsite("https://www.covers.com/sport/football/nfl/odds");//Info from log-in date through the present NFL week
//        System.out.println("Main56 week number => " + weekNumber + ", week date => " + weekDate + ", " + weekElements.size() + " games this week") ;
//        System.out.println(xRefMap);
//        dataCollector.collectTeamInfo(weekElements);
//        sportDataWorkbook = excelReader.readSportData();
//        //
////        {
////            System.out.println("SetUp version " + version);
////            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
////            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
////            drv = new FirefoxDriver();
////            drv.manage().window().maximize();
////            drv.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
////            String url = "https://www.covers.com/sport/football/nfl/odds";
////            drv.get(url);
////            drv.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
////            List<WebElement> elements = drv.findElements(By.cssSelector("#__spreadTotalDiv-nfl-265276 > table > tbody > tr:nth-child(2) > td:nth-child(9)"));
////            {
////                for (WebElement e : elements)
////                {
////                    System.out.println(e.getText());
////                }
////            }
////        }
//        //
//        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< MAIN LOOP===================MAIN LOOP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> processing all mtchups this week
//        for (Map.Entry<String, String> entry : xRefMap.entrySet())
//        {
//            String dataEventId = entry.getKey();
//            String dataGame = xRefMap.get(dataEventId);
//            //System.out.println("Main64 " + dataEventId + " " + xRefMap.get(dataEventId) + " " + dataCollector.getGameDatesMap().get(dataEventId) + " " + dataCollector.getAwayFullNameMap().get(dataEventId) + " vs " + dataCollector.getHomeFullNameMap().get(dataEventId));
//
//
//            Elements moneyLineOddsElements = oddsElements.select("[data-game*=" + dataGame + "]:nth-child(9)");
//            System.out.println("....." + moneyLineOddsElements.text());
//            String moneyLineOdds = dataCollector.collectMoneylineOdds(moneyLineOddsElements, xRefMap, dataEventId);
//            excelBuilder.setMoneyLineOdds(moneyLineOdds, dataEventId);
//            consensusElements = webSiteReader.readCleanWebsite("https://contests.covers.com/consensus/matchupconsensusdetails?externalId=%2fsport%2ffootball%2fcompetition%3a" + dataEventId);
//            dataCollector.collectConsensusData(consensusElements, dataEventId);
//            excelBuilder.setThisWeekAwayTeamsMap(dataCollector.getAwayFullNameMap());
//            excelBuilder.setHomeTeamsMap(dataCollector.getHomeFullNameMap());
//            excelBuilder.setGameDatesMap(dataCollector.getGameDatesMap());
//            excelBuilder.setAtsHomesMap(dataCollector.getAtsHomesMap());
//            excelBuilder.setAtsAwaysMap(dataCollector.getAtsAwaysMap());
//            excelBuilder.setOuOversMap(dataCollector.getOuOversMap());
//            excelBuilder.setOuUndersMap(dataCollector.getOuUndersMap());
//            excelBuilder.setCompleteHomeTeamName(dataCollector.getHomeTeamCompleteName());
//            excelBuilder.setCompleteAwayTeamName(dataCollector.getAwayTeamCompleteName());
//            excelBuilder.setGameIdentifier(dataCollector.getGameIdentifierMap().get(dataEventId));
//            excelBuilder.buildExcel(sportDataWorkbook, dataEventId, globalMatchupIndex, dataCollector.getGameIdentifierMap().get(dataEventId));
//            globalMatchupIndex++;
//        }
//        excelWriter.openOutputStream();
//        excelWriter.writeSportData(sportDataWorkbook);
//        excelWriter.closeOutputStream();
//        System.out.println("Proper Finish...HOORAY!");
//    }

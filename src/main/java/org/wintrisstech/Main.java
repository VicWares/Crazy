package org.wintrisstech;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
/****************************************
 * Crazy Working selenium demo
 * version crazy2 220707
 ****************************************/
public class Main
{
    private static String version = "220707";
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
    public static WebDriver driver;
    public static WebDriverWait wait;
    private int gameIndex;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        System.out.println("Main38 Main40 Starting main()");
        System.setProperty("webdriver.chrome.driver", "/Users/vicwintriss/Downloads/chromedriver");
        Main.driver = new ChromeDriver();
        Main.wait = new WebDriverWait(driver,  Duration.ofSeconds(10));
        Main main = new Main();
        main.getGoing();//To get out of static context
    }
    private void getGoing() throws IOException, InterruptedException
    {
        System.out.println("Main45 GetGoing()");
        fillCityNameMap();
        fillWeekNumberMap();
        String weekNumber = JOptionPane.showInputDialog("Enter NFL week number");
        weekNumber = "1";
        String weekDate = weekNumberMap.get(weekNumber);
        System.out.println("Main53 >>>>>>>>>>>>>>>>>>>>> weekDate: " + weekDate);
        org.jsoup.select.Elements nflElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + weekDate);
        org.jsoup.select.Elements weekElements = nflElements.select(".cmg_game_data, .cmg_matchup_game_box");
        xRefMap = buildXref(weekElements);
        System.out.println(xRefMap);
        System.out.println("Main58 week number => " + weekNumber + ", week date => " + weekDate + ", " + weekElements.size() + " games this week");
        dataCollector.collectTeamInfo(weekElements);
        sportDataWorkbook = excelReader.readSportData();
        Main.driver.get("https://www.covers.com/sport/football/nfl/odds");//Get current year odds & betting lines
        ///////////Click on bet menu
        Main.wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#__betMenu")));
        WebElement bm = Main.driver.findElement(By.cssSelector("#__betMenu"));
        bm.click();
        System.out.println("Main71 clicked on betMenu");
        //////////Click on Moneyline
        Main.wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-value=moneyline]")));
        WebElement ml = Main.driver.findElement(By.cssSelector("[data-value=moneyline]"));//Moneyline
        ml.click();
        System.out.println("Main76 clicked on Moneyline");
        ///////////////////////////////////////////////////////////////////////// MAIN LOOP ////////////////////////////////////////////////////////////
        for (Map.Entry<String, String> entry : xRefMap.entrySet())
        {
            gameIndex++;
            String dataEventId = entry.getKey();
            System.out.println("Main82 /////////////////////////////////////// BEGIN MAIN LOOP ///////////////////////////////////////////////////////////////////////////=> " + gameIndex);
            System.out.println("Main83, data-event-id=> " + dataEventId + ", " + xRefMap.get(dataEventId) + " " + dataCollector.getGameDatesMap().get(dataEventId) + " " + dataCollector.getAwayFullNameMap().get(dataEventId) + " vs " + dataCollector.getHomeFullNameMap().get(dataEventId));
            consensusElements = webSiteReader.readCleanWebsite("https://contests.covers.com/consensus/matchupconsensusdetails?externalId=%2fsport%2ffootball%2fcompetition%3a" + dataEventId);
            dataCollector.collectConsensusData(consensusElements, dataEventId);
            excelBuilder.setThisWeekAwayTeamsMap(dataCollector.getAwayFullNameMap());
            excelBuilder.setHomeTeamsMap(dataCollector.getHomeFullNameMap());
            excelBuilder.setGameDatesMap(dataCollector.getGameDatesMap());
            excelBuilder.setAtsHomesMap(dataCollector.getAtsHomesMap());
            excelBuilder.setAtsAwaysMap(dataCollector.getAtsAwaysMap());
            excelBuilder.setOuOversMap(dataCollector.getOuOversMap());
            excelBuilder.setOuUndersMap(dataCollector.getOuUndersMap());
            excelBuilder.setCompleteHomeTeamName(dataCollector.getHomeTeamCompleteName());
            excelBuilder.setCompleteAwayTeamName(dataCollector.getAwayTeamCompleteName());
            excelBuilder.setGameIdentifier(dataCollector.getGameIdentifierMap().get(dataEventId));
            excelBuilder.buildExcel(sportDataWorkbook, dataEventId, globalMatchupIndex, dataCollector.getGameIdentifierMap().get(dataEventId));
            String dataGame = xRefMap.get(dataEventId);
            dataCollector.getOdds(dataGame);
            globalMatchupIndex++;
        }
        ///////////////////////////////////////////////////////////////////////// END MAIN LOOP ////////////////////////////////////////////////////////////
        excelWriter.openOutputStream();
        excelWriter.writeSportData(sportDataWorkbook);
        excelWriter.closeOutputStream();
        driver.quit();
        System.out.println("Main112 Proper Finish...HOORAY!");
    }

    public HashMap<String, String> buildXref(org.jsoup.select.Elements weekElements)
    {
        for (Element e : weekElements)
        {
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
        weekNumberMap.put("1", "2022-09-08");//Season start...Week 1
        weekNumberMap.put("2", "2022-09-15");
        weekNumberMap.put("3", "2022-09-22");
        weekNumberMap.put("4", "2022-09-29");
        weekNumberMap.put("5", "2022-10-06");
        weekNumberMap.put("6", "2022-10-13");
        weekNumberMap.put("7", "2022-10-20");
        weekNumberMap.put("8", "2022-10-27");
        weekNumberMap.put("9", "2022-11-03");
        weekNumberMap.put("10", "2022-11-10");
        weekNumberMap.put("11", "2022-11-17");
        weekNumberMap.put("12", "2022-11-24");
        weekNumberMap.put("13", "2022-12-01");
        weekNumberMap.put("14", "2022-12-08");
        weekNumberMap.put("15", "2022-12-15");
        weekNumberMap.put("16", "2022-12-22");
        weekNumberMap.put("17", "2022-12-29");
        weekNumberMap.put("18", "2023-01-08");
        weekNumberMap.put("19", "2023-02-05");
    }
}

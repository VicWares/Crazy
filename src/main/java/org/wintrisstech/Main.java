package org.wintrisstech;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/****************************************
 * Crazy Working selenium demo
 * version crazy 220721
 ****************************************/
public class Main
{
    private static final String VERSION = "220719B";
    private final HashMap<String, String> weekDateMap = new HashMap<>();
    private final HashMap<String, String> cityNameMap = new HashMap<>();
    private HashMap<String, String> xRefMap = new HashMap<>();
    private final WebSiteReader webSiteReader = new WebSiteReader();
    public ExcelReader excelReader = new ExcelReader();
    public ExcelBuilder excelBuilder = new ExcelBuilder();
    public ExcelWriter excelWriter = new ExcelWriter();
    public DataCollector dataCollector = new DataCollector();
    private int globalMatchupIndex = 3;
    private int loopCounter;
    public static void main(String[] args) throws IOException
    {
        Main main = new Main();
        main.getGoing();//To get out of static context
    }
    private void getGoing() throws IOException
    {
        fillCityNameMap();
        fillWeekNumberMap();
        String weekNumber = JOptionPane.showInputDialog("Enter NFL week number, e. g. 2");
        weekNumber = "1";
        String weekDate = weekDateMap.get(weekNumber);//Gets week date e.g. 2022-09-08 from week number e.g. 1,2,3,4,...
        org.jsoup.select.Elements nflElements = WebSiteReader.readWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + weekDate);//Covers.com "Scores and Matchups" page for this week
        org.jsoup.select.Elements weekElements = nflElements.select(".cmg_game_data, .cmg_matchup_game_box");//Lots of good stuff in this Element: team name, team city...
        xRefMap = buildXref(weekElements);//Key is data-event-ID e.g. 87579, Value is data-game e.g. 265282, two different ways of selecting the same matchup (game)
        System.out.println("Main66 week number => " + weekNumber + ", week date => " + weekDate + ", " + weekElements.size() + " games this week");
        dataCollector.collectTeamInfo(weekElements);
        XSSFWorkbook sportDataWorkbook = excelReader.readSportData();
        org.jsoup.select.Elements soupOddsElements = WebSiteReader.readWebsite("https://www.covers.com/sport/football/nfl/odds");
        System.out.println("Main69 /////////////////////////////////////// MAIN LOOP, VERSION #" + VERSION + " ///////////////////////////////////////////////////////////////////////////");
        for (Map.Entry<String, String> entry : xRefMap.entrySet())
        {
            loopCounter++;
            String dataEventId = entry.getKey();
            String dataGame = xRefMap.get(dataEventId);
            org.jsoup.select.Elements consensusElements = WebSiteReader.readWebsite("https://contests.covers.com/consensus/matchupconsensusdetails?externalId=%2fsport%2ffootball%2fcompetition%3a" + dataEventId);
            dataCollector.collectConsensusData(consensusElements, dataEventId);
            excelBuilder.setThisWeekAwayTeamsMap(dataCollector.getAwayFullNameMap());
            excelBuilder.setHomeTeamsMap(dataCollector.getHomeFullNameMap());
            excelBuilder.setGameDatesMap(dataCollector.getGameDatesMap());
            excelBuilder.setAtsHomesMap(dataCollector.getAtsHomesMap());
            excelBuilder.setAtsAwaysMap(dataCollector.getAtsAwaysMap());
            excelBuilder.setOuOversMap(dataCollector.getOuOversMap());
            excelBuilder.setOuUndersMap(dataCollector.getOuUndersMap());
            excelBuilder.setCompleteHomeTeamName(dataCollector.getHomeTeamCompleteName());
            excelBuilder.setCompleteAwayTeamName(dataCollector.getAwayTeamCityPlusNickName());
            excelBuilder.setGameIdentifier(dataCollector.getGameIdentifierMap().get(dataEventId));
            excelBuilder.buildExcel(sportDataWorkbook, dataEventId, globalMatchupIndex, dataCollector.getGameIdentifierMap().get(dataEventId));
            String moneyLineAwayOddsString = dataCollector.collectMoneyLineAwayOdds(dataGame, soupOddsElements);//Bet365 MoneyLine Odds
            String moneyLineHomeOddsString = dataCollector.collectMoneyLineHomeOdds(dataGame, soupOddsElements);
            excelBuilder.setMoneyLineAwayOddsString(moneyLineAwayOddsString);
            excelBuilder.setMoneyLineHomeOddsString(moneyLineHomeOddsString);
            String spreadAwayOddsString = dataCollector.collectSpreadAwayOdds(dataGame, soupOddsElements);//Bet365 Spread Odds
            String spreadHomeOddsString = dataCollector.collectSpreadHomeOdds(dataGame, soupOddsElements);
            excelBuilder.setSpreadAwayOddsString(spreadAwayOddsString);
            excelBuilder.setSpreadHomeOddsString(spreadHomeOddsString);
            System.out.println("Main67, data-event-id=> " + dataEventId + ", data-game=> " + dataGame + ", " + " " + dataCollector.getAwayFullNameMap().get(dataEventId) + " vs " + dataCollector.getHomeFullNameMap().get(dataEventId) + " away/home MoneyLine odds => " + moneyLineAwayOddsString + "/" + moneyLineHomeOddsString + " away/home Spread Odds => " + spreadAwayOddsString + "/" + spreadHomeOddsString) ;
            globalMatchupIndex++;
        }
        System.out.println("Main70 /////////////////////////////////////// END MAIN LOOP, VERSION #" + VERSION + " ///////////////////////////////////////////////////////////////////////////=> " + loopCounter + " games.");
        excelWriter.openOutputStream();
        excelWriter.writeSportData(sportDataWorkbook);
        excelWriter.closeOutputStream();
        System.out.println("Main74 Proper Finish...HOORAY!");
    }
    public HashMap<String, String> buildXref(org.jsoup.select.Elements weekElements)
    {
        for (Element e : weekElements)
        {
            String dataLinkString = e.attr("data-link");
            String[] dataLinkStringArray = dataLinkString.split("/");
            String dataLink = dataLinkStringArray[5];
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
    private void fillWeekNumberMap()
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

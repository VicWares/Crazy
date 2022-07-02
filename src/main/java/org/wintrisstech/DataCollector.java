package org.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version crazy2 220702
 * Builds data event id array and calendar date array
 *******************************************************************/
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
public class DataCollector
{
    private static HashMap<String, String> bet365HomeTeamOdds = new HashMap<>();
    private static HashMap<String, String> bet365AwayTeamOdds = new HashMap<>();
    private static HashMap<String, String> bet365Odds = new HashMap<>();
    private static ArrayList<String> thisWeekMatchuplist = new ArrayList<>();
    private static ArrayList<String> homeAmericanOddsArray = new ArrayList<>();
    private static HashMap<String, String> homeAmericanOddsMap = new HashMap<>();
    private static ArrayList<String> homeDecimalOddsArray = new ArrayList<>();
    private static HashMap<String, String> homeDecimalOddsMap = new HashMap<>();
    private static ArrayList<String> homeFractionalOddsArray = new ArrayList<>();
    private static HashMap<String, String> homeFractionalOddsMap = new HashMap<>();
    private static ArrayList<String> awayAmericanOddsArray = new ArrayList<>();
    private static HashMap<String, String> awayAmericanOddsMap = new HashMap<>();
    private static ArrayList<String> awayDecimalOddsArray = new ArrayList<>();
    private static HashMap<String, String> awayMLoddsMap = new HashMap<>();
    private static HashMap<String, String> homeMLoddsMap = new HashMap<>();
    private static ArrayList<String> awayFractionalOddsArray = new ArrayList<>();
    private static HashMap<String, String> awayFractionalOddsMap = new HashMap<>();
    private HashMap<String, String> mlHomeOdds = new HashMap<String, String>();
    private HashMap<String, String> mlAwayOdds = new HashMap<String, String>();
    private String dataEventId;
    private String MLhomeOdds;
    private String MLawayOdds;
    private String homeTeamNickname;//e.g. Browns...data-home-team-nickname-search
    private String awayTeamNickname;//e.g Texans...data-away-team-nickname-search
    private String awayTeamFullName;//e.g. Cleveland...data-home-team-fullname-search
    private String homeTeamFullName;//e.g Houston...data-home-team-fullname-search
    private String awayTeamCompleteName;//e.g. Kansas City Chiefs
    private static String homeTeamCompleteName;//e.g Houston Texans
    private String gameIdentifier;//e.g 2020 - Houston Texans @ Kansas City Chiefs
    private String awayTeamScore;
    private String homeTeamScore;
    private String gameDate;
    private String awayTeamCity;
    private String homeTeamCity;
    private String thisWeek;
    private String thisSeason = "2022";
    private ArrayList<String> thisWeekGameDates = new ArrayList<String>();
    private ArrayList<String> thisGameWeekNumbers = new ArrayList<String>();
    private ArrayList<String> thisWeekHomeTeamScores = new ArrayList<String>();
    private ArrayList<String> thisWeekAwayTeamScores = new ArrayList<String>();
    private ArrayList<String> thisWeekHomeTeams = new ArrayList<String>();
    private ArrayList<String> atsHomes = new ArrayList<String>();
    private ArrayList<String> thisWeekAwayTeams = new ArrayList<String>();
    private HashMap<String, String> gameDatesMap = new HashMap<>();
    private HashMap<String, String> gameIdentifierMap = new HashMap<>();
    private HashMap<String, String> homeFullNameMap = new HashMap<>();
    private HashMap<String, String> awayFullNameMap = new HashMap<>();
    private HashMap<String, String> homeShortNameMap = new HashMap<>();
    private HashMap<String, String> awayShortNameMap = new HashMap<>();
    private HashMap<String, String> atsHomesMap = new HashMap<>();
    private HashMap<String, String> atsAwaysMap = new HashMap<>();
    private HashMap<String, String> ouUndersMap = new HashMap<>();
    private HashMap<String, String> ouOversMap = new HashMap<>();
    private HashMap<String, String> cityNameMap = new HashMap<>();
    private HashMap<String, String> idXref = new HashMap<>();
    private String[] bet365OddsArray = new String[6];
    public HashMap<String, String> getAwayMLoddsMap() {return awayMLoddsMap;}
    public HashMap<String, String> getHomeMLoddsMap() {return awayMLoddsMap;}
    private WebDriverWait wait;
    public void collectTeamInfo(Elements thisWeekElements)//From covers.com website for this week's matchups
    {
        for (Element e : thisWeekElements)//Build week matchup IDs array
        {
            homeTeamFullName = e.attr("data-home-team-fullname-search");//e.g. Houston...correcting for different city/name usage
            homeTeamNickname = e.attr("data-home-team-nickname-search");//e.g. Texans
            homeTeamCity = e.attr("data-home-team-city-search");
            homeTeamCity = cityNameMap.get(homeTeamCity);
            homeTeamCompleteName = homeTeamCity + " " + homeTeamNickname;
            awayTeamFullName = e.attr("data-away-team-fullname-search");//e.g. Dallas
            awayTeamNickname = e.attr("data-away-team-nickname-search");//e.g. Cowboys
            awayTeamCity = e.attr("data-away-team-city-search");
            awayTeamCity = cityNameMap.get(awayTeamCity);
            awayTeamCompleteName = awayTeamCity + " " + awayTeamNickname;
            gameIdentifier = thisSeason + " - " + awayTeamCompleteName + " @ " + homeTeamCompleteName;
            dataEventId = e.attr("data-event-id");
            String[] gameDateTime = e.attr("data-game-date").split(" ");
            gameDate = gameDateTime[0];
            awayTeamScore = e.attr("data-away-score");
            thisWeek = e.attr("data-competition-type");
            thisWeekGameDates.add(gameDate);
            gameDatesMap.put(dataEventId, gameDate);
            gameIdentifierMap.put(dataEventId, gameIdentifier);
            thisWeekHomeTeams.add(homeTeamCompleteName);
            thisWeekAwayTeams.add(awayTeamCompleteName);
            homeFullNameMap.put(dataEventId, homeTeamFullName);
            awayFullNameMap.put(dataEventId, awayTeamFullName);
            thisWeekHomeTeamScores.add(homeTeamScore);
            thisWeekAwayTeamScores.add((awayTeamScore));
            thisGameWeekNumbers.add(thisWeek);
            String awayShortName = e.attr("data-away-team-shortname-search");//Away team
            awayShortNameMap.put(dataEventId, awayShortName);
            String homeShortName = e.attr("data-home-team-shortname-search");//Home team
            homeShortNameMap.put(dataEventId, homeShortName);
        }
    }
    public void collectConsensusData(Elements Consensus, String MatchupID)
    {
        this.dataEventId = MatchupID;
        String ouOver = null;
        String ouUnder = null;
        String atsHome = null;
        String atsAway = null;
        Elements rightConsensus = Consensus.select(".covers-CoversConsensusDetailsTable-finalWagersright");//Home/Under
        Elements leftConsensus = Consensus.select(".covers-CoversConsensusDetailsTable-finalWagersleft");//Away/Over
        try//To catch missing consensus data due to delayed or cancelled game
        {
            ouUnder = rightConsensus.select("div").get(1).text();
            ouOver = leftConsensus.select("div").get(1).text();
            atsHome = leftConsensus.select("div").get(0).text();
            atsAway = rightConsensus.select("div").get(0).text();
        }
        catch (Exception e)
        {
            System.out.println("DC127 DataCollector, no consensus data");
        }
        ouOversMap.put(MatchupID, ouOver);
        ouUndersMap.put(MatchupID, ouUnder);
        atsHomesMap.put(MatchupID, atsAway);
        atsAwaysMap.put(MatchupID, atsHome);
    }
    public void getOdds(WebDriver driver, String dataEventId, HashMap xRefMap)
    {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(10));
            String dataGame = (String)xRefMap.get(dataEventId);
            driver.get("https://www.covers.com/sport/football/nfl/odds");//Get current year odds & betting lines
            /////////////Click on bet menu
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#__betMenu")));
            WebElement bm = driver.findElement(By.cssSelector("#__betMenu"));
            bm.click();
            System.out.println("GetOdds147 clicked on betMenu");
            //////////Click on Moneyline
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-value=moneyline]")));
            WebElement ml = driver.findElement(By.cssSelector("[data-value=moneyline]"));//Moneyline
            ml.click();
            System.out.println("GetOdds152 clicked on Moneyline");
            ///////////////Get bet365 awayOdds
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#__moneylineDiv-nfl-265283 > table:nth-child(2) > tbody:nth-child(3) > tr:nth-child(3) > td:nth-child(9) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)")));
//            WebElement we = driver.findElement(By.cssSelector("#__moneylineDiv-nfl-265283 > table:nth-child(2) > tbody:nth-child(3) > tr:nth-child(3) > td:nth-child(9) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)"));
//            System.out.println("getOdds156 =================== " + we.getText());
            ///////////////Get bet365 homeOdds
            String selHome = "#__moneylineDiv-nfl-265283 > table:nth-child(2) > tbody:nth-child(3) > tr:nth-child(3) > td:nth-child(9) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)";
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#__moneylineDiv-nfl-265283 > table:nth-child(2) > tbody:nth-child(3) > tr:nth-child(3) > td:nth-child(9) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)")));
//            WebElement we2 = driver.findElement(By.cssSelector("#__moneylineDiv-nfl-265283 > table:nth-child(2) > tbody:nth-child(3) > tr:nth-child(3) > td:nth-child(9) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)"));
//            System.out.println("getOdds161 =================== " + we2.getText());
            //System.out.println("getOdds162 we*******************************bet365, data-game => " + "---, " + " data-book='bet365, awayOdds' => " + we.getText());
//            System.out.println("getOdds163 we*******************************bet365, data-game => " + "---, " + " data-book='bet365, homeOdds' => " + we2.getText());
    }
    public HashMap<String, String> getHomeFullNameMap()
    {
        return homeFullNameMap;
    }
    public HashMap<String, String> getAwayFullNameMap()
    {
        return awayFullNameMap;
    }
    public HashMap<String, String> getGameDatesMap()
    {
        return gameDatesMap;
    }
    public HashMap<String, String> getAtsHomesMap()
    {
        return atsHomesMap;
    }
    public HashMap<String, String> getAtsAwaysMap()
    {
        return atsAwaysMap;
    }
    public HashMap<String, String> getOuOversMap()
    {
        return ouOversMap;
    }
    public HashMap<String, String> getOuUndersMap()
    {
        return ouUndersMap;
    }
    public HashMap<String, String> getGameIdentifierMap(){return gameIdentifierMap;}
    public void setThisSeason(String thisSeason)
    {
        this.thisSeason = thisSeason;
    }
    public String getAwayTeamCompleteName()
    {
        return awayTeamCompleteName;
    }
    public String getHomeTeamCompleteName()
    {
        return homeTeamCompleteName;
    }
    public void setCityNameMap(HashMap<String, String> cityNameMap)
    {
        this.cityNameMap = cityNameMap;
    }
}



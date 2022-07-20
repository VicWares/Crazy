package org.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version crazy 220720A
 * Builds data event id array and calendar date array
 *******************************************************************/
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashMap;
public class DataCollector
{
    private static HashMap<String, String> bet365OAwayOddsMap = new HashMap<>();
    private static HashMap<String, String> bet365OHomeOddsMap = new HashMap<>();
    private String dataEventId;
    private String homeTeamNickname;//e.g. Browns...data-home-team-nickname-search
    private String awayTeamNickname;//e.g Texans...data-away-team-nickname-search
    private String awayTeamCityName;//e.g. Cleveland...data-home-team-fullname-search
    private String homeTeamCityName;//e.g Houston...data-home-team-fullname-search
    private String awayTeamCityPlusNickName;//e.g. Kansas City Chiefs
    private static String homeTeamCityPlusNickName;//e.g Houston Texans
    private String gameIdentifier;//Column A e.g 2020 - Houston Texans @ Kansas City Chiefs
    private String awayTeamScore;
    private String homeTeamScore;
    private String gameDate;
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
    private HashMap<String, String> atsHomesMap = new HashMap<>();
    private HashMap<String, String> atsAwaysMap = new HashMap<>();
    private HashMap<String, String> ouUndersMap = new HashMap<>();
    private HashMap<String, String> ouOversMap = new HashMap<>();
    public void collectTeamInfo(Elements weekElements)//From covers.com website for this week's matchups
    {
        for (Element e : weekElements)//Build week matchup IDs array
        {
            homeTeamCityName = e.attr("data-home-team-fullname-search");//e.g. Houston
            awayTeamCityName = e.attr("data-away-team-fullname-search");//e.g. Dallas
            homeTeamNickname = e.attr("data-home-team-nickname-search");//e.g. Texans
            awayTeamNickname = e.attr("data-away-team-nickname-search");//e.g. Cowboys
            homeTeamCity = e.attr("data-home-team-city-search");
            homeTeamCity = (homeTeamCityName);//To correct for NFL stndard city names
            homeTeamCityPlusNickName = homeTeamCityName + " " + homeTeamNickname;
            awayTeamCityPlusNickName = awayTeamCityName + " " +  awayTeamNickname;
            awayTeamCityName = e.attr("data-away-team-city-search");
            awayTeamCityName = (awayTeamCityName);
            awayTeamCityPlusNickName = awayTeamCityName + " " + awayTeamNickname;
            gameIdentifier = thisSeason + " - " + awayTeamCityPlusNickName + " @ " + homeTeamCityPlusNickName;
            dataEventId = e.attr("data-event-id");
            String[] gameDateTime = e.attr("data-game-date").split(" ");
            gameDate = gameDateTime[0];
            awayTeamScore = e.attr("data-away-score");
            thisWeek = e.attr("data-competition-type");
            thisWeekGameDates.add(gameDate);
            gameDatesMap.put(dataEventId, gameDate);
            gameIdentifierMap.put(dataEventId, gameIdentifier);
            thisWeekHomeTeams.add(homeTeamCityPlusNickName);
            thisWeekAwayTeams.add(awayTeamCityPlusNickName);
            homeFullNameMap.put(dataEventId, homeTeamCityName);
            awayFullNameMap.put(dataEventId, awayTeamCityName);
            thisWeekHomeTeamScores.add(homeTeamScore);
            thisWeekAwayTeamScores.add((awayTeamScore));
            thisGameWeekNumbers.add(thisWeek);
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
            ouUnder = "no data";
            ouOver = "no data";
            atsHome = "no data";
            atsAway = "no data";
        }
        ouOversMap.put(MatchupID, ouOver);
        ouUndersMap.put(MatchupID, ouUnder);
        atsHomesMap.put(MatchupID, atsAway);
        atsAwaysMap.put(MatchupID, atsHome);
    }
    public String collectAwayOdds(String dataGame, Elements soupOddsElements)
    {
        String awayOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='moneyline'] .__awayOdds .American.__american").text();
        String homeOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='moneyline'] .__homeOdds .American.__american").text();
        bet365OAwayOddsMap.put(dataEventId, awayOddsString);
        return awayOddsString;
    }public String collectHomeOdds(String dataGame, Elements soupOddsElements)
    {
        String homeOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='moneyline'] .__homeOdds .American.__american").text();
        bet365OHomeOddsMap.put(dataEventId, homeOddsString);
        return homeOddsString;
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
    public HashMap<String, String> getGameIdentifierMap()
    {
        return gameIdentifierMap;
    }
    public String getAwayTeamCityPlusNickName()
    {
        return awayTeamCityPlusNickName;
    }
    public String getHomeTeamCompleteName()
    {
        return homeTeamCityPlusNickName;
    }
}



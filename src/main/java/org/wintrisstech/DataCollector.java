package org.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version crazy 220801
 * Builds data event id array and calendar date array
 *******************************************************************/
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
public class DataCollector
{
    private String dataEventId;
    private String awayTeamCityPlusNickName;//e.g. Kansas City Chiefs
    private static String homeTeamCityPlusNickName;//e.g. Houston Texans
    private final HashMap<String, String> gameDatesMap = new HashMap<>();
    private final HashMap<String, String> gameIdentifierMap = new HashMap<>();
    private final HashMap<String, String> homeFullNameMap = new HashMap<>();
    private final HashMap<String, String> awayFullNameMap = new HashMap<>();
    private final HashMap<String, String> atsHomesMap = new HashMap<>();
    private final HashMap<String, String> atsAwaysMap = new HashMap<>();
    private final HashMap<String, String> ouUndersMap = new HashMap<>();
    private final HashMap<String, String> ouOversMap = new HashMap<>();
    public void collectTeamInfo(Elements weekElements)//From covers.com website for this week's matchups
    {
        for (Element e : weekElements)//Build week matchup IDs array
        {
            //e.g Houston...data-home-team-fullname-search
            String homeTeamCityName = e.attr("data-home-team-fullname-search");//e.g. Houston
            //e.g. Cleveland...data-home-team-fullname-search
            String awayTeamCityName = e.attr("data-away-team-fullname-search");//e.g. Dallas
            //e.g. Browns...data-home-team-nickname-search
            String homeTeamNickname = e.attr("data-home-team-nickname-search");//e.g. Texans
            //e.g Texans...data-away-team-nickname-search
            String awayTeamNickname = e.attr("data-away-team-nickname-search");//e.g. Cowboys
            //String homeTeamCity = e.attr("data-home-team-city-search");
            //homeTeamCity = (homeTeamCityName);//To correct for NFL stndard city names TODO:Fix this
            homeTeamCityPlusNickName = homeTeamCityName + " " + homeTeamNickname;
            awayTeamCityPlusNickName = awayTeamCityName + " " + awayTeamNickname;
            awayTeamCityName = e.attr("data-away-team-city-search");
            //awayTeamCityName = (awayTeamCityName);
            awayTeamCityPlusNickName = awayTeamCityName + " " + awayTeamNickname;
            //Column A e.g. 2020 - Houston Texans @ Kansas City Chiefs
            String thisSeason = "2022";
            String gameIdentifier = thisSeason + " - " + awayTeamCityPlusNickName + " @ " + homeTeamCityPlusNickName;
            dataEventId = e.attr("data-event-id");
            String[] gameDateTime = e.attr("data-game-date").split(" ");
            String gameDate = gameDateTime[0];
            gameDatesMap.put(dataEventId, gameDate);
            gameIdentifierMap.put(dataEventId, gameIdentifier);
            homeFullNameMap.put(dataEventId, homeTeamCityName);
            awayFullNameMap.put(dataEventId, awayTeamCityName);
        }
    }
    public void collectConsensusData(Elements Consensus, String MatchupID)
    {
        this.dataEventId = MatchupID;
        String ouOver;
        String ouUnder;
        String atsHome;
        String atsAway;
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
    public String collectMoneyLineAwayOdds(String dataGame, Elements soupOddsElements)
    {
        String awayOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='moneyline'] .__awayOdds .American.__american").text();
        return awayOddsString;
    }
    public String collectMoneyLineHomeOdds(String dataGame, Elements soupOddsElements)
    {
        String homeOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='moneyline'] .__homeOdds .American.__american").text();
        return homeOddsString;
    }
    public String collectSpreadHomeOdds(String dataGame, Elements soupOddsElements)
    {
        String awaySpreadOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='spread'] .__awayOdds .American.__american").text();
        String[] split = awaySpreadOddsString.split(" ");
        return split[2];
    }
    public String collectSpreadAwayOdds(String dataGame, Elements soupOddsElements)
    {
        String awaySpreadOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='spread'] .__awayOdds .American.__american").text();
        String[] split = awaySpreadOddsString.split(" ");
        return split[0];
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



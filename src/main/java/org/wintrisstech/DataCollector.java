package org.wintrisstech;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version crazy 220805
 * Builds data event id array and calendar date array
 *******************************************************************/

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class DataCollector {
    private String dataEventId;
    private String awayCity;//e.g Seattle
    private String homeCity;//e.g.Dallas
    private String awayCityPlusNickname;//e.g. Kansas City Chiefs
    private String homeCityPlusNickname;//e.g. Houston Texans
    private final HashMap<String, String> gameDatesMap = new HashMap<>();
    private final HashMap<String, String> gameIdentifierMap = new HashMap<>();
    private final HashMap<String, String> homeCityPlusNicknameMap = new HashMap<>();
    private final HashMap<String, String> awayCityPlusNicknameMap = new HashMap<>();
    private final HashMap<String, String> atsHomesMap = new HashMap<>();
    private final HashMap<String, String> atsAwaysMap = new HashMap<>();
    private final HashMap<String, String> ouUndersMap = new HashMap<>();
    private final HashMap<String, String> ouOversMap = new HashMap<>();
    private Elements weekElements;
    private String awayNickname;
    private String homeNickname;
    private String awayCityPlusNicknme;

    public void collectTeamInfo(Elements weekElements)//From covers.com website for this week's matchups
    {
       this.weekElements = weekElements;
        for (Element e : weekElements)//Build week matchup IDs array
        {
            //e.g Houston...data-home-team-fullname-search
            homeCity = e.attr("data-home-team-fullname-search");//e.g. Houston
            //e.g. Cleveland...data-home-team-fullname-search
            String awayCity = e.attr("data-away-team-fullname-search");//e.g. Dallas
            //e.g. Browns...data-home-team-nickname-search
            homeNickname = e.attr("data-home-team-nickname-search");//e.g. Texans
            awayNickname = e.attr("data-away-team-nickname-search");//e.g. Cowboys
            //homeCity = (homeTeamCityName);//To correct for NFL stndard city names TODO:Fix this
            homeCityPlusNickname = homeCity + " " + homeNickname;
            awayCityPlusNickname = awayCity + " " + awayNickname;
            String thisSeason = "2022";
            String gameIdentifier = thisSeason + " - " + awayCityPlusNickname + " @ " + homeCityPlusNickname;//Column A e.g. 2020 - Houston Texans @ Kansas City Chiefs
            dataEventId = e.attr("data-event-id");
            String[] gameDateTime = e.attr("data-game-date").split(" ");
            String gameDate = gameDateTime[0];
            gameDatesMap.put(dataEventId, gameDate);
            gameIdentifierMap.put(dataEventId, gameIdentifier);
            homeCityPlusNicknameMap.put(dataEventId, homeCityPlusNickname);
            awayCityPlusNicknameMap.put(dataEventId, awayCityPlusNickname);
        }
    }
    private String collectAwayCityPlusNickname()
    {
        awayCity = weekElements.select("data-away-team-fullname-search").text();//e.g. Dallas
        awayNickname = weekElements.select("data-away-team-nickname-search").text();//e.g. Cowboys
        return awayCityPlusNicknme;
    } private String collectHomeCityPlusNickname()
    {
        homeCity = weekElements.select("data-home-team-fullname-search").text();//e.g. Dallas
        homeNickname = weekElements.select("data-home-team-nickname-search").text();//e.g. Cowboys
        homeCityPlusNickname = homeCity + homeNickname;
        return awayCityPlusNicknme;
    }

    public void collectConsensusData(Elements Consensus, String MatchupID) {
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
        } catch (Exception e) {
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
    public String collectMoneyLineAwayOdds(String dataGame, Elements soupOddsElements) {
        String awayOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='moneyline'] .__awayOdds .American.__american").text();
        return awayOddsString;
    }
    public String collectMoneyLineHomeOdds(String dataGame, Elements soupOddsElements) {
        String homeOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='moneyline'] .__homeOdds .American.__american").text();
        return homeOddsString;
    }
    public String collectSpreadHomeOdds(String dataGame, Elements soupOddsElements) {
        String awaySpreadOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='spread'] .__awayOdds .American.__american").text();
        String[] split = awaySpreadOddsString.split(" ");
        return split[1];//TODO: fix
    }
    public String collectSpreadAwayOdds(String dataGame, Elements soupOddsElements) {
        String awaySpreadOddsString = soupOddsElements.select("[data-book='WynnBET'][data-game='" + dataGame + "'][data-type='spread'] .__awayOdds .American.__american").text();
        String[] split = awaySpreadOddsString.split(" ");
        return split[0];
    }
    public HashMap<String, String> getHomeCityPlusNicknameMap() {
        return homeCityPlusNicknameMap;
    }

    public HashMap<String, String> getAwayCityPlusNicknameMap() {
        return awayCityPlusNicknameMap;
    }

    public HashMap<String, String> getGameDatesMap() {
        return gameDatesMap;
    }

    public HashMap<String, String> getAtsHomesMap() {
        return atsHomesMap;
    }

    public HashMap<String, String> getAtsAwaysMap() {
        return atsAwaysMap;
    }

    public HashMap<String, String> getOuOversMap() {
        return ouOversMap;
    }

    public HashMap<String, String> getOuUndersMap() {
        return ouUndersMap;
    }
    public HashMap<String, String> getGameIdentifierMap() {return gameIdentifierMap;}//e.g. 2022 - Jacksonville Jaguars @ Washington Commanders
    public String getAwayCityPlusNickname() {
        return awayCityPlusNickname;
    }
    public String getHomeCityPlusNickname() {
        return homeCityPlusNickname;
    }
    public String getHomeCity() {return homeCity;}
    public String getAwayCity() {return awayCity;}

}



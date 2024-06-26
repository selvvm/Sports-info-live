package com.crick.api.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.crick.api.entities.Match;
import com.crick.api.repositories.MatchRepo;

import com.crick.api.service.MatchService;


@Service
public class MatchServiceImpl implements MatchService{

    @Autowired
    private MatchRepo matchRepo;
    

    @Override
    public List<Match> getAllMatches() {
        return matchRepo.findAll();
    }

    @Override
    public List<Match> getLiveMatches() {
         List<Match> matches = new ArrayList<>();
        try {
            String url = "https://www.cricbuzz.com/cricket-match/live-scores";
            Document document = Jsoup.connect(url).get();
            Elements liveScoreElements = document.select("div.cb-mtch-lst.cb-tms-itm");
            for (Element match : liveScoreElements) {
                HashMap<String, String> liveMatchInfo = new LinkedHashMap<>();
                String teamsHeading = match.select("h3.cb-lv-scr-mtch-hdr").select("a").text();
                String matchNumberVenue = match.select("span").text();
                Elements matchBatTeamInfo = match.select("div.cb-hmscg-bat-txt");
                String battingTeam = matchBatTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String score = matchBatTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                Elements bowlTeamInfo = match.select("div.cb-hmscg-bwl-txt");
                String bowlTeam = bowlTeamInfo.select("div.cb-hmscg-tm-nm").text();
                String bowlTeamScore = bowlTeamInfo.select("div.cb-hmscg-tm-nm+div").text();
                String textLive = match.select("div.cb-text-live").text();
                String textComplete = match.select("div.cb-text-complete").text();
                //getting match link
                String matchLink = match.select("a.cb-lv-scrs-well.cb-lv-scrs-well-live").attr("href").toString();

                Match match1 = new Match();
                match1.setTeamHeading(teamsHeading);
                match1.setMatchNumberVenue(matchNumberVenue);
                match1.setBattingTeam(battingTeam);
                match1.setBattingTeamScore(score);
                match1.setBowlingTeam(bowlTeam);
                match1.setBowlingTeamScore(bowlTeamScore);
                match1.setLiveText(textLive);
                match1.setMatchLink(matchLink);
                match1.setTextComplete(textComplete);
                match1.SetMatchStatus();


                matches.add(match1);

//                update the match in database
                updateMatchInDb(match1);

                updateMatch(match1);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }


    private void updateMatchInDb(Match match1) {
        this.matchRepo.findByTeamHeading(match1.getTeamHeading()).ifPresent(match -> {
            match.setMatchNumberVenue(match1.getMatchNumberVenue());
            match.setBattingTeam(match1.getBattingTeam());
            match.setBattingTeamScore(match1.getBattingTeamScore());
            match.setBowlingTeam(match1.getBowlingTeam());
            match.setBowlingTeamScore(match1.getBowlingTeamScore());
            match.setLiveText(match1.getLiveText());
            match.setMatchLink(match1.getMatchLink());
            match.setTextComplete(match1.getTextComplete());
            match.SetMatchStatus();
            this.matchRepo.save(match);
        });
    }


    private void updateMatch(Match match1) {

        Match match = this.matchRepo.findByTeamHeading(match1.getTeamHeading()).orElse(null);
        if (match == null) {
            this.matchRepo.save(match1);
        } else {

            match1.setMatchId(match.getMatchId());
            this.matchRepo.save(match1);
        }

    }


    @Override
    public List<List<String>> getPointsTable() {

        List<List<String>> pointTable = new ArrayList<>();
        String tableURL = "https://www.cricbuzz.com/cricket-series/6732/icc-cricket-world-cup-2023/points-table";
        try {
            Document document = Jsoup.connect(tableURL).get();
            Elements table = document.select("table.cb-srs-pnts");
            Elements tableHeads = table.select("thead>tr>*");
            List<String> headers = new ArrayList<>();
            tableHeads.forEach(element -> {
                headers.add(element.text());
            });
            pointTable.add(headers);
            Elements bodyTrs = table.select("tbody>*");
            bodyTrs.forEach(tr -> {
                List<String> points = new ArrayList<>();
                if (tr.hasAttr("class")) {
                    Elements tds = tr.select("td");
                    String team = tds.get(0).select("div.cb-col-84").text();
                    points.add(team);
                    tds.forEach(td -> {
                        if (!td.hasClass("cb-srs-pnts-name")) {
                            points.add(td.text());
                        }
                    });
 //                    System.out.println(points);
                    pointTable.add(points);
                }


            });

            System.out.println(pointTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointTable;
    }

    @Override
    public List<List<String>> getPointsTableSuper8() {
        List<List<String>> pointsTable = new ArrayList<>();
        String url = "https://www.cricbuzz.com/cricket-series/6732/icc-mens-t20-world-cup-2024/points-table";

        try {
            Document doc = Jsoup.connect(url).get();
            Elements tables = doc.select("table.table.cb-srs-pnts");
            
            for (Element table : tables) {
                Element previousElement = table.previousElementSibling();
                if (previousElement != null && previousElement.text().contains("Super 8 Group 1")) {
                    Elements rows = table.select("tr");

                    // Add header
                    Elements headers = rows.first().select("th, td");
                    List<String> headerList = new ArrayList<>();
                    for (Element header : headers) {
                        String text = header.text().trim();
                        if (!text.isEmpty() && !text.equals("")) {
                            headerList.add(text);
                        }
                    }
                    pointsTable.add(headerList);

                    // Add team data
                    for (int i = 1; i < rows.size(); i++) {
                        Element row = rows.get(i);
                        if (!row.hasClass("cb-srs-pnts-dwn")) {
                            Elements cols = row.select("td");
                            if (!cols.isEmpty()) {
                                List<String> teamData = new ArrayList<>();
                                Element teamNameElement = row.select("td.cb-srs-pnts-name").first();
                                if (teamNameElement != null) {
                                    teamData.add(teamNameElement.text().trim()); // Team name
                                    for (Element col : cols) {
                                        String text = col.text().trim();
                                        if (!text.isEmpty() && !text.equals("-")) {
                                            teamData.add(text);
                                        }
                                    }
                                    if (teamData.size() > 1) {  // Ensure we're not adding empty rows
                                        pointsTable.add(teamData);
                                    }
                                }
                            }
                        }
                    }
                    break;  // We've found the table we want, so we can stop looking
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pointsTable;
    }
}



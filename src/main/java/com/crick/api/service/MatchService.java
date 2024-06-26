package com.crick.api.service;

import com.crick.api.entities.Match;
import java.util.List;


public interface MatchService {
   List<Match> getAllMatches();
   List<Match> getLiveMatches();
   List<List<String>>getPointsTable();
   List<List<String>>getPointsTableSuper8();
}

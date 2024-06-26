package com.crick.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.crick.api.entities.Match;


import com.crick.api.service.MatchService;



@RestController
@RequestMapping("/match")
@CrossOrigin("*")
public class MatchController {
    
    @Autowired
    private MatchService matchService;

    public MatchController(MatchService matchService){
        this.matchService = matchService;
    }

    @RequestMapping("/live")
    public ResponseEntity<List<Match>>getLiveMatches(){
       return new ResponseEntity<>(this.matchService.getLiveMatches(), HttpStatus.OK);
    }
    
    @RequestMapping("/all")
    public ResponseEntity<List<Match>>getAllMatches(){
        return new ResponseEntity<>(this.matchService.getAllMatches(), HttpStatus.OK);
    }
    
    @RequestMapping("/points")
    public ResponseEntity<List<List<String>>>getPointsTable(){
        return new ResponseEntity<>(this.matchService.getPointsTable(), HttpStatus.OK);
    }
    
    @GetMapping("/super8")
    public ResponseEntity<List<List<String>>> getPointsTableSuper8() {
        try {
             List<List<String>> pointsTable = matchService.getPointsTableSuper8();
            System.out.println("Points Table Size: " + pointsTable.size()); // Check the size of the points table
            if (pointsTable.isEmpty()) {
                System.out.println("Points Table is Empty!"); // Log a message if the points table is empty
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pointsTable, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


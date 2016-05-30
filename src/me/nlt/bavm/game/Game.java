package me.nlt.bavm.game;

import java.util.HashMap;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamCoefficients.StatCoefficient;

public class Game
{
    public static int[] simulateGame(int homeID, int visitorID) 
    {
    	int[] goalResult = new int[2];
    	
    	Team home = BAVM.getTeamManager().getTeam(homeID);
    	Team visitor = BAVM.getTeamManager().getTeam(visitorID);
    	
    	HashMap<StatCoefficient, Double> homeCoefficients = home.getTeamInfo().getTeamCoefficients().getStatCoefficients();
    	HashMap<StatCoefficient, Double> visitorCoefficients = visitor.getTeamInfo().getTeamCoefficients().getStatCoefficients();
    	
    	for (StatCoefficient statCoeffcient : homeCoefficients.keySet()) 
    	{
    		System.out.println(homeCoefficients.get(statCoeffcient));
    	}
    	
    	return goalResult;
    }
}

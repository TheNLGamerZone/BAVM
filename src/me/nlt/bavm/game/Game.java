package me.nlt.bavm.game;

import java.util.HashMap;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo.StatCoefficient;

public class Game
{
    public static int[] simulateGame(int homeID, int visitorID) 
    {
    	int[] goalResult = new int[2];
    	
    	Team home = BAVM.getTeamManager().getTeam(homeID);
    	Team visitor = BAVM.getTeamManager().getTeam(visitorID);
    	
    	HashMap<StatCoefficient, Double> homeCoefficients = home.getTeamInfo().getStatCoefficients();
    	HashMap<StatCoefficient, Double> visitorCoefficients = visitor.getTeamInfo().getStatCoefficients();
    	
    	for (StatCoefficient statCoefficient : homeCoefficients.keySet())
    	{
    		System.out.println(homeCoefficients.get(statCoefficient));
    	}

        for (StatCoefficient statCoefficient : visitorCoefficients.keySet())
        {
            System.out.println(visitorCoefficients.get(statCoefficient));
        }
    	
    	return goalResult;
    }
}

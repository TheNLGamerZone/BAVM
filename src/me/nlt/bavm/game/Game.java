package me.nlt.bavm.game;

import java.util.HashMap;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo;
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

        double homeLuck = Math.random();
        double visitorLuck = Math.random();

        double homeValues[] = new double[homeCoefficients.size()];
        double visitorValues[] = new double[visitorCoefficients.size()];

        for (int i = 0; i < homeCoefficients.size(); i++) {
            homeValues[i] = (1.2 * home.getTeamInfo().getCoefficientValue(i) / 100) + (0.8 * homeLuck + 0.5);
            visitorValues[i] = (1.2 * visitor.getTeamInfo().getCoefficientValue(i) / 100) + (0.8 * visitorLuck + 0.5);
        }


    	
    	return goalResult;
    }
}

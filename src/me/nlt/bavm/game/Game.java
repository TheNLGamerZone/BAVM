package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo.StatCoefficient;

import java.util.HashMap;

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
            System.out.println("HOME - " + statCoefficient.name() + ": " + homeCoefficients.get(statCoefficient));
        }

        for (StatCoefficient statCoefficient : visitorCoefficients.keySet())
        {
            System.out.println("VISITOR - " + statCoefficient.name() + ": " + visitorCoefficients.get(statCoefficient));
        }

        double homeLuck = Math.random();
        double visitorLuck = Math.random();

        double homeValues[] = new double[homeCoefficients.size()];
        double visitValues[] = new double[visitorCoefficients.size()];

        for (int i = 0; i < homeCoefficients.size(); i++)
        {
            homeValues[i] = (1.2 * home.getTeamInfo().getCoefficientValue(i) / 100) + (0.8 * homeLuck + 0.5);
            visitValues[i] = (1.2 * visitor.getTeamInfo().getCoefficientValue(i) / 100) + (0.8 * visitorLuck + 0.5);
        }

        int time = 0;
        int ballQuarter = 0;
        int ballPossession = 0;

        while (time < 2000)
        {
            int conflictResult = getConflictResult(homeValues, visitValues, ballPossession);

            switch (conflictResult)
            {

            }

            time++;

        }


        return goalResult;
    }

    public static int getConflictResult(double[] homeValues, double[] visitValues, int ballPossession) {
        int conflictResult = 0;

        double attValues[] = new double[homeValues.length];
        double defValues[] = new double[homeValues.length];

        if (ballPossession == 0)
        {
            attValues = homeValues;
            defValues = visitValues;
        } else if (ballPossession == 1)
        {
            attValues = visitValues;
            defValues = homeValues;
        } else
        {
            return -1;
        }

        for (int i = 0; i < attValues.length; i++) {
            attValues[i] = attValues[i] * 10;
            defValues[i] = defValues[i] * 10;
        }

        //nextQuarter formula, 0=afm, 1=att, 2=pos, 3=def, 4=kep, 5=cnd
        int attackResult = (int) attValues[1] + (int) (500 * Math.random()) - (int) defValues[3] - (int) (500 * Math.random());



        return conflictResult;
    }
}

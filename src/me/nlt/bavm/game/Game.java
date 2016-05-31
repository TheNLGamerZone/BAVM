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
            homeValues[i] = 10 * ((1.2 * home.getTeamInfo().getCoefficientValue(i) / 100) + (0.8 * homeLuck + 0.5));
            visitValues[i] = 10 * ((1.2 * visitor.getTeamInfo().getCoefficientValue(i) / 100) + (0.8 * visitorLuck + 0.5));
        }

        int time = 0;
        int ballQuarter = 0;
        int ballPossession = 0;

        while (time < 2000)
        {

            int conflictResult = getConflictResult(homeValues, visitValues, ballPossession);

            switch (conflictResult)
            {
                case -1 :
                    time++;
                    break;
                case 0 :
                    if (ballPossession == 0) {
                        ballPossession = 1;
                    } else {
                        ballPossession = 0;
                    }
                    break;
                case 1 :
                    if (ballPossession == 0) {
                        ballQuarter++;
                    } else {
                        ballQuarter--;
                    }
                    break;
                case 2 :
                    time++;
                    break;
            }

            if (ballPossession == 0 && ballQuarter == 4) {
                int isGoal = getGoalResult(homeValues, visitValues, ballPossession);
            } else if (ballPossession == 1 && ballQuarter == -1) {
                int isGoal = getGoalResult(homeValues, visitValues, ballPossession);
            }

        }


        return goalResult;
    }

    public static int getConflictResult(double[] homeValues, double[] visitValues, int ballPossession) {
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

        //nextQuarter formula, 0=afm, 1=att, 2=pos, 3=def, 4=kep, 5=cnd (AKA THE MOST IMPORTANT ONE)
        double attackResult = Math.round((attValues[1] * (1 + Math.random())) - (defValues[3] * (1 + Math.random())));
        System.out.println("ATTACKRESULT: " + attackResult);

        if (attackResult < 0)
        {
            //if possession coef is high enough the team has a chance to keep the ball anyway
            attackResult = Math.round(attackResult + (0.25 * (defValues[2] * (1 + Math.random()))));
            System.out.println("ATTACKRESULT2: " + attackResult);
        }

        if (attackResult < 0)
        {
            //lose possession
            return 0;
        } else if (attackResult > 2.5)
        {
            //next quarter
            return 1;
        } else
        {
            //try again
            return 2;
        }
    }

    public static int getGoalResult(double[] homeValues, double[] visitValues, int ballPossession) {
        return 0;
    }
}

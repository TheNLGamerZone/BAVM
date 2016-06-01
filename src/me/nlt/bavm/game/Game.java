package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo.StatCoefficient;

import java.util.HashMap;
import java.util.Random;

//TODO add condition effect

public class Game
{
    public static int[] simulateGame(int homeID, int visitorID)
    {
        Random rnd = new Random();

        int[] goalResult = new int[2];

        Team home = BAVM.getTeamManager().getTeam(homeID);
        Team visitor = BAVM.getTeamManager().getTeam(visitorID);

        HashMap<StatCoefficient, Double> homeCoefficients = home.getTeamInfo().getStatCoefficients();
        HashMap<StatCoefficient, Double> visitorCoefficients = visitor.getTeamInfo().getStatCoefficients();

        for (StatCoefficient statCoefficient : homeCoefficients.keySet())
        {
            //System.out.println("HOME - " + statCoefficient.name() + ": " + homeCoefficients.get(statCoefficient));
        }

        for (StatCoefficient statCoefficient : visitorCoefficients.keySet())
        {
            //System.out.println("VISITOR - " + statCoefficient.name() + ": " + visitorCoefficients.get(statCoefficient));
        }

        double luck1 = (rnd.nextDouble() * 0.5) + 0.35;
        double tst = (rnd.nextInt(200) + 75);
        double mdTst = tst / 1000;
        double luck2 = luck1 - mdTst;
        int chooser = rnd.nextInt(2);

        double homeLuck;
        double visitorLuck;

        if (chooser == 1) {
            homeLuck = luck1;
            visitorLuck = luck2;
        } else {
            homeLuck = luck2;
            visitorLuck = luck1;
        }


        //System.out.println(homeLuck);
        //System.out.println(visitorLuck);


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
        int conflictResult = 0;
        int attemptResult;
        boolean modifyCoefficients = false;

        while (time < 90)
        {
            //BAVM.getDisplay().appendText("time: " + time);

            if (modifyCoefficients && ballPossession == 0) {
                visitValues[0] = visitValues[0] * 0.9;
                modifyCoefficients = false;
            } else if (modifyCoefficients && ballPossession == 1) {
                homeValues[0] = homeValues[0] * 0.9;
                modifyCoefficients = false;
            }

            if (conflictResult != -1) {
                conflictResult = getConflictResult(homeValues, visitValues, ballPossession);
            }

            switch (conflictResult)
            {
                case -1 :
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
                    continue;
            }

            conflictResult = 0;

            if (ballQuarter == 4 && ballPossession == 0) {
                attemptResult = getAttemptResult(homeValues, visitValues, ballPossession);
            } else if (ballQuarter == -1 && ballPossession == 1) {
                attemptResult = getAttemptResult(homeValues, visitValues, ballPossession);
            } else {
                attemptResult = -1;
            }

            switch (attemptResult) {
                case -1 :
                    time++;
                    break;
                case 0 :
                    if (ballPossession == 0) {
                        ballPossession = 1;
                        ballQuarter = 3;
                    } else {
                        ballPossession = 0;
                        ballQuarter = 0;
                    }
                    break;
                case 1 :
                    if (ballPossession == 0) {
                        goalResult[0]++;
                        ballQuarter = 1;
                        ballPossession = 1;
                        time++;
                        modifyCoefficients = true;
                    } else {
                        goalResult[1]++;
                        ballQuarter = 2;
                        ballPossession = 0;
                        time++;
                        modifyCoefficients = true;
                    }
                    break;
                case 2 :
                    conflictResult = -1;
                    break;
            }

        }



        System.out.println(goalResult[0] + "-" + goalResult[1]);
        return goalResult;
    }

    public static int getConflictResult(double[] homeValues, double[] visitValues, int ballPossession) {
        double attValues[];
        double defValues[];

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



        if (attackResult < 0)
        {
            //if possession coef is high enough the team has a chance to keep the ball anyway
            attackResult = Math.round(attackResult + (0.25 * (defValues[2] * (1 + Math.random()))));
        }

        //BAVM.getDisplay().appendText("ballpossession: " + ballPossession + ", conflictResult: " + attackResult);

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

    public static int getAttemptResult(double[] homeValues, double[] visitValues, int ballPossession) {
        double attValues[];
        double defValues[];

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

        //attempt formula, 0=afm, 1=att, 2=pos, 3=def, 4=kep, 5=cnd
        double attemptResult = Math.round((attValues[0] * (1 + Math.random())) - (defValues[4] * (1 + Math.random())));

        if (attemptResult < 0)
        {
            //if possession coef is high enough the team has a chance to keep the ball anyway
            attemptResult = Math.round(attemptResult + (0.3 * (defValues[2] * (1 + Math.random()))));
        }

        //BAVM.getDisplay().appendText("ballpossession: " + ballPossession + ", attemptResult: " + attemptResult);

        if (attemptResult < 0)
        {
            //lose possession
            return 0;
        } else if (attemptResult > 10)
        {
            //goal
            //BAVM.getDisplay().appendText("Team " + ballPossession + " has scored!");
            return 1;
        } else
        {
            //try again
            return 2;
        }
    }
}

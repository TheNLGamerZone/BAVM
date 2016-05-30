package me.nlt.bavm.game;


import java.util.HashMap;
import java.util.Random;

public class Game
{
    /*public static void initSim(int teamID0, int teamID1) {
        //TODO get teams from teamID (Tim jij moet een teammanager maken)
    }*/

    private static HashMap<Coefficient, Double> coefficients = new HashMap<>();

    public String getMatchResult()
    {

        int goal0 = 0;
        int goal1 = 0;

        for (int i = 0; i < 5; i++)
        {
            goal0 = goal0 + simulateGame()[0];
            goal1 = goal1 + simulateGame()[1];
        }

        goal0 = Math.round(goal0 / 5);
        goal1 = Math.round(goal1 / 5);

        return goal0 + "-" + goal1;
    }

    public enum Coefficient
    {
        AFMCOEF0(0), AFMCOEF1(1), ATTCOEF0(2), ATTCOEF1(3), POSCOEF0(4), POSCOEF1(5),
        DEFCOEF0(6), DEFCOEF1(7), KEP0(8), KEP1(9), CNDCOEF0(10), CNDCOEF1(11);
        private int index;

        /**
         * Stat constructor
         *
         * @param index Index
         */
        private Coefficient(int index)
        {
            this.index = index;
        }

        /**
         * Stuurt een int terug die staat voor de standaard plek in arrays voor deze skill
         *
         * @return Standaard plek voor deze skill
         */
        public int getIndex()
        {
            return this.index;
        }
    }

    //0=next minute, same status, 1=change possession, 2=next quarter
    public static int getConflictResult(HashMap<Coefficient, Double> coefficients, double luck0, double luck1, int ballQuarter, int ballPossession)
    {
        Random rnd = new Random();

        if (ballPossession == 0)
        {

            double quarterNumber = 0;

            switch (ballQuarter)
            {
                case 0:
                    quarterNumber = 1;
                    break;
                case 1:
                    quarterNumber = 0.975;
                    break;
                case 2:
                    quarterNumber = 0.95;
                    break;
                case 3:
                    quarterNumber = 0.85;
                    break;
            }
            double forwardNumber = 1.3 * quarterNumber * ((luck0 + (rnd.nextInt(2)))) * coefficients.get(Coefficient.ATTCOEF0);
            double pressureNumber = 1 * ((luck1 + (rnd.nextInt(2)))) * coefficients.get(Coefficient.DEFCOEF1);
            double possessionNumber = 0.25 * coefficients.get(Coefficient.POSCOEF0);

            double forwardResult = forwardNumber - pressureNumber;

            System.out.println("forwardresult for team0: " + forwardResult);

            if (forwardResult < 0)
            {
                forwardResult = forwardResult + possessionNumber;

                if (forwardResult < 0)
                {
                    return 1;
                } else
                {
                    return 0;
                }
            } else if (forwardResult < 1)
            {
                return 0;
            } else
            {
                return 2;
            }
        } else
        {

            double quarterNumber = 0;

            switch (ballQuarter)
            {
                case 3:
                    quarterNumber = 1;
                    break;
                case 2:
                    quarterNumber = 0.975;
                    break;
                case 1:
                    quarterNumber = 0.950;
                    break;
                case 0:
                    quarterNumber = 0.850;
                    break;
            }
            double forwardNumber = 1.3 * quarterNumber * ((luck1 + (rnd.nextInt(2))) * coefficients.get(Coefficient.ATTCOEF1));
            double pressureNumber = 1 * ((luck0 + (rnd.nextInt(2))) * coefficients.get(Coefficient.DEFCOEF0));
            double possessionNumber = 0.25 * coefficients.get(Coefficient.POSCOEF1);

            double forwardResult = forwardNumber - pressureNumber;

            System.out.println("forwardresult for team1: " + forwardResult);

            if (forwardResult < 0)
            {
                forwardResult = forwardResult + possessionNumber;

                if (forwardResult < 0)
                {
                    return 1;
                } else
                {
                    return 0;
                }
            } else if (forwardResult < 1)
            {
                return 0;
            } else
            {
                return 2;
            }
        }
    }

    //0=new goal chance, 1=fail, 2=goal, 3=back to final ball quarter
    public static int getGoalChanceResult(HashMap<Coefficient, Double> coefficients, double luck0, double luck1, int ballPossession)
    {
        Random rnd = new Random();

        if (ballPossession == 0)
        {

            double goalNumber = coefficients.get(Coefficient.AFMCOEF0) * (luck0 + rnd.nextInt(2) + 0.5) - (coefficients.get(Coefficient.KEP1) * (luck1 + rnd.nextInt(2) + 0.5));
            double possessionNumber = 0.33 * coefficients.get(Coefficient.POSCOEF0);

            System.out.println("goalnumber attempt from 0: " + goalNumber);

            if (goalNumber < 0)
            {
                goalNumber = goalNumber + possessionNumber;
                if (goalNumber < 0)
                {
                    return 1;
                } else
                {
                    return 3;
                }
            } else if (goalNumber < 1)
            {
                return 0;
            } else
            {
                return 2;
            }
        } else
        {
            double goalNumber = coefficients.get(Coefficient.AFMCOEF1) * (luck1 + rnd.nextInt(2) + 0.5) - (coefficients.get(Coefficient.KEP0) * (luck0 + rnd.nextInt(2) + 0.5));
            double possessionNumber = 0.33 * coefficients.get(Coefficient.POSCOEF1);

            System.out.println("goalnumber attempt from 1: " + goalNumber);

            if (goalNumber < 0)
            {
                goalNumber = goalNumber + possessionNumber;
                if (goalNumber < 0)
                {
                    return 1;
                } else
                {
                    return 3;
                }
            } else if (goalNumber < 1)
            {
                return 0;
            } else
            {
                return 2;
            }
        }
    }

    public static int[] simulateGame()
    {
        //TODO base coefficients on team stats

        Random rnd = new Random();

        int ballQuarter = 0;

        //0=team0, 1=team1, -1=afpakmoment team1, -2=afpakmoment team0
        int ballPossession = 0;

        int counter = 1;
        for (Coefficient coefficient : Coefficient.values())
        {
            if (counter % 2 != 0)
            {
                //team0
                coefficients.put(coefficient, 100.0);
            } else
            {
                //team1
                coefficients.put(coefficient, 1000.0);
            }
            counter++;
        }

        double luck0 = rnd.nextInt(3) - 0.5;
        double luck1 = rnd.nextInt(3) - 0.5;

        System.out.println("luck0: " + luck0 + " and luck1: " + luck1);

        int goal0 = 0;
        int goal1 = 0;

        double cndPart = (coefficients.get(Coefficient.CNDCOEF0) + coefficients.get(Coefficient.CNDCOEF1)) / 1.95;

        double cndModifier0 = 0.95 * (coefficients.get(Coefficient.CNDCOEF0) / cndPart);
        double cndModifier1 = 0.95 * (coefficients.get(Coefficient.CNDCOEF1) / cndPart);

        for (int minute = 1; minute <= 900; minute++)
        {
            System.out.println("minute: " + minute);

            if (minute % 150 == 0)
            {
                counter = 1;

                for (Coefficient coef : Coefficient.values())
                {
                    if (counter % 2 != 0)
                    {
                        if (counter < 11)
                        {
                            coefficients.put(coef, coefficients.get(coef) * cndModifier0);
                        } else
                        {
                            coefficients.put(coef, coefficients.get(coef));
                        }
                    } else
                    {
                        if (counter < 11)
                        {
                            coefficients.put(coef, coefficients.get(coef) * cndModifier1);
                        } else
                        {
                            coefficients.put(coef, coefficients.get(coef));
                        }
                    }
                    counter++;
                }
            }

            if (ballPossession == -2)
            {
                ballPossession = 0;
            } else if (ballPossession == -1)
            {
                ballPossession = 1;
            }

            System.out.println("ballquarter: " + ballQuarter + " and ball possesion is " + ballPossession);

            while (ballPossession == 0)
            {
                int conflictResult = getConflictResult(coefficients, luck0, luck1, ballQuarter, 0);

                if (conflictResult == 1)
                {
                    ballPossession = 1;
                } else if (conflictResult == 2)
                {
                    ballQuarter++;
                    System.out.println("ballquarter: " + ballQuarter);
                } else
                {
                    ballPossession = -2;
                }

                while (ballQuarter == 4)
                {
                    System.out.println("team0 attempts to score");

                    int goalChanceResult = getGoalChanceResult(coefficients, luck0, luck1, 0);

                    if (goalChanceResult == 2)
                    {
                        goal0++;
                        System.out.println("team 0 has scored!");
                        System.out.println("the current stance is " + goal0 + "-" + goal1);
                        ballQuarter = 1;
                        ballPossession = 1;
                    } else if (goalChanceResult == 1)
                    {
                        ballQuarter = 3;
                        ballPossession = 1;
                    } else if (goalChanceResult == 3)
                    {
                        ballQuarter = 3;
                    } else
                    {
                        ballQuarter = 4;
                    }
                }
            }

            while (ballPossession == 1)
            {
                int conflictResult = getConflictResult(coefficients, luck0, luck1, ballQuarter, 1);

                if (conflictResult == 1)
                {
                    ballPossession = 0;
                } else if (conflictResult == 2)
                {
                    ballQuarter--;
                    System.out.println("ballquarter: " + ballQuarter);
                } else
                {
                    ballPossession = -1;
                }

                while (ballQuarter == -1)
                {
                    System.out.println("team1 attempts to score");

                    int goalChanceResult = getGoalChanceResult(coefficients, luck0, luck1, 1);

                    if (goalChanceResult == 2)
                    {
                        goal1++;
                        ballQuarter = 2;
                        ballPossession = 0;
                    } else if (goalChanceResult == 1)
                    {
                        ballQuarter = 0;
                        ballPossession = 0;
                    } else if (goalChanceResult == 3)
                    {
                        ballQuarter = 0;
                    } else
                    {
                        ballQuarter = -1;
                    }
                }
            }
        }

        /*
        while (minute < 90) {
            System.out.println(minute);

            if (ballPossession == -2) {
                if (ballQuarter > 1) {
                    defCoef0 = defCoef0 * 0.6;
                    ballPossession = 0;
                    counter = true;
                } else {
                    ballPossession = 0;
                }
            } else if (ballPossession == -1){
                if (ballQuarter < 2) {
                    defCoef0 = defCoef1 * 0.6;
                    ballPossession = 1;
                    counter = true;
                } else {
                    ballPossession = 1;
                }
            }

            while (ballPossession == 0) {
                System.out.println(minute);
                double quarterNumber = 0;

                switch (ballQuarter) {
                    case 0 : quarterNumber = 1.0;
                        break;
                    case 1 : quarterNumber = 0.8;
                        break;
                    case 2 : quarterNumber = 0.5;
                        break;
                    case 3 : quarterNumber = 0.3;
                        break;
                }
                double forwardNumber = quarterNumber * (0.5 * luck0 * (Math.random() + 0.5)) * attCoef0;
                double pressureNumber = (0.5 * luck1 * (Math.random() + 0.5)) * defCoef1;

                double conflictResult = forwardNumber - pressureNumber;

                if (conflictResult > 1) {
                    ballQuarter++;
                } else if (conflictResult < 0) {
                    ballPossession = -1;
                }

                while (ballQuarter > 3 && ballPossession == 0) {
                    double goalNumber = (afmCoef0 * luck0 * (Math.random() + 0.5)) - (kep1 * luck1 * (Math.random() + 0.5));

                    if (goalNumber > 1) {
                        goal0++;
                        ballPossession = 1;
                        ballQuarter = 1;
                    } else if (goalNumber < 0) {
                        ballPossession = -1;
                    }
                }

                defCoef1 = (counter == true) ? defCoef1 / 0.6 : defCoef1;
                counter = (counter == true) ? counter = false : counter;
            }

            while (ballPossession == 1) {
                double quarterNumber = 0;

                switch (ballQuarter) {
                    case 0 : quarterNumber = 0.3;
                        break;
                    case 1 : quarterNumber = 0.5;
                        break;
                    case 2 : quarterNumber = 0.8;
                        break;
                    case 3 : quarterNumber = 1.0;
                        break;
                }
                double forwardNumber = quarterNumber * (0.5 * luck1 * (Math.random() + 0.5)) * attCoef1;
                double pressureNumber = (0.5 * luck0 * (Math.random() + 0.5)) * defCoef0;

                double conflictResult = forwardNumber - pressureNumber;

                if (conflictResult > 1) {
                    ballQuarter--;
                } else if (conflictResult < 0) {
                    ballPossession = -2;
                }

                while (ballQuarter < 2 && ballPossession == 1) {
                    double goalNumber = (afmCoef1 * luck1 * (Math.random() + 0.5)) - (kep0 * luck0 * (Math.random() + 0.5));

                    if (goalNumber > 1) {
                        goal1++;
                        ballPossession = 0;
                        ballQuarter = 2;
                    } else if (goalNumber < 0) {
                        ballPossession = -2;
                    }
                }

                defCoef0 = (counter == true) ? defCoef0 / 0.6 : defCoef0;
                counter = (counter == true) ? counter = false : counter;
            }

            minute++;
        } */

        int roundedGoal0 = Math.round(goal0 / 100);
        int roundedGoal1 = Math.round(goal1 / 100);

        System.out.println("the final match result is " + roundedGoal0 + "-" + roundedGoal1);

        int goals[] = {roundedGoal0, roundedGoal1};

        return goals;
    }
}

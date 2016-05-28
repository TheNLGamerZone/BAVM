package me.nlt.bavm.game;


import java.util.HashMap;

public class Game {
    /*public static void initSim(int teamID0, int teamID1) {
        //TODO get teams from teamID (Tim jij moet een teammanager maken)
    }*/

    private HashMap<Coefficient, Double> coefficients = new HashMap<>();

    public enum Coefficient {
        AFMCOEF0(0), AFMCOEF1(1), ATTCOEF0(2), ATTCOEF1(3), POSCOEF0(4), POSCOEF1(5),
        DEFCOEF0(6), DEFCOEF1(7), CNDCOEF0(8), CNDCOEF1(9), KEP0(10), KEP1(11);
        private int index;

        /**
         * Stat constructor
         * @param index Index
         */
        private Coefficient(int index)
        {
            this.index = index;
        }

        /**
         * Stuurt een int terug die staat voor de standaard plek in arrays voor deze skill
         * @return Standaard plek voor deze skill
         */
        public int getIndex()
        {
            return this.index;
        }
    }

    //0=next minute, same status, 1=change possession, 2=next quarter
    public int getConflictResult(HashMap<Coefficient, Double> coefficients, double luck0, double luck1, int ballQuarter, int ballPossession) {
        return 0;
    }

    public int isGoal(HashMap<Coefficient, Double> coefficients, double luck0, double luck1, int ballPossession) {
        return 0;
    }

    public String simulateGame() {
        //TODO base coefficients on team stats

        int ballQuarter = 0;

        //0=team0, 1=team1, -1=afpakmoment team1, -2=afpakmoment team2
        int ballPossession = 0;

        int counter = 1;
        for (Coefficient coefficient : Coefficient.values()) {
            if (counter % 2 == 0) {
                coefficients.put(coefficient, 1.0);
            } else {
                coefficients.put(coefficient, 1.5);
            }
            counter++;
        }

        double luck0 = Math.random() + 0.5;
        double luck1 = Math.random() + 0.5;

        int goal0 = 0;
        int goal1 = 0;

        for (int minute = 1; minute <= 90; minute++) {
            System.out.println(minute);

            if (ballPossession == -2) {
                ballPossession = 0;
            } else if (ballPossession == -1) {
                ballPossession = 1;
            }

            while (ballPossession == 0) {
                int conflictResult = getConflictResult(coefficients, luck0, luck1, ballQuarter, ballPossession);

                if (conflictResult == 1) {
                    ballPossession = 1;
                } else if (conflictResult == 2) {
                    ballQuarter++;
                } else {
                    ballPossession = -2;
                }
            }

            while (ballPossession == 1) {
                int conflictResult = getConflictResult(coefficients, luck0, luck1, ballQuarter, ballPossession);

                if (conflictResult == 1) {
                    ballPossession = 0;
                } else if (conflictResult == 2) {
                    ballQuarter++;
                } else {
                    ballPossession = -1;
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

        return goal0 + "-" + goal1;
    }
}

package me.nlt.bavm.game;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerStats;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo;
import me.nlt.bavm.teams.team.TeamInfo.StatCoefficient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

//TODO add condition effect

public class Game
{
    private int[] gameResult;
    private ArrayList<String> gameLog = new ArrayList<>();

    /**
     * Deze methode is de constructor voor het Game object, dat op basis van de team id's van het thuis- en uitteam de wedstrijd simuleert,
     * de twee variabelen zijn de gameResult (uitslag) en gameLog (log vanwedstrijdverloop
     */
    public Game(int homeID, int visitorID)
    {
        /*
         * simulateGame simuleert een voetbalwedstrijd met enkel de id van de twee teams, een home team en een visitor.
         * Verder zijn er nog twee andere methodes die helpen bij het simuleren.
         */

        //een instance van random wordt gemaakt voor willekeurige getallen en een array met als eerste waarde de goals van home en als tweede van visit
        Random rnd = new Random();
        int[] goalResult = new int[2];

        //er worden twee team objecten geimporteerd op basis van de id's
        Team home = BAVM.getTeamManager().getTeam(homeID);
        Team visitor = BAVM.getTeamManager().getTeam(visitorID);

        //de coefficienten worden geimporteerd, dus alle gemiddelde stats
        HashMap<StatCoefficient, Double> homeCoefficients = home.getTeamInfo().getStatCoefficients();
        HashMap<StatCoefficient, Double> visitorCoefficients = visitor.getTeamInfo().getStatCoefficients();

        /*
         * Geluk van de wedstrijd wordt hier aangemaakt. Er is altijd een team met minder geluk en een willekeruig getal voorspelt welke.
         * Het verschil zit altijd tussen de 0.075 en 0.275
         * De hoge gelukwaarde zit altijd tussen de 0.35 en 0.85.
         */
        double luck1 = (rnd.nextDouble() * 0.5) + 0.35;
        double tst = (rnd.nextInt(200) + 75);
        double mdTst = tst / 1000;
        double luck2 = luck1 - mdTst;
        int chooser = rnd.nextInt(2);

        double homeLuck;
        double visitorLuck;

        // Hier worden de gelukwaardes aan de teams gegeven
        if (chooser == 1)
        {
            homeLuck = luck1;
            visitorLuck = luck2;
        } else
        {
            homeLuck = luck2;
            visitorLuck = luck1;
        }

        gameLog.add(" ");

        gameLog.add(home.getTeamName() + " is the home team and " + visitor.getTeamName() + " is the visiting team.");

        gameLog.add(" ");

        gameLog.add("Home luck: " + homeLuck);
        gameLog.add("Visitor luck: " + visitorLuck);


        /*
         * Deze for loop geeft de waarde van de coefficienten aan een simpele array met enkel een index en geen key, zoals de hashmap waar de
         * coefficienten in worden opgeslagen. De coefficienten worden ook *10 gedaan voor meer verschil in de formules en de geluksfactor wordt
         * ingebracht, met de verhouding 0.6 deel stats en 0.4 deel geluk.
         */
        double homeValues[] = new double[homeCoefficients.size()];
        double visitValues[] = new double[visitorCoefficients.size()];

        for (int i = 0; i < homeCoefficients.size(); i++)
        {
            homeValues[i] = 10 * ((1.2 * home.getTeamInfo().getCoefficientValue(i) / 100) + (0.8 * homeLuck + 0.5));
            visitValues[i] = 10 * ((1.2 * visitor.getTeamInfo().getCoefficientValue(i) / 100) + (0.8 * visitorLuck + 0.5));
        }

        // Variabelen initializen
        int time = 0;
        int ballQuarter = 0;
        int ballPossession = 0;
        int conflictResult = 0;
        int attemptResult;
        boolean modifyCoefficients = false;
        boolean conditionModify = false;

        /*
         * Dit is de main while loop. Time is in dit geval de tijd in minuten, de loop kan echer ook teruggaan zonder dat de tijd meer wordt,
         * in het echt kan namelijk een team ook binnen een minuut meerdere helften vooruit gaan.
         */
        while (time < 90)
        {
            gameLog.add(" ");
            gameLog.add("Time: " + time);

            /*
             * De waardes worden aangepast op basis van de conditieskills
             */

            if (conditionModify)
            {
                for (int i = 0; i < 5; i++)
                {
                    visitValues[i] = visitValues[i] - (0.001 * (100 - visitValues[5]));
                    homeValues[i] = homeValues[i] - (0.001 * (100 - homeValues[5]));
                }

                conditionModify = false;
            }

            /*
             * Hij past de attack skill aan zodat er niet te veel goals worden gemaakt. modifyCoefficients is alleen true als er gescoord is.
             */
            if (modifyCoefficients)
            {
                if (ballPossession == 0)
                {
                    visitValues[0] = visitValues[0] * 0.850;
                    visitValues[1] = visitValues[1] * 0.925;
                } else
                {
                    homeValues[0] = homeValues[0] * 0.850;
                    homeValues[1] = homeValues[1] * 0.925;
                }
                modifyCoefficients = false;
            }

            if (conflictResult != -1)
            {
                conflictResult = getConflictResult(homeValues, visitValues, ballPossession);
                String conflictString = (ballPossession == 0) ?
                        "Conflict result of " + home.getTeamName() + " against " + visitor.getTeamName() + " on quarter " + ballQuarter + " equals " + conflictResult :
                        "Conflict result of " + visitor.getTeamName() + " against " + home.getTeamName() + " on quarter " + ballQuarter + " equals " + conflictResult;
                gameLog.add(conflictString);
            }

            /*
             * De simulator werkt op basis van een "conflict" tussen balbezitter en verdediger. Hiervoor wordt een int gemaakt die elk voor een
             * ander scenario staan, die wordt gemaakt door de methode getConflictResult.
             */
            switch (conflictResult)
            {
                case -1:
                    // Voor als er iets mis is of als er een nieuwe goal attempt gedaan moet worden
                    gameLog.add("conflictResult = -1, skipping getConflictResult...");
                    break;
                case 0:
                    // Balbezit wordt veranderd
                    if (ballPossession == 0)
                    {
                        ballPossession = 1;
                        gameLog.add(visitor.getTeamName() + " is now in possession of the ball!");
                    } else
                    {
                        ballPossession = 0;
                        gameLog.add(home.getTeamName() + " is now in possession of the ball!");
                    }
                    break;
                case 1:
                    // De ballquarter is absoluut, dus voor visit moeten ze op kwart 0 komen en voor home op kwart 3
                    if (ballPossession == 0)
                    {
                        ballQuarter++;
                        gameLog.add(home.getTeamName() + " has advanced to quarter " + ballQuarter + "!");
                    } else
                    {
                        ballQuarter--;
                        gameLog.add(visitor.getTeamName() + " has advanced to quarter " + ballQuarter + "!");
                    }
                    break;
                case 2:
                    // Tijd gaat verder en hij continued naar het einde van de loop, hij slaat goalberekening dus over
                    gameLog.add("No progress has been made!");
                    time++;
                    conditionModify = true;
                    continue;
            }

            conflictResult = 0;

            /*
             * Als het programma verder gaat wordt er gekeken of ze kwart 0 of kwart 3 hebben verslagen en dus op de volgende "kwart" zijn.
             * op basis daarvan wordt een attemptResult berekent door de methode getAttemptResult
             */
            if (ballQuarter == 4 && ballPossession == 0)
            {
                attemptResult = getAttemptResult(homeValues, visitValues, ballPossession);
                gameLog.add("Atemmpt result of " + visitor.getTeamName() + " against " + home.getTeamName() + " equals " + attemptResult);
            } else if (ballQuarter == -1 && ballPossession == 1)
            {
                attemptResult = getAttemptResult(homeValues, visitValues, ballPossession);
                gameLog.add("Attempt result of " + home.getTeamName() + " against " + visitor.getTeamName() + " equals " + attemptResult);
            } else
            {
                attemptResult = -1;
            }

            switch (attemptResult)
            {
                case -1:
                    // Er moet geen goal berekent worden
                    time++;
                    conditionModify = true;
                    break;
                case 0:
                    // Goalkans gefaald, ander krijgt de bal op hun eigen kwart
                    if (ballPossession == 0)
                    {
                        ballPossession = 1;
                        ballQuarter = 3;
                        gameLog.add("Goal attempt by " + visitor.getTeamName() + " has failed, " + home.getTeamName() + " is now in possession of the ball!");
                    } else
                    {
                        ballPossession = 0;
                        ballQuarter = 0;
                        gameLog.add("Goal attempt by " + visitor.getTeamName() + " has failed, " + home.getTeamName() + " is now in possession of the ball!");
                    }
                    break;
                case 1:
                    // Goal gelukt, modifyCoefficients zodat het volgende goal moeilijk wordt (om groot aantal goals tegen te werken)
                    if (ballPossession == 0)
                    {
                        goalResult[0]++;
                        ballQuarter = 1;
                        ballPossession = 1;
                        time++;
                        conditionModify = true;
                        modifyCoefficients = true;
                        gameLog.add(home.getTeamName() + " has scored!");
                        home.getTeamInfo().increaseTeamScores(TeamInfo.Score.GOALSFOR, 1);
                        visitor.getTeamInfo().increaseTeamScores(TeamInfo.Score.GOALSAGAINST, 1);
                    } else
                    {
                        goalResult[1]++;
                        ballQuarter = 2;
                        ballPossession = 0;
                        time++;
                        conditionModify = true;
                        modifyCoefficients = true;
                        gameLog.add(visitor.getTeamName() + " has scored!");
                        visitor.getTeamInfo().increaseTeamScores(TeamInfo.Score.GOALSFOR, 1);
                        home.getTeamInfo().increaseTeamScores(TeamInfo.Score.GOALSAGAINST, 1);
                    }

                    gameLog.add("Current stance is " + goalResult[0] + "-" + goalResult[1] + ".");
                    break;
                case 2:
                    // Opnieuw, nu skipt hij de conflict berekenaar en probeert ie opnieuw een goal totdat er een uitkomst is.
                    gameLog.add("Goal attempt failed, but they are looking for a rebound!");
                    conflictResult = -1;
                    break;
            }

        }

        gameLog.add("Final stance is " + goalResult[0] + "-" + goalResult[1] + ".");

        /*
         * Verhoogt de score van de winnende teams.
         */
        if (goalResult[0] == goalResult[1])
        {
            home.getTeamInfo().increaseTeamScores(TeamInfo.Score.DRAWS, 1);
            visitor.getTeamInfo().increaseTeamScores(TeamInfo.Score.DRAWS, 1);

            home.getTeamInfo().increaseTeamScores(TeamInfo.Score.POINTS, 1);
            visitor.getTeamInfo().increaseTeamScores(TeamInfo.Score.POINTS, 1);
        } else if (goalResult[0] > goalResult[1])
        {
            home.getTeamInfo().increaseTeamScores(TeamInfo.Score.WINS, 1);
            visitor.getTeamInfo().increaseTeamScores(TeamInfo.Score.LOSSES, 1);

            home.getTeamInfo().increaseTeamScores(TeamInfo.Score.POINTS, 3);
        } else
        {
            visitor.getTeamInfo().increaseTeamScores(TeamInfo.Score.WINS, 1);
            home.getTeamInfo().increaseTeamScores(TeamInfo.Score.LOSSES, 1);

            visitor.getTeamInfo().increaseTeamScores(TeamInfo.Score.POINTS, 3);
        }

        playerExperience(home, visitor, homeLuck, visitorLuck);

        gameResult = goalResult;
    }

    /**
     * Returnt de uitslag van de game
     *
     * @return Het gameresult
     */
    public int[] getGameResult()
    {
        return gameResult;
    }

    /**
     * Returnt de gamelog
     *
     * @return De Gamelog
     */
    public ArrayList<String> getGameLog()
    {
        return gameLog;
    }

    /**
     * Deze methode berekent het conflictResult, op basis daarvan wordt elke minuut beslist wat er gebeurt
     *
     * @param homeValues     De waardes voor het team dat thuis speelt
     * @param visitValues    De waardes voor het team dat uit speelt
     * @param ballPossession Het balbezit
     * @return Het conflict result
     */
    public static int getConflictResult(double[] homeValues, double[] visitValues, int ballPossession)
    {
        double attValues[];
        double defValues[];
        Random rnd = new Random();
        
        /*
         * Hier kijkt de methode wie de bal heeft, en past daarop aan wie de aanvaller is en wie de verdediger.
         */
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

        /*
         * nextQuarter formula, 0=afm, 1=att, 2=pos, 3=def, 4=kep, 5=cnd dit is de belangrijkste formule van de simulator.
         * eerst wordt de aanvalsstat keer een willekeurig getal gedaan dat meer is dan 1 en dan min de verdedigingsstat.
         */
        double attackResult = Math.round((attValues[1] * (1 + (rnd.nextInt(20) / 10))) - (defValues[3] * (1 + (rnd.nextInt(20) / 10))));


        if (attackResult < 0)
        {
            // Als de balbezit hoog genoeg is hebben ze een kans om alsnog de bal te houden.
            attackResult = Math.round(attackResult + (0.25 * (defValues[2] * (1 + Math.random()))));
        }

        /*
         * De scenario's
         */
        if (attackResult < 0)
        {
            //lose possession
            return 0;
        } else if (attackResult > 6)
        {
            //next quarter
            return 1;
        } else
        {
            //try again
            return 2;
        }
    }

    /**
     * Als een team de 4e helft heeft gepasseerd (vanuit hun oogpunt) wordt deze methode aangeroepen en die berekent of het goal wordt of niet
     *
     * @param homeValues     De waardes voor het team dat thuis speelt
     * @param visitValues    De waardes voor het team dat uit speelt
     * @param ballPossession Het balbezit
     * @return Het pogingresultaat
     */
    public static int getAttemptResult(double[] homeValues, double[] visitValues, int ballPossession)
    {
        // Doet hetzelfde als conflictresult maar dan met andere stats (afmaken en keeper)

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

        // Attempt formula, 0=afm, 1=att, 2=pos, 3=def, 4=kep, 5=cnd
        double attemptResult = Math.round((attValues[0] * (1 + Math.random())) - (defValues[4] * (1 + Math.random())));

        if (attemptResult < 0)
        {
            // Als de balbezit hoog genoeg is hebben ze een kans om alsnog de bal te houden.
            attemptResult = Math.round(attemptResult + (0.3 * (attValues[2] * (1 + Math.random()))));
        }

        if (attemptResult < 0)
        {
            // Lose possession
            return 0;
        } else if (attemptResult > 4)
        {
            // Goal
            return 1;
        } else
        {
            // Try again
            return 2;
        }
    }

    /**
     * Deze methode zorgt dat de spelers beter worden na de wedstrijd
     *
     * @param home        Team dat thuis speelt
     * @param visitor     Team dat uit speelt
     * @param homeLuck    Geluk van het team dat thuis speelt
     * @param visitorLuck Geluk van het team dat uit speelt
     */
    public static void playerExperience(Team home, Team visitor, double homeLuck, double visitorLuck)
    {
        double keeperBases[] = {0.4, 0.4, 0.6, 0.8, 1.0, 1.0};
        double defenderBases[] = {0.5, 0.4, 1.0, 1.0, 0.3, 1.0};
        double midfielderBases[] = {0.4, 0.9, 1.0, 0.9, 0.2, 0.8};
        double attackerBases[] = {1.0, 1.0, 0.5, 0.5, 0.2, 1.0};


        for (PlayerStats.Stat stat : PlayerStats.Stat.values())
        {
            for (int i = 0; i < 2; i++)
            {
                Team team;
                double upgradeValue;

                if (i == 0)
                {
                    team = home;
                    upgradeValue = homeLuck / 4;
                } else
                {
                    team = visitor;
                    upgradeValue = visitorLuck / 4;
                }

                // Attackers
                for (Player player : team.getTeamInfo().getPlayerPlacement().getAttackers().stream().collect(Collectors.toList()))
                {
                    upgradeValue = attackerBases[stat.getLocation()] * upgradeValue;

                    double currentStat = player.getPlayerStats().getValue(stat);
                    double upgrade = (currentStat + upgradeValue <= 100
                            ? upgradeValue
                            : upgradeValue - Math.abs((currentStat + upgradeValue) - 100));

                    player.getPlayerStats().increaseSkill(stat, upgrade);
                }

                // Midfielders
                for (Player player : team.getTeamInfo().getPlayerPlacement().getMidfielders().stream().collect(Collectors.toList()))
                {
                    upgradeValue = midfielderBases[stat.getLocation()] * upgradeValue;

                    double currentStat = player.getPlayerStats().getValue(stat);
                    double upgrade = (currentStat + upgradeValue <= 100
                            ? upgradeValue
                            : upgradeValue - Math.abs((currentStat + upgradeValue) - 100));

                    player.getPlayerStats().increaseSkill(stat, upgrade);
                }

                // Defenders
                for (Player player : team.getTeamInfo().getPlayerPlacement().getDefenders().stream().collect(Collectors.toList()))
                {
                    upgradeValue = defenderBases[stat.getLocation()] * upgradeValue;

                    double currentStat = player.getPlayerStats().getValue(stat);
                    double upgrade = (currentStat + upgradeValue <= 100
                            ? upgradeValue
                            : upgradeValue - Math.abs((currentStat + upgradeValue) - 100));

                    player.getPlayerStats().increaseSkill(stat, upgrade);
                }

                // Keepers
                Player player = team.getTeamInfo().getPlayerPlacement().getKeeper();

                upgradeValue = keeperBases[stat.getLocation()] * upgradeValue;

                double currentStat = player.getPlayerStats().getValue(stat);
                double upgrade = (currentStat + upgradeValue <= 100
                        ? upgradeValue
                        : upgradeValue - Math.abs((currentStat + upgradeValue) - 100));

                player.getPlayerStats().increaseSkill(stat, upgrade);
            }
        }
    }
}

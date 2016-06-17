package me.nlt.bavm.season;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.team.TeamInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class AllScores {
    private static ArrayList<Integer> sortedTeams = new ArrayList<>();

    public static int displayScores() {
        HashMap<Integer, Integer> points = new HashMap<>();
        HashMap<Integer, Integer> pointsCompare = new HashMap<>();
        HashMap<Integer, Integer> goalDifference = new HashMap<>();
        HashMap<Integer, Integer> goalsFor = new HashMap<>();

        for (int i = 0; i < 20; i++)
        {
            HashMap<TeamInfo.Score, Integer> teamScores = BAVM.getTeamManager().getTeam(i).getTeamInfo().getTeamScores();

            points.put(i, teamScores.get(TeamInfo.Score.POINTS));
            pointsCompare.put(i, teamScores.get(TeamInfo.Score.POINTS));
            goalDifference.put(i, teamScores.get(TeamInfo.Score.GOALSFOR) - teamScores.get(TeamInfo.Score.GOALSAGAINST));
            goalsFor.put(i,teamScores.get(TeamInfo.Score.GOALSFOR));
        }

        for (int i = 0; i < 20; i++)
        {
            //gestolen vna het internet, manier om de hoogste waarde van een hashmap te krijgen
            int key = points.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            //DEBUG
            //System.out.println("it: " + i + ", highest key: " + key);


            if (i != 0 && pointsCompare.get(sortedTeams.get(i - 1)) == pointsCompare.get(key))
            {
                int otherKey = sortedTeams.get(i - 1);
                //DEBUG
                //System.out.println(key + ", " + otherKey);

                if (goalDifference.get(sortedTeams.get(i - 1)) > goalDifference.get(key))
                {
                    sortedTeams.add(i, key);
                    points.remove(key);
                    //DEBUG
                    //System.out.println(otherKey + " > " + key);
                } else if (goalDifference.get(sortedTeams.get(i - 1)) < goalDifference.get(key))
                {
                    sortedTeams.remove(i - 1);
                    sortedTeams.add(key);
                    sortedTeams.add(otherKey);
                    points.remove(key);
                    //DEBUG
                    //System.out.println(otherKey + " < " + key);
                } else
                {
                    //DEBUG
                    //System.out.println(otherKey + " = " + key + " in goal difference");
                    if (goalsFor.get(sortedTeams.get(i - 1)) > goalsFor.get(key))
                    {
                        sortedTeams.add(i, key);
                        points.remove(key);
                        //DEBUG
                        //System.out.println(otherKey + " > " + key + " in goals for");
                    } else if (goalsFor.get(sortedTeams.get(i - 1)) < goalsFor.get(key))
                    {
                        sortedTeams.remove(i - 1);
                        sortedTeams.add(key);
                        sortedTeams.add(otherKey);
                        points.remove(key);
                        //DEBUG
                        //System.out.println(otherKey + " < " + key + " in goals for");
                    } else {
                        //DEBUG
                        //System.out.println("pleez nevu rhappin");
                        sortedTeams.add(i, key);
                        points.remove(key);
                    }
                }
            } else
            {
                sortedTeams.add(i, key);
                points.remove(key);
            }
        }

        BAVM.getDisplay().appendText("\n\t\t- - - - - - - - - - - [ Competitiestand ] - - - - - - - - - - - ",
                "\tPts\tW\tD\tL\tF\tA");

                for (int i : sortedTeams)
                {
                    String toDisplay = BAVM.getTeamManager().getTeam(i).getTeamName() + " (ID: " + i + ")" + (i == 19 ? " (Dit ben jij)" : "") + ":\n\t";

                    for (TeamInfo.Score score : TeamInfo.Score.values())
                    {
                        toDisplay = toDisplay + BAVM.getTeamManager().getTeam(i).getTeamInfo().getTeamScores().get(score) + "\t";
                    }

                    BAVM.getDisplay().appendText(toDisplay);
                }

        return sortedTeams.get(0);

        //RESET

    }
}

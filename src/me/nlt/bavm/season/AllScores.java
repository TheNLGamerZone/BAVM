package me.nlt.bavm.season;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.team.TeamInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AllScores
{
    /**
     * Deze methode laat een mooie stand zien (alle punten, doelsaldo etc.) en returnt ook de teamID van de nr. 1
     *
     * @return ID van winnende team
     */
    public static int displayScores()
    {
        ArrayList<Integer> sortedTeams = new ArrayList<>();
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
            goalsFor.put(i, teamScores.get(TeamInfo.Score.GOALSFOR));
        }

        for (int i = 0; i < 20; i++)
        {
            int key = points.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

            if (i != 0 && Objects.equals(pointsCompare.get(sortedTeams.get(i - 1)), pointsCompare.get(key)))
            {
                int otherKey = sortedTeams.get(i - 1);

                if (goalDifference.get(sortedTeams.get(i - 1)) > goalDifference.get(key))
                {
                    sortedTeams.add(i, key);
                    points.remove(key);
                } else if (goalDifference.get(sortedTeams.get(i - 1)) < goalDifference.get(key))
                {
                    sortedTeams.remove(i - 1);
                    sortedTeams.add(key);
                    sortedTeams.add(otherKey);
                    points.remove(key);
                } else
                {
                    if (goalsFor.get(sortedTeams.get(i - 1)) > goalsFor.get(key))
                    {
                        sortedTeams.add(i, key);
                        points.remove(key);
                    } else if (goalsFor.get(sortedTeams.get(i - 1)) < goalsFor.get(key))
                    {
                        sortedTeams.remove(i - 1);
                        sortedTeams.add(key);
                        sortedTeams.add(otherKey);
                        points.remove(key);
                    } else
                    {
                        // pleez nevu rhappin
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

        //Pts=punten, W=wins (gewonnen), D=draws (gelijkspellen), L=losses (verliezen), F=for (voordoelpunten), A=against (tegendoelpunten)
        BAVM.getDisplay().appendText("\t\t- - - - - - - - - - - [ Competitiestand ] - - - - - - - - - - - ",
                "\tPts\tW\tD\tL\tF\tA");

        int counter = 1;
        for (int i : sortedTeams)
        {
            String toDisplay = counter + ": " + BAVM.getTeamManager().getTeam(i).getTeamName() + " (ID: " + i + ")" + (i == 19 ? " (Dit ben jij)" : "") + ":\n\t";

            for (TeamInfo.Score score : TeamInfo.Score.values())
            {
                toDisplay = toDisplay + BAVM.getTeamManager().getTeam(i).getTeamInfo().getTeamScores().get(score) + "\t";
            }

            BAVM.getDisplay().appendText(toDisplay);
            counter++;
        }

        BAVM.getDisplay().appendText("\n");

        return sortedTeams.get(0);
    }
}

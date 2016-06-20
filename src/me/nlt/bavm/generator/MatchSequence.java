package me.nlt.bavm.generator;


import java.util.ArrayList;

public class MatchSequence
{
    /**
     * Deze methode zorgt dat er een goede volgorde van alle wedstrijden wordt gemaakt (iedereen speelt 1 keer uit en 1 keer thuis, dit wordt gedaan door de teams "door te draaien"
     * De volgorde wordt opgeslagen in een arraylist met strings in de vorm "homeID-visitorID"
     *
     * @return ArrayList met strings die staan voor de volgorde van de matches
     */
    public static ArrayList<String> getMatchSequence()
    {
        ArrayList<String> matchSequence = new ArrayList<>();

        int leftColumn[] = new int[9];
        int rightColumn[] = new int[leftColumn.length + 1];
        int teamCycle[] = new int[leftColumn.length + rightColumn.length];
        int reversedLeftColumn[] = new int[leftColumn.length];
        int adder = 2;
        int shiftAmount = 39;

        for (int i = 0; i < rightColumn.length; i++)
        {
            if (i == rightColumn.length - 1)
            {
                rightColumn[i] = adder - 1;
            } else
            {
                leftColumn[i] = adder;
                rightColumn[i] = adder - 1;
            }
            adder = adder + 2;
        }

        int iterator = leftColumn.length - 1;

        for (int i : leftColumn)
        {
            reversedLeftColumn[iterator] = i;
            iterator--;
        }

        iterator = 0;

        for (int i : rightColumn)
        {
            teamCycle[iterator] = i;
            iterator++;
        }
        for (int i : reversedLeftColumn)
        {
            teamCycle[iterator] = i;
            iterator++;
        }

        for (int i = 0; i < (teamCycle.length / 2) + 1; i++)
        {
            if (i == 0)
            {
                matchSequence.add(0 + "-" + teamCycle[i]);
            } else
            {
                matchSequence.add(teamCycle[teamCycle.length - i] + "-" + teamCycle[i]);
            }
        }

        for (int i = 0; i < shiftAmount; i++)
        {
            int lastE = teamCycle[teamCycle.length - 1];
            System.arraycopy(teamCycle, 0, teamCycle, 1, teamCycle.length - 1);
            teamCycle[0] = lastE;

            for (int j = 0; j < (teamCycle.length / 2) + 1; j++)
            {
                if (j == 0)
                {
                    matchSequence.add(0 + "-" + teamCycle[j]);
                } else
                {
                    matchSequence.add(teamCycle[teamCycle.length - j] + "-" + teamCycle[j]);
                }
            }
        }

        return matchSequence;
    }
}

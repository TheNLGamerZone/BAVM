package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.generator.RandomNames;

import java.util.ArrayList;

public class TeamManager
{
    private ArrayList<Team> loadedTeams;

    public TeamManager(boolean generateTeamsCoaches)
    {
        this.loadedTeams = new ArrayList<>();

        int teamsCoachesToGenerate = 20;

        if (generateTeamsCoaches)
        {
            this.generateTeamsCoaches(teamsCoachesToGenerate);
        }

        // Spelers laden
        this.loadTeamsCoaches();
    }

    private void loadTeamsCoaches()
    {
        //TODO teams en coaches laden uit txt
    }

    private void saveTeamsCoaches()
    {
        //TODO teams en coaches saven in txt
    }

    private void generateTeamsCoaches(int teamsToGenerate)
    {

        for (int i = 0; i < teamsToGenerate; i++)
        {
            double teamTalent = Math.random();

            loadedTeams.add(new Team(RandomNames.getTeamName(), i, generatePlayerIDList(i), i));
        }

        BAVM.getDisplay().appendText(teamsToGenerate + " teams gegenereerd!");
    }

    private int[] generatePlayerIDList(int teamNR)
    {
        int playerIDList[] = new int[23];

        for (int i = 0; i < 23; i++)
        {
            playerIDList[i] = (teamNR * 23) + i;
        }

        return playerIDList;
    }

    public Team getTeam(int teamID)
    {
        for (Team team : loadedTeams)
        {
            if (team.getTeamID() == teamID)
            {
                return team;
            }
        }

        return null;
    }
}

package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.Manageable;

public class Team implements Manageable
{
    private String teamName;
    private int teamID;

    private TeamInfo teamInfo;

    public boolean unsavedChanges;

    public Team(String teamName, int teamID, int[] playerIDs, int coachID, double teamTalent, int money, String placement)
    {
        System.out.printf("Created team %s (p=%d, id=%d, t=%f)%n", teamName, playerIDs.length, teamID, teamTalent);

        this.teamName = teamName;
        this.teamID = teamID;
        this.teamInfo = new TeamInfo(playerIDs, coachID, teamTalent, money, placement);

        if (!teamName.equals("marketTeam") && teamID != -1 && playerIDs.length != 21)
        {
            BAVM.getDisplay().appendText("Invalid team: " + teamID + " (" + playerIDs.length + " players)");
        }

        this.unsavedChanges = false;
    }

    public String getTeamName()
    {
        return this.teamName;
    }

    public int getTeamID()
    {
        return this.teamID;
    }

    public TeamInfo getTeamInfo()
    {
        return this.teamInfo;
    }

    @Override
    public String toString()
    {
        return "Team{" +
                "name=" + this.teamName.replaceAll(" ", "_") +
                ",id=" + this.teamID +
                ",info=" + this.teamInfo.toString() +
                "}";
    }

    @Override
    public int getID()
    {
        return this.teamID;
    }

    @Override
    public boolean unsavedChanges()
    {
        return unsavedChanges;
    }
}
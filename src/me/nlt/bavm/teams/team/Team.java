package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.Manageable;

public class Team implements Manageable
{
    private String teamName;
    private String directorName;
    private int teamID;

    private TeamInfo teamInfo;

    public boolean unsavedChanges;

    public Team(String teamName, String directorName, int teamID, int[] playerIDs, int coachID, double teamTalent, int currentGeld, int weeklyIncome, String placement, String scores)
    {
        System.out.printf("Created team %s (p=%d, id=%d, t=%f)%n", teamName, playerIDs.length, teamID, teamTalent);

        this.teamName = teamName;
        this.directorName = directorName;
        this.teamID = teamID;
        this.teamInfo = new TeamInfo(this, playerIDs, coachID, teamTalent, currentGeld, weeklyIncome, placement, scores);

        if (!teamName.equals("marketTeam") && teamID != 19 && playerIDs.length != 21)
        {
            BAVM.getDisplay().appendText("Invalid team: " + teamID + " (" + playerIDs.length + " players)");
        }

        this.unsavedChanges = false;
    }

    public String getTeamName()
    {
        return this.teamName;
    }

    public String getDirectorName()
    {
        return this.directorName;
    }

    public TeamInfo getTeamInfo()
    {
        return this.teamInfo;
    }

    public void setDirectorName(String directorName)
    {
        this.directorName = directorName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    @Override
    public String toString()
    {
        return "Team{" +
                "name=" + this.teamName.replaceAll(" ", "_") +
                ",id=" + this.teamID +
                ",info=" + this.teamInfo.toString() +
                ",directorName=" + this.directorName.replaceAll(" ", "_") +
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
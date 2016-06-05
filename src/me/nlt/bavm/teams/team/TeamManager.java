package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.teams.Factory;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;
import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;

public class TeamManager<T extends Manageable> extends Manager<T>
{
    public Team marketTeam;
    public Team playerTeam;

    public TeamManager(boolean generateTeams)
    {
        super();

        if (generateTeams)
        {
            this.generateManageables();
        } else
        {
            // Teams laden
            this.loadManageables();
        }
    }

    public Team getTeam(int teamID)
    {
        T team = super.getManageable(teamID);

        return team == null ? null : (Team) team;
    }

    public ArrayList<T> getLoadedTeams()
    {
        return manageables;
    }

    @Override
    public void loadManageables()
    {
        int amount = BAVM.getFileManager().readAmount("teams");

        for (int i = 0; i < amount - 2; i++)
        {
            manageables.add((T) Factory.createTeam(BAVM.getFileManager().readData("team", i)));
        }

        marketTeam = Factory.createTeam(BAVM.getFileManager().readData("team", amount - 2));
        playerTeam = Factory.createTeam(BAVM.getFileManager().readData("team", amount - 1));
    }

    @Override
    public void saveManageables(boolean firstSave)
    {
        int counter = 2;

        for (T type : manageables)
        {
            Team team = (Team) type;

            if ((firstSave || team.unsavedChanges()))
            {
                BAVM.getFileManager().saveData("team", team.toString(), team.getID());
                counter++;
            }
        }

        BAVM.getFileManager().saveData("team", marketTeam.toString(), manageables.size());
        BAVM.getFileManager().saveData("team", playerTeam.toString(), manageables.size() + 1);

        System.out.println(counter + " veranderingen in teams opgeslagen!");
    }

    @Override
    public void generateManageables()
    {
        int teams = 20;

        for (int i = 0; i < teams; i++)
        {
            double teamTalent = Math.random();
            String name = RandomNames.getTeamName();
            int[] playerIDs = BAVM.getPlayerManager().getPlayerIDs(this, teamTalent);

            manageables.add((T) new Team(name, i, playerIDs, i, teamTalent, 1000000, BAVM.getPlayerManager().getPlacementString(playerIDs)));
        }

        int[] playerIDs = BAVM.getPlayerManager().getPlayerIDs(this, 0.457);

        marketTeam = new Team("marketTeam", -666, BAVM.getPlayerManager().getFreePlayers(this), -1, 0.0, 234730247, "");
        playerTeam = new Team(RandomNames.getTeamName(), -1, playerIDs, teams, 0.457, 27500, BAVM.getPlayerManager().getPlacementString(playerIDs));

        this.saveManageables(true);

        BAVM.getDisplay().appendText(teams + " teams gegenereerd!");
    }

    public TransferResult transferPlayer(Team sendingTeam, Team receivingTeam, Player player, int price)
    {
        if (!sendingTeam.getTeamInfo().getPlayers().contains(player))
        {
            return TransferResult.FAILED_NOT_IN_TEAM;
        }

        if (receivingTeam.getTeamInfo().getTeamGeld().getCurrentGeldK() < price && receivingTeam != this.playerTeam)
        {
            return TransferResult.FAILED_NO_MONEY_OTHER;
        }

        if (receivingTeam.getTeamInfo().getTeamGeld().getCurrentGeldK() < price && receivingTeam == this.playerTeam)
        {
            return TransferResult.FAILED_NO_MONEY;
        }

        if (sendingTeam.getTeamInfo().getPlayers().size() - 1 < 14)
        {
            return TransferResult.FAILED_NOT_ENOUGH_PLAYERS;
        }

        if (sendingTeam.getTeamInfo().getPlayerPlacement().isPlaced(player))
        {
            return TransferResult.FAILED_IN_FORMATION;
        }

        sendingTeam.getTeamInfo().getPlayers().remove(player);
        sendingTeam.getTeamInfo().getTeamGeld().addGeld(price);
        sendingTeam.unsavedChanges = true;

        receivingTeam.getTeamInfo().getPlayers().add(player);
        receivingTeam.getTeamInfo().getTeamGeld().removeGeld(price);
        receivingTeam.unsavedChanges = true;

        return TransferResult.SUCCESS;
    }
}

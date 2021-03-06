package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Factory;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;
import me.nlt.bavm.teams.exceptions.FactoryException;
import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;

public class TeamManager<T extends Manageable> extends Manager<T>
{
    public boolean dataLoaded = false;
    public Team marketTeam;
    public Team playerTeam;

    /**
     * TeamManager constructor
     *
     * @param generateTeams Boolean die aangeeft of er nieuwe teams gemaakt moeten worden
     */
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

    /**
     * Returnt het team met het gegeven ID
     *
     * @param teamID ID
     * @return Het team met het gegeven ID
     */
    public Team getTeam(int teamID)
    {
        T team = super.getManageable(teamID);

        return team == null ? null : (Team) team;
    }

    /**
     * Returnt alle teams die geladen zijn
     *
     * @return Alle teams
     */
    public ArrayList<T> getLoadedTeams()
    {
        return manageables;
    }

    @Override
    /**
     * Laadt alle spelers uit het databestand en schrijft ze naar het geheugen
     */
    public void loadManageables()
    {
        int amount = BAVM.getFileManager().readAmount("teams");

        for (int i = 0; i < amount - 1; i++)
        {
            try
            {
                manageables.add((T) Factory.createTeam(BAVM.getFileManager().readData("team", i)));
            } catch (FactoryException e)
            {
                BAVM.getDisplay().printException(e);
            }
        }

        playerTeam = (Team) super.getManageable(19);
        try
        {
            marketTeam = Factory.createTeam(BAVM.getFileManager().readData("team", amount - 1));
        } catch (FactoryException e)
        {
            BAVM.getDisplay().printException(e);
        }

        BAVM.getDisplay().clearText();
        BAVM.getDisplay().appendText("Thread locked, aan het wachten op een unlock", "Thread ge-unlocked", "Spelers, teams, coaches en wedstrijden worden geladen", "  Alle spelers geladen", "  Alle coaches gelade", "  Alle teams geladen");
        System.out.println("Alle teams geladen");

        dataLoaded = true;
    }

    @Override
    /**
     * Schrijft alle teams uit het geheugen naar het databestand
     */
    public void saveManageables(boolean firstSave)
    {
        if (!BAVM.getFileManager().firstStart)
        {
            BAVM.getDisplay().appendText("    -> Teams aan het opslaan ...");
        }

        int counter = 1;

        for (T type : manageables)
        {
            Team team = (Team) type;

            if ((firstSave || team.unsavedChanges()))
            {
                BAVM.getFileManager().writeData("team", team.toString(), team.getID());
                team.unsavedChanges = false;
                counter++;
            }
        }

        BAVM.getFileManager().writeData("team", marketTeam.toString(), 20);

        System.out.println((counter == 0 ? "Geen" : counter) + " veranderingen in teams opgeslagen!");
    }

    @Override
    /**
     * Genereert de teams
     */
    public void generateManageables()
    {
        int teams = 19;

        for (int i = 0; i < teams; i++)
        {
            double teamTalent = Math.random();
            int[] playerIDs = BAVM.getPlayerManager().getPlayerIDs(this, teamTalent);

            manageables.add((T) new Team(RandomNames.getTeamName(), RandomNames.getPeopleName(), i, playerIDs, i, teamTalent, -1, -1, BAVM.getPlayerManager().getPlacementString(playerIDs), null));
        }

        int[] playerIDs = BAVM.getPlayerManager().getPlayerIDs(this, 0.457);

        manageables.add((T) new Team(RandomNames.getTeamName(), RandomNames.getPeopleName(), 19, playerIDs, teams, 0.4750, -1, -1, BAVM.getPlayerManager().getPlacementString(playerIDs), null));
        playerTeam = (Team) super.getManageable(19);
        marketTeam = new Team("marketTeam", "Satan", -666, BAVM.getPlayerManager().getFreePlayers(this), -1, 0.0, 234730247, 0, "", null);

        this.saveManageables(true);

        BAVM.getDisplay().appendText(teams + " teams gegenereerd!");
    }

    public TransferResult transferPlayer(Team sendingTeam, Team receivingTeam, Player player, int price)
    {
        if (!sendingTeam.getTeamInfo().getPlayers().contains(player))
        {
            return TransferResult.FAILED_NOT_IN_TEAM;
        }

        if (receivingTeam.getTeamInfo().getTeamGeld().getCurrentGeld() < price && receivingTeam != this.playerTeam)
        {
            return TransferResult.FAILED_NO_MONEY_OTHER;
        }

        if (receivingTeam.getTeamInfo().getTeamGeld().getCurrentGeld() < price && receivingTeam == this.playerTeam)
        {
            return TransferResult.FAILED_NO_MONEY;
        }

        if (sendingTeam.getTeamInfo().getPlayers().size() - 1 < 14)
        {
            return TransferResult.FAILED_NOT_ENOUGH_PLAYERS;
        }

        if (sendingTeam != BAVM.getTeamManager().marketTeam && sendingTeam.getTeamInfo().getPlayerPlacement().isPlaced(player))
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

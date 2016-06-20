package me.nlt.bavm.teams.coach;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Factory;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.generator.RandomStats;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;
import me.nlt.bavm.teams.exceptions.FactoryException;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TransferResult;

import java.util.ArrayList;

public class CoachManager<T extends Manageable> extends Manager<T>
{
    public boolean dataLoaded = false;

    /**
     * CoachManager constructor
     *
     * @param generateCoaches Boolean die aangeeft of er coaches gegeneerd moeten worden
     */
    public CoachManager(boolean generateCoaches)
    {
        super();

        if (generateCoaches)
        {
            this.generateManageables();
        } else
        {
            loadManageables();
        }
    }

    /**
     * Returnt de coach met het gegeven ID
     *
     * @param coachID et ID
     * @return De coach
     */
    public Coach getCoach(int coachID)
    {
        T coach = super.getManageable(coachID);

        return coach == null ? null : (Coach) coach;
    }

    @Override
    /**
     * Laadt alle coaches uit het databestand en schrijft ze naar het geheugen
     */
    public void loadManageables()
    {
        int amount = BAVM.getFileManager().readAmount("coaches");

        for (int i = 0; i < amount; i++)
        {
            try
            {
                manageables.add((T) Factory.createCoach(BAVM.getFileManager().readData("coach", i)));

                if (i % 15 == 0)
                {
                    BAVM.getDisplay().clearText();
                    BAVM.getDisplay().appendText("Thread locked, aan het wachten op een unlock", "Thread ge-unlocked", "Spelers, teams, coaches en wedstrijden worden geladen", "  Alle spelers geladen", "  " + i + " coaches geladen ...");
                }
            } catch (FactoryException e)
            {
                BAVM.getDisplay().printException(e);
            }
        }

        System.out.println("Alle coaches geladen");
        dataLoaded = true;
    }

    @Override
    /**
     * Schrijft alle coaches uit het geheugen naar het databestand
     */
    public void saveManageables(boolean firstSave)
    {
        if (!BAVM.getFileManager().firstStart)
        {
            BAVM.getDisplay().appendText("    -> Coaches aan het opslaan ...");
        }

        int counter = 0;

        for (T type : manageables)
        {
            Coach coach = (Coach) type;

            if ((firstSave || coach.unsavedChanges()))
            {
                BAVM.getFileManager().writeData("coach", coach.toString(), coach.getID());
                coach.unsavedChanges = false;
                counter++;
            }
        }

        System.out.println((counter == 0 ? "Geen" : counter) + " veranderingen met coaches opgeslagen!");
    }

    @Override
    /**
     * Genereert de coaches
     */
    public void generateManageables()
    {
        for (int i = 0; i < 74; i++)
        {
            double teamTalent = Math.random();

            manageables.add((T) new Coach(RandomNames.getPeopleName(), i, RandomStats.randomCStats(teamTalent)));
        }

        this.saveManageables(true);
    }

    /**
     * Methode die de tranfer van een coach op zich neemt
     *
     * @param coach         De coach in kwestie
     * @param receivingTeam Team die de coach ontvangt
     * @param price         Prijs van de tranfer
     * @return Het resultaat van de transfer
     */
    public TransferResult transferCoach(Coach coach, Team receivingTeam, double price)
    {
        if (receivingTeam.getTeamInfo().getTeamGeld().getCurrentGeld() < price)
        {
            return TransferResult.FAILED_NO_MONEY_COACH;
        }

        if (!getFreeCoaches().contains(coach))
        {
            return TransferResult.FAILED_NOT_AVAILABLE;
        }

        receivingTeam.getTeamInfo().getTeamGeld().removeGeld((int) price);
        receivingTeam.getTeamInfo().setTeamCoach(coach);

        return TransferResult.SUCCESS_COACH;
    }

    /**
     * Returnt een arraylist met alle coaches die beschikbaar zijn voor een transfer
     *
     * @return Een arraylist met alle coaches die beschikbaar zijn voor een transfer
     */
    public ArrayList<Coach> getFreeCoaches()
    {
        ArrayList<Coach> freeCoaches = new ArrayList<>();

        for (T type : manageables)
        {
            Coach coach = (Coach) type;
            boolean inTeam = false;

            for (Object object : BAVM.getTeamManager().getLoadedTeams())
            {
                Team team = (Team) object;

                if (team.getTeamInfo().getTeamCoach() == coach)
                {
                    inTeam = true;
                }
            }

            if (!inTeam)
            {
                freeCoaches.add(coach);
            }
        }

        return freeCoaches;
    }
}

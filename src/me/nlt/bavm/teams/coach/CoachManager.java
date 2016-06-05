package me.nlt.bavm.teams.coach;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.generator.RandomStats;
import me.nlt.bavm.teams.Factory;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;

public class CoachManager<T extends Manageable> extends Manager<T>
{
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

    public Coach getCoach(int coachID)
    {
        T coach = super.getManageable(coachID);

        return coach == null ? null : (Coach) coach;
    }

    @Override
    public void loadManageables()
    {
        int amount = BAVM.getFileManager().readAmount("coaches");

        for (int i = 0; i < amount; i++)
        {
            manageables.add((T) Factory.createCoach(BAVM.getFileManager().readData("coach", i)));
        }
    }

    @Override
    public void saveManageables(boolean firstSave)
    {
        int counter = 0;

        for (T type : manageables)
        {
            Coach coach = (Coach) type;

            if ((firstSave || coach.unsavedChanges()))
            {
                BAVM.getFileManager().saveData("coach", coach.toString(), coach.getID());
                counter++;
            }
        }

        System.out.println(counter + " veranderingen met coaches opgeslagen!");
    }

    @Override
    public void generateManageables()
    {
        for (int i = 0; i < 24; i++)
        {
            double teamTalent = Math.random();

            manageables.add((T) new Coach(RandomNames.getPeopleName(), i, RandomStats.randomCStats(teamTalent)));
        }

        this.saveManageables(true);
    }
}

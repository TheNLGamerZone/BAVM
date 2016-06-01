package me.nlt.bavm.teams.coach;

import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.generator.RandomStats;
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

    }

    @Override
    public void saveManageables()
    {

    }

    @Override
    public void generateManageables()
    {
        for (int i = 0; i < 24; i++)
        {
            double teamTalent = Math.random();

            manageables.add((T) new Coach(RandomNames.getPeopleName(), i, RandomStats.randomCStats(teamTalent)));
        }
    }
}

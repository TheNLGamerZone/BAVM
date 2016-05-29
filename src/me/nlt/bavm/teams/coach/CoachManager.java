package me.nlt.bavm.teams.coach;

import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.generator.RandomStats;

import java.util.ArrayList;

public class CoachManager {
    private ArrayList<Coach> loadedCoaches;

    public CoachManager(boolean generateCoaches)
    {
        this.loadedCoaches = new ArrayList<>();

        int coachesToGenerate = 24;

        if (generateCoaches)
        {
            this.generateCoaches(coachesToGenerate);
        }
    }

    private void generateCoaches(int coachesToGenerate) {
        for (int i = 0; i < coachesToGenerate; i++)
        {
            double teamTalent = Math.random();

            loadedCoaches.add(new Coach(RandomNames.getPeopleName(), i, RandomStats.randomCStats(teamTalent)));
        }
    }

    public Coach getCoach(int coachID) {
        for (Coach coach : loadedCoaches) {
            if (coach.getCoachID() == coachID) {
                return coach;
            }
        }

        return null;
    }
}

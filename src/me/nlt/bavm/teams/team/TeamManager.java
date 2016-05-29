package me.nlt.bavm.teams.team;

import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.generator.RandomStats;
import me.nlt.bavm.teams.coach.Coach;

import java.util.ArrayList;

public class TeamManager {
    private ArrayList<Team> loadedTeams;
    private ArrayList<Coach> loadedCoaches;

    public TeamManager(boolean generateTeams) {
        this.loadedTeams = new ArrayList<>();
        this.loadedCoaches = new ArrayList<>();

        if (generateTeams)
        {
            this.generateTeams();
        }

        // Spelers laden
        this.loadTeamsCoaches();
    }

    private void loadTeamsCoaches() {
        //TODO teams en coaches laden uit txt
    }

    private void saveTeamsCoaches() {
        //TODO teams en coaches saven in txt
    }

    private void generateTeams() {
        //aantal teams
        int amount = 20;

        for (int i = 0; i < amount; i++) {
            double teamTalent = Math.random();

            loadedCoaches.add(new Coach(RandomNames.getPeopleName(), i, RandomStats.randomCStats(teamTalent)));
            loadedTeams.add(new Team(RandomNames.getTeamName(), i, generatePlayerIDList(i), i));
        }
    }

    private int[] generatePlayerIDList(int teamNR) {
        int playerIDList[] = new int[23];

        for (int i = 0; i < 23; i++) {
            playerIDList[i] = (teamNR * 23) + i;
        }

        return playerIDList;
    }

    public Team getTeam(int teamID) {
        for (Team team : loadedTeams) {
            if (team.getTeamID() == teamID) {
                return team;
            }
        }

        return null;
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

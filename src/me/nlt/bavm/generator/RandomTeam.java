package me.nlt.bavm.generator;


import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.team.Team;

public class RandomTeam {
    public static void createRandomTeams (int amount) {

        for (int i = 0; i < amount; i++) {
            double teamTalent = Math.random();

            int playerIDList[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};

            Coach coach = new Coach(RandomNames.getPeopleName(), i, RandomStats.randomCStats(teamTalent));
            Team team = new Team(RandomNames.getTeamName(), i, RandomTeam.generatePlayerIDList(teamTalent), i);

            System.out.println("Van team: " + team.toString());
        }
    }

    public static int[] generatePlayerIDList (double teamTalent) {
        //TODO take a group of players from the NoTeamPlayer list, sorted on teamTalent, and put them in playerIDList

        int playerIDList[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};

        return playerIDList;
    }
}

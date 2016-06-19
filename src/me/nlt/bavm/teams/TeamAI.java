package me.nlt.bavm.teams;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerStats;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TeamAI {
    public static void doTeamAI()
    {
        for (int i = 0; i < 19; i++)
        {
            Team team = BAVM.getTeamManager().getTeam(i);
            TeamInfo teamInfo = team.getTeamInfo();
            int currentGeld = teamInfo.getTeamGeld().getCurrentGeld();
            int weeklyIncome = teamInfo.getTeamGeld().getWeeklyIncome();

            //AI must only do something if the team has enough money
            if (currentGeld > weeklyIncome)
            {
                ArrayList<Player> players = new ArrayList<>();
                players.addAll(team.getTeamInfo().getPlayers().stream().collect(Collectors.toList()));

                int key = teamInfo.getStatCoefficients().entrySet().stream().min((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey().getIndex();

                //get maximum price
                final double stepSize = 2.6;
                double price = 0;
                double upgradeValue = 0.1;
                int counter = 0;
                ArrayList<Double> prices = new ArrayList<>();


                while (price < currentGeld)
                {
                    for (Player player : players)
                    {
                        double currentStat = player.getPlayerStats().getValue(PlayerStats.getSkill(key));
                        final double upgrade = (currentStat + upgradeValue <= 100
                                ? upgradeValue
                                : upgradeValue - Math.abs((currentStat + upgradeValue) - 100));
                        final double newStat = (currentStat + upgrade <= 100 ? currentStat + upgrade : 100);

                        for (double j = upgrade; j >= 0; j -= stepSize)
                        {
                            if (currentStat + stepSize > newStat)
                            {
                                price += (currentStat * 3 * (currentStat / 100)) * currentStat + Math.abs(currentStat - newStat) * 15000 - players.size();
                                currentStat += Math.abs(currentStat - newStat);
                            } else
                            {
                                price += (currentStat * 3 * (currentStat / 100)) * currentStat + stepSize * 15000 - players.size();
                                currentStat += stepSize;
                            }
                        }
                    }

                    upgradeValue += 0.1;
                    prices.add(price);
                    counter++;
                }

                if (counter != 1)
                {
                    price = prices.get(counter - 2);
                } else {
                    price = prices.get(counter - 1);
                }

                if (price < currentGeld)
                {
                    for (Player player : players)
                    {
                        double currentStat = player.getPlayerStats().getValue(PlayerStats.getSkill(key));
                        double upgrade = (currentStat + upgradeValue <= 100
                                ? upgradeValue
                                : upgradeValue - Math.abs((currentStat + upgradeValue) - 100));

                        player.getPlayerStats().increaseSkill(PlayerStats.getSkill(key), upgrade);
                    }

                    teamInfo.getTeamGeld().removeGeld((int) price);
                }
            }
        }
    }
}

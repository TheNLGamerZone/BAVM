package me.nlt.bavm.teams.player;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.generator.RandomStats;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;
import me.nlt.bavm.teams.Market;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamManager;

import java.util.ArrayList;

public class PlayerManager<T extends Manageable> extends Manager<T>
{
    public PlayerManager(boolean generatePlayers)
    {
        super();

        if (generatePlayers)
        {
            this.generateManageables();
        }

        // Spelers laden
        this.loadManageables();

        // Waarden berekenen
        Market.calculatePlayerValues(this);
    }

    @Override
    public void loadManageables()
    {
        //TODO Spelers laden uit tekstbestand
    }

    @Override
    public void saveManageables()
    {
        //TODO Spelers opslaan in tekstbestanden
    }

    @Override
    public void generateManageables()
    {
        //DEBUG
        System.out.println("15% kans op keeper, 25% kans op defender, 30% kans op attacker en 30% kans op midfielder");

        int keeper = 0, defender = 0, attacker = 0, midfielder = 0, total = 0;

        //amount of players to generate
        int playersToGenerate = 1028;

        for (Position position : generatePositions(playersToGenerate, new int[]{15, 25, 30, 30}))
        {
            manageables.add((T) new Player(RandomNames.getPeopleName(), this.getNextAvailableID(), position, RandomStats.randomStats(position)));

            switch (position)
            {
                case KEEPER:
                    keeper++;
                    break;
                case DEFENDER:
                    defender++;
                    break;
                case ATTACKER:
                    attacker++;
                    break;
                case MIDFIELDER:
                    midfielder++;
                    break;
            }

            total++;
        }

        //DEBUG
        System.out.printf("%d keepers, %d defenders, %d attackers and %d midfielders (%d total)%n", keeper, defender, attacker, midfielder, total);

        BAVM.getDisplay().appendText(playersToGenerate + " spelers gegenereerd!");

        // Save players
        this.saveManageables();
    }

    public int[] getFreePlayers(TeamManager teamManager)
    {
        ArrayList<Player> players = new ArrayList<>();
        int[] playerIDs;
        for (Manageable manageable : manageables)
        {
            Player player = (Player) manageable;

            players.add(player);
        }

        for(Object object : teamManager.getLoadedTeams())
        {
            Team team = (Team) object;

            team.getTeamInfo().getPlayers().forEach(players::remove);
        }

        playerIDs = new int[players.size()];

        for (int i = 0; i < players.size(); i++)
        {
            playerIDs[i] = players.get(i).getPlayerID();
        }

        return playerIDs;
    }

    public int[] getPlayerIDs(TeamManager teamManager, double teamTalent)
    {
        //System.out.println("----------------");
        int percentage = (int) (teamTalent * 10000);

        ArrayList<Player> newTeam = new ArrayList<>();
        Player[][] possiblePlayers = new Player[][]{
                getPlayers(teamManager, Position.KEEPER, true),
                getPlayers(teamManager, Position.ATTACKER, true),
                getPlayers(teamManager, Position.DEFENDER, true),
                getPlayers(teamManager, Position.MIDFIELDER, true)
        };

        for (Player[] players : possiblePlayers)
        {
            if (players.length == 0)
            {
                continue;
            }

            double stepSize = 10000 / players.length;
            int playersLeft = (players[0].getPosition() == Position.MIDFIELDER ? 21 - newTeam.size() : players[0].getPosition().getStartPlayers());

            for (int i = 1; i <= players.length; i++)
            {
                double currentDiff = Math.abs(percentage - stepSize * i);
                double nextDiff = (i + playersLeft >= players.length ? Double.MAX_VALUE : Math.abs(percentage - stepSize * (i + 1)));

                //System.out.println("CD: " + currentDiff + ", ND: " + nextDiff);

                if (currentDiff < nextDiff || currentDiff == nextDiff)
                {
                    newTeam.add(players[i - 1]);
                    playersLeft--;
                }


                if (playersLeft <= 0)
                {
                    //System.out.println("BRK: " + playersLeft);
                    break;
                }
            }

            //System.out.println("NULL: " + playersLeft);

        }
        //System.out.println("----------------");


        int[] playerIDs = new int[newTeam.size()];

        for (int i = 0; i < newTeam.size(); i++)
        {
            playerIDs[i] = newTeam.get(i).getPlayerID();
        }

        //System.out.println("Size: " + newTeam.size() + " _ " + playerIDs.length);
        return playerIDs;
    }

    private boolean playerInTeam(TeamManager teamManager, int playerID)
    {
        if (teamManager.getLoadedTeams().isEmpty())
        {
            return false;
        }

        for (Object object : teamManager.getLoadedTeams())
        {
            Team team = (Team) object;

            for (Player player : team.getTeamInfo().getPlayers())
            {
                if (player.getPlayerID() == playerID)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private Player[] getPlayers(TeamManager teamManager, Position position, boolean onlyAvailablePlayers)
    {
        ArrayList<Player> players = new ArrayList<>();

        for (Object manageable : manageables)
        {
            Player player = (Player) manageable;

            if (player.getPosition() == position && (onlyAvailablePlayers ? !playerInTeam(teamManager, player.getPlayerID()) : true))
            {
                if (players.isEmpty())
                {
                    //System.out.println("Added first player: " + player.getPlayerStats().getCheckSum());
                    players.add(player);
                    continue;
                }

                ArrayList<Player> playerCopy = new ArrayList<>();

                //System.out.println("Loop: " + player.getPlayerStats().getCheckSum());

                for (int i = 0; i < players.size(); i++)
                {
                    //System.out.println("    Checking: " + player.getPlayerStats().getCheckSum() + " < " + players.get(i).getPlayerStats().getCheckSum());

                    if (players.get(i).getPlayerStats().getCheckSum() > player.getPlayerStats().getCheckSum())
                    {
                        /*System.out.println("    true -> copying array from index " + i + " to " + (players.size() - 1));
                        StringBuilder sb = new StringBuilder();

                        for (Player player1 : players)
                            sb.append(player1.getPlayerStats().getCheckSum() + ", ");

                        System.out.println("    Old array: " + sb.toString());
                        sb.setLength(0);*/

                        int size = players.size();
                        int cuts = 0;

                        for (int j = i; j < size; j++)
                        {
                            playerCopy.add(players.get(j));

                           /* for (Player player1 : playerCopy)
                                sb.append(player1.getPlayerStats().getCheckSum() + ", ");

                            System.out.println("        Copy array: " + sb.toString() + " (j=" + j + ", m=" + (size) + ")");
                            sb.setLength(0);*/
                            cuts++;
                        }
                        //System.out.println("        Size copy array: " + playerCopy.size());

                        for (int j = 0; j < cuts; j++)
                        {
                            int index = (players.size() == 1 ? 0 : players.size() - 1);

                            //System.out.println("        Removed value: " + players.get(index).getPlayerStats().getCheckSum() + " (i=" + index + ")");
                            players.remove(index);
                        }

                        players.add(player);

                        players.addAll(playerCopy);

                        /*for (Player player1 : players)
                            sb.append(player1.getPlayerStats().getCheckSum() + ", ");

                        System.out.println("    New array: " + sb.toString());
                        sb.setLength(0);*/
                        break;
                    }

                    if (i + 1 == players.size())
                    {
                        //System.out.println("    highest number -> player added");
                        players.add(player);

                        /*StringBuilder sb = new StringBuilder();
                        for (Player player1 : players)
                            sb.append(player1.getPlayerStats().getCheckSum() + ", ");

                        System.out.println("    New array: " + sb.toString());*/
                        break;
                    }

                    //System.out.println("    false -> checking next player");
                }
            }
        }

        //System.out.println("Size: " + players.size() );
        return players.toArray(new Player[players.size()]);
    }

    private Position[] generatePositions(int amount, int[] percentages)
    {
        ArrayList<Position> positions = new ArrayList<>();

        for (int i = 0; i < amount; i++)
        {
            int chanceNumber = 0;
            int previousValues = 0;

            while (chanceNumber == 0)
            {
                chanceNumber = (int) (Math.random() * 100);
            }


            for (int chance = 0; chance < percentages.length; chance++)
            {
                int currentIndex = percentages[chance] + previousValues;

                if (chanceNumber > previousValues && chanceNumber <= currentIndex)
                {
                    for (Position position : Position.values())
                    {
                        if (position.getId() == chance)
                        {
                            positions.add(position);
                        }
                    }
                }

                previousValues += percentages[chance];
            }
        }

        return positions.toArray(new Position[positions.size()]);
    }

    public Player getPlayer(int playerID)
    {
        T player = super.getManageable(playerID);

        return player == null ? null : (Player) player;
    }

    public int getNextAvailableID()
    {
        return manageables.size();
    }

    public ArrayList<T> getLoadedPlayers()
    {
        return manageables;
    }
}

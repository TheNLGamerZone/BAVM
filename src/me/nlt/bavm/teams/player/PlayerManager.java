package me.nlt.bavm.teams.player;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.generator.RandomStats;

import java.util.ArrayList;

public class PlayerManager
{
    private ArrayList<Player> loadedPlayers;

    public PlayerManager(boolean generatePlayers)
    {
        this.loadedPlayers = new ArrayList<>();

        if (generatePlayers)
        {
            this.generatePlayers();
        }

        // Spelers laden
        this.loadPlayers();
    }

    private void loadPlayers()
    {
        //TODO Spelers laden uit tekstbestand
    }

    private void savePlayers()
    {
        //TODO Spelers opslaan in tekstbestanden

        for (Player player : getPlayers(Position.KEEPER))
            System.out.printf("%s, %d (%d)%n", player.getPlayerName(), (int) player.getPlayerStats().getCheckSum(), (int) player.getPlayerStats().getValue(PlayerStats.Stat.DOELMAN.getLocation()));
    }

    private void generatePlayers()
    {
        //DEBUG
        System.out.println("15% kans op keeper, 25% kans op defender, 50% kans op attacker en 10% kans op midfielder");

        int keeper = 0, defender = 0, attacker = 0, midfielder = 0, total = 0;

        //amount of players to generate
        int playersToGenerate = 512;

        for (Position position : generatePositions(playersToGenerate, new int[]{15, 25, 50, 10}))
        {
            loadedPlayers.add(new Player(RandomNames.getPeopleName(), this.getNextAvailableID(), position, RandomStats.randomStats(position)));

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
        this.savePlayers();
    }

    public int[] getPlayerIDs(double teamTalent)
    {
        int percentage = (int) (teamTalent * 10);

        Player[] possibleKeepers = this.getPlayers(Position.KEEPER);
        Player[] possibleAttackers = this.getPlayers(Position.ATTACKER);
        Player[] possibleDefenders = this.getPlayers(Position.DEFENDER);
        Player[] possibleMidfielders = this.getPlayers(Position.MIDFIELDER);

        return null;
    }

    private Player[] getPlayers(Position position)
    {
        ArrayList<Player> players = new ArrayList<>();

        for (Player player : loadedPlayers)
        {
            if (player.getPosition() == position)
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

    public Player getPlayer(String playerName)
    {
        for (Player player : loadedPlayers)
        {
            if (player.getPlayerName().equals(playerName))
            {
                return player;
            }
        }

        return null;
    }

    public Player getPlayer(int playerID)
    {
        for (Player player : loadedPlayers)
        {
            if (player.getPlayerID() == playerID)
            {
                return player;
            }
        }

        return null;
    }

    public int getNextAvailableID()
    {
        return loadedPlayers.size();
    }
}

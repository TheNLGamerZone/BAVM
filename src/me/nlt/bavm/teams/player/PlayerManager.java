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
    }

    private void generatePlayers()
    {
        //DEBUG
        //System.out.println("15% kans op keeper, 25% kans op defender, 50% kans op attacker en 10% kans op midfielder");

        //int keeper = 0, defender = 0, attacker = 0, midfielder = 0, total = 0;

        //amount of players to generate
        int playersToGenerate = 512;

        for (Position position : generatePositions(playersToGenerate, new int[]{15, 25, 50, 10}))
        {
            loadedPlayers.add(new Player(RandomNames.getPeopleName(), this.getNextAvailableID(), RandomStats.randomStats(position)));

                /*switch (position)
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

                total++;*/
        }

        //DEBUG
        //System.out.printf("%d keepers, %d defenders, %d attackers and %d midfielders (%d total)%n", keeper, defender, attacker, midfielder, total);


        //DEBUG
        /*for (Player player : loadedPlayers)
        {
            System.out.println(player.toString());
        }*/

        BAVM.getDisplay().appendText("512 spelers gegenereerd!");

        // Save players
        this.savePlayers();
    }

    private Position[] generatePositions(int amount, int[] percentages)
    {
        ArrayList<Position> positions = new ArrayList<>();

        for (int i = 0; i < amount; i++)
        {
            int chanceNumer = 0;
            int previousValues = 0;

            while (chanceNumer == 0)
            {
                chanceNumer = (int) (Math.random() * 100);
            }


            for (int chance = 0; chance < percentages.length; chance++)
            {
                int lastIndex = (chance - 1 == -1 ? 0 : percentages[chance - 1]);
                int currentIndex = percentages[chance] + previousValues;

                if (chanceNumer > previousValues && chanceNumer <= currentIndex)
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
        int currentID = -1;

        for (Player player : loadedPlayers)
        {
            currentID++;
        }

        return currentID + 1;
    }
}

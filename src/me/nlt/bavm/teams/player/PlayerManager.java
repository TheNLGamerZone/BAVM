package me.nlt.bavm.teams.player;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Factory;
import me.nlt.bavm.generator.RandomNames;
import me.nlt.bavm.generator.RandomStats;
import me.nlt.bavm.teams.Manageable;
import me.nlt.bavm.teams.Manager;
import me.nlt.bavm.teams.Market;
import me.nlt.bavm.teams.exceptions.InvalidPlayerException;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamManager;

import java.util.ArrayList;

public class PlayerManager<T extends Manageable> extends Manager<T>
{
    public boolean dataLoaded = false;

    /**
     * PlayerManager constructor
     *
     * @param generatePlayers Boolean die aangeeft of er spelers gegeneerd moeten worden
     */
    public PlayerManager(boolean generatePlayers)
    {
        super();

        if (generatePlayers)
        {
            BAVM.getDisplay().appendText("Alle spelers, teams en coaches aan het generen en opslaan, dit kan even duren!");
            this.generateManageables();
        } else
        {
            // Spelers laden
            this.loadManageables();
        }

        // Waarden berekenen
        Market.calculatePlayerValues(this);
    }

    @Override
    /**
     * Laadt alle spelers uit het databestand en schrijft ze naar het geheugen
     */
    public void loadManageables()
    {
        int amount = BAVM.getFileManager().readAmount("players");

        for (int i = 0; i < amount; i++)
        {
            try
            {
                manageables.add((T) Factory.createPlayer(BAVM.getFileManager().readData("player", i)));

                if (i % 30 == 0)
                {
                    BAVM.getDisplay().clearText();
                    BAVM.getDisplay().appendText("Thread locked, aan het wachten op een unlock", "Thread ge-unlocked", "Spelers, teams, coaches en wedstrijden worden geladen", "  " + i + " spelers geladen ...");
                }
            } catch (InvalidPlayerException e)
            {
                BAVM.getDisplay().printException(e);
            }
        }

        System.out.println("Alle spelers geladen");

        dataLoaded = true;
    }

    @Override
    /**
     * Schrijft alle spelers uit het geheugen naar het databestand
     */
    public void saveManageables(boolean firstSave)
    {
        if (!BAVM.getFileManager().firstStart)
        {
            BAVM.getDisplay().appendText("    -> Spelers aan het opslaan ...");
        }

        int counter = 0;

        for (T type : manageables)
        {
            Player player = (Player) type;

            if ((firstSave || player.unsavedChanges()))
            {
                BAVM.getFileManager().writeData("player", player.toString(), player.getID());
                player.unsavedChanges = false;
                counter++;
            }
        }

        System.out.println((counter == 0 ? "Geen" : counter) + " veranderingen met spelers opgeslagen!");
    }

    @Override
    /**
     * Genereert de spelers
     */
    public void generateManageables()
    {
        // Amount of players to generate
        int playersToGenerate = 750;

        for (Position position : generatePositions(playersToGenerate, new int[]{15, 25, 30, 30}))
        {
            manageables.add((T) new Player(RandomNames.getPeopleName(), this.getNextAvailableID(), position, RandomStats.randomStats(position)));
        }

        // Save players
        this.saveManageables(true);

        BAVM.getDisplay().appendText(playersToGenerate + " spelers gegenereerd!");
    }

    /**
     * Returnt de string die de data bevat om PlayerPlacement te maken
     *
     * @param playerIDs De ID's van de spelers
     * @return De placementstring
     */
    public String getPlacementString(int[] playerIDs)
    {
        ArrayList<Player> keepers = new ArrayList<>();
        ArrayList<Player> attackers = new ArrayList<>();
        ArrayList<Player> defenders = new ArrayList<>();
        ArrayList<Player> midfielders = new ArrayList<>();

        for (int playerID : playerIDs)
        {
            Player player = getPlayer(playerID);

            if (player == null)
            {
                continue;
            }

            switch (player.getPosition())
            {
                case KEEPER:
                    keepers.add(player);
                    break;
                case ATTACKER:
                    attackers.add(player);
                    break;
                case DEFENDER:
                    defenders.add(player);
                    break;
                case MIDFIELDER:
                    midfielders.add(player);
                    break;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("keeper!" + keepers.get(0).getPlayerID() + "@attackers!");

        for (int i = 0; i < 3; i++)
        {
            stringBuilder.append(attackers.get(i).getPlayerID() + "#");
        }

        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append("@defenders!");

        for (int i = 0; i < 3; i++)
        {
            stringBuilder.append(defenders.get(i).getPlayerID() + "#");
        }

        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append("@midfielders!");

        for (int i = 0; i < 4; i++)
        {
            stringBuilder.append(midfielders.get(i).getPlayerID() + "#");
        }

        stringBuilder.setLength(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    /**
     * Returnt een array met alle ID's van spelers zonder club
     *
     * @param teamManager De teammanager
     * @return Een array met alle ID's van spelers zonder club
     */
    public int[] getFreePlayers(TeamManager teamManager)
    {
        ArrayList<Player> players = new ArrayList<>();
        int[] playerIDs;

        for (Manageable manageable : manageables)
        {
            Player player = (Player) manageable;

            players.add(player);
        }

        for (Object object : teamManager.getLoadedTeams())
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

    /**
     * Returnt een array met ID's van spelers die passen bij het teamtalent
     *
     * @param teamManager De teammanager
     * @param teamTalent  Het teamtalent
     * @return Een array met ID's van spelers die passen bij het teamtalent
     */
    public int[] getPlayerIDs(TeamManager teamManager, double teamTalent)
    {
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

                if (currentDiff < nextDiff || currentDiff == nextDiff)
                {
                    newTeam.add(players[i - 1]);
                    playersLeft--;
                }


                if (playersLeft <= 0)
                {
                    break;
                }
            }
        }

        int[] playerIDs = new int[newTeam.size()];

        for (int i = 0; i < newTeam.size(); i++)
        {
            playerIDs[i] = newTeam.get(i).getPlayerID();
        }

        return playerIDs;
    }

    /**
     * Checkt of een speler al in een team zit
     *
     * @param teamManager De teammanager
     * @param playerID    Het ID van de speler
     * @return Boolean die aangeeft of de speler al in een team zit
     */
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

    /**
     * Maakt een array met spelers die voldoen aan de positie
     * Ook worden ze gesorteerd op hun controlegetal
     *
     * @param teamManager          De teammanager
     * @param position             De positie
     * @param onlyAvailablePlayers Boolean die aangeeft of speler in een team mogen zitten of niet
     * @return Een gesorteerd array van spelers die aan alle eisen voldoen
     */
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
                    players.add(player);
                    continue;
                }

                ArrayList<Player> playerCopy = new ArrayList<>();

                for (int i = 0; i < players.size(); i++)
                {
                    if (players.get(i).getPlayerStats().getCheckSum() > player.getPlayerStats().getCheckSum())
                    {
                        int size = players.size();
                        int cuts = 0;

                        for (int j = i; j < size; j++)
                        {
                            playerCopy.add(players.get(j));
                            cuts++;
                        }

                        for (int j = 0; j < cuts; j++)
                        {
                            int index = (players.size() == 1 ? 0 : players.size() - 1);

                            players.remove(index);
                        }

                        players.add(player);
                        players.addAll(playerCopy);
                        break;
                    }

                    if (i + 1 == players.size())
                    {
                        players.add(player);
                        break;
                    }
                }
            }
        }

        return players.toArray(new Player[players.size()]);
    }

    /**
     * Maakt een array met posities gebaseerd op percentages
     * Hier worden later spelers mee gemaakt
     *
     * @param amount      Aantal posities om te genereren
     * @param percentages De percentages van posities
     * @return Een array met posities
     */
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
                        if (position.getID() == chance)
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

    /**
     * Returnt de speler met het gegeven ID
     *
     * @param playerID ID
     * @return De speler met het ID
     */
    public Player getPlayer(int playerID)
    {
        T player = super.getManageable(playerID);

        return player == null ? null : (Player) player;
    }

    /**
     * Returnt het volgende ID
     *
     * @return Volgende ID
     */
    public int getNextAvailableID()
    {
        return manageables.size();
    }

    /**
     * Returnt een arraylist met alle spelers in het geheugen
     *
     * @return Een arraylist met alle spelers in het geheugen
     */
    public ArrayList<T> getLoadedPlayers()
    {
        return manageables;
    }
}

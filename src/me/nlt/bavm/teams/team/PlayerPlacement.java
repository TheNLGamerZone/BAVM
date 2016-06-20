package me.nlt.bavm.teams.team;

import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.Position;

import java.util.ArrayList;

public class PlayerPlacement
{
    private Player keeper;
    private ArrayList<Player> defenders;
    private ArrayList<Player> attackers;
    private ArrayList<Player> midfielders;

    /**
     * PlayerPlacement constructor
     *
     * @param keeper      De keeper
     * @param defenders   De verdedigers
     * @param attackers   De aanvallers
     * @param midfielders De middenvelders
     */
    public PlayerPlacement(Player keeper, ArrayList<Player> defenders, ArrayList<Player> attackers, ArrayList<Player> midfielders)
    {
        this.keeper = keeper;
        this.defenders = defenders;
        this.midfielders = midfielders;
        this.attackers = attackers;
    }

    /*
     * Deze spreken wel voorzich hoop ik
     */
    public Player getKeeper()
    {
        return this.keeper;
    }

    public ArrayList<Player> getDefenders()
    {
        return this.defenders;
    }

    public ArrayList<Player> getAttackers()
    {
        return this.attackers;
    }

    public ArrayList<Player> getMidfielders()
    {
        return this.midfielders;
    }

    /**
     * Methode om spelers makkelijk te wisselen
     *
     * @param playerInPlacement Speler in de opstelling
     * @param transferPlayer    Speler die de opstelling in gaat
     * @param position          De positie
     */
    public void exchangePlayers(Player playerInPlacement, Player transferPlayer, Position position)
    {
        this.removePlayer(playerInPlacement);

        switch (position)
        {
            case KEEPER:
                keeper = transferPlayer;
                break;
            case ATTACKER:
                attackers.add(transferPlayer);
                break;
            case MIDFIELDER:
                midfielders.add(transferPlayer);
                break;
            case DEFENDER:
                defenders.add(transferPlayer);
                break;
            default:
                break;
        }
    }

    /**
     * Methode om de speler uit de opstelling te halen
     *
     * @param player De speler
     */
    public void removePlayer(Player player)
    {
        if (!isPlaced(player))
        {
            return;
        }

        if (keeper == player)
        {
            keeper = null;
        } else if (attackers.contains(player))
        {
            attackers.remove(player);
        } else if (midfielders.contains(player))
        {
            midfielders.remove(player);
        } else
        {
            defenders.remove(player);
        }
    }

    /**
     * Checkt of de speler in de opstelling staat
     *
     * @param player De speler
     * @return Boolean die aangeeft of de speler in de opstelling staat
     */
    public boolean isPlaced(Player player)
    {
        return player == keeper || attackers.contains(player) || midfielders.contains(player) || defenders.contains(player);
    }

    /**
     * Returnt de hoeveelheid spelers in de opstelling
     *
     * @return De hoeveelheid spelers in de opstelling
     */
    public int getSize()
    {
        return attackers.size() + defenders.size() + midfielders.size() + (keeper != null ? 1 : 0);
    }

    /**
     * Returnt een mooie string voor iederen positie
     *
     * @param position De positie
     * @return Een mooie string voor iederen positie
     */
    public String getPlacement(Position position)
    {
        StringBuilder stringBuilder = new StringBuilder();

        switch (position)
        {
            case KEEPER:
                return keeper.getPlayerName() + " (" + keeper.getID() + ")";
            case ATTACKER:
                for (Player player : attackers)
                {
                    stringBuilder.append(player.getPlayerName() + " (" + player.getID() + "), ");
                }
                break;
            case DEFENDER:
                for (Player player : defenders)
                {
                    stringBuilder.append(player.getPlayerName() + " (" + player.getID() + "), ");
                }
                break;
            case MIDFIELDER:
                for (Player player : midfielders)
                {
                    stringBuilder.append(player.getPlayerName() + " (" + player.getID() + "), ");
                }
                break;
        }

        stringBuilder.setLength(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }

    @Override
    /**
     * Maakt en antwoord een string met alle data voor de opstelling
     *
     * @return De string met alle data
     */
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = new StringBuilder[]{new StringBuilder(), new StringBuilder(), new StringBuilder()};

        for (int i = 0; i < stringBuilders.length; i++)
        {
            String identifier = (i == 0 ? "defenders" : (i == 1 ? "attackers" : "midfielders"));
            ArrayList<Player> players = (i == 0 ? defenders : (i == 1 ? attackers : midfielders));

            for (Player player : players)
            {
                stringBuilders[i].append(player.getPlayerID() + "#");
            }

            stringBuilders[i].setLength(stringBuilders[i].length() - 1);

            stringBuilder.append(identifier + "!" + stringBuilders[i].toString() + "@");
        }

        stringBuilder.append("keeper!" + keeper.getPlayerID());

        return stringBuilder.toString();
    }
}

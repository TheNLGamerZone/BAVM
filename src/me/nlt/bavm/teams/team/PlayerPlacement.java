package me.nlt.bavm.teams.team;

import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;

public class PlayerPlacement
{
    private Player keeper;
    private ArrayList<Player> defenders;
    private ArrayList<Player> attackers;
    private ArrayList<Player> midfielders;

    public PlayerPlacement(Player keeper, ArrayList<Player> defenders, ArrayList<Player> attackers, ArrayList<Player> midfielders)
    {
        this.keeper = keeper;
        this.defenders = defenders;
        this.midfielders = midfielders;
        this.attackers = attackers;
    }

    public Player getKeeper()
    {
        return this.keeper;
    }

    public void setKeeper(Player keeper)
    {
        this.keeper = keeper;
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

    public boolean isPlaced(Player player)
    {
        return player == keeper || attackers.contains(player) || midfielders.contains(player) || defenders.contains(player);
    }

    public int getSize()
    {
        return attackers.size() + defenders.size() + midfielders.size() + (keeper != null ? 1 : 0);
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder[] stringBuilders = new StringBuilder[]{new StringBuilder(), new StringBuilder(), new StringBuilder()};

        for (int i = 0; i < stringBuilders.length; i++)
        {
            String identifier = (i ==  0 ? "defenders" : (i == 1 ? "attackers" : "midfielders"));
            ArrayList<Player> players = (i ==  0 ? defenders : (i == 1 ? attackers : midfielders));

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

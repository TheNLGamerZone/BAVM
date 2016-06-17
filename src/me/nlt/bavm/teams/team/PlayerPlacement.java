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

    public boolean isPlaced(Player player)
    {
        return player == keeper || attackers.contains(player) || midfielders.contains(player) || defenders.contains(player);
    }

    public int getSize()
    {
        return attackers.size() + defenders.size() + midfielders.size() + (keeper != null ? 1 : 0);
    }

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

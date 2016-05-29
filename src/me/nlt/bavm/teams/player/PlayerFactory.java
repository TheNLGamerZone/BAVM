package me.nlt.bavm.teams.player;

import me.nlt.bavm.teams.PlayerStats;
import me.nlt.bavm.teams.exceptions.InvalidPlayerException;

public class PlayerFactory
{
    /**
     * Deze methode maakt een speler-object uit een string met data
     * @param playerString String met data
     * @return Gemaakte speler
     * @throws InvalidPlayerException Als een speler onjuiste gegevens of een onjuist controlegetal heeft
     */
    public static Player createPlayer(String playerString) throws InvalidPlayerException
    {
        String playerName = "";
        int playerID = 0;
        Position position = null;
        double[] playerStats = new double[PlayerStats.Stat.values().length];
        double checkSum = 0;

        // Randen van de string strippen
        playerString = playerString.substring(7, playerString.length() - 1);

        // String in stukjes 'hakken' op de plekken van de komma's
        String[] rawPlayerData = playerString.split(",");

        // Door de stukjes loopen
        for (String string : rawPlayerData)
        {
            // Losse stukjes maken met een datatype en de data zelf
            String dataType = string.split("=")[0];
            String data = string.split("=")[1];

            switch (dataType)
            {
                case "playerstats":
                    data = data.substring(12, data.length());

                    for (String stats : data.split(">"))
                    {
                        // Stat verkrijgen
                        String stat = stats.split(":")[0];
                        PlayerStats.Stat playerStat = PlayerStats.getSkill(stat.toUpperCase());
                        double value = 0;

                        // Double proberen maken uit string
                        try {
                            value = Double.parseDouble(stats.split(":")[1].split("%", 2)[0].replaceAll("}", ""));
                        } catch (NumberFormatException e)
                        {
                        	throw new InvalidPlayerException(playerName);
                        }

                        // Juiste waarde in de array zetten mits de huidige stat een bestaande is
                        if (playerStat == null)
                        {
                        	throw new InvalidPlayerException(playerName);
                        }

                        playerStats[playerStat.getLocation()] = value;
                    }
                    
                    // Proberen het controlegetal te parsen
                    try {
                    	checkSum = Double.parseDouble(data.split("%")[1].replace("}", ""));
                    } catch (NumberFormatException e)
                    {
                    	throw new InvalidPlayerException(playerName);
                    }
                    break;
                case "name":
                    // _'s worden vervangen met een spatie en dan wordt de waarde opgeslagen in de variabele playerName
                    playerName = data.replaceAll("_", " ");
                    break;
                case "id":
                    int value = 0;

                    // Integer proberen maken uit string
                    try {
                        value = Integer.parseInt(data);
                    } catch (NumberFormatException e)
                    {
                        break;
                    }

                    playerID = value;
                    break;
                case "position":
                    for (Position positions : Position.values())
                    {
                        if (positions.name().equalsIgnoreCase(data))
                        {
                            position = positions;
                        }
                    }
                default:
                    break;
            }
        }

        // Speler met de verkregen gegevens maken
        Player player = new Player(playerName, playerID, position, playerStats);
        
        // Controlegetallen vergelijken
        if (player.getPlayerStats().getCheckSum() != checkSum)
        {
        	// Controlegetallen kloppen niet, er zijn dus waarschijnlijk handmatig stats aangepast
        	throw new InvalidPlayerException(playerName, checkSum);
        }
        
        // Controlegetallen kloppen
        return player;
    }
}

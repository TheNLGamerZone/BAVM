package me.nlt.bavm.teams;

public class PlayerFactory
{
    /**
     * Deze methode maakt een speler-object uit een string met data
     * @param playerString String met data
     * @return Gemaakte speler
     */
    public static Player createPlayer(String playerString)
    {
        String playerName = "";
        int playerID = 0;
        double[] playerStats = new double[PlayerStats.Stat.values().length];

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
                            value = Double.parseDouble(stats.split(":")[1].replaceAll("}", ""));
                        } catch (NumberFormatException e)
                        {
                            continue;
                        }

                        // Juiste waarde in de array zetten mits de huidige stat een bestaande is
                        if (playerStat == null)
                        {
                            continue;
                        }

                        playerStats[playerStat.getLocation()] = value;
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
                default:
                    break;
            }
        }

        return new Player(playerName, playerID, playerStats);
    }
}

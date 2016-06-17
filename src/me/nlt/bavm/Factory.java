package me.nlt.bavm;

import me.nlt.bavm.game.Match;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.exceptions.FactoryException;
import me.nlt.bavm.teams.exceptions.InvalidPlayerException;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerStats;
import me.nlt.bavm.teams.player.Position;
import me.nlt.bavm.teams.team.Team;

import java.util.ArrayList;
import java.util.List;

public class Factory
{
    /**
     * Deze methode maakt een speler-object uit een string met data
     *
     * @param playerString String met data
     * @return Gemaakte speler
     * @throws me.nlt.bavm.teams.exceptions.InvalidPlayerException Als een speler onjuiste gegevens of een onjuist controlegetal heeft
     */
    public static Player createPlayer(String playerString) throws InvalidPlayerException
    {
        String playerName = "";
        int playerID = 0;
        Position position = null;
        double[] playerStats = new double[PlayerStats.Stat.values().length];
        double checkSum = 0;

        // Randen van de string strippen
        playerString = playerString.trim().substring(7, playerString.trim().length() - 1);

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
                        try
                        {
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
                    try
                    {
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
                    try
                    {
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

    public static Coach createCoach(String coachString) throws FactoryException
    {
        String coachName = null;
        int coachID = -1;
        double[] coachStats = null;

        coachString = coachString.trim().substring(6).replaceAll("}", "");

        for (String coachData : coachString.split(","))
        {
            switch (coachData.split("=")[0])
            {
                case "name":
                    coachName = coachData.split("=")[1].replaceAll("_", " ");
                    break;
                case "id":
                    try
                    {
                        coachID = Integer.parseInt(coachData.split("=")[1]);
                    } catch (NumberFormatException e)
                    {
                        throw new FactoryException("coach", -1, "Kon ID niet laden");
                    }
                    break;
                case "coachstats":
                    List<Double> cStats = new ArrayList<>();
                    String[] stats = coachData.split("=")[1].substring(11).replaceAll("}", "").split(">");

                    for (String stat : stats)
                    {
                        cStats.add(Double.parseDouble(stat.split(":")[1]));
                    }

                    coachStats = new double[cStats.size()];

                    for (int i = 0; i < cStats.size(); i++)
                    {
                        coachStats[i] = cStats.get(i);
                    }
                    break;
            }
        }

        if (coachStats == null || coachStats.length != 3)
        {
            throw new FactoryException("coach", coachID, "Kon niet alle stats laden");
        } else if (coachName == null)
        {
            throw new FactoryException("coach", coachID, "Kon naam niet laden");
        }

        return new Coach(coachName, coachID, coachStats);
    }

    public static Team createTeam(String teamString) throws FactoryException
    {
        String teamName = null, placement = null, scores = null;
        int teamID = -2, coachID = -1, currentGeld = -1, weeklyIncome = -1;
        int[] playerIDs = null;
        double teamTalent = -1;

        teamString = teamString.trim().substring(5).replaceAll("}", "");

        for (String teamData : teamString.split(","))
        {
            String data = teamData.split("=")[1];

            switch (teamData.split("=")[0])
            {
                case "name":
                    teamName = data.replaceAll("_", " ");
                    break;
                case "id":
                    try
                    {
                        teamID = Integer.parseInt(data);
                    } catch (NumberFormatException e)
                    {
                        throw new FactoryException("team", -1, "Kon ID niet laden");
                    }
                    break;
                case "info":
                    for (String infoString : data.split("<"))
                    {
                        String infoData = infoString.split(";")[1];

                        switch (infoString.split(";")[0])
                        {
                            case "teamtalent":
                                try
                                {
                                    teamTalent = Double.parseDouble(infoData);
                                } catch (NumberFormatException e)
                                {
                                    throw new FactoryException("team", teamID, "Kon talent niet laden");
                                }
                                break;
                            case "players":
                                ArrayList<Integer> ids = new ArrayList<>();

                                for (String id : infoData.split(">"))
                                {
                                    ids.add(Integer.parseInt(id));
                                }

                                playerIDs = new int[ids.size()];

                                for (int i = 0; i < ids.size(); i++)
                                {
                                    playerIDs[i] = ids.get(i);
                                }

                                if (playerIDs.length == 0)
                                {
                                    throw new FactoryException("team", teamID, "Kon spelers niet laden");
                                }

                                break;
                            case "coach":
                                try
                                {
                                    coachID = Integer.parseInt(infoData);
                                } catch (NumberFormatException e)
                                {
                                    throw new FactoryException("team", teamID, "Kon ID van coach niet laden");
                                }
                                break;
                            case "geld":
                                try
                                {
                                    currentGeld = Integer.parseInt(infoData.split("!")[1]);
                                    weeklyIncome = Integer.parseInt(infoData.split("!")[2]);
                                } catch (NumberFormatException e)
                                {
                                    throw new FactoryException("team", teamID, "Kon geld niet laden");
                                }
                                break;
                            case "placement":
                                placement = infoData;
                                break;
                            case "scores":
                            	scores = infoData;
                            	break;
                        }
                    }
                    break;
            }
        }

        if (teamName == null || placement == null)
        {
            throw new FactoryException("team", teamID, "Kon " + (teamName == null ? "naam" : "opstelling") + " niet laden");
        }

        return new Team(teamName, teamID, playerIDs, coachID, teamTalent, currentGeld, weeklyIncome, placement, scores);
    }

    public static Match createMatch(String matchString) throws FactoryException
    {
        int matchID = -1, homeID = -1, visitorID = -1;
        int[] matchResult = new int[2];
        ArrayList<String> matchLog = new ArrayList<>();
        matchString = matchString.trim().substring(6).replaceAll("}", "");

        for (String matchData : matchString.split(","))
        {
            String data = matchData.split("=")[1];

            switch (matchData.split("=")[0])
            {
                case "id":
                    try
                    {
                        matchID = Integer.parseInt(data);
                    } catch (NumberFormatException e)
                    {
                        throw new FactoryException("match", -1, "Kon ID niet laden");
                    }
                    break;
                case "teams":
                    try
                    {
                        homeID = Integer.parseInt(data.split(":")[0]);
                        visitorID = Integer.parseInt(data.split(":")[1]);
                    } catch (NumberFormatException e)
                    {
                        throw new FactoryException("match", matchID, "Kon teams niet laden");
                    }
                    break;
                case "score":
                    try
                    {
                        matchResult[0] = Integer.parseInt(data.split(":")[0]);
                        matchResult[1] = Integer.parseInt(data.split(":")[1]);
                    } catch (NumberFormatException e)
                    {
                        throw new FactoryException("match", matchID, "Kon score niet laden");
                    }
                    break;
                case "logs":
                    for (String logMessage : data.split("@"))
                    {
                        matchLog.add(logMessage.replaceAll("_", " ").replaceAll("%", ",").replaceAll("~", "="));
                    }
                    break;
            }
        }

        if (matchLog.isEmpty())
        {
            throw new FactoryException("match", matchID, "Kon de logs niet laden");
        }

        return new Match(BAVM.getTeamManager().getTeam(homeID) + " - " + BAVM.getTeamManager().getTeam(visitorID), matchID, homeID, visitorID, matchResult, matchLog);
    }
}

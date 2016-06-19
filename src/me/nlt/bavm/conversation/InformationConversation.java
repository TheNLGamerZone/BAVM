package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.coach.CoachStats;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerStats;
import me.nlt.bavm.teams.player.Position;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo;

import java.text.DecimalFormat;

public class InformationConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        backToMain:
        while (true)
        {
            display.appendText("\n\t\t- - - - - - - - - [ Het informatiecentrum ] - - - - - - - - -",
                    "Typ altijd '-1' om terug te keren naar de vorige setting en typ altijd '-2' om terug te keren naar het hoofdmenu",
                    "Typ '-3' om je eigen team te bekijken",
                    "Typ '-4' om een ander team te bekijken",
                    "Typ '-5' voor een lijst met alle teams",
                    "Typ '-6' om een speler te bekijken",
                    "Typ '-7' om een coach te bekijken"
            );

            int mainNumber = (int) display.readDouble(false);

            if (mainNumber == -1 || mainNumber == -2)
            {
                break;
            }

            if (mainNumber == -3 || mainNumber == -4)
            {

                Team team;

                if (mainNumber == -3)
                {
                    team = BAVM.getTeamManager().playerTeam;
                } else
                {
                    while (true)
                    {
                        int teamID = (int) display.readDouble(false, "Typ het ID van het team dat je wilt bekijken.");
                        team = BAVM.getTeamManager().getTeam(teamID);

                        if (team != null)
                        {
                            break;
                        }

                        display.appendText("Dat team bestaat niet!");
                    }
                }

                display.appendText("\n\t\t- - - - - - - - - - - [ Team bekijken ] - - - - - - - - - - - ",
                        "Naam: " + team.getTeamName(),
                        "Directeur: " + team.getDirectorName(),
                        "Team talent: " + team.getTeamInfo().getTeamTalent(),
                        "Team ID: " + team.getID(),
                        "Geld in de kas: $" + team.getTeamInfo().getTeamGeld().getCurrentGeld(),
                        "Inkomsten per week: $" + team.getTeamInfo().getTeamGeld().getWeeklyIncome(),
                        "Coach: " + team.getTeamInfo().getTeamCoach().getCoachName() + " (ID: " + team.getTeamInfo().getTeamCoach().getCoachID() + ")",
                        "Opstelling:",
                        "   Keeper: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.KEEPER),
                        "   Aanvallers: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.ATTACKER),
                        "   Verdedigers: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.DEFENDER),
                        "   Middenvelders: " + team.getTeamInfo().getPlayerPlacement().getPlacement(Position.MIDFIELDER),
                        "Spelers:"
                );

                for (Player player : team.getTeamInfo().getPlayers())
                {
                    display.appendText("   " + player.getPlayerName() + " (ID: " + player.getID() + ")");
                }

                display.appendText("Co\u00EBfficienten: ");

                for (TeamInfo.StatCoefficient statCoefficient : team.getTeamInfo().getStatCoefficients().keySet())
                {
                    display.appendText("   " + statCoefficient.name().substring(0, 1) + statCoefficient.name().substring(1).toLowerCase() + ": " + team.getTeamInfo().getStatCoefficients().get(statCoefficient));
                }

                display.readLine("Typ iets om terug te keren naar het informatiecentrum.");
            }

            if (mainNumber == -5)
            {
                display.appendText("\n\t\t- - - - - - - - - - - [ Teams bekijken ] - - - - - - - - - - - ");

                for (Object object : BAVM.getTeamManager().getLoadedTeams())
                {
                    Team team = (Team) object;

                    display.appendText(" " + team.getTeamName() + " (ID: " + team.getID() + ")");
                }

                display.readLine(false, "Typ iets om terug te keren naar het informatiecentrum.");
            }

            if (mainNumber == -6)
            {
                Player player;

                display.appendText("\n\t\t- - - - - - - - - - [ Speler bekijken ] - - - - - - - - - - ");

                while (true)
                {
                    int playerID = (int) display.readDouble(false, "Typ het ID van een speler om zijn informatie te bekijken");
                    player = BAVM.getPlayerManager().getPlayer(playerID);

                    if (playerID == -2)
                    {
                        break backToMain;
                    } else if (player == null)
                    {
                        display.appendText("Die speler bestaat niet!");
                    } else
                    {
                        break;
                    }
                }

                display.appendText("Naam: " + player.getPlayerName(),
                        "Speler ID: " + player.getID(),
                        "Aanbevolen positie: " + player.getPosition().name().substring(0, 1) + player.getPosition().name().substring(1).toLowerCase(),
                        "Totale skill: " + player.getPlayerStats().getTotalSkill(),
                        "Waarde: $" + new DecimalFormat("#####.##").format(player.getMarketValue()) + (BAVM.getTeamManager().marketTeam.getTeamInfo().getPlayers().contains(player) ? " (Speler te koop)" : " (Speler niet te koop)"),
                        "Skills:"
                );

                for (PlayerStats.Stat stat : PlayerStats.Stat.values())
                {
                    display.appendText("   " + stat.name().substring(0, 1) + stat.name().substring(1).toLowerCase() + ": " + player.getPlayerStats().getValue(stat));
                }

                display.readLine(false, "Typ iets om terug te keren naar het informatiecentrum.");
            }

            if (mainNumber == -7)
            {
                Coach coach;

                display.appendText("\n\t\t- - - - - - - - - - [ Coach bekijken ] - - - - - - - - - - ");

                while (true)
                {
                    int playerID = (int) display.readDouble(false, "Typ het ID van een coach om zijn informatie te bekijken");
                    coach = BAVM.getCoachManager().getCoach(playerID);

                    if (playerID == -2)
                    {
                        break backToMain;
                    } else if (coach == null)
                    {
                        display.appendText("Die coach bestaat niet!");
                    } else
                    {
                        break;
                    }
                }

                display.appendText("Naam: " + coach.getCoachName(),
                        "Coach ID: " + coach.getCoachID(),
                        "Skills:"
                );

                for (CoachStats.CStat stat : CoachStats.CStat.values())
                {
                    display.appendText("   " + stat.name().substring(0, 1) + stat.name().substring(1).toLowerCase() + ": " + coach.getCoachStats().getValue(stat));
                }

                display.readLine(false, "Typ iets om terug te keren naar het informatiecentrum.");
            }
        }
    }
}

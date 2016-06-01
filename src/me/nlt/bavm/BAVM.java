package me.nlt.bavm;

import me.nlt.bavm.files.FileManager;
import me.nlt.bavm.game.Match;
import me.nlt.bavm.game.MatchManager;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.coach.CoachManager;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerManager;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamInfo;
import me.nlt.bavm.teams.team.TeamManager;

import java.awt.*;

public class BAVM
{
    private static Display display;
    private final Object lockObject;

    private static FileManager fileManager;
    private static PlayerManager<Player> playerManager;
    private static TeamManager<Team> teamManager;
    private static MatchManager<Match> matchManager;
    private static CoachManager<Coach> coachManager;

    /**
     * Main method
     *
     * @param args Arguments
     */
    public static void main(String[] args)
    {
        // Zo snel mogelijk weg van static
        new BAVM();
    }

    /**
     * BAVM constructor
     */
    public BAVM()
    {
        // Lock aanmaken
        lockObject = new Object();

        // Display aanmaken op EDT
        EventQueue.invokeLater(() -> display = new Display(16, 58, lockObject));

        // Wachten op unlock
        this.lockThread();
    }

    /**
     * Verder de game opstarten
     */
    private void lockThread()
    {
        try
        {
            // Object 'locken'
            synchronized (lockObject)
            {
                lockObject.wait();
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // Eigenlijk lieg ik bij het eerste bericht maar anders kan het niet
        display.appendText("Thread locked, aan het wachten op een unlock", "Thread ge-unlocked\n");

        //TODO replace 'true' with 'fileManager.firstStart'
        fileManager = new FileManager();
        playerManager = new PlayerManager<Player>(true);
        coachManager = new CoachManager<Coach>(true);
        teamManager = new TeamManager<Team>(true);
        matchManager = new MatchManager<Match>();

        // Zorgen dat de rest laad
        display.appendText("BAVM is gereed om te worden gebruikt!\n\n----");
        this.initGame();
    }

    /**
     * Deze wordt aangeroepen nadat de thread ge-unlocked is zodat alles kan laden
     */
    private void initGame()
    {

        for (int i = 0; i < 25; i++)
        {
            matchManager.simulateMatch(0, 1);
            Match match = matchManager.getMatch(i);

            display.appendText(match.getMatchGoals()[0] + "-" + match.getMatchGoals()[1] + " -> " + (match.getMatchGoals()[0] > match.getMatchGoals()[1] ? "Team 0" : (match.getMatchGoals()[0] == match.getMatchGoals()[1] ? "Gelijk" : "Team 1")));
        }

        while (true)
        {
            display.appendText("Wil je een speler (-2) of een team bekijken (-3)?", "Typ -1 om te stoppen");

            int readInt = (int) display.readDouble();

            if (readInt == -1)
            {
                display.appendText("Gestopt.");
                break;
            } else if (readInt == -2)
            {
                while (true)
                {
                    display.appendText("Typ een id in om een speler te bekijken", "Typ -1 om iets anders te doen");
                    int id = (int) display.readDouble();
                    Player player = playerManager.getPlayer(id);

                    if (id == -1)
                    {
                        break;
                    } else if (player == null)
                    {
                        display.appendText("Bij dat ID hoort geen speler!");
                    } else
                    {
                        display.appendText("Naam: " + player.getPlayerName(), "ID: " + player.getPlayerID(), "Aanbevolen positie: " + player.getPosition(), "Stats: " + player.getPlayerStats().toString(), " ");
                    }
                }
            } else if (readInt == -3)
            {
                while (true)
                {
                    display.appendText("Typ een id in om een team te bekijken", "Typ -1 om iets anders te doen");
                    int id = (int) display.readDouble();
                    Team team = teamManager.getTeam(id);

                    if (id == -1)
                    {
                        break;
                    } else if (id == -666)
                    {
                        display.appendText("Naam: marketTeam", "Players: " + teamManager.marketTeam.getTeamInfo().getPlayers().size());
                    } else if (team == null)
                    {
                        display.appendText("Bij dat ID hoort geen team!");
                    } else
                    {
                        display.appendText("Naam: " + team.getTeamName(), "ID: " + team.getTeamID(), "Coach: " + team.getTeamInfo().getTeamCoach().getCoachName() + " (" + team.getTeamInfo().getTeamCoach().getID() + ")", "Players:");

                        for (Player player : team.getTeamInfo().getPlayers())
                        {
                            display.appendText(" - " + player.getPlayerName() + " (" + player.getPosition() + ")");
                        }

                        while (true)
                        {
                            display.appendText(" ", "Als je de teamcoëfficienten wilt bekijken, typ -4, zo niet, typ een ander getal");

                            int askCoefficient = (int) display.readDouble();

                            if (askCoefficient == -4)
                            {
                                TeamInfo teamInfo = team.getTeamInfo();

                                for (TeamInfo.StatCoefficient statCoefficient : teamInfo.getStatCoefficients().keySet())
                                {
                                    display.appendText("Coefficient " + statCoefficient.name() + ": " + teamInfo.getStatCoefficients().get(statCoefficient));
                                }
                            } else
                            {
                                break;
                            }
                        }
                    }
                }
            } else
            {
                display.appendText("Foute input!");
            }

        }

        /*
        //ONE TEAM
        //amount of players to create (in multiples of 23)
        int amount23s = 23;
        //the place ratio's (goalkeeper 3/23, defender 8/23 etc., midfielder, attacker)
        double placeRatios[] = {3, 8, 6, 6};
        RandomPlayer.createRandomPlayers(amount23s, placeRatios);
        RandomTeam.createRandomTeams(19);

        String PlayerCoachName = display.readLine("Welcome to BAVM, please enter your coach name.");
        double[] coachStats = {50, 50, 50};
        Coach coach = new Coach(PlayerCoachName, 19, coachStats);
        display.appendText(coach.toString());

        String PlayerTeamName = display.readLine("Welcome to BAVM, please enter your team name.");
        Team team = new Team(PlayerTeamName, 19, RandomTeam.generatePlayerIDList(0.5), 19);
        display.appendText(team.toString());

    	Player player = new Player("Tim Anema", 0, new double[]{0, 1, 2, 3, 4, 5});
        Player playerCopy = null;

        try {
			playerCopy = PlayerFactory.createPlayer(player.toString());
		} catch (InvalidPlayerException e) {
			e.printStackTrace();
			return;
		}

        System.out.println("Van player: " + player.toString());
        System.out.println("Van playerCopy: " + playerCopy.toString());*/
    }

    public static Display getDisplay()
    {
        return display;
    }

    public static PlayerManager getPlayerManager()
    {
        return playerManager;
    }

    public static TeamManager getTeamManager()
    {
        return teamManager;
    }

    public static MatchManager getMatchManager()
    {
        return matchManager;
    }

    public static CoachManager getCoachManager()
    {
        return coachManager;
    }
}
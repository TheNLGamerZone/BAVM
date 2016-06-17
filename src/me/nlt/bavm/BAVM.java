package me.nlt.bavm;

import me.nlt.bavm.conversation.*;
import me.nlt.bavm.files.FileManager;
import me.nlt.bavm.game.Match;
import me.nlt.bavm.game.MatchManager;
import me.nlt.bavm.season.Season;
import me.nlt.bavm.season.Week;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.coach.CoachManager;
import me.nlt.bavm.teams.player.Player;
import me.nlt.bavm.teams.player.PlayerManager;
import me.nlt.bavm.teams.team.Team;
import me.nlt.bavm.teams.team.TeamManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class BAVM
{
    private static Display display;
    private final Object lockObject;

    private static FileManager fileManager;
    private static PlayerManager<Player> playerManager;
    private static TeamManager<Team> teamManager;
    private static MatchManager<Match> matchManager;
    private static CoachManager<Coach> coachManager;
    private static Season season;

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
        EventQueue.invokeLater(() -> display = new Display(35, 65, lockObject));

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
        display.appendText("Thread locked, aan het wachten op een unlock", "Thread ge-unlocked", "Spelers en teams worden geladen\n");

        fileManager = new FileManager();
        playerManager = new PlayerManager<>(fileManager.firstStart);
        coachManager = new CoachManager<>(fileManager.firstStart);
        teamManager = new TeamManager<>(fileManager.firstStart);
        matchManager = new MatchManager<>(fileManager.firstStart);
        Week.weekNumber = fileManager.getWeekNumber();
        season = new Season(true);

        // Zorgen dat de rest laad
        display.appendText("BAVM is gereed om te worden gebruikt!",
                "Aantal spelers: " + (fileManager.readAmount("players") - 1),
                "Aantal teams: " + (fileManager.readAmount("teams") - 1),
                "Aantal coaches: " + (fileManager.readAmount("coaches") - 1)
        );

        this.initGame();
    }

    /**
     * Deze wordt aangeroepen nadat de thread ge-unlocked is zodat alles kan laden
     */
    private void initGame()
    {
        if (fileManager.firstStart)
        {
            new StartingConversation().startConversation(display);
        }

        display.appendText("\n\t\t- - - - - - - - - - - - - - - [ WEEK " + (Week.getWeekNumber() + 1) + " ] - - - - - - - - - - - - - -");

        while (true)
        {
            display.appendText("\n\t\t- - - - - - - - - - - - - [ Hoofdmenu ] - - - - - - - - - - - - - ", "Opties:"
                    , "    -9 -> Verwijder alle voortgang en begin opnieuw"
                    , "    0  -> Stop het spel"
                    , "    1  -> Ga naar het informatiecentrum"
                    , "    2  -> Ga naar het wedstrijdcentrum"
                    , "    3  -> Ga naar teammanagement"
                    , "    4  -> Ga naar de markt"
                    , "    5  -> Ga naar het seizoencentrum"
                    , "    6  -> Be\u00EBindig deze week"
            );

            int mainNumber = (int) display.readDouble(false);

            if (mainNumber == 0)
            {
                display.onClose();
            }

            if (mainNumber == 1)
            {
                new InformationConversation().startConversation(display);
            }

            if (mainNumber == 2)
            {
                new MatchConversation().startConversation(display);
            }

            if (mainNumber == 3)
            {
                new ManagementConversation().startConversation(display);
            }

            if (mainNumber == 4)
            {
                new MarketConversation().startConversation(display);
            }

            if (mainNumber == 5)
            {
                new SeasonConversation().startConversation(display);
            }

            if (mainNumber == 6)
            {
                new WeekendConversation().startConversation(display);
            }

            if (mainNumber == -9)
            {
                if ((int) display.readDouble(false, "Je staat op het punt om al je voortgang DEFINITIEF te verwijderen en opnieuw te beginnen\nTyp 123 om dit te bevestigen") == 123)
                {
                    display.clearText();
                    display.appendText("Data wordt verwijderd en spel wordt opnieuw gestart!");

                    if (!resetGame())
                    {
                        display.appendText("Deze functie is alleen beschikbaar met een JAR, dus niet via een built-in IDE Java Application");
                    }
                } else
                {
                    display.appendText("Je start niet opnieuw!");
                }
            }

            if (mainNumber == 8080)
            {
                rollCredits();
            }
        }
    }

    public static FileManager getFileManager()
    {
        return fileManager;
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

    public static Season getSeason()
    {
        return season;
    }

    public static void rollCredits()
    {
        display.clearText();

        try
        {
            String[] messages = {
                    "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",
                    "Bedankt voor het spelen van de BrinkAnema Voetbal Manager!", "", "", "Dit spel is gemaakt voor de NLT Java opdracht.", "", "", "Game Director: Tim Anema", "", "",
                    "Co-Game Director: Tip ten Brink", "", "", "Lead Designer: Tim Anema", "", "", "Co-Lead Designer: Tip ten Brink", "", "", "Lead Programmer: Tim Anema", "", "",
                    "Co-Lead Programmer: Tip ten Brink", "", "", "Muziek: 'Victory' by Two Steps from Hell", "", "", "Special thanks:", " Stackoverflow", " Meneer Erik Smedema", " Meneer Theulings",
                    "", "",
                    "More credits:", "Conversation: Tim", "InformationConversation: Tim", "ManagementConversation: Tim", "MarketConversation: Tim", "MarketFilter: Tim", "MatchConversation: Tip", "SeasonConversation: Tip", "WeekendConversation: Tip",
                    "FileManager: Tim", "Game: Tip", "Match: Tip", "MatchManager: Tip & Tim", "MatchSequence: Tip", "RandomNames: Tip", "RandomStats: Tip & Tim",
                    "AllScores: Tip", "MatchWeek: Tip", "PlannedWeek: Tip", "Season: Tip", "Week: Tip",
                    "Coach: Tip & Tim", "CoachManager: Tim", "CoachStats: Tip & Tim", "FactoryException: Tim", "InvalidPlayerException: Tim", "Player: Tim", "PlayerManager: Tim", "PlayerStats: Tim", "Position: Tim",
                    "Geld: Tip", "PlayerPlacement: Tim", "Team: Tip & Tim", "TeamInfo: Tip & Tim", "TeamManager: Tim", "TransferResult: Tim",
                    "Manageable: Tim", "Manager: Tim", "Market: Tim", "BAVM (multi-threading): Tim", "BAVM (menu): Tip & Tim", "Display: Tim", "Factory: Tim", "XML-Indeling: Tim",
                    "", "Dat was het!", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "\n"
            };
            int delay = (2 * 60 + 48) * 1000 / messages.length;
            URL url = new URL("http://tim.suppatim.me/credit_music_edited.wav");
            Clip clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

            clip.open(audioInputStream);
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-10.0F);
            clip.loop(clip.LOOP_CONTINUOUSLY);

            display.appendText(delay, messages);
            Thread.sleep(5);
            display.clearText();
            clip.stop();
            clip.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean resetGame()
    {
        try
        {
            final String javaBinary = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File gameJAR = new File(BAVM.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            final ArrayList<String> commands = new ArrayList<>();
            final ProcessBuilder processBuilder;

            if (!gameJAR.getName().endsWith(".jar"))
            {
                return false;
            }

            commands.add(javaBinary);
            commands.add("-jar");
            commands.add(gameJAR.getPath());
            processBuilder = new ProcessBuilder(commands);
            processBuilder.start();
            fileManager.deleteData();

            System.exit(0);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
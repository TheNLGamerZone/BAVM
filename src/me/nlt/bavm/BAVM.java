package me.nlt.bavm;

import me.nlt.bavm.league.RandomNames;

import java.awt.*;

public class BAVM
{
    private static BAVM mainInstance;
    private Display display;

    private final Object lockObject;


    /**
     * Main method
     *
     * @param args Arguments
     */
    public static void main(String[] args)
    {
        // Zo snel mogelijk weg van static
        mainInstance = new BAVM();
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
        display.appendText("Thread locked, aan het wachten op een unlock");
        display.appendText("Thread ge-unlocked\n");

        // Zorgen dat de rest laad
        this.initGame();
    }

    /**
     * Deze wordt aangeroepen nadat de thread ge-unlocked is zodat alles kan laden
     */
    private void initGame()
    {
        RandomNames randomNames = new RandomNames();

        String BALTeamNames[] = randomNames.getBALTeamNames();

        for (int i = 0; i < 20; i++)
        {
            display.appendText(i + 1 + ". " + BALTeamNames[i]);
        }
    }

    public static BAVM getMainInstance()
    {
        return mainInstance;
    }
}

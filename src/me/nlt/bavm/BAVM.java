package me.nlt.bavm;

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
        System.out.println("Thread locked, aan het wachten op een unlock");
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

        System.out.println("Thread ge-unlocked");

        // Zorgen dat de rest laad
        this.initGame();
    }

    /**
     * Deze wordt aangeroepen nadat de thread ge-unlocked is zodat alles kan laden
     */
    private void initGame()
    {
        // Even testen
        display.appendText("Hallo ik ben een string", "Ik wil kaas", "Ik ben ook een klant");
    }

    public static BAVM getMainInstance()
    {
        return mainInstance;
    }
}

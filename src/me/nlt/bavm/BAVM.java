package me.nlt.bavm;

import me.nlt.bavm.league.RandomPlayer;
import me.nlt.bavm.teams.Player;
import me.nlt.bavm.teams.PlayerFactory;
import me.nlt.bavm.teams.exceptions.InvalidPlayerException;

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
        display.appendText("Thread locked, aan het wachten op een unlock", "Thread ge-unlocked\n", "BAVM is gereed om te worden gebruikt!\n\n----");

        // Zorgen dat de rest laad
        this.initGame();
    }

    /**
     * Deze wordt aangeroepen nadat de thread ge-unlocked is zodat alles kan laden
     */
    private void initGame()
    {
        //ONE TEAM
        //amount of players to create (in multiples of 23)
        int amount23s = 10;
        //the place ratio's (goalkeeper 3/23, defender 8/23 etc., midfielder, attacker)
        double placeRatios[] = {3, 8, 6, 6};
        RandomPlayer.createRandomPlayers(amount23s, placeRatios);



    	/*Player player = new Player("Tim Anema", 0, new double[]{0, 1, 2, 3, 4, 5});
        Player playerCopy = null;

        try {
			playerCopy = PlayerFactory.createPlayer("Player{name=Tim_Anema,id=0,playerstats=PlayerStats{afmaken:0.0>aanval:4.0>balbezit:2.0>verdedigen:3.0>conditie:4.0>geluk:5.0>doelman:6.0%130.0}}");
		} catch (InvalidPlayerException e) {
			e.printStackTrace();
			return;
		}

        System.out.println("Van player: " + player.toString());
        System.out.println("Van playerCopy: " + playerCopy.toString());*/
    }

    public static BAVM getMainInstance()
    {
        return mainInstance;
    }
}

package me.nlt.bavm.files;

import me.nlt.bavm.BAVM;

import java.util.Timer;
import java.util.TimerTask;

public class AutoSave
{
    private Timer timer;
    private final int delay;

    /**
     * AutoSave contructor
     *
     * @param delayInSecs Tijd in secondes tussen een autosave
     */
    public AutoSave(final int delayInSecs)
    {
        this.timer = new Timer(true);
        this.delay = delayInSecs * 1000;

        BAVM.getFileManager().saveData();
        this.runTimer();
    }

    /**
     * Start de timer die om de zoveel tijd alles opslaat
     */
    private void runTimer()
    {
        // Timer starten
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                // Wat berichten printen en dingen opslaan
                System.out.println("Starting autosave ...");
                BAVM.getDisplay().appendText("\n  -> Automatisch aan het opslaan ...");
                BAVM.getFileManager().saveAll();
                BAVM.getDisplay().appendText("  -> Data automatisch opgeslagen!\n");
            }
        }, delay, delay);
    }
}

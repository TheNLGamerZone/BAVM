package me.nlt.bavm.files;

import me.nlt.bavm.BAVM;

import java.util.Timer;
import java.util.TimerTask;

public class AutoSave
{
    private Timer timer;
    private final int delay;

    public AutoSave(final int delayInSecs)
    {
        this.timer = new Timer(true);
        this.delay = delayInSecs * 1000;

        BAVM.getFileManager().saveData();
        this.runTimer();
    }

    private void runTimer()
    {
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                BAVM.getDisplay().appendText("\n  -> Automatisch aan het opslaan ...");
                BAVM.getFileManager().saveAll();
                BAVM.getDisplay().appendText("  -> Data automatisch opgeslagen!\n");
            }
        }, delay, delay);
    }
}

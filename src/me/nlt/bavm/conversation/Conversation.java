package me.nlt.bavm.conversation;

import me.nlt.bavm.Display;

import java.text.DecimalFormat;

public interface Conversation
{
    DecimalFormat decimalFormat = new DecimalFormat("#######.##");

    /**
     * Methode die gebruikt wordt om een conversatie te starten
     *
     * @param display De display die gebruikt wordt
     */
    public void startConversation(Display display);
}

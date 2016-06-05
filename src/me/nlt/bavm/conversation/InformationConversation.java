package me.nlt.bavm.conversation;

import me.nlt.bavm.Display;

public class InformationConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        display.appendText("Komt nog!");
    }
}

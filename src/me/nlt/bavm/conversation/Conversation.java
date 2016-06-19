package me.nlt.bavm.conversation;

import me.nlt.bavm.Display;

import java.text.DecimalFormat;

public interface Conversation
{
    DecimalFormat decimalFormat = new DecimalFormat("#######.##");
    public void startConversation(Display display);
}

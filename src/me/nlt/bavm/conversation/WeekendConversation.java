package me.nlt.bavm.conversation;

import me.nlt.bavm.Display;
import me.nlt.bavm.season.Week;

public class WeekendConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        display.appendText("Je staat op het punt om naar de volgende week te gaan, weet je het zeker?\nTyp 123 om naar de volgende week te gaan.");

        int confirmationNumber = (int) display.readDouble(false);

        if (confirmationNumber == 123)
        {
            display.appendText("Weekeinde wordt berekend...");
            Week.endWeek();
        } else
        {
            display.appendText("Je gaat niet naar de volgende week.");
        }
    }
}

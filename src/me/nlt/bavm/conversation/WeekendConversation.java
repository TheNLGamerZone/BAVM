package me.nlt.bavm.conversation;

import me.nlt.bavm.Display;
import me.nlt.bavm.season.Week;

public class WeekendConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        if (Week.getWeekNumber() != 37)
        {
            display.appendText("Je staat op het punt om naar de volgende week te gaan, weet je het zeker?\nTyp 123 om naar de volgende week te gaan.");

            int confirmationNumber = (int) display.readDouble(false);

            if (confirmationNumber == 123)
            {
                display.appendText("Weekeinde wordt berekend...");
                Week.endWeek(false);
            } else if (confirmationNumber == 666)
            {
                for (int i = Week.getWeekNumber(); i < 37; i++)
                {
                    Week.endWeek(false);
                }
            } else
            {
                display.appendText("Je gaat niet naar de volgende week.");
            }
        } else
        {
            display.appendText("Je staat op het punt om naar het SEIZOEN te be\u00EBindigen en de laatste wedstrijd te spelen! Weet je dat heel zeker? \nTyp 123 om naar dit seizoen te be\u00EBindigen.");

            int confirmationNumber = (int) display.readDouble(false);

            if (confirmationNumber == 123)
            {
               Week.endWeek(true);
            } else
            {
                display.appendText("Je eindigt dit seizoen niet.");
            }
        }
    }
}

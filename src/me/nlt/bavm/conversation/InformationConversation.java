package me.nlt.bavm.conversation;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.Display;
import me.nlt.bavm.teams.team.Team;

public class InformationConversation implements Conversation
{
    @Override
    public void startConversation(Display display)
    {
        Team team = BAVM.getTeamManager().playerTeam;

        display.appendText(team.toString());
    }
}

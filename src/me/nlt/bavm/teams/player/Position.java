package me.nlt.bavm.teams.player;

public enum Position
{
    KEEPER(0, 2, 3), DEFENDER(1, 5, 6), MIDFIELDER(2, -1, -1), ATTACKER(3, 6, 7);

    int id;
    int minStart;
    int maxStart;

    private Position(int id, int minStart, int maxStart)
    {
        this.id = id;
        this.minStart = minStart;
        this.maxStart = maxStart;
    }

    public int getId()
    {
        return this.id;
    }

    public int getStartPlayers()
    {
        return Math.random() < .5 ? maxStart : minStart;
    }
}

package me.nlt.bavm.teams.player;

public enum Position
{
    KEEPER(0), DEFENDER(1), MIDFIELDER(2), ATTACKER(3);//, NONE(-1);

    int id;

    private Position(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}

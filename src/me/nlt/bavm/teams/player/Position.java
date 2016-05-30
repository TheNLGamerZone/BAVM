package me.nlt.bavm.teams.player;

public enum Position
{
    KEEPER(0), DEFENDER(1), ATTACKER(2), MIDFIELDER(3);

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

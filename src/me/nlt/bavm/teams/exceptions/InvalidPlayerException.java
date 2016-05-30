package me.nlt.bavm.teams.exceptions;

public class InvalidPlayerException extends Exception
{
    public InvalidPlayerException(String playerName)
    {
        super("Kon speler '" + playerName + "' niet laden: Onjuiste gegevens");
    }

    public InvalidPlayerException(String playerName, double checkSum)
    {
        super("Kon speler '" + playerName + "' niet laden: Onjuist controlegetal " + checkSum);
    }
}

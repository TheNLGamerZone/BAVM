package me.nlt.bavm.teams.exceptions;

public class InvalidPlayerException extends Exception
{
    /**
     * Fout die kan optreden tijdens het maken van een speler
     *
     * @param playerName De naam van de speler
     */
    public InvalidPlayerException(String playerName)
    {
        super("Kon speler '" + playerName + "' niet laden: Onjuiste gegevens");
    }

    /**
     * Fout die optreedt als de controlegetallen niet overeenkomen
     *
     * @param playerName De naam van de speler
     * @param checkSum   Het controlegetal
     */
    public InvalidPlayerException(String playerName, double checkSum)
    {
        super("Kon speler '" + playerName + "' niet laden: Onjuist controlegetal " + checkSum);
    }
}

package me.nlt.bavm.teams.exceptions;

public class FactoryException extends Exception
{
    /**
     * Fout die kan optreden tijdens het maken van een manageable
     *
     * @param type         Type manageable
     * @param id           ID van manageable
     * @param errorMessage het bericht
     */
    public FactoryException(String type, int id, String errorMessage)
    {
        super("Tijdens het maken van een '" + type + "' " + (id == -1 ? " met ID '" + id + "' " : " ") + "is de volgende fout op getreden: " + errorMessage);
    }
}

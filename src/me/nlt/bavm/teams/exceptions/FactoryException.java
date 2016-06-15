package me.nlt.bavm.teams.exceptions;

public class FactoryException extends Exception
{
    public FactoryException(String type, int id, String errorMessage)
    {
        super("Tijdens het maken van een '" + type + "' " + (id == -1 ? " met ID '" + id + "' " : " ") + "is de volgende fout op getreden: " + errorMessage);
    }
}

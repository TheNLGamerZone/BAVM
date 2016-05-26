package me.nlt.bavm.teams.exceptions;

public class InvalidPlayerException extends Exception {
	public InvalidPlayerException(String playerName)
	{
		super("Failed to load player: " + playerName);
	}
}

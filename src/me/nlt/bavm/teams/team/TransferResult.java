package me.nlt.bavm.teams.team;

public enum TransferResult {
	SUCCESS("De transfer is succesvol verlopen!"),
	FAILED_NO_MONEY("Je hebt niet genoeg geld om die speler te kopen!"),
	FAILED_NO_MONEY_OTHER("Het andere team heeft niet genoeg geld!"), 
	FAILED_NOT_ENOUGH_PLAYERS("Door deze tranfers zou dit team uit het toernooi worden gezet!"),
    FAILED_NOT_IN_TEAM("Die speler is niet te koop!");
	
	private String message;
	
	private TransferResult(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
}

package me.nlt.bavm.teams.team;

public enum TransferResult {
	SUCCESS("De spelerruil is succesvol verlopen!"),
	FAILED_NO_MONEY("Je hebt niet genoeg geld om die speler te kopen!"),
	FAILED_NO_MONEY_OTHER("Het andere team heeft niet genoeg geld!"), 
	FAILED_NOT_ENOUGH_PLAYERS("Door deze tranfers zou dit team uit het toernooi worden gezet!");
	
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

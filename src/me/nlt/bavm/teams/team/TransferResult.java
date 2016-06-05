package me.nlt.bavm.teams.team;

public enum TransferResult {
	SUCCESS("Gelukt: De transfer is succesvol verlopen!"),
	FAILED_NO_MONEY("Niet gelukt: Je hebt niet genoeg geld om die speler te kopen!"),
	FAILED_NO_MONEY_OTHER("Niet gelukt: Het andere team heeft niet genoeg geld!"),
	FAILED_NOT_ENOUGH_PLAYERS("Niet gelukt: Door deze tranfer zou dit team uit het toernooi worden gezet!"),
    FAILED_NOT_IN_TEAM("Niet gelukt: Die speler is niet te koop!"),
    FAILED_IN_FORMATION("Niet gelukt: De speler moet eerst uit de teamformatie worden gehaald!");
	
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

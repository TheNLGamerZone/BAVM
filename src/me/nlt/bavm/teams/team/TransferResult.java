package me.nlt.bavm.teams.team;

public enum TransferResult {
	SUCCESS("GELUKT: De transfer is succesvol verlopen!\n"),
    SUCCESS_COACH("GELUKT: De coach werkt nu voor jou!\n"),
	FAILED_NO_MONEY("NIET GELUKT: Je hebt niet genoeg geld om die speler te kopen!\n"),
	FAILED_NO_MONEY_OTHER("NIET GELUKT: Het andere team heeft niet genoeg geld!\n"),
	FAILED_NOT_ENOUGH_PLAYERS("NIET GELUKT: Door deze tranfer zou dit team uit het toernooi worden gezet!\n"),
    FAILED_NOT_IN_TEAM("NIET GELUKT: Die speler is niet te koop!\n"),
    FAILED_IN_FORMATION("NIET GELUKT: De speler moet eerst uit de teamformatie worden gehaald!\n"),
    FAILED_NO_MONEY_COACH("NIET GELUKT: Je hebt niet genoeg geld om die coach aan te nemen!\n"),
    FAILED_NOT_AVAILABLE("NIET GELUKT: Die coach is niet beschikbaar!\n");
	
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

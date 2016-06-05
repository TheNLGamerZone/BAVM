package me.nlt.bavm.teams.team;

public class Geld {
    private int monthlyIncomeK;
    private int currentGeldK;

    public Geld (double teamTalent, int money)
    {
        int teamIncomeFactor = (int) (teamTalent * 1000);
        int teamIncomeVariance = (int) (Math.random() * 500);

        monthlyIncomeK = teamIncomeFactor + teamIncomeVariance;
        currentGeldK = money;
    }

    public int getMonthlyIncomeK()
    {
        return this.monthlyIncomeK;
    }

    public int getCurrentGeldK()
    {
        return this.currentGeldK;
    }
    
    public void removeGeld(int amount)
    {
    	this.currentGeldK -= amount;
    }
    
    public void addGeld(int amount)
    {
    	this.currentGeldK += amount;
    }

    @Override
    public String toString()
    {
        return "money!" + currentGeldK;
    }
}

package me.nlt.bavm.teams.team;

public class Geld {
    private int weeklyIncome;
    private int currentGeld;

    public Geld (double teamTalent, int money, boolean generateNew)
    {
        int teamIncomeFactor = (int) ((teamTalent + 1) * 30000);
        int teamIncomeVariance = (int) ((Math.random() + 1) * 10000);

        weeklyIncome = teamIncomeFactor + teamIncomeVariance;
        currentGeld = (weeklyIncome * (int) ((Math.random() + 1) * 15)) + money;
    }

    public int getWeeklyIncome()
    {
        return this.weeklyIncome;
    }

    public int getCurrentGeldK()
    {
        return this.currentGeld;
    }
    
    public void removeGeld(int amount)
    {
    	this.currentGeld -= amount;
    }
    
    public void addGeld(int amount)
    {
    	this.currentGeld += amount;
    }

    @Override
    public String toString()
    {
        return "money!" + currentGeld;
    }
}

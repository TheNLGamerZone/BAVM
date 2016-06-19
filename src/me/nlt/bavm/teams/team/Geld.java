package me.nlt.bavm.teams.team;

public class Geld {
    private int weeklyIncome;
    private int currentGeld;

    public Geld (double teamTalent, int currentGeld, int weeklyIncome, int id)
    {
        int teamIncomeFactor = (int) ((teamTalent + 1) * 30000);
        int teamIncomeVariance = (int) ((Math.random() + 1) * 10000);

        this.weeklyIncome = (weeklyIncome == -1 ? teamIncomeFactor + teamIncomeVariance : weeklyIncome);
        this.currentGeld = (currentGeld == -1 ? (id != 19 ? this.weeklyIncome * (int) ((Math.random() + 1) * 3) : this.weeklyIncome * (int) ((Math.random() + 1) * 15)) : currentGeld);
    }

    public int getWeeklyIncome()
    {
        return this.weeklyIncome;
    }

    public int getCurrentGeld()
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
        return "geld!" + currentGeld + "!" + weeklyIncome;
    }
}

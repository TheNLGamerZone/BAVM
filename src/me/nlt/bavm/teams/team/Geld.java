package me.nlt.bavm.teams.team;

public class Geld {
    private int weeklyIncome;
    private int currentGeld;

    public Geld (double teamTalent, int currentGeld, int weeklyIncome, int id)
    {
        int teamIncomeFactor = (int) ((teamTalent + 1) * 30000);
        int teamIncomeVariance = (int) ((Math.random() + 1) * 10000);

        if (weeklyIncome == -1)
        {
            this.weeklyIncome = teamIncomeFactor + teamIncomeVariance;
        } else {
            this.weeklyIncome = weeklyIncome;
        }

        if (currentGeld == -1)
        {
            if (id != 19)
            {
                this.currentGeld = this.weeklyIncome * (int) ((Math.random() + 1) * 3);
            } else {
                this.currentGeld = this.weeklyIncome * (int) ((Math.random() + 1) * 15);
            }
        } else {
            this.currentGeld = currentGeld;
        }
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

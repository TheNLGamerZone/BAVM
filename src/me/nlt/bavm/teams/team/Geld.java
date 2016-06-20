package me.nlt.bavm.teams.team;

public class Geld
{
    private int weeklyIncome;
    private int currentGeld;

    /**
     * Het geld object heeft de wekelijkse inkomsten en huidige geldreserve in zich
     *
     * @param teamTalent   Talent van het team
     * @param currentGeld  Huidige hoeveelheid geld
     * @param weeklyIncome Wekelijkse inkomen
     * @param id           ID van het team
     */
    public Geld(double teamTalent, int currentGeld, int weeklyIncome, int id)
    {
        int teamIncomeFactor = (int) ((teamTalent + 1) * 30000);
        int teamIncomeVariance = (int) ((Math.random() + 1) * 10000);

        this.weeklyIncome = (weeklyIncome == -1 ? teamIncomeFactor + teamIncomeVariance : weeklyIncome);
        this.currentGeld = (currentGeld == -1 ? (id != 19 ? this.weeklyIncome * (int) ((Math.random() + 1) * 3) : this.weeklyIncome * (int) ((Math.random() + 1) * 15)) : currentGeld);
    }

    /**
     * Returnt het wekelijkse inkomen van het team
     *
     * @return Wekelijkse inkomen
     */
    public int getWeeklyIncome()
    {
        return this.weeklyIncome;
    }

    /**
     * Returnt de huidige hoeveelheid geld
     *
     * @return
     */
    public int getCurrentGeld()
    {
        return this.currentGeld;
    }

    /**
     * Methode om geld weg te halen
     *
     * @param amount Geld om weg te halen
     */
    public void removeGeld(int amount)
    {
        this.currentGeld -= amount;
    }

    /**
     * Methode om geld toe te voegen
     *
     * @param amount Geld om toe te voegen
     */
    public void addGeld(int amount)
    {
        this.currentGeld += amount;
    }

    @Override
    /**
     * Maakt en antwoord een string met alle data voor geld
     *
     * @return De string met alle data
     */
    public String toString()
    {
        return "geld!" + currentGeld + "!" + weeklyIncome;
    }
}

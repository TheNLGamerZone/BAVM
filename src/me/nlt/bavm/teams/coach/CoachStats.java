package me.nlt.bavm.teams.coach;

import me.nlt.bavm.BAVM;

import java.util.HashMap;

public class CoachStats
{
    private HashMap<CStat, Double> coachSkills = new HashMap<>();
    private int coachID;

    public enum CStat
    {
        MOTIVATIE(0), INZICHT(1), TEAMBUILDING(2);
        private int index;

        /**
         * Stat constructor
         *
         * @param index Index
         */
        private CStat(int index)
        {
            this.index = index;
        }

        /**
         * Stuurt een int terug die staat voor de standaard plek in arrays voor deze skill
         *
         * @return Standaard plek voor deze skill
         */
        public int getIndex()
        {
            return this.index;
        }
    }

    /**
     * PlayerStats constructor
     *
     * @param skillValues SkillValues
     */
    public CoachStats(int coachID, double[] skillValues)
    {
        this.coachID = coachID;

        // Skills in de array zetten
        for (CStat stat : CStat.values())
        {
            // Skills in hashmap zetten
            coachSkills.put(stat, skillValues[stat.getIndex()]);
        }
    }

    /**
     * Geeft de waarde van de skill op de plek van de gegeven locatie
     *
     * @param index De index/plek van de skill
     * @return De waarde van de skill
     */
    public double getValue(int index)
    {
        // Checken of de gegeven waarde bestaat in de hashmap
        if (index < 0 || index > coachSkills.size())
        {
            return -1;
        }


        for (CStat stat : CStat.values())
        {
            if (stat.getIndex() == index)
            {
                return coachSkills.get(stat);
            }
        }

        return 0.0;
    }

    public double getValue(CStat stat)
    {
        for (CStat skill : CStat.values())
        {
            if (stat == skill)
            {
                return coachSkills.get(stat);
            }
        }

        return 0.0;
    }

    /**
     * Hiermee kan een stat verhoogd of verlaagd worden
     *
     * @param stat      De stat die verhoogd/verlaagd moet worden
     * @param increment De verhoging/verlaging
     */
    public void increaseSkill(CStat stat, double increment)
    {
        coachSkills.put(stat, coachSkills.get(stat) + increment);
        BAVM.getCoachManager().getCoach(coachID).unsavedChanges = true;
    }

    /**
     * Zoekt de stat bij de string
     *
     * @param skillName Mogelijke naam voor een stat
     * @return De stat met die naam
     */
    public static CStat getSkill(String skillName)
    {
        // Door alle stats loopen en kijken of er een naam overeenkomt
        for (CStat stat : CStat.values())
        {
            if (stat.name().equals(skillName))
            {
                return stat;
            }
        }

        // Zo niet returnen we 'null'
        return null;
    }

    public double getTotalSkill()
    {
        double skill = 0;

        for (CStat cStat : CStat.values())
        {
            skill += getValue(cStat);
        }

        return skill;
    }

    /**
     * Maakt en antwoord een string waar alle data voor playerStats in staat
     *
     * @return
     */
    @Override
    public String toString()
    {
        // Stringbuilder maken
        StringBuilder stringBuilder = new StringBuilder();
        String statString;

        // String maken met stats
        for (CStat stat : CStat.values())
        {
            stringBuilder.append(stat.name().toLowerCase() + ":" + coachSkills.get(stat) + ">");
        }

        // Laatste komma weghalen
        stringBuilder.setLength(stringBuilder.length() - 1);

        // String maken van stringbuilder
        statString = stringBuilder.toString();

        return "CoachStats{" +
                statString +
                "}";
    }
}

package me.nlt.bavm.teams;

import java.util.HashMap;

public class PlayerStats
{
    private HashMap<Stat, Integer> playerSkills = new HashMap<>();
    public enum Stat
    {
        AFMAKEN(0), AANVAL(1), BALBEZIT(2), VERDEDIGEN(3), CONDITIE(4), GELUK(5), DOELMAN(6);
        private int location;

        private Stat(int location)
        {
            this.location = location;
        }

        public int getLocation()
        {
            return this.location;
        }
    }

    public PlayerStats(int[] skillValues)
    {
        // Skills in de array zetten
        for (Stat stat : Stat.values())
        {
            // Skills in hashmap zetten
            playerSkills.put(stat, skillValues[stat.getLocation()]);
        }
    }

    public static Stat getSkill(String skillName)
    {
        // Door alle stats loopen en kijken of er een naam overeenkomt
        for (Stat stat : Stat.values())
        {
            if (stat.name().equals(skillName))
            {
                return stat;
            }
        }

        // Zo niet returnen we 'null'
        return null;
    }

    @Override
    public String toString()
    {
        // Stringbuilder maken
        StringBuilder stringBuilder = new StringBuilder();
        String statString;

        // String maken met stats
        for (Stat stat : Stat.values())
        {
            stringBuilder.append(stat.name().toLowerCase() + ":" + playerSkills.get(stat) + ">");
        }

        // Laatste komma weghalen
        stringBuilder.setLength(stringBuilder.length() - 1);

        // String maken van stringbuilder
        statString = stringBuilder.toString();

        return "PlayerStats{" +
                statString +
                "}";
    }
}

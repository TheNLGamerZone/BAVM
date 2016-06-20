package me.nlt.bavm.teams.player;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.Market;

import java.util.HashMap;

public class PlayerStats
{
    private HashMap<Stat, Double> playerSkills = new HashMap<>();
    private double checkSum;
    private double totalSkill;

    private int playerID;

    public enum Stat
    {
        AFMAKEN(0), AANVAL(1), BALBEZIT(2), VERDEDIGEN(3), DOELMAN(4), CONDITIE(5);
        private int location;

        /**
         * Stat constructor
         *
         * @param location Location
         */
        private Stat(int location)
        {
            this.location = location;
        }

        /**
         * Stuurt een int terug die staat voor de standaard plek in arrays voor deze skill
         *
         * @return Standaard plek voor deze skill
         */
        public int getLocation()
        {
            return this.location;
        }
    }

    /**
     * PlayerStats constructor
     *
     * @param skillValues SkillValues
     */
    public PlayerStats(int playerID, double[] skillValues)
    {
        // Skills in de array zetten
        for (Stat stat : Stat.values())
        {
            // Skills in hashmap zetten
            playerSkills.put(stat, skillValues[stat.getLocation()]);
        }

        // Checksum maken
        this.checkSum = 0;
        this.totalSkill = 0;

        // Checksum 'vullen'
        for (int i = 0; i < playerSkills.size(); i++)
        {
            //DEBUG
            //System.out.println(this.getValue(i));
            checkSum += (this.getValue(i) * (i * 2) + this.getValue(i));
            totalSkill += this.getValue(i);
        }

        this.playerID = playerID;
    }

    /**
     * Geeft de waarde van de skill op de plek van de gegeven locatie
     *
     * @param location De locatie van de skill
     * @return De waarde van de skill
     */
    public double getValue(int location)
    {
        // Checken of de gegeven waarde bestaat in de hashmap
        if (location < 0 || location > playerSkills.size())
        {
            return -1;
        }

        for (Stat stat : Stat.values())
        {
            if (stat.getLocation() == location)
            {
                return playerSkills.get(stat);
            }
        }

        return 0.0;
    }

    /**
     * Geeft de waarde van de skill
     *
     * @param stat De stat
     * @return De waarde van de skill
     */
    public double getValue(Stat stat)
    {
        for (Stat skill : Stat.values())
        {
            if (stat == skill)
            {
                return playerSkills.get(stat);
            }
        }

        return 0.0;
    }

    /**
     * Geeft de waarde van de checksum
     *
     * @return Checksum
     */
    public double getCheckSum()
    {
        return this.checkSum;
    }

    /**
     * Hiermee kan een stat verhoogd of verlaagd worden
     *
     * @param stat      De stat die verhoogd/verlaagd moet worden
     * @param increment De verhoging/verlaging
     */
    public void increaseSkill(Stat stat, double increment)
    {
        playerSkills.put(stat, playerSkills.get(stat) + increment);

        Market.statsChanged = true;
        BAVM.getPlayerManager().getPlayer(playerID).unsavedChanges = true;

        this.checkSum = 0;
        this.totalSkill = 0;

        for (int i = 0; i < playerSkills.size(); i++)
        {
            checkSum += (this.getValue(i) * (i * 2) + this.getValue(i));
            totalSkill += this.getValue(i);
        }
    }


    /**
     * Zoekt de stat bij de string
     *
     * @param skillName Mogelijke naam voor een stat
     * @return De stat met die naam
     */
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

    /**
     * Returnt de skill op een bepaalde index
     *
     * @param index De index
     * @return De skill
     */
    public static Stat getSkill(int index)
    {
        for (Stat stat : Stat.values())
        {
            if (stat.getLocation() == index)
            {
                return stat;
            }
        }

        return null;
    }

    /**
     * Returnt de totale skill van de speler
     *
     * @return De totale skill van de speler
     */
    public double getTotalSkill()
    {
        return this.totalSkill;
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
                "%" +
                this.checkSum +
                "}";
    }
}

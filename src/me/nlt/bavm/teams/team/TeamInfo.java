package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamInfo
{
    private ArrayList<Player> teamPlayerList = new ArrayList<>();
    private Coach teamCoach;
    private HashMap<StatCoefficient, Double> statCoefficients = new HashMap<>();

    public TeamInfo(int[] playerIDs, int coachID)
    {
        for (int i : playerIDs)
        {
            teamPlayerList.add(BAVM.getPlayerManager().getPlayer(i));
        }

        //DEBUG
        //System.out.println(coachID);
        this.teamCoach = BAVM.getCoachManager().getCoach(coachID);
    }

    public enum StatCoefficient
    {
        AFMCOEF(0), ATTCOEF(1), POSCOEF(2), DEFCOEF(3), KEP(4), CNDCOEF(5);
        private int index;

        /**
         * Stat constructor
         * @param index Index
         */
        private StatCoefficient(int index)
        {
            this.index = index;
        }

        /**
         * Stuurt een int terug die staat voor de standaard plek in arrays voor deze skill
         * @return Standaard plek voor deze skill
         */
        public int getIndex()
        {
            return this.index;
        }
    }

    public double getValue(int index)
    {
        // Checken of de gegeven waarde bestaat in de hashmap
        if (index < 0 || index > statCoefficients.size())
        {
            return -1;
        }


        return (double) statCoefficients.values().toArray()[index];
    }

    public HashMap<StatCoefficient, Double> getStatCoefficients()
    {
        int afm = 0;
        int att = 0;
        int pos = 0;
        int def = 0;
        int kep = 0;
        int cnd = 0;

        for (StatCoefficient statCoefficient : StatCoefficient.values()) {
            statCoefficients.put(statCoefficient, 0.0);
        }



        for (Player player : teamPlayerList)
        {
            switch (player.getPosition().getId())
            {
                case 0 :
                    //relevant keeper stats

                    statCoefficients.put(StatCoefficient.KEP, (statCoefficients.get(StatCoefficient.KEP) + player.getPlayerStats().getValue(4)));
                    kep++;
                    statCoefficients.put(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    break;
                case 1 :
                    //relevant defender stats

                    statCoefficients.put(StatCoefficient.DEFCOEF, (statCoefficients.get(StatCoefficient.DEFCOEF) + player.getPlayerStats().getValue(3)));
                    def++;
                    statCoefficients.put(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    statCoefficients.put(StatCoefficient.POSCOEF, (statCoefficients.get(StatCoefficient.POSCOEF) + player.getPlayerStats().getValue(2)));
                    pos++;
                    break;
                case 2 :
                    //relevant midfielder stats
                    statCoefficients.put(StatCoefficient.ATTCOEF, (statCoefficients.get(StatCoefficient.ATTCOEF) + player.getPlayerStats().getValue(1)));
                    att++;
                    statCoefficients.put(StatCoefficient.DEFCOEF, (statCoefficients.get(StatCoefficient.DEFCOEF) + player.getPlayerStats().getValue(3)));
                    def++;
                    statCoefficients.put(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    statCoefficients.put(StatCoefficient.POSCOEF, (statCoefficients.get(StatCoefficient.POSCOEF) + player.getPlayerStats().getValue(2)));
                    pos++;
                    break;
                case 3 :
                    //relevant attacker stats
                    statCoefficients.put(StatCoefficient.ATTCOEF, (statCoefficients.get(StatCoefficient.ATTCOEF) + player.getPlayerStats().getValue(1)));
                    att++;
                    statCoefficients.put(StatCoefficient.AFMCOEF, (statCoefficients.get(StatCoefficient.AFMCOEF) + player.getPlayerStats().getValue(0)));
                    afm++;
                    statCoefficients.put(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    break;
            }
        }

        statCoefficients.put(StatCoefficient.AFMCOEF, (0.75 * statCoefficients.get(StatCoefficient.AFMCOEF) / afm) + (teamCoach.getCoachStats().getValue(0) * 0.25));
        statCoefficients.put(StatCoefficient.ATTCOEF, (0.75 * statCoefficients.get(StatCoefficient.ATTCOEF) / att) + (teamCoach.getCoachStats().getValue(1) * 0.25));
        statCoefficients.put(StatCoefficient.POSCOEF, (0.75 * statCoefficients.get(StatCoefficient.POSCOEF) / pos) + (teamCoach.getCoachStats().getValue(2) * 0.25));
        statCoefficients.put(StatCoefficient.DEFCOEF, (0.75 * statCoefficients.get(StatCoefficient.DEFCOEF) / def) + (teamCoach.getCoachStats().getValue(1) * 0.25));
        statCoefficients.put(StatCoefficient.KEP, (0.75 * statCoefficients.get(StatCoefficient.KEP) / kep) + (teamCoach.getCoachStats().getValue(0) * 0.25));
        statCoefficients.put(StatCoefficient.CNDCOEF, (0.75 * statCoefficients.get(StatCoefficient.CNDCOEF) / cnd) + (teamCoach.getCoachStats().getValue(0) * 0.25));

        return statCoefficients;
    }

    public double getCoefficientValue(int index) {
        // Checken of de gegeven waarde bestaat in de hashmap
        if (index < 0 || index > statCoefficients.size())
        {
            return -1;
        }

        switch (index) {
            case 0 :
                return statCoefficients.get(StatCoefficient.AFMCOEF);
            case 1 :
                return statCoefficients.get(StatCoefficient.ATTCOEF);
            case 2 :
                return statCoefficients.get(StatCoefficient.POSCOEF);
            case 3 :
                return statCoefficients.get(StatCoefficient.DEFCOEF);
            case 4 :
                return statCoefficients.get(StatCoefficient.KEP);
            case 5 :
                return statCoefficients.get(StatCoefficient.CNDCOEF);
        }

        return 0.0;
    }

    public ArrayList<Player> getPlayers()
    {
        return this.teamPlayerList;
    }

    public Coach getTeamCoach()
    {
        return this.teamCoach;
    }

    @Override
    public String toString()
    {
        // Stringbuilder maken
        StringBuilder stringBuilder = new StringBuilder();
        String infoString;

        // String maken met stats
        for (Player player : teamPlayerList)
        {
            stringBuilder.append(player.toString() + ">");
        }

        // Laatste komma weghalen
        stringBuilder.setLength(stringBuilder.length() - 1);

        // String maken van stringbuilder
        infoString = stringBuilder.toString();

        String playerString = "PlayerIDs{" +
                infoString +
                "}";

        String coachString = teamCoach.toString();

        return playerString + "," + coachString;
    }
}

package me.nlt.bavm.teams.team;

import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamCoefficients {
    private HashMap<StatCoefficient, Double> statCoefficients = new HashMap<>();

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

    public TeamCoefficients(ArrayList<Player> teamPlayerList, Coach teamCoach)
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

                    statCoefficients.replace(StatCoefficient.KEP, (statCoefficients.get(StatCoefficient.KEP) + player.getPlayerStats().getValue(4)));
                    kep++;
                    statCoefficients.replace(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    break;
                case 1 :
                    //relevant defender stats

                    statCoefficients.replace(StatCoefficient.DEFCOEF, (statCoefficients.get(StatCoefficient.DEFCOEF) + player.getPlayerStats().getValue(3)));
                    def++;
                    statCoefficients.replace(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    statCoefficients.replace(StatCoefficient.POSCOEF, (statCoefficients.get(StatCoefficient.POSCOEF) + player.getPlayerStats().getValue(2)));
                    pos++;
                    break;
                case 2 :
                    //relevant midfielder stats
                    statCoefficients.replace(StatCoefficient.ATTCOEF, (statCoefficients.get(StatCoefficient.ATTCOEF) + player.getPlayerStats().getValue(1)));
                    att++;
                    statCoefficients.replace(StatCoefficient.DEFCOEF, (statCoefficients.get(StatCoefficient.DEFCOEF) + player.getPlayerStats().getValue(3)));
                    def++;
                    statCoefficients.replace(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    statCoefficients.replace(StatCoefficient.POSCOEF, (statCoefficients.get(StatCoefficient.POSCOEF) + player.getPlayerStats().getValue(2)));
                    pos++;
                    break;
                case 3 :
                    //relevant midfielder stats
                    statCoefficients.replace(StatCoefficient.ATTCOEF, (statCoefficients.get(StatCoefficient.ATTCOEF) + player.getPlayerStats().getValue(1)));
                    att++;
                    statCoefficients.replace(StatCoefficient.AFMCOEF, (statCoefficients.get(StatCoefficient.AFMCOEF) + player.getPlayerStats().getValue(0)));
                    afm++;
                    statCoefficients.replace(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    statCoefficients.replace(StatCoefficient.POSCOEF, (statCoefficients.get(StatCoefficient.POSCOEF) + player.getPlayerStats().getValue(2)));
                    pos++;
                    break;
            }
        }

        statCoefficients.replace(StatCoefficient.AFMCOEF, (statCoefficients.get(StatCoefficient.AFMCOEF) / afm * ((teamCoach.getCoachStats().getValue(0) / 100) + 1)));
        statCoefficients.replace(StatCoefficient.ATTCOEF, (statCoefficients.get(StatCoefficient.ATTCOEF) / att * ((teamCoach.getCoachStats().getValue(1) / 100) + 1)));
        statCoefficients.replace(StatCoefficient.POSCOEF, (statCoefficients.get(StatCoefficient.POSCOEF) / pos * ((teamCoach.getCoachStats().getValue(2) / 100) + 1)));
        statCoefficients.replace(StatCoefficient.DEFCOEF, (statCoefficients.get(StatCoefficient.DEFCOEF) / def * ((teamCoach.getCoachStats().getValue(1) / 100) + 1)));
        statCoefficients.replace(StatCoefficient.KEP, (statCoefficients.get(StatCoefficient.KEP) / kep * ((teamCoach.getCoachStats().getValue(0) / 100) + 1)));
        statCoefficients.replace(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) / cnd * ((teamCoach.getCoachStats().getValue(0) / 100) + 1)));

    }

    public HashMap<StatCoefficient, Double> getStatCoefficients()
    {
        return statCoefficients;
    }
}

package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.player.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class TeamCoefficients {
    private static HashMap<StatCoefficient, Double> statCoefficients = new HashMap<>();

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

    public TeamCoefficients(ArrayList<Player> teamPlayerList, Coach teamCoach) {
        int afm = 0;
        int att = 0;
        int pos = 0;
        int def = 0;
        int kep = 0;
        int cnd = 0;

        for (Player player : teamPlayerList) {
            switch (player.getPosition().getId())
            {
                case 0 :
                    statCoefficients.put(StatCoefficient.KEP, (statCoefficients.get(StatCoefficient.KEP) + player.getPlayerStats().getValue(5)));
                    kep++;
                    statCoefficients.put(StatCoefficient.AFMCOEF, (statCoefficients.get(StatCoefficient.AFMCOEF) + player.getPlayerStats().getValue(0)));
                    afm++;
                    break;
                case 1 :

                    break;
            }
        }
    }
}

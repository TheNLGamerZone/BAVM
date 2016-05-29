package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.player.Player;

import java.util.HashMap;

public class TeamCoefficients {
    private static HashMap<StatCoefficient, Double> statCoefficients = new HashMap<>();

    public enum StatCoefficient
    {
        AFMCOEF0(0), AFMCOEF1(1), ATTCOEF0(2), ATTCOEF1(3), POSCOEF0(4), POSCOEF1(5),
        DEFCOEF0(6), DEFCOEF1(7), KEP0(8), KEP1(9), CNDCOEF0(10), CNDCOEF1(11);
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

    public TeamCoefficients(int[] playerIDList, int coachID) {
        for (int i : playerIDList) {

        }
    }
}

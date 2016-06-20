package me.nlt.bavm.season;

import me.nlt.bavm.BAVM;

import java.util.ArrayList;

public class Season
{
    private ArrayList<MatchWeek> seasonWeeks = new ArrayList<>();

    /*
     * Season heeft alle geplande wedstrijden in MatchWeeks in zich
     */
    public Season(boolean createSeason) {
        int matchWeeks = 38;

        if (createSeason) {
            for (int i = 0; i < matchWeeks / 2; i++) {
                seasonWeeks.add(new MatchWeek(i, 1));
            }
            for (int i = matchWeeks / 2; i < matchWeeks; i++) {
                seasonWeeks.add(new MatchWeek(i, 2));
            }

            BAVM.getDisplay().appendText("Season aangemaakt!");
        }
    }

    public ArrayList<MatchWeek> getSeasonWeeks() {
        return seasonWeeks;
    }

}

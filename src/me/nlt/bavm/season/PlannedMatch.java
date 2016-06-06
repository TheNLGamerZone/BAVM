package me.nlt.bavm.season;


public class PlannedMatch {
    private String matchName;
    private int[] teamIDs = new int[2];

    public PlannedMatch(String matchName, int seasonHalf) {
        this.matchName = matchName;

        String[] splitName = matchName.split("-");
        if (seasonHalf == 1) {
            teamIDs[0] = Integer.parseInt(splitName[0]);
            teamIDs[1] = Integer.parseInt(splitName[1]);
        } else {
            teamIDs[1] = Integer.parseInt(splitName[0]);
            teamIDs[0] = Integer.parseInt(splitName[1]);
        }

    }

    public String getMatchName() {
        return matchName;
    }

    public int[] getTeamIDs() {
        return teamIDs;
    }
}

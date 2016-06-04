package me.nlt.bavm.league;

import me.nlt.bavm.BAVM;

import java.util.ArrayList;

public class League {
    private ArrayList<PlannedMatch> plannedFirst = new ArrayList<>();
    private ArrayList<PlannedMatch> plannedSecond = new ArrayList<>();

    public League(boolean createLeague) {
        if (createLeague) {
            for (String str : getStandardSequence()) {
                plannedFirst.add(new PlannedMatch(str, 1));
                plannedSecond.add(new PlannedMatch(str, 2));
            }

            BAVM.getDisplay().appendText("League aangemaakt!");
        }
    }

    public ArrayList<String> getStandardSequence() {
        ArrayList<String> matches = new ArrayList<>();

        int array1[] = new int[9];
        int array2[] = new int[array1.length + 1];
        int array3[] = new int[array1.length + array2.length];
        int array1Rev[] = new int[array1.length];
        int adder = 2;
        int shiftAmount = 18;

        for (int i = 0; i < array2.length; i++) {
            if (i == array2.length - 1) {
                array2[i] = adder - 1;
            } else {
                array1[i] = adder;
                array2[i] = adder - 1;
            }
            adder = adder + 2;
        }

        int it = array1.length - 1;

        for (int i : array1) {
            array1Rev[it] = i;
            it--;
        }

        it = 0;

        for (int i : array2) {
            array3[it] = i;
            it++;
        }
        for (int i : array1Rev) {
            array3[it] = i;
            it++;
        }

        for (int i = 0; i < (array3.length / 2) + 1; i++) {
            if (i == 0) {
                matches.add(0 + "-" + array3[i]);
            } else {
                matches.add(array3[array3.length - i] + "-" + array3[i]);
            }
        }

        for (int i = 0; i < shiftAmount; i++) {
            int lastE = array3[array3.length - 1];
            System.arraycopy(array3, 0, array3, 1, array3.length - 1);
            array3[0] = lastE;

            for (int j = 0; j < (array3.length / 2) + 1; j++) {
                if (j == 0) {
                    matches.add(0 + "-" + array3[j]);
                } else {
                    matches.add(array3[array3.length - j] + "-" + array3[j]);
                }
            }

        }

        //DEBUG
        /*for (String str : matches) {
            BAVM.getDisplay().appendText(str);
        }*/

        return matches;
    }

    public ArrayList<PlannedMatch> getPlannedFirst() {
        return plannedFirst;
    }

    public ArrayList<PlannedMatch> getPlannedSecond() {
        return plannedSecond;
    }
}

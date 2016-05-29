package me.nlt.bavm.generator;

import java.util.Random;

public class RandomNames {
    public static String getTeamName() {
        //in deze method wordt een willekeurige teamnaam gemaakt

        Random randomNumber = new Random();

        String teamName;
        String prefixes[] = {"", "FC ", "FC ", "FC ", "SV ", "SV "};

       //er wordt steeds een willekeurig woord samengevoegd met een prefix zoals 'FC'
        teamName = prefixes[randomNumber.nextInt(6)] + getRandomWord();

        return teamName;
    }

    public static String getPeopleName() {
        //deze methode voegt twee willekeurige woorden samen als een voor- en achternaam
        
    	return getRandomWord() + " " + getRandomWord();
    }

    public static String getRandomWord() {
        //deze methode maakt willekeurige woorden aan, altijd in de volgorde (klinker-)medeklinkder-klinker-medeklinker etc.

        //eerst de waardes van de klinkers en medeklinkers geven
        String vowelsLowerCase = "aeiouy";
        String vowelsUpperCase = "AEIOUY";
        String consonantsLowerCase = "bcdfghjklmnpqrstvwxyz";
        String consonantsUpperCase = "BCDFGHJKLMNPQRSTVWXYZ";

        Random randomNumber = new Random();

        //de woordlengte wordt aangemaakt, +3 zodat elk woord minimaal 3 letters lang is
        int wordLength = randomNumber.nextInt(4) + 3;
        String word = "";

        //willekeurig 1 of 0 om te kijken of het woord met een medeklinker ja of nee begint
        int consonantStart = randomNumber.nextInt(2);
        boolean isConsonant = (consonantStart != 0);
        //de 'charAt' method van String geeft een char op de gespecificeerde index van de gegeven String, in dit geval is de index random zodat ook de letter ranodm is
        word = (isConsonant) ? word + consonantsUpperCase.charAt(randomNumber.nextInt(21)) : word + vowelsUpperCase.charAt(randomNumber.nextInt(6));

        //eerst wordt gekeken of het wordt met een medeklinker begint om te kijken wat de volgende letter moet zijn, en vervolgens wordt steeds door 2 gedeeld om afwisselend medeklinkers of klinkers toe te voegen
        for (int i = (isConsonant) ? 1 : 2; i <= wordLength; i++) {
            word = (i % 2 == 0) ? word + consonantsLowerCase.charAt(randomNumber.nextInt(21)) : word + vowelsLowerCase.charAt(randomNumber.nextInt(6));
        }

        return word;
    }
}


package me.nlt.bavm.teams.team;

import me.nlt.bavm.BAVM;
import me.nlt.bavm.teams.coach.Coach;
import me.nlt.bavm.teams.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamInfo
{
    private Team team;
    private ArrayList<Player> teamPlayerList = new ArrayList<>();
    private Coach teamCoach;
    private Geld teamGeld;
    private double teamTalent;
    private PlayerPlacement playerPlacement;
    private HashMap<Score, Integer> teamScores = new HashMap<>();

    private HashMap<StatCoefficient, Double> statCoefficients = new HashMap<>();

    public TeamInfo(Team team, int[] playerIDs, int coachID, double teamTalent, int currentGeld, int weeklyIncome, String placement, String scores)
    {
        this.team = team;
        for (int i : playerIDs)
        {
            teamPlayerList.add(BAVM.getPlayerManager().getPlayer(i));
        }

        //DEBUG
        //System.out.println(coachID);
        this.teamCoach = BAVM.getCoachManager().getCoach(coachID);
        this.teamGeld = new Geld(teamTalent, currentGeld, weeklyIncome);
        this.teamTalent = teamTalent;

        this.createPlacement(placement);
        this.createScores(scores);
    }
    
    private void createScores(String scores)
    {
    	if (scores == null)
    	{
    		for (Score score : Score.values())
            {
                teamScores.put(score, 0);
            }
    	} else
    	{
    		for (String data : scores.split("%"))
    		{
    			for (Score score : Score.values())
    			{
    				if (score.name().equals(data.split(">")[0]))
    				{
    					teamScores.put(score, Integer.parseInt(data.split(">")[1]));
    				}
    			}
    		}
    	}
    }
    
    private void createPlacement(String placement)
    {
        if (placement.equals("null") || placement.equals(""))
        {
            return;
        }

        Player keeper = null;
        ArrayList<Player> attackers = new ArrayList<>();
        ArrayList<Player> defenders = new ArrayList<>();
        ArrayList<Player> midfielders = new ArrayList<>();

        String[] positions = placement.split("@");

        for (String position : positions)
        {
            String[] ids;

            switch (position.split("!")[0])
            {
                case "keeper":
                    keeper = BAVM.getPlayerManager().getPlayer(Integer.parseInt(position.split("!")[1]));
                    break;
                case "attackers":
                    ids = position.split("!")[1].split("#");

                    for (String id : ids)
                    {
                        attackers.add(BAVM.getPlayerManager().getPlayer(Integer.parseInt(id)));
                    }
                    break;
                case "defenders":
                    ids = position.split("!")[1].split("#");

                    for (String id : ids)
                    {
                        defenders.add(BAVM.getPlayerManager().getPlayer(Integer.parseInt(id)));
                    }
                    break;
                case "midfielders":
                    ids = position.split("!")[1].split("#");

                    for (String id : ids)
                    {
                        midfielders.add(BAVM.getPlayerManager().getPlayer(Integer.parseInt(id)));
                    }
                    break;
            }
        }

        this.playerPlacement = new PlayerPlacement(keeper, defenders, attackers, midfielders);
    }

    public enum Score {
        POINTS(0), WINS(1), DRAWS(2), LOSSES(3), GOALSFOR(4), GOALSAGAINST(5);
        private int index;

        private Score(int location)
        {
            this.index = index;
        }

        public int getIndex()
        {
            return this.index;
        }
    }

    public void increaseTeamScores(Score score, int increment)
    {
        teamScores.replace(score, teamScores.get(score) + increment);
    }

    public enum StatCoefficient
    {
        AFMCOEF(0), ATTCOEF(1), POSCOEF(2), DEFCOEF(3), KEP(4), CNDCOEF(5);
        private int index;

        /**
         * Stat constructor
         *
         * @param index Index
         */
        private StatCoefficient(int index)
        {
            this.index = index;
        }

        /**
         * Stuurt een int terug die staat voor de standaard plek in arrays voor deze skill
         *
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

        for (StatCoefficient statCoefficient : StatCoefficient.values())
        {
            statCoefficients.put(statCoefficient, 0.0);
        }


        for (Player player : teamPlayerList)
        {
            if (player == null)
            {
                System.out.println("Null player");
                continue;
            }

            if (!playerPlacement.isPlaced(player))
            {
                continue;
            }

            switch (player.getPosition().getId())
            {
                case 0:
                    //relevant keeper stats

                    statCoefficients.put(StatCoefficient.KEP, (statCoefficients.get(StatCoefficient.KEP) + player.getPlayerStats().getValue(4)));
                    kep++;
                    statCoefficients.put(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    break;
                case 1:
                    //relevant defender stats

                    statCoefficients.put(StatCoefficient.DEFCOEF, (statCoefficients.get(StatCoefficient.DEFCOEF) + player.getPlayerStats().getValue(3)));
                    def++;
                    statCoefficients.put(StatCoefficient.CNDCOEF, (statCoefficients.get(StatCoefficient.CNDCOEF) + player.getPlayerStats().getValue(5)));
                    cnd++;
                    statCoefficients.put(StatCoefficient.POSCOEF, (statCoefficients.get(StatCoefficient.POSCOEF) + player.getPlayerStats().getValue(2)));
                    pos++;
                    break;
                case 2:
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
                case 3:
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

    public double getCoefficientValue(int index)
    {
        // Checken of de gegeven waarde bestaat in de hashmap
        if (index < 0 || index > statCoefficients.size())
        {
            return -1;
        }

        switch (index)
        {
            case 0:
                return statCoefficients.get(StatCoefficient.AFMCOEF);
            case 1:
                return statCoefficients.get(StatCoefficient.ATTCOEF);
            case 2:
                return statCoefficients.get(StatCoefficient.POSCOEF);
            case 3:
                return statCoefficients.get(StatCoefficient.DEFCOEF);
            case 4:
                return statCoefficients.get(StatCoefficient.KEP);
            case 5:
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

    public Geld getTeamGeld()
    {
        // Quick 'n dirty
        team.unsavedChanges = true;
        return this.teamGeld;
    }

    public double getTeamTalent()
    {
        return this.teamTalent;
    }

    public PlayerPlacement getPlayerPlacement()
    {
        return this.playerPlacement;
    }

    public HashMap<Score, Integer> getTeamScores()
    {
        return teamScores;
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
            if (player == null)
            {
                continue;
            }

            stringBuilder.append(player.getPlayerID() + ">");
        }

        // Laatste komma weghalen
        stringBuilder.setLength(stringBuilder.length() - 1);
        infoString = stringBuilder.toString();
        stringBuilder.setLength(0);
        
        for (Score score : Score.values())
        {
        	stringBuilder.append(score.name() + ">" + teamScores.get(score) + "%");
        }
        
        stringBuilder.setLength(stringBuilder.length() - 1);

        return "teamtalent;" + this.teamTalent + 
        		"<players;" + infoString + 
        		"<coach;" + (teamCoach != null ? teamCoach.getCoachID() : "-1") + 
        		"<geld;" + teamGeld.toString() +
        		"<placement;" + (playerPlacement != null ? playerPlacement.toString() : "null") +
        		"<scores;" + stringBuilder.toString();
    }
}

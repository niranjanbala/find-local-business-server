package biz.finder.ipl;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Predictor {
	public static final int WINNING_TEAM_RUNS=170;
	public static final float OVERS=20.0F;
	public static final int MARGIN=20;
	public static Map<Team, Double> analysis(Map<Team, PointTable> pointTableMap,
			Map<Integer, Team[]> matches) throws FileNotFoundException {
		int matchesRemaining = matches.size();
		Map<Team, Double> qualify = canQualify(pointTableMap, matches,
				matchesRemaining);
		return qualify;
	}

	private static Map<Team, Double> canQualify(
			Map<Team, PointTable> pointTableMap, Map<Integer, Team[]> matches,
			int matchesRemaining) {
		Map<Team, Double> qualify = new HashMap<Team, Double>();
		for (Team t : Team.values()) {
			qualify.put(t, Double.valueOf(0.0D));
		}
		double TOTAL_POSSIBILITIES = Math.pow(2.0D, matchesRemaining);	
		for (int i = 0; i < TOTAL_POSSIBILITIES; i++) {
			String binary= padLeft(Integer.toBinaryString(i), matchesRemaining);
			Map<Team, Integer> rankings = new HashMap<Team, Integer>();
			Map<Team, PointTable> results = new HashMap<Team, PointTable>();
			for (Team t : Team.values()) {
				results.put(t, PointTable.fromPointTable(pointTableMap.get(t)));
				rankings.put(t, Integer.valueOf(1));
			}

			for (int j = 0; j < matchesRemaining; j++) {
				Team[] teams = matches.get(j);
				Team winner = teams[Integer.valueOf(String.valueOf(binary
						.charAt(j)))];
				Team loser=teams[0].equals(winner)?teams[1]:teams[0];
				results.get(winner).updateMatchResult(2, WINNING_TEAM_RUNS, OVERS, WINNING_TEAM_RUNS-MARGIN, OVERS);
				results.get(loser).updateMatchResult(0, WINNING_TEAM_RUNS-MARGIN, OVERS, WINNING_TEAM_RUNS, OVERS);							
			}

			for (Team t : Team.values()) {
				for (Team t1 : Team.values()) {
					if(t1.equals(t)) continue;
					if(results.get(t).compareTo(results.get(t1))>0) {
						rankings.put(t, rankings.get(t) + 1);
					}
				}
			}
			for (Team t : Team.values()) {
				if (rankings.get(t) > 5) {
					qualify.put(t, qualify.get(t) + 1);
				}
			}
		}
		for (Team t : Team.values()) {
			qualify.put(t, Double.valueOf(Double.valueOf(
					((Double) qualify.get(t)).doubleValue()).doubleValue()
					/ TOTAL_POSSIBILITIES * 100.0D));
		}
		return qualify;
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", new Object[] { s }).replaceAll(
				" ", "0");
	}
}
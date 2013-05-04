package biz.finder.ipl;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Predictor {
	public static Map<Team, Double> analysis(Map<Team, Integer> pointTableMap,
			Map<Integer, Team[]> matches) throws FileNotFoundException {
		int matchesRemaining = matches.size();
		Map<Team, Double> qualify = canQualify(pointTableMap, matches,
				matchesRemaining);
		return qualify;
	}

	private static Map<Team, Double> canQualify(
			Map<Team, Integer> pointTableMap, Map<Integer, Team[]> matches,
			int matchesRemaining) {
		Map<Team, Double> qualify = new HashMap<Team, Double>();
		for (Team t : Team.values()) {
			qualify.put(t, Double.valueOf(0.0D));
		}
		double TOTAL_POSSIBILITIES = Math.pow(2.0D, matchesRemaining);
		String binary;
		Map<Team, Integer> results = new HashMap<Team, Integer>();
		for (int i = 0; i <= TOTAL_POSSIBILITIES; i++) {
			binary = padLeft(Integer.toBinaryString(i), matchesRemaining);
			Map<Team, Integer> rankings = new HashMap<Team, Integer>();
			for (Team t : Team.values()) {
				results.put(t, (Integer) pointTableMap.get(t));
				rankings.put(t, Integer.valueOf(1));
			}

			for (int j = 0; j < matchesRemaining; j++) {
				Team[] teams = matches.get(j);
				Team winner = teams[Integer.valueOf(String.valueOf(binary
						.charAt(j)))];
				results.put(winner, results.get(winner) + 2);
			}

			for (Team t : Team.values()) {
				for (Team t1 : Team.values()) {
					if (t.equals(t1))
						continue;
					if (results.get(t1) < results.get(t)) {
						rankings.put(t, rankings.get(t) + 1);
					}
				}
			}
			for (Team t : Team.values()) {
				if (rankings.get(t) >= 5) {
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
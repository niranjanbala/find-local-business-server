package biz.finder.ipl;

import java.io.*;
import java.util.*;

public class Predictor {
	public static void main(String[] args) throws Exception {
		analysis();
		/*Scanner sc = new Scanner(new File("chances"));
		while(sc.hasNext()) {			
			String line[]=sc.nextLine().split(" - ");
			Team team = Team.valueOf(line[0].trim());
			
			double TOTAL_POSSIBILITIES = Math.pow(2, 28);
			System.out.println(team +"="+Math.round(Double.valueOf(line[1])/TOTAL_POSSIBILITIES*100));
		}*/
	}

	private static void analysis() throws FileNotFoundException {
		Map<Team, PointTable> pointTableMap = extractPointTableMap();
		Map<Integer, Team[]> matches = extractFixtures();
		int matchesRemaining = matches.size();
		Map<Team, Integer> qualify = canQualify(pointTableMap, matches,
				matchesRemaining);	
		printResults(qualify);
		System.out.println("Analysis Complete");
	}

	private static Map<Team, Integer> canQualify(
			Map<Team, PointTable> pointTableMap, Map<Integer, Team[]> matches,
			int matchesRemaining) {
		Map<Team, Integer> qualify = new HashMap<Team, Integer>();
		for (Team t : Team.values()) {			
			qualify.put(t, 0);
		}
		double TOTAL_POSSIBILITIES = Math.pow(2, matchesRemaining);
		for (int i = 0; i <= TOTAL_POSSIBILITIES; i++) {
			String binary = padLeft(Integer.toBinaryString(i), matchesRemaining);
			Map<Team, Integer> results = new HashMap<Team, Integer>();
			Map<Team, Integer> rankings = new HashMap<Team, Integer>();
			for (Team t : Team.values()) {
				results.put(t, pointTableMap.get(t).getPoints());
				rankings.put(t, 1);
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
					if(results.get(t1)<results.get(t)) {
						rankings.put(t, rankings.get(t) + 1);
					}
				}
			}
			for (Team t : Team.values()) {
				if(rankings.get(t)>=5) {
					qualify.put(t, qualify.get(t) + 1);
				}
			}
		}		
		for (Team t : Team.values()) {			
				qualify.put(t, (int)Math.round(Double.valueOf(qualify.get(t))/TOTAL_POSSIBILITIES*100));			
		}
		return qualify;
	}

	private static void printResults(Map<Team, Integer> results) {
		for (Team t : Team.values()) {
			System.out.println(t.name() + " - " + results.get(t));
		}
	} 

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s).replaceAll(" ", "0");
	}

	private static Map<Integer, Team[]> extractFixtures()
			throws FileNotFoundException {
		Scanner sc = new Scanner(new File("fixtures"));
		Map<Integer, Team[]> matches = new HashMap<Integer, Team[]>();
		int count = 0;
		while (sc.hasNext()) {
			sc.nextLine();
			sc.nextLine();
			String match[] = sc.nextLine().split(" - ")[1].split(" v ");
			sc.nextLine();
			sc.nextLine();
			try {
				Team team1 = Team.valueOf(match[0].replace(" ", "_"));
				Team team2 = Team.valueOf(match[1].replace(" ", "_"));
				matches.put(count++, new Team[] { team1, team2 });
			} catch (Exception e) {
				//
			}
		}
		return matches;
	}

	private static Map<Team, PointTable> extractPointTableMap()
			throws FileNotFoundException {
		Scanner sc = new Scanner(new File("results"));
		Map<Team, PointTable> pointTableMap = new HashMap<Team, PointTable>();
		while (sc.hasNext()) {
			String[] result = sc.nextLine().split(",");
			Team team = Team.valueOf(result[0].replace(" ", "_"));
			Scanner resultInfo = new Scanner(result[1]);
			int matches = resultInfo.nextInt();
			int won = resultInfo.nextInt();
			int lost = resultInfo.nextInt();
			int tied = resultInfo.nextInt();
			int nr = resultInfo.nextInt();
			int points = resultInfo.nextInt();
			float netrr = resultInfo.nextFloat();
			int forRuns = resultInfo.nextInt();
			float forOver = resultInfo.nextFloat();
			int againstRuns = resultInfo.nextInt();
			float againstOvers = resultInfo.nextFloat();
			pointTableMap.put(team, new PointTable(team.name(),matches, won, lost, tied,
					nr, points, netrr, forRuns, forOver, againstRuns,
					againstOvers));
		}
		return Collections.unmodifiableMap(pointTableMap);
	}
}

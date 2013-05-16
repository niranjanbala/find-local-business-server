
package biz.finder.ipl;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.google.appengine.api.datastore.Entity;

@PersistenceCapable
public class PointTable implements Comparable<PointTable>{

	@Persistent
	private float againstOvers;
	@Persistent
	private String teamName;
	@Persistent
	private int matches;
	@Persistent
	private int won;
	@Persistent
	private int lost;
	@Persistent
	private int tied;
	@Persistent
	private int nr;
	@Persistent
	private int points;
	@Persistent
	private float netrr;
	@Persistent
	private int forRuns;
	@Persistent
	private float forOver;
	@Persistent
	private int againstRuns;

	public PointTable() {

	}

	public static PointTable fromEntity(Entity entity) {
		//TODO: 
		String teamNameIn = String.valueOf(entity.getProperty("teamName"));
		int matchesIn = Integer.valueOf(String.valueOf(entity.getProperty("matches")));
		int wonIn = Integer.valueOf(String.valueOf(entity.getProperty("won")));
		int lostIn = Integer.valueOf(String.valueOf(entity.getProperty("lost")));
		int nrIn = Integer.valueOf(String.valueOf(entity.getProperty("nr")));
		int tiedIn = Integer.valueOf(String.valueOf(entity.getProperty("tied")));
		int pointsIn = Integer.valueOf(String.valueOf(entity.getProperty("points")));
		float netrrIn = Float.valueOf(String.valueOf(entity.getProperty("netrr")));
		float forOverIn = Float.valueOf(String.valueOf(entity.getProperty("forOver")));
		int forRunsIn = Integer.valueOf(String.valueOf(entity.getProperty("forRuns")));
		float againstOversIn = Float.valueOf(String.valueOf(entity
				.getProperty("againstOver")));
		int againstRunsIn = Integer.valueOf(String.valueOf(entity
				.getProperty("againstRuns")));
		return new PointTable(teamNameIn, matchesIn, wonIn, lostIn, tiedIn,
				nrIn, pointsIn, netrrIn, forRunsIn, forOverIn, againstRunsIn,
				againstOversIn);
	}

	public static PointTable fromPointTable(PointTable pointTable) {
		return new PointTable(pointTable.teamName, pointTable.matches,
				pointTable.won, pointTable.lost, pointTable.tied,
				pointTable.nr, pointTable.points, pointTable.netrr,
				pointTable.forRuns, pointTable.forOver, pointTable.againstRuns,
				pointTable.againstOvers);
	}

	public PointTable(String teamName, int matches, int won, int lost,
			int tied, int nr, int points, float netrr, int forRuns,
			float forOver, int againstRuns, float againstOvers) {
		this.teamName = teamName;
		this.matches = matches;
		this.won = won;
		this.lost = lost;
		this.tied = tied;
		this.nr = nr;
		this.points = points;
		this.netrr = netrr;
		this.forRuns = forRuns;
		this.forOver = forOver;
		this.againstRuns = againstRuns;
		this.againstOvers = againstOvers;
	}

	public float getAgainstOvers() {
		return againstOvers;
	}

	public int getMatches() {
		return matches;
	}

	public int getWon() {
		return won;
	}

	public int getLost() {
		return lost;
	}

	public int getTied() {
		return tied;
	}

	public int getNr() {
		return nr;
	}

	public int getPoints() {
		return points;
	}

	public float getNetrr() {
		return netrr;
	}

	public int getForRuns() {
		return forRuns;
	}

	public float getForOver() {
		return forOver;
	}

	public int getAgainstRuns() {
		return againstRuns;
	}

	public String getTeamName() {
		return teamName;
	}

	public static int getNumberOfBalls(Float overs) {
		int numberOfBalls = (int) ((overs - overs.intValue()) * 10);
		return numberOfBalls + overs.intValue() * 6;
	}

	public void computeNrr() {
		float forAvg = Float.valueOf(forRuns) / getNumberOfBalls(forOver) * 6;
		float aAvg = Float.valueOf(againstRuns)
				/ getNumberOfBalls(againstOvers) * 6;
		this.netrr = forAvg - aAvg;
	}

	public void updateMatchResult(int pointIncrement, int runsFor,
			float oversFor, int runsAgainst, float overAgainst) {
		this.matches++;
		if(pointIncrement==0) {
			this.lost++;	
		}else {		
			this.won++;
		}
		this.points += pointIncrement;
		this.forRuns += runsFor;
		this.forOver += oversFor;
		this.againstRuns += runsAgainst;
		this.againstOvers += overAgainst;
		this.computeNrr();
	}

	@Override
	public int compareTo(PointTable o) {
		return new CompareToBuilder().append(points, o.points).append(netrr, o.netrr).toComparison();
	}
}

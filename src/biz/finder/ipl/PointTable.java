package biz.finder.ipl;

import javax.jdo.annotations.*;

@PersistenceCapable
public class PointTable {

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
	public PointTable(){
		
	}
	public PointTable(String teamName,int matches, int won, int lost, int tied, int nr,
			int points, float netrr, int forRuns, float forOver,
			int againstRuns, float againstOvers) {
		this.teamName=teamName;
		this.matches=matches;
		this.won=won;
		this.lost=lost;
		this.tied=tied;
		this.nr=nr;
		this.points=points;
		this.netrr=netrr;
		this.forRuns=forRuns;
		this.forOver=forOver;
		this.againstRuns=againstRuns;
		this.againstOvers=againstOvers;
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

}

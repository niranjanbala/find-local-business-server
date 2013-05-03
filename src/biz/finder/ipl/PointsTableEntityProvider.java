package biz.finder.ipl;

import java.text.DecimalFormat;
import java.util.Scanner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class PointsTableEntityProvider extends AbstractEntityProvider {
	public static final String POINTS_TABLE = "PointsTable";
	@Override
	protected String getKind() {
		return POINTS_TABLE;
	}
	protected Entity prepareEntity(DatastoreService datastore, String line) {
		String[] result = line.split(",");
		Team team = Team.Chennai_Super_Kings;
		team = Team.valueOf(result[0].replace(" ", "_"));

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
		PointTable pointTable = new PointTable(team.name(), matches,
				won, lost, tied, nr, points, netrr, forRuns, forOver,
				againstRuns, againstOvers);
		Entity pointTableRow = datastore
				.prepare(
						new Query(getKind())
								.setFilter(new FilterPredicate(
										"teamName",
										FilterOperator.EQUAL, team
												.name())))
				.asSingleEntity();
		if (pointTableRow == null) {
			pointTableRow = new Entity(getKind());
		}
		pointTableRow.setProperty("teamName", pointTable.getTeamName());
		pointTableRow.setProperty("matches", pointTable.getMatches());
		pointTableRow.setProperty("won", pointTable.getWon());
		pointTableRow.setProperty("points", pointTable.getPoints());
		pointTableRow.setProperty("lost", pointTable.getLost());
		pointTableRow.setProperty("tied", pointTable.getTied());
		pointTableRow.setProperty("nr", pointTable.getNr());
		pointTableRow.setProperty("netrr", pointTable.getNetrr());
		pointTableRow.setProperty("forOver", new DecimalFormat("#.0")
				.format(pointTable.getForOver()));
		pointTableRow.setProperty("forRuns", pointTable.getForRuns());
		pointTableRow.setProperty("againstRuns",
				pointTable.getAgainstRuns());
		pointTableRow.setProperty("againstOver", new DecimalFormat(
				"#.0").format(pointTable.getAgainstOvers()));
		return pointTableRow;
	}

}

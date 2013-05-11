package biz.finder.ipl;

import java.text.DecimalFormat;
import java.util.Scanner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class PointsTableEntityProvider extends AbstractEntityProvider {
	public static final String POINTS_TABLE = "PointsTable";

	@Override
	protected String getKind() {
		return POINTS_TABLE;
	}

	protected Entity prepareEntity(DatastoreService datastore, String line,
			int index) {
		PointTable pointTable = extractPointTable(line);
		Entity pointTableRow = datastore==null?null:datastore.prepare(
				new Query(getKind()).setFilter(new Query.FilterPredicate(
						"teamName", Query.FilterOperator.EQUAL, pointTable.getTeamName())))
				.asSingleEntity();
		if (pointTableRow == null) {
			pointTableRow = new Entity(getKind());
		}
		createEntityFromPointTable(pointTable, pointTableRow);
		return pointTableRow;
	}

	protected void createEntityFromPointTable(PointTable pointTable,
			Entity pointTableRow) {
		pointTableRow.setProperty("teamName", pointTable.getTeamName());
		pointTableRow.setProperty("matches",
				Integer.valueOf(pointTable.getMatches()));
		pointTableRow.setProperty("won", Integer.valueOf(pointTable.getWon()));
		pointTableRow.setProperty("points",
				Integer.valueOf(pointTable.getPoints()));
		pointTableRow
				.setProperty("lost", Integer.valueOf(pointTable.getLost()));
		pointTableRow
				.setProperty("tied", Integer.valueOf(pointTable.getTied()));
		pointTableRow.setProperty("nr", Integer.valueOf(pointTable.getNr()));
		pointTableRow
				.setProperty("netrr", Float.valueOf(pointTable.getNetrr()));
		pointTableRow.setProperty("forOver",
				new DecimalFormat("#.0").format(pointTable.getForOver()));
		pointTableRow.setProperty("forRuns",
				Integer.valueOf(pointTable.getForRuns()));
		pointTableRow.setProperty("againstRuns",
				Integer.valueOf(pointTable.getAgainstRuns()));
		pointTableRow.setProperty("againstOver",
				new DecimalFormat("#.0").format(pointTable.getAgainstOvers()));
	}

	protected PointTable extractPointTable(String line) {
		String[] result = line.split(",");		
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
		PointTable pointTable = new PointTable(team.name(), matches, won, lost,
				tied, nr, points, netrr, forRuns, forOver, againstRuns,
				againstOvers);
		return pointTable;
	}
}

package biz.finder.ipl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class UpdatePointsTableServlet extends HttpServlet {
	private static final String POINTS_TABLE = "PointsTable";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String url = "http://find-business.appspot.com/ipl/results.now";
		String resultString = "";
		String status = "";
		try {
			resultString = new UrlRetriever().retrieve(url);
			Scanner sc = new Scanner(resultString);
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			// List<Filter> filters=new ArrayList<Query.Filter>();
			// for(Team t:Team.values()) {
			// filters.add(new
			// FilterPredicate("teamName",FilterOperator.EQUAL,t.name()));
			// }
			// Filter allFilter=new
			// CompositeFilter(CompositeFilterOperator.OR,filters);
			// Query q = new Query(POINTS_TABLE).setFilter(allFilter);
			// q.setKeysOnly();
			// PreparedQuery pq = datastore.prepare(q);
			//
			// List<Entity> results =
			// pq.asList(FetchOptions.Builder.withLimit(10));
			// for(Entity e:results) {
			// datastore.delete(e.getKey());
			// }
			List<Entity> pointTableRows = new ArrayList<Entity>();
			while (sc.hasNext()) {
				String nextLine = sc.nextLine();
				status += nextLine;
				String[] result = nextLine.split(",");
				Team team = Team.Chennai_Super_Kings;
				try {
					team = Team.valueOf(result[0].replace(" ", "_"));
				} catch (Exception e) {
					status += "exception with team name" + team.name();
				}
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
								new Query(POINTS_TABLE)
										.setFilter(new FilterPredicate(
												"teamName",
												FilterOperator.EQUAL, team
														.name())))
						.asSingleEntity();
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
				pointTableRows.add(pointTableRow);
			}
			datastore.put(pointTableRows);

		} catch (Exception e) {
			resultString = "failed" + status + e;
		}
		resp.setContentType("text/plain");
		resp.getWriter().println(resultString);
	}
}

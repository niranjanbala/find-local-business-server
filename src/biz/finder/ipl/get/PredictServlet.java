package biz.finder.ipl.get;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import biz.finder.Util;
import biz.finder.ipl.FixturesEntityProvider;
import biz.finder.ipl.PointsTableEntityProvider;
import biz.finder.ipl.Predictor;
import biz.finder.ipl.Team;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class PredictServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Map<String,String> userPredictions = new HashMap<String,String>();
		for (int i = 1; i <= 10; i++) {
			String par = req.getParameter("param" + i);
			if (par != null) {
				String[] param = par.split(",");
				if ((param.length == 3) && (!param[2].trim().isEmpty())) {
					userPredictions.put(param[0] + "," + param[1], param[2]);
				}
			}
		}
		resp.setContentType("text/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");
		List<Entity> pointsTable = new PointsTableEntityProvider().findAll(datastore,
				null, 10);
		List<Entity> fixtures = new FixturesEntityProvider().findAll(datastore,
				new Query.FilterPredicate("status", Query.FilterOperator.EQUAL,
						"PENDING"), 100);
		Map<Team,Integer> pointTableMap = new HashMap<Team,Integer>();
		for (Entity e : pointsTable) {
			Team team = Team.valueOf(String.valueOf(e.getProperty("teamName")));
			int points = (int) Math.round(Double.valueOf(
					String.valueOf(e.getProperty("points"))).doubleValue());
			pointTableMap.put(team, Integer.valueOf(points));
		}
		int points;
		for (String winner : userPredictions.values()) {
			try {
			Team winnerTeam = Team.valueOf(winner);
			points = ((Integer) pointTableMap.get(winnerTeam)).intValue();
			pointTableMap.put(winnerTeam, Integer.valueOf(points + 2));
			}catch(Exception e) {
				//eat
			}
		}
		Map<Integer,Team[]> matches = new HashMap<Integer,Team[]>();
		int index = 0;
		String team1;
		for (Entity e : fixtures) {
			team1 = String.valueOf(e.getProperty("team1"));
			String team2 = String.valueOf(e.getProperty("team2"));
			if (!userPredictions.containsKey(team1 + "," + team2)) {
				matches.put(Integer.valueOf(index++),
						new Team[] { Team.valueOf(team1), Team.valueOf(team2) });
			}
		}
		Map<Team,Double> resultAnalysis = Predictor.analysis(pointTableMap, matches);
		for (Entity e : pointsTable) {
			Team team = Team.valueOf(String.valueOf(e.getProperty("teamName")));
			e.setProperty("chances", resultAnalysis.get(team));
		}
		resp.getWriter().println(Util.writeJSON(pointsTable));
	}
}
package biz.finder.ipl.get;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import biz.finder.Util;
import biz.finder.ipl.FixturesEntityProvider;
import biz.finder.ipl.PointTable;
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
					userPredictions.put(param[0] + "," + param[1], param[2]+",10");
				}
				if ((param.length > 3) && (!param[2].trim().isEmpty())) {
					userPredictions.put(param[0] + "," + param[1], param[2]+","+param[3]);
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
		Map<Team,PointTable> pointTableMap = new HashMap<Team,PointTable>();		
		for (Entity e : pointsTable) {
			Team team = Team.valueOf(String.valueOf(e.getProperty("teamName")));
			pointTableMap.put(team, PointTable.fromEntity(e));
		}
		

		Set<Entry<String, String>> entrySet = userPredictions.entrySet();
		for(Entry<String,String> entry:entrySet) {
			String teams[]=entry.getKey().split(",");
			Team team1 = Team.valueOf(teams[0]);
			Team team2 = Team.valueOf(teams[1]);
			String winnerMargin[]=entry.getValue().split(",");
			Team winner = Team.valueOf(winnerMargin[0]);
			int margin=Integer.valueOf(winnerMargin[1]);
			Team loser=team1.equals(winner)?team1:team2;
			pointTableMap.get(winner).updateMatchResult(2, Predictor.WINNING_TEAM_RUNS,  Predictor.OVERS,  Predictor.WINNING_TEAM_RUNS-margin, Predictor.OVERS);
			pointTableMap.get(loser).updateMatchResult(0,  Predictor.WINNING_TEAM_RUNS-margin, Predictor.OVERS,  Predictor.WINNING_TEAM_RUNS, Predictor.OVERS);
		}
//		for (String winner : userPredictions.values()) {
//			try {
//			String input[]=winner.split(",");
//			Team winnerTeam = Team.valueOf(input[0]);
//			int margin=Integer.valueOf(input[1]);
//			//int points= pointTableMap.get(winnerTeam).getPoints();
//			//pointTableMap.put(winnerTeam, Integer.valueOf(points + 2));
//			}catch(Exception e) {
//				//eat
//			}
//		}
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
			e.setProperty("chances", Math.min(100.0, resultAnalysis.get(team)));
		}
		resp.getWriter().println(Util.writeJSON(pointsTable));
	}
}
package biz.finder.ipl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import biz.finder.Util;
import biz.finder.ipl.FixturesEntityProvider;
import biz.finder.ipl.PointsTableEntityProvider;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class PredictServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		for(int i=1;i<=10;i++) {
			req.getParameter("param"+i);
		}
		resp.setContentType("text/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");		
		List<Entity> pointsTable = new PointsTableEntityProvider().findAll(datastore,null);
		new FixturesEntityProvider().findAll(datastore,(new FilterPredicate("status",
				FilterOperator.EQUAL, "PENDING")));
		for(Entity e:pointsTable) {
			e.setProperty("chances", 99.5);
		}
		resp.getWriter().println(Util.writeJSON(pointsTable));
	}

}

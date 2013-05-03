package biz.finder.ipl.get;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import biz.finder.Util;
import biz.finder.ipl.FixturesEntityProvider;
import biz.finder.ipl.IEntityProvider;
import biz.finder.ipl.PointsTableEntityProvider;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class ListServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("Cache-Control", "no-cache");

		String kind = req.getParameter("kind");
		if (kind.equals(PointsTableEntityProvider.POINTS_TABLE)) {
			IEntityProvider entityProvider = new PointsTableEntityProvider();
			List<Entity> entities = entityProvider.findAll(datastore,null);
			resp.getWriter().println(Util.writeJSON(entities));
		}
		else {
			IEntityProvider entityProvider = new FixturesEntityProvider();
			List<Entity> entities=entityProvider.findAll(datastore,(new FilterPredicate("status",
					FilterOperator.EQUAL, "PENDING")));
			Util.writeJSON(entities);
			resp.getWriter().println(Util.writeJSON(entities));
		}
	}

}


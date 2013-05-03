package biz.finder.ipl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class UpdateFixturesTableServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String url = "http://find-business.appspot.com/ipl/fixtures.now";
		String resultString = "";
		try {
			resultString = new UrlRetriever().retrieve(url);			
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			IEntityProvider entityProvider=new FixturesEntityProvider();
			List<Entity> pointTableRows = entityProvider.preparePointsTableEntityCollection(
					resultString, datastore);
			datastore.put(pointTableRows);

		} catch (Exception e) {
			resultString = "failed"  + e;
		}
		resp.setContentType("text/plain");
		resp.getWriter().println(resultString);
	}

}

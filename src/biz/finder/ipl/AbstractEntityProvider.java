package biz.finder.ipl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

public abstract class AbstractEntityProvider implements IEntityProvider {
	// private static final String POINTS_TABLE = "PointsTable";
	protected abstract String getKind();

	protected abstract Entity prepareEntity(DatastoreService datastore,
			String line);

	@Override
	public List<Entity> preparePointsTableEntityCollection(String resultString,
			DatastoreService datastore) {
		Scanner sc = new Scanner(resultString);
		List<Entity> pointTableRows = new ArrayList<Entity>();
		while (sc.hasNext()) {
			String nextLine = sc.nextLine();
			Entity pointTableRow = prepareEntity(datastore, nextLine);
			pointTableRows.add(pointTableRow);
		}
		return pointTableRows;
	}

	@Override
	public List<Entity> findAll(DatastoreService datastore,Filter filter) {		
		Query query = new Query(getKind());
		if(filter!=null) {
			query.setFilter(filter);
		}
		return datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));
	}
}

package biz.finder.ipl;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class FixturesEntityProvider extends AbstractEntityProvider {
	private static final String FIXTURES_TABLE = "Fixtures";

	@Override
	protected String getKind() {
		return FIXTURES_TABLE;
	}

	@Override
	protected Entity prepareEntity(DatastoreService datastore, String line) {
		String[] result = line.split("-");		
		Team team1 = Team.valueOf(result[0]);
		Team team2 = Team.valueOf(result[1]);

		List<Filter> filters=new ArrayList<Query.Filter>();
		filters.add(new FilterPredicate("team1",
				FilterOperator.EQUAL, team1.name()));
		filters.add(new FilterPredicate("team2",
				FilterOperator.EQUAL, team2.name()));
		CompositeFilter filter = new CompositeFilter(CompositeFilterOperator.AND,filters);		
		Entity pointTableRow = datastore.prepare(
				new Query(getKind()).setFilter(filter)).asSingleEntity();
		if (pointTableRow == null) {
			pointTableRow = new Entity(getKind());
		}
		pointTableRow.setProperty("team1", team1.name());
		pointTableRow.setProperty("team2",  team2.name());
		pointTableRow.setProperty("status", result[2]);
		return pointTableRow;

	}
}

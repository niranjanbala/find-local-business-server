package biz.finder.ipl;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;

public class FixturesEntityProvider extends AbstractEntityProvider {
	private static final String FIXTURES_TABLE = "Fixtures";

	@Override
	protected String getKind() {
		return FIXTURES_TABLE;
	}

	protected Entity prepareEntity(DatastoreService datastore, String line,
			int index) {
		String[] result = line.split("-");
		Team team1 = Team.valueOf(result[0]);
		Team team2 = Team.valueOf(result[1]);

		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Query.FilterPredicate("team1",
				Query.FilterOperator.EQUAL, team1.name()));
		filters.add(new Query.FilterPredicate("team2",
				Query.FilterOperator.EQUAL, team2.name()));
		Query.CompositeFilter filter = new Query.CompositeFilter(
				Query.CompositeFilterOperator.AND, filters);
		Entity pointTableRow = datastore.prepare(
				new Query(getKind()).setFilter(filter)).asSingleEntity();
		if (pointTableRow == null) {
			pointTableRow = new Entity(getKind(), index);
		}
		pointTableRow.setProperty("team1", team1.name());
		pointTableRow.setProperty("team2", team2.name());
		pointTableRow.setProperty("status", result[2]);
		return pointTableRow;
	}
}

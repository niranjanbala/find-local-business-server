package biz.finder.ipl.get;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import biz.finder.ipl.FixturesEntityProvider;
import biz.finder.ipl.IEntityProvider;
import biz.finder.ipl.PointTable;
import biz.finder.ipl.PointsTableEntityProvider;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.apphosting.api.ApiProxy;

public class PredictServletTest {

	public static void main(String[] args) throws Exception {
		Map<String, String> userPredictions = new HashMap<String, String>();
		userPredictions.put("Pune_Warriors,Mumbai_Indians", "Mumbai_Indians,10");
		userPredictions.put("Royal_Challengers_Bangalore,Kings_XI_Punjab", "Royal_Challengers_Bangalore,10");
		List<Entity> fixtures = readFixturesTable();
		List<Entity> pointsTable = readPointTable();
		PredictServlet.doFoo(fixtures, userPredictions, pointsTable);
	}

	public static List<Entity> readPointTable() throws Exception {
		Scanner sc = new Scanner(new File("war/ipl/results.now"));
		String resultString = "";

		while (sc.hasNext()) {
			resultString += sc.nextLine() + "\n";

		}
		ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
		IEntityProvider entityProvider = new PointsTableEntityProvider() {
			protected Entity prepareEntity(DatastoreService datastore, String line,
					int index) {
				PointTable pointTable = extractPointTable(line);
				Entity pointTableRow= new Entity("PointTable");				
				createEntityFromPointTable(pointTable, pointTableRow);
				return pointTableRow;
			}
		};
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		List<Entity> pointTableRows = entityProvider
				.preparePointsTableEntityCollection(resultString, datastore);
		return pointTableRows;
	}
	public static List<Entity> readFixturesTable() throws Exception {
		Scanner sc = new Scanner(new File("war/ipl/fixtures.now"));
		String resultString = "";
		while (sc.hasNext()) {
			resultString += sc.nextLine() + "\n";
		}
		ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment());
		IEntityProvider entityProvider = new FixturesEntityProvider() {
			protected Entity prepareEntity(DatastoreService datastore, String line,
					int index) {
				return super.prepareEntity(null, line, index);
			}
		};
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		List<Entity> fixtures = entityProvider
				.preparePointsTableEntityCollection(resultString, datastore);
		List<Entity> fixturesRemaining=new ArrayList<Entity>();
		for(Entity fixture: fixtures) {
			if(String.valueOf(fixture.getProperty("status")).equals("PENDING")) {
				fixturesRemaining.add(fixture);
			}
		}
		return fixturesRemaining;
	}
}

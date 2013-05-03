package biz.finder.ipl;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;

public interface IEntityProvider {

	List<Entity> preparePointsTableEntityCollection(String resultString,
			DatastoreService datastore);

	List<Entity> findAll(DatastoreService datastore,Filter filter);

}

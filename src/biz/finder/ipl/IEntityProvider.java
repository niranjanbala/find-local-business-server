package biz.finder.ipl;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public abstract interface IEntityProvider
{
  public abstract List<Entity> preparePointsTableEntityCollection(String paramString, DatastoreService paramDatastoreService);

  public abstract List<Entity> findAll(DatastoreService paramDatastoreService, Query.Filter paramFilter, int paramInt);
}
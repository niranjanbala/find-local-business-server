package biz.finder.ipl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

public abstract class AbstractEntityProvider
  implements IEntityProvider
{
  protected abstract String getKind();

  protected abstract Entity prepareEntity(DatastoreService paramDatastoreService, String paramString, int paramInt);

  public List<Entity> preparePointsTableEntityCollection(String resultString, DatastoreService datastore)
  {
    Scanner sc = new Scanner(resultString);
    List<Entity> pointTableRows = new ArrayList<Entity>();
    int index = 1;
    while (sc.hasNext()) {
      String nextLine = sc.nextLine();
      Entity pointTableRow = prepareEntity(datastore, nextLine, index++);
      pointTableRows.add(pointTableRow);
    }
    return pointTableRows;
  }

  public List<Entity> findAll(DatastoreService datastore, Query.Filter filter, int limit)
  {
    Query query = new Query(getKind());
    if (filter != null) {
      query.setFilter(filter);
    }
    return datastore.prepare(query).asList(FetchOptions.Builder.withLimit(limit));
  }
}
package controllers;

import org.joda.time.DateTime;
import org.joda.time.Days;
import models.Checkoff;
import models.Goal;
import play.modules.twig.Datastore;
import play.mvc.Controller;
import play.data.validation.Valid;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import net.urbancoding.checkingspree.NewGoalRequest;
import net.urbancoding.checkingspree.GoalResponse;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: May 26, 2011
 */
public class Checkoffs extends Controller {

  public static void changeState(Long goalId, int daysAgo, Checkoff.State state) {
    Goal goal = Datastore.load(Goal.class, goalId);
    int daysSinceEpoch = Days.daysBetween(Checkoff.EPOCH_DAY, new DateTime().minusDays(daysAgo)).getDays();

    //If you are working on a large-scale project, you may want to use constants for field names.
    //Querying for a field that doesn't exist on the model class will throw an exception.
    Checkoff checkoff = Datastore.find().type(Checkoff.class)
        .addFilter("daysSinceEpoch", Query.FilterOperator.EQUAL, daysSinceEpoch)
        .ancestor(goal)
        .returnUnique()
        .now();


    if (null != checkoff) {
      checkoff.setState(state);

      //checkoff should already be associated to goal, so lets just update it
      //no need to call Datastore.associate because it's the same instance we
      //pulled from the datastore
      Datastore.update(checkoff);
    }
    else {
      checkoff = new Checkoff(goal, state, daysAgo);
      goal.addCheckoff(checkoff);

      //In this case, we are performing two datastore writes, so lets do that
      //in a transaction. If you have multiple writes, you absolutely must do
      //it in a single transaction (same entity group), use a failure-sensitive
      //update task, or have a data integrity background task to fix it.
      //Updates do fail on GAE and not handling them causes unsexy problems.
      Transaction transaction = Datastore.beginTransaction();
      Datastore.store(checkoff);
      Datastore.update(goal);
      transaction.commit();
    }

    renderJSON("");
  }
  
}

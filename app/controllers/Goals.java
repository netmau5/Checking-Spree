package controllers;

import play.data.validation.Valid;
import play.data.validation.Required;
import play.modules.twig.Datastore;
import play.mvc.Controller;
import net.urbancoding.checkingspree.NewGoalRequest;
import models.Goal;
import com.google.appengine.api.datastore.Query;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: Jun 3, 2011
 */
public class Goals extends Controller {

  public static void add(@Valid NewGoalRequest goalRequest) {
    /**
     * Remember when using filters, the datastore representation of the value
     * is what will be compared against. That is to say, if your DateTime object
     * is represented as a date in the datastore, you need to convert your value
     * to a milliseconds value or Date object.
     *
     * In this case, we are querying for a String so all is well.
     */
    Goal goal = Datastore.find().type(Goal.class)
        .addFilter("name", Query.FilterOperator.EQUAL, goalRequest.name)
        .returnUnique()
        .now();
    if (null == goal) {
      Datastore.store(goalRequest.newGoal());
    }
    else {
      editGoal(goal, goalRequest);
    }
    Application.index();
  }

  public static void edit(@Required Long goalId, @Valid NewGoalRequest goalRequest) {
    Goal goal = Datastore.load(Goal.class, goalId);
    editGoal(goal, goalRequest);
    Application.index();
  }

  private static void editGoal(Goal goal, NewGoalRequest goalRequest) {
    goal.setName(goalRequest.name);
    goal.setActiveDays(goalRequest.getActiveDays());
    goal.setCreated(goalRequest.getCreatedDateTime());

    /**
     * Update will not work on an object that doesn't have a valid id as
     * demarcated by the @Id annotation. I recommend creating a base class
     * for your entities including an Id field and then creating a static
     * helper to automatically decide which one to use.
     */
    if (null != goal.getId()) {
      Datastore.update(goal);
    }
    else {
      Datastore.store(goal);
    }
  }

  public static void index(@Required Long goalId) {
    Goal goal = Datastore.load(Goal.class, goalId);
    render(goal);
  }

  public static void delete(@Required Long goalId) {
    Datastore.delete(Datastore.load(Goal.class, goalId));
    Application.index();
  }

}

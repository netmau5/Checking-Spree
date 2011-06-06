package controllers;

import play.*;
import play.modules.twig.Datastore;
import play.mvc.*;

import java.util.*;

import models.*;
import net.urbancoding.checkingspree.GoalResponse;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: May 26, 2011
 */
public class Application extends Controller {

  public static void index() {
    /**
     * In a larger project, you will want to isolate your data access to a different
     * layer of your application. If you're developing a small app, however, don't
     * out architect yourself!
     */
    List<Goal> goals = Datastore.find().type(Goal.class).returnAll().now();

    /**
     * When passing several objects to your template or when you have to perform
     * several data mapping steps before presentation, I recommend using a wrapper
     * object to contain it. Furthermore, if using an advanced IDE, you will get
     * some refactoring support.
     */
    GoalResponse goalResponse = new GoalResponse(goals);
    render(goalResponse);
  }

}
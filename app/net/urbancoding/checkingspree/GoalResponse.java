package net.urbancoding.checkingspree;

import models.Goal;
import models.Checkoff;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import org.joda.time.Days;
import org.joda.time.DateTime;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: May 31, 2011
 */
public class GoalResponse {

  public DateTime NOW = new DateTime();

  /* map containing a goal to a map of checkoffs organized by how many days ago it was made */
  private Map<Goal, Map<Integer, Checkoff>> checkoffsByGoal;

  private List<Goal> goals;
  private DateTime now;

  public GoalResponse(List<Goal> goals) {
    this.goals = goals;
    this.checkoffsByGoal = Maps.newHashMap();
    this.now = new DateTime();

    DateTime now = new DateTime();
    for (Goal goal: goals) {
      checkoffsByGoal.put(goal, Maps.<Integer, Checkoff>newHashMap());
      for (Checkoff checkoff: goal.getLastMonthsCheckoffs()) {
        Map<Integer, Checkoff> checkoffByDaysAgo = checkoffsByGoal.get(checkoff.getGoal());
        checkoffByDaysAgo.put(Days.daysBetween(checkoff.getCreated(), now).getDays(), checkoff);
      }
    }
  }

  public List<Goal> getGoals() {
    return goals;
  }

  public Checkoff getCheckoff(int daysAgo, Goal goal) {
    Checkoff checkoff = checkoffsByGoal.get(goal).get(daysAgo);


    if (daysAgo > Days.daysBetween(goal.getCreated(), this.now).getDays()) {
      return Checkoff.newDayNotCounted(daysAgo);
    }
    else if (!Iterables.any(goal.getActiveDays(), new IsDayActivePredicate(daysAgo))) {
      return Checkoff.newDayNotCounted(daysAgo);
    }
    if (null == checkoff && daysAgo <= Days.daysBetween(goal.getCreated(), this.now).getDays()){
      return Checkoff.newDayUnset(daysAgo);
    }
    else if (null != checkoff) {
      return checkoff;
    }
    else {
      throw new IllegalStateException();
    }
  }

  private class IsDayActivePredicate implements Predicate<Goal.Day> {

    private String dayOfWeek;

    private IsDayActivePredicate(int daysAgo) {
      this.dayOfWeek = now.minusDays(daysAgo).dayOfWeek().getAsText();
    }

    public boolean apply(@Nullable Goal.Day day) {
      return StringUtils.equalsIgnoreCase(
          dayOfWeek,
          day.toString()
      );
    }
  }

}

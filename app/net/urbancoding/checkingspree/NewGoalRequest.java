package net.urbancoding.checkingspree;

import models.Goal;

import java.util.Set;
import java.sql.Date;

import com.google.common.collect.Sets;
import play.data.validation.Required;
import play.data.binding.As;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimeFormat;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: May 26, 2011
 */
public class NewGoalRequest {

  @Required public String name;
  public String created;
  public boolean monday;
  public boolean tuesday;
  public boolean wednesday;
  public boolean thursday;
  public boolean friday;
  public boolean saturday;
  public boolean sunday;
  
  public Set<Goal.Day> getActiveDays() {
    Set<Goal.Day> days = Sets.newHashSet();
    if (monday) days.add(Goal.Day.MONDAY);
    if (tuesday) days.add(Goal.Day.TUESDAY);
    if (wednesday) days.add(Goal.Day.WEDNESDAY);
    if (thursday) days.add(Goal.Day.THURSDAY);
    if (friday) days.add(Goal.Day.FRIDAY);
    if (saturday) days.add(Goal.Day.SATURDAY);
    if (sunday) days.add(Goal.Day.SUNDAY);
    return days;
  }
  
  public Goal newGoal() {
    Goal goal = new Goal();
    goal.setName(name);
    goal.setActiveDays(getActiveDays());

    if (null != created) {
      goal.setCreated(getCreatedDateTime());
    }
    
    return goal;
  }

  public DateTime getCreatedDateTime() {
    return DateTimeFormat.forPattern("MM/dd/yyyy").parseDateTime(created);
  }

}

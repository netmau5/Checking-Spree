package models;

import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Parent;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: May 26, 2011
 */
public class Checkoff {

  /**
   * To make for easier querying, we will add a measurement of days since the epoch,
   * an arbitrary start day for our application.
   */
  public static DateTime EPOCH_DAY = new DateTime(2001, 6, 1, 0, 0, 0, 0);

  @Id
  private Long id;

  private DateTime created;
  private int daysSinceEpoch;

  private State state;
  private int streak;

  /**
   * We intend to store checkoffs and goals together in the same transaction. Therefore, a checkoff
   * must be in the same entity group as a goal. This can be done by associating it to a parent goal.
   */
  @Parent
  private Goal goal;

  public enum State {
    CHECKED,      //completed goal for day
    NOT_CHECKED,  //didn't complete goal for day, breaks streak
    SKIPPED,      //user selected skip day for their own reasons, doesn't count against streak
    UNSET,        //no user input
    NOT_COUNTED   //days that arent counted because of goal configuration, doesn't count against streka
  }

  public Checkoff() {
    this(new DateTime());
  }

  public Checkoff(DateTime created) {
    this.created = created;
    this.daysSinceEpoch = Days.daysBetween(EPOCH_DAY, this.created).getDays();
  }

  public Checkoff(Goal goal, State state, int daysAgo) {
    this(new DateTime().minusDays(daysAgo));
    this.goal = goal;
    this.state = state;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getDaysSinceEpoch() {
    return daysSinceEpoch;
  }

  public void setDaysSinceEpoch(int daySinceEpoch) {
    this.daysSinceEpoch = daySinceEpoch;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public int getStreak() {
    return streak;
  }

  public void setStreak(int streak) {
    this.streak = streak;
  }

  public Goal getGoal() {
    return goal;
  }

  public void setGoal(Goal goal) {
    this.goal = goal;
  }

  public static Checkoff newDayNotCounted(int daysAgo) {
    Checkoff checkoff = new Checkoff();
    checkoff.setState(State.NOT_COUNTED);
    return checkoff; 
  }

  public static Checkoff newDaySkipped(int daysAgo) {
    Checkoff checkoff = newDayNotCounted(daysAgo);
    checkoff.setState(State.SKIPPED);
    return checkoff;
  }

  public static Checkoff newDayUnset(int daysAgo) {
    Checkoff checkoff = newDayNotCounted(daysAgo);
    checkoff.setState(State.UNSET);
    return checkoff;
  }
}

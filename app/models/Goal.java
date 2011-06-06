package models;

import com.google.code.twig.annotation.Id;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Preconditions;

import java.util.Set;
import java.util.List;
import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: May 26, 2011
 */
public class Goal {

  @Id
  private Long id;

  private String name;
  private Set<Day> activeDays;
  private DateTime created;

  /**
   * In this particular case, it would make sense to use the
   * @Embed annotation to attach these checkoffs directly to the
   * goal for better performance. However, we want to use this
   * to show the usage of cascading (or lack thereof) and
   * transactions.
   *
   * Using this approach, we should also have a background task that
   * clears old checkoffs (after a month) from this list.
   */
  private List<Checkoff> lastMonthsCheckoffs;

  public enum Day {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;
  }

  public Goal() {
    this.activeDays = Sets.newHashSet();
    this.lastMonthsCheckoffs = Lists.newArrayList();
    this.created = new DateTime();
  }

  public boolean isActive(String day) {
    return this.activeDays.contains(Day.valueOf(day));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Day> getActiveDays() {
    return activeDays;
  }

  public void setActiveDays(Set<Day> activeDays) {
    this.activeDays = activeDays;
  }

  public void addDay(Day day) {
    this.activeDays.add(day);
  }

  public List<Checkoff> getLastMonthsCheckoffs() {
    return lastMonthsCheckoffs;
  }

  public void setLastMonthsCheckoffs(List<Checkoff> lastMonthsCheckoffs) {
    this.lastMonthsCheckoffs = lastMonthsCheckoffs;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public void addCheckoff(Checkoff checkoff) {
    Preconditions.checkState(checkoff.getCreated().isAfter(this.getCreated()));
    this.lastMonthsCheckoffs.add(checkoff);
  }
}

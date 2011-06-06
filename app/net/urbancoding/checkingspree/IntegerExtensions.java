package net.urbancoding.checkingspree;

import play.templates.JavaExtensions;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: May 26, 2011
 */
public class IntegerExtensions extends JavaExtensions {

  public static DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd");


  public static String daysAgo(Integer i) {
    return DATE_FORMAT.format(new DateTime().minusDays(i).toDate());
  }

}

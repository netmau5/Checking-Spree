package net.urbancoding.checkingspree;

import play.templates.JavaExtensions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: Jun 3, 2011
 */
public class DateTimeExtensions extends JavaExtensions {

  public static String format(DateTime dateTime) {
    return DateTimeFormat.forPattern("MM/dd/yyyy").print(dateTime);
  }

}

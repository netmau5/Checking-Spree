package net.urbancoding.checkingspree;

import org.joda.time.DateTime;
import play.modules.twig.TwigPropertyTranslator;

/**
 * @author Dave Jafari (djafaricom@gmail.com)
 * @created: May 26, 2011
 */
public class DateTimeTranslator extends TwigPropertyTranslator<DateTime> {

  public Class<DateTime> getTranslatorClass() {
    return DateTime.class;
  }

  public DateTime decode(Object datastoreProperty) {
    return new DateTime((Long) datastoreProperty);
  }

  public Object encode(DateTime objectProperty) {
    return objectProperty.getMillis();
  }
}

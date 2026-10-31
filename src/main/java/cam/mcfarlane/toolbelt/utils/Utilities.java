package cam.mcfarlane.toolbelt.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;

/**
 * A collection of static utilities to use throughout the codebase.
 */
public class Utilities {

  /**
   * @return the number of days since the configured server start date.
   */
  public static int getDaysSinceServerStart() {
    LocalDate now = LocalDate.now(ZoneId.of("Australia/Adelaide"));
    return (int) ChronoUnit.DAYS.between(ToolbeltConfiguration.SERVER_START_DATE, now) + 1;
  }

  /**
   * Formats the given {@link TemporalAccessor} timestamp using the
   * {@link FormatStyle#SHORT} format and the system default timezone.<br/>
   * Example: 1:32PM
   * 
   * @param temporal the timestamp to format.
   * @return a formatted time string.
   */
  public static String formatTime(TemporalAccessor temporal) {
    return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
      .withZone(ZoneId.systemDefault())
      .format(temporal);
  }
  
}

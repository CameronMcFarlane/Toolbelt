/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt;

import java.time.LocalDate;

/**
 * Stores configuration properties to be accessed throughout the plugin.
 */
public class ToolbeltConfiguration {

  public static String    SERVER_NAME = "Coldbrew";
  public static LocalDate SERVER_START_DATE = LocalDate.of(2026, 10, 26);

  public static boolean   AWAY_ENABLED = true;
  public static int       AWAY_THRESHOLD_SECS = 120;

  public static boolean   BULLETIN_ENABLED = true;

  public static boolean   DIMENSION_ENABLED = true;
  public static boolean   DIMENSION_NETHER_ENABLED = false;
  public static boolean   DIMENSION_END_ENABLED = false;
  public static boolean   DIMENSION_OVERWORLD_ENABLED = true;

  public static boolean   ROSTER_ENABLED = true;
  public static boolean   ROSTER_HEARTS = false;
  
}

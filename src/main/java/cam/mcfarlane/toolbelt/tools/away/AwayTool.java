/* Copyright (C) 2026 Cameron McFarlane */

package cam.mcfarlane.toolbelt.tools.away;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;
import cam.mcfarlane.toolbelt.tools.Tool;
import net.kyori.adventure.text.Component;

/**
 * A tool which keeps track of how long players have been away.
 */
public class AwayTool implements Tool {

  private AwayListener m_listener = new AwayListener(this);
  private ConcurrentHashMap<String, Integer> m_timeAway = new ConcurrentHashMap<>();
  private ExecutorService m_executor = Executors.newSingleThreadExecutor();

  // TODO: Change to use system time instead of ticking a value up every second

  @Override
  public void init(JavaPlugin plugin) {
    if (!ToolbeltConfiguration.AWAY_ENABLED)
      return;

    // Register events
    Bukkit.getPluginManager().registerEvents(m_listener, plugin);

    // Start thread for ticking away times
    Runnable timer = () -> {
      try {
        while (true) {
          m_timeAway.forEach((playerName, awaySecs) -> {
            boolean currentlyAway = awaySecs >= ToolbeltConfiguration.AWAY_THRESHOLD_SECS;
            awaySecs++;
            // If the player wasn't away before but now is, update their status
            if (!currentlyAway && awaySecs >= ToolbeltConfiguration.AWAY_THRESHOLD_SECS) {
              updatePlayerStatus(playerName, true);
            }
          });
          Thread.sleep(1000);
        }
      } catch (InterruptedException ex) {
        return;
      }
    };
    m_executor.submit(timer);
  }

  @Override
  public void shutDown() {
    // Shut down the executor and any running threads
    m_executor.shutdown();
  }

  /**
   * Updates the away status of the given player with a broadcasted message to the server
   * and their player name in the list changing colour.
   * @param playerName the name of the player to update the status for.
   * @param away whether the player is away or back.
   */
  public void updatePlayerStatus(String playerName, boolean away) {
    if (away) {
      Bukkit.broadcast(Component.text(String.format("%s is AFK.", playerName)));
    } else {
      Bukkit.broadcast(Component.text(String.format("%s is no longer AFK.", playerName)));
    }
  }

  /**
   * Check if the given player has been away longer than the configured away time.
   * @param playerName the name of the player.
   * @return true if the player is AFK.
   */
  public boolean isPlayerAway(String playerName) {
    Integer awaySecs = m_timeAway.get(playerName);
    if (awaySecs == null || awaySecs >= ToolbeltConfiguration.AWAY_THRESHOLD_SECS)
      return true;
    return false;
  }

  /**
   * Resets the given player's away time back to 0, adding the player to
   * the map if it doesn't already exist.
   * @param playerName The name of the player to reset the away time for.
   */
  public void resetAwayTime(String playerName) {
    m_timeAway.put(playerName, 0);
  }

  /**
   * Removes a player from the away list.
   * @param playerName The name of the player to remove.
   */
  public void removePlayer(String playerName) {
    m_timeAway.remove(playerName);
  }

  /**
   * Sends a message to the player with a list of connected players and their away status.
   * @param player the player to send the message to.
   */
  public void sendAwayStatus(Player player) {
    m_timeAway.forEach((playerName, awaySecs) -> {
      player.sendMessage(Component.text(String.format("%s : %d seconds", playerName, awaySecs)));
    });
  }

}

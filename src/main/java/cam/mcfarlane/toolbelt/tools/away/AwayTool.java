/* Copyright (C) 2026 Cameron McFarlane */

package cam.mcfarlane.toolbelt.tools.away;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;
import cam.mcfarlane.toolbelt.tools.Tool;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * A tool which keeps track of how long players have been away.
 */
public class AwayTool implements Tool {

  private AwayListener m_listener = new AwayListener(this);
  private ConcurrentHashMap<UUID, Integer> m_timeAway = new ConcurrentHashMap<>();
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
          m_timeAway.forEach((id, awaySecs) -> {
            boolean currentlyAway = awaySecs >= ToolbeltConfiguration.AWAY_THRESHOLD_SECS;
            m_timeAway.put(id, ++awaySecs);
            // If the player wasn't away before but now is, update their status
            if (!currentlyAway && awaySecs >= ToolbeltConfiguration.AWAY_THRESHOLD_SECS) {
              updatePlayerStatus(Bukkit.getPlayer(id), true);
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
   * @param player the player to update the status for.
   * @param away whether the player is away or back.
   */
  public void updatePlayerStatus(Player player, boolean away) {
    String playerName = player.getName();
    if (away) {
      // Broadcast a message to the server
      Component broadcastMessage = Component.text(playerName)
        .decorate(TextDecoration.BOLD)
        .append(Component.text(" is away.").decoration(TextDecoration.BOLD, false));
      Bukkit.broadcast(broadcastMessage);
      // Change the style of the player's name in the player list
      Component playerListName = player.displayName()
        .decorate(TextDecoration.ITALIC)
        .color(NamedTextColor.GRAY);
      player.playerListName(playerListName);
    } else {
      // Broadcast a message to the server
      Component broadcastMessage = Component.text(playerName)
        .decorate(TextDecoration.BOLD)
        .append(Component.text(" is back!").decoration(TextDecoration.BOLD, false));
      Bukkit.broadcast(broadcastMessage);
      // Change the style of the player's name in the player list
      Component playerListName = player.displayName()
        .decoration(TextDecoration.ITALIC, false)
        .color(NamedTextColor.WHITE);
      player.playerListName(playerListName);
    }
  }

  /**
   * Check if the given player has been away longer than the configured away time.
   * @param player the player to check.
   * @return true if the player is AFK.
   */
  public boolean isPlayerAway(Player player) {
    Integer awaySecs = m_timeAway.get(player.getUniqueId());
    if (awaySecs != null && awaySecs >= ToolbeltConfiguration.AWAY_THRESHOLD_SECS)
      return true;
    return false;
  }

  /**
   * Resets the given player's away time back to 0, adding the player to
   * the map if it doesn't already exist.
   * @param player The player to reset the away time for.
   */
  public void resetAwayTime(Player player) {
    m_timeAway.put(player.getUniqueId(), 0);
  }

  /**
   * Removes a player from the away list.
   * @param player The player to remove.
   */
  public void removePlayer(Player player) {
    m_timeAway.remove(player.getUniqueId());
  }

  /**
   * Sends a message to the player with a list of connected players and their away status.
   * @param player the player to send the message to.
   */
  public void sendAwayStatus(Player player) {
    StringBuilder awayList = new StringBuilder();
    m_timeAway.forEach((id, awaySecs) -> {
      String playerName = Bukkit.getPlayer(id).getName();
      if (isPlayerAway(player)) {
        awayList.append(String.format("%s has been away for %d seconds.%n", playerName, awaySecs));
      } else {
        awayList.append(String.format("%s is not away.%n", playerName));
      }
    });
    player.sendMessage(Component.text(awayList.toString().trim()));
  }

}

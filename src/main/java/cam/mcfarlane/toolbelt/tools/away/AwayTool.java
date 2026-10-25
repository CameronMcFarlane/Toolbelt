/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.away;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;
import cam.mcfarlane.toolbelt.ToolbeltPlugin;
import cam.mcfarlane.toolbelt.tools.Tool;
import cam.mcfarlane.toolbelt.tools.roster.RosterTool;

/**
 * Keeps track of how long players have been inactive.
 */
public class AwayTool extends Tool {

  /**
   * Tracks the status of each player, including a timestamp of
   * when they were last seen and a boolean to determine if they
   * have already been marked as away.
   */
  private class AwayStatus {
    Instant lastSeen = Instant.now();
    boolean away = false;
  }

  private final int AWAY_CHECK_FREQUENCY_TICKS = 20;

  private ConcurrentHashMap<UUID, AwayStatus> m_awayStatus = new ConcurrentHashMap<>();
  private BukkitTask m_awayTask;

  public AwayTool(ToolbeltPlugin plugin) {
    super(plugin);
  }

  @Override
  public void init() {
    m_listener = new AwayListener(this);

    m_awayTask = m_plugin.getServer().getScheduler().runTaskTimer(
      m_plugin,
      this::checkAway,
      0,
      AWAY_CHECK_FREQUENCY_TICKS);
  }

  @Override
  public void shutDown() {
    // Shut down the executor and any running threads
    m_awayTask.cancel();
  }

  @Override
  public boolean isEnabled() {
    return ToolbeltConfiguration.AWAY_ENABLED;
  }

  /**
   * Iterates through each of the connected players who aren't away
   * and updates their status if they
   */
  private void checkAway() {
    m_awayStatus.forEach((id, status) -> {
      if (!status.away) {
        Duration duration = Duration.between(status.lastSeen, Instant.now());
        if (duration.toSeconds() > ToolbeltConfiguration.AWAY_THRESHOLD_SECS) {
          status.away = true;
          updatePlayerStatus(Bukkit.getPlayer(id), true);
        }
      }
    });
  }

  /**
   * Resets the given player's away time, adding the player to
   * the map if it doesn't already exist.
   * @param player The player to reset the away time for.
   */
  public void resetAwayTime(Player player) {
    m_awayStatus.put(player.getUniqueId(), new AwayStatus());
  }

  /**
   * Updates the away status of the given player with a broadcasted message to the server
   * and their player name in the list changing colour.
   * @param player the player to update the status for.
   * @param away whether the player is away or back.
   */
  public void updatePlayerStatus(Player player, boolean away) {
    @SuppressWarnings("null")
    Optional<RosterTool> rosterTool = m_plugin.getTool(RosterTool.class);

    // Construct broadcast message and send to the server
    Component message = text(player.getName()).decorate(BOLD).color(GRAY);
    if (away) {
      message = message.append(text(" is away.").decoration(BOLD, false));
      // Change the player's team (if the roster tool is enabled)
      if (rosterTool.isPresent()) {
        rosterTool.get().joinTeam(player, RosterTool.TEAM_AWAY);
      }
    } else {
      message = message.append(text(" is back!").decoration(BOLD, false));
      // Change the player's team (if the roster tool is enabled)
      if (rosterTool.isPresent()) {
        rosterTool.get().joinDefaultTeam(player);
      }
    }
    playAwaySound(player);
    Bukkit.broadcast(message);
  }

  /**
   * Check if the given player has been away longer than the configured time.
   * @param player the player to check.
   * @return true if the player is away.
   */
  public boolean isPlayerAway(Player player) {
    return m_awayStatus.get(player.getUniqueId()).away;
  }

  /**
   * Sends a message to the player with a list of connected players and their away status.
   * @param player the player to send the message to.
   */
  public void sendAwayStatus(Player player) {
    StringBuilder awayList = new StringBuilder();
    m_awayStatus.forEach((id, awaySecs) -> {
      String playerName = Bukkit.getPlayer(id).getName();
      if (isPlayerAway(player)) {
        awayList.append(String.format("%s has been away for %d seconds.%n", playerName, awaySecs));
      } else {
        awayList.append(String.format("%s is not away.%n", playerName));
      }
    });
    player.sendMessage(Component.text(awayList.toString()));
  }

  /**
   * Removes a player from the away list.
   * @param player The player to remove.
   */
  public void removePlayer(Player player) {
    m_awayStatus.remove(player.getUniqueId());
  }

  public void playAwaySound(Player player) {
    // https://minecraft.wiki/w/Sounds.json
    String option1 = "item.book.page_turn";
    String option2 = "item.lodestone_compass.lock";

    Sound sound = Sound.sound(Key.key(option1), Sound.Source.PLAYER, 1f, 1f);
    player.playSound(sound);
  }

}

/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.away;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.TextComponent.Builder;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;
import cam.mcfarlane.toolbelt.ToolbeltPlugin;
import cam.mcfarlane.toolbelt.tools.Tool;
import cam.mcfarlane.toolbelt.utils.Utilities;

/**
 * Keeps track of how long players have been inactive.
 */
public class AwayTool extends Tool {

  private final int AWAY_CHECK_FREQUENCY_TICKS = 20;
  private final String NOTIFICATION_SOUND = "item.book.page_turn";

  private ConcurrentHashMap<UUID, Status> m_playerStatus;
  private BukkitTask m_awayTask;

  /**
   * Tracks the status of each player, including a timestamp of
   * when they were last seen and a boolean to determine if they
   * have already been marked as away.
   */
  private class Status {
    Instant lastSeen = Instant.now();
    boolean away = false;
  }

  public AwayTool(ToolbeltPlugin plugin) {
    super(plugin);
    m_playerStatus = new ConcurrentHashMap<>();
    m_command = new AwayCommand(this);
  }

  @Override
  public boolean isEnabled() {
    return ToolbeltConfiguration.AWAY_ENABLED;
  }

  @Override
  public void init() {
    ToolbeltPlugin.LOGGER.info("Initialising the Away tool...");
    m_listener = new AwayListener(this);

    // Create a scheduled task to continually check for away players
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

  /**
   * Iterates through each of the connected players who aren't away
   * and updates their status if their last seen value is greater
   * than the configured threshold.
   */
  private void checkAway() {
    m_playerStatus.forEach((id, status) -> {
      if (!status.away) {
        Duration duration = Duration.between(status.lastSeen, Instant.now());
        if (duration.toSeconds() > ToolbeltConfiguration.AWAY_THRESHOLD_SECS) {
          setPlayerAway(Bukkit.getPlayer(id));
        }
      }
    });
  }

  /**
   * Plays a notification sound when a player changes away status.
   * 
   * @param player the player to play the sound for.
   */
  private void playAwaySound(Player player) {
    Sound sound = Sound.sound(
      Key.key(NOTIFICATION_SOUND),
      Sound.Source.PLAYER,
      1f,
      1f);
    player.playSound(sound);
  }

  /**
   * Sets the given player's status to away.
   * 
   * @param player the player to set the status for.
   */
  protected void setPlayerAway(Player player) {
    m_playerStatus.get(player.getUniqueId()).away = true;
    updatePlayerStatus(player, true);
  }

  /**
   * Check if the given player has been away longer than the configured time.
   * 
   * @param player the player to check.
   * @return true if the player is away.
   */
  protected boolean isPlayerAway(Player player) {
    return m_playerStatus.get(player.getUniqueId()).away;
  }

  /**
   * Updates the away status of the given player, sending a broadcast message,
   * playing a sound, and calling the {@link AwayEvent} for listeners to do
   * additional actions.
   * 
   * @param player the player to update the status for.
   * @param away whether the player is away or back.
   */
  protected void updatePlayerStatus(Player player, boolean away) {
    Builder message = text().content(player.getName()).decorate(BOLD).color(GRAY);
    if (away) {
      message.append(text(" is away.").decoration(BOLD, false));
    } else {
      message.append(text(" is back!").decoration(BOLD, false));
    }
    Bukkit.broadcast(message.build());
    playAwaySound(player);
    new AwayEvent(player, away).callEvent();
  }

  /**
   * Resets the given player's last seen time and, if their current status
   * is away, resets their status to no longer be away.
   * 
   * @param player the player which moved.
   */
  protected void playerMoved(Player player) {
    if (isPlayerAway(player)) {
      updatePlayerStatus(player, false);
    }
    resetAwayTime(player);
  }

  /**
   * Resets the given player's away time, adding the player to
   * the map if they don't already exist.
   * 
   * @param player the player to reset the away time for.
   */
  protected void resetAwayTime(Player player) {
    m_playerStatus.put(player.getUniqueId(), new Status());
  }

  /**
   * Removes a player from the away list.
   * 
   * @param player the player to remove.
   */
  protected void removePlayer(Player player) {
    m_playerStatus.remove(player.getUniqueId());
  }

  /**
   * Sends a message to the given audience with a list of connected
   * players and their away status.
   * 
   * @param audience the audience to send the list of away players.
   */
  protected void sendAwayStatus(Audience audience) {
    if (m_playerStatus.isEmpty()) {
      audience.sendMessage(text("Where is everyone?"));
      return;
    }
    m_playerStatus.forEach((id, status) -> {
      Builder message = text();
      String name = Bukkit.getPlayer(id).getName();
      message.append(text(name).decorate(BOLD)).appendSpace();
      if (status.away) {
        message.append(text("was last seen at %s.".formatted(Utilities.formatTime(status.lastSeen))));
      } else {
        message.append(text("is active."));
      }
      audience.sendMessage(message.build());
    });
  }

}

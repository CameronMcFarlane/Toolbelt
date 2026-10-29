/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.away;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Tracks all events needed for managing away times.
 */
public class AwayListener implements Listener {

  AwayTool m_tool;

  public AwayListener(AwayTool tool) {
    m_tool = tool;
  }

  /**
   * Initialises the tracking of the player's last movement when
   * the player joins the server.
   * 
   * @param event The {@link PlayerJoinEvent} metadata.
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    m_tool.resetAwayTime(event.getPlayer());
  }

  /**
   * Removes tracking of the player when they leave the server.
   * 
   * @param event The {@link PlayerQuitEvent} metadata.
   */
  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    m_tool.removePlayer(event.getPlayer());
  }

  /**
   * Updates the last player movement when input is detected
   * from the player, and updates their away status if they are
   * currently away.
   * 
   * @param event The {@link PlayerInputEvent} metadata.
   */
  @EventHandler 
  public void onPlayerInput(PlayerInputEvent event) {
    if (m_tool.isPlayerAway(event.getPlayer())) {
      m_tool.updatePlayerStatus(event.getPlayer(), false);
    }
    m_tool.resetAwayTime(event.getPlayer());
  }

  /**
   * Updates the last player movement when an interaction (click)
   * is detected from the player, and updates their away status
   * if they are currently away.
   * 
   * @param event The {@link PlayerInteractEvent} metadata.
   */
  @EventHandler
  public void onPlayerClick(PlayerInteractEvent event) {
    if (m_tool.isPlayerAway(event.getPlayer())) {
      m_tool.updatePlayerStatus(event.getPlayer(), false);
    }
    m_tool.resetAwayTime(event.getPlayer());
  }

}

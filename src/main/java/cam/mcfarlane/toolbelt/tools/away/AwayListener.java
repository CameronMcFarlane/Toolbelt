/* Copyright (C) 2026 Cameron McFarlane */

package cam.mcfarlane.toolbelt.tools.away;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Tracks all events needed for managing away times.
 */
public class AwayListener implements Listener {

  // Reference back to the parent tool
  private AwayTool m_tool;

  public AwayListener(AwayTool tool) {
    m_tool = tool;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    m_tool.resetAwayTime(event.getPlayer());
  }
  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    if (event.hasExplicitlyChangedPosition()) {
      if (m_tool.isPlayerAway(event.getPlayer())) {
        m_tool.updatePlayerStatus(event.getPlayer(), false);
      }
      m_tool.resetAwayTime(event.getPlayer());
    }
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    m_tool.removePlayer(event.getPlayer());
  }

}

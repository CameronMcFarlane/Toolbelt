/* Copyright (C) 2026 Cameron McFarlane */

package cam.mcfarlane.toolbelt.tools.away;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
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
  public void onPlayerMove(PlayerMoveEvent event) {
    String playerName = event.getPlayer().getName();
    if (m_tool.isPlayerAway(playerName)) {
      m_tool.updatePlayerStatus(playerName, false);
    }
    m_tool.resetAwayTime(playerName);
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    String playerName = event.getPlayer().getName();
    m_tool.removePlayer(playerName);
  }

  @EventHandler
  public void onPlayerFish(PlayerFishEvent event) {
    m_tool.sendAwayStatus(event.getPlayer());
  }

}

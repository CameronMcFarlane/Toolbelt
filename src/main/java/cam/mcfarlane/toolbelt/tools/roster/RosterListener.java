/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.roster;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cam.mcfarlane.toolbelt.tools.away.AwayEvent;

public class RosterListener implements Listener {

  private RosterTool m_tool;

  public RosterListener(RosterTool tool) {
    m_tool = tool;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    // Set header and footer for the player list
    event.getPlayer().sendPlayerListHeader(m_tool.getPlayerListHeader());
    event.getPlayer().sendPlayerListFooter(m_tool.getPlayerListFooter());

    // Add player to their default team
    m_tool.joinDefaultTeam(event.getPlayer());
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    // Remove the player from their team
    m_tool.leaveTeam(event.getPlayer());
  }

  /**
   * Listens for when a player's away status changes and updates
   * their team membership to/from the away team.
   * 
   * @param event The {@link AwayEvent} metadata.
   */
  @EventHandler
  public void onPlayerAway(AwayEvent event) {
    if (event.isPlayerAway()) {
      m_tool.joinTeam(event.getPlayer(), RosterTool.TEAM_AWAY);
    } else {
      m_tool.joinDefaultTeam(event.getPlayer());
    }
  }
  
}

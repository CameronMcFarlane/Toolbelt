/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.roster;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    
    // Override the default join message
    event.joinMessage(m_tool.getRandomJoinPhrase(event.getPlayer().getName()));
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    // Remove the player from their team
    m_tool.leaveTeam(event.getPlayer());

    // Override the default disconnect message
    event.quitMessage(m_tool.getRandomQuitPhrase(event.getPlayer().getName()));
  }
  
}

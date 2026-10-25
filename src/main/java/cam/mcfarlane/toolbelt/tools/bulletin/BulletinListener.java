/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.bulletin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listens to PlayerJoinEvent and sends the bulletin message to the player.
 */
public class BulletinListener implements Listener {

  private BulletinTool m_tool;

  public BulletinListener(BulletinTool tool) {
    m_tool = tool;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    m_tool.showTitle(event.getPlayer());
  }

}

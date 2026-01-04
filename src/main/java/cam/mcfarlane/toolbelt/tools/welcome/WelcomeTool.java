/* Copyright (C) 2026 Cameron McFarlane */

package cam.mcfarlane.toolbelt.tools.welcome;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;
import cam.mcfarlane.toolbelt.tools.Tool;

/**
 * A tool which displays a welcome message to the player upon connecting to the server.
 */
public class WelcomeTool implements Tool {

  private WelcomeListener m_listener = new WelcomeListener();

  @Override
  public void init(JavaPlugin plugin) {
    if (!ToolbeltConfiguration.WELCOME_ENABLED)
      return;
    
    Bukkit.getPluginManager().registerEvents(m_listener, plugin);
  }

  @Override
  public void shutDown() {
    // Nothing to shut down
  }
  
}

/* Copyright (C) 2026 Cameron McFarlane */

package cam.mcfarlane.toolbelt;

import org.bukkit.plugin.java.JavaPlugin;

import cam.mcfarlane.toolbelt.tools.Tool;
import cam.mcfarlane.toolbelt.tools.away.AwayTool;
import cam.mcfarlane.toolbelt.tools.welcome.WelcomeTool;

/**
 * The entry-point for the Toolbelt plugin.
 */
public class ToolbeltPlugin extends JavaPlugin {

  // A list of components which add functionality to the plugin
  private Tool[] m_tools = {
    new WelcomeTool(),
    new AwayTool()
  };

  @Override
  public void onLoad() {
    // TODO: Load plugin configuration
  }

  @Override
  public void onEnable() {
    for (Tool tool : m_tools) {
      tool.init(this);
    }
  }

  @Override
  public void onDisable() {
    for (Tool tool : m_tools) {
      tool.shutDown();
    }
  }

}

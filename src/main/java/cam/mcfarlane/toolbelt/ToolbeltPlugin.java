/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import cam.mcfarlane.toolbelt.tools.Tool;
import cam.mcfarlane.toolbelt.tools.away.AwayTool;
import cam.mcfarlane.toolbelt.tools.bulletin.BulletinTool;
import cam.mcfarlane.toolbelt.tools.dimension.DimensionTool;
import cam.mcfarlane.toolbelt.tools.roster.RosterTool;

/**
 * The entry-point for the Toolbelt plugin.
 */
public class ToolbeltPlugin extends JavaPlugin {

  // The plugin's Logger instance
  public static Logger LOGGER;

  // A list of components which add functionality to the plugin
  private final Tool[] m_toolbelt = {
    new AwayTool(this),
    new BulletinTool(this),
    new DimensionTool(this),
    new RosterTool(this)
  };

  @Override
  public void onLoad() {
    // TODO: Load plugin configuration
  }

  @Override
  public void onEnable() {
    PluginManager manager = Bukkit.getPluginManager();
    List<LiteralCommandNode<CommandSourceStack>> commandsToRegister = new ArrayList<>();
    LOGGER = this.getSLF4JLogger();

    for (Tool tool : m_toolbelt) {
      if (tool.isEnabled()) {
        tool.init();
        if (tool.hasListener())
          manager.registerEvents(tool.getListener(), this);
        if (tool.hasCommand())
          commandsToRegister.add(tool.getCommand());
      }
    }

    getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
      for (var command : commandsToRegister) {
        event.registrar().register(command);
      }
    });
  }

  @Override
  public void onDisable() {
    for (Tool tool : m_toolbelt) {
      tool.shutDown();
    }
  }

}

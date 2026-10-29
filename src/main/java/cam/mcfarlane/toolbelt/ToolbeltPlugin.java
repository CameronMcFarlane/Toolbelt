/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

  /*
   * Todo:
   * - Lock dimensions
   *   - Implement commands
   * - Event calendar
   *   - Hook into other tools, like dimensions
   *   - Play sounds when events start
   *   - Implement commands
   * - Bulletin
   *   - Server messages
   *   - Implement commands
   * - Away
   *   - Implement commands
   *   - Fix away/return triggers
   *     - punching/interacting should count as return trigger
   *     - being pushed should not count
   * - Chat
   *   - Override the default formatting of chat messages
   *   - Send sounds when a chat message is sent (can be disabled)
   *   - Implement commands
   */

  // A list of components which add functionality to the plugin
  private final Tool[] m_tools = {
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

    for (Tool tool : m_tools) {
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
    for (Tool tool : m_tools) {
      tool.shutDown();
    }
  }

  public <T extends Tool> Optional<T> getTool(Class<T> toolClass) {
    for (Tool tool : m_tools) {
      if (toolClass.isInstance(tool))
        return Optional.of(toolClass.cast(tool));
    }
    return Optional.empty();
  }

  public int getDaysSinceServerStart() {
    LocalDate now = LocalDate.now(ZoneId.of("Australia/Adelaide"));
    return (int) ChronoUnit.DAYS.between(ToolbeltConfiguration.SERVER_START_DATE, now) + 1;
  }

}

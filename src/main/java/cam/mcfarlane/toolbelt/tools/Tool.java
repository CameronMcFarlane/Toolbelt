/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools;

import org.bukkit.event.Listener;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;

import cam.mcfarlane.toolbelt.ToolbeltPlugin;

public abstract class Tool {

  protected ToolbeltPlugin m_plugin;
  protected Listener m_listener;
  protected ToolCommand m_command;

  public Tool() {}

  public Tool(ToolbeltPlugin plugin) {
    m_plugin = plugin;
  }
  
  /**
   * Initialisation logic for the tool.
   */
  public abstract void init();

  /**
   * Clean-up tasks to be executed on plugin shutdown.
   */
  public abstract void shutDown();

  /**
   * Determines whether to enable the plugin on start-up.
   * @return
   */
  public abstract boolean isEnabled();

  /**
   * @return a boolean which determines if a {@link Listener} has been initialised.
   */
  public boolean hasListener() {
    return m_listener != null;
  }

  /**
   * @return this tool's {@link Listener}.
   */
  public Listener getListener() {
    return m_listener;
  }

  /**
   * @return a boolean which determines if a {@link ToolCommand} has been initialised.
   */
  public boolean hasCommand() {
    return m_command != null;
  }

  /**
   * @return this tool's {@link ToolCommand}.
   */
  public LiteralCommandNode<CommandSourceStack> getCommand() {
    return m_command.create();
  }

}

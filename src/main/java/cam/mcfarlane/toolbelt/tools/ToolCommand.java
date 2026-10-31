/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;

/**
 * Basic requirement for implementing a tool command.
 */
public abstract class ToolCommand {

  /**
   * Builds and returns a command structure to interact with this tool.
   * @return the parent node containing the command tree.
   */
  public abstract LiteralCommandNode<CommandSourceStack> create();
  
}

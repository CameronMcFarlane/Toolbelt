/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.away;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import cam.mcfarlane.toolbelt.tools.ToolCommand;

/**
 * A command for managing away status and checking the status of others.
 */
public class AwayCommand extends ToolCommand {

  AwayTool m_tool;

  public AwayCommand(AwayTool tool) {
    m_tool = tool;
  }
  
  @Override
  public LiteralCommandNode<CommandSourceStack> create() {
    return Commands.literal("away")
      .executes(context -> executeAway(context))
      .then(Commands.literal("list")
        .executes(context -> executeList(context)))
      .build();
  }

  /**
   * Sets the executor's status to away.
   * 
   * @param context metadata of the executed command.
   * @return 1 if the command execution was successful.
   */
  private int executeAway(CommandContext<CommandSourceStack> context) {
    CommandSender sender = context.getSource().getSender();
    Entity executor = context.getSource().getExecutor();
    if (executor instanceof Player player) {
      if (m_tool.isPlayerAway(player)) {
        sender.sendMessage(Component.text("You're already away, you turnip."));
      } else {
        m_tool.setPlayerAway(player);
      }
    } else {
      sender.sendMessage(Component.text("You are omnipresent."));
    }
    return Command.SINGLE_SUCCESS;
  }

  /**
   * Provides the away status of each connected player.
   * 
   * @param context metadata of the executed command.
   * @return 1 if the command execution was successful.
   */
  private int executeList(CommandContext<CommandSourceStack> context) {
    CommandSender sender = context.getSource().getSender();
    m_tool.sendAwayStatus(sender);
    return Command.SINGLE_SUCCESS;
  }

}

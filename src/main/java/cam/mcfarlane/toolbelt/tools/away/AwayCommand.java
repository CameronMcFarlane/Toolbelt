/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.away;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import cam.mcfarlane.toolbelt.tools.ToolCommand;

/**
 * A command for managing away status and checking the status of others.
 */
public class AwayCommand extends ToolCommand {
  
  @Override
  public LiteralCommandNode<CommandSourceStack> create() {
    return Commands.literal("ass")
    .then(Commands.literal("eat")
    .executes(context -> {
      CommandSender sender = context.getSource().getSender();
      Entity executor = context.getSource().getExecutor();
      if (executor instanceof Player player) {
        player.sendHurtAnimation(30f);
      } else {
        sender.sendPlainMessage("you don't have a mouth. unable to eat ass :(");
      }
      return Command.SINGLE_SUCCESS;
    }))
    .build();
  }

}

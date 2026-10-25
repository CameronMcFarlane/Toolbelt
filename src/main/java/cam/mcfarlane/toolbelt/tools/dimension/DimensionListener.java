/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.dimension;

import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;

import net.kyori.adventure.text.Component;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor. *;
import static net.kyori.adventure.text.format.TextDecoration.*;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;

/**
 * Listens to dimension-related events.
 */
public class DimensionListener implements Listener {

  public final Component m_netherText = text("The Nether hasn't opened yet...").color(RED).decorate(ITALIC);
  public final Component m_endText = text("The End hasn't opened yet...").color(LIGHT_PURPLE).decorate(ITALIC);

  @EventHandler
  public void onUsePortal(PlayerPortalEvent event) {
    Environment envTo = event.getTo().getWorld().getEnvironment();

    if (!ToolbeltConfiguration.DIMENSION_NETHER_ENABLED && envTo == Environment.NETHER) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(m_netherText);
    }

    if (!ToolbeltConfiguration.DIMENSION_END_ENABLED && envTo == Environment.THE_END) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(m_endText);
    }
  }

  @EventHandler
  public void onCreatePortal(PortalCreateEvent event) {
    if (!ToolbeltConfiguration.DIMENSION_NETHER_ENABLED && event.getReason() == CreateReason.FIRE) {
      event.setCancelled(true);
      Entity creator = event.getEntity();
      if (creator != null && creator instanceof Player player) {
        player.sendMessage(m_netherText);
      }
    }
  }

  @EventHandler
  public void onEndPortalFrameInteract(PlayerInteractEvent event) {
    if (!ToolbeltConfiguration.DIMENSION_END_ENABLED && event.getAction().isRightClick()) {
      Block clickedBlock = event.getClickedBlock();
      if (Objects.nonNull(clickedBlock) && clickedBlock.getType() == Material.END_PORTAL_FRAME) {
        event.setCancelled(true);
        event.getPlayer().sendMessage(m_endText);
      }
    }
  }

}

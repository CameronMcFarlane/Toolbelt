/* Copyright (C) 2026 Cameron McFarlane */

package cam.mcfarlane.toolbelt.tools.welcome;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * Listens to PlayerJoinEvent and sends a message to the player.
 */
public class WelcomeListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    TextComponent greeting = Component.text("Welcome to Faffin' About!", Style.style(TextDecoration.BOLD));
    TextComponent websiteDetails = Component.text("Visit https://mcfarlane.cam for client set-up instructions.");

    event.getPlayer().sendMessage(greeting);
    event.getPlayer().sendMessage(websiteDetails);
  }

}

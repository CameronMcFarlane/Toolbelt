/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.bulletin;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;

import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;
import cam.mcfarlane.toolbelt.ToolbeltPlugin;
import cam.mcfarlane.toolbelt.tools.Tool;

/**
 * A tool which displays a welcome message to the player upon connecting
 * and formats the scoreboard screen.
 */
public class BulletinTool extends Tool {

  private static final Logger LOG = LoggerFactory.getLogger(BulletinTool.class);

  public BulletinTool(ToolbeltPlugin plugin) {
    super(plugin);
    m_listener = new BulletinListener(this);
  }

  @Override
  public void init() {
    LOG.info("Initialising the Bulletin tool...");
    // Nothing to init
  }

  @Override
  public void shutDown() {
    // Nothing to shut down
  }

  @Override
  public boolean isEnabled() {
    return ToolbeltConfiguration.BULLETIN_ENABLED;
  }

  public void showTitle(Player player) {
    TextComponent title = text(ToolbeltConfiguration.SERVER_NAME).color(GREEN).decorate(BOLD);
    TextComponent subtitle = text("Day %d".formatted(m_plugin.getDaysSinceServerStart()));

    player.showTitle(Title.title(title, subtitle));
  }
  
}

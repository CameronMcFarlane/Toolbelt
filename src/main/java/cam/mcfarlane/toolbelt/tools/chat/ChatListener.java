package cam.mcfarlane.toolbelt.tools.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ChatListener implements Listener {

  ChatTool m_tool;

  public ChatListener(ChatTool tool) {
    m_tool = tool;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    // Override the default join message
    event.joinMessage(m_tool.getRandomJoinPhrase(event.getPlayer().getName()));
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    // Override the default disconnect message
    event.quitMessage(m_tool.getRandomQuitPhrase(event.getPlayer().getName()));
  }

  @EventHandler
  public void onPlayerChat(AsyncChatEvent event) {
    event.renderer((source, sourceDisplayName, message, viewer) -> {
      return sourceDisplayName
        .decorate(TextDecoration.BOLD)
        .append(Component.text(" • ").color(NamedTextColor.GOLD))
        .append(message.color(NamedTextColor.WHITE));
    });
  }
  
}

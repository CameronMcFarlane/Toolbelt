package cam.mcfarlane.toolbelt.tools.chat;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;
import net.kyori.adventure.text.Component;

import cam.mcfarlane.toolbelt.tools.Tool;

public class ChatTool extends Tool {

  private String[] m_joinPhrases = {
    "has joined!",
    "has graced us with their presence!",
    "has succumbed to the craft!",
  };

  private String[] m_quitPhrases = {
    "has left.",
    "has disconnected.",
    "has abandoned us all.",
    "has gone to get milk.",
    "will come back. They promised."
  };

  public ChatTool() {
    m_listener = new ChatListener(this);
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void init() {
  }

  @Override
  public void shutDown() {
  }

  public Component getRandomJoinPhrase(String playerName) {
    String phrase = m_joinPhrases[(int) Math.round(Math.random() * (m_joinPhrases.length - 1))];
    return text(playerName)
      .decorate(BOLD)
      .color(GOLD)
      .appendSpace()
      .append(text(phrase).decoration(BOLD, false));
  }

  public Component getRandomQuitPhrase(String playerName) {
    String phrase = m_quitPhrases[(int) Math.round(Math.random() * (m_quitPhrases.length - 1))];
    return text(playerName)
      .decorate(BOLD)
      .color(GOLD)
      .appendSpace()
      .append(text(phrase).decoration(BOLD, false));
  }

}

package cam.mcfarlane.toolbelt.tools.away;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * An event triggered when a player's away status changes.
 */
public class AwayEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();
  
  private Player m_player;
  private boolean m_isAway;

  public AwayEvent(Player player, boolean isAway) {
    m_player = player;
    m_isAway = isAway;
  }

  /**
   * @return the {@link Player} who's away status has changed.
   */
  public Player getPlayer() {
    return m_player;
  }

  /**
   * @return true if the player is away, false if they're back.
   */
  public boolean isPlayerAway() {
    return m_isAway;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }
  
}

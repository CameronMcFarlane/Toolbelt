/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.roster;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.*;
import net.kyori.adventure.text.Component;

import cam.mcfarlane.toolbelt.ToolbeltConfiguration;
import cam.mcfarlane.toolbelt.ToolbeltPlugin;
import cam.mcfarlane.toolbelt.tools.Tool;
import cam.mcfarlane.toolbelt.utils.Utilities;

public class RosterTool extends Tool {

  // Team identifiers
  protected static final String TEAM_PLAYER = "toolbelt_player";
  protected static final String TEAM_ADMIN = "toolbelt_admin";
  protected static final String TEAM_AWAY = "toolbelt_away";
  
  // Objective identifiers
  protected static final String OBJECTIVE_HEALTH = "toolbelth_health";

  Scoreboard m_scoreboard;

  private String[] m_teamNames = {
    TEAM_PLAYER,
    TEAM_ADMIN,
    TEAM_AWAY
  };

  @Override
  public void init() {
    ToolbeltPlugin.LOGGER.info("Initialising the Roster tool...");
    m_listener = new RosterListener(this);
    m_scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    // Register each of the teams
    for (String teamName : m_teamNames) {
      registerTeam(teamName);
    }

    // Configure health to be shown in the player list, if configured
    Objective health = m_scoreboard.getObjective(OBJECTIVE_HEALTH);
    if (Objects.nonNull(health)) {
      health.unregister();
    }
    if (ToolbeltConfiguration.ROSTER_HEARTS) {
      health = m_scoreboard.registerNewObjective(OBJECTIVE_HEALTH, Criteria.HEALTH, text("Health"));
      health.setDisplaySlot(DisplaySlot.PLAYER_LIST);
      health.setRenderType(RenderType.HEARTS);
    }
  }

  @Override
  public void shutDown() {
    // Unregister each of the teams
    for (String teamName : m_teamNames) {
      Team team = m_scoreboard.getTeam(teamName);
      if (Objects.nonNull(team)) {
        team.unregister();
      }
    }
  }

  @Override
  public boolean isEnabled() {
    return ToolbeltConfiguration.ROSTER_ENABLED;
  }

  /**
   * Gets a {@link Team} from the {@link Scoreboard} with the given name, 
   * registering it if it does not exist.
   * 
   * @param teamName the name of the {@link Team} to fetch.
   * @return a {@link Team} with the given name.
   */
  public Team getTeam(String teamName) {
    Team team = m_scoreboard.getTeam(teamName);
    if (Objects.isNull(team)) {
      team = registerTeam(teamName);
    }
    return team;
  }

  public void joinDefaultTeam(Player player) {
    joinTeam(player, player.isOp() ? TEAM_ADMIN : TEAM_PLAYER);
  }

  public void joinTeam(Player player, String teamName) {
    getTeam(teamName).addPlayer(player);
  }

  public void leaveTeam(Player player) {
    Team team = m_scoreboard.getPlayerTeam(player.getPlayer());
    if (Objects.nonNull(team)) {
      team.removePlayer(player.getPlayer());
    }
  }

  private Team registerTeam(String teamName) {
    Team team = m_scoreboard.getTeam(teamName);
    if (Objects.nonNull(team)) {
      team.unregister();
    }
    team = m_scoreboard.registerNewTeam(teamName);
    switch (teamName) {
      case TEAM_AWAY:
        team.color(GRAY);
        team.prefix(text("[💤] "));
        break;
      case TEAM_ADMIN:
        team.color(GOLD);
        break;
    }
    return team;
  }

  public Component getPlayerListHeader() {
    return text(ToolbeltConfiguration.SERVER_NAME)
      .decorate(BOLD)
      .color(GREEN)
      .appendSpace()
      .append(
        text("• Day %d".formatted(Utilities.getDaysSinceServerStart()))
        .color(WHITE)
        .decoration(BOLD, false))
      .appendNewline();
  }

  public Component getPlayerListFooter() {
    return text(" ");
  }
  
}

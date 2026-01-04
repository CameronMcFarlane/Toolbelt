/* Copyright (C) 2026 Cameron McFarlane */

package cam.mcfarlane.toolbelt.tools;

import org.bukkit.plugin.java.JavaPlugin;

public interface Tool {
  
  public void init(JavaPlugin plugin);
  public void shutDown();

}

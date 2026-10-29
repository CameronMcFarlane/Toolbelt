/* Copyright (C) 2026 Cameron McFarlane */
package cam.mcfarlane.toolbelt.tools.dimension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cam.mcfarlane.toolbelt.ToolbeltPlugin;
import cam.mcfarlane.toolbelt.tools.Tool;

/**
 * Provides functionality for guarding access to dimensions, such as the Nether, End, or even the Overworld.
 */
public class DimensionTool extends Tool {

  private static final Logger LOG = LoggerFactory.getLogger(DimensionTool.class);

  public DimensionTool(ToolbeltPlugin plugin) {
    super(plugin);
  }

  @Override
  public void init() {
    LOG.info("Initialising the Dimension tool...");
    this.m_listener = new DimensionListener();
  }

  @Override
  public void shutDown() {
    // No shut-down
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}

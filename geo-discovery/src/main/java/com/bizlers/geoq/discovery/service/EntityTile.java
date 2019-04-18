package com.bizlers.geoq.discovery.service;

import java.util.ArrayList;
import java.util.List;

import com.bizlers.geoquotient.utils.Tile;
import com.jcabi.aspects.Loggable;
import com.jcabi.log.Logger;

public class EntityTile extends Tile {

	private List<Object> resources;

	protected EntityTile(Tile tile) {
		super(tile.getTileXY());
	}

	@Loggable(Loggable.TRACE)
	public synchronized void resourceEnters(Object resourceId) {
		if (resources == null) {
			resources = new ArrayList<>();
		}
		resources.add(resourceId);
	}

	@Loggable(Loggable.TRACE)
	public synchronized void resourceLeaves(Object resourceId) {
		if (resources != null) {
			resources.remove(resourceId);
		}
	}

	/**
	 * @return List of ids of resources in the Tile.
	 */
	@Loggable(Loggable.TRACE)
	public List<Object> getResourceIds() {
		return resources;
	}

	/**
	 * @return Total number of resources in the Tile
	 */
	@Loggable(Loggable.TRACE)
	public int getResourcesCount() {
		if (resources == null) {
			return 0;
		} else {
			return resources.size();
		}
	}

	@Loggable(Loggable.TRACE)
	public boolean contains(Object resourceId) {
		if (getResourcesCount() != 0) {
			return resources.contains(resourceId);
		}
		return false;
	}

	@Loggable(Loggable.TRACE)
	public void resourceMoves(Object resourceId) {
		Logger.debug(this, "Resource %s moves in %s tile", resourceId, this);
	}
}

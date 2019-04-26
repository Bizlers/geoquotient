package com.bizlers.geoq.discovery.service;

import com.bizlers.geoquotient.utils.GeoLocation;
import com.bizlers.geoquotient.utils.GeoProjection;
import com.bizlers.geoquotient.utils.Point;
import com.bizlers.geoquotient.utils.Tile;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Use this class to obtain entity tiles
 * 
 * @author Pawan D
 * 
 */
@Component
@PropertySource("classpath:geo-discovery.properties")
public class EntityTileProvider {

	/*
	 * The links below help determining desired zoom levels:
	 * 
	 * 1. Tiles la Google Maps: Coordinates, Tile Bounds and Projection
	 * http://www.maptiler.org/google-maps-coordinates-tile-bounds-projection/
	 * 
	 * 2. Coordinate systems for Google Maps
	 * http://facstaff.unca.edu/mcmcclur/GoogleMaps
	 * /Projections/GoogleCoords.html
	 */

	// TODO REPLACE registry MAP WITH CACHING FRAMEWORK TO IMPROVE SCALABILITY
	//
	// We have to implement caching instead of storing Tiles unlimited
	// number of times; The Caching framework should deallocated tiles with some
	// LRU, MRU approaches.

	public static final int POS_NORTH_WEST = 0;

	public static final int POS_NORTH = 1;

	public static final int POS_NORTH_EAST = 2;

	public static final int POS_WEST = 3;

	public static final int POS_EAST = 4;

	public static final int POS_SOUTH_WEST = 5;

	public static final int POS_SOUTH = 6;

	public static final int POS_SOUTH_EAST = 7;

	private TileRegistry tileRegistry = new TileRegistry();

	@Value("${tilingDepth}")
	private int tilingDepth;

	@Loggable(Loggable.TRACE)
	public EntityTile getEntityTile(GeoLocation geoLocation) {
		if (geoLocation == null) {
			return null;
		}
		GeoProjection projection = new GeoProjection(tilingDepth);
		Point tileXY = projection
				.getTileXY(new GeoLocation(geoLocation
						.getLatitude(), geoLocation.getLongitude(), geoLocation
						.getAccuracy()));
		EntityTile entityTile = tileRegistry.getTile(tileXY);
		return entityTile;
	}

	@Loggable(Loggable.TRACE)
	public EntityTile[] getSurroundingTiles(Tile tile) {
		EntityTile[] tiles = new EntityTile[8];
		long x = tile.getTileXY().x;
		long y = tile.getTileXY().y;

		// Locate nearby 8 tiles in 8 directions
		// +----------+----------+----------+
		// |....NW....|....N.....|....NE....|
		// |(x-1, y-1)|(x,y-1)...|(x+1, y-1)|
		// |..........|..........|..........|
		// +----------+----------+----------+
		// |....W.....|..........|....E.....|
		// |(x-1, y)..|..(x, y)..|(x+1, y)..|
		// |..........|..........|..........|
		// +----------+----------+----------+
		// |....SW....|....S.....|....SE....|
		// |(x-1, y+1)|(x, y+1)..|(x+1, y+1)|
		// |..........|..........|..........|
		// +----------+----------+----------+

		Point[] points = new Point[8];

		points[POS_NORTH_WEST] = new Point(x - 1, y - 1);

		points[POS_NORTH] = new Point(x, y - 1);

		points[POS_NORTH_EAST] = new Point(x + 1, y - 1);

		points[POS_WEST] = new Point(x - 1, y);

		points[POS_EAST] = new Point(x + 1, y);

		points[POS_SOUTH_WEST] = new Point(x - 1, y + 1);

		points[POS_SOUTH] = new Point(x, y + 1);

		points[POS_SOUTH_EAST] = new Point(x + 1, y + 1);

		int pos = 0;
		for (Point p : points) {
			if (!tileRegistry.containsKey(p)) {
				tileRegistry.putTile(p);
			}
			tiles[pos++] = tileRegistry.getTile(p);
		}
		return tiles;
	}

	/**
	 * Wrapper over the tileRegistry map
	 * 
	 * @author Pawan D
	 * 
	 */
	private static class TileRegistry {

		private Map<Point, EntityTile> registry;

		protected TileRegistry() {
			registry = new ConcurrentHashMap<Point, EntityTile>(1000);
		}

		@Loggable(Loggable.TRACE)
		public EntityTile putTile(Point p) {
			EntityTile entityTile = putTile(new Tile(p));
			return entityTile;
		}

		@Loggable(Loggable.TRACE)
		public EntityTile putTile(Tile tile) {
			EntityTile entityTile = new EntityTile(tile);
			registry.put(tile.getTileXY(), entityTile);
			return entityTile;
		}

		@Loggable(Loggable.TRACE)
		public EntityTile getTile(Point tileKey) {
			EntityTile entityTile = null;
			if (registry.containsKey(tileKey)) {
				entityTile = registry.get(tileKey);
			} else {
				entityTile = putTile(new Tile(tileKey));
			}
			return entityTile;
		}

		@Loggable(Loggable.TRACE)
		public boolean containsKey(Point tileKey) {
			boolean flag = registry.containsKey(tileKey);
			return flag;
		}
	}
}

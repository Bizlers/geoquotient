package com.bizlers.geoquotient.utils;

import java.io.Serializable;

public class Tile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Point tileXY;

	protected Tile() {

	}

	public Tile(Point tileXY) {
		this.tileXY = tileXY;
	}

	public Point getUpperLeftPoint() {
		return new Point(256 * tileXY.x, 256 * tileXY.y);
	}

	public Point getLowerRightPoint() {
		return new Point((256 * (tileXY.x + 1)), (256 * (tileXY.y + 1)));
	}

	public Point getTileXY() {
		return tileXY;
	}

	@Override
	public final String toString() {
		return tileXY.x + "_" + tileXY.y;
	}

	@Override
	public boolean equals(Object tileObject) {
		if (tileObject instanceof Tile) {
			Tile tile = (Tile) tileObject;
			return (tile.tileXY.x == tileXY.x && tile.tileXY.y == tileXY.y);
		} else {
			return false;
		}
	}
}
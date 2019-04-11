package com.bizlers.geoquotient.utils;

import java.io.Serializable;

public class Point implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public long x;
	public long y;

	Point() {

	}

	public Point(long x, long y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return x + ", " + y;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object p) {
		if (p != null && p instanceof Point) {
			return (((Point) p).x == x && ((Point) p).y == y);
		}
		return false;
	}
}

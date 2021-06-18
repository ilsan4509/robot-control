package org.robot.project.map;

public class MapSizeVO {
	int maxX;
	int maxY;
	public int getMaxX() {
		return maxX;
	}
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	public int getMaxY() {
		return maxY;
	}
	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	@Override
	public String toString() {
		return "MapSizeVO [maxX=" + maxX + ", maxY=" + maxY + "]";
	}
}

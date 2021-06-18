package org.robot.project.map;

public class MapViewVO {
	int map_seq;
	int store_seq;
	int node_id;
	int node_x;
	int node_y;
	public int getMap_seq() {
		return map_seq;
	}
	public void setMap_seq(int map_seq) {
		this.map_seq = map_seq;
	}
	public int getStore_seq() {
		return store_seq;
	}
	public void setStore_seq(int store_seq) {
		this.store_seq = store_seq;
	}
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public int getNode_x() {
		return node_x;
	}
	public void setNode_x(int node_x) {
		this.node_x = node_x;
	}
	public int getNode_y() {
		return node_y;
	}
	public void setNode_y(int node_y) {
		this.node_y = node_y;
	}
	@Override
	public String toString() {
		return "MapViewVO [map_seq=" + map_seq + ", store_seq=" + store_seq + ", node_id=" + node_id + ", node_x=" + node_x + ", node_y=" + node_y + "]";
	}
}

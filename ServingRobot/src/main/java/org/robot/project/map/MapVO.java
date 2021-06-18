package org.robot.project.map;

public class MapVO {
	private int id;
	private int stem;
	private int existence;
	private int node_l;
	private int node_r;
	private int node_up;
	private int node_down;
	private int map_controller_seq;
	private int store_seq;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStem() {
		return stem;
	}
	public void setStem(int stem) {
		this.stem = stem;
	}
	public int getExistence() {
		return existence;
	}
	public void setExistence(int existence) {
		this.existence = existence;
	}
	public int getNode_l() {
		return node_l;
	}
	public void setNode_l(int node_l) {
		this.node_l = node_l;
	}
	public int getNode_r() {
		return node_r;
	}
	public void setNode_r(int node_r) {
		this.node_r = node_r;
	}
	public int getNode_up() {
		return node_up;
	}
	public void setNode_up(int node_up) {
		this.node_up = node_up;
	}
	public int getNode_down() {
		return node_down;
	}
	public void setNode_down(int node_down) {
		this.node_down = node_down;
	}
	public int getMap_controller_seq() {
		return map_controller_seq;
	}
	public void setMap_controller_seq(int map_controller_seq) {
		this.map_controller_seq = map_controller_seq;
	}
	public int getStore_seq() {
		return store_seq;
	}
	public void setStore_seq(int store_seq) {
		this.store_seq = store_seq;
	}
	
	
	
	@Override
	public String toString() {
		return "MapVO [id=" + id + ", stem=" + stem + ", existence=" + existence + ", node_l=" + node_l + ", node_r="
				+ node_r + ", node_up=" + node_up + ", node_down=" + node_down +", map_controller_seq="+ map_controller_seq + ", store_seq=" + store_seq+ "]";
	}
}

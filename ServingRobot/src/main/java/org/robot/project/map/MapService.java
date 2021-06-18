package org.robot.project.map;

import java.util.List;


public interface MapService {
	List<MapVO> getMapGrid(MapVO vo);
	List<MapColumnsVO> getMapColumns(MapColumnsVO vo);
	List<MapViewVO> getMapView(int store_seq);
	void modiMap1(MapVO vo);
	void modiMap2(MapVO vo);
	void modiMap3(MapVO vo);
	void modiMap4(MapVO vo);
	void modiMap5(MapVO vo);
	int getMaxNodeX(int store_seq);
	int getMaxNodeY(int store_seq);
}

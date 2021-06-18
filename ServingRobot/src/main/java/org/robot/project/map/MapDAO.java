package org.robot.project.map;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("mapDAO")
public class MapDAO {
	@Autowired
	SqlSessionTemplate mySQL;
	
	private String loc = "org.robot.project.MapMapper.";
	
	public List<MapVO> getMapGrid(MapVO vo) {
		return mySQL.selectList(loc+"getMapGrid", vo);
	}
	
	public List<MapColumnsVO> getMapColumns(MapColumnsVO vo) {
		return mySQL.selectList(loc+"getMapColumns", vo);
	}
	
	public List<MapViewVO> getMapView(int store_seq) {
		return mySQL.selectList(loc+"getMapView", store_seq);
	}
	
	public void modiMap1(MapVO vo) {
		mySQL.update(loc+"modiMap1", vo);
	}
	
	public void modiMap2(MapVO vo) {
		mySQL.update(loc+"modiMap2", vo);
	}
	
	public void modiMap3(MapVO vo) {
		mySQL.update(loc+"modiMap3", vo);
	}
	
	public void modiMap4(MapVO vo) {
		mySQL.update(loc+"modiMap4", vo);
	}
	
	public void modiMap5(MapVO vo) {
		mySQL.update(loc+"modiMap5", vo);
	}
	
	public int getMaxNodeX(int store_seq) {
		return mySQL.selectOne(loc + "getMaxNodeX", store_seq);
	}
	
	public int getMaxNodeY(int store_seq) {
		return mySQL.selectOne(loc + "getMaxNodeY", store_seq);
	}
	
}

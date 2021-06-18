package org.robot.project.map;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MapService")
public class MapServiceImpl implements MapService {
	@Autowired
	private MapDAO mapDAO;

	@Override
	public List<MapVO> getMapGrid(MapVO vo) {
		return mapDAO.getMapGrid(vo);
	}

	@Override
	public List<MapColumnsVO> getMapColumns(MapColumnsVO vo) {
		return mapDAO.getMapColumns(vo);
	}
	
	@Override
	public List<MapViewVO> getMapView(int store_seq) {
		return mapDAO.getMapView(store_seq);
	}

	@Override
	public void modiMap1(MapVO vo) {
		mapDAO.modiMap1(vo);
	}

	@Override
	public void modiMap2(MapVO vo) {
		mapDAO.modiMap2(vo);
	}

	@Override
	public void modiMap3(MapVO vo) {
		mapDAO.modiMap3(vo);
	}

	@Override
	public void modiMap4(MapVO vo) {
		mapDAO.modiMap4(vo);
	}

	@Override
	public void modiMap5(MapVO vo) {
		mapDAO.modiMap5(vo);
	}

	@Override
	public int getMaxNodeX(int store_seq) {
		return mapDAO.getMaxNodeX(store_seq);
	}

	@Override
	public int getMaxNodeY(int store_seq) {
		return mapDAO.getMaxNodeY(store_seq);
	}

}

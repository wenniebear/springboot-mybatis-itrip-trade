package com.boot.service;

import com.boot.po.ItripHotelRoom;
import com.boot.util.ItripHotelRoomVO;

import java.util.List;
import java.util.Map;

public interface ItripHotelRoomService {
    public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String, Object> param)throws Exception;
    public ItripHotelRoom getItripHotelRoomById(Long id) throws Exception;
}

package com.boot.service;

import com.boot.mapper.ItripHotelRoomMapper;
import com.boot.po.ItripHotelRoom;
import com.boot.util.ItripHotelRoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItripHotelRoomServiceImp implements  ItripHotelRoomService {
    @Autowired
    private ItripHotelRoomMapper itripHotelRoomMapper;

    public ItripHotelRoomMapper getItripHotelRoomMapper() {
        return itripHotelRoomMapper;
    }

    public void setItripHotelRoomMapper(ItripHotelRoomMapper itripHotelRoomMapper) {
        this.itripHotelRoomMapper = itripHotelRoomMapper;
    }

    @Override
    public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String, Object> param) throws Exception {
        return itripHotelRoomMapper.getItripHotelRoomListByMap(param);
    }

    @Override
    public ItripHotelRoom getItripHotelRoomById(Long id) throws Exception {
        return itripHotelRoomMapper.getItripHotelRoomById(id);
    }
}

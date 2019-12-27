package com.boot.service;

import com.boot.util.StoreVO;

import java.util.List;
import java.util.Map;

public interface ItripHotelTempStoreService {
    public StoreVO queryRoomStore(Map<String, Object> param) throws Exception;
    public Integer getTempStore(Map<String, Object> param) throws Exception;
    public Integer getItripHotelTempStoreCountByMap(Map<String, Object> param)throws Exception;
    public boolean vaildateRoomStore(Map<String ,Object> param) throws Exception;
    public Integer updateRoomStore(Map<String, Object> param)throws Exception;
}

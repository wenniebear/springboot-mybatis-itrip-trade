package com.boot.service;

import com.boot.mapper.ItripHotelTempStoreMapper;
import com.boot.util.EmptyUtils;
import com.boot.util.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItripHotelTempStoreServiceImp implements  ItripHotelTempStoreService {
    @Autowired
    private ItripHotelTempStoreMapper itripHotelTempStoreMapper;

    public ItripHotelTempStoreMapper getItripHotelTempStoreMapper() {
        return itripHotelTempStoreMapper;
    }

    public void setItripHotelTempStoreMapper(ItripHotelTempStoreMapper itripHotelTempStoreMapper) {
        this.itripHotelTempStoreMapper = itripHotelTempStoreMapper;
    }

    @Override
    public StoreVO queryRoomStore(Map<String, Object> param) throws Exception {
        return itripHotelTempStoreMapper.queryRoomStore(param);
    }

    @Override
    public Integer getTempStore(Map<String, Object> param) throws Exception {
        return itripHotelTempStoreMapper.getTempStore(param);
    }

    @Override
    public Integer getItripHotelTempStoreCountByMap(Map<String, Object> param) throws Exception {
        Integer store=itripHotelTempStoreMapper.getItripHotelTempStoreCountByMap(param);
        return store;
    }

    @Override
    public boolean vaildateRoomStore(Map<String, Object> param) throws Exception {
        Integer count=(Integer)param.get("count");//获取需要预定数量
        System.out.println("需要预定数量："+count);
        Integer store=itripHotelTempStoreMapper.getTempStore(param);
        System.out.println("2Store:"+store);
        if (EmptyUtils.isEmpty(store)){
            return false;
        }
        if (count<store)return true;
        return true;
    }

    @Override
    public Integer updateRoomStore(Map<String, Object> param) throws Exception {
        return itripHotelTempStoreMapper.updateRoomStore(param);
    }
}

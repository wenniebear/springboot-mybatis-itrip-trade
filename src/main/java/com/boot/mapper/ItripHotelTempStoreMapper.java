package com.boot.mapper;

import com.boot.po.ItripHotelTempStore;
import com.boot.util.StoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
@Mapper
public interface ItripHotelTempStoreMapper {

	public ItripHotelTempStore getItripHotelTempStoreById(@Param(value = "id") Long id)throws Exception;

	public List<ItripHotelTempStore>  getItripHotelTempStoreListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripHotelTempStoreCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripHotelTempStore(ItripHotelTempStore itripHotelTempStore)throws Exception;

	public Integer updateItripHotelTempStore(ItripHotelTempStore itripHotelTempStore)throws Exception;

	public Integer deleteItripHotelTempStoreById(@Param(value = "id") Long id)throws Exception;

	public StoreVO queryRoomStore(Map<String, Object> param) throws Exception;

	public void flushStore(Map<String, Object> param)throws Exception;

	public Integer updateRoomStore(Map<String, Object> param)throws Exception;

	public Integer getTempStore(Map<String, Object> param)throws Exception;
}

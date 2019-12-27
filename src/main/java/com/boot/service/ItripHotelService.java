package com.boot.service;

import com.boot.po.ItripHotel;
import com.boot.util.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItripHotelService {
    public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception;
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(@Param(value = "id") Long id) throws Exception;
    public ItripSearchPolicyHotelVO queryHotelPolicy(@Param(value = "id") Long id) throws Exception;
    public HotelVideoDescVO getVideoDescByHotelId(Long id)throws Exception;
    public ItripHotel getItripHotelById(@Param(value = "id") Long id)throws Exception;

}

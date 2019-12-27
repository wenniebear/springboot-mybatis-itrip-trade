package com.boot.service;

import com.boot.util.ItripListCommentVO;
import com.boot.util.ItripScoreCommentVO;
import com.boot.util.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItripCommonService {
    public ItripScoreCommentVO getCommentAvgScore(@Param(value = "hotelId") Long hotelId) throws Exception;
    public Integer getItripCommentCountByMap(Map<String, Object> param)throws Exception;
    public Page<ItripListCommentVO> getItripCommentListByMap(Map<String,Object> param,Integer pageNo,Integer pageSize) throws Exception;
}

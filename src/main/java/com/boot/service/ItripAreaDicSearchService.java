package com.boot.service;

import com.boot.po.ItripAreaDic;
import com.boot.util.ItripAreaDicVO;

import java.util.List;
import java.util.Map;

public interface ItripAreaDicSearchService {
    public List<ItripAreaDic> getItripAreaDicListByMap(Map<String,Object>  param)throws Exception;
    /*public List<ItripAreaDic> queryTradeArea(Long id)throws Exception;*/
}

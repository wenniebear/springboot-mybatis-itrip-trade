package com.boot.service;

import com.boot.po.ItripLabelDic;
import com.boot.util.ItripLabelDicVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItripLableDicService {
    public List<ItripLabelDic> getItripLabelDicListByMap(Map<String,Object> param) throws Exception;
    public List<ItripLabelDicVO> getItripLabelDicByParentId(@Param(value = "parentId") Long parentId)throws Exception;
}

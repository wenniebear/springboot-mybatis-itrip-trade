package com.boot.service;

import com.boot.mapper.ItripAreaDicMapper;
import com.boot.po.ItripAreaDic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItripAreadicSearchServiceImp implements ItripAreaDicSearchService {
    @Autowired
    private ItripAreaDicMapper itripAreaDicMapper;

    public ItripAreaDicMapper getItripAreaDicMapper() {
        return itripAreaDicMapper;
    }

    public void setItripAreaDicMapper(ItripAreaDicMapper itripAreaDicMapper) {
        this.itripAreaDicMapper = itripAreaDicMapper;
    }

    @Override
    public List<ItripAreaDic> getItripAreaDicListByMap(Map<String, Object> param) throws Exception {
        return itripAreaDicMapper.getItripAreaDicListByMap(param);
    }
}

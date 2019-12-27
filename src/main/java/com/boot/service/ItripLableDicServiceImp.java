package com.boot.service;

import com.boot.mapper.ItripLabelDicMapper;
import com.boot.po.ItripLabelDic;
import com.boot.util.ItripLabelDicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItripLableDicServiceImp implements ItripLableDicService {
    @Autowired
    private ItripLabelDicMapper itripLabelDicMapper;

    public ItripLabelDicMapper getItripLabelDicMapper() {
        return itripLabelDicMapper;
    }

    public void setItripLabelDicMapper(ItripLabelDicMapper itripLabelDicMapper) {
        this.itripLabelDicMapper = itripLabelDicMapper;
    }

    @Override
    public List<ItripLabelDic> getItripLabelDicListByMap(Map<String, Object> param) throws Exception {
        return itripLabelDicMapper.getItripLabelDicListByMap(param);
    }

    @Override
    public List<ItripLabelDicVO> getItripLabelDicByParentId(Long parentId) throws Exception {
        return itripLabelDicMapper.getItripLabelDicByParentId(parentId);
    }
}

package com.boot.service;

import com.boot.mapper.ItripImageMapper;
import com.boot.po.ItripImage;
import com.boot.util.ItripImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItripImageServiceImp implements ItripImageService {
    @Autowired
    private ItripImageMapper itripImageMapper;

    public ItripImageMapper getItripImageMapper() {
        return itripImageMapper;
    }

    public void setItripImageMapper(ItripImageMapper itripImageMapper) {
        this.itripImageMapper = itripImageMapper;
    }

    @Override
    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param) throws Exception {
        return itripImageMapper.getItripImageListByMap(param);
    }
}

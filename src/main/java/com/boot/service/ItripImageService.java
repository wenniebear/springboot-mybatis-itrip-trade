package com.boot.service;

import com.boot.util.ItripImageVO;

import java.util.List;
import java.util.Map;

public interface ItripImageService {
    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param)throws Exception;
}

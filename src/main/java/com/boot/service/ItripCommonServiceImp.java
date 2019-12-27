package com.boot.service;

import com.boot.mapper.ItripCommentMapper;
import com.boot.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItripCommonServiceImp implements ItripCommonService {
    @Autowired
    private ItripCommentMapper itripCommentMapper;

    public ItripCommentMapper getItripCommentMapper() {
        return itripCommentMapper;
    }

    public void setItripCommentMapper(ItripCommentMapper itripCommentMapper) {
        this.itripCommentMapper = itripCommentMapper;
    }

    @Override
    public Page<ItripListCommentVO> getItripCommentListByMap(Map<String, Object> param,Integer pageNo,Integer pageSize) throws Exception {
        //初始化分页信息
        pageNo= EmptyUtils.isEmpty(pageNo)? Constants.DEFAULT_PAGE_NO:pageNo;
        pageSize=EmptyUtils.isEmpty(pageSize)?Constants.DEFAULT_PAGE_SIZE:pageSize;
        Integer count=itripCommentMapper.getItripCommentCountByMap(param);
        //初始化page对象
        Page page=new Page(pageNo,pageSize,count);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        //参数beginPos、pageSize
        List<ItripListCommentVO> list=itripCommentMapper.getItripCommentListByMap(param);
        page.setRows(list);
        return page;
    }

    @Override
    public ItripScoreCommentVO getCommentAvgScore(Long hotelId) throws Exception {
        return itripCommentMapper.getCommentAvgScore(hotelId);
    }

    @Override
    public Integer getItripCommentCountByMap(Map<String, Object> param) throws Exception {

        return itripCommentMapper.getItripCommentCountByMap(param);
    }
}

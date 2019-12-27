package com.boot.service;

import com.boot.mapper.ItripHotelMapper;
import com.boot.po.ItripAreaDic;
import com.boot.po.ItripHotel;
import com.boot.po.ItripLabelDic;
import com.boot.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItripHotelServiceImp implements ItripHotelService {
    @Autowired
    private ItripHotelMapper itripHotelMapper;
    public ItripHotelMapper getItripHotelMapper() {
        return itripHotelMapper;
    }
    public void setItripHotelMapper(ItripHotelMapper itripHotelMapper) {
        this.itripHotelMapper = itripHotelMapper;
    }

    @Override
    public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception {
        //通用字典集合
        List<ItripLabelDic> itripLabelDicList=new ArrayList<>();
        //获取feature特色集合
        itripLabelDicList=itripHotelMapper.getHotelFeatureByHotelId(id);
        //封装数据去前台
        //建立前台类型数组用来
        List<ItripSearchDetailsHotelVO> list = new ArrayList<ItripSearchDetailsHotelVO>();
        ItripSearchDetailsHotelVO vo = new ItripSearchDetailsHotelVO();
        vo.setName("酒店介绍");
        vo.setDescription(itripHotelMapper.getItripHotelById(id).getDetails());
        list.add(vo);
            //从特色集合中取到介绍
        for (ItripLabelDic itripLabelDic:itripLabelDicList) {
            ItripSearchDetailsHotelVO vo2 = new ItripSearchDetailsHotelVO();
            vo2.setName(itripLabelDic.getName());
            vo2.setDescription(itripLabelDic.getDescription());
            list.add(vo2);
        }
        return  list;
    }

    @Override
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelFacilitiesById(id);
    }

    @Override
    public ItripSearchPolicyHotelVO queryHotelPolicy(Long id) throws Exception {
        return itripHotelMapper.queryHotelPolicy(id);
    }

    @Override
    public HotelVideoDescVO getVideoDescByHotelId(Long id) throws Exception {
        HotelVideoDescVO videoVo=new HotelVideoDescVO();
        List<ItripAreaDic> itripAreaDicslist=new ArrayList<>();
        List<ItripLabelDic> itripLabelDicslist=new ArrayList<>();
        itripAreaDicslist=itripHotelMapper.getHotelAreaByHotelId(id);
        //1
        List<String> list1=new ArrayList<>();
        for (ItripAreaDic dic:itripAreaDicslist){
            list1.add(dic.getName());//获取商圈名
        }
        //2
        itripLabelDicslist=itripHotelMapper.getHotelFeatureByHotelId(id);
        List<String> list2=new ArrayList<>();
        for (ItripLabelDic dic:itripLabelDicslist){
            list2.add(dic.getName());//获取
        }
        //存入对象
        videoVo.setTradingAreaNameList(list1);//商圈
        videoVo.setHotelName(itripHotelMapper.getItripHotelById(id).getHotelName());//酒店名
        videoVo.setHotelFeatureList(list2);
        return videoVo;
    }

    @Override
    public ItripHotel getItripHotelById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelById(id);
    }


}

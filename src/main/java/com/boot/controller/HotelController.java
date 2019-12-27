package com.boot.controller;

import com.boot.mapper.ItripHotelMapper;
import com.boot.po.Dto;
import com.boot.po.ItripAreaDic;
import com.boot.po.ItripLabelDic;
import com.boot.service.ItripAreaDicSearchService;
import com.boot.service.ItripHotelService;
import com.boot.service.ItripLableDicService;
import com.boot.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotel")
public class HotelController {
    @Autowired
    private ItripLableDicService itripLableDicService;

    public ItripLableDicService getItripLableDicService() {
        return itripLableDicService;
    }

    public void setItripLableDicService(ItripLableDicService itripLableDicService) {
        this.itripLableDicService = itripLableDicService;
    }

    @Autowired
    private ItripAreaDicSearchService itripAreaDicSearchService;

    public ItripAreaDicSearchService getItripAreaDicSearchService() {
        return itripAreaDicSearchService;
    }

    public void setItripAreaDicSearchService(ItripAreaDicSearchService itripAreaDicSearchService) {
        this.itripAreaDicSearchService = itripAreaDicSearchService;
    }

    @Autowired
    private ItripHotelService itripHotelService;

    public ItripHotelService getItripHotelService() {
        return itripHotelService;
    }

    public void setItripHotelService(ItripHotelService itripHotelService) {
        this.itripHotelService = itripHotelService;
    }

    /****r
     * 查询热门城市的接口
     *
     * @param type
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询热门城市", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询国内、国外的热门城市(1:国内 2:国外)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10201 : hotelId不能为空 </p>" +
            "<p>10202 : 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhotcity/{type}")
    @ResponseBody
    public Dto<ItripAreaDicVO> queryHotCity(@PathVariable Integer type) {
        System.out.println("查询热门城市");
        List<ItripAreaDic> areaDics = null;
        List<ItripAreaDicVO> areaDicVOS = null;//传出去只有两个字段，id,name
        try {
            if (EmptyUtils.isNotEmpty(type)) {
                Map param = new HashMap();
                param.put("isHot", 1);
                param.put("isChina", type);
                areaDics = itripAreaDicSearchService.getItripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(areaDics)) {
                    areaDicVOS = new ArrayList<>();//建立集合用来接收vos的值
                    //封装数据到前台
                    for (ItripAreaDic dic : areaDics) {
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        BeanUtils.copyProperties(dic, vo);//把dic的东西复制到vo中，前提两个必须拥有共同字段
                        areaDicVOS.add(vo);
                    }
                }
            } else {
                return DtoUtil.returnFail("type不能为空", "10201");
            }
            return DtoUtil.returnDataSuccess(areaDicVOS);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10202");
        }
    }

    /***
     * 查询酒店特色列表
     *
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询酒店特色列表", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "获取酒店特色(用于查询页列表)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码: </p>" +
            "<p>10205: 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhotelfeature")
    public Dto<ItripLabelDicVO> queryHotelFeature() {
        System.out.println("根据城市查询特色....");
        List<ItripLabelDic> itripLabelDics = null;
        List<ItripLabelDicVO> itripLabelDicVOS = null;
        try {
            Map param = new HashMap();
            param.put("parentId", 16);
            itripLabelDics = itripLableDicService.getItripLabelDicListByMap(param);
            if (EmptyUtils.isNotEmpty(itripLabelDics)) {
                itripLabelDicVOS = new ArrayList();
                for (ItripLabelDic dic : itripLabelDics) {
                    ItripLabelDicVO vo = new ItripLabelDicVO();
                    BeanUtils.copyProperties(dic, vo);
                    itripLabelDicVOS.add(vo);
                }
            }
        } catch (Exception e) {
            DtoUtil.returnFail("系统异常,获取失败", "10204");
            e.printStackTrace();
        }
        return DtoUtil.returnDataSuccess(itripLabelDicVOS);
    }

    /***
     * 根据酒店id查询酒店特色和介绍 -add by donghai
     *
     * @return
     * @throws Exception
     */
  /*  @ApiOperation(value = "根据酒店id查询酒店特色和介绍", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店特色和介绍" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10210: 酒店id不能为空</p>" +
            "<p>10211: 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhoteldetails/{id}")
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelDetails(@PathVariable Long id) {
        System.out.println("根据酒店id查询特色和介绍....");
        if (EmptyUtils.isEmpty(id)) {
            return DtoUtil.returnFail("酒店id不能为空", "10210");
        }
        try {
            List<ItripSearchDetailsHotelVO> itripSearchDetailsHotelVOList = null;
            itripSearchDetailsHotelVOList = itripHotelService.queryHotelDetails(id);
            return DtoUtil.returnDataSuccess(itripSearchDetailsHotelVOList);

        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10211");
        }
    }

    /***
     * 根据酒店id查询酒店设施 -add by donghai
     *
     * @return
     * @throws Exception
     */
  /*  @ApiOperation(value = "根据酒店id查询酒店设施", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店设施" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10206: 酒店id不能为空</p>" +
            "<p>10207: 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhotelfacilities/{id}")
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelFacilities(@PathVariable Long id) {
        System.out.println("根据酒店id查询酒店设施....");
        if (EmptyUtils.isEmpty(id)) {
            return DtoUtil.returnFail("酒店id不能为空", "10206");
        }
        try {
            ItripSearchFacilitiesHotelVO itripSearchFacilitiesHotelVO = null;
            itripSearchFacilitiesHotelVO = itripHotelService.getItripHotelFacilitiesById(id);
            return DtoUtil.returnDataSuccess(itripSearchFacilitiesHotelVO.getFacilities());
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常，获取失败", "10207");
        }
    }

    /***
     * 根据酒店id查询酒店政策 -add by donghai
     *
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "根据酒店id查询酒店政策", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店政策" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10208: 酒店id不能为空</p>" +
            "<p>10209: 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhotelpolicy/{id}")
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelPolicy(@PathVariable Long id) {
        System.out.println("根据酒店id查询酒店政策。。。。");
        ItripSearchPolicyHotelVO itripSearchPolicyHotelVO = null;
        if (EmptyUtils.isEmpty(id)) {
            return DtoUtil.returnFail("酒店id不能为空", "10208");
        }
        try {
            itripSearchPolicyHotelVO = itripHotelService.queryHotelPolicy(id);
            return DtoUtil.returnDataSuccess(itripSearchPolicyHotelVO.getHotelPolicy());
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10209");
        }
    }

    /***
     * 查询商圈的接口
     *
     * @param cityId 根据城市查询商圈
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询商圈", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据城市查询商圈" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10203 : cityId不能为空 </p>" +
            "<p>10204 : 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/querytradearea/{cityId}")
    @ResponseBody
    public Dto<ItripAreaDicVO> queryTradeArea(@PathVariable Long cityId) {
        System.out.println("根据城市查询商圈....");
        List<ItripAreaDic> itripAreaDics = null;
        List<ItripAreaDicVO> itripAreaDicVOS = null;
        if (EmptyUtils.isNotEmpty(cityId)) {
            try {
                Map param = new HashMap();
                param.put("isTradingArea", 1);
                param.put("parent", cityId);
                itripAreaDics = itripAreaDicSearchService.getItripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(itripAreaDics)) {
                    itripAreaDicVOS=new ArrayList<>();
                        for (ItripAreaDic dic : itripAreaDics) {
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        BeanUtils.copyProperties(dic, vo);
                        itripAreaDicVOS.add(vo);
                    }
                    System.out.println(itripAreaDicVOS);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("系统异常,获取失败", "10204");
            }
            return DtoUtil.returnDataSuccess(itripAreaDicVOS);
        } else {
            return DtoUtil.returnFail("cityId不能为空", "10203");
        }
    }
    /*   @ApiOperation(value = "根据酒店id查询酒店特色、商圈、酒店名称", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店特色、商圈、酒店名称（视频文字描述）" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100214 : 获取酒店视频文字描述失败 </p>" +
            "<p>100215 : 酒店id不能为空</p>")*/
    @RequestMapping(value = "/getvideodesc/{hotelId}")
    @ResponseBody
    public Dto<Object> getVideoDescByHotelId(/*@ApiParam(required = true, name = "hotelId", value = "酒店ID")*/
            @PathVariable String hotelId) {
        System.out.println("根据酒店id查询视频头....");
        Dto<Object> dto=new Dto<>();
        HotelVideoDescVO hotelVideoDescVO=null;
        if (EmptyUtils.isNotEmpty(hotelId)){
            try {
                hotelVideoDescVO=itripHotelService.getVideoDescByHotelId(Long.valueOf(hotelId));
                dto=DtoUtil.returnDataSuccess(hotelVideoDescVO);

            } catch (Exception e) {
                e.printStackTrace();
                dto=DtoUtil.returnFail("获取酒店视频文字描述失败","100214");
            }
        }else {
            dto=DtoUtil.returnFail("酒店id不能为空","100215");
        }
        return dto;
    }
}

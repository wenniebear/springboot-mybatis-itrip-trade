package com.boot.controller;

import com.boot.po.Dto;
import com.boot.po.ItripHotelRoom;
import com.boot.po.ItripImage;
import com.boot.service.ItripHotelRoomService;
import com.boot.service.ItripImageService;
import com.boot.service.ItripLableDicService;
import com.boot.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotelroom")
public class ItripHotelRoomController {
    @Autowired
    private ItripLableDicService itripLableDicService;

    public ItripLableDicService getItripLableDicService() {
        return itripLableDicService;
    }

    public void setItripLableDicService(ItripLableDicService itripLableDicService) {
        this.itripLableDicService = itripLableDicService;
    }

    @Autowired
    private ItripHotelRoomService itripHotelRoomService;

    public ItripHotelRoomService getItripHotelRoomService() {
        return itripHotelRoomService;
    }

    public void setItripHotelRoomService(ItripHotelRoomService itripHotelRoomService) {
        this.itripHotelRoomService = itripHotelRoomService;
    }
    @Autowired
    private ItripImageService itripImageService;

    public ItripImageService getItripImageService() {
        return itripImageService;
    }

    public void setItripImageService(ItripImageService itripImageService) {
        this.itripImageService = itripImageService;
    }

    /* @ApiOperation(value = "查询酒店房间床型列表", httpMethod = "GET",
               protocols = "HTTP", produces = "application/json",
               response = Dto.class, notes = "查询酒店床型列表" +
               "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
               "<p>错误码：</p>" +
               "<p>100305 : 获取酒店房间床型失败</p>")*/
    @RequestMapping(value = "/queryhotelroombed")
    @ResponseBody
    public Dto<Object> queryHotelRoomBed() {
        //下拉列表！！！！！！！！
        System.out.println("查询酒店房间床型列表.....");
        try {
            List<ItripLabelDicVO> itripLabelDicVOList=itripLableDicService.getItripLabelDicByParentId(new Long(1));
            return DtoUtil.returnDataSuccess(itripLabelDicVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取酒店房间床型失败","100305");
        }
    }
    /*@ApiOperation(value = "查询酒店房间列表", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询酒店房间列表" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100303 : 酒店id不能为空,酒店入住及退房时间不能为空,入住时间不能大于退房时间</p>" +
            "<p>100304 : 系统异常</p>")*/
    @RequestMapping(value = "/queryhotelroombyhotel")
    @ResponseBody
    public Dto<List<ItripHotelRoomVO>> queryHotelRoomByHotel(@RequestBody SearchHotelRoomVO vo) {
        System.out.println("查询酒店房间列表....注意修改了mapper中集合判null");
        List<List<ItripHotelRoomVO>> list=new ArrayList<>();//多种房型+多种床型等内容。集合+集合
        Map<String,Object> param=new HashMap<>();
        if (EmptyUtils.isEmpty(vo.getHotelId())){
            return DtoUtil.returnFail("酒店id不能为空","100303");
        }
        param.put("hotelId",vo.getHotelId());
        if (EmptyUtils.isEmpty(vo.getStartDate())||EmptyUtils.isEmpty(vo.getEndDate())){
            return DtoUtil.returnFail("酒店入住及退房时间不能为空","100303");
        }
        if (vo.getEndDate().getTime()<vo.getStartDate().getTime()){
            return DtoUtil.returnFail("入住时间不能大于退房时间","100303");
        }
        if (vo.getStartDate().getTime()<System.currentTimeMillis()){
            return DtoUtil.returnFail("入住时间不能小于当前时间","100303");
        }
        List list1=DateUtil.getBetweenDates(vo.getStartDate(),vo.getEndDate());//时间区间
        param.put("timesList",list1);
        vo.setIsBook(EmptyUtils.isEmpty(vo.getIsBook())?null:vo.getIsBook());//预定
        param.put("isBook",vo.getIsBook());
        vo.setIsCancel(EmptyUtils.isEmpty(vo.getIsCancel())?null:vo.getIsCancel());//可取消
        param.put("isCancel",vo.getIsCancel());
        vo.setIsHavingBreakfast(EmptyUtils.isEmpty(vo.getIsHavingBreakfast())?null:vo.getIsHavingBreakfast());//有早
        param.put("isHavingBreakfast",vo.getIsHavingBreakfast());
        vo.setIsTimelyResponse(EmptyUtils.isEmpty(vo.getIsTimelyResponse())?null:vo.getIsTimelyResponse());//立即确认
        param.put("isTimelyResponse",vo.getIsTimelyResponse());
        vo.setPayType(EmptyUtils.isEmpty(vo.getPayType())?null:vo.getPayType());//支付方式
        if (EmptyUtils.isEmpty(vo.getPayType())||vo.getPayType()==3){
            param.put("payType",null);
        }else {
            param.put("payType",vo.getPayType());
        }
        vo.setRoomBedTypeId(EmptyUtils.isEmpty(vo.getRoomBedTypeId())?null:vo.getRoomBedTypeId());//床型
        param.put("roomBedTypeId",vo.getRoomBedTypeId());
        try {
            List<ItripHotelRoomVO> roomVOList=null;
            roomVOList=itripHotelRoomService.getItripHotelRoomListByMap(param);
            for (ItripHotelRoomVO dic:roomVOList){
                List<ItripHotelRoomVO> temp=new ArrayList<>();
                temp.add(dic);
                list.add(temp);
            }
            return DtoUtil.returnSuccess("获取成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常","100304");
        }
    }
    /* @ApiOperation(value = "根据targetId查询酒店房型图片(type=1)", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店房型ID查询酒店房型图片" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100301 : 获取酒店房型图片失败 </p>" +
            "<p>100302 : 酒店房型id不能为空</p>")*/
    @RequestMapping(value = "/getimg/{targetId}")
    @ResponseBody
    public Dto<Object> getImgByTargetId(@PathVariable String targetId) {
        System.out.println("根据targetId查询酒店房型图片...");
        if (EmptyUtils.isNotEmpty(targetId)){
            List<ItripImageVO> list=null;
            Map<String,Object> param = new HashMap<>();
            param.put("type",1);//图片类型1---房间图片
            param.put("targetId",targetId);
            try {
                list=itripImageService.getItripImageListByMap(param);
                return DtoUtil.returnDataSuccess(list);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("获取酒店房型图片失败","100301");
            }
        }else {
            return DtoUtil.returnFail("酒店房型id不能为空","100302");
        }

    }
}

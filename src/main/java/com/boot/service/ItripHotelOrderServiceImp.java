package com.boot.service;

import com.boot.mapper.ItripHotelOrderMapper;
import com.boot.mapper.ItripHotelRoomMapper;
import com.boot.mapper.ItripOrderLinkUserMapper;
import com.boot.po.ItripHotelOrder;
import com.boot.po.ItripHotelRoom;
import com.boot.po.ItripOrderLinkUser;
import com.boot.po.ItripUserLinkUser;
import com.boot.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_DOWN;

@Service
public class ItripHotelOrderServiceImp implements ItripHotelOrderService {
    @Autowired
    private ItripHotelOrderMapper itripHotelOrderMapper;

    public ItripHotelOrderMapper getItripHotelOrderMapper() {
        return itripHotelOrderMapper;
    }

    public void setItripHotelOrderMapper(ItripHotelOrderMapper itripHotelOrderMapper) {
        this.itripHotelOrderMapper = itripHotelOrderMapper;
    }
    @Autowired
    private ItripHotelRoomMapper itripHotelRoomMapper;

    public ItripHotelRoomMapper getItripHotelRoomMapper() {
        return itripHotelRoomMapper;
    }

    public void setItripHotelRoomMapper(ItripHotelRoomMapper itripHotelRoomMapper) {
        this.itripHotelRoomMapper = itripHotelRoomMapper;
    }
    @Autowired
    private ItripOrderLinkUserMapper itripOrderLinkUserMapper;

    public ItripOrderLinkUserMapper getItripOrderLinkUserMapper() {
        return itripOrderLinkUserMapper;
    }

    public void setItripOrderLinkUserMapper(ItripOrderLinkUserMapper itripOrderLinkUserMapper) {
        this.itripOrderLinkUserMapper = itripOrderLinkUserMapper;
    }

    /*
                添加订单数据到数据库
                * */
    @Override
    //计算总支付价格
    //BigDecimal格式为精确数字类型，保留所有位小数，一般金钱需要用本类
    public BigDecimal getOrderPayAmount(Integer count, Long roomId) throws Exception {
        BigDecimal payAmount=null;
        BigDecimal roomPrice=itripHotelRoomMapper.getItripHotelRoomById(roomId).getRoomPrice();
        //        计算类          方法          num1 , num2    ,  switch 选择 multiply           ，setScale(scale,roundingMode)
        payAmount=BigDecimalUtil.OperationASMD(count,roomPrice,BigDecimalUtil.BigDecimalOprations.multiply,2,ROUND_DOWN);
        return payAmount;
    }
    /*
        添加订单数据到数据库
        * */
    @Override
    public Map<String, String> itripAddItripHotelOrder(ItripHotelOrder itripHotelOrder, List<ItripUserLinkUser> linkUsers) throws Exception {
        Map<String ,String > param =new HashMap<>();
        if (EmptyUtils.isNotEmpty(itripHotelOrder)){
            int flag=0;//定义常量0
            //传入所有参数都应齐全，除了-->创建时间
            itripHotelOrder.setCreationDate(new Date());
            //调用方法添加，返回影响行数flag
            flag=itripHotelOrderMapper.insertItripHotelOrder(itripHotelOrder);
            if (flag>0){
                //添加成功后需要向，还需往订单联系人表添加记录
                Long orderId=itripHotelOrder.getId();
                if (orderId > 0) {
                    for (ItripUserLinkUser linkUser:linkUsers){//存入订单联系人
                        ItripOrderLinkUser user=new ItripOrderLinkUser();
                        user.setOrderId(orderId);
                        user.setLinkUserId(itripHotelOrder.getUserId());
                        user.setLinkUserName(itripHotelOrder.getLinkUserName());
                        user.setCreationDate(new Date());
                        user.setCreatedBy(itripHotelOrder.getCreatedBy());
                        itripOrderLinkUserMapper.insertItripOrderLinkUser(user);
                    }
                }
                param.put("id",itripHotelOrder.getId().toString());
                param.put("orderNo",itripHotelOrder.getOrderNo());
                return param;
            }
        }
        return null;
    }

    @Override
    public Page getOrderListByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        //获取总记录数
        Integer total=itripHotelOrderMapper.getOrderCountByMap(param);
        //分页处理-->前台给不给pageNo和pageSize 不给即使用默认参数
        pageNo=EmptyUtils.isEmpty(pageNo)? Constants.DEFAULT_PAGE_NO:pageNo;
        pageSize=EmptyUtils.isEmpty(pageSize)?Constants.DEFAULT_PAGE_SIZE:pageSize;
        Page page=new Page(pageNo,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List<ItripListHotelOrderVO> list=itripHotelOrderMapper.getOrderListByMap(param);
        page.setRows(list);
        return page;
    }

    @Override
    public Integer updateHotelOrderStatusPayOk(String orderNo) throws Exception {
        return itripHotelOrderMapper.updateHotelOrderStatusPayOk(orderNo);
    }

    @Override
    public Integer updateHotelOrderStatusSureGoIn(String orderNo) throws Exception {
        return itripHotelOrderMapper.updateHotelOrderStatusSureGoIn(orderNo);
    }

    @Override
    public ItripHotelOrder getItripHotelOrderById(Long id) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderById(id);
    }

    @Override
    public List<ItripHotelOrder> getItripHotelOrderListByMap(Map<String, Object> param) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderListByMap(param);
    }

    @Override
    public ItripPersonalOrderRoomVO getItripHotelOrderRoomInfoById(Long id) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderRoomInfoById(id);
    }
}

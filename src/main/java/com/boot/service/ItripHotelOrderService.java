package com.boot.service;

import com.boot.po.ItripHotelOrder;
import com.boot.po.ItripUserLinkUser;
import com.boot.util.ItripListHotelOrderVO;
import com.boot.util.ItripPersonalOrderRoomVO;
import com.boot.util.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ItripHotelOrderService {
    /*
    * 计算总金额==需要获取到price
    * */
    public BigDecimal getOrderPayAmount(Integer count,Long roomId) throws Exception;

    /*
    添加订单数据到数据库
    * */
    //Map<字段名，返回行数>
    public Map<String ,String>  itripAddItripHotelOrder(ItripHotelOrder itripHotelOrder, List<ItripUserLinkUser> linkUsers) throws Exception;

    /*根据用户数据查询订单-->该用户所有信息--->返回是一个集合*/
    public Page getOrderListByMap(Map<String,Object> param, Integer pageNo, Integer pageSize) throws Exception;
    /*查询酒店信息 -->通过订单Id*/
    public ItripHotelOrder getItripHotelOrderById(@Param(value = "id") Long id)throws Exception;

    /*查询房间信息-->通过订单ID*/
    public ItripPersonalOrderRoomVO getItripHotelOrderRoomInfoById(@Param(value = "id") Long id)throws Exception;
    //通过订单ID查询订单信息
    public List<ItripHotelOrder>	getItripHotelOrderListByMap(Map<String, Object> param)throws Exception;
    /***
     * 更改订单状态--payok
     */
    public Integer updateHotelOrderStatusPayOk(String orderNo)throws Exception;
    /***
     * 更改订单状态--SureGoin
     */
    public Integer updateHotelOrderStatusSureGoIn(String orderNo)throws Exception;
}

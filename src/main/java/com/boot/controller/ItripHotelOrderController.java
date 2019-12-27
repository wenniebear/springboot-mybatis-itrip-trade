package com.boot.controller;

import com.alibaba.fastjson.JSONObject;
import com.boot.po.*;
import com.boot.service.ItripHotelOrderService;
import com.boot.service.ItripHotelRoomService;
import com.boot.service.ItripHotelService;
import com.boot.service.ItripHotelTempStoreService;
import com.boot.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotelorder")
public class ItripHotelOrderController {
    private Jedis jedis=new Jedis("127.0.0.1",6379);
    @Autowired
    private ItripHotelTempStoreService itripHotelTempStoreService;
    @Autowired
    private ItripHotelRoomService itripHotelRoomService;
    @Autowired
    private ItripHotelService itripHotelService;
    @Autowired
    private ItripHotelOrderService itripHotelOrderService;

    public ItripHotelOrderService getItripHotelOrderService() {
        return itripHotelOrderService;
    }

    public void setItripHotelOrderService(ItripHotelOrderService itripHotelOrderService) {
        this.itripHotelOrderService = itripHotelOrderService;
    }

    public ItripHotelService getItripHotelService() {
        return itripHotelService;
    }

    public void setItripHotelService(ItripHotelService itripHotelService) {
        this.itripHotelService = itripHotelService;
    }

    public ItripHotelTempStoreService getItripHotelTempStoreService() {
        return itripHotelTempStoreService;
    }

    public void setItripHotelTempStoreService(ItripHotelTempStoreService itripHotelTempStoreService) {
        this.itripHotelTempStoreService = itripHotelTempStoreService;
    }

    public ItripHotelRoomService getItripHotelRoomService() {
        return itripHotelRoomService;
    }

    public void setItripHotelRoomService(ItripHotelRoomService itripHotelRoomService) {
        this.itripHotelRoomService = itripHotelRoomService;
    }

    /* @ApiOperation(value = "生成订单前,获取预订信息", httpMethod = "POST",
                    protocols = "HTTP", produces = "application/json",
                    response = Dto.class, notes = "生成订单前,获取预订信息" +
                    "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                    "<p>错误码：</p>" +
                    "<p>100000 : token失效，请重登录 </p>" +
                    "<p>100510 : hotelId不能为空</p>" +
                    "<p>100511 : roomId不能为空</p>" +
                    "<p>100512 : 暂时无房</p>" +
                    "<p>100513 : 系统异常</p>")*/
    @RequestMapping(value = "/getpreorderinfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Dto<RoomStoreVO> getPreOrderInfo(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
        System.out.println("订单生成前，酒店信息获取展示。。。。");
        //置换
        String token = request.getHeader("token");
        System.out.println("客户端token："+token );
        System.out.println("本地端token: "+jedis.get("token"));
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //json字符串转成用户对象
        ItripUser currentUser=(ItripUser)JSONObject.toJavaObject(jsonObject,ItripUser.class);
        //订单
        if (EmptyUtils.isEmpty(currentUser)){
            return DtoUtil.returnFail("token失效，请重登录","100000");
        }
        if (EmptyUtils.isEmpty(validateRoomStoreVO.getHotelId())){
            return DtoUtil.returnFail("hotelId不能为空","100510");
        }
        if (EmptyUtils.isEmpty(validateRoomStoreVO.getRoomId())){
            return DtoUtil.returnFail("roomId不能为空","100511");
        }
        try {
            //房型单价
            ItripHotelRoom itripHotelRoom=itripHotelRoomService.getItripHotelRoomById(validateRoomStoreVO.getRoomId());
            BigDecimal price=itripHotelRoom.getRoomPrice();
            //房间名称
            ItripHotel itripHotel=itripHotelService.getItripHotelById(validateRoomStoreVO.getHotelId());
            String hotelName=itripHotel.getHotelName();
            //map存入前台传入参数
            Map<String,Object> param = new HashMap<>();
            param.put("hotelId",validateRoomStoreVO.getHotelId());
            param.put("startTime",validateRoomStoreVO.getCheckInDate());
            param.put("endTime",validateRoomStoreVO.getCheckOutDate());
            param.put("roomId",validateRoomStoreVO.getRoomId());

            //传入前台参数
            RoomStoreVO storeVO=new RoomStoreVO();
            storeVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
            storeVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
            storeVO.setHotelId(validateRoomStoreVO.getHotelId());
            storeVO.setRoomId(validateRoomStoreVO.getRoomId());
            storeVO.setHotelName(hotelName);
            storeVO.setPrice(price);
            storeVO.setCount(1);
            Integer store=itripHotelTempStoreService.getTempStore(param);
            System.out.println("1Store"+store);
            if (EmptyUtils.isEmpty(store)){
                return DtoUtil.returnFail("暂时无房","100512");
            }
            storeVO.setStore(store);
            return DtoUtil.returnDataSuccess(storeVO);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常","100513");
        }
    }
    /*@ApiOperation(value = "生成订单", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "生成订单" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100505 : 生成订单失败 </p>" +
            "<p>100506 : 不能提交空，请填写订单信息 </p>" +
            "<p>100507 : 库存不足 </p>" +
            "<p>100000 : token失效，请重登录</p>")*/
    @RequestMapping(value = "/addhotelorder")
    @ResponseBody
    public Dto<Object> addHotelOrder(@RequestBody ItripAddHotelOrderVO itripAddHotelOrderVO, HttpServletRequest request) {
        System.out.println("生成未付款订单。。。。");
        //获取token，验证是否登录失败
        String token=request.getHeader("token");
        System.out.println("本地端token: "+jedis.get("token"));
        System.out.println("cookie的token:"+token);
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //json字符串转成用户对象
        ItripUser currentUser=(ItripUser)JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (EmptyUtils.isEmpty(currentUser)) {
            return DtoUtil.returnFail("token失效，请重登录","10000");
        }
        try {
        Map<String ,Object> param=new HashMap<>();
        param.put("count",itripAddHotelOrderVO.getCount());//买入数量
        param.put("hotelId",itripAddHotelOrderVO.getHotelId());
        param.put("startTime",itripAddHotelOrderVO.getCheckInDate());
        param.put("endTime",itripAddHotelOrderVO.getCheckOutDate());
        param.put("roomId",itripAddHotelOrderVO.getRoomId());
            //库存
            boolean flag=itripHotelTempStoreService.vaildateRoomStore(param);

            System.out.println("Controllerflag"+flag);
            if (flag&&EmptyUtils.isNotEmpty(itripAddHotelOrderVO)){
                //计算天数
                int days=DateUtil.getBetweenDates(itripAddHotelOrderVO.getCheckInDate(),itripAddHotelOrderVO.getCheckOutDate()).size()-1;
                if (days<0){
                    return DtoUtil.returnFail("退房日期必须大于入住日期","100505");
                }
                ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
                itripHotelOrder.setId(itripAddHotelOrderVO.getId());//订单id
                itripHotelOrder.setUserId(currentUser.getId());//用户ID
                itripHotelOrder.setOrderType(itripAddHotelOrderVO.getOrderType());//订单类型(0:旅游产品 1:酒店产品 2：机票产品
                itripHotelOrder.setHotelId(itripAddHotelOrderVO.getHotelId());//酒店ID
                itripHotelOrder.setHotelName(itripAddHotelOrderVO.getHotelName());
                itripHotelOrder.setRoomId(itripAddHotelOrderVO.getRoomId());
                itripHotelOrder.setCount(itripAddHotelOrderVO.getCount());//预订数量
                itripHotelOrder.setCheckInDate(itripAddHotelOrderVO.getCheckInDate());
                itripHotelOrder.setCheckOutDate(itripAddHotelOrderVO.getCheckOutDate());
                itripHotelOrder.setNoticePhone(itripAddHotelOrderVO.getNoticePhone());//登记电话
                itripHotelOrder.setNoticeEmail(itripAddHotelOrderVO.getNoticeEmail());
                itripHotelOrder.setSpecialRequirement(itripAddHotelOrderVO.getSpecialRequirement());
                itripHotelOrder.setIsNeedInvoice(itripAddHotelOrderVO.getIsNeedInvoice());//是否发票
                itripHotelOrder.setInvoiceHead(itripAddHotelOrderVO.getInvoiceHead());//发票抬头
                itripHotelOrder.setInvoiceType(itripAddHotelOrderVO.getInvoiceType());//发票类型
                itripHotelOrder.setCreatedBy(currentUser.getId());//订单创建人
                itripHotelOrder.setBookingDays(days);//预定天数
                //获取用户联系人信息
                List<ItripUserLinkUser> linkUserList = itripAddHotelOrderVO.getLinkUser();
                StringBuilder linkUserName=new StringBuilder();
                int size=linkUserList.size();
                for (int i=0;i<size;i++){
                    if (i!=size-1){
                        linkUserName.append(linkUserList.get(i).getLinkUserName()+",");
                    }else {
                        linkUserName.append(linkUserList.get(i).getLinkUserName());
                    }
                }
                itripHotelOrder.setLinkUserName(linkUserName.toString());
                //获取预定类型bookType
                if (token.startsWith("token:PC")){
                    itripHotelOrder.setBookType(0);
                }else if(token.startsWith("token:MOBILE")){
                    itripHotelOrder.setBookType(1);
                }else {
                    itripHotelOrder.setBookType(2);
                }
                //支付之前生成的订单状态:0未支付
                itripHotelOrder.setOrderStatus(0);
                //生成订单号：机器码 +日期+（MD5）（酒店ID+房间ID+毫秒数+1000000的随机数）
                StringBuilder md5String=new StringBuilder();
                md5String.append(itripHotelOrder.getHotelId());
                md5String.append(itripHotelOrder.getRoomId());
                md5String.append(System.currentTimeMillis());
                md5String.append(Math.random()*10000000);
                String md5=MD5Util.getMd5(md5String.toString(),6);
                //生成订单编号(固定字符串+当前时间+订单编号md5)
                StringBuilder orderNo=new StringBuilder();
                orderNo.append("D1000001");
                orderNo.append(DateUtil.format(new Date(),"yyyyMMddHHmmss"));
                orderNo.append(md5);
                itripHotelOrder.setOrderNo(orderNo.toString());
                //计算订单金额
                BigDecimal amount=itripHotelOrderService.getOrderPayAmount(days*itripAddHotelOrderVO.getCount(),itripAddHotelOrderVO.getRoomId());
                itripHotelOrder.setPayAmount(amount);
                Map<String,String> map=itripHotelOrderService.itripAddItripHotelOrder(itripHotelOrder,linkUserList);
                Map<String,Object> roomStore=new HashMap<>();
                roomStore.put("roomId",itripHotelOrder.getRoomId());
                roomStore.put("count",itripHotelOrder.getCount());
                roomStore.put("startTime",itripHotelOrder.getCheckInDate());
                roomStore.put("endTime",itripHotelOrder.getCheckOutDate());
                roomStore.put("recordDate",itripHotelOrder.getCreationDate());
                if (itripHotelTempStoreService.updateRoomStore(roomStore)!=0){
                    System.out.println("减库存成功");
                };
                return DtoUtil.returnDataSuccess(map);
            }else if (flag&&EmptyUtils.isEmpty(itripAddHotelOrderVO)){
                return DtoUtil.returnFail("能提交空，请填写订单信息","100506");
            }else {
                return DtoUtil.returnFail("库存不足","100507");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("生成订单失败","100505");
        }
    }
    /* @ApiOperation(value = "根据个人订单列表，并分页显示", httpMethod = "POST",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据条件查询个人订单列表，并分页显示" +
            "<p>订单类型(orderType)（-1：全部订单 0:旅游订单 1:酒店订单 2：机票订单）：</p>" +
            "<p>订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）：</p>" +
            "<p>对于页面tab条件：</p>" +
            "<p>全部订单（orderStatus：-1）</p>" +
            "<p>未出行（orderStatus：2）</p>" +
            "<p>待付款（orderStatus：0）</p>" +
            "<p>待评论（orderStatus：3）</p>" +
            "<p>已取消（orderStatus：1）</p>" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100501 : 请传递参数：orderType </p>" +
            "<p>100502 : 请传递参数：orderStatus </p>" +
            "<p>100503 : 获取个人订单列表错误 </p>" +
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value = "/getpersonalorderlist")
    @ResponseBody
    public Dto<Object> getPersonalOrderList(@RequestBody ItripSearchOrderVO itripSearchOrderVO,HttpServletRequest request) {
        System.out.println("根据用户信息查看订单列表，并分页显示");
        String token = request.getHeader("token");
        System.out.println("客户端的token："+token );
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //json字符串转成用户对象
        ItripUser currentUser=(ItripUser)JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (EmptyUtils.isEmpty(currentUser)){
            return DtoUtil.returnFail("token失效，请重登录","100000");
        }
        try {
            Integer orderType=itripSearchOrderVO.getOrderType();
            Integer orderStatus=itripSearchOrderVO.getOrderStatus();
            if (EmptyUtils.isEmpty(orderType))return DtoUtil.returnFail("请传递参数：orderType","100501");
            if (EmptyUtils.isEmpty(orderStatus))return DtoUtil.returnFail("请传递参数：orderStatus","100502");
            Map<String,Object> param=new HashMap<>();
            param.put("orderType",orderType == -1 ? null : orderType);
            param.put("orderStatus",orderStatus == -1 ? null : orderStatus);
            param.put("userId",currentUser.getId());
            param.put("orderNo",itripSearchOrderVO.getOrderNo());
            param.put("linkUserName",itripSearchOrderVO.getLinkUserName());
            param.put("endDate",itripSearchOrderVO.getEndDate());
            param.put("startDate",itripSearchOrderVO.getStartDate());
            Page page=itripHotelOrderService.getOrderListByMap(param,itripSearchOrderVO.getPageNo(),itripSearchOrderVO.getPageSize());
            List<ItripListHotelOrderVO> list=page.getRows();
            for (int i=0;i<page.getRows().size();i++){
                ItripListHotelOrderVO vo=new ItripListHotelOrderVO();
                vo=list.get(i);
                //CreationDate入住日期
                //CheckInDate 创建日期
                //与实际相反
                vo.setCheckInDate(new SimpleDateFormat("yyyy-MM-dd").format(vo.getCheckInString()));
                vo.setCreationDate(new SimpleDateFormat("yyyy-MM-dd").format(vo.getCreationString()));
                list.set(i,vo);
                System.out.println(list.get(i).toString());
            }
            return DtoUtil.returnDataSuccess(page);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取个人订单列表错误","100503");
        }
    }
    /* @ApiOperation(value = "根据订单ID查看个人订单详情", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据订单ID查看个人订单详情" +
            "<p>订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）：</p>" +
            "<p>订单流程：</p>" +
            "<p>订单状态(0：待支付 2:支付成功 3:已消费 4:已点评)的流程：{\"1\":\"订单提交\",\"2\":\"订单支付\",\"3\":\"支付成功\",\"4\":\"入住\",\"5\":\"订单点评\",\"6\":\"订单完成\"}</p>" +
            "<p>订单状态(1:已取消)的流程：{\"1\":\"订单提交\",\"2\":\"订单支付\",\"3\":\"订单取消\"}</p>" +
            "<p>支持支付类型(roomPayType)：{\"1\":\"在线付\",\"2\":\"线下付\",\"3\":\"不限\"}</p>" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100525 : 请传递参数：orderId </p>" +
            "<p>100526 : 没有相关订单信息 </p>" +
            "<p>100527 : 获取个人订单信息错误 </p>" +
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value = "/getpersonalorderinfo/{orderId}")
    @ResponseBody
    public Dto<Object> getPersonalOrderInfo(@PathVariable String orderId, HttpServletRequest request) {
        System.out.println("根据订单ID,查看个人订单详情。。。。");
        String token = request.getHeader("token");
        System.out.println("客户端的token1："+token );
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //json字符串转成用户对象
        ItripUser currentUser=(ItripUser)JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (EmptyUtils.isEmpty(currentUser))return DtoUtil.returnFail("token失效，请重登录","100000");
        try {
            ItripHotelOrder iho=itripHotelOrderService.getItripHotelOrderById(Long.valueOf(orderId));
            if (EmptyUtils.isEmpty(iho))return  DtoUtil.returnFail("请传递参数","100525");
            ItripPersonalHotelOrderVO vo=new ItripPersonalHotelOrderVO();
            vo.setId(iho.getId());
            vo.setOrderNo(iho.getOrderNo());
            vo.setBookType(iho.getBookType());
            vo.setNoticePhone(iho.getNoticePhone());
            vo.setPayAmount(iho.getPayAmount());
            vo.setPayType(iho.getPayType());
            Integer roomPayType=itripHotelRoomService.getItripHotelRoomById(iho.getRoomId()).getPayType();
            if (EmptyUtils.isEmpty(roomPayType))return DtoUtil.returnFail("酒店id为空","100001");
            vo.setRoomPayType(roomPayType);
            vo.setCreationDate(new SimpleDateFormat("yyyy-MM-dd").format(iho.getCreationDate()));
            Integer orderStatus=iho.getOrderStatus();
            vo.setOrderStatus(orderStatus);
            if (orderStatus==0){
                vo.setProcessNode("2");
            }else if (orderStatus==1){
                vo.setProcessNode("3");
            }else if (orderStatus==2){
                vo.setProcessNode("4");
            }else if (orderStatus==3){
                vo.setProcessNode("5");
            }else if (orderStatus==4){
                vo.setProcessNode("6");
            }else {
                vo.setOrderProcess(null);
                vo.setProcessNode(null);
            }
            return DtoUtil.returnSuccess("获取订单成功",vo);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取个人订单信息错误 ","100527");
        }
    }
    /*@ApiOperation(value = "根据订单ID查看个人订单详情-房型相关信息", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据订单ID查看个人订单详情-房型相关信息" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100529 : 请传递参数：orderId </p>" +
            "<p>100530 : 没有相关订单房型信息 </p>" +
            "<p>100531 : 获取个人订单房型信息错误 </p>" +
            "<p>支持支付类型(roomPayType)：{\"1\":\"在线付\",\"2\":\"线下付\",\"3\":\"不限\"}</p>" +
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value = "/getpersonalorderroominfo/{orderId}")
    @ResponseBody
    public Dto<Object> getPersonalOrderRoomInfo(@PathVariable String orderId, HttpServletRequest request) {
        System.out.println("根据订单ID查看个人订单详情-房型相关信息。。。。");
        String token = request.getHeader("token");
        System.out.println("客户端的token2："+token );
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //json字符串转成用户对象
        ItripUser currentUser=(ItripUser)JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (EmptyUtils.isEmpty(currentUser)) return DtoUtil.returnFail("token失效，请重登录","100000");
        if (EmptyUtils.isEmpty(orderId))return  DtoUtil.returnFail("请传递参数：orderId","100529");
        try {
            ItripPersonalOrderRoomVO vo=itripHotelOrderService.getItripHotelOrderRoomInfoById(Long.valueOf(orderId));
            if (EmptyUtils.isEmpty(vo)) return DtoUtil.returnFail("没有相关订单房型信息", "100530");
            return DtoUtil.returnDataSuccess(vo);

        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("没有相关订单房型信息","100530");
        }
     }
    /* @ApiOperation(value = "根据订单ID获取订单信息", httpMethod = "GET",
           protocols = "HTTP", produces = "application/json",
           response = Dto.class, notes = "根据订单ID获取订单信息" +
           "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
           "<p>错误码：</p>" +
           "<p>100533 : 没有查询到相应订单 </p>" +
           "<p>100534 : 系统异常 </p>")*/
    @RequestMapping(value = "/queryOrderById/{orderId}")
    @ResponseBody
    public Dto<Object> queryOrderById(@PathVariable Long orderId,HttpServletRequest request) {
        System.out.println("订单修改。。。。");
        try {
            String token=request.getHeader("token");
            System.out.println("token:"+token);
            JSONObject jsonObject = JSONObject.parseObject(jedis.get(token).toString());

            //将json字符串转成用户对象
            ItripUser currentUser = (ItripUser) JSONObject.toJavaObject(jsonObject,ItripUser.class);
            if(EmptyUtils.isEmpty(currentUser)){
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }
            ItripHotelOrder order = itripHotelOrderService.getItripHotelOrderById(orderId);
            if (EmptyUtils.isEmpty(order)) {
                return DtoUtil.returnFail("未查询到订单", "100533");
            }
            ItripModifyHotelOrderVO modifyVO = new ItripModifyHotelOrderVO();
            BeanUtils.copyProperties(order, modifyVO);
            System.out.println("入住日期："+modifyVO.getCheckInDate());
            System.out.println("入住日期："+modifyVO.getCheckOutDate());
            //itripModifyHotelOrderVO.setCheckInDate(new SimpleDateFormat("yyyy-MM-dd").parse(itripModifyHotelOrderVO.getCheckInDate()));
            //Map<String, Object> param = new HashMap<String, Object>();
            //param.put("orderId", order.getId());
            //List<ItripOrderLinkUserVo> itripOrderLinkUserList = itripOrderLinkUserService.getItripOrderLinkUserListByMap(param);
            // itripModifyHotelOrderVO.setItripOrderLinkUserList(itripOrderLinkUserList);
            return DtoUtil.returnSuccess("获取订单成功", modifyVO);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100534");
        }
    }
    }

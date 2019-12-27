package com.boot.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.boot.po.ItripHotelOrder;
import com.boot.service.ItripHotelOrderService;
import com.boot.util.AlipayConfig;
import com.boot.util.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class TradeController {
    @Autowired
    private ItripHotelOrderService itripHotelOrderService;

    public ItripHotelOrderService getItripHotelOrderService() {
        return itripHotelOrderService;
    }

    public void setItripHotelOrderService(ItripHotelOrderService itripHotelOrderService) {
        this.itripHotelOrderService = itripHotelOrderService;
    }

    /**
     * 确认订单信息
     *
     * @param //id
     *            通过订单编号获取订单信息并展示
     * @return
     */
    /*@ApiIgnore*/
    @RequestMapping(value = "/prepay/{orderNo}")
    public ModelAndView prePay(@PathVariable String orderNo, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("支付前订单确认.......");
        HttpSession session=request.getSession();
        if (EmptyUtils.isEmpty(orderNo)){
            System.out.println("订单编号不能为空！");
        }else {
            try {
                Map<String,Object> param=new HashMap<>();
                param.put("orderNo",orderNo);
                List<ItripHotelOrder> list=itripHotelOrderService.getItripHotelOrderListByMap(param);
                if (EmptyUtils.isEmpty(list)){
                    System.out.println("查询订单为空！");
                }else {
                    if (list.size()==1){
                        ItripHotelOrder order=list.get(0);
                        session.setAttribute("orderNo",orderNo);

                        session.setAttribute("hotelName",order.getHotelName());
                        session.setAttribute("payAmount",order.getPayAmount());
                        session.setAttribute("roomId",order.getRoomId());
                        session.setAttribute("count",order.getCount());
                        RedirectView redirectView=new RedirectView("/pay.jsp");
                        ModelAndView mv=new ModelAndView();
                        mv.setView(redirectView);
                        return mv;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("系统异常！");
            }
        }
        return  null;
    }
    /**
     * 向支付宝提交支付请求
     *
     * @param WIDout_trade_no
     *            商户订单号，商户网站订单系统中唯一订单号，必填
     * @param WIDsubject
     *            订单名称，必填
     * @param WIDtotal_amount
     *            付款金额，必填
     */
	/*@ApiOperation(value = "订单支付", httpMethod = "POST",
			protocols = "HTTP", produces = "application/xml", consumes="application/x-www-form-urlencoded",
			response =  String.class,
			WIDout_trade_no:订单编号
			WIDsubject:订单名称
			WIDtotal_amount:订单金额
			notes = "客户端提交订单支付请求，对该API的返回结果不用处理，浏览器将自动跳转至支付宝。<br><b>请使用普通表单提交，不能使用ajax异步提交。</b>")	*/
    @RequestMapping(value = "/pay")
    public void pay(String WIDout_trade_no,String WIDsubject,String WIDtotal_amount, HttpServletResponse response) {
        System.out.println("支付宝支付。。。。。");
        if (EmptyUtils.isEmpty(WIDout_trade_no)||EmptyUtils.isEmpty(WIDtotal_amount)||EmptyUtils.isEmpty(WIDsubject)){
            System.out.println("订单信息有错，请核查！");
        }else {
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,AlipayConfig.APP_ID,AlipayConfig.APP_PRIVATE_KEY,"JSON",AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.sign_type);
            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.return_url);
            alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

            //商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no =WIDout_trade_no;
            //付款金额，必填
            String total_amount =WIDtotal_amount;
            //订单名称，必填
            String subject=WIDsubject;
            //商品描述，可空
            String body="酒店预订";
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                    + "\"total_amount\":\""+ total_amount +"\","
                    + "\"subject\":\""+ subject +"\","
                    + "\"body\":\""+ body +"\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        /* alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+【商户订单号】+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+【支付金额】+"," +
                "    \"subject\":\""+【商品名称】+"\"," +
               "    \"body\":\""+【商品介绍】+"\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\""+aliPayConfig.getSysServiceProviderId()+"\"" +
                "    }"+
                "  }");//填充业务参数
        */
            //请求
            String form="";
            try {
                form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            try {
                response.getWriter().write(form);//直接将完整的表单html输出到页面
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("输出异常！");
            }
        }
    }
    /**
     * 支付宝页面跳转同步通知页面
     */
    @RequestMapping(value = "/return")
    public ModelAndView callback(String WIDout_trade_no,String WIDsubject,String WIDtotal_amount, HttpServletResponse response) {
        System.out.println("支付成功。。。。。");
        System.out.println("截取前订单编号:" + WIDout_trade_no);
        String orderNo=WIDout_trade_no.substring(WIDout_trade_no.indexOf(",")+1,WIDout_trade_no.length());
        System.out.println("截取后订单编号:"+orderNo);
        if (EmptyUtils.isEmpty(orderNo)){
            System.out.println("订单返回为空");
        }else {
            try {
                Integer flag=itripHotelOrderService.updateHotelOrderStatusPayOk(orderNo);
                if (flag>0){
                    setTime(orderNo);
                }
                RedirectView redirectView=new RedirectView("Http://127.0.0.1:8085/index.html");
                ModelAndView mv=new ModelAndView();
                mv.setView(redirectView);
                System.out.println("付款成功！");
                return  mv;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("系统异常！");
            }
        }
        return null;
    }
    public void setTime(String orderNo){
        System.out.println("定时器调用成功！");
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    itripHotelOrderService.updateHotelOrderStatusSureGoIn(orderNo);
                    System.out.println("入住更改成功！");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("入住更改失败！");
                }
            }
        },60000);
    }
}

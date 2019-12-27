package com.boot.controller;

import com.alibaba.fastjson.JSONObject;
import com.boot.po.Dto;
import com.boot.po.ItripUser;
import com.boot.po.ItripUserLinkUser;
import com.boot.service.ItripUserLinkUserService;
import com.boot.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "/api/userinfo")
public class ItripUserLinkUserController {
    @Autowired
    private ItripUserLinkUserService itripUserLinkUserService;

    public ItripUserLinkUserService getItripUserLinkUserService() {
        return itripUserLinkUserService;
    }

    public void setItripUserLinkUserService(ItripUserLinkUserService itripUserLinkUserService) {
        this.itripUserLinkUserService = itripUserLinkUserService;
    }
    private Jedis jedis=new Jedis("127.0.0.1",6379);

    @RequestMapping(value = "/adduserlinkuser")
    public Dto addItripUserLinkUser(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO){
        System.out.println("添加");
        //找到缓存中的存储内容
        String strtoken=jedis.get(jedis.get("token"));
        //将json格式的内容 转为 字符
        JSONObject jsonObject= JSONObject.parseObject(strtoken.toString());
        //将jsonobject字符串转为user
        ItripUser currentUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        System.out.println("currentUSer:"+currentUser.toString());
        //前后台转存处理
        ItripUserLinkUser user=new ItripUserLinkUser();
        if (currentUser!=null){
            if (itripAddUserLinkUserVO!=null){
                user.setLinkUserName(itripAddUserLinkUserVO.getLinkUserName());
                user.setLinkIdCard(itripAddUserLinkUserVO.getLinkIdCard());
                user.setLinkPhone(itripAddUserLinkUserVO.getLinkPhone());
                user.setCreationDate(new Date());
                user.setLinkIdCardType(0);
                //获取存入者id
                user.setCreatedBy(currentUser.getId());
                user.setUserId(currentUser.getId());
                try {
                    System.out.println("新增联系人列表"+user.toString());
                    int i=itripUserLinkUserService.insertItripUserLinkUser(user);
                    System.out.println("cccccccc:"+i);
                }catch (Exception e){
                    e.printStackTrace();
                    return DtoUtil.returnFail("新增常用联系人失败", "100411");
                }
                return DtoUtil.returnSuccess("新增常用联系人成功");
            }else {
                return DtoUtil.returnFail("提交信息为空，请重新填写内容", "100412");
            }
        }else {
            return DtoUtil.returnFail("token失效，请重新登录", "100000");
        }
    }

    @RequestMapping(value = "/queryuserlinkuser")
    public  Dto<ItripUserLinkUser> queryUserLinkUser(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripSearchUserLinkUserVO itripSearchUserLinkUserVO){
        //查询需要返回一个list集合给前台用以展示内容

        //找到缓存中的存储内容
        String strtoken=jedis.get(jedis.get("token"));
        //将json格式的内容 转为 字符
        JSONObject jsonObject= JSONObject.parseObject(strtoken.toString());
        //将jsonobject字符串转为user对象
        ItripUser currentUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (strtoken!=null){
            if (itripSearchUserLinkUserVO!=null){
                //获取关键信息，
                //可通过姓名查找
                String linkusername=itripSearchUserLinkUserVO.getLinkUserName();
                //可通过添加人编号查找
                long userid=currentUser.getId();
                Map map=new HashMap();
                //靠linkUserName单个查询
                map.put("linkUserName",linkusername);
                //靠userId查询全部
                map.put("userId",userid);
                try {
                    //集合接收查询到的内容
                    List<ItripUserLinkUser> userLink=new ArrayList<ItripUserLinkUser>();
                    userLink=itripUserLinkUserService.getItripUserLinkUserListByMap(map);
                    for(int i=0;i<userLink.size();i++){
                        System.out.println("查询返回结果："+userLink.get(i).toString());
                    }
                    return DtoUtil.returnSuccess("获取常用联系人信息成功",userLink);
                } catch (Exception e) {
                    e.printStackTrace();
                    return DtoUtil.returnFail("获取常用联系人信息失败","100401");
                }
            }else {
                return DtoUtil.returnFail("获取常用联系人信息失败","100401");
            }
        }else {
            return DtoUtil.returnFail("token失效，请重新登陆","100000");
        }
    }

        @RequestMapping(value="/deluserlinkuser")
        @ResponseBody
        public Dto deleteItripUserLinkUserByIds(HttpServletRequest request, HttpServletResponse response,Long[] ids){
            System.out.println("删除");
            //找到缓存中的存储内容
            String strtoken=jedis.get(jedis.get("token"));
            //将json格式的内容 转为 字符
            JSONObject jsonObject= JSONObject.parseObject(strtoken.toString());
            //将jsonobject字符串转为user对象
            ItripUser currentUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
            List<Long> idsList = new ArrayList<Long>();//订单部分
        if (EmptyUtils.isNotEmpty(ids)){
            try {
                //订单部分暂时不处理
               /* List<Long> linkUserIds = itripOrderLinkUserService.getItripOrderLinkUserIdsByOrder();
                Collections.addAll(idsList, ids);
                idsList.retainAll(linkUserIds);
                if(idsList.size() > 0)
                {
                    return DtoUtil.returnFail("所选的常用联系人中有与某条待支付的订单关联的项，无法删除","100431");
                }else{*/
                itripUserLinkUserService.deleteItripUserLinkUserByIds(ids);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("删除失败", "100432");
            }
            return DtoUtil.returnSuccess("删除成功");
        }
        return null;
        }

    @RequestMapping(value="/modifyuserlinkuser")
    public Dto<Object> updateUserLinkUser(@RequestBody ItripModifyUserLinkUserVO itripModifyUserLinkUserVO, HttpServletRequest request, HttpServletResponse response){
        System.out.println("修改");
        //找到缓存中的存储内容
        String strtoken=jedis.get(jedis.get("token"));
        //将json格式的内容 转为 字符
        JSONObject jsonObject= JSONObject.parseObject(strtoken.toString());
        //将jsonobject字符串转为user对象
        ItripUser currentUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        ItripUserLinkUser user=new ItripUserLinkUser();
        if (EmptyUtils.isNotEmpty(strtoken)){
            if (EmptyUtils.isNotEmpty(itripModifyUserLinkUserVO)){
                user.setId(itripModifyUserLinkUserVO.getId());
                System.out.println(user.getId()+"cccccc");
                user.setLinkUserName(itripModifyUserLinkUserVO.getLinkUserName());
                user.setLinkIdCard(itripModifyUserLinkUserVO.getLinkIdCard());
                user.setLinkPhone(itripModifyUserLinkUserVO.getLinkPhone());
                user.setModifiedBy(currentUser.getId());
                /*
                Problem：日期会报错java.lang.IllegalArgumentException: invalid comparison: java.util.Date and java.lang.String
                Reason：编写mybatis的XML文件出错，在判空时，加入了判断空字符串的语句，无法比较 java.util.Date类型与  java.lang.Stringd的""
                Solve：去除XML文件中对于日期判""空值的操作modifydate!=''
                */
                user.setCreationDate(new Date());
                System.out.println(user.toString());
                try {
                    itripUserLinkUserService.updateItripUserLinkUser(user);
                    return DtoUtil.returnSuccess("修改常用联系人成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    return DtoUtil.returnFail("修改常用联系人失败","100421");
                }
            }else {
                return DtoUtil.returnFail("不能提交空，请填写常用联系人信息","100422");
            }
        }else {
            return DtoUtil.returnFail("token失效，请重新登录", "100000");
        }
    }
}

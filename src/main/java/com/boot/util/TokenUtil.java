package com.boot.util;

import com.alibaba.fastjson.JSON;
import com.boot.po.ItripUser;
import cz.mallat.uasparser.UserAgentInfo;
import redis.clients.jedis.Jedis;

import javax.xml.transform.Source;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TokenUtil {
    private static String tokenPrefix = "token:";//统一加入 token前缀标识
    private static Jedis jedis = new Jedis("127.0.0.1", 6379);
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }
    /***
     * @param agent Http头中的user-agent信息
     * @param user 用户信息
     * @return Token格式<br/>
     * 	PC：“前缀PC-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     *  <br/>
     *  Android：“前缀ANDROID-USERCODE-USERID-CREATIONDATE-RONDEM[6位]”
     *  生成token
     */
    public static String getTokenGenerator(String agent, ItripUser user) {
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(agent);
            System.out.println("UA-Info-->"+userAgentInfo.toString()+"\n*****************************");
            StringBuilder sb = new StringBuilder();
            sb.append(tokenPrefix);//统一前缀
            if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
                if (UserAgentUtil.CheckAgent(agent)) {
                    sb.append("MOBILE-");
                } else {
                    sb.append("PC-");
                }
            } else if (userAgentInfo.getDeviceType()
                    .equals("Personal computer")) {

                sb.append("PC-");
            } else {
                sb.append("MOBILE-");
            }
                sb.append(MD5Util.getMd5(user.getUsercode(), 32) + "-");//加密用户名称
                sb.append(user.getId() + "-");//用户编号
                sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ "-");//日期
                sb.append(MD5Util.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码
            System.out.println("统一前缀sb"+sb);
            return sb.toString();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return  null;
    }
    /*token时间验证*/
    public static boolean validate(String agent, String token) {
        System.out.println("时间验证");
        if (token==null) {// token不存在
            System.out.println("token不存在");
            return false;
        }
        try {
            Date TokenGenTime=null;// token生成时间
            String agentMD5=null;
            String[] tokenDetails = token.split("-");//以-分割
            System.out.println("tokenDetails："+tokenDetails);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间
            TokenGenTime = formatter.parse(tokenDetails[3]);//获取到创建时间
            System.out.println("Token存入时间TokenGenTime:"+TokenGenTime);
            long passed = Calendar.getInstance().getTimeInMillis()
                    - TokenGenTime.getTime();//获取时间间隔
            if(passed>60*60*1000) {//超出时间限制
                System.out.println("token时间："+passed);
                System.out.println("间隔："+1000*60);
                return false;
            }
            agentMD5 = tokenDetails[4];
            if(MD5Util.getMd5(agent, 6).equals(agentMD5))
                return true;
        } catch (ParseException e) {
            return false;
        }
        return false;
    }
   /*删除token*/
    public static void delete(String token) {
        if (jedis.get(token)!=null) {
            jedis.del(token);
        }
    }
    /*置换token*/
    public static String replaceToken(String agent, String token)
            throws TokenValidationFailedException {
        System.out.println("进入置换");
        // 验证旧token是否有效
        if (jedis.get(token)==null) {// token不存在
            throw new TokenValidationFailedException("未知的token或 token已过期");// 终止置换
        }
        Date TokenGenTime;// token生成时间
        try {
            String[] tokenDetails = token.split("-");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            TokenGenTime = formatter.parse(tokenDetails[3]);
        } catch (ParseException e) {
            throw new TokenValidationFailedException("token格式错误:" + token);
        }

        long passed = Calendar.getInstance().getTimeInMillis()
                - TokenGenTime.getTime();// token已产生时间
        if (passed <  1000) {// 置换保护期内
            throw new TokenValidationFailedException("token处于置换保护期内，剩余"
                    + (1000 - passed) / 1000
                    + "(s),禁止置换");
        }
        // 置换token
        String newToken = "";
      /* List<ItripUser> listuser = JSON.parseArray(jedis.get(token).toString(),ItripUser.class);
       ItripUser user=listuser.get(0);
        long ttl = jedis.ttl(token);// token有效期（剩余秒数 ）
        if (ttl > 0 || ttl == -1) {// 兼容手机与PC端的token在有效期
            newToken = getTokenGenerator(agent, user);
            //this.save(newToken, user);// 缓存新token
            //将token存入redis(因为将来取值时需要返回对象所以转成json存进去)
            String strJson = JSON.toJSONString(user);
            jedis.setex(newToken,7200,strJson);
            jedis.setex(token, 7200,
                    JSON.toJSONString(user));// 2分钟后旧token过期，注意手机端由永久有效变为2分钟（REPLACEMENT_DELAY默认值）后失效
        } else {// 其它未考虑情况，不予置换
            throw new TokenValidationFailedException("当前token的过期时间异常,禁止置换");
        }*/
        return newToken;
    }
}

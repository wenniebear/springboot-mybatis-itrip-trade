package com.boot.service;

import com.boot.po.ItripUserLinkUser;

import java.util.List;
import java.util.Map;

public interface ItripUserLinkUserService {
    public int insertItripUserLinkUser(ItripUserLinkUser linkUser)throws Exception;
    //查询常用联系人
    public List<ItripUserLinkUser> getItripUserLinkUserListByMap(Map<String,Object> param)throws Exception;
    //删除常用联系人
    public int deleteItripUserLinkUserByIds(Long[] ids)throws Exception;
    //修改常用联系人
    public int updateItripUserLinkUser(ItripUserLinkUser linkUser)throws Exception;
}

package com.boot.mapper;

import com.boot.po.ItripUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItripUserMapper {
    int deleteByPrimaryKey(Long id);
    //添加
    int insert(ItripUser record);

    int insertSelective(ItripUser record);

    ItripUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ItripUser record);

    int updateByPrimaryKey(ItripUser record);
    //查找数据库看有无用户重复
    public ItripUser findByUsercode(ItripUser user);
    //激活账户
    public int updateUserActivated(ItripUser user);
    //登录账户
    public ItripUser dologin(ItripUser user);
}
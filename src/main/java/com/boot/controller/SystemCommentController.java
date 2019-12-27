package com.boot.controller;

import com.boot.po.Dto;
import com.boot.service.ItripCommonService;
import com.boot.service.ItripImageService;
import com.boot.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/comment")
public class SystemCommentController {
    @Autowired
    private ItripCommonService itripCommonService;

    public ItripCommonService getItripCommonService() {
        return itripCommonService;
    }

    public void setItripCommonService(ItripCommonService itripCommonService) {
        this.itripCommonService = itripCommonService;
    }
    @Autowired
    private ItripImageService itripImageService;

    public ItripImageService getItripImageService() {
        return itripImageService;
    }

    public void setItripImageService(ItripImageService itripImageService) {
        this.itripImageService = itripImageService;
    }

    /*  @ApiOperation(value = "据酒店id查询酒店平均分", httpMethod = "GET",
                        protocols = "HTTP",produces = "application/json",
                        response = Dto.class,notes = "总体评分、位置评分、设施评分、服务评分、卫生评分"+
                        "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                        "<p>错误码：</p>"+
                        "<p>100001 : 获取评分失败 </p>"+
                        "<p>100002 : hotelId不能为空</p>")*/
    @RequestMapping(value = "/gethotelscore/{hotelId}")
    @ResponseBody
    public Dto<Object> getHotelScore(@PathVariable String hotelId) {
        System.out.println("根据酒店id查询酒店平均分。。。");
        Dto<Object> dto=new Dto<>();
        ItripScoreCommentVO itripScoreCommentVO=null;
        if (EmptyUtils.isNotEmpty(hotelId)){
            try {
                itripScoreCommentVO= itripCommonService.getCommentAvgScore(Long.valueOf(hotelId));
                dto=DtoUtil.returnDataSuccess(itripScoreCommentVO);
            } catch (Exception e) {
                dto=DtoUtil.returnFail("获取评分失败","100001");
            }
        }else {
            dto= DtoUtil.returnFail("hotelId不能为空","100002");
        }
        return dto;
    }
    /*@ApiOperation(value = "根据酒店id查询各类评论数量", httpMethod = "GET",
			protocols = "HTTP",produces = "application/json",
			response = Dto.class,notes = "根据酒店id查询评论数量（全部评论、值得推荐、有待改善、有图片）"+
			"<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
			"<p>错误码：</p>"+
			"<p>100014 : 获取酒店总评论数失败 </p>"+
			"<p>100015 : 获取酒店有图片评论数失败</p>"+
			"<p>100016 : 获取酒店有待改善评论数失败</p>"+
			"<p>100017 : 获取酒店值得推荐评论数失败</p>"+
			"<p>100018 : 参数hotelId为空</p>")*/
    @RequestMapping(value = "/getcount/{hotelId}")
    @ResponseBody
    public Dto<Object> getCommentCountByType(@PathVariable String hotelId) {
        System.out.println("根据酒店id查询各类评论数。。。");
        Dto<Object> dto=new Dto<>();
        Map<String,Object> param=new HashMap();
        Map<String,Object> countMap = new HashMap<>();
        Integer count=-1;
        if (EmptyUtils.isNotEmpty(hotelId)){
            try {
                param.put("hotelId",hotelId);
                count=itripCommonService.getItripCommentCountByMap(param);
                if (count!=-1){
                    countMap.put("allcomment",count);
                }else {
                    return DtoUtil.returnFail("获取酒店总评论数失败","100014");
                }
                param.put("isOk",1);
                count=itripCommonService.getItripCommentCountByMap(param);
                if (count!=-1){
                    countMap.put("isok",count);
                }else {
                    return DtoUtil.returnFail("获取酒店值得推荐评论数失败","100017");
                }
                param.put("isOk",0);
                count=itripCommonService.getItripCommentCountByMap(param);
                if (count!=-1){
                    countMap.put("improve",count);
                }else {
                    return DtoUtil.returnFail("获取酒店有待改善评论数失败","100016");
                }
                param.put("isHavingImg",1);//0:无图片 1:有图片
                param.put("isOk",null);
                count=itripCommonService.getItripCommentCountByMap(param);
                if(EmptyUtils.isNotEmpty(count)){
                    countMap.put("havingimg",count);
                }else{
                    return DtoUtil.returnFail("获取酒店有图片评论数失败","100015");
                }
                dto=DtoUtil.returnDataSuccess(countMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            dto=DtoUtil.returnFail("参数hotelId为空","100018");
        }
        return dto;
    }
    /*@ApiOperation(value = "根据评论类型查询评论列表，并分页显示", httpMethod = "POST",
			protocols = "HTTP",produces = "application/json",
			response = Dto.class,notes = "根据评论类型查询评论列表，并分页显示"+
			"<p>参数数据e.g：</p>" +
			"<p>全部评论：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":-1,\"pageSize\":5,\"pageNo\":1}</p>" +
			"<p>有图片：{\"hotelId\":10,\"isHavingImg\":1,\"isOk\":-1,\"pageSize\":5,\"pageNo\":1}</p>" +
			"<p>值得推荐：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":1,\"pageSize\":5,\"pageNo\":1}</p>" +
			"<p>有待改善：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":0,\"pageSize\":5,\"pageNo\":1}</p>" +
			"<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
			"<p>错误码：</p>"+
			"<p>100020 : 获取评论列表错误 </p>")*/
    @RequestMapping(value = "/getcommentlist")
    @ResponseBody
    public Dto<Object> getCommentList(@RequestBody ItripSearchCommentVO itripSearchCommentVO) {
        System.out.println("根据酒店id查询评论分页列表");
        Dto<Object> dto = new Dto<Object>();
        //是否有图片和是否有改进的条件
        if (itripSearchCommentVO.getIsOk()==-1){
            itripSearchCommentVO.setIsOk(null);
        }
        if (itripSearchCommentVO.getIsHavingImg()==-1){
            itripSearchCommentVO.setIsHavingImg(null);
        }
        Map<String, Object> param = new HashMap<>();
        param.put("isOk",itripSearchCommentVO.getIsOk());
        param.put("isHavingImg",itripSearchCommentVO.getIsHavingImg());
        param.put("hotelId",itripSearchCommentVO.getHotelId());
        try {
            Page page=itripCommonService.getItripCommentListByMap(param,itripSearchCommentVO.getPageNo(),itripSearchCommentVO.getPageSize());
            dto=DtoUtil.returnDataSuccess(page);
        } catch (Exception e) {
            e.printStackTrace();
            dto=DtoUtil.returnFail("获取评论列表错误","100020");
        }
        return dto;
    }
    /*@ApiOperation(value = "根据targetId查询评论照片(type=2)", httpMethod = "GET",
			protocols = "HTTP",produces = "application/json",
			response = Dto.class,notes = "总体评分、位置评分、设施评分、服务评分、卫生评分"+
			"<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
			"<p>错误码：</p>"+
			"<p>100012 : 获取评论图片失败 </p>"+
			"<p>100013 : 评论id不能为空</p>")*/
    @RequestMapping(value = "/getimg/{targetId}")
    public Dto<Object> getImgByTargetId(@PathVariable String targetId) {
        System.out.println("根据targetId查询评论照片");
        Dto<Object> dto = new Dto<Object>();
        if (EmptyUtils.isNotEmpty(targetId)){
            try {
                Map<String,Object> param=new HashMap<>();
                param.put("type",2);//图片2----评论图片
                param.put("targetId",targetId);
                List<ItripImageVO> list=null;
                list=itripImageService.getItripImageListByMap(param);
                dto=DtoUtil.returnDataSuccess(list);
            } catch (Exception e) {
                e.printStackTrace();
                dto=DtoUtil.returnFail("获取评论图片失败","100012");
            }
        }else {
            dto=DtoUtil.returnFail("评论id不能为空","100013");
        }
        return dto;
    }
    }

package com.zjg.blog.controller.admin.ajax;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.TimesStatistic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(description = "AdminAjaxSysView")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxSysView extends BaseController {
    /**
     * SysView
     */
    @ApiOperation("获取访问记录统计")
    @GetMapping(value = "/view/count/{days}")
    public  String getViewCount(@PathVariable(name = "days") int days){
        JSONObject result=new JSONObject();
        result.put("code",200);
        List<TimesStatistic> timesStatisticList=sysViewService.queryViewCountByDays(days);
        result.put("total",timesStatisticList.size());
        result.put("rows",timesStatisticList);
        return result.toJSONString();
    }
    @ApiOperation("删除指定访问记录(id)")
    @DeleteMapping(value = "/view/{id}")
    public String deleteView(@PathVariable(value = "id") long id){
        JSONObject result=new JSONObject();
        sysViewService.deleteView(id);
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("查看访问记录 （后台 dataTable结合）")
    @GetMapping(value = "/view/list")
    public String getViewList(@RequestParam(value = "draw")int draw, @RequestParam(value = "start")long start,
                              @RequestParam(value = "length")int pageSize, @RequestParam(value = "search[value]")String searchValue,
                              @RequestParam(value = "order[0][column]")int column, @RequestParam(value = "order[0][dir]")String orderDirection){
        String orderProperty=null;//排序属性
        switch (column){//dataTable列号与表字段映射，详情请查看插件官网 http://datatables.club/manual/server-side.html
            case 1:orderProperty="id";break;
            case 2:orderProperty="ip";break;
            case 3:orderProperty="address";break;
            case 4:orderProperty="isp";break;
            case 5:orderProperty="operate_by";break;
            default:orderProperty="create_by";break;
        }
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=sysViewService.queryViews(pageNum,pageSize,searchValue,orderProperty,orderDirection);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    @ApiOperation("清除浏览记录")
    @DeleteMapping(value = "/view/out/{days}")
    public String deleteViewOutOfDays(@PathVariable(name="days")int days){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",sysViewService.wipeViewOutOfDays(days));
        return result.toJSONString();
    }
}

package com.zjg.blog.controller.admin.ajax;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(description = "AdminAjaxSysLog")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxSysLog extends BaseController {
    /**
     * SysLog
     */
    @ApiOperation("查看访问日志 （后台 dataTable结合）")
    @GetMapping(value = "/log/list")
    public String getLogList(@RequestParam(value = "draw")int draw, @RequestParam(value = "start")long start,
                             @RequestParam(value = "length")int pageSize, @RequestParam(value = "search[value]")String searchValue,
                             @RequestParam(value = "order[0][column]")int column, @RequestParam(value = "order[0][dir]")String orderDirection){
        String orderProperty=null;//排序属性
        switch (column){//dataTable列号与表字段映射，详情请查看插件官网 http://datatables.club/manual/server-side.html
            case 1:orderProperty="id";break;
            case 2:orderProperty="remark";break;
            case 3:orderProperty="operate_url";break;
            case 4:orderProperty="operate_by";break;
            default:orderProperty="create_by";break;
        }
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=sysLogService.queryLogs(pageNum,pageSize,searchValue,orderProperty,orderDirection);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    @ApiOperation("删除指定日志（id）")
    @DeleteMapping(value = "/log/{id}")
    public String deleteLog(@PathVariable(value = "id")long id){
        JSONObject result=new JSONObject();
        sysLogService.deleteLog(id);
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("清除日志")
    @DeleteMapping(value = "/log/out/{days}")
    public String deleteLogOutOfDays(@PathVariable(name="days")int days){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",sysLogService.wipeOutOfDays(days));
        return result.toJSONString();
    }
}

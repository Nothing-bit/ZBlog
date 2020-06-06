package com.zjg.blog.controller.admin.ajax;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.TagInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(description = "AdminAjaxTagInfo")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxTagInfo extends BaseController {
    /**
     * tagInfo
     */
    @ApiOperation("查看标签列表 （后台 dataTable结合）")
    @GetMapping(value = "/tag/list")
    public String getTagList(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,
                             @RequestParam(value = "length")int pageSize,@RequestParam(value = "search[value]")String searchValue,
                             @RequestParam(value = "order[0][column]")int column,@RequestParam(value = "order[0][dir]")String orderDirection){
        String orderProperty=null;//排序属性
        switch (column){//dataTable列号与表字段映射，详情请查看插件官网 http://datatables.club/manual/server-side.html
            case 1:orderProperty="id";break;
            case 2:orderProperty="name";break;
            case 3:orderProperty="number";break;
            default:orderProperty="create_by";break;
        }
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=tagInfoService.queryTagInfos(pageNum,pageSize,searchValue,orderProperty,orderDirection);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    @ApiOperation("新增一个标签")
    @PostMapping(value = "/tag")
    public String addTagInfo(@RequestBody TagInfo tagInfo){
        JSONObject result=new JSONObject();
        tagInfoService.addTagInfo(tagInfo);
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("删除一个标签")
    @DeleteMapping(value = "/tag/{id}")
    public String deleteTagById(@PathVariable(value ="id")long id){
        JSONObject result=new JSONObject();
        tagInfoService.deleteTagById(id);
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("更新标签信息")
    @PutMapping(value = "/tag/{id}")
    public String updateTagInfo(@RequestBody TagInfo tagInfo, @PathVariable(value = "id") long id){
        JSONObject result=new JSONObject();
        tagInfo.setId(id);
        tagInfoService.updateTagInfo(tagInfo);
        result.put("code",200);
        return result.toJSONString();
    }
    @ApiOperation("模糊查询标签信息")
    @GetMapping(value = "/tag/name/like")
    public String getTagNameLike(@RequestParam(value = "tagName") String tagName){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("data",tagInfoService.queryTagInfosNameLike(tagName));
        return result.toJSONString();
    }
}

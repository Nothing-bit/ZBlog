package com.zjg.blog.controller.admin.ajax;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.entity.CategoryInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "AdminAjaxCategory")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxCategory extends BaseController {
    /**
     * 文章分类管理
     */
    /**
     *
     * create 2020年4月6日18:55:26
     * author zjg
     */
    @ApiOperation("获取全部分类信息")
    @GetMapping(value = "/category/all")
    public String getAllCategoryInfo(){
        JSONObject result=new JSONObject();
        List<CategoryInfo>categoryInfoList=categoryInfoService.queryAll();
        result.put("code",200);
        result.put("total",categoryInfoList.size());
        result.put("rows",categoryInfoList);
        return result.toJSONString();
    }
    @ApiOperation("增加文章分类")
    @PostMapping(value = "/category")
    public String postCategory(@RequestBody CategoryInfo categoryInfo){
        JSONObject result=new JSONObject();
        if(categoryInfoService.addCategory(categoryInfo)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    @ApiOperation("更新分类信息")
    @PutMapping(value = "/category/{id}")
    public String putCategory(@RequestBody CategoryInfo categoryInfo,@PathVariable long id){
        JSONObject result=new JSONObject();
        categoryInfo.setId(id);
        if(categoryInfoService.updateCategory(categoryInfo)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }
    @ApiOperation("删除分类信息")
    @DeleteMapping(value="/category/{id}")
    public String deleteCategory(@PathVariable long id){
        JSONObject result=new JSONObject();
        if(categoryInfoService.deleteCategoryById(id)==1){
            result.put("code",200);
        }
        return result.toJSONString();
    }

    /**
     * category table新增排序搜索
     * update 2020年4月2日10:55:37
     * author zjg
     */
    @ApiOperation("获取分类信息 (后台 dataTable结合）")
    @GetMapping(value = "/category/list")
    public String getCategoryList(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,
                                  @RequestParam(value = "length")int pageSize,@RequestParam(value = "search[value]")String searchValue,
                                  @RequestParam(value = "order[0][column]")int column,@RequestParam(value = "order[0][dir]")String orderDirection){
        String orderProperty=null;//排序属性
        switch (column){//dataTable列号与表字段映射，详情请查看插件官网 http://datatables.club/manual/server-side.html
            case 1:orderProperty="id";break;
            case 2:orderProperty="name";break;
            case 3:orderProperty="number";break;
            case 4:orderProperty="modified_by";break;
            default:orderProperty="create_by";break;
        }
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=categoryInfoService.queryCategories(pageNum,pageSize,searchValue,orderProperty,orderDirection);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
}

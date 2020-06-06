package com.zjg.blog.controller.admin.ajax;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.controller.BaseController;
import com.zjg.blog.dto.ArticleView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "AdminAjaxArticle")
@RestController
@RequestMapping(value = "/admin")
public class AdminAjaxArticle extends BaseController {
    /**
     * article
     */
    @ApiOperation(value = "获取单个全部文章信息")
    @GetMapping(value = "/article/{id}")
    @ApiImplicitParam(paramType = "path",name="id",value = "article Id",required = true,dataType = "Long")
    public String getArticle(@PathVariable Long id){

        JSONObject result=new JSONObject();
        ArticleView view=articleService.getOneById(id);
        result.put("code",200);
        result.put("article",view);
        return result.toJSONString();
    }

    @ApiOperation(value = "修改一篇文章")
    @ApiImplicitParam(paramType = "path",name="id",value = "文章id",required = true,dataType = "Long")
    @PutMapping(value = "/article/{id}")
    public String putArticle(@PathVariable long id,@RequestBody ArticleView dto){
        JSONObject result=new JSONObject();
        dto.setId(id);
        if(articleService.updateArticle(dto)==1){
            result.put("code",200);
        }else {
            result.put("code",100);
        }
        return result.toJSONString();
    }

    @ApiOperation("删除一篇文章")
    @ApiImplicitParam(name="id",value = "文章id",required = true,dataType = "long",paramType = "path")
    @DeleteMapping(value = "/article/{id}")
    public String deleteArticle(@PathVariable long id){
        JSONObject result=new JSONObject();
        if(articleService.deleteArticle(id)==1){
            result.put("code",200);
        }else{
            result.put("code",100);
        }
        return result.toJSONString();
    }

    @ApiOperation("新增一篇文章")
    @PostMapping(value = "/article")
    public String postArticle(@RequestBody ArticleView dto){
        JSONObject result=new JSONObject();
        if(articleService.insertArticle(dto)==1){
            result.put("code",200);
        }else{
            result.put("code",100);
        }
        return result.toJSONString();
    }

    /**
     * article dataTable新增排序，搜索
     * update 2020年4月1日20:10:27
     * author zjg
     */
    @ApiOperation("获取文章（后台 dataTable结合）")
    @GetMapping(value = "/article/list")
    public String getArticleList(@RequestParam(value = "draw")int draw,@RequestParam(value = "start")long start,
                                 @RequestParam(value = "length")int pageSize,@RequestParam(value = "search[value]")String searchValue,
                                 @RequestParam(value = "order[0][column]")int column,@RequestParam(value = "order[0][dir]")String orderDirection){
        String orderProperty=null;//排序属性
        switch (column){//dataTable列号与表字段映射，详情请查看插件官网 http://datatables.club/manual/server-side.html
            case 1:orderProperty="id";break;
            case 2:orderProperty="title";break;
            case 3:orderProperty="summary";break;
            case 4:orderProperty="category_id";break;
            case 5:orderProperty="is_top";break;
            case 6:orderProperty="is_privated";break;
            case 7:orderProperty="traffic";break;
            default:orderProperty="create_by";break;
        }
        JSONObject result=new JSONObject();
        int pageNum=(int)start/pageSize+1;
        PageInfo pageInfo=articleService.queryPreviews(pageNum,pageSize,searchValue,orderProperty,orderDirection);
        result.put("draw",draw);
        result.put("recordsTotal",pageInfo.getTotal());
        result.put("recordsFiltered",pageInfo.getTotal());
        result.put("data",pageInfo.getList());
        return result.toJSONString();
    }
    /**
     * 图片上传
     */
    @ApiOperation("上传图片")
    @PostMapping(value = "/upload/images")
    public String postPicture(@RequestParam("upload") MultipartFile file){
        JSONObject result=new JSONObject();
        result.put("code",200);
        result.put("uploaded",1);
        result.put("filename","test");
        result.put("url",pictureService.uploadPicture(file));
        return result.toJSONString();
    }
}

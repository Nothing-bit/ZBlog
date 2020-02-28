package com.zjg.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.dao.*;
import com.zjg.blog.dto.ArticlePreviewDto;
import com.zjg.blog.dto.ArticleViewDto;
import com.zjg.blog.entity.*;
import com.zjg.blog.service.ArticleService;
import com.zjg.blog.util.PageInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ArticleServiceImpl implements ArticleService {
    @Value("${images.upload.path}")
    private String picturePath;
    @Autowired
    private ArticleInfoMapper infoMapper;
    @Autowired
    private ArticleContentMapper contentMapper;
    @Autowired
    private CategoryInfoMapper categoryInfoMapper;
    @Autowired
    private ArticlePictureMapper pictureMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagInfoMapper tagInfoMapper;
    /**
     *  根据文件名链表实现批量删除
     * @param fileList
     * @return
     */
    private boolean deleteFiles(List<String> fileList){
        for(int i=0;i<fileList.size();i++){
            String filename=fileList.get(i);
            File file=new File(filename);
            if(file.exists()){
                file.delete();
            }
        }
        return true;
    }

    /**
     * 修改新图片的文章归属
     * @param id
     * @return
     */
    private boolean updatePictureArticleId(long id){
        ArticlePicture picture=new ArticlePicture();
        picture.setArticleId(id);
        picture.setPictureUrl(null);
        picture.setId(null);
        ArticlePictureExample example=new ArticlePictureExample();
        ArticlePictureExample.Criteria pictureCritera=example.createCriteria();
        pictureCritera.andArticleIdEqualTo(-1L);
        pictureMapper.updateByExampleSelective(picture,example);
        return  true;
    }

    /**
     * 组装dto传输对象
     * @param daoList
     * @return
     */
    private List<ArticlePreviewDto> assembleDtoList(List<ArticleInfo> daoList){
        List<ArticlePreviewDto>dtoList=new ArrayList<>();
        if(daoList.size()>0){
            for(ArticleInfo item:daoList){
                CategoryInfo categoryInfo=categoryInfoMapper.selectByPrimaryKey(item.getCategoryId());
                ArticlePreviewDto preview=new ArticlePreviewDto();

                List<String> tagList=queryTags(item.getId());
                BeanUtil.copyProperties(item,preview);
                preview.setTraffic(item.getTraffic());
                preview.setCategoryName(categoryInfo.getName());
                preview.setTagList(tagList);
                dtoList.add(preview);
            }
        }
        return dtoList;
    }


    /**
     *根据条件进行分页查询info
     * @param example
     * @param pageNum
     * @param pageSize
     * @return
     */
    private PageInfo<ArticlePreviewDto> getPageInfo(ArticleInfoExample example, int pageNum, int pageSize){

        PageHelper.startPage(pageNum,pageSize);
        List<ArticleInfo>daoList=infoMapper.selectByExample(example);
        PageInfo<ArticleInfo> daoPageInfo=new PageInfo<>(daoList);
        List<ArticlePreviewDto>dtoList=assembleDtoList(daoList);
        PageInfo<ArticlePreviewDto> dtoPageInfo=new PageInfo<>(dtoList);
        PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
        return dtoPageInfo;
    }

    /**
     * 插入日志tag，并且维护tagInfo表（有则返回，无则添加）
     * @param tagList
     * @param articleId
     * @return
     */
    private int insertTags(List<String> tagList,long articleId){
        for(String item:tagList){
            TagInfoExample tagInfoExample=new TagInfoExample();
            TagInfoExample.Criteria tagInfoCriteria=tagInfoExample.createCriteria();
            tagInfoCriteria.andNameEqualTo(item);
            List<TagInfo> tagInfoList=tagInfoMapper.selectByExample(tagInfoExample);
            TagInfo tagInfo=new TagInfo();
            if(tagInfoList.size()==0){
                tagInfo.setName(item);
                tagInfo.setNumber(0L);
                tagInfo.setCreateBy(new Date());
                tagInfoMapper.insert(tagInfo);
            }else{
                tagInfo=tagInfoList.get(0);
            }
            ArticleTag articleTag=new ArticleTag();
            articleTag.setTagId(tagInfo.getId());
            articleTag.setArticleId(articleId);
            articleTag.setCreateBy(new Date());
            articleTagMapper.insert(articleTag);
            tagInfo.setNumber(tagInfo.getNumber()+1);
            tagInfoMapper.updateByPrimaryKey(tagInfo);
        }
        return 1;
    }

    /**
     * 删除对应文章id 的tag属性
     * @param articleId
     * @return
     */
    private int deleteTags(long articleId){
        ArticleTagExample articleTagExample=new ArticleTagExample();
        ArticleTagExample.Criteria articleTagCriteria=articleTagExample.createCriteria();
        articleTagCriteria.andArticleIdEqualTo(articleId);
        List<ArticleTag> articleTagList=articleTagMapper.selectByExample(articleTagExample);
        for(ArticleTag item:articleTagList){
            TagInfo tagInfo=tagInfoMapper.selectByPrimaryKey(item.getTagId());
            tagInfo.setNumber(tagInfo.getNumber()-1);
            tagInfoMapper.updateByPrimaryKeySelective(tagInfo);
            articleTagMapper.deleteByPrimaryKey(item.getId());
        }
        return 1;
    }
    private List<String> queryTags(long articleId){
        ArticleTagExample articleTagExample=new ArticleTagExample();
        ArticleTagExample.Criteria articleTagCriteria=articleTagExample.createCriteria();
        articleTagCriteria.andArticleIdEqualTo(articleId);
        List<ArticleTag> articleTagList=articleTagMapper.selectByExample(articleTagExample);
        List<String> tagList=new ArrayList<>();
        for(ArticleTag item:articleTagList){
            TagInfo tagInfo=tagInfoMapper.selectByPrimaryKey(item.getTagId());
            tagList.add(tagInfo.getName());
        }
        return tagList;
    }
////////////////////////////////接口函数部分//////////////////////////////////
    /**
     * 添加新日志，需要维护多个表
     * @param articleViewDto
     * @return
     */
    @Override
    public int insertArticle(ArticleViewDto articleViewDto) {
        /**
         * 封装插入，注意对分类信息条目的影响
         */
        //封装info
        ArticleInfo info=new ArticleInfo();
        BeanUtil.copyProperties(articleViewDto,info);
        info.setPrivated(false);
        info.setCreateBy(new Date());
        info.setModifiedBy(new Date());
        infoMapper.insert(info);
        //封装content
        ArticleContent content=new ArticleContent();
        content.setArticleId(info.getId());
        content.setContent(articleViewDto.getContent());
        content.setModifiedBy(new Date());
        content.setCreateBy(new Date());
        contentMapper.insert(content);
        //封装tags
        insertTags(articleViewDto.getTagList(),info.getId());
        //修改category
        CategoryInfo categoryInfo=categoryInfoMapper.selectByPrimaryKey(articleViewDto.getCategoryId());
        categoryInfo.setNumber(categoryInfo.getNumber()+1);
        categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
        //修改图片所属id
        updatePictureArticleId(info.getId());
        return 1;
    }

    @Override
    public int deleteArticle(long id) {
        /**
         * 注意对三张表的修改 content,info,category
         */
        //删除info
        ArticleInfo info=infoMapper.selectByPrimaryKey(id);
        infoMapper.deleteByPrimaryKey(id);
        //删除content
        ArticleContentExample contentExample=new ArticleContentExample();
        contentExample.createCriteria().andArticleIdEqualTo(id);
        ArticleContent content=contentMapper.selectByExample(contentExample).get(0);
        contentMapper.deleteByPrimaryKey(content.getId());
        //修改分类文章数
        CategoryInfo categoryInfo=categoryInfoMapper.selectByPrimaryKey(info.getCategoryId());
        categoryInfo.setNumber(categoryInfo.getNumber()-1);
        categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
        //删除图片
        ArticlePictureExample pictureExample=new ArticlePictureExample();
        ArticlePictureExample.Criteria pictureCriteria=pictureExample.createCriteria();
        pictureCriteria.andArticleIdEqualTo(info.getId());
        List<ArticlePicture> pictureUrlList=pictureMapper.selectByExample(pictureExample);
        pictureMapper.deleteByExample(pictureExample);
        List<String >filenameList=new ArrayList<>();
        for(int i=0;i<pictureUrlList.size();i++){
            String pictureUrl=pictureUrlList.get(i).getPictureUrl();
            pictureUrl=pictureUrl.substring(pictureUrl.lastIndexOf("images")+7);
            String filename=picturePath+pictureUrl;
            filenameList.add(filename);
        }
        deleteFiles(filenameList);
        //删除标签
        deleteTags(info.getId());
        return 1;
    }

    @Override
    public int updateArticle(ArticleViewDto dto) {
        /**
         * update需要注意分类的改变
         */
        ArticleInfo info=infoMapper.selectByPrimaryKey(dto.getId());
        CategoryInfo oldCategory=categoryInfoMapper.selectByPrimaryKey(info.getCategoryId());
        //修改info
        BeanUtil.copyProperties(dto,info);
        info.setModifiedBy(new Date());
        infoMapper.updateByPrimaryKeySelective(info);
        //修改content
        ArticleContentExample example=new ArticleContentExample();
        ArticleContentExample.Criteria criteria=example.createCriteria();
        criteria.andArticleIdEqualTo(info.getId());
        ArticleContent content=contentMapper.selectByExample(example).get(0);
        content.setContent(dto.getContent());
        content.setModifiedBy(new Date());
        contentMapper.updateByPrimaryKeySelective(content);
        //改变分类下文章条数
        if(!oldCategory.getId().equals(info.getCategoryId())){
            CategoryInfo newCategory=categoryInfoMapper.selectByPrimaryKey(info.getCategoryId());
            oldCategory.setNumber(oldCategory.getNumber()-1);
            newCategory.setNumber(newCategory.getNumber()+1);
            categoryInfoMapper.updateByPrimaryKeySelective(newCategory);
            categoryInfoMapper.updateByPrimaryKeySelective(oldCategory);
        }
        //修改图片归属
        updatePictureArticleId(info.getId());
        //更新tags（先删除原来的tag，再插入新的tag）
        deleteTags(info.getId());
        insertTags(dto.getTagList(),info.getId());
        return 1;
    }

    @Override
    public PageInfo queryPreviews(int pageNum,int pageSize) {
        ArticleInfoExample example=new ArticleInfoExample();
        example.setOrderByClause("create_by desc");
        return getPageInfo(example,pageNum,pageSize);
    }

    @Override
    public PageInfo<ArticlePreviewDto> queryPublicPreviews(int pageNum, int pageSize) {
        ArticleInfoExample example=new ArticleInfoExample();
        example.setOrderByClause("is_top desc, create_by desc");
        example.createCriteria().andPrivatedEqualTo(false);
        PageInfo<ArticlePreviewDto> pageInfo=getPageInfo(example,pageNum,pageSize);
        return pageInfo;
    }

    @Override
    public PageInfo<ArticlePreviewDto> queryPreviewsByCategory(long categoryId, int pageNum, int pageSize) {
        ArticleInfoExample example=new ArticleInfoExample();
        example.setOrderByClause("create_by desc");
        example.createCriteria().andCategoryIdEqualTo(categoryId)
                .andPrivatedEqualTo(false);
        PageInfo<ArticlePreviewDto> pageInfo=getPageInfo(example,pageNum,pageSize);
        return pageInfo;
    }

    @Override
    /**
     * 查询tagId，获取articleId,获取articleInfo
     */
    public PageInfo<ArticlePreviewDto> queryPreviewsByTagName(String tagName, int pageNum, int pageSize) {
       TagInfoExample tagInfoExample=new TagInfoExample();
       tagInfoExample.createCriteria().andNameEqualTo(tagName);
       TagInfo tagInfo=tagInfoMapper.selectByExample(tagInfoExample).get(0);

       ArticleTagExample articleTagExample=new ArticleTagExample();
       articleTagExample.createCriteria().andTagIdEqualTo(tagInfo.getId());
       PageHelper.startPage(pageNum,pageSize);
       List<ArticleTag> articleTagList=articleTagMapper.selectByExample(articleTagExample);
       PageInfo<ArticleTag> daoPageInfo=new PageInfo<>(articleTagList);

       List<ArticleInfo> articleInfoList=new ArrayList<>();
       for(ArticleTag item:articleTagList){
           ArticleInfo articleInfo=infoMapper.selectByPrimaryKey(item.getArticleId());
           articleInfoList.add(articleInfo);
       }
       List<ArticlePreviewDto> dtoList=assembleDtoList(articleInfoList);

       PageInfo<ArticlePreviewDto> dtoPageInfo=new PageInfo<>(dtoList);
       PageInfoUtil.copyPageInfo(daoPageInfo,dtoPageInfo);
       return dtoPageInfo;
    }

    @Override
    public List<String> queryMonthList() {
        List<String> months=infoMapper.queryMonths();
        return months;
    }

    @Override
    public PageInfo<ArticlePreviewDto> queryPreviewsByBeginAndEndDate(Date beginDate, Date endDate,int pageNum, int pageSize) {
        ArticleInfoExample articleInfoExample=new ArticleInfoExample();
        articleInfoExample.setOrderByClause("create_by desc");
        articleInfoExample.createCriteria().andCreateByBetween(beginDate,endDate);
        PageInfo pageInfo=getPageInfo(articleInfoExample,pageNum,pageSize);
        return pageInfo;
    }

    @Override
    public PageInfo<ArticlePreviewDto> queryPrivatedPreviews(int pageNum, int pageSize) {
        ArticleInfoExample example=new ArticleInfoExample();
        example.setOrderByClause("create_by desc");
        ArticleInfoExample.Criteria criteria=example.createCriteria();
        criteria.andPrivatedEqualTo(true);
        PageInfo<ArticlePreviewDto> pageInfo=getPageInfo(example,pageNum,pageSize);
        return pageInfo;
    }

    @Override
    public ArticleViewDto getOneById(long id) {
        ArticleInfo info=infoMapper.selectByPrimaryKey(id);
        ArticleContentExample example=new ArticleContentExample();
        ArticleContentExample.Criteria criteria=example.createCriteria();
        criteria.andArticleIdEqualTo(id);
        ArticleContent content=contentMapper.selectByExample(example).get(0);
        CategoryInfo categoryInfo=categoryInfoMapper.selectByPrimaryKey(info.getCategoryId());
        info.setTraffic(info.getTraffic()+1);
        infoMapper.updateByPrimaryKeySelective(info);
        ArticleViewDto view=new ArticleViewDto();
        BeanUtil.copyProperties(info,view);
        view.setTagList(queryTags(id));
        view.setCategoryName(categoryInfo.getName());
        view.setContent(content.getContent());
        ArticleInfoExample examplePre=new ArticleInfoExample();
        ArticleInfoExample.Criteria criteriaPre=examplePre.createCriteria();
        examplePre.setOrderByClause("id desc limit 1");
        criteriaPre.andIdLessThan(id);
        List<ArticleInfo> preview=infoMapper.selectByExample(examplePre);
        if(preview.size()>0){
            view.setPreId(preview.get(0).getId());
            view.setPreTitle(preview.get(0).getTitle());
        }else{
            view.setPreId(id);
            view.setPreTitle("没有上一篇啦！");
        }
        ArticleInfoExample exampleNext=new ArticleInfoExample();
        ArticleInfoExample.Criteria criteriaNext=exampleNext.createCriteria();
        exampleNext.setOrderByClause("id  limit 1");
        criteriaNext.andIdGreaterThan(id);
        List<ArticleInfo> nextview=infoMapper.selectByExample(exampleNext);
        if(nextview.size()>0){
            view.setNextId(nextview.get(0).getId());
            view.setNextTitle(nextview.get(0).getTitle());
        }else{
            view.setNextId(id);
            view.setNextTitle("没有下一篇啦！");
        }
        //装配tags

        return view;
    }

    @Override
    public long countPublicArticle() {
        ArticleInfoExample example=new ArticleInfoExample();
        ArticleInfoExample.Criteria criteria=example.createCriteria();
        criteria.andPrivatedEqualTo(false);
        return infoMapper.countByExample(example);
    }
}

package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.CertFicate;
import com.dm.user.entity.PubArticle;
import com.dm.user.entity.PubArticleDetail;
import com.dm.user.service.PubArticleService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "普法专栏")
@RestController
@RequestMapping(value = "/article")
public class PubArticleController extends BaseController {

    @Autowired
    private PubArticleService pubArticleService;

    @ApiOperation(value = "普法专栏详情", response = ResultUtil.class)
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Result articleDetail(@PathVariable Integer id) throws Exception {
        PubArticleDetail articleDetail = pubArticleService.articleDetail(id);
        return ResultUtil.success(articleDetail);
    }

    @ApiOperation(value = "普法专栏列表", response = ResultUtil.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result articleList(Page<CertFicate> page) throws Exception {
        List<PubArticle> articleList = pubArticleService.articleList(page);
        return ResultUtil.success(articleList);
    }

}

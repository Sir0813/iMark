package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.ApplySupplement;
import com.dm.user.entity.ApplySupplementVo;
import com.dm.user.service.ApplySupplementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "补充材料")
@RestController
@RequestMapping(value = "/apply/supplement")
public class ApplySupplementController extends BaseController {

    @Autowired
    private ApplySupplementService applySupplementService;

    @ApiOperation(value = "公证人员补充材料提交", response = ResultUtil.class)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody ApplySupplement applySupplement) throws Exception {
        return applySupplementService.add(applySupplement);
    }



    @ApiOperation(value = "用户补充材料提交", response = ResultUtil.class)
    @RequestMapping(value = "/userApplySupplement", method = RequestMethod.POST)
    public Result userApplySupplement(@RequestBody ApplySupplementVo applySupplementVo ) throws Exception {
        Result  result = applySupplementService.userApplySupplement(applySupplementVo);

        return null;
}




}

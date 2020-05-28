package com.dm.user.controller;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.service.CertFicateService;
import com.dm.user.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author cui
 * @date 2019-09-26
 */
@Api(description = "上传")
@RestController
@RequestMapping
public class FileController {

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private CertFicateService certFicateService;


    @ApiOperation(value = "文件上传", response = ResultUtil.class)
    @RequestMapping(value = "/api/upload", method = RequestMethod.POST)
    public Result upload(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "file") MultipartFile[] multipartFile, String aid, String signature) throws Exception {
        Map<String, Object> map = fileUtil.uploadFile(request, response, multipartFile, aid, signature);
        if (map.containsKey("fileIds")) {
            return ResultUtil.success(map);
        }
        /*else if (map.containsKey("verifyFailed")) {
            return ResultUtil.info("error.code", "signature.error.msg");
        }*/
        return null;
    }

    @ApiOperation(value = "出证上传图片", response = ResultUtil.class)
    @RequestMapping(value = "/real/upload", method = RequestMethod.POST)
    public Result realUpload(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile[] multipartFile, String type) throws Exception {
        Map<String, Object> map = fileUtil.realUpload(request, response, multipartFile, type);
        if (map.containsKey("fileIds")) {
            return ResultUtil.success(map);
        }
        return ResultUtil.error();
    }

    @ApiOperation(value = "公正用户上传资料(批量上传)", response = ResultUtil.class)
    @RequestMapping(value = "/item/upload", method = RequestMethod.POST)
    public Result itemUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = fileUtil.itemUpload(request, response);
        /*if (map.containsKey("verifyFailed")) {
            return ResultUtil.info("error.code", "signature.error.msg");
        }*/
        return ResultUtil.success(map);
    }

    @ApiOperation(value = "公正用户上传资料(单张上传)", response = ResultUtil.class)
    @RequestMapping(value = "/item/uploadOne", method = RequestMethod.POST)
    public Result itemUploadOne(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "file") MultipartFile multipartFile) throws Exception {
        return fileUtil.itemUploadOne(request, response, multipartFile);
    }

    @ApiOperation(value = "公正附件(单张上传)", response = ResultUtil.class)
    @RequestMapping(value = "/item/attachmentUpload", method = RequestMethod.POST)
    public Result attachmentUpload(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "file") MultipartFile multipartFile) throws Exception {

        Result result = fileUtil.itemUploadOne(request, response, multipartFile);
        return result;
    }

    @ApiOperation(value = "上传公正意见书", response = ResultUtil.class)
    @RequestMapping(value = "/apply/upload", method = RequestMethod.POST)
    public Result applyUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = fileUtil.applyUpload(request, response);
        /*if (map.containsKey("verifyFailed")) {
            return ResultUtil.info("error.code", "signature.error.msg");
        } else */
        if (map.containsKey("fileid")) {
            return ResultUtil.success(map);
        }
        return null;
    }

}

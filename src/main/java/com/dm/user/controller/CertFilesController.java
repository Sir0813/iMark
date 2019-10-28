package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.user.service.CertFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cui
 * @date 2019-09-26
 */
@RestController
@RequestMapping(value = "/certFile")
public class CertFilesController extends BaseController {

    @Autowired
    private CertFilesService certFilesService;

}

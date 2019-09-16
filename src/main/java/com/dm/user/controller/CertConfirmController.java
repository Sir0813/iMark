package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.user.service.CertConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm")
public class CertConfirmController extends BaseController{

	@Autowired
	private CertConfirmService certConfirmService;

}

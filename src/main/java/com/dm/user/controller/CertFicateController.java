package com.dm.user.controller;

import com.dm.frame.jboot.base.controller.BaseController;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.CertFicate;
import com.dm.user.entity.TemCertFile;
import com.dm.user.entity.TemFile;
import com.dm.user.service.CertFicateService;
import com.dm.user.util.PDFConvertUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/certficate")
public class CertFicateController extends BaseController {

	@Autowired
	private CertFicateService certFicateService;

	@Autowired
	private PDFConvertUtil pdfConvertUtil;

	/**
	 * 存证
	 * @param certFicate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public Result save(@RequestBody CertFicate certFicate) throws Exception {
		CertFicate cert = certFicateService.save(certFicate);
		return ResultUtil.success(cert);
	}

	/**
	 * 模板存证存草稿
	 * @param certFicate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/temSave",method=RequestMethod.POST)
	public Result temSave(@RequestBody TemCertFile temCertFile) throws Exception {
		CertFicate cert = certFicateService.temSave(temCertFile);
		return ResultUtil.success(cert);
	}

	/**
	 * 我的存证
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Result list(Page<CertFicate> page, String state) throws Exception {
		PageInfo<CertFicate> certFicateList = certFicateService.list(page,state);
		return ResultUtil.success(certFicateList);
	}

	/**
	 * 存证详情
	 * @param certFicateId 存证ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/details",method=RequestMethod.GET)
	public Result details(Integer certFicateId) throws Exception {
		CertFicate cert = certFicateService.details(certFicateId);
		return ResultUtil.success(cert);
	}
	
	/**
	 * 撤回待他人确认存证
	 * @param certId 存证ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/revoke",method=RequestMethod.GET)
	public Result revoke(int certId) throws Exception {
		certFicateService.revoke(certId);
		return ResultUtil.success();
	}
	
	/**
	 * 退回待自己确认存证
	 * @param reason 原因
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/returnReason",method=RequestMethod.POST)
	public Result returnReason(@RequestBody Map<String,Object>map) throws Exception {
		certFicateService.returnReason(map);
		return ResultUtil.success();
	}
	
	/**
	 * 确认待自己确认
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/confirm",method=RequestMethod.POST)
	public Result confirm(@RequestBody Map<String,Object>map) throws Exception {
		certFicateService.confirm(map);
		return ResultUtil.success();
	}
	
	/**
	 * 草稿删除
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/draftDel",method=RequestMethod.POST)
	public Result draftDel(@RequestBody CertFicate certFicate) throws Exception {
		certFicateService.draftDel(certFicate);
		return ResultUtil.success();
	}

	/**
	 * 获取存证证书
	 * @param certId 存证ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getCertImg", method = RequestMethod.GET, headers = "Accept=image/jpeg")
	public ByteArrayResource getCertImg(Integer certId) throws Exception {
		ByteArrayResource bar = certFicateService.getCertImg(certId);
		return bar;
	}

	/**
	 * 修改模板后保存为pdf文件
	 * @param text
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/convertPDF", method = RequestMethod.POST)
	public void pdf(@RequestBody Map<String,Object> map) throws Exception {
		TemFile temFile = certFicateService.selectByCertId(map.get("certId").toString());
		pdfConvertUtil.acceptPage(temFile.getTemFileText(),temFile.getCertId());
	}

}

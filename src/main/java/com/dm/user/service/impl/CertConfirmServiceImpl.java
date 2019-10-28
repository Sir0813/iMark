package com.dm.user.service.impl;

import com.dm.user.entity.CertConfirm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dm.user.mapper.CertConfirmMapper;
import com.dm.user.service.CertConfirmService;
import java.util.List;
import java.util.Map;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CertConfirmServiceImpl implements CertConfirmService{

	@Autowired
	private CertConfirmMapper certConfirmMapper;

	@Override
	public void deleteByCertId(Integer certId) throws Exception {
		try {
			certConfirmMapper.deleteByCertId(certId);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void insertSelective(CertConfirm certConfirm) throws Exception {
		try {
			certConfirmMapper.insertSelective(certConfirm);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public List<CertConfirm> selectByCertId(int parseInt) throws Exception {
		try {
			return certConfirmMapper.selectByCertId(parseInt);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public List<CertConfirm> selectByUserId(int parseInt) throws Exception {
		try {
			return certConfirmMapper.selectByUserId(parseInt);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public List<CertConfirm> selectByuserIdAndState(String userId, String s) throws Exception {
		try {
			return certConfirmMapper.selectByuserIdAndState(userId, s);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void updateByCertId(Map<String, Object> map) throws Exception {
		try {
			certConfirmMapper.updateByCertId(map);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void updateConfirmState(Map<String, Object> map) throws Exception {
		try {
			certConfirmMapper.updateConfirmState(map);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public List<CertConfirm> selectByState(Map<String, Object> map) throws Exception {
		try {
			return certConfirmMapper.selectByState(map);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public List<CertConfirm> selectByPhone(String username) throws Exception {
		try {
			return certConfirmMapper.selectByPhone(username);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void updateByPrimaryKeySelective(CertConfirm l) {
		certConfirmMapper.updateByPrimaryKeySelective(l);
	}
}

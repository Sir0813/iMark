package com.dm.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dm.user.mapper.CertConfirmMapper;
import com.dm.user.service.CertConfirmService;

@Service
@Transactional(rollbackFor=Exception.class)
public class CertConfirmServiceImpl implements CertConfirmService{

	@Autowired
	private CertConfirmMapper certConfirmMapper;
	
}

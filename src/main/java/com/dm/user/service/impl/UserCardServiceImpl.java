package com.dm.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dm.user.mapper.UserCardMapper;
import com.dm.user.service.UserCardService;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserCardServiceImpl implements UserCardService{

	@Autowired
	private UserCardMapper userCardMapper;
	
}

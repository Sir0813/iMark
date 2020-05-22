package com.dm.user.service;

import com.dm.user.entity.ItemNotice;
import com.dm.user.entity.ItemNoticeApply;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ItemNoticeService {

    /**
     * 列表
     *
     * @return
     */
    List<ItemNotice> noticeList();

    /**
     * 风险签名
     *
     * @param request
     * @param response
     * @param multipartFile
     * @return
     * @throws Exception
     */
    List<ItemNoticeApply> signature(HttpServletRequest request, HttpServletResponse response, MultipartFile multipartFile) throws Exception;
}

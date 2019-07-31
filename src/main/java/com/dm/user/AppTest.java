package com.dm.user;

import com.dm.cid.sdk.service.ChainCertService;
import com.dm.cid.sdk.service.impl.ChainCertServiceImpl;

/**
 * @Description
 * @Package src.main.java.com.dm.baas
 * @Author bin
 * @DATE 2019/7/31
 */
public class AppTest {
    public static void main(String[] args) {
        ChainCertService ccs = new ChainCertServiceImpl();
        try {
            String result = ccs.query("id00001");
            System.out.println("======"+result);
            ccs.save("id00003","存证测试2","2019-07-31","");
            result = ccs.query("id00003");
            System.out.println("======"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

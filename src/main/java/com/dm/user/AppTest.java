package com.dm.user;


import com.dm.cid.sdk.service.CIDService;
import com.dm.cid.sdk.service.impl.CIDServiceImpl;

/**
 * @Description
 * @Package src.main.java.com.dm.baas
 * @Author bin
 * @DATE 2019/7/31
 */
public class AppTest {
    public static void main(String[] args) {
        CIDService ccs = new CIDServiceImpl();
        try {
            //String result = ccs.query("5EEsG2VNPYOYerDAMKSeGfZXTlJ4mkHrjURXPl5MG0c=");
            //System.out.println("======"+result);
            //Result res = ccs.save("id00003","存证测试2","2019-07-31","");
           // System.out.println(res.getData());
            String result = ccs.query("id00003");
            System.out.println("======"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.dm.user;


import com.dm.app.tid.sdk.model.entity.Authentication;
import com.dm.app.tid.sdk.model.entity.TID;
import com.dm.app.tid.sdk.service.TIDService;
import com.dm.app.tid.sdk.service.impl.TIDServiceImpl;
import com.dm.cid.sdk.service.CIDService;
import com.dm.cid.sdk.service.impl.CIDServiceImpl;
import com.dm.fchain.sdk.model.TransactionResult;
import com.dm.fchain.sdk.msg.Result;

import static java.lang.String.format;

/**
 * @Description
 * @Package src.main.java.com.dm.baas
 * @Author bin
 * @DATE 2019/7/31
 */
public class AppTest {

    public static void main(String[] args) {
        int rand = (int) (Math.random() * 1000000000);
        TIDService tidService = new TIDServiceImpl();
        TID tid = new TID();
        Authentication authentication = new Authentication();
        //保存测试用户test1
        tid.setNo("test" + rand);
        tid.setName("测试" + rand);
        tid.setPassword("123");
        tid.setAddress("北京111111");
        authentication.setTid("root");
        authentication.setPassword("123456");
        Result result = null;
        try {
            result = tidService.save(tid, authentication);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(format(" transaction result is  %s", result.getData()));


        try {
            CIDService chainCertService = new CIDServiceImpl();
            String cidString = "";
            try {
                cidString = chainCertService.query("++6+g5P3wG22mCJKpv0AOkWtKcTke6kjD0RCbFlTb1c=");
                System.out.println(cidString);
            } catch (Exception var13) {
                cidString = "";
            }
            Result result2 = chainCertService.save("test" + rand, "测试3", "2019-8-20", "");
            Object data = result2.getData();
            long blockNumber = 0L;
            if (data instanceof TransactionResult) {
                TransactionResult tr = (TransactionResult) result2.getData();
                String txid = tr.getTransactionID();
                blockNumber = tr.getBlockNumber();
                System.out.println(txid);
                System.out.println(blockNumber);
            } else {
                System.out.println("存证失败");
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        }

    }

}

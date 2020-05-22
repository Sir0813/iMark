package com.dm.user;


import com.dm.app.tid.sdk.model.form.BindForm;
import com.dm.app.tid.sdk.model.form.RegisterForm;
import com.dm.app.tid.sdk.model.form.SimpleForm;
import com.dm.app.tid.sdk.model.form.UpdateTidForm;
import com.dm.app.tid.sdk.service.TIDService;
import com.dm.app.tid.sdk.service.impl.TIDServiceImpl;
import com.dm.fchain.sdk.msg.Result;

/**
 * @Description
 * @Package src.main.java.com.dm.baas
 * @Author bin
 * @DATE 2019/7/31
 */
public class AppTest {
    public static void main(String[] args) throws Exception {
        /*CIDService chainCertService = new CIDServiceImpl();
        Result result = chainCertService.save("123r1sdfsdf234143", "1312312312312", DateUtil.timeToString2(new Date()), "123123");
        System.out.println(result.getCode());*/
//        checkTid("18811012222", "131182199408131233");
        // aaaa11112222  asdf23   aaaawer234
//        addTid("iMark1111");
//        register("18808080808", "9651e04928c2568e8a8b70d643bd9790");
//        addTid("user0002");
//        addBind("user002", "qwe123", "user001");
//        checkTid("18808080808", "131182199408131233");
//        TIDService tidService = new TIDServiceImpl();
//        String iMark18808080808123123 = tidService.history("iMark18811012959");
//        System.out.println(iMark18808080808123123);
//        Result result = tidService.queryTID("131182199408131233");
//        System.out.println(result.getCode());
        /*JSONObject jsonObject = JSONObject.parseObject(iMark18811014321);
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        Object parent = jsonObject1.get("parent");
        System.out.println(parent);
        System.out.println(iMark18811014321);*/
//        updatePassword("18910500312", "11b0ac1a8ca68fad3f5e73858dc59070", "e6bb8eae2959e050997c58d15ea00577");
//        String history = history("18910500312");
//        System.out.println(history);


        /*BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        InputStream input = new FileInputStream(new File("D:\\360极速浏览器下载\\test.pdf"));
        PdfReader reader = new PdfReader(input);
        OutputStream output = new FileOutputStream(new File("D:\\360极速浏览器下载\\test1.pdf"));
        PdfStamper stamper = new PdfStamper(reader, output);
        PdfContentByte page = stamper.getOverContent(1);
        //将文字贴入pdf
        *//*page.beginText();
        page.setFontAndSize(baseFont, 12);
        BaseColor coler = new BaseColor(0, 0, 0);
        page.setColorFill(coler);
        page.setTextMatrix(100, 500); //设置文字在页面中的坐标
        page.showText("添加文字信息");
        page.endText();*//*
        //将图片贴入pdf
        Image image = Image.getInstance("D:\\360极速浏览器下载\\8.png");
        image.setAbsolutePosition(300, 300); //设置图片在页面中的坐标
        page.addImage(image);
        stamper.close();
        reader.close();
        input.close();*/

        /*String pdfname = "D:\\360极速浏览器下载\\test.pdf";
        //查找签名位置
        float[] position = PdfKeywordFinder.getAddImagePositionXY(pdfname, "委托人（签字）：");
        PdfReader pdfReader = new PdfReader(new FileInputStream(new File("D:\\360极速浏览器下载\\test.pdf")));
        System.out.println("x:" + position[1] + " y:" + position[2]);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("E:\\opt\\czt-upload\\headfile\\123.pdf"));
        Image image = Image.getInstance("E:\\opt\\czt-upload\\headfile\\328e66d3-1d74-4569-8d68-fd03f6cb03c2.png");
        image.setAbsolutePosition(position[1], position[2]);
        System.out.println("pages:" + pdfReader.getNumberOfPages());
        PdfContentByte content = pdfStamper.getOverContent(1);
        content.addImage(image);
        pdfStamper.close();*/


    }


    private static String appCode = "iMark";
    private static String passWord = "123456";

    /**
     * 链上注册用户身份
     *
     * @param userName 手机号
     * @param password 密码
     */
    public static void register(String userName, String password) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        RegisterForm registerForm = new RegisterForm();
        registerForm.setAppCode(appCode);
        registerForm.setAppPassword(passWord);
        registerForm.setMobile(appCode + userName);
        registerForm.setPassword(password);
        Result register = tidService.register(registerForm);
        if (!"200".equals(register.getCode())) {
            System.out.println("用户身份上链注册失败!");
        } else {
            System.out.println("注册成功!");
        }
    }

    /**
     * 实名身份注册
     *
     * @param userCard
     * @throws Exception
     */
    public static void addTid(String userCard) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        SimpleForm simpleForm = new SimpleForm();
        simpleForm.setCode(userCard);
        simpleForm.setPassword(passWord);
        Result result = tidService.addTid(simpleForm);
        System.out.println(result.getCode());
    }

    /**
     * 修改链上身份密码
     *
     * @param userName    手机号
     * @param oldPassWord 旧密码
     * @param newPassword 新密码
     */
    public static void updatePassword(String userName, String oldPassWord, String newPassword) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        UpdateTidForm tidForm = new UpdateTidForm();
        tidForm.setCode(appCode + userName);
        tidForm.setPassword(oldPassWord);
        tidForm.setNewPassword(newPassword);
        Result result = tidService.updateTid(tidForm);
        if ("200".equals(result.getCode())) {
            System.out.println("身份修改成功!");
        } else {
            System.out.println("身份信息修改失败!");
        }
    }

    /**
     * 查询修改历史记录
     *
     * @param userName
     */
    public static String history(String userName) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        String history = tidService.history(appCode + userName);
        return history;
    }

    /**
     * 检测实名信息
     *
     * @param userName
     * @param userCard
     * @throws Exception
     */
    public static void checkTid(String userName, String userCard) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        Result result = tidService.checkTID(appCode + userName, userCard);
        if ("201".equals(result.getCode())) {
            // 账号未实名认证 可以实名
        } else if ("203".equals(result.getCode())) {
            // 实名身份和证件号码不匹配  (多次实名时 )
        } else if ("200".equals(result.getCode())) {
            // 实名身份与证件号码匹配 (可以实名)
        } else if ("404".equals(result.getCode())) {

        }
    }

    public static void addBind(String userName, String userPassword, String userCard) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        BindForm bindForm = new BindForm();
        bindForm.setUserNo(appCode + userName);
        bindForm.setPassword(userPassword);
        bindForm.setToUserNo(userCard);
        bindForm.setToUserPassword(passWord);
        Result result = tidService.addBind(bindForm);
        System.out.println(result.getCode());
    }

}

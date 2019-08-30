package com.dm.user.util;

import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.CertFicate;
import org.springframework.core.io.ByteArrayResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CertImgUtil {

    /**
     * @param certFicate 存证对象
     * @param path 原图片地址
     * @param outPath 输出图片地址
     * @param qrPath  二维码图片地址
     * @return
     * @throws IOException
     */
    public static ByteArrayResource createStringMark(CertFicate certFicate,String path, String qrPath) throws IOException, FontFormatException {
        File file = new File(path);
        Image img = ImageIO.read(file);
        int width = img.getWidth(null);//水印宽度
        int height = img.getHeight(null);//水印高
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        Graphics graphics = bi.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//设置抗锯齿
        g.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        g.setPaint(new Color(0,0,0,64));//阴影颜色
        g.setPaint(Color.BLACK);
        g.setFont(new Font("黑体", Font.PLAIN, 35));
        g.drawString("文件存证证书", (int) 185, (int) 100);
        g.setFont(new Font("黑体", Font.PLAIN, 20));
        g.drawString("证书ID："+certFicate.getCertId().toString(), (int) 267, (int) 200);
        g.setFont(new Font("黑体", Font.PLAIN, 15));
        //文件名称
        g.drawString("文件名称：" + certFicate.getCertName(), (int) 100, (int) 350);
        //用户
        g.drawString("用户：" + certFicate.getCertOwner(), (int) 100, (int) 372);
        //文件类型
        String type = "";
        switch (certFicate.getCertType()){
            case 1 : type = "文件存证";break;
            case 2 : type = "拍照存证";break;
            case 3 : type = "相册存证";break;
            case 4 : type = "录像存证";break;
            case 5 : type = "录音存证";break;
            case 6 : type = "录屏存证";break;
            case 7 : type = "模板存证";break;
            default: type = "图片";break;
        }
        g.drawString("存证类型：" + type, (int) 100, (int) 395);
        //存证时间
        g.drawString("存证时间：" + DateUtil.timeToString2(certFicate.getCertDate()), (int) 100, (int) 418);
        //存证平台
        String platform = "存证通";
        g.drawString("存证平台：" + platform, (int) 100, (int) 443);
        g.drawString("区块链存证ID：", (int) 100, (int) 500);
        g.setFont(new Font("黑体", Font.PLAIN, 12));
        g.drawString(certFicate.getCertChainno(), (int) 100, (int) 525);
        g.setFont(new Font("黑体", Font.PLAIN, 15));

        g.drawString("存证声明：", (int) 100, (int) 630);
        g.drawString("1.本证书最终解释权归北京迪曼森科技有限公司所有。", (int) 100, (int) 655);
        /*二维码*/
        ImageIcon QrcodeimgIcon = new ImageIcon(qrPath);
        Image img1 = QrcodeimgIcon.getImage();
        graphics.drawImage(img1, 387, 670, null);
        graphics.setColor(Color.WHITE);
        graphics.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        ByteArrayResource bar = new ByteArrayResource(bytes);
        File qrFile = new File(qrPath);
        qrFile.delete();
        return bar;
    }

    //String imageStr = encoder.encodeBuffer(bytes).trim();
    //System.out.println(imageStr);
    //OutputStream os = new FileOutputStream(outPath);
    //创键编码器，用于编码内存中的图象数据。
    //JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
    //en.encode(bi);
    //os.close();
       /* try {
            FileOutputStream out = new FileOutputStream(outPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
            param.setQuality(100, true);
            encoder.encode(bi, param);
            InputStream is = new FileInputStream(outPath);
            //通过JPEG图象流创建JPEG数据流解码器
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
            //解码当前JPEG数据流，返回BufferedImage对象
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
            //得到画笔对象
            Graphics g2 = buffImg.getGraphics();
            //小图片的路径
            ImageIcon QrcodeimgIcon = new ImageIcon(qrPath);
            //得到Image对象。
            Image img1 = QrcodeimgIcon.getImage();
            //将小图片绘到大图片上,5,300 .表示你的小图片在大图片上的位置。
            g2.drawImage(img1, 387, 670, null);
            //设置颜色。
            g2.setColor(Color.WHITE);
            g2.dispose();
            File qrFile = new File(qrPath);
            qrFile.delete();
            OutputStream os = new FileOutputStream(outPath);
            //创键编码器，用于编码内存中的图象数据。
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            en.encode(buffImg);
            is.close();
            os.close();
            out.close();
        } catch (Exception e) {
            return false;
        }*/

}

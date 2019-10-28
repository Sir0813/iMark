package com.dm.user.util;

import com.dm.frame.jboot.util.DateUtil;
import com.dm.user.entity.CertFicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author cui
 * @date 2019-09-26
 */
public class CertImgUtil {

    /**
     * @param certFicate 存证对象
     * @param path       原图片地址
     * @param outPath    输出图片地址
     * @param qrPath     二维码图片地址
     * @return
     * @throws IOException
     */
    public static ByteArrayResource createStringMark(CertFicate certFicate, String path, String qrPath) throws IOException, FontFormatException {
        File file = new File(path);
        Image img = ImageIO.read(file);
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        Graphics graphics = bi.getGraphics();
        // 设置抗锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        // 阴影颜色
        g.setPaint(new Color(0, 0, 0, 64));
        g.setPaint(Color.BLACK);
        g.setFont(new Font("黑体", Font.PLAIN, 35));
        g.drawString("文件存证证书", (int) 185, (int) 100);
        g.setFont(new Font("黑体", Font.PLAIN, 20));
        g.drawString("证书编号：" + certFicate.getCertCode(), (int) 120, (int) 170);
        g.setFont(new Font("黑体", Font.PLAIN, 15));
        //文件名称
        g.drawString("文件名称：" + certFicate.getCertName(), (int) 120, (int) 210);
        //用户
        g.drawString("用户：" + certFicate.getCertOwner(), (int) 120, (int) 250);
        //存证时间
        g.drawString("存证时间：" + DateUtil.timeToString2(certFicate.getCertDate()), (int) 120, (int) 290);
        //存证平台
        g.drawString("存证平台：" + "DMS-iMark", (int) 120, (int) 330);
        String substring = certFicate.getCertChainno().substring(0, 34);
        String s = certFicate.getCertChainno().substring(34, certFicate.getCertChainno().length());
        g.drawString("区块链存证编号：" + substring, (int) 120, (int) 370);
        g.drawString(s, (int) 120, (int) 390);
        String certAddress = certFicate.getCertAddress();
        String address = "";
        String address2 = "";
        if (StringUtils.isNotBlank(certAddress)) {
            int maxLength = 22;
            if (certAddress.length() > maxLength) {
                address = certAddress.substring(0, 22);
                address2 = certAddress.substring(22, certAddress.length());
            }
        }
        g.drawString("存证位置：" + address, (int) 120, (int) 430);
        g.drawString(address2, (int) 120, (int) 450);
        g.setFont(new Font("黑体", Font.PLAIN, 15));
        g.drawString("存证声明：", (int) 120, (int) 615);
        g.drawString("1.本证书最终解释权归北京迪曼森科技有限公司所有。", (int) 120, (int) 640);
        /*二维码*/
        ImageIcon QrcodeimgIcon = new ImageIcon(qrPath);
        Image img1 = QrcodeimgIcon.getImage();
        graphics.drawImage(img1, 235, 670, null);
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

package com.dm.user;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Description
 * @Package src.main.java.com.dm.baas
 * @Author bin
 * @DATE 2019/7/31
 */
public class AppTest {
    public static void main(String[] args) {
        try {
            //CIDService ccs = new CIDServiceImpl();
            //String result = ccs.query("Rg7LgvLAWQCXWfFsYJ2Y69zD/ArDr3hy7aqpSWtnkf8=");
            //System.out.println("======"+result);
            //Result res = ccs.save("id000031234545645","存证测试2","2019-07-31","");
            //System.out.println(res.getMsg());
            AppTest.fetchFrame("D:\\upload\\5052ba8d-1f84-4c91-932b-69e2bc109a56.mp4", "D:\\upload\\11111111111111111111111.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取指定视频的帧并保存为图片至指定目录
     * @param videofile  源视频文件路径
     * @param framefile  截取帧的图片存放路径
     * @throws Exception
     */
    public static void fetchFrame(String videofile, String framefile)
            throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = new File(framefile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
        ff.start();
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            f = ff.grabFrame();
            if ((i > 5) && (f.image != null)) {
                break;
            }
            i++;
        }
        int owidth = f.imageWidth;
        int oheight = f.imageHeight;
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage fecthedImage = converter.getBufferedImage(f);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width,height, Image.SCALE_SMOOTH),
                0, 0, null);
        ImageIO.write(bi, "jpg", targetFile);
        //ff.flush();
        ff.stop();
        System.out.println(System.currentTimeMillis() - start);

    }
}

package com.example.util.commonutil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class ImgUtils {

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param path 图片路径
     * @return base64 数据
     */
    public static String getImageStr(String path) {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        String encode = encoder.encode(data);

        return encode;
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr imgStr base64数据
     * @param path   path
     * @return 是否成功
     */
    public static boolean generateImage(String imgStr, String path) {
        //图像数据为空
        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                //调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //生成jpeg图片 新生成的图片
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package com.txpc.captcha.captcha.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.txpc.captcha.captcha.component.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * @ProjectName: captcha
 * @Package: com.txpc.captcha.captcha.utils
 * @ClassName: CaptchaUtil
 * @Author: zbx
 * @Description: create captcha class
 * @Date: 2019/5/21 14:14
 * @Version: 1.0
 */
@Component
public class CaptchaUtil {

    @Autowired
    private RedisUtil redisUtil;

    static final Character[] arr = {'1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    // 验证码图片的宽度。
    static final int WIDTH = 70;
    // 验证码图片的高度。
    static final int HEIGHT = 25;
    // 随机数
    static final Random random = new Random();
    // 图片类型
    static final String IMAGE_TYPE = "jpg";
    // 验证码位数
    static final int DEFAULT_NUM = 4;

    //uid末尾拼接的自增数
    @Value("#{service.ukey}")
    static String uKey;
    /**
     * 获取验证码图片
     * */
    public HashMap<String,String> getImageBase64Str(String channel, String uid) throws IOException {
        if(uid==null||uid.length()==0){
            uid = getUniqueId();
        }
        // redis namespace值
        StringBuffer key = new StringBuffer(channel).append(".").append(uid);
        // 初始化图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        Graphics graphics =image.getGraphics();
        // 设置颜色
        Color color=getRandomColor(200, 250);
        graphics.setColor(color);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Times New Roman", Font.HANGING_BASELINE, 24);
        // 设置字体。
        graphics.setFont(font);
        // 画边框。
        graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        // 把字符写入图片
        writeStrIntoImage(graphics,key.toString());
        HashMap data = new HashMap();
        data.put("image",BufferedImageToBase64Str(image));
        data.put("uid",uid);
        return data;
    }

    /**
     * 检验验证码是否在redis内存在。如果不存在则不通过该验证
     * */
    public String checkVerificationCode(String channel,String uid,String code){
        // redis namespace值
        StringBuffer key = new StringBuffer(channel).append(".").append(uid);
        code = code.toLowerCase();
        Object val = redisUtil.get(key.toString());
        if(val==null){
            return "NOT_EXIST";
        }
        if(val.equals(code)){
            redisUtil.del(key.toString());
            return "SUCCESS";
        }
        return "FAIL";
    }

    /**
     * 给出颜色范围并随即背景色
     *
     * */
    private Color getRandomColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }

        if (bc > 255) {
            bc = 255;
        }

        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 把计算出的要放入图片的字串按位写入图片
     * 并且把图片的答案存入redis
     * */
    private void writeStrIntoImage(Graphics g,String key){
        // 图片显示字符串
        String imageStr = getRandomStr();
        // 设置redis值
        redisUtil.set(key,imageStr,RedisUtil.DEFAULT_TIME);
        // 写入验证码。
        for (int i = 0; i < imageStr.length(); i++) {
            String strRand = imageStr.charAt(i)+"";
            // 生成随机颜色
            g.setColor(new Color(20 + random.nextInt(110), 20 + random
                    .nextInt(110), 20 + random.nextInt(110)));

            //随机位置出现
//            g.drawString(strRand, (int) (15*i+Math.random()*10) , (int) (Math.random()*10+20));
            int randomX = random.nextInt(10);
//            int randomY = random.nextInt(4);
            //随机位置出现
            g.drawString(strRand, 15*i+(randomX<2?3: randomX), 4*HEIGHT/5);
        }
    }

    /**
     * 设置并返回图片显示字符串（例如公式和显示字符等）
     * 并且把图片的答案存入redis
     *
     * */
    private String getRandomStr(){
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<DEFAULT_NUM;i++){
            int j = random.nextInt(arr.length);
            sb.append(arr[j]);
        }
        return sb.toString();
    }

    /**
     * 把画好的图片转码成base64字符串
     *
     * */
    private String BufferedImageToBase64Str(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image,IMAGE_TYPE,out);
        return Base64.encode(out.toByteArray());
    }

    /**
     * 获取全局唯一的key
     *
     * */
    private String getUniqueId() throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(System.currentTimeMillis());
        sb.append(redisUtil.incr(uKey));
        return sb.toString();
    }
}

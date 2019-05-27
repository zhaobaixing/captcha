package com.txpc.captcha.captcha.controller;

import com.txpc.captcha.captcha.vo.ResultVO;
import com.txpc.captcha.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * @ProjectName: captcha
 * @Package: com.txpc.captcha.captcha.controller
 * @ClassName: KaptchaController
 * @Author: zbx
 * @Description:
 * @Date: 2019/5/21 17:43
 * @Version: 1.0
 */
@RestController
public class CaptchaController {

    @Autowired
    CaptchaUtil captchaUtil;

    @RequestMapping("/getCaptchaBase64")
    public ResultVO getCaptchaBase64(@RequestBody HashMap<String,String> map) throws IOException {
        HashMap data = captchaUtil.getImageBase64Str(map.get("channel"),map.get("uid"));
        ResultVO vo = new ResultVO();
        vo.setCode(0);
        vo.setData(data);
        return vo;
    }

    @RequestMapping("/checkVerificationCode")
    public ResultVO checkVerificationCode(@RequestBody HashMap<String,String> map){
        String b = captchaUtil.checkVerificationCode(map.get("channel"),map.get("uid"),map.get("code"));
        ResultVO vo = new ResultVO();
        vo.setCode(0);
        vo.setData(b);
        return vo;
    }
}

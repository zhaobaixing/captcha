package com.txpc.captcha.captcha.service;

import com.txpc.captcha.captcha.component.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: captcha
 * @Package: com.txpc.captcha.captcha.service
 * @ClassName: RestTemplateService
 * @Author: zbx
 * @Description:
 * @Date: 2019/5/21 18:02
 * @Version: 1.0
 */
@Service
public class RequestService {
    @Autowired
    RequestUtil requestUtil;

//    public ResponseEntity<ResultVO> requestService(){
//        return requestUtil.requestPostForEntity();
//    }
}

package com.txpc.captcha.captcha.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @ProjectName: captcha
 * @Package: com.txpc.captcha.captcha.component
 * @ClassName: RequestUtil
 * @Author: zbx
 * @Description:
 * @Date: 2019/5/21 17:57
 * @Version: 1.0
 */
@Component
public class RequestUtil {
    @Autowired
    RestTemplate restTemplate;

    @Value("#{service.url}")
    static String url;

    /**
     * 返回接口处理数据类型
     * param: para为接口参数
     * param: c为转型类型
     * */
    @SuppressWarnings("unchecked")
    public <T> T requestPostForEntity(Object parameter,Class c) {
        return (T)restTemplate.postForEntity(url,parameter,c).getBody();
    }

    @SuppressWarnings("unchecked")
    public <T> T requestGetForEntity(Object parameter,Class c){
        return (T)restTemplate.getForEntity(url,c).getBody();
    }

}

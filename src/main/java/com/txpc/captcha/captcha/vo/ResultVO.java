package com.txpc.captcha.captcha.vo;

import lombok.Data;

/**
 * @ProjectName: captcha
 * @Package: com.txpc.captcha.captcha.vo
 * @ClassName: ResultVO
 * @Author: zbx
 * @Description:
 * @Date: 2019/5/21 17:46
 * @Version: 1.0
 */
@Data
public class ResultVO {
    public int code;
    public Object data;
    public String msg;
}

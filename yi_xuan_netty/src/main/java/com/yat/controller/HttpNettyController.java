package com.yat.controller;

import com.yat.mvc.annotation.ActionMapping;
import com.yat.mvc.annotation.ResponseType;
import com.yat.utils.ResultResponse;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/6 14:04
 */
@Component
@ActionMapping(actionKey = "controller")
@ResponseType
public class HttpNettyController implements BaseActionController {

    @ActionMapping(actionKey = "method")
    public ResultResponse method(String a, String b) {
        String format = String.format("a:%s,b:%s", a, b);
        return ResultResponse.success(format);

    }

    public static void main(String[] args) {

    }
}

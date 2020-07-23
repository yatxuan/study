package com.yat.reptile;

import com.yat.common.util.StringUtils;
import com.yat.reptile.module.VerifyImageDTO;
import com.yat.reptile.module.VerifyImageVO;
import com.yat.reptile.rule.RuleActuator;
import com.yat.reptile.util.VerifyImageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Yat-Xuan
 * @since 2019/7/9
 */
@Component
public class ValidateFormService {

    @Autowired
    private RuleActuator actuator;

    @Autowired
    private VerifyImageUtil verifyImageUtil;

    /**
     * 验证验证码
     *
     * @param params 、
     * @return 、
     */
    public boolean validate(Map<String, String> params, HttpServletRequest request) {
        String verifyId = params.get("verifyId");
        String result = params.get("result");
        String realRequestUri = params.get("realRequestUri");
        String actualResult = verifyImageUtil.getVerifyCodeFromRedis(verifyId);

        if (StringUtils.isNotBlank(actualResult) && request != null && actualResult.equals(result.toLowerCase())) {
            actuator.reset(request, realRequestUri);
            return true;
        }
        return false;
    }

    /**
     * 刷新验证码
     *
     * @param verifyId 验证码的UUID
     * @return 、
     */
    public VerifyImageVO refresh(String verifyId) {
        verifyImageUtil.deleteVerifyCodeFromRedis(verifyId);
        VerifyImageDTO verifyImageDTO = verifyImageUtil.generateVerifyImg(verifyId);
        verifyImageUtil.saveVerifyCodeToRedis(verifyImageDTO);
        VerifyImageVO verifyImageVO = new VerifyImageVO();
        // copyProperties:方法的作用是，将一个Bean对象中的数据封装到另一个属性结构相似的Bean对象中
        BeanUtils.copyProperties(verifyImageDTO, verifyImageVO);
        return verifyImageVO;
    }
}

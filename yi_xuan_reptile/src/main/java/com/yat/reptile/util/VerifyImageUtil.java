package com.yat.reptile.util;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import com.yat.common.util.StringUtils;
import com.yat.reptile.module.VerifyImageDTO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Yat-Xuan
 * @since 2019/7/16 11:05
 */
@Component
public class VerifyImageUtil {

    private static final String VERIFY_CODE_KEY = "yat:verify:code_";
    private final static Map<Integer, Captcha> CAPTCHA_MAP = new HashMap<>(5);
    private final static int WIDTH = 130;
    private final static int HEIGHT = 47;

    @Autowired
    private RedissonClient redissonClient;

    static {
        // png类型
        SpecCaptcha specCaptcha = new SpecCaptcha(WIDTH, HEIGHT);
        // gif类型
        GifCaptcha gifCaptcha = new GifCaptcha(WIDTH, HEIGHT);

        // 中文类型
        ChineseCaptcha chineseCaptcha = new ChineseCaptcha(WIDTH, HEIGHT);

        // 中文gif类型
        ChineseGifCaptcha chineseGifCaptcha = new ChineseGifCaptcha(WIDTH, HEIGHT);

        // 算术类型
        ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(WIDTH, HEIGHT);

        CAPTCHA_MAP.put(0, specCaptcha);
        CAPTCHA_MAP.put(1, gifCaptcha);
        CAPTCHA_MAP.put(2, chineseCaptcha);
        CAPTCHA_MAP.put(3, chineseGifCaptcha);
        CAPTCHA_MAP.put(4, arithmeticCaptcha);
    }


    /**
     * 生成图片验证码
     *
     * @return 、
     */
    public VerifyImageDTO generateVerifyImg(String verifyId) {
        int cType = new Random().nextInt(4);
        verifyId = StringUtils.isBlank(verifyId) ? IdUtil.simpleUUID() : verifyId;
        Captcha captcha = CAPTCHA_MAP.get(cType);

        VerifyImageDTO verifyImageDTO = new VerifyImageDTO();

        switch (cType) {
            case 0:
                // png类型
                verifyImageDTO.setVerifyType("png");
                break;
            case 1:
                // gif类型
                verifyImageDTO.setVerifyType("gif");
                break;
            case 2:
                // 中文类型
                verifyImageDTO.setVerifyType("chinese");
                break;
            case 3:
                // 中文gif类型
                verifyImageDTO.setVerifyType("chineseGif");
                break;
            case 4:
                // 算术类型
                verifyImageDTO.setVerifyType("number");
                // 几位数运算
                captcha.setLen(3);
                break;
            default:
                // 算术类型
                captcha = CAPTCHA_MAP.get(4);
                verifyImageDTO.setVerifyType("number");
                // 几位数运算
                captcha.setLen(3);
                break;
        }


        String rest = "0.0";
        String result = captcha.text();
        if (StringUtils.equals(rest, result)) {
            result = "0";
        }

        verifyImageDTO.setVerifyId(verifyId);
        verifyImageDTO.setResult(result);
        verifyImageDTO.setVerifyImgStr(captcha.toBase64());

        return verifyImageDTO;
    }

    /**
     * 存储新的图片验证码数据
     *
     * @param verifyImage 图片验证码数据
     */
    public void saveVerifyCodeToRedis(VerifyImageDTO verifyImage) {
        RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY + verifyImage.getVerifyId());
        rBucket.set(verifyImage.getResult(), 60, TimeUnit.SECONDS);
    }

    /**
     * 根据图片的UUID，删除对应的验证码图片
     *
     * @param verifyId UUID
     */
    public void deleteVerifyCodeFromRedis(String verifyId) {
        RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY + verifyId);
        rBucket.delete();
    }

    /**
     * 获取验证码的值
     *
     * @param verifyId 验证码的UUID
     * @return 验证码的数据值
     */
    public String getVerifyCodeFromRedis(String verifyId) {
        RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY + verifyId);
        rBucket.delete();
        return rBucket.get();
    }
}

package com.yat.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * <p>Description: 水印生成辅助类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:20
 */
public class FontImage {
    @Data
    public static class Watermark {
        private Boolean enable;
        private String text;
        private String dateFormat;
        private String color;
    }

    public static BufferedImage createWatermarkImage(Watermark watermark) {
        if (watermark == null) {
            watermark = new FontImage.Watermark();
            watermark.setEnable(true);
            watermark.setText("内部资料");
            watermark.setColor("#C5CBCF");
            watermark.setDateFormat("yyyy-MM-dd HH:mm");
        } else {
            if (StringUtils.isEmpty(watermark.getDateFormat())) {
                watermark.setDateFormat("yyyy-MM-dd HH:mm");
            } else if (watermark.getDateFormat().length() == 16) {
                watermark.setDateFormat("yyyy-MM-dd HH:mm");
            } else if (watermark.getDateFormat().length() == 10) {
                watermark.setDateFormat("yyyy-MM-dd");
            }
            if (StringUtils.isEmpty(watermark.getText())) {
                watermark.setText("内部资料");
            }
            if (StringUtils.isEmpty(watermark.getColor())) {
                watermark.setColor("#C5CBCF");
            }
        }
        String[] textArray = watermark.getText().split("\n");
        Font font = new Font("microsoft-yahei", Font.PLAIN, 20);
        Integer width = 300;
        Integer height = 100;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 背景透明 开始
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        // 背景透明 结束
        g = image.createGraphics();
        // 设定画笔颜色
        g.setColor(new Color(Integer.parseInt(watermark.getColor().substring(1), 16)));
        // 设置画笔字体
        g.setFont(font);
        // 设定倾斜度
        g.shear(0.1, -0.26);

        //设置字体平滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = 50;
        for (String s : textArray) {
            // 画出字符串
            g.drawString(s, 0, y);
            y = y + font.getSize();
        }
        g.dispose();// 释放画笔
        return image;
    }
}

package com.yat.model.controller;

import com.yat.config.strategy.HandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @version 1.0
 * @date 2020/11/25 - 15:08
 */
@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {

    private final HandlerContext handlerContext;

    @GetMapping
    public void pay(String payType) {
        handlerContext.getOrderStrategy(payType).pay();
    }
}

package com.yat.netty.handle;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/8/6 14:28
 * @Sharable 注解用来说明ChannelHandler是否可以在多个channel直接共享使用
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class SocketServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        //	System.out.println(msg);
        ctx.writeAndFlush("text");
        ctx.channel().close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        String resourceName = "testSentinel";
        Entry entry = null;
        String retVal;
        try {
            entry = SphU.entry(resourceName, EntryType.IN);
            retVal = "passed";
        } catch (BlockException e) {
            retVal = "blocked";
            System.out.println(retVal);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }

        log.info("连接");
    }


    @PostConstruct
    public void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource("testSentinel");
        // set limit qps to 20
        rule1.setCount(20);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }

}

package com.klw.creational.factory;

import com.klw.creational.factory.entity.PayChannelConfig;
import com.klw.creational.factory.entity.PaymentRequest;

import java.math.BigDecimal;
import java.util.HashMap;

public class PayHandlerFactoryDemo {
    public static void main(String[] args) {
        String env = "DEV";
        // startup
        PayHandlerFactory factory = PayHandlerFactory.getInstance();
        if ("TEST".equals(env)) {
            factory.register("WECHAT", MockPayHandler::new, true);
        } else {
            factory.register("WECHAT", WeChatPayHandler::new, true);
//            factory.register("ALIPAY", AlipayPayHandler::new, true);
        }
        // load configs from properties and put to factory
        factory.putConfig("WECHAT", new PayChannelConfig("id", new HashMap<>()));

        factory.getHandler("WECHAT").pay(new PaymentRequest("orderId", BigDecimal.ONE, new HashMap<>()));
    }
}

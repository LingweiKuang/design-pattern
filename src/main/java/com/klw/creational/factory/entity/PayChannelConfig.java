package com.klw.creational.factory.entity;

import java.util.Map;

public class PayChannelConfig {
    public String id;
    public Map<String, String> props;

    public PayChannelConfig(String id, Map<String, String> props) {
        this.id = id;
        this.props = props;
    }
}

package com.klw.structural.bridge;

public class BridgePatternDemo {
    public static void main(String[] args) {
        // 订单内容和各种通知方式桥接组合
        Notification emailOrderNotification = new OrderNotification(new EmailSender());
        Notification pushOrderNotification = new OrderNotification(new PushSender());
        Notification smsOrderNotification = new OrderNotification(new SmsSender());

        emailOrderNotification.notifyUser();
        pushOrderNotification.notifyUser();
        smsOrderNotification.notifyUser();

        // Alert 告警内容和各种通知方式桥接组合
        Notification emailAlertNotification = new AlertNotification(new EmailSender());
        Notification pushAlertNotification = new AlertNotification(new PushSender());
        Notification smsAlertNotification = new AlertNotification(new SmsSender());

        emailAlertNotification.notifyUser();
        pushAlertNotification.notifyUser();
        smsAlertNotification.notifyUser();
    }
}

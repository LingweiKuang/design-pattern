package com.klw.behavioral.observer;

public class ObserverDemo {
    public static void main(String[] args) throws InterruptedException {
        StockTicker ticker = new StockTicker();

        Client clientA = new Client() {
            @Override
            public void update(String symbol, double price) {
                System.out.println("[ClientA] " + symbol + " -> " + price);
            }

            @Override
            public String toString() {
                return "ClientA";
            }
        };

        Client clientB = new Client() {
            @Override
            public void update(String symbol, double price) {
                System.out.println("[ClientB] " + symbol + " -> " + price);
            }

            @Override
            public String toString() {
                return "ClientB";
            }
        };

        // clientA 只订阅 AAPL
        ticker.subscribe(clientA, "AAPL");
        // clientB 订阅 AAPL 和 TSLA
        ticker.subscribe(clientB, "AAPL");
        ticker.subscribe(clientB, "TSLA");
        // 假如还有一个管理类想订阅全部：
        Client audit = (s, p) -> System.out.println("[Audit]-" + s + ":" + p);
        ticker.subscribe(audit, "*");

        // 发送价格更新
        ticker.updatePrice("AAPL", 172.55);
        ticker.updatePrice("TSLA", 312.20);

        // clientB 退订 TSLA
        ticker.unsubscribe(clientB, "TSLA");
        ticker.updatePrice("TSLA", 315.00);
    }
}

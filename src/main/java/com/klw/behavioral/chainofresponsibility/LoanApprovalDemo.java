package com.klw.behavioral.chainofresponsibility;

import com.klw.behavioral.chainofresponsibility.entity.LoanApplication;
import com.klw.behavioral.chainofresponsibility.entity.RuleService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 使用示例（并发模拟）
public class LoanApprovalDemo {
    public static void main(String[] args) throws InterruptedException {
        RuleService ruleService = new RuleService();

        // 构建链：Auto -> CustomerService -> Supervisor -> Legal
        AutoRiskHandler auto = new AutoRiskHandler();
        CustomerServiceHandler cs = new CustomerServiceHandler();
        SupervisorHandler sup = new SupervisorHandler();
        LegalHandler legal = new LegalHandler();
        auto.setNext(cs).setNext(sup).setNext(legal);

        ChainManager manager = new ChainManager();
        manager.setChainHead(auto);

        ExecutorService pool = Executors.newFixedThreadPool(4);
        // 几个申请并发提交
        LoanApplication a1 = new LoanApplication(50000, 720, 0);    // 应被 Auto 直接放款
        LoanApplication a2 = new LoanApplication(300000, 580, 2);   // 过期记录太多 -> 拒绝
        LoanApplication a3 = new LoanApplication(150000, 650, 0);   // 流经人工/主管/法务
        LoanApplication a4 = new LoanApplication(80_000, 610, 0);   // 可能被客服拒绝

        LoanApplication[] apps = {a1, a2, a3, a4};

        for (LoanApplication app : apps) {
            pool.submit(() -> manager.process(app, ruleService));
        }

        // 模拟规则热更新：降低自动放款阈值
        ruleService.update(new RuleService.Rules(700, 40000, 1));
        System.out.println("Rules updated at runtime.");

        Thread.sleep(1000); // 等待任务结束
        pool.shutdown();
    }
}

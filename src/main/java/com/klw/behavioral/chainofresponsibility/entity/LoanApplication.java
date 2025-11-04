package com.klw.behavioral.chainofresponsibility.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

// 贷款申请上下文
@Getter
public class LoanApplication {
    // final 值仅支持读取, 无需考虑多线程并发问题
    private final String id = UUID.randomUUID().toString();
    private final double amount;
    private final int creditScore;
    private final int overdueCount;
    // 防御式设计（defensive programming）
    // 考虑未来可能出现的情况：
    // 1. 异步日志记录：日志线程或监控线程可能读取 LoanApplication 的属性（例如异步写入数据库）。
    // 2. 监控/追踪模块：有时业务系统会异步上报申请状态（比如另一个线程拿到对象引用生成审计报告）。
    // 3. 后续处理线程（compensation / saga）：若链上某节点触发异步补偿线程，可能在另一个线程中读写同一上下文对象。
    @Setter
    private volatile String rejectReason;
    @Setter
    private volatile String disbursementId;

    public LoanApplication(double amount, int creditScore, int overdueCount) {
        this.amount = amount;
        this.creditScore = creditScore;
        this.overdueCount = overdueCount;
    }
}

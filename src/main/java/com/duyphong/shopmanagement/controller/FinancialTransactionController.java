package com.duyphong.shopmanagement.controller;


import com.duyphong.shopmanagement.consts.TransactionType;
import com.duyphong.shopmanagement.entity.transaction.FinancialTransaction;
import com.duyphong.shopmanagement.service.FinancialTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    public FinancialTransactionController(FinancialTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST /api/v1/transactions
     * Tạo mới một giao dịch Thu hoặc Chi
     */
    @PostMapping
    public ResponseEntity<FinancialTransaction> createTransaction(@RequestBody FinancialTransaction transaction) {
        // Lưu ý: Cần xử lý việc mapping DTO (Data Transfer Object) ở đây trong dự án thực tế
        FinancialTransaction savedTransaction = transactionService.saveTransaction(transaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/transactions?start=2025-01-01T00:00:00&end=2025-12-31T23:59:59
     * Lấy các giao dịch trong một khoảng thời gian
     */
    @GetMapping
    public ResponseEntity<List<FinancialTransaction>> getTransactions(
            @RequestParam String start,
            @RequestParam String end) {

        // Cần xử lý lỗi định dạng ngày tháng thực tế
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        List<FinancialTransaction> transactions = transactionService.getTransactionsBetween(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    // --- API BÁO CÁO TỔNG HỢP ---

    /**
     * GET /api/v1/transactions/report/total?type=INCOME&start=...&end=...
     * Tính tổng thu hoặc tổng chi trong khoảng thời gian
     */
    @GetMapping("/report/total")
    public ResponseEntity<BigDecimal> getTotalByType(
            @RequestParam TransactionType type,
            @RequestParam String start,
            @RequestParam String end) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        BigDecimal total = transactionService.calculateTotal(startDate, endDate, type);
        return ResponseEntity.ok(total);
    }

    /**
     * GET /api/v1/transactions/report/net-profit?start=...&end=...
     * Tính Lợi nhuận Ròng (Net Profit)
     */
    @GetMapping("/report/net-profit")
    public ResponseEntity<BigDecimal> getNetProfit(
            @RequestParam String start,
            @RequestParam String end) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        BigDecimal netProfit = transactionService.calculateNetProfit(startDate, endDate);
        return ResponseEntity.ok(netProfit);
    }
}
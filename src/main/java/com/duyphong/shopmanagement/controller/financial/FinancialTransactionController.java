package com.duyphong.shopmanagement.controller.financial;

import com.duyphong.shopmanagement.entity.transaction.FinancialTransaction;
import com.duyphong.shopmanagement.enums.TransactionType;
import com.duyphong.shopmanagement.model.request.revenue.CreateTransactionRequest;
import com.duyphong.shopmanagement.model.response.revenue.FinancialTransactionResponse;
import com.duyphong.shopmanagement.service.financial.FinancialTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    /**
     * POST /api/transactions
     * Tạo mới một giao dịch Thu hoặc Chi
     */
    @PostMapping
    public ResponseEntity<FinancialTransaction> createTransaction(@RequestBody CreateTransactionRequest request) {
        FinancialTransaction savedTransaction = transactionService.saveTransaction(request);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    /**
     * GET /api/transactions?start=2025-01-01T00:00:00&end=2025-12-31T23:59:59
     * Lấy các giao dịch trong một khoảng thời gian
     */
    @GetMapping
    public ResponseEntity<List<FinancialTransactionResponse>> getTransactions(
            @RequestParam String start,
            @RequestParam String end) {

        return ResponseEntity.ok(transactionService.getTransactionsBetween(start, end));
    }

    // --- API BÁO CÁO TỔNG HỢP ---

    /**
     * GET /api/transactions/report/total?type=INCOME&start=...&end=...
     * Tính tổng thu hoặc tổng chi trong khoảng thời gian
     */
    @GetMapping("/report/total")
    public ResponseEntity<BigDecimal> getTotalByType(
            @RequestParam TransactionType type,
            @RequestParam String start,
            @RequestParam String end) {
        BigDecimal total = transactionService.calculateTotal(start, end, type);
        return ResponseEntity.ok(total);
    }

    /**
     * GET /api/transactions/report/net-profit?start=...&end=...
     * Tính Lợi nhuận Ròng (Net Profit)
     */
    @GetMapping("/report/net-profit")
    public ResponseEntity<BigDecimal> getNetProfit(
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(transactionService.calculateNetProfit(start, end));
    }
}
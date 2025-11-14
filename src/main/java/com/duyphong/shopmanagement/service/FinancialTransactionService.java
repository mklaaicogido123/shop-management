package com.duyphong.shopmanagement.service;


import com.duyphong.shopmanagement.consts.TransactionType;
import com.duyphong.shopmanagement.entity.transaction.FinancialTransaction;
import com.duyphong.shopmanagement.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final FinancialClassificationService classificationService; // Cần dùng để kiểm tra classification

    public FinancialTransactionService(FinancialTransactionRepository transactionRepository,
                                       FinancialClassificationService classificationService) {
        this.transactionRepository = transactionRepository;
        this.classificationService = classificationService;
    }

    /**
     * Lưu (Thêm/Cập nhật) một giao dịch mới
     */
    public FinancialTransaction saveTransaction(FinancialTransaction transaction) {
        // Kiểm tra logic nghiệp vụ:
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền giao dịch phải lớn hơn 0.");
        }

        // Đảm bảo classification tồn tại (Quan trọng!)
        String classificationId = transaction.getClassification().getId();
        classificationService.findById(classificationId)
                .orElseThrow(() -> new IllegalArgumentException("ID phân loại không hợp lệ: " + classificationId));

        return transactionRepository.save(transaction);
    }

    /**
     * Lấy tất cả các giao dịch trong một khoảng thời gian
     */
    public List<FinancialTransaction> getTransactionsBetween(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTransactionDateBetween(start, end);
    }

    // --- LOGIC BÁO CÁO TÍNH TOÁN (QUAN TRỌNG) ---

    /**
     * Tính tổng các khoản Thu và Chi trong một khoảng thời gian.
     */
    public BigDecimal calculateTotal(LocalDateTime start, LocalDateTime end, TransactionType type) {
        return getTransactionsBetween(start, end).stream()
                .filter(t -> t.getClassification().getType() == type)
                .map(FinancialTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Tính Lợi nhuận Ròng (Net Profit) từ các khoản Thu Chi (Chưa bao gồm Doanh thu Bán hàng)
     */
    public BigDecimal calculateNetProfit(LocalDateTime start, LocalDateTime end) {
        BigDecimal totalIncome = calculateTotal(start, end, TransactionType.INCOME);
        BigDecimal totalExpense = calculateTotal(start, end, TransactionType.EXPENSE);

        return totalIncome.subtract(totalExpense);
    }
}

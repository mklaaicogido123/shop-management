package com.duyphong.shopmanagement.service.financial;


import com.duyphong.shopmanagement.entity.transaction.FinancialClassification;
import com.duyphong.shopmanagement.entity.transaction.FinancialTransaction;
import com.duyphong.shopmanagement.enums.TransactionType;
import com.duyphong.shopmanagement.mapper.financial.TransactionMapper;
import com.duyphong.shopmanagement.model.request.revenue.CreateTransactionRequest;
import com.duyphong.shopmanagement.model.response.revenue.FinancialTransactionResponse;
import com.duyphong.shopmanagement.repository.financial.FinancialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final FinancialClassificationService classificationService;
    private final TransactionMapper transactionMapper;


    /**
     * Lưu (Thêm/Cập nhật) một giao dịch mới
     */
    public FinancialTransaction saveTransaction(CreateTransactionRequest request) {
        // Kiểm tra logic nghiệp vụ:
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền giao dịch phải lớn hơn 0.");
        }

        // Đảm bảo classification tồn tại (Quan trọng!)
        String classificationId = request.getClassificationId();
        FinancialClassification financialClassification = classificationService.findById(classificationId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại giao dịch"));

        return transactionRepository.save(FinancialTransaction.builder()
                .transactionDate(LocalDateTime.now())
                .amount(request.getAmount())
                .description(request.getDescription())
                .classification(financialClassification)
                .build());
    }

    /**
     * Lấy tất cả các giao dịch trong một khoảng thời gian
     */
    public List<FinancialTransactionResponse> getTransactionsBetween(String start, String end) {
        return transactionMapper.toDtoList(transactionRepository.findTransactionDateBetween(start, end));
    }

    // --- LOGIC BÁO CÁO TÍNH TOÁN (QUAN TRỌNG) ---

    /**
     * Tính tổng các khoản Thu và Chi trong một khoảng thời gian.
     */
    public BigDecimal calculateTotal(String start, String end, TransactionType type) {
        return getTransactionsBetween(start, end).stream()
                .filter(t -> t.getClassificationType().equals(type.toString()))
                .map(FinancialTransactionResponse::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Tính Lợi nhuận Ròng (Net Profit) từ các khoản Thu Chi (Chưa bao gồm Doanh thu Bán hàng)
     */
    public BigDecimal calculateNetProfit(String start, String end) {
        BigDecimal totalIncome = calculateTotal(start, end, TransactionType.INCOME);
        BigDecimal totalExpense = calculateTotal(start, end, TransactionType.EXPENSE);

        return totalIncome.subtract(totalExpense);
    }
}

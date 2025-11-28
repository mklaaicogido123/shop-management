package com.duyphong.shopmanagement.service.financial;


import ch.qos.logback.core.util.StringUtil;
import com.duyphong.shopmanagement.entity.transaction.FinancialClassification;
import com.duyphong.shopmanagement.enums.TransactionType;
import com.duyphong.shopmanagement.model.request.revenue.CreateOrUpdateClassificationRequest;
import com.duyphong.shopmanagement.repository.financial.FinancialClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinancialClassificationService {

    private final FinancialClassificationRepository classificationRepository;


    /**
     * Lấy tất cả các danh mục
     */
    public List<FinancialClassification> findAllClassifications() {
        return classificationRepository.findAll();
    }

    /**
     * Lấy danh mục theo ID
     */
    public Optional<FinancialClassification> findById(String id) {
        return classificationRepository.findById(id);
    }

    /**
     * Lấy danh mục theo loại (INCOME hoặc EXPENSE)
     */
    public List<FinancialClassification> findByType(TransactionType type) {
        return classificationRepository.findByType(type);
    }

    /**
     * Lưu (Thêm/Cập nhật) một danh mục mới
     */
    public String saveClassification(CreateOrUpdateClassificationRequest request) {
        if (StringUtil.isNullOrEmpty(request.getName())) {
            throw new IllegalArgumentException("Tên phân loại không được để trống");
        }
        classificationRepository.save(FinancialClassification.builder()
                .name(request.getName())
                .type(request.getType())
                .build());
        return "SUCCCESS";
    }

    /**
     * Xóa một danh mục
     */
    public String deleteClassification(String id) {
        if (!classificationRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy phân loại");
        }
        classificationRepository.deleteById(id);
        return "DELETED";
    }
}

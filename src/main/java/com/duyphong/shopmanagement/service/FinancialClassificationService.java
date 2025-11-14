package com.duyphong.shopmanagement.service;


import com.duyphong.shopmanagement.consts.TransactionType;
import com.duyphong.shopmanagement.entity.transaction.FinancialClassification;
import com.duyphong.shopmanagement.repository.FinancialClassificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialClassificationService {

    private final FinancialClassificationRepository classificationRepository;

    public FinancialClassificationService(FinancialClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

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
    public FinancialClassification saveClassification(FinancialClassification classification) {
        // Có thể thêm logic kiểm tra dữ liệu đầu vào (validation) ở đây
        if (classification.getName() == null || classification.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phân loại không được để trống.");
        }
        return classificationRepository.save(classification);
    }

    /**
     * Xóa một danh mục
     */
    public void deleteClassification(String id) {
        classificationRepository.deleteById(id);
    }
}

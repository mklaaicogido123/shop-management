package com.duyphong.shopmanagement.controller;

import com.duyphong.shopmanagement.consts.TransactionType;
import com.duyphong.shopmanagement.entity.transaction.FinancialClassification;
import com.duyphong.shopmanagement.service.FinancialClassificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classifications")
public class FinancialClassificationController {

    private final FinancialClassificationService classificationService;

    public FinancialClassificationController(FinancialClassificationService classificationService) {
        this.classificationService = classificationService;
    }

    /**
     * POST /api/v1/classifications
     * Tạo mới hoặc cập nhật một phân loại
     */
    @PostMapping
    public ResponseEntity<FinancialClassification> createOrUpdateClassification(@RequestBody FinancialClassification classification) {
        // Lưu ý: Trong thực tế, bạn nên dùng DTO cho đầu vào thay vì Entity trực tiếp
        FinancialClassification savedClassification = classificationService.saveClassification(classification);
        return new ResponseEntity<>(savedClassification, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/classifications
     * Lấy tất cả các phân loại
     */
    @GetMapping
    public ResponseEntity<List<FinancialClassification>> getAllClassifications() {
        List<FinancialClassification> classifications = classificationService.findAllClassifications();
        return ResponseEntity.ok(classifications);
    }

    /**
     * GET /api/v1/classifications/type?type=INCOME
     * Lấy phân loại theo loại (INCOME/EXPENSE)
     */
    @GetMapping("/type")
    public ResponseEntity<List<FinancialClassification>> getClassificationsByType(@RequestParam TransactionType type) {
        List<FinancialClassification> classifications = classificationService.findByType(type);
        return ResponseEntity.ok(classifications);
    }

    /**
     * DELETE /api/v1/classifications/{id}
     * Xóa một phân loại
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassification(@PathVariable String id) {
        classificationService.deleteClassification(id);
        return ResponseEntity.noContent().build();
    }
}
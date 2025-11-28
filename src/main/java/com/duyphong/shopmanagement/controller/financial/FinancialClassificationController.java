package com.duyphong.shopmanagement.controller.financial;

import com.duyphong.shopmanagement.entity.transaction.FinancialClassification;
import com.duyphong.shopmanagement.enums.TransactionType;
import com.duyphong.shopmanagement.model.request.revenue.CreateOrUpdateClassificationRequest;
import com.duyphong.shopmanagement.service.financial.FinancialClassificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classifications")
public class FinancialClassificationController {

    private final FinancialClassificationService classificationService;

    public FinancialClassificationController(FinancialClassificationService classificationService) {
        this.classificationService = classificationService;
    }

    /**
     * POST /apiclassifications
     * Tạo mới hoặc cập nhật một phân loại
     */
    @PostMapping
    public ResponseEntity<String> createOrUpdateClassification(@RequestBody CreateOrUpdateClassificationRequest request) {
        return new ResponseEntity<>(classificationService.saveClassification(request), HttpStatus.CREATED);
    }

    /**
     * GET /api/classifications
     * Lấy tất cả các phân loại
     */
    @GetMapping
    public ResponseEntity<List<FinancialClassification>> getAllClassifications() {
        List<FinancialClassification> classifications = classificationService.findAllClassifications();
        return ResponseEntity.ok(classifications);
    }

    /**
     * GET /api/classifications/type?type=INCOME
     * Lấy phân loại theo loại (INCOME/EXPENSE)
     */
    @GetMapping("/type")
    public ResponseEntity<List<FinancialClassification>> getClassificationsByType(@RequestParam TransactionType type) {
        List<FinancialClassification> classifications = classificationService.findByType(type);
        return ResponseEntity.ok(classifications);
    }

    /**
     * DELETE /api/classifications/{id}
     * Xóa một phân loại
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClassification(@PathVariable String id) {
        return ResponseEntity.ok(classificationService.deleteClassification(id));
    }
}
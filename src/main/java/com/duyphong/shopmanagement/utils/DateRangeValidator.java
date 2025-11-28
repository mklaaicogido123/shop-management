package com.duyphong.shopmanagement.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startFieldName;
    private String endFieldName;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        // Lấy tên trường động từ annotation khi nó được khởi tạo
        this.startFieldName = constraintAnnotation.startField();
        this.endFieldName = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        try {
            // Lấy giá trị của các trường động bằng Reflection
            String startValue = (String) getValue(request, startFieldName);
            String endValue = (String) getValue(request, endFieldName);

            if (startValue == null || endValue == null) {
                return true; // Để @NotNull xử lý
            }

            LocalDate startDate = LocalDate.parse(startValue);
            LocalDate endDate = LocalDate.parse(endValue);

            if (startDate.isAfter(endDate)) {
                // Tùy chỉnh lỗi và gán vào tên trường động
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(startFieldName) // <-- SỬ DỤNG TÊN TRƯỜNG ĐỘNG
                        .addConstraintViolation();
                return false;
            }
            return true;
        } catch (Exception e) {
            // Nếu có lỗi Reflection hoặc Parse, ta coi như thành công
            // để lỗi khác (như @Pattern) bắt
            return true;
        }
    }

    // Hàm tiện ích để lấy giá trị của trường bằng Reflection (giữ nguyên)
    private Object getValue(Object source, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = source.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(source);
    }
}

package com.duyphong.shopmanagement.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {

    // 1. Thêm thuộc tính để chỉ định tên trường BẮT ĐẦU
    String startField() default "start";

    // 2. Thêm thuộc tính để chỉ định tên trường KẾT THÚC
    String endField() default "end";

    String message() default "Ngày bắt đầu không được sau ngày kết thúc.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.duyphong.shopmanagement.model.request.common;


import com.duyphong.shopmanagement.utils.ValidDateRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ValidDateRange
public class FromToDateRequest {

    // Regex chuẩn cho YYYY-MM-DD
    private static final String DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    @NotBlank(message = "Ngày bắt đầu không được để trống.")
    @Pattern(regexp = DATE_REGEX, message = "Ngày bắt đầu phải theo định dạng YYYY-MM-DD.")
    String startDate;

    @NotBlank(message = "Ngày kết thúc không được để trống.")
    @Pattern(regexp = DATE_REGEX, message = "Ngày kết thúc phải theo định dạng YYYY-MM-DD.")
    String endDate;
}

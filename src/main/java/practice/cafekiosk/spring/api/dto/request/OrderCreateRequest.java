package practice.cafekiosk.spring.api.dto.request;

import java.util.List;

public record OrderCreateRequest(
        List<String> productNumbers
) {
}

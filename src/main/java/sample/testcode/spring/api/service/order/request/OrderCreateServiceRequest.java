package sample.testcode.spring.api.service.order.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.testcode.spring.api.controller.order.request.OrderCreateRequest;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    private List<String> productNumbers;

    @Builder
    private OrderCreateServiceRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public static OrderCreateServiceRequest toServiceRequest(OrderCreateRequest orderCreateRequest) {
        return OrderCreateServiceRequest.builder()
                .productNumbers(orderCreateRequest.getProductNumbers())
                .build();
    }
}

package sample.testcode.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.testcode.spring.api.controller.order.request.OrderCreateRequest;
import sample.testcode.spring.api.service.order.response.OrderResponse;
import sample.testcode.spring.domain.order.Order;
import sample.testcode.spring.domain.order.OrderRepository;
import sample.testcode.spring.domain.product.Product;
import sample.testcode.spring.domain.product.ProductRepository;
import sample.testcode.spring.domain.product.ProductType;
import sample.testcode.spring.domain.stock.Stock;
import sample.testcode.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime now) {
        List<String> productNumbers = request.getProductNumbers();
        // 중복을 고려한 Product
        List<Product> duplicateProducts = findProductsBy(productNumbers);

        deductStockQuantity(duplicateProducts);

        // Order
        Order order = Order.create(duplicateProducts, now);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    private void deductStockQuantity(List<Product> duplicateProducts) {
        //재고 차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = extractStockProductNumbers(duplicateProducts);

        //재고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = createStockMapBy(stocks);

        // 상품별 counting
        Map<String, Long> productCountingMap = CreateCountingMapBy(stockProductNumbers);

        //재고 차감 시도
        for(String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();
            if (stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다");
            }
            stock.deductQuantity(quantity);
        }
    }

    private Map<String, Long> CreateCountingMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

    private Map<String, Stock> createStockMapBy(List<Stock> stocks) {
        return stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
    }

    private List<String> extractStockProductNumbers(List<Product> duplicateProducts) {
        return duplicateProducts.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));
        List<Product> duplicateProducts = productNumbers.stream()
                .map(productNumber -> productMap.get(productNumber))
                .collect(Collectors.toList());
        return duplicateProducts;
    }
}

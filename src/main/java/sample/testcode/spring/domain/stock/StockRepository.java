package sample.testcode.spring.domain.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.testcode.spring.domain.product.Product;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAllByProductNumberIn(List<String> productNumbers);
}

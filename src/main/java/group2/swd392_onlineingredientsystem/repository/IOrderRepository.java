package group2.swd392_onlineingredientsystem.repository;

import group2.swd392_onlineingredientsystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
}

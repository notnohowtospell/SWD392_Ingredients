package group2.swd392_onlineingredientsystem.repository;

import group2.swd392_onlineingredientsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    // Có thể thêm phương thức custom nếu cần
}
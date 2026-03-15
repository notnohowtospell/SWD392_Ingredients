package group2.swd392_onlineingredientsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userId;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

//    @Column(name = "balance")
//    private double balance = 0;

    @Column(name = "Money")
    private double money = 0;


    @ManyToOne
    @JoinColumn(name = "roleid")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
} 
package group2.swd392_onlineingredientsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Column(name = "roleid")
    private Integer roleId;

    @Column(name = "rolename", nullable = false, length = 50)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<User> users;
} 
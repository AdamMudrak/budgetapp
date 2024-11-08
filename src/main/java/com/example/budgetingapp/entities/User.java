package com.example.budgetingapp.entities;

import static com.example.budgetingapp.constants.entities.EntitiesConstants.BOOLEAN_TO_INT;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.ROLE_ID;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.USERS;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.USERS_ROLES_JOIN_TABLE;
import static com.example.budgetingapp.constants.entities.EntitiesConstants.USER_ID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@Table(name = USERS)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = USERS_ROLES_JOIN_TABLE,
            joinColumns = @JoinColumn(name = USER_ID),
            inverseJoinColumns = @JoinColumn(name = ROLE_ID)
    )
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false, columnDefinition = BOOLEAN_TO_INT)
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } //Not in use so far

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } //Not in use so far

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } //Not in use so far

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}

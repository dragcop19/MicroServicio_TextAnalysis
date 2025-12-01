package com.example.marketing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private OffsetDateTime creationDate;

    // Relación con Roles
    @ManyToOne(fetch = FetchType.EAGER) // EAGER es más simple para seguridad
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // --- PrePersist para la fecha de creación ---
    @PrePersist
    protected void onCreate() {
        creationDate = OffsetDateTime.now();
    }

    // --- Getters y Setters (Parciales, o usa Lombok) ---

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
    
    // ... más getters y setters si los necesitas ...

    // --- Implementación de Métodos UserDetails ---

    @Override
    public String getUsername() {
        // Usamos el email como nombre de usuario para Spring Security
        return this.email;
    }

    @Override
    public String getPassword() {
        // Usamos el hash de la BD
        return this.passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aquí convertimos nuestro Rol en un "Authority" de Spring
        if (this.role == null) {
            return Collections.emptyList();
        }
        // Spring Security requiere el prefijo "ROLE_"
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + this.role.getRoleName());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isEnabled() {
        // Usamos nuestro campo 'is_active'
        return this.isActive;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Asumimos que nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Asumimos que nunca se bloquea
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Asumimos que las credenciales nunca expiran
    }
}
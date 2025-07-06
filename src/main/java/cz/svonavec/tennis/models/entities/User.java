package cz.svonavec.tennis.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@ToString
@Table(name = "Users")
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @Size(max = 30)
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Size(min = 8)
    @Column(name = "password")
    private String password;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    /**
     * Validates phone number
     *
     * @return true, if the number begins with "+" character and contains only numbers and common formatting characters
     */
    public boolean validatePhoneNumber () {
        if (phoneNumber == null || phoneNumber.trim().isEmpty() || phoneNumber.length() > 30) {
            return false;
        }
        return phoneNumber.matches("^\\+[\\d\\s\\-().]+$");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }
        return Objects.equals(getName(), user.getName()) && Objects.equals(getPhoneNumber(), user.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPhoneNumber());
    }
}

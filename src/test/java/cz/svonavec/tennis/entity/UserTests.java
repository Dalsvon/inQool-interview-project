package cz.svonavec.tennis.entity;

import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTests {
    @Test
    public void testUserDetailsFunctions_allFunctionCorrectly_allReturnTrue() {
        User user = UserFactory.createUser("+421907123456", "John Smith", "passworD15");
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void testUserGetFunctions_allFunctionCorrectly_allReturnDataCorrectly() {
        User user = UserFactory.createUser("+421907123456", "John Smith", "passworD15");
        assertThat(user.getUsername()).isEqualTo("+421907123456");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        assertThat(user.getAuthorities()).isEqualTo(authorities);
    }

    @Test
    public void testUserValidateGoodPhoneNumber() {
        User user = UserFactory.createUser("+421907123456", "John Smith", "passworD15");
        assertThat(user.validatePhoneNumber()).isTrue();
    }

    @Test
    public void testUserValidatePhoneNumberNoPlus() {
        User user = UserFactory.createUser("421907123456", "John Smith", "passworD15");
        assertThat(user.validatePhoneNumber()).isFalse();
    }

    @Test
    public void testUserValidatePhoneNumberNonAllowedChars() {
        User user = UserFactory.createUser("+42190a123456", "John Smith", "passworD15");
        assertThat(user.validatePhoneNumber()).isFalse();
    }
}

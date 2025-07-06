package cz.svonavec.tennis.models.entities;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return this.roleName;
    }
}

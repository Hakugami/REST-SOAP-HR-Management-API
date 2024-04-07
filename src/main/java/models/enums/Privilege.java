package models.enums;

import lombok.Getter;

@Getter
public enum Privilege {
    NONE(0),
    EMPLOYEE(1),
    HR(2),
    MANAGER(3),
    CEO(4);

    private final int level;

    Privilege(int level) {
        this.level = level;
    }

    public static Privilege getPrivilege(int level) {
        for (Privilege privilege : Privilege.values()) {
            if (privilege.level == level) {
                return privilege;
            }
        }
        return NONE;
    }
}

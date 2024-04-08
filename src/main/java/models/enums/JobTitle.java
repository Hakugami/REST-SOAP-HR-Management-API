package models.enums;

import lombok.Getter;

@Getter
public enum JobTitle {
    ENTRY_LEVEL("Entry Level", 0, 1000, 0, "Entry level position"),
    JUNIOR_DEVELOPER("Junior Developer", 1000, 2000, 1, "Junior developer position"),
    DEVELOPER("Developer", 2000, 3000, 2, "Developer position"),
    SENIOR_DEVELOPER("Senior Developer", 3000, 4000, 3, "Senior developer position"),
    HR("HR", 2000, 3000, 2, "Human resources position"),
    MANAGER("Manager", 4000, 5000, 5, "Manager position"),
    CEO("CEO", 10000, 20000, 10, "CEO position");

    private final String title;
    private final int minSalary;
    private final int maxSalary;
    private final int requiredExperience;
    private final String description;

    JobTitle(String title, int minSalary, int maxSalary, int requiredExperience, String description) {
        this.title = title;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.requiredExperience = requiredExperience;
        this.description = description;
    }

}
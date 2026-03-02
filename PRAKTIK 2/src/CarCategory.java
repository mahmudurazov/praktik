public enum CarCategory {
    SEDAN("Седан", "#06b6d4"),
    SUV("Внедорожник", "#8b5cf6"),
    ELECTRIC("Электро", "#10b981"),
    HATCHBACK("Хэтчбек", "#f59e0b"),
    COUPE("Купе", "#ec4899"),
    TRUCK("Грузовик", "#6366f1");

    private final String displayName;
    private final String colorCode;

    CarCategory(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
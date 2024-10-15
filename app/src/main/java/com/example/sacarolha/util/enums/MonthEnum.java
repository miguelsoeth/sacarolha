package com.example.sacarolha.util.enums;

public enum MonthEnum {
    JANUARY("Jan"),
    FEBRUARY("Feb"),
    MARCH("Mar"),
    APRIL("Apr"),
    MAY("May"),
    JUNE("Jun"),
    JULY("Jul"),
    AUGUST("Aug"),
    SEPTEMBER("Sep"),
    OCTOBER("Oct"),
    NOVEMBER("Nov"),
    DECEMBER("Dec");

    private final String shortName;

    MonthEnum(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static MonthEnum fromNumber(int monthNumber) {
        switch (monthNumber) {
            case 1: return JANUARY;
            case 2: return FEBRUARY;
            case 3: return MARCH;
            case 4: return APRIL;
            case 5: return MAY;
            case 6: return JUNE;
            case 7: return JULY;
            case 8: return AUGUST;
            case 9: return SEPTEMBER;
            case 10: return OCTOBER;
            case 11: return NOVEMBER;
            case 12: return DECEMBER;
            default: throw new IllegalArgumentException("Invalid month number: " + monthNumber);
        }
    }
}


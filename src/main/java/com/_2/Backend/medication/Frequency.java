package com._2.Backend.medication;

import lombok.Getter;

@Getter
public enum Frequency {
    ONCE_A_DAY("Una vez al día"),
    TWICE_A_DAY("Dos veces al día"),
    THREE_TIMES_A_DAY("Tres veces al día"),
    FOUR_TIMES_A_DAY("Cuatro veces al día"),
    EVERY_FOUR_HOURS("Cada 4 horas"),
    EVERY_SIX_HOURS("Cada 6 horas"),
    EVERY_EIGHT_HOURS("Cada 8 horas"),
    AS_NEEDED("Según sea necesario");

    private final String displayInSpanish;

    Frequency(String displayInSpanish) {
        this.displayInSpanish = displayInSpanish;
    }
}
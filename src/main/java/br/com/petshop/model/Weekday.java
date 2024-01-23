package br.com.petshop.model;

public enum Weekday {
    SUNDAY ("Domingo"),
    MONDAY ("Segunda-feira"),
    TUESDAY ("Terça-feira"),
    WEDNESDAY ("Quarta-feira"),
    THURSDAY ("Quinta-feira"),
    FRIDAY ("Sexta-feira"),
    SATURDAY ("Sábado"),;

    private String label;

    Weekday(String label) {
        this.label = label;
    }

    public String get() {
        return label;
    }
}

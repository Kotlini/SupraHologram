package fr.ylanouh.supraholograms.messages;

public enum Prefix {
    PREFIX("§9§l( §bSupraHolograms §9§l) "),
    PREFIX_ERROR("§4§l( §cSorry §4§l)  "),
    ERROR_ARGS("");

    private final String message;

    Prefix(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

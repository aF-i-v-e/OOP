package ru.praktika95.bot;

public enum E {
    A("sas") {
        @Override
        public void handle() {

        }
    },
    B("") {
        @Override
        public void handle() {

        }
    };

    private final String type;

    E(String type) {
        this.type = type;
    }

    public abstract void handle();

    public static E findValue(String value) {
        for (E e : values()) {
            if (e.type.equals(value)) {
                return e;
            }
        }
        return null;
    }
}

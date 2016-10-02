package net.interfax.rest.client.domain.enums;

public enum Sharing {

    sharingDoc ("shared"),
    privateDoc ("private");

    private final String value;

    private Sharing(String s) {
        value = s;
    }

    public boolean equalsName(String otherName) {
        return otherName != null && value.equals(otherName);
    }

    public String toString() {
        return this.value;
    }
}

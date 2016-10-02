package net.interfax.rest.client.domain.enums;

public enum Disposition {

    singleUse ("singleUse"),
    multiUse ("multiUse"),
    permanent ("permanent");

    private final String name;

    private Disposition(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return otherName != null && name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}

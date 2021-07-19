package Type;

public class BooleanType extends Type {

    public String toString() {
        return "boolean";
    }

    public boolean equals(Object o) {
        if (o instanceof BooleanType) {
            return true;
        }
        return false;
    }

    public String toChar() {
        return "Z";
    }
}

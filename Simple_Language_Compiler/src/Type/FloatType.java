package Type;

public class FloatType extends Type {

    public String toString() {
        return "float";
    }

    public boolean equals(Object o) {
        if (o instanceof FloatType) {
            return true;
        }
        return false;
    }

    public String toChar() {
        return "F";
    }
}

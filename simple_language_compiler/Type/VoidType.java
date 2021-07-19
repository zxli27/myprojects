package Type;

public class VoidType extends Type {

    public String toString() {
        return "void";
    }
    public boolean equals(Object o) {
        if (o instanceof VoidType) {
            return true;
        }
        return false;
    }

    public String toChar() {
        return "V";
    }
}

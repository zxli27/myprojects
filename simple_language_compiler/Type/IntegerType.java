package Type;

public class IntegerType extends Type {

    public String toString() {
        return "int";
    }

    public boolean equals(Object o) {
        if (o instanceof IntegerType) {
            return true;
        }
        return false;
    }

    public String toChar() {
        return "I";
    }
}

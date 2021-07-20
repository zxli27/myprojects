package Type;

public class StringType extends Type {

    public String toString() {
        return "string";
    }

    public boolean equals(Object o) {
        if (o instanceof StringType) {
            return true;
        }
        return false;
    }

    public String toChar() {
        return "U";
    }
}

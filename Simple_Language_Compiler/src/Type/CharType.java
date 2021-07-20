package Type;

public class CharType extends Type {

    public String toString() {
        return "char";
    }

    public boolean equals(Object o) {
        if (o instanceof CharType) {
            return true;
        }
        return false;
    }

    public String toChar() {
        return "C";
    }
}

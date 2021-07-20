package Type;

public class ArrayType extends Type {
    public Type type;
    public int size;

    public ArrayType(Type type, int size) {
        this.type = type;
        this.size = size;
    }

    public String toString() {
        return this.type.toString() + "[" + this.size + "]" ;
    }

    public boolean equals(Object o) {
        if (o instanceof ArrayType && ((ArrayType)o).size == this.size) {
            return true;
        }
        return false;
    }

    public String toChar() {
        return "A" + this.type.toChar();
    }
}

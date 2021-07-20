package Environment;

import java.util.Stack;
import java.util.HashMap;

public class StackEnvironment<K, V> implements Environment<K, V> {
    private Stack<HashMap<K, V>> stack;

    public StackEnvironment() {
        this.stack = new Stack<HashMap<K, V>>();
    }

    public void beginScope() {
        this.stack.push(new HashMap<K, V>());
    }

    public void endScope() {
        this.stack.pop();
    }

    public boolean inCurrentScope(K key) {
        return this.stack.peek().containsKey(key);
    }

    public void add(K key, V value) {
        this.stack.peek().put(key, value);
    }

    public V lookup(K key) {      
        return this.stack.peek().get(key);
    }
}

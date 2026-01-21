public interface IHashTable<K, V> {
    void put(K key, V value);
    PostingList get(K key);
    void remove(K key);
    void resize(int newCapacity);
}

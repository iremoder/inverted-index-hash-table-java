public class HashEntry<K, V> {
    K key;
    PostingList postingList;
    boolean deleted;

    public HashEntry(K key) {
        this.key = key;
        this.postingList = new PostingList();
        this.deleted = false;
    }
}
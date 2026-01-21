import java.util.ArrayList;
@SuppressWarnings("unchecked")
public class HashTable<K, V> implements IHashTable<K, V> {

    private HashEntry<K, V>[] table;
    private int size;
    private double loadFactor;
    private IHashFunction hashFunction;
    private ICollisionResolver resolver;
    private int collisionCount=0;

    public HashTable(int capacity, double loadFactor, IHashFunction hashFunction, ICollisionResolver resolver) {
        this.table = (HashEntry<K, V>[]) new HashEntry[capacity];
        this.size = 0;
        this.loadFactor = loadFactor;
        this.hashFunction = hashFunction;
        this.resolver = resolver;
    }
    public int getCollisionCount() {
        return collisionCount;
    }

    private HashEntry<K, V> findEntry(K key) {
        int tableSize = table.length;
        String keyStr = key.toString();

        int hashVal = hashFunction.hash(keyStr, tableSize);
        int i = 0;
        int index = hashVal;
        int secondary = 0;

        if (resolver instanceof DoubleHashing) {
            secondary = ((DoubleHashing) resolver).secondaryHash(keyStr, tableSize);
        }

        while (table[index] != null) {
            if (!table[index].deleted && table[index].key.equals(key)) {
                return table[index];
            }
            i++;
            index = resolver.probe(hashVal, i, tableSize, secondary);
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if ((double) size / table.length >= loadFactor) {
            resize(nextPrime(table.length * 2));
        }
        String keyStr = (String) key;
        int hashVal = hashFunction.hash(keyStr, table.length);
        int i = 0;
        int index = hashVal;
        int secondary = 0;

        if (resolver instanceof DoubleHashing) {
            secondary = ((DoubleHashing) resolver).secondaryHash(keyStr, table.length);
        }

        while (table[index] != null) {
            if (!table[index].deleted && table[index].key.equals(key)) {
                table[index].postingList.addOrIncrement(value.toString());
                return;
            }
            collisionCount++;
            i++;
            index = resolver.probe(hashVal, i, table.length, secondary);
        }
        HashEntry<K, V> newEntry = new HashEntry<>(key);
        newEntry.postingList.addOrIncrement(value.toString());
        table[index] = newEntry;
        size++;
    }
    @Override
    public PostingList get(K key) {
        System.out.println("> Search: " + key);
        HashEntry<K, V> entry = findEntry(key);

        if (entry == null) {
            System.out.println("Not found!");
            System.out.println();
            return null;
        } else {
            ArrayList<DocumentEntry> docs = entry.postingList.getDocuments();
            System.out.println(docs.size() + " documents found");

            for (DocumentEntry doc : docs) {
                System.out.println(doc.getFrequency() + "-" + doc.getDocName());
            }
            System.out.println();
            return entry.postingList;
        }
    }

    public PostingList getSearch(K key) {
        HashEntry<K, V> entry = findEntry(key);
        return (entry != null) ? entry.postingList : null;
    }

    @Override
    public void resize(int newCapacity) {
        HashEntry<K, V>[] oldTable = table;
        table = (HashEntry<K, V>[]) new HashEntry[newCapacity];
        size = 0;

        for (HashEntry<K, V> entry : oldTable) {
            if (entry != null && !entry.deleted) {
                String keyStr = (String) entry.key;
                int hashVal = hashFunction.hash(keyStr, newCapacity);
                int i = 0;
                int index = hashVal;
                int secondary = 0;

                if (resolver instanceof DoubleHashing) {
                    secondary = ((DoubleHashing) resolver).secondaryHash(keyStr, newCapacity);
                }

                while (table[index] != null) {
                    i++;
                    index = resolver.probe(hashVal, i, newCapacity, secondary);
                }
                table[index] = entry;
                size++;
            }
        }
    }
    @Override
    public void remove(K key) {
        int tableSize = table.length;
        String keyStr = (String) key;
        int hashVal = hashFunction.hash(keyStr, tableSize);
        int i = 0;
        int index = hashVal;
        int secondary = 0;

        if (resolver instanceof DoubleHashing) {
            secondary = ((DoubleHashing) resolver).secondaryHash(keyStr, tableSize);
        }
        while (table[index] != null) {
            if (!table[index].deleted && table[index].key.equals(key)) {

                table[index].deleted = true;
                size--;
                return;
            }
            i++;
            index = resolver.probe(hashVal, i, tableSize, secondary);
        }
    }
    private boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num <= 3) return true;
        if (num % 2 == 0 || num % 3 == 0) return false;
        for (int i = 5; i * i <= num; i = i + 6) {
            if (num % i == 0 || num % (i + 2) == 0)
                return false;
        }
        return true;
    }
    private int nextPrime(int num) {
        if (num % 2 == 0) num++;
        while (!isPrime(num)) {
            num += 2;
        }
        return num;
    }
}
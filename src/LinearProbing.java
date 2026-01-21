public class LinearProbing implements ICollisionResolver {
    @Override
    public int probe(int hashIndex, int i, int tableSize, int secondaryHash) {
        return (hashIndex + i) % tableSize;
    }
}

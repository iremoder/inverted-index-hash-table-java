public interface ICollisionResolver {
    int probe(int hashIndex, int i, int tableSize, int secondaryHash);
}

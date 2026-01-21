public class DoubleHashing implements ICollisionResolver{

    @Override
    public int probe(int hashIndex, int i, int tableSize, int secondaryHash){
        long sum = (long) hashIndex + (long) i * (long) secondaryHash;
        return Math.floorMod(sum, tableSize);
    }

    public int secondaryHash(String key, int tableSize){
        int q = getPrimeSmallerThan(tableSize);
        long h = 0;
        int z = 33;
        for (char c : key.toLowerCase().toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                h = (h * z + (c - 'a' + 1));
            }
        }
        int keyHash = (int) Math.floorMod(h, tableSize);
        int d = q - (keyHash % q);
        if (d == 0) d = 1;
        return d;
    }
    private int getPrimeSmallerThan(int n){
        for (int i = n - 1; i >= 2; i--){
            if (isPrime(i)) return i;
        }
        return 3;
    }
    private boolean isPrime(int num){
        if (num <= 1) return false;
        for (int i = 2; i * i <= num; i++){
            if (num % i == 0) return false;
        }
        return true;
    }
}

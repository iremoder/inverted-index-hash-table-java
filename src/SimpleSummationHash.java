public class SimpleSummationHash implements IHashFunction {
    @Override
    public int hash(String key, int tableSize) {
        int sum = 0;
        int n = key.length();
        for (int k = 0; k < n; k++) {
            sum += key.charAt(k);
        }
        return Math.abs(sum % tableSize);
    }
}

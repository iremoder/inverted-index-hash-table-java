public class PolynomialHash implements IHashFunction {
    @Override
    public int hash(String key, int tableSize) {
        int z = 33;
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            char c = Character.toLowerCase(key.charAt(i));
            int charVal = c - 'a' + 1;
            if (charVal < 1 || charVal > 26) {
            }
            h = (z * h + charVal) % tableSize;
        }
        if (h < 0) h += tableSize;
        return h;
    }
}
public class SDES {
    private String Key1;
    private String Key2;

    private int p10[];
    private int p8[];
    private int p4[];
    private int initialPermutation[];
    private int inverseInitialPermutation[];
    private int expansionPermutation[];
    private String S1[][];
    private String S0[][];

    private String p10Permutation(String Key) {
        String permutedKey = "";
        for (int i = 0; i<Key.length(); i++ ) {
            permutedKey += Key.charAt(p10[i] - 1);
        }
        return permutedKey;
    }

    private String p8Permutation(String Key) {
        String permutedKey = "";
        for (int i = 2; i<Key.length(); i++) {
            permutedKey += Key.charAt(p8[i - 2] - 1);
        }
        return permutedKey;
    }

    private String p4Permutation(String Text) {
        String permutedText = "";
        for(int i=0; i<4; i++) {
            permutedText += Text.charAt(p4[i] - 1);
        }
        return permutedText;
    }

    private String InitialPermutation(String text) {
        String permutedText = "";
        for(int i = 0; i<text.length(); i++) {
            permutedText += text.charAt(initialPermutation[i] - 1);
        }
        return permutedText;
    }

    private String InverseInitialPermutation(String text) {
        String permutedText = "";
        for(int i = 0; i<text.length(); i++) {
            permutedText += text.charAt(inverseInitialPermutation[i] - 1);
        }
        return permutedText;
    }

    private String ExpansionPermutation(String text) {
        String permutedText = "";
        for(int i = 0; i<8; i++) {
            permutedText += text.charAt(expansionPermutation[i] - 1);
        }
        return permutedText;
    }

    private String leftRotate(String Key, int bits) {
        String resultantKey = Key.substring(bits) + Key.substring(0, bits);
        return resultantKey;
    }

    private void generateKeys(String Key) {
        String key = p10Permutation(Key);

        String keyPartOne = leftRotate(key.substring(0, key.length()/2), 1);
        String keyPartTwo = leftRotate(key.substring(key.length()/2), 1);
        key = keyPartOne.concat(keyPartTwo);
        Key1 = p8Permutation(key);

        keyPartOne = leftRotate(key.substring(0, key.length()/2), 2);
        keyPartTwo = leftRotate(key.substring(key.length()/2), 2);
        key = keyPartOne.concat(keyPartTwo);
        Key2 = p8Permutation(key);
    }

    public SDES(String Key) {
        p10 = new int[]{3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
        p8 = new int[]{6, 3, 7, 4, 8, 5, 10, 9};
        p4 = new int[]{2, 4, 3, 1};
        initialPermutation = new int[]{2, 6, 3, 1, 4, 8, 5, 7};
        inverseInitialPermutation = new int[]{4, 1, 3, 5, 7, 2, 8, 6};
        expansionPermutation = new int[]{4, 1, 2, 3, 2, 3, 4, 1};
        S0 = new String[][]{{"01", "00", "11", "10"},
                            {"11", "10", "01", "00"},
                                {"00", "10", "01", "11"},
                                    {"11", "01", "11", "10"}};
        S1 = new String[][]{{"00", "01", "10", "11"},
                            {"10", "00", "01", "11"},
                                {"11", "00", "01", "00"},
                                    {"10", "01", "00", "11"}};
        generateKeys(Key);
    }

    private String FK(String Text, String Key) {
        String leftPlainText = Text.substring(0, Text.length()/2);
        String rightPlainText = Text.substring(Text.length()/2);

        String cipherRightText = ExpansionPermutation(rightPlainText);

        String cipherXORRightText = "";
        for(int i=0; i<cipherRightText.length(); i++) {
            cipherXORRightText += Integer.toString(cipherRightText.charAt(i) ^ Key.charAt(i));
        }

        String S0RowValue = cipherXORRightText.substring(0, 1) + cipherXORRightText.substring(3, 4);
        String S0ColumnValue = cipherXORRightText.substring(1, 2) + cipherXORRightText.substring(2, 3);
        String S1RowValue = cipherXORRightText.substring(4, 5) + cipherXORRightText.substring(7, 8);
        String S1ColumnValue = cipherXORRightText.substring(5, 6) + cipherXORRightText.substring(6, 7);

        String SBoxValue = "";
        SBoxValue += S0[Integer.parseInt(S0RowValue, 2)][Integer.parseInt(S0ColumnValue, 2)];
        SBoxValue += S1[Integer.parseInt(S1RowValue, 2)][Integer.parseInt(S1ColumnValue, 2)];
        String resultantString = p4Permutation(SBoxValue);

        String finalXOR = "";
        for(int i=0; i<leftPlainText.length(); i++) {
            finalXOR += Integer.toString(leftPlainText.charAt(i) ^ resultantString.charAt(i));
        }

        return finalXOR + rightPlainText;
    }

    public String encrypt(String plainText) {
        String Text = InitialPermutation(plainText);
        Text = FK(Text, this.Key1);
        String swapText = Text.substring(Text.length()/2) + Text.substring(0, Text.length()/2);
        Text = FK(swapText, this.Key2);
        return InverseInitialPermutation(Text);
    }

    public String decrypt(String plainText) {
        String Text = InitialPermutation(plainText);
        Text = FK(Text, this.Key2);
        String swapText = Text.substring(Text.length()/2) + Text.substring(0, Text.length()/2);
        Text = FK(swapText, this.Key1);
        return InverseInitialPermutation(Text);
    }

    public String getKey1() {
        return Key1;
    }

    public String getKey2() {
        return Key2;
    }
}

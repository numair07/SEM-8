import java.util.Scanner;

public class main {
    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter 10 Bit Key : "); String Key = scanner.next();

        if(Key.length()!=10) {
            System.out.println("Invalid Key");
            return;
        }

        SDES simplifiedDES = new SDES(Key);
        System.out.println("Keys Generated :");
        System.out.println("Key 1 : " + simplifiedDES.getKey1());
        System.out.println("Key 2 : " + simplifiedDES.getKey2());

        System.out.print("Enter 8 Bit Plain Text : "); String Text = scanner.next();
        if(Text.length()!=8) {
            System.out.println("Invalid Text");
            return;
        }
        System.out.println("Encrypted Text : " + simplifiedDES.encrypt(Text));
    }
}

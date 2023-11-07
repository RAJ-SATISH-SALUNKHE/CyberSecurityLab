import java.util.*;

class DHKE {
    long p = 23;
    long alpha = 5;
    Random random = new Random();

    public long mod(long exp) {
        return exp % p;
    }

    public void keyExcg() {
        long alicePrivKey = random.nextInt((int) (p - 2)) + 2;
        long bobPrivKey = random.nextInt((int) (p - 2)) + 2;

        System.out.println("Alice pvt: " + alicePrivKey);
        System.out.println("Bob pvt: " + bobPrivKey);

        long alicePubkey = mod((long) Math.pow(alpha, alicePrivKey));
        long bobPubkey = mod((long) Math.pow(alpha, bobPrivKey));

        long sharedAlice = mod((long) Math.pow(bobPubkey, alicePrivKey));
        long sharedBob = mod((long) Math.pow(alicePubkey, bobPrivKey));

        System.out.println("Alice shared key is: " + sharedAlice);
        System.out.println("Bob shared key is: " + sharedBob);
    }
}

public class SDHKE {
    public static void main(String[] args) {
        DHKE dhke = new DHKE();
        dhke.keyExcg();
    }
}

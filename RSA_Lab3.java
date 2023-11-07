class RSA
{
    int p = 3;
    int q = 11;
    int phi, pubkey, privkey, n;
    int plain_text = 6;
    int cipher_text;
    long decrypted_text;

    public int calcInv(int e, int phi_n)
    {

        for(int i = 0; i < phi_n; i++)
        {
            if((e * i) % phi_n == 1)
            {
                return i;
            }
        }

        return 1;
    }

    public int calcPubKey(int phi_n)
    {
        for (int e = 2; e < phi_n; e++)
        {
            if (gcd(e, phi_n) == 1)
            {
                return e;
            }
        }
        return -1;
    }

    public int gcd(int a, int b)
    {
        if (b == 0)
        {
            return a;
        }
        return gcd(b, a % b);

    }

    public void Ecrypt()
    {
        System.out.println("The plain text is : " + plain_text);
        n = p * q;
        System.out.println("The n is : " + n);
        phi = (p-1) * (q-1);
        System.out.println("The phi is : " + phi);
        pubkey = calcPubKey(phi);
        System.out.println("The public key is : " + pubkey);
        privkey = calcInv(pubkey, phi);
        System.out.println("The private key is : " + privkey);

        cipher_text = (int)(Math.pow(plain_text, pubkey)) % n;

        System.out.println("The cipher text is : " + cipher_text);


    }

    public void Decrypt()
    {
        decrypted_text = (long)(Math.pow(cipher_text, privkey)) % n;
        System.out.println("The decrypted_text is : " + decrypted_text);

    }
}




public class RSA_Lab3
{
    public static void main(String[] args) {

        RSA rsa = new RSA();

        rsa.Ecrypt();
        rsa.Decrypt();
    }
}

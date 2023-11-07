import java.util.*;

//Status: Both Encryption and Decryption done successfully

class DES
{
    public static String key = "0010010111";
    public static String plainText = "10100101";

    int[] p10 = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
    int[] p8 =  { 6, 3, 7, 4, 8, 5, 10, 9 };
    int[] p4 =  { 2, 4, 3, 1 };
    int[] ip =  { 2, 6, 3, 1, 4, 8, 5, 7 };
    int[] ep =  { 4, 1, 2, 3, 2, 3, 4, 1 };

    int[] ip_inv = { 4, 1, 3, 5, 7, 2, 8, 6 };
    int[][] s0 = { { 1, 0, 3, 2 },
            { 3, 2, 1, 0 },
            { 0, 2, 1, 3 },
            { 3, 1, 3, 2 } };

    int[][] s1 = { { 0, 1, 2, 3 },
            { 2, 0, 1, 3 },
            { 3, 0, 1, 0 },
            { 2, 1, 0, 3 } };

    // int key1 = new int[8];
    // int key2 = new int[8];
    String key1, key2;

    String XOR(String s1, String s2)
    {
        String s = "";
        for(int i = 0; i < s1.length(); i++)
        {
            if(s1.charAt(i) == s2.charAt(i))
            {
                s = s + '0';
            }
            else{
                s = s + '1';
            }
        }

        return s;
    }

    String LS(String str, int n)
    {
        StringBuilder s = new StringBuilder(str);
        while(n > 0)
        {
            char first = s.charAt(0);
            s.deleteCharAt(0);
            s.append(first);
            n--;
        }


        return s.toString();
    }

    String P10(String str)
    {
        String s = "";
        for(int i = 0; i < 10; i++)
        {
            s = s + str.charAt(p10[i] - 1);
        }

        return s;
    }

    String P8(String str)
    {
        String s = "";
        for(int i = 0; i < 8; i++)
        {
            s = s + str.charAt(p8[i] - 1);
        }

        return s;
    }

    String P4(String str)
    {
        String s = "";
        for(int i = 0; i < 4; i++)
        {
            s = s + str.charAt(p4[i] - 1);
        }

        return s;
    }

    String IP(String str, int inv)
    {
        String s = "";
        if(inv == 0)
        {
            for(int i = 0; i < 8; i++)
            {
                s = s + str.charAt(ip[i] - 1);
            }
        }
        else if(inv == -1)
        {
            for(int i = 0; i < 8; i++)
            {
                s = s + str.charAt(ip_inv[i] - 1);
            }
        }


        return s;
    }

    String EP(String str)
    {
        String s = "";
        for(int i = 0; i < 8; i++)
        {
            s = s + str.charAt(ep[i] - 1);
        }

        return s;
    }

    void generateKeys()
    {
        key = P10(key);
        String left = LS(key.substring(0, key.length() / 2), 1);
        String right = LS(key.substring(key.length() / 2), 1);


        key1 = P8(left + right);

        String left2 = LS(left, 2);
        String right2 = LS(right, 2);

        key2 = P8(left2 + right2);


        System.out.println("key1 is : "+ key1);
        System.out.println("key2 is : "+ key2);
    }

    String bin(int val)
    {
        if (val == 0)
        {
            return "00";
        }
        else if (val == 1)
        {
            return "01";
        }
        else if (val == 2)
        {
            return "10";
        }
        else
        {
            return "11";
        }
    }



    String sBox(String s)
    {
        int row, column, val;
        String left = s.substring(0, s.length()/2);
        String right = s.substring(s.length()/2);

        // row = Integer.parseInt("" + left.charAt(0)) - 48 + left.charAt(3) - 48, 2);
        // column = Integer.parseInt("" + left.charAt(1)) - 48 + left.charAt(2) - 48, 2);
        row = (left.charAt(0) - '0') * 2 + (left.charAt(3) - '0');
        column = (left.charAt(1) - '0') * 2 + (left.charAt(2) - '0');


        val = s0[row][column];
        String l = bin(val);

        // row = Integer.parseInt("" + right.charAt(0)) - 48 + right.charAt(3) - 48, 2);
        // column = Integer.parseInt("" + right.charAt(1)) - 48 + right.charAt(2) - 48, 2);
        row = (right.charAt(0) - '0') * 2 + (right.charAt(3) - '0');
        column = (right.charAt(1) - '0') * 2 + (right.charAt(2) - '0');

        val = s1[row][column];
        String r = bin(val);

        return l + r;


    }



    String encrypt()
    {
        plainText = IP(plainText, 0);
        String left1 = plainText.substring(0, plainText.length()/2);
        String right1 = plainText.substring(plainText.length()/2);

        String right1_copy = EP(right1);

        right1_copy = XOR(right1_copy, key1);

        right1_copy = P4(sBox(right1_copy));

        right1_copy = XOR(right1_copy, left1);


        String swapped = right1 + right1_copy;



        String left2 = swapped.substring(0, swapped.length()/2);
        String right2 = swapped.substring(swapped.length()/2);

        String right2_copy = EP(right2);
        right2_copy = XOR(right2_copy, key2);
        right2_copy = P4(sBox(right2_copy));
        right2_copy = XOR(right2_copy, left2);
        right2_copy = right2_copy + right2;

        String cipher_text = IP(right2_copy, -1);
        System.out.println("The cipher text is : " + cipher_text);




        return cipher_text;


    }

    String decrypt(String cipher)
    {
        cipher = IP(cipher, 0);
        String left1 = cipher.substring(0, cipher.length()/2);
        String right1 = cipher.substring(cipher.length()/2);

        String right1_copy = EP(right1);

        right1_copy = XOR(right1_copy, key2);

        right1_copy = P4(sBox(right1_copy));

        right1_copy = XOR(right1_copy, left1);


        String swapped = right1 + right1_copy;



        String left2 = swapped.substring(0, swapped.length()/2);
        String right2 = swapped.substring(swapped.length()/2);

        String right2_copy = EP(right2);
        right2_copy = XOR(right2_copy, key1);
        right2_copy = P4(sBox(right2_copy));
        right2_copy = XOR(right2_copy, left2);
        right2_copy = right2_copy + right2;

        String decrypted = IP(right2_copy, -1);

        System.out.println("The decrypted text is : " + decrypted);


        return decrypted;
    }


}



public class Main
{
    public static void main(String[] args) {
        System.out.println("The plaintext is : " + DES.plainText);
        System.out.println("The key is : " + DES.key);
        DES des = new DES();
        des.generateKeys();
        String cipher = des.encrypt();
        String decrypted = des.decrypt(cipher);


    }
}

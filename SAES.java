import java.util.*;

class AES
{
    boolean debugKey = false;
    boolean debugArk = false;

    String key1, key2, key3, ark0;
    String p = "1101011100101000";
    private String cipher_text;


    HashMap<String, String> sBox = new HashMap<>();
    HashMap<String, String> invSBox = new HashMap<>();

    public AES()
    {
        sBox.put("0000", "1001");
        sBox.put("0001", "0100");
        sBox.put("0010", "1010");
        sBox.put("0011", "1011");
        sBox.put("0100", "1101");
        sBox.put("0101", "0001");
        sBox.put("0110", "1000");
        sBox.put("0111", "0101");
        sBox.put("1000", "0110");
        sBox.put("1001", "0010");
        sBox.put("1010", "0000");
        sBox.put("1011", "0011");
        sBox.put("1100", "1100");
        sBox.put("1101", "1110");
        sBox.put("1110", "1111");
        sBox.put("1111", "0111");

        for (Map.Entry<String, String> entry : sBox.entrySet()) {
            invSBox.put(entry.getValue(), entry.getKey());
        }
    }

    public String binaryMultiply(String binaryString1, String binaryString2)
    {
        int result = Integer.parseInt(binaryString1, 2) * Integer.parseInt(binaryString2, 2);
        return String.format("%04d", Integer.parseInt(Integer.toBinaryString(result)));
    }

    String XOR(String s1, String s2)
    {
        StringBuilder s = new StringBuilder("");
        for(int i = 0; i < 4; i++)
        {
            if(s1.charAt(i) == s2.charAt(i))
            {
                s.append("0");
            }
            else
            {
                s.append("1");
            }
        }

        // System.out.println("The xored strinf is : " + s);

        return s.toString();
    }

    public static String reduceBinary(String binaryString)
    {
        String polynomial = "10011";
        if (binaryString.length() <= polynomial.length())
        {
            return binaryString;
        }

        StringBuilder reduced = new StringBuilder(binaryString);

        while (reduced.length() >= polynomial.length()) {
            if (reduced.charAt(0) == '1') reduced = xorBinary(reduced, polynomial);

            while (reduced.length() > 0 && reduced.charAt(0) == '0') {
                reduced.deleteCharAt(0);
            }

        }

        while (reduced.length() < 4) {
            reduced.insert(0, '0');
        }

        return reduced.toString();
    }

    public static StringBuilder xorBinary(StringBuilder binaryString, String polynomial)
    {
        StringBuilder result = new StringBuilder(binaryString);

        for (int i = 0; i < polynomial.length(); i++) {
            char binaryBit = binaryString.charAt(i);
            char polynomialBit = polynomial.charAt(i);
            if (binaryBit == polynomialBit) {
                result.setCharAt(i, '0');
            } else {
                result.setCharAt(i, '1');
            }
        }

        return result;
    }

    void Encrypt(String pkey)
    {
        generateKeys(pkey);
        StringBuilder ark = new StringBuilder("");
        for(int i = 0; i < p.length(); i++)
        {
            ark.append(p.charAt(i)^key1.charAt(i));
        }
        ark.replace(0, 4, sBox.get(ark.substring(0, 4)));
        ark.replace(4, 8, sBox.get(ark.substring(4, 8)));
        ark.replace(8, 12, sBox.get(ark.substring(8, 12)));
        ark.replace(12, 16, sBox.get(ark.substring(12, 16)));

        String temp = ark.substring(4,8);
        ark.replace(4, 8, ark.substring(12, 16));
        ark.replace(12, 16, temp);

        String[][] pString = {
                {ark.substring(0, 4), ark.substring(8, 12)},
                {ark.substring(4, 8), ark.substring(12, 16)}
        };



        String[][] mixColumnMatrix = {
                {"0001", "0100"},
                {"0100", "0001"}
        };

        p = new String("");



        String frfc = XOR(reduceBinary(binaryMultiply(mixColumnMatrix[0][0], pString[0][0])), reduceBinary(binaryMultiply(mixColumnMatrix[0][1], pString[1][0])));
        String srfc = XOR(reduceBinary(binaryMultiply(mixColumnMatrix[1][0], pString[0][0])), reduceBinary(binaryMultiply(mixColumnMatrix[1][1], pString[1][0])));
        String frsc = XOR(reduceBinary(binaryMultiply(mixColumnMatrix[0][0], pString[0][1])), reduceBinary(binaryMultiply(mixColumnMatrix[0][1], pString[1][1])));
        String srsc = XOR(reduceBinary(binaryMultiply(mixColumnMatrix[1][0], pString[0][1])), reduceBinary(binaryMultiply(mixColumnMatrix[1][1], pString[1][1])));



        p = frfc + srfc + frsc + srsc;

        if(debugArk)System.out.println("ARK0 p is : " + p);


        // ARK2

        ark = new StringBuilder("");
        for(int i = 0; i < p.length(); i++)
        {
            ark.append(p.charAt(i)^key2.charAt(i));
        }
        ark.replace(0, 4, sBox.get(ark.substring(0, 4)));
        ark.replace(4, 8, sBox.get(ark.substring(4, 8)));
        ark.replace(8, 12, sBox.get(ark.substring(8, 12)));
        ark.replace(12, 16, sBox.get(ark.substring(12, 16)));

        String temp2 = ark.substring(4,8);
        ark.replace(4, 8, ark.substring(12, 16));
        ark.replace(12, 16, temp2);


        p = ark.toString();
        if(debugArk)System.out.println("ARK2 p is : " + p);

        ark = new StringBuilder("");
        for(int i = 0; i < p.length(); i++)
        {
            ark.append(p.charAt(i)^key3.charAt(i));
        }

        p = ark.toString();
        cipher_text = p;

        System.out.println("The 16-bit cipher text is : " + p);

    }

    void Decrypt()
    {
        String p = cipher_text;
        StringBuilder ark = new StringBuilder("");
        for(int i = 0; i < p.length(); i++)
        {
            ark.append(p.charAt(i)^key3.charAt(i));

        }



        String temp = ark.substring(4,8);
        ark.replace(4, 8, ark.substring(12, 16));
        ark.replace(12, 16, temp);


        ark.replace(0, 4, invSBox.get(ark.substring(0, 4)));
        ark.replace(4, 8, invSBox.get(ark.substring(4, 8)));
        ark.replace(8, 12, invSBox.get(ark.substring(8, 12)));
        ark.replace(12, 16, invSBox.get(ark.substring(12, 16)));



        System.out.println("ARK3 IS : " + ark);
        System.out.println("Key2 IS : " + key2);


        p = ark.toString();
        ark = new StringBuilder("");
        for(int i = 0; i < p.length(); i++)
        {
            ark.append(p.charAt(i)^key2.charAt(i));

        }

        System.out.println("ARK2 is : "+ ark);

        String[][] mixColumnMatrix = {
                {"1001", "0010"},
                {"0010", "1001"}
        };


        String[][] pString = {
                {ark.substring(0, 4), ark.substring(8, 12)},
                {ark.substring(4, 8), ark.substring(12, 16)}
        };

        for (int i = 0; i < pString.length; i++)
        {
            for (int j = 0; j < pString.length; j++)
            {
                String element = pString[i][j];
                System.out.println("Matrix[" + i + "][" + j + "] = " + element);
            }
        }


        p = new String("");




        String frfc = XOR(reduceBinary(binaryMultiply(mixColumnMatrix[0][0], pString[0][0])), reduceBinary(binaryMultiply(mixColumnMatrix[0][1], pString[1][0])));
        String srfc = XOR(reduceBinary(binaryMultiply(mixColumnMatrix[1][0], pString[0][0])), reduceBinary(binaryMultiply(mixColumnMatrix[1][1], pString[1][0])));
        String frsc = XOR(reduceBinary(binaryMultiply(mixColumnMatrix[0][0], pString[0][1])), reduceBinary(binaryMultiply(mixColumnMatrix[0][1], pString[1][1])));
        String srsc = XOR(reduceBinary(binaryMultiply(mixColumnMatrix[1][0], pString[0][1])), reduceBinary(binaryMultiply(mixColumnMatrix[1][1], pString[1][1])));

        // System.out.println("raj is : " + frfc);

        p = frfc + srfc + frsc + srsc;

        // p = new String("0010111011101110");

        System.out.println("After matrix : " + p);

        ark = new StringBuilder(p);

        temp = ark.substring(4,8);
        ark.replace(4, 8, ark.substring(12, 16));
        ark.replace(12, 16, temp);


        ark.replace(0, 4, invSBox.get(ark.substring(0, 4)));
        ark.replace(4, 8, invSBox.get(ark.substring(4, 8)));
        ark.replace(8, 12, invSBox.get(ark.substring(8, 12)));
        ark.replace(12, 16, invSBox.get(ark.substring(12, 16)));

        System.out.println("OLT : " + ark);
        System.out.println("ky1 : " + key1);
        p = ark.toString();

        ark = new StringBuilder("");
        for(int i = 0; i < p.length(); i++)
        {
            ark.append(p.charAt(i)^key1.charAt(i));

        }

        System.out.println("MAIN TEXT: " + ark);









        // System.out.println("The p in decrypt is : " + p);





    }



    public String SubRot(String s)
    {

        String temp1 = ""; String temp2 = "";
        for(int i = 0; i < s.length()/2; i++)
        {
            temp2 += s.charAt(i);
        }
        for(int i = s.length()/2; i < s.length(); i++)
        {
            temp1 += s.charAt(i);
        }

        s = sBox.get(temp1) + sBox.get(temp2);


        return s;
    }

    public void generateKeys(String pkey)
    {
        StringBuilder w0 = new StringBuilder(pkey.substring(0, pkey.length() / 2));
        StringBuilder w1 = new StringBuilder(pkey.substring(pkey.length() / 2));
        StringBuilder w2 = new StringBuilder("10000000");
        StringBuilder w3 = new StringBuilder("");
        StringBuilder w4 = new StringBuilder("00110000");
        StringBuilder w5 = new StringBuilder("");
        StringBuilder temp = new StringBuilder(SubRot(w1.toString()));

        char xored = '0';


        for (int i = 0; i < w0.length(); i++)
        {
            xored = (char) (w0.charAt(i) ^ w2.charAt(i) ^ temp.charAt(i));
            w2.setCharAt(i, xored);
            w3.append((w2.charAt(i) ^ w1.charAt(i)));

        }

        temp = new StringBuilder(SubRot(w3.toString()));

        for (int i = 0; i < w0.length(); i++)
        {
            xored = (char) (w2.charAt(i) ^ w4.charAt(i) ^ temp.charAt(i));
            w4.setCharAt(i, xored);
            w5.append((w4.charAt(i) ^ w3.charAt(i)));
        }

        key1 = w0.toString() + w1.toString();
        key2 = w2.toString() + w3.toString();
        key3 = w4.toString() + w5.toString();


        if(debugKey)
        {
            System.out.println("W0: " + w0);
            System.out.println("W1: " + w1);
            System.out.println("W2: " + w2);
            System.out.println("W3: " + w3);
            System.out.println("W4: " + w4);
            System.out.println("W5: " + w5);
            System.out.println("key1: " + key1);
            System.out.println("key2: " + key2);
            System.out.println("key3: " + key3);
        }

    }
}


public class SAES
{
    public static void main(String[] args) {
        System.out.println("S - AES Algorithm");
        String pkey = "0100101011110101";
        AES s = new AES();
        s.Encrypt(pkey);
        s.Decrypt();


        //PLAIN TEXT IS INITIALISED INSIDE THE AES CLASS.
    }
}

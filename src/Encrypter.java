/** CpSc 1181    Lab XII
 * Both the letters from the key and from the plain text 
 * are translated to upper case.
 * Based on an assignment I gave in CPSC 1150.
 * @author Gladys Monagan
 * @version March 31, 2017
 */

import java.util.Scanner;

public class Encrypter implements SecretKey
{
   /**
   * the alphabet has 26 letters, all upper case and the 27th
   * corresponds to everything else (including blanks)
   */ 
   public static final int NUM_LETTERS = 27;
     
   // it's easier to work with an array of characters I think
   private static final char[] KEY = KEY_STRING.toCharArray();
   
   /**
   * Program to test this class.
   * @param args line arguments - not used
   */ 
   public static void  main(String[] args) 
   {
      // use the console   
      Scanner scanner = new Scanner(System.in);

      // give the user a choice of encrypting or decrypting
      System.out.print("Enter 'true' for encoding 'false' for decoding: ");
      boolean encoding = scanner.nextBoolean();
      
      System.out.print("Enter the text : ");
      // consume end of line so that the message can have blanks
      scanner.nextLine();
      
      String message = scanner.nextLine();
      
      if (encoding) 
      {
         System.out.println("plain text is " + message);
         System.out.println("cipher text is " + encrypt(message));
      }
      else {
         // i.e. decoding
         System.out.println("encrypted text is " + message);
         System.out.println("deciphered text is " + decrypt(message));
      }
   }

   /** 
   * Encrypts a message using a (secret) key with the Vigenere Code. 
   * The message is first translated into upper case letters.
   *
   * @param plainText  has the plain text
   * @return the encrypted message
   */
   public static String encrypt(String plainText)
   {   
      int m = KEY.length;
      char[] cipher = plainText.toUpperCase().toCharArray();
      for (int i=0; i < cipher.length; i++)
      {
         cipher[i] = letter( (code(KEY[i%m]) + code(cipher[i]) ) % NUM_LETTERS);
      }
      return new String(cipher);
   }

   /** 
   * Decrypts a message using a (secret) key with the Vigenere Code.  
   * It only deciphers into upper case letters.
   * 
   * @param cypherText the encrypted text
   * @return the decrypted (plain) text
   */     
   public static String decrypt(String cypherText) {
      int m = KEY.length;
      char[] plain = cypherText.toUpperCase().toCharArray();
      for (int i=0; i< plain.length; i++)
      {
         plain[i] = letter( (code(plain[i]) + 
                             NUM_LETTERS - 
                            code(KEY[i%m]) )  % NUM_LETTERS);
      }
      return new String(plain);
   }

  /** 
   * Returns the code of a characcter passed. If the character corresponds to
   * a letter,  the lower case letter is translated to upper case and then the codes
   * are returned as 
   *   'A' is given code 0
   *   'B' is given code 1
   *       ...
   *   'Z' is given code25
   * and everything else (including blanks) is given code NUM_LETTERS-1
   *
   * @param ch character whose code we return
   * @return a code in [0 .. NUM_LETTERS-1]
    */
   public static int code(char ch) 
   {
      ch = Character.toUpperCase(ch);
      if (('A' <= ch) && (ch <='Z'))
      {
         return (int)ch - (int)'A';
      }
      else  // everything else
      {
         return NUM_LETTERS - 1;
      }
   }

   /** 
   * Returns the letter of the code passed. The inverse method of code
   * Given 
   *   the code 0, is given the letter 'A'  
   *   the code 1, is given the letter  'B'
   *    ...
   *   the code 25, is given the letter 'Z' 
   *   the code NUM_LETTERS-1 is given ' '
   * 
   *  @param x is in [0 .. NUM_LETTER-1], this is a precondition 
   *  @return an upper case letter
   */
   public static char letter(int x) {
      if (x == NUM_LETTERS - 1)
      {
         return ' ';
      }
      else
      {
         return (char)(x + (int)'A');
      }
   }

   /**
   * Get the inverted key.
   * You can decipher by using the inverted key and the
   * ciphered text but still use the encrypt function
   * invertKey is like negating the key
   *
   * @param key has characters assigned up to key.length
   */
   public static char[] getInvertedKey(char[] key) 
   {
      int m = key.length;
      char[] invKey = new char[m];
      for (int i=0; i<m; i++)
      {
         invKey[i] = letter(NUM_LETTERS - code(key[i]));
      }
      return invKey;
   }
} 

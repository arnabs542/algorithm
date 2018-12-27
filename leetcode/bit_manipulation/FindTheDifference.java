/**
389. Find the Difference
Given two strings s and t which consist of only lowercase letters.

String t is generated by random shuffling string s and then add one more letter at a random position.

Find the letter that was added in t.

Example:

Input:
s = "abcd"
t = "abcde"

Output:
e

Explanation:
'e' is the letter that was added.
*/

/**
Solution 1: Using Ascii as HashMap count.
How to Arrive:
* Count the S chars in Ascii array.
* Decrement Ascii array counts for each T chars.
* If there is negative count, Then that char is not in S. return this Char.
* Time: O(n)
* Space: O(1)

-----------

Solution 2: Using XOR to cancel out same chars.
Ex: "abcd" ^ "abecd";
a^a=0, b^b=0, c^c=0, d^d=0, 'e' leftover;
return 'e';

How to Arrive:
* XOR will get the binary diff between 2 nums/chars. 
* If (x ^ x) = 0; No Diff;
* If (x ^ 0) = x; Diff between 0 and x is x.
* So if chars vals (binary vals) are the same, then XOR will cancel it out, and the leftover is the diff char;
* "abcd" ^ "abcd"  = 0;  "abcd" ^ "abced" = 'e';
* Time: O(n)
* Space: O(1)
*/

class FindTheDifference {

	/**
	 * Solution 2: Using XOR to cancel out same chars.
	 * Time: O(n)
	 * Space: O(1)
	 */
	public char findTheDifference(String s, String t) {
    // (x ^ 0 = x);
    char c = 0;
    for (int i = 0; i < s.length(); ++i) {
      // XOR will cancel out same vals, (x ^ x = 0); 
      // "abcd" ^ "abecd"; a^a=0, b^b=0, c^c=0, d^d=0, 'e' leftover;
      c ^= s.charAt(i);
    }
    for (int i = 0; i < t.length(); ++i) {
      c ^= t.charAt(i);
    }
    return c;
  }

	/**
	 * Solution 1: Using Ascii as HashMap count.
	 * Time: O(n)
	 * Space: O(1)
	 */
  public char findTheDifference(String s, String t) {
    int[] lettersCount = new int[26];
    for (char c : s.toCharArray()) {
      lettersCount[c - 'a']++;
    }
    for (char c : t.toCharArray()) {
      lettersCount[c - 'a']--;
      if (lettersCount[c - 'a'] < 0) {
        return c;
      }
    }
    return 'a';
  }
  
}
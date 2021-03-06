/**
Guess Number

http://www.1point3acres.com/bbs/forum.php?mod=viewthread&tid=189016
猜数字
猜一个4位的数字,每位上数字从1到6.

提供一个API,输入一个你猜的数字,返回有几位是猜对的。
比如要猜的数字是1234,猜的数字是1111,则返回1
猜的数字是1212,则返回2
要求写个程序多次调用这个API以后,返回猜的数字结果是什么.

1111,2222,3333,4444,5555
5 次找出 4 个数字 6666 不用找,因为 guess(6666) = 4 - ( guess(1111)+...+guess(5555) )
然后试

a1a1a1a1, a1a2a2a2, a1a3a3a3 同理 a1a4a4a4 不用试
目的是找出后三个数的数字组合 (不是排列)
找出后 3 个数字 就确定了第一个数字,而且这一轮 a1a1a1a1 不用调 api,第一轮里有
第二轮 2 次: 固定第一个数字,找到后 3 个数字组合

同理第三轮 1 次:固定第二个数字,找到后 2 个数字组合
sorry 第四轮还有 1 次,后两个数字的排列测一个 剩下的就是

不过思考一下这一轮可能不需要.
更正一下:目前能想到最好 5+2+1 = 8 次


http://www.1point3acres.com/bbs/forum.php?mod=viewthread&tid=290126
要猜出一四位数字。
例子:
# correct code: 3264
# GUESS 1111 => 0 0 (no correct digits
# GUESS 1214 => 2 0 (digits 2 and 4 are correct and on correct position)
# GUESS 6111 => 0 1 (digit 6 is present, but on a different position)
# GUESS 6211 => 1 1 (digit 2 is not counted towards the second count!)
写的时候要连到一个 server 上。
# START\n
# GUESS 1111\n =>0 0
# etc......
没看到过面经,几经提示答出来了。隔天说过了。
这个和 bull and cow 不一样。例子是 server 的回复。你的目标是要根据这个回复来猜出原数字是
什么。你可以设计要怎么猜。
感觉更好的方法是初始 candidates = {0, 1, 2, 3, 4,..., 9} (用 priorityqueue 可能比较方便写),
第一轮当然遍历 0000, 1111, 2222... 并且几下每组对应的返回值(a, b), 然后 cnt = a + b,同时

candidates 删除(a = 0, b = 0)的元素,priorityqueue 以 cnt 从小到大排序。
猜的话初始 XXXX 表示四个位置全部 available,可以用 candidates 外的元素作为初始值,以 cnt 最
大的开始猜,比如 cnt = 2, 就猜 available 位置放 cnt 最大 digit 的元素,遍历下去,AAXX, AXAX,
AXXA...这样下去 avaialble 的位置就减少到 4 - cnt, 依次进行,但是每次只在 available 的位置遍历,
减小搜索空间,方法适用于 n 长的 String.
*/

/**
3234

Res: 3234
candidates: 1,2,3,4,5,6
1111 -> (0,0);
2222 -> (1,0); a 2 digit in correctPos
3333 -> (2,0); two 3 digits in correctPos
4444 -> (1,0); a 4 in correctPos.
eliminates 1,5,6;

candidates: 2(1), 3(2), 4(1);
0222 -> (1,0);
0333 -> (1,0); 3 has 2counts - 1 = 1; found 1st digit = 3; 3(2)->3(1)

candidates: 2(1), 3(1), 4(1);
0022 -> (0,0); 2(1) -> 0; found 2nd digit; 2(1)->2(0);

candidates: 3(1), 4(1);
0003 -> (0,0); 3(1)-> 0; found 3rd; 3(1)->0;

candidates: 4(1); leftover = 1 = (1) count of 4; 4th digit DONE
*/

import java.util.*;
import java.io.*;

public class GuessNumber {

	String target;

  public GuessNumber(String target) {
      this.target = target;
  }

  private int guessServer(String guess) {
    int res = 0;
    int[] tarArr = new int[256];
    for (char c : target.toCharArray()) {
    	tarArr[c]++;
    }
    for (char c : guess.toCharArray()) {
    	if (tarArr[c] > 0) {
    		res++;
    		tarArr[c]--;
    	}
    }
    return res;
  }

  private String genNumber(List<Integer> guessed, int c) {
    StringBuilder sb = new StringBuilder();
    // how many 0s to fill before cand.
    for (int i = 0; i < guessed.size(); i++) {
    	sb.append(guessed.get(i));
    }
    // 1111, 2222... 0111, 0222.. 0011
    for (int i = guessed.size(); i < 4; i++) {
    	sb.append(c);
    } 
    return sb.toString();
  }

	private String IntsToString(List<Integer> guessed) {
	  if (guessed == null || guessed.size() == 0) return "";
	  StringBuilder sb = new StringBuilder();
	  for (int i = 0; i < guessed.size(); i++) {
	  	sb.append(guessed.get(i));
	  }
	  return sb.toString();
	}
	
	/**
	 * Solution: 
	 */
	public String guessNumber() {
    List<Integer> res = new ArrayList<>();
    List<Integer> cands = new ArrayList<>();
    for (int i = 1; i < 6; i++) {
      cands.add(i);
    }
    
    Iterator<Integer> iter = cands.iterator();
    
    while (iter.hasNext() && res.size() < 4) {
      int cand = iter.next();
      int filled = res.size();
      String guess = genNumber(res, cand);
      int corrects = guessServer(guess);
      
      if (corrects == filled) {
        iter.remove();
      } else if (corrects > filled) {
        for (int i = filled; i < corrects; i++) {
          res.add(cand);
        }
        iter.remove();
      }
    }
    
    while (res.size() < 4) {
      res.add(6);
    }
    
    return IntsToString(res);
  }

  /**
   * Follow Up: return (correctPos, correctNotPos);
    * Time: O(4 * 4);
   3234

Res: 3234
candidates: 1,2,3,4,5,6
1111 -> (0,0);
2222 -> (1,0); a 2 digit in correctPos
3333 -> (2,0); two 3 digits in correctPos
4444 -> (1,0); a 4 in correctPos.
eliminates 1,5,6;

candidates: 2(1), 3(2), 4(1);
0222 -> (1,0);
0333 -> (1,0); 3 has 2counts - 1 = 1; found 1st digit = 3; 3(2)->3(1)

candidates: 2(1), 3(1), 4(1);
0022 -> (0,0); 2(1) -> 0; found 2nd digit; 2(1)->2(0);

candidates: 3(1), 4(1);
0003 -> (0,0); 3(1)-> 0; found 3rd; 3(1)->0;

candidates: 4(1); leftover = 1 = (1) count of 4; 4th digit DONE
   */
  public String guessNumber2() {
    List<Integer> res = new ArrayList<>();
    // 1->6; index0 not counted. Count their appearances.
    int[] candCount = new int[7];
    Arrays.fill(candCount, -1);
    int leftover = 4;
    // cur pos to fill, 1->4; 0 means first iter eliminates round. could be 4 left.
    int pos = 0;

    while (leftover > 0 && pos <= 4) {
      // founded candidates
      int founded = 0;
      for (int cand = 1; cand <= 6; cand++) {
        // skip, eliminated cands
        if (candCount[cand] == 0) {
          continue;
        }
        // if founded cands == leftover, means rest can be eliminated
        if (founded == leftover) {
          candCount[cand] = 0;
          continue;
        }
        // generate masks guess, 2222, 3333,
        String guess = filledNumber(cand, pos);
        int[] corrects = guessServer2(guess);
        // check its pos corrects == leftover, found whole number.
        if (corrects[0] == leftover) {
          while (leftover > 0) {
            res.add(cand);
            candCount[cand]--;
            leftover--;
            pos++;
          }
          break;
        } else if (candCount[cand] > corrects[0]) {
          // curPos is this cand.
          res.add(cand);
          leftover--;
          candCount[cand] = corrects[0];
          founded += candCount[cand];
          // found the pos, move to next pos.
          break;
        }
        // update count
        candCount[cand] = corrects[0];
        founded += candCount[cand];
      }
      pos++;
    }

    return IntsToString(res);
  }

  private int[] guessServer2(String guess) {
    // [0]= correct pos; [1] corrects - correctPos;
    int[] res = new int[2];
    // counts
    int[] tarArr = new int[256];
    for (char c : target.toCharArray()) {
      tarArr[c]++;
    }
    
    for (int i = 0; i < guess.length(); i++) {
      // if target have that number
      if (tarArr[guess.charAt(i)] > 0) {
        res[1]++;
        tarArr[guess.charAt(i)]--;
      }
      // if matches position
      if (guess.charAt(i) == target.charAt(i)) {
        res[0]++;
        res[1]--;
      }
    }
    return res;
  }

  private String filledNumber(int digit, int pos) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < pos; i++) {
      sb.append(0);
    }
    for (int i = pos; i < 4; i++) {
      sb.append(digit);
    }
    return sb.toString();
  }

	
	public static void main(String[] args) {
		GuessNumber obj = new GuessNumber("2111");
		String res = obj.guessNumber();
		System.out.println("res1 " + res);

    String res2 = obj.guessNumber2();
    System.out.println("res2: " + res2);
	}

}
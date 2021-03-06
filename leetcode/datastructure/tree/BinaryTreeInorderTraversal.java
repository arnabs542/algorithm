/**
94. Binary Tree Inorder Traversal
Given a binary tree, return the inorder traversal of its nodes' values.

Example:

Input: [1,null,2,3]
   1
    \
     2
    /
   3

Output: [1,3,2]
Follow up: Recursive solution is trivial, could you do it iteratively?
*/

/**
Solution 1: iterative, helper class of ope: visit or print.
* When print, just print. When visit, becomes 3 operations.
* [1,2,3,4,5] => [4,2,5,1,3] left, root, right
* How to arrive the answer:
*
* Realize there are 2 posible operations: print or visit;
* When visit, it becomes 3 calls -> visit left, print self, visit right;
* PROBLEM: what data structure to use that will allows me to print 4 first, despite 3 being in the data first.
* KEY: use a Stack. LIFO. When I visit node 2, the stack is [v2->push(v4->[..], p2, v5), p1, v3];
* Ex: I can push 4,5 to be pop first, despite node 3 being there first.
*
* Time: O(N), visit nodes once.
* Space: O(N);
*/

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class BinaryTreeInorderTraversal {
	
	/**
   * Solution 1: iterative, helper class of ope: visit or print.
   * Time: O(n)
   * Space: O(lgN) average, O(n) in worst.
   */
  public List<Integer> inorderTraversal(TreeNode root) {
    // reslist of left, root, right
    List<Integer> res = new ArrayList<>();
    // iterative with stack.
    Stack<Guide> stack = new Stack<>();
    // add root to traverse inorder. left, root, right
    stack.push(new Guide(root, 0));
    while (!stack.isEmpty()) {
      Guide guide = stack.pop();
      // check null
      if (guide.node == null) {
        continue;
      }
      // check traverse or print
      if (guide.ope == 1) {
        // print/add
        res.add(guide.node.val);
      } else {
        // traverse/visit, left, root, right. Stack LIFO.
        stack.push(new Guide(guide.node.right, 0));
        stack.push(new Guide(guide.node, 1));
        stack.push(new Guide(guide.node.left, 0));
      }
    }
    // return reslist
    return res;
  }
  
  class Guide {
    TreeNode node;
    int ope; // 0 traverse, 1 print/add
    
    Guide(TreeNode node, int ope) {
      this.node = node;
      this.ope = ope;
    }
  }
  

   /**
   * Solution 2: recursive
   * Time: O(n)
   * Space: O(lgN) average, O(n) worst
   */
  List<Integer> res = new ArrayList<>();
  
  public List<Integer> inorderTraversal(TreeNode root) {
    inorder(root);
    return res;
  }
  
  public void inorder(TreeNode root) {
    if (root == null) {
      return;
    }
    // left, root, right
    inorder(root.left);
    res.add(root.val);
    inorder(root.right);
  }
  
}
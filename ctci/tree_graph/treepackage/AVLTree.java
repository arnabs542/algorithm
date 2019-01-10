/**
AVL tree is a self-balancing Binary Search Tree (BST) where the difference between heights of left and right subtrees cannot be more than one for all nodes.
*/

// package treepackage;
import java.io.*;
import java.util.*;

public class AVLTree {

	class Node {
		int val;
		int height;
		Node left;
		Node right;

		Node(int val) {
			this.val = val;
			this.height = 1;
			left = right = null;
		}
	}

	Node root = null;

	public int height(Node node) {
		if (node == null) {
			return 0;
		}
		return node.height;
	}

	// 2/1 is leftH > rightH by 2/1, -2/-1 is rightH > leftH by 2/1.
	public int balanceFactor(Node node) {
		// check the height diff of left and right subtree
		if (node == null) {
			return 0;
		}
		return height(node.left) - height(node.right);
	}

	/**
	 * Time: O(1)
	 */
	public Node leftRotate(Node target) {
		// target is the root to rotate.
		// target's right node to become root.
		Node rightNode = target.right;
		// rightNode's left node to connect as target.right.
		Node cutOffNode = rightNode.left;
		// Rotate left.
		// rightNode's left becomes target, as rightNode becomes root.
		rightNode.left = target;
		// target's right node becomes rightNode's left node.
		target.right = cutOffNode;

		// update heights, target update first b/c it is a child of rightNode now.
		target.height = 1 + Math.max(height(target.left), height(target.right));
		rightNode.height = 1 + Math.max(height(rightNode.left), height(rightNode.right));
		// return new root.
		return rightNode;
	}

	/**
	 * Time: O(1)
	 */
	public Node rightRotate(Node target) {
		// target is the root to rotate.
		// target's leftNode becomes new root.
		Node leftNode = target.left;
		// cut off leftNode's right node to connect to target's left.
		Node cutOffNode = leftNode.right;
		// Rotate right
		// leftNode connect target as right.
		leftNode.right = target;
		target.left = cutOffNode;

		// update heights for switched target and leftNode, other children Nodes remain the same height.
		target.height = 1 + Math.max(height(target.left), height(target.right));
		leftNode.height = 1 + Math.max(height(leftNode.left), height(leftNode.right));
		// return new root.
		return leftNode;
	}

	/**
	 * Time: O(lgN), find the node go down the branch. Height of the tree.
	 * B/c the tree is always rebalanced, the insertion takes O(lgN) or O(h) always;
	 */
	public Node insert(Node rootNode, int val) {
		// find where the val lies
		if (rootNode == null) {
			return new Node(val);
		}
		if (rootNode.val > val) {
			// on left;
			rootNode.left = insert(rootNode.left, val);
		} else if (rootNode.val < val) {
			rootNode.right = insert(rootNode.right, val);
		} else {
			// same val, duplicates not allow in this example.
			return rootNode;
		}
		// update height
		rootNode.height = 1 + Math.max(height(rootNode.left), height(rootNode.right));

		// check balanced.
		int diff = balanceFactor(rootNode);
		// 4 cases:
		// Diff >= 2 indicate left > right by 2. So left side is too long
		// Left Left, inserted val < root && val < root.left. 
		if (diff >= 2 && val < rootNode.left.val) {
			// rotate right once on root to get balance
			return rightRotate(rootNode);
		}
		// Left Right, val < root && val > root.left
		if (diff >= 2 && val > rootNode.left.val) {
			// rotate left on root.left to become Left Left first.
			// newRoot is returned, connect to rootNode
			rootNode.left = leftRotate(rootNode.left);
			// then right rotate on root to become balanced
			return rightRotate(rootNode);
		}
		// Diff <= -2 indicates right > left by 2. So right side is too long.
		// Right Right, val > root && val > root.right
		if (diff <= -2 && val > rootNode.right.val) {
			// rotate left once on root to get balance.
			return leftRotate(rootNode);
		}
		// Right Left, val > root && val < root.right
		if (diff <= -2 && val < rootNode.right.val) {
			// rotate right to become Right Right, connect root to newRight
			rootNode.right = rightRotate(rootNode.right);
			// then left rotate to balance
			return leftRotate(rootNode);
		}
		return rootNode;
	}

	public Node minNode(Node rootNode) {
		if (rootNode == null) {
			return null;
		}
		// min is the left most node
		while (rootNode.left != null) {
			rootNode = rootNode.left;
		}
		return rootNode;
	}

	public Node delete(Node rootNode, int val) {
		// Do regular BST delete first
		// nothing to delete
		if (rootNode == null) {
			return null;
		}
		// search node
		if (rootNode.val > val) {
			// on left
			rootNode.left = delete(rootNode.left, val);
		} else if (rootNode.val < val) {
			// on right
			rootNode.right = delete(rootNode.right, val);
		} else {
			System.out.println(rootNode.val);
			// if rootNode is == val, delete this node.
			// 3 Cases
			// Case1: rootNode is leaf, then no child need to connect
			if (rootNode.left == null && rootNode.right == null) {
				rootNode = null;
			} else if (rootNode.left != null || rootNode.right != null) {
				// Case2: rootNode has only 1 child left or right, return that child as new root.
				if (rootNode.left == null) {
					rootNode = rootNode.right;
				} else if (rootNode.right == null) {
					rootNode = rootNode.left;
				} else {
					// Case3: both left and right have nodes
					// search the smallest node on right.
					Node minNode = minNode(rootNode.right);
					// replace root with minNode's val
					rootNode.val = minNode.val;
					System.out.println(minNode.val);
					// delete minNode.
					rootNode.right = delete(rootNode.right, minNode.val);
				}
			} 
		}
		// finished regular BST deletion.
		// only 1 node, then return
		if (rootNode == null) {
			return null;
		}
		// update the height of rootNode
		rootNode.height = 1 + Math.max(height(rootNode.left), height(rootNode.right));
		// Check balance factor
		int diff = balanceFactor(rootNode);
		// 4 cases
		// Left heavy diff >= 2
		// Left Left, deleted val on right and leftside's leftBranch is heavier/highest.
		// or leftside's left and right ==
		if (diff >= 2 && balanceFactor(rootNode.left) >= 0) {
			// rotate right the entire tree to balance
			return rightRotate(rootNode);
		}
		// Left Right, deleted val on right and leftside's rightBranch is heavier/highest
		if (diff >= 2 && balanceFactor(rootNode.left) <= -1) {
			// rotate left on root.left to form Left Left shape
			rootNode.left = leftRotate(rootNode.left);
			// then rotate right the entire tree to balance
			return rightRotate(rootNode);
		}
		// Right side heavy, <= -2
		// Right Right, deleted val on left and rightside's rightBranch is heavier/highest
		// or rightside's left and right ==
		if (diff <= -2 && balanceFactor(rootNode.right) <= 0) {
			// rotate left on entire tree to balance
			return leftRotate(rootNode);
		}
		// Right Left, deleted val on left and rightside's leftBranch is heavier/highest
		if (diff <= -2 && balanceFactor(rootNode.right) >= 1) {
			// rotate right on root.right to form Right Right
			rootNode.right = rightRotate(rootNode.right);
			// then rotate left on entire tree to balance
			return leftRotate(rootNode);
		}
		// return delete/updated tree
		return rootNode;
	}


	// print preorder
	public void preOrder(Node node) { 
    if (node == null) { 
    	return;
    }
    System.out.print(node.val + " "); 
    preOrder(node.left);
    preOrder(node.right); 
  } 

  public static void main(String[] args) { 
    AVLTree tree = new AVLTree(); 

    /* Constructing tree given in the above figure */
    tree.root = tree.insert(tree.root, 10); 
    tree.root = tree.insert(tree.root, 20); 
    tree.root = tree.insert(tree.root, 30); 
    tree.root = tree.insert(tree.root, 40); 
    tree.root = tree.insert(tree.root, 50); 
    // tree.root = tree.insert(tree.root, 25);
    // tree.root = tree.insert(tree.root, 5);
    // tree.root = tree.insert(tree.root, 23);
    tree.root = tree.insert(tree.root, 35);


    /* The constructed AVL Tree would be 
         30 
        /  \ 
      20   40 
     /  \     \ 
    10  25    50 
    /		/
   (5) (23)
    */
    System.out.println("Preorder traversal of constructed tree is : "); 
    tree.preOrder(tree.root); 
    System.out.println();

    System.out.println("delete Node 20 to: "); 
    tree.root = tree.delete(tree.root, 20);
    System.out.println("delete Node 10 to: "); 
    tree.root = tree.delete(tree.root, 10);
    tree.preOrder(tree.root); 
    System.out.println();
  }

}
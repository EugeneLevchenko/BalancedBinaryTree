package com.eugene_levchenko.redBlackTree;

import java.util.Iterator;

public class RedBlackTree<T extends Comparable<T>> implements Iterable<T> {

    int numberOfNodes = 0;

    Node findRemovingNode(T removingData) throws Exception {

        Node currentNode = root;
        while (true)
        {
            if (currentNode==null)
            {
                throw new Exception("No data found");
            }
            int resultOfCompare=removingData.compareTo((T) currentNode.data);

            switch (resultOfCompare) {
                //when >
                case 1:
                    currentNode=currentNode.rightNode;
                    break;
                //when <
                case -1:
                    currentNode=currentNode.leftNode;
                    break;
                //when ==
                case 0:
                    return currentNode;
            }
        }
    }

    Node findMinNode(Node nodeR) {
        Node res=nodeR;
        while (true)
        {
            if (res.leftNode==null)
            {
                return res;
            }
            res=res.leftNode;
        }
    }

    Node removeOneLeaf(Node removingNode)
    {
        Node parNode=removingNode.parentNode;
        if (removingNode.parentNode!=null)
        {
            //removingNode.parentNode=null;
            if (removingNode.parentNode.leftNode==removingNode)
            {
                removingNode.parentNode.leftNode=null;
            }
            else {
                removingNode.parentNode.rightNode=null;
            }
            removingNode.parentNode=null;
        }
        else {
            this.root=null;
        }
        return parNode;
    }

    void remove(T removeData) throws Exception {
        Node removingNode=findRemovingNode(removeData);
        //left
        Node left=removingNode.leftNode;
        //right
        Node right=removingNode.rightNode;
        Node nodeShouldbeBalanced=null;
        if (left==null && right==null)
        {
            nodeShouldbeBalanced=removeOneLeaf(removingNode);

        }
        else if (left!=null && right==null)
        {
            nodeShouldbeBalanced= removeWithOneChild(removingNode,false);

        }
        else if (left==null && right!=null)
        {
            nodeShouldbeBalanced= removeWithOneChild(removingNode,true);

        }
        //если правое поддерево непустое
        else {
            nodeShouldbeBalanced= removeNodeWith2Child(removeData);

        }
        recalcNodeHeight(nodeShouldbeBalanced);
        balance(nodeShouldbeBalanced);
    }

    Node removeNodeWith2Child(T removeData) throws Exception {
        Node removingNode=findRemovingNode(removeData);
        Node parentOfRemovingNode=removingNode.parentNode;
        Node left=removingNode.leftNode;
        Node right=removingNode.rightNode;
        Node minNode = findMinNode(right);
        Node parNodeOfMinNode=minNode.parentNode;
        Node subTreeMinNode=minNode.rightNode;

        parNodeOfMinNode.leftNode=null;
        minNode.parentNode=null;
        if (subTreeMinNode!=null)
        {
            parNodeOfMinNode.leftNode=subTreeMinNode;
            subTreeMinNode.parentNode=parNodeOfMinNode;
            minNode.rightNode=null;
        }
        minNode.leftNode=left;
        left.parentNode=minNode;
        if (minNode!=right) {
            minNode.rightNode = right;
            right.parentNode=minNode;
        }

        if (removingNode.parentNode==null) {
                this.root=minNode;
        }
        else{
            minNode.parentNode=parentOfRemovingNode;
            if (parentOfRemovingNode.leftNode==removingNode)
            {
                parentOfRemovingNode.leftNode=minNode;
            }
            else {
                parentOfRemovingNode.rightNode=minNode;
            }
        }
        return parNodeOfMinNode;
    }

    Node removeWithOneChild(Node removingNode, boolean isRightChild) {
        Node parNode=removingNode.parentNode;
        Node child = (isRightChild)?removingNode.rightNode:removingNode.leftNode;
        if (parNode!=null)
        {
            child.parentNode=parNode;

            if (parNode.rightNode==removingNode)
            {
                parNode.rightNode=child;
            }
            else
            {
                parNode.leftNode=child;
            }
            if (isRightChild)
            {
                removingNode.rightNode=null;
            }
            else
            {
                removingNode.leftNode=null;
            }
            removingNode.parentNode=null;
        }
        else
        {
            this.root=child;
        }
        return parNode;
    }

    public Node rightSmallRotate(Node p) throws Exception {
        if (p == null) {
            throw new Exception("p is null");
        }

        Node z = p.parentNode;
        Node q = p.leftNode;
        if (q == null) {
            throw new Exception("q is null");
        }
        Node b = q.rightNode;

        if (z != null) {
            if (z.leftNode == p) {
                z.leftNode = q;
            } else {
                z.rightNode = q;
            }
        }

        if (b != null) {
            p.leftNode = b;
            b.parentNode = p;
        } else {
            p.leftNode = null;
        }

        q.rightNode = p;
        p.parentNode = q;
        q.parentNode = z;

        if (p == root) {
            root = q;
        }

        return q;
    }

    public Node leftSmallRotate(Node q) throws Exception {
        if (q == null) {
            throw new Exception("q is null");
        }

        Node z = q.parentNode;
        Node p = q.rightNode;
        if (p == null) {
            throw new Exception("p is null");
        }
        Node b = p.leftNode;

        if (z != null) {
            if (z.leftNode == q) {
                z.leftNode = p;
            } else {
                z.rightNode = p;
            }
        }

        if (b != null) {
            q.rightNode = b;
            b.parentNode = q;
        } else {
            q.rightNode = null;
        }

        p.leftNode = q;
        p.parentNode = z;
        q.parentNode = p;

        if (q == root) {
            root = p;
        }
        return p;
    }

    void recalcNodeHeight(Node node) throws Exception {
        while (node != null) {
            int rightH = node.rightNode == null ? -1 : node.rightNode.height;
            int leftH = node.leftNode == null ? -1 : node.leftNode.height;
            node.height = ((rightH > leftH) ? rightH : leftH) + 1;
            node = node.parentNode;
        }
    }

    void balance(Node node) throws Exception {
        while (node != null) {
            boolean bigRotate = false;
            int rightH = node.rightNode == null ? -1 : node.rightNode.height;
            int leftH = node.leftNode == null ? -1 : node.leftNode.height;
            if ((leftH - rightH) > 1) {

                Node newNode = applyRightRotateBigOrSmall(node);

                recalcNodeHeight(node);
                node = newNode;
            } else if ((leftH - rightH) < -1) {

                Node newNode = applyLeftRotateBigOrSmall(node);
                recalcNodeHeight(node);
                node = newNode;
            }
            node = node.parentNode;
        }
    }

    private Node applyLeftRotateBigOrSmall(Node node) throws Exception {
        Node q = node.rightNode;
        Node s = q.leftNode;
        Node d = q.rightNode;
        if (s != null && d != null && s.height > d.height) {
            Node newNode = rightSmallRotate(q);
            recalcNodeHeight(q);
            return leftSmallRotate(node);
        }
        return leftSmallRotate(node);
    }

    private Node applyRightRotateBigOrSmall(Node node) throws Exception {
        Node q = node.leftNode;
        Node s = q.leftNode;
        Node d = q.rightNode;
        if (s != null && d != null && s.height < d.height) {
            Node newNode = leftSmallRotate(q);
            recalcNodeHeight(q);
            return rightSmallRotate(node);
        }
        return rightSmallRotate(node);
    }

    double log(double x) {
        return Math.ceil((Math.log(x + 1) / Math.log(2)) - 1);
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator(this);
    }

    private  void  internalPrint(Node n,String prefix)
    {
        prefix +="/"+n.data.toString()+"("+n.height+")"+ ( (n.leftNode==null && n.rightNode==null) ?" * ":"");
        System.out.println(prefix);
        if (n.leftNode!=null)
            internalPrint(n.leftNode,prefix +" L ");

        if (n.rightNode!=null)
            internalPrint(n.rightNode,prefix +" R ");
    }
    public void PrintToConsole() {
        /*MyIterator i = new MyIterator(this);
        while (i.hasNext()){
            i.currentNode
        }
        */
        if (this.root==null){return;}
        internalPrint(this.root,"");
    }

    public void printAllData() {
        for (T i:this ) {
            System.out.print(i +" ");}
        System.out.println("");System.out.println("");
    }

    class Node<T extends Comparable<T>> {
        Node rightNode;
        Node leftNode;
        Node parentNode;
        T data;
        int height = 0;
    }

    class MyIterator implements Iterator<T> {
        RedBlackTree<T> tree;
        Node currentNode;
        boolean restrictedLeft = false;
        boolean restrictedRight = false;

        public MyIterator(RedBlackTree<T> tree) {
            this.tree = tree;
        }

        @Override
        public boolean hasNext() {

            if (currentNode == null) {
                currentNode = tree.root;
                if (currentNode == null) {
                    return false;
                }
            }
            while (true) {
//left down
                if (currentNode.leftNode != null && !restrictedLeft) {
                    while (currentNode.leftNode != null) {
                        currentNode = currentNode.leftNode;
                    }
                    restrictedLeft = true;
                    return true;
                }
//right down
                if (currentNode.rightNode != null && !restrictedRight) {
                    restrictedLeft = false;
                    currentNode = currentNode.rightNode;
                    if (currentNode.leftNode == null) {
                        return true;

                    }
                    continue;
                }
// up
                if (currentNode.parentNode == null) {
                    return false;
                }
                boolean currentIsRightInHisParent = currentNode == currentNode.parentNode.rightNode;
                currentNode = currentNode.parentNode;
                restrictedLeft = true;
                restrictedRight = currentIsRightInHisParent;
                if (!currentIsRightInHisParent) {
                    return true;
                }
            }
        }

        @Override
        public T next() {
            return (T) currentNode.data;
        }

        @Override
        public void remove() {
        }
    }

    Node root;

    public T getOrAdd(T addedData) throws Exception {
        if (root == null) {
            root = new Node();
            root.data = addedData;
            numberOfNodes++;
            return (T) root.data;
        }
        Node currentNode;
        currentNode = root;
        while (true) {
            int resultOfCompare = currentNode.data.compareTo(addedData);
            if (resultOfCompare == 1) {
                if (currentNode.leftNode == null) {
                    Node node = new Node();
                    currentNode.leftNode = node;
                    node.parentNode = currentNode;
                    node.data = addedData;
                    numberOfNodes++;
                    recalcNodeHeight(currentNode);
                    balance(currentNode);
                    return (T) node.data;
                } else {
                    currentNode = currentNode.leftNode;
                    continue;
                }
            }
            if (resultOfCompare == -1) {
                if (currentNode.rightNode == null) {
                    Node node = new Node();
                    currentNode.rightNode = node;
                    node.parentNode = currentNode;
                    node.data = addedData;
                    numberOfNodes++;
                    recalcNodeHeight(currentNode);
                    balance(currentNode);
                    return (T) node.data;
                } else {
                    currentNode = currentNode.rightNode;
                    continue;
                }
            }
            numberOfNodes++;
            return (T) currentNode.data;
        }
    }
}
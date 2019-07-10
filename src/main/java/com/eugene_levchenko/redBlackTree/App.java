package com.eugene_levchenko.redBlackTree;

public class App
{
    public static void main( String[] args ) throws Exception {

        RedBlackTree< Integer> myTree = new RedBlackTree< Integer>();
        myTree.getOrAdd(1);
        myTree.getOrAdd(2);
        myTree.getOrAdd(3);
        myTree.getOrAdd(4);
        myTree.getOrAdd(5);
        myTree.getOrAdd(6);

        myTree.PrintToConsole();
    }
}


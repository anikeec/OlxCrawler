/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.structure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author apu
 * @param <T>
 */
public class DirectoryTree<T> {
    
    private DirectoryTreeNode<T> root;

    public DirectoryTree() {
        super();
    }

    public DirectoryTreeNode<T> getRoot() {
        return this.root;
    }

    public void setRoot(DirectoryTreeNode<T> root) {
        this.root = root;
    }

    public int getNumberOfNodes() {
        int numberOfNodes = 0;

        if(root != null) {
            numberOfNodes = auxiliaryGetNumberOfNodes(root) + 1; //1 for the root!
        }

        return numberOfNodes;
    }

    private int auxiliaryGetNumberOfNodes(DirectoryTreeNode<T> node) {
        int numberOfNodes = node.getNumberOfChildren();

        for(DirectoryTreeNode<T> child : node.getChildren()) {
            numberOfNodes += auxiliaryGetNumberOfNodes(child);
        }

        return numberOfNodes;
    }

    public boolean exists(T dataToFind) {
        return (find(dataToFind) != null);
    }

    public DirectoryTreeNode<T> find(T dataToFind) {
        DirectoryTreeNode<T> returnNode = null;

        if(root != null) {
            returnNode = auxiliaryFind(root, dataToFind);
        }

        return returnNode;
    }

    private DirectoryTreeNode<T> auxiliaryFind(DirectoryTreeNode<T> currentNode, T dataToFind) {
        DirectoryTreeNode<T> returnNode = null;
        int i;

        if (currentNode.getData().equals(dataToFind)) {
            returnNode = currentNode;
        }

        else if(currentNode.hasChildren()) {
            i = 0;
            while(returnNode == null && i < currentNode.getNumberOfChildren()) {
                returnNode = auxiliaryFind(currentNode.getChildAt(i), dataToFind);
                i++;
            }
        }

        return returnNode;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public List<DirectoryTreeNode<T>> build(DirectoryTreeOrderEnum traversalOrder) {
        List<DirectoryTreeNode<T>> returnList = null;

        if(root != null) {
            returnList = build(root, traversalOrder);
        }

        return returnList;
    }

    public List<DirectoryTreeNode<T>> build(DirectoryTreeNode<T> node, DirectoryTreeOrderEnum traversalOrder) {
        List<DirectoryTreeNode<T>> traversalResult = new ArrayList<>();

        if(traversalOrder == DirectoryTreeOrderEnum.PRE_ORDER) {
            buildPreOrder(node, traversalResult);
        }

        else if(traversalOrder == DirectoryTreeOrderEnum.POST_ORDER) {
            buildPostOrder(node, traversalResult);
        }

        return traversalResult;
    }

    private void buildPreOrder(DirectoryTreeNode<T> node, List<DirectoryTreeNode<T>> traversalResult) {
        traversalResult.add(node);

        for(DirectoryTreeNode<T> child : node.getChildren()) {
            buildPreOrder(child, traversalResult);
        }
    }

    private void buildPostOrder(DirectoryTreeNode<T> node, List<DirectoryTreeNode<T>> traversalResult) {
        for(DirectoryTreeNode<T> child : node.getChildren()) {
            buildPostOrder(child, traversalResult);
        }

        traversalResult.add(node);
    }

    public Map<DirectoryTreeNode<T>, Integer> buildWithDepth(DirectoryTreeOrderEnum traversalOrder) {
        Map<DirectoryTreeNode<T>, Integer> returnMap = null;

        if(root != null) {
            returnMap = buildWithDepth(root, traversalOrder);
        }

        return returnMap;
    }

    public Map<DirectoryTreeNode<T>, Integer> buildWithDepth(DirectoryTreeNode<T> node, DirectoryTreeOrderEnum traversalOrder) {
        Map<DirectoryTreeNode<T>, Integer> traversalResult = new LinkedHashMap<>();

        if(traversalOrder == DirectoryTreeOrderEnum.PRE_ORDER) {
            buildPreOrderWithDepth(node, traversalResult, 0);
        }

        else if(traversalOrder == DirectoryTreeOrderEnum.POST_ORDER) {
            buildPostOrderWithDepth(node, traversalResult, 0);
        }

        return traversalResult;
    }

    private void buildPreOrderWithDepth(DirectoryTreeNode<T> node, Map<DirectoryTreeNode<T>, Integer> traversalResult, int depth) {
        traversalResult.put(node, depth);

        for(DirectoryTreeNode<T> child : node.getChildren()) {
            buildPreOrderWithDepth(child, traversalResult, depth + 1);
        }
    }

    private void buildPostOrderWithDepth(DirectoryTreeNode<T> node, Map<DirectoryTreeNode<T>, Integer> traversalResult, int depth) {
        for(DirectoryTreeNode<T> child : node.getChildren()) {
            buildPostOrderWithDepth(child, traversalResult, depth + 1);
        }

        traversalResult.put(node, depth);
    }

    @Override
    public String toString() {
        /*
        We're going to assume a pre-order traversal by default
         */

        String stringRepresentation = "";

        if(root != null) {
            stringRepresentation = build(DirectoryTreeOrderEnum.PRE_ORDER).toString();

        }

        return stringRepresentation;
    }

    public String toStringWithDepth() {
        /*
        We're going to assume a pre-order traversal by default
         */

        String stringRepresentation = "";

        if(root != null) {
            stringRepresentation = buildWithDepth(DirectoryTreeOrderEnum.PRE_ORDER).toString();
        }

        return stringRepresentation;
    }
}

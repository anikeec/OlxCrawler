/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author apu
 * @param <T>
 */
public class DirectoryTreeNode<T> {
    
    private T data;
    private List<DirectoryTreeNode<T>> children;
    private DirectoryTreeNode<T> parent;

    public DirectoryTreeNode() {
        super();
        children = new ArrayList<>();
    }

    public DirectoryTreeNode(T data) {
        this();
        setData(data);
    }

    public DirectoryTreeNode<T> getParent() {
        return this.parent;
    }

    public List<DirectoryTreeNode<T>> getChildren() {
        return this.children;
    }

    public int getNumberOfChildren() {
        return getChildren().size();
    }

    public boolean hasChildren() {
        return (getNumberOfChildren() > 0);
    }

    public void setChildren(List<DirectoryTreeNode<T>> children) {
        for(DirectoryTreeNode<T> child : children) {
           child.parent = this;
        }

        this.children = children;
    }

    public void addChild(DirectoryTreeNode<T> child) {
        child.parent = this;
        children.add(child);
    }

    public void addChildAt(int index, DirectoryTreeNode<T> child) throws IndexOutOfBoundsException {
        child.parent = this;
        children.add(index, child);
    }

    public void removeChildren() {
        this.children = new ArrayList<>();
    }

    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    public DirectoryTreeNode<T> getChildAt(int index) throws IndexOutOfBoundsException {
        return children.get(index);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return getData().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
           return true;
        }
        if (obj == null) {
           return false;
        }
        if (getClass() != obj.getClass()) {
           return false;
        }
        DirectoryTreeNode<?> other = (DirectoryTreeNode<?>) obj;
        if (data == null) {
           if (other.data != null) {
              return false;
           }
        } else if (!data.equals(other.data)) {
           return false;
        }
        return true;
    }

    /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
    @Override
    public int hashCode() {
       final int prime = 31;
       int result = 1;
       result = prime * result + ((data == null) ? 0 : data.hashCode());
       return result;
    }
    
    public String getPathToRoot() {
        return getPathToParent() + "/";
    }
    
    public String getPathToParent() {
        if(this.parent != null)
            return parent.getPathToParent() + "/" + this.getData().toString();
        else
            return this.getData().toString();
    }

    public String toStringVerbose() {
        String stringRepresentation = getData().toString() + ":[";

        for (DirectoryTreeNode<T> node : getChildren()) {
            stringRepresentation += node.getData().toString() + ", ";
        }

        //Pattern.DOTALL causes ^ and $ to match. Otherwise it won't. It's retarded.
        Pattern pattern = Pattern.compile(", $", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(stringRepresentation);

        stringRepresentation = matcher.replaceFirst("");
        stringRepresentation += "]";

        return stringRepresentation;
    }
}

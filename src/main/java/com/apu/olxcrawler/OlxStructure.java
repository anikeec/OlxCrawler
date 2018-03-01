/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.structure.DirectoryTree;
import com.apu.olxcrawler.structure.DirectoryTreeNode;
import com.apu.olxcrawler.structure.DirectoryTreeOrderEnum;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class OlxStructure {
    private final DirectoryTree<String> directoryTree;
    private DirectoryTreeNode<String> rootDir;
    
    public OlxStructure() {
        this.directoryTree = new DirectoryTree<>();
        this.init();
    }
    
    private void init() {  
        DirectoryTreeNode<String> node1, node2;
        
        rootDir = new DirectoryTreeNode(OlxCategory.olx_root_dir);
        directoryTree.setRoot(rootDir);        
        
        node1 = new DirectoryTreeNode<>(OlxCategory.hobbi_otdyh_i_sport);
        
        node2 = new DirectoryTreeNode<>(OlxCategory.antikvariat_kollektsii);
        node1.addChild(node2);
        
        node2 = new DirectoryTreeNode<>(OlxCategory.knigi_zhurnaly);
        node1.addChild(node2);
        
        rootDir.addChild(node1);
    }
    
    private DirectoryTree<String> getTree() {
        return this.directoryTree;
    }
    
    public List<String> getCategoriesList() {
        List<DirectoryTreeNode<String>> nodeList = 
                    getTree().build(DirectoryTreeOrderEnum.PRE_ORDER);        
        List<String> linkList = new ArrayList<>();
        for(DirectoryTreeNode<String> node:nodeList) {
            if(node.hasChildren() == false) {
                linkList.add(node.getPathToRoot());
            }
        }        
        return linkList;
    }

}

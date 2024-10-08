package com.nicholastcc.datainventor.OCRMaven.webVersion;

import java.util.ArrayList;
import java.util.List;

class TreeNode {
    String url;
    int depth;
    List<TreeNode> children;

    TreeNode(String url, int depth) {
        this.url = url;
        this.depth = depth;
        this.children = new ArrayList<>();
    }
}

package com.kuhakupixel.onlinedecompiler;

import java.util.List;
import java.util.ArrayList;
public class ClassInfo {
    
    public String className;

    public ClassInfo(String className){
        this.className = className;
    }
    public List<String> methodStrings = new ArrayList<String>();
    
}

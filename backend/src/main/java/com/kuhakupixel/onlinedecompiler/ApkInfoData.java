
package com.kuhakupixel.onlinedecompiler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;


public class ApkInfoData {
    @Id
    public String apkHash;
    public List<ClassInfo> classInfos;

    public ApkInfoData(){

    }
    public ApkInfoData(String apkHash, List<ClassInfo> classInfos){
        this.apkHash = apkHash;
        this.classInfos = classInfos;

    }
    
}

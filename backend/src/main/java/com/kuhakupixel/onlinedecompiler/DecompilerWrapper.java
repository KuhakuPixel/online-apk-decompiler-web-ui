package com.kuhakupixel.onlinedecompiler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;
import jadx.api.JavaMethod;
import jadx.api.impl.NoOpCodeCache;
import jadx.api.impl.SimpleCodeWriter;
import jadx.cli.JadxCLI;
import jadx.plugins.tools.JadxExternalPluginsLoader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

public class DecompilerWrapper {

    public static List<ClassInfo> GetSource(Path apkPath, File zipSourceOut) throws IOException {
        List<ClassInfo> classInfos = new ArrayList<ClassInfo>();
        System.out.println("saving apk to: " + apkPath.toString());
        // ============================ start decompilation process

        Path tempDecompilationDir = Files.createTempDirectory("TempDecompiledApkDir");

        JadxArgs jadxArgs = new JadxArgs();
        jadxArgs.setCodeCache(new NoOpCodeCache());
        jadxArgs.setCodeWriterProvider(SimpleCodeWriter::new);
        jadxArgs.setPluginLoader(new JadxExternalPluginsLoader());
        jadxArgs.setInputFile(apkPath.toFile());
        jadxArgs.setOutDir(tempDecompilationDir.toFile());
        jadxArgs.setExportAsGradleProject(true);

        try (JadxDecompiler jadx = new JadxDecompiler(jadxArgs)) {
            jadx.load();
            // save all methods and their class
            for (JavaClass cls : jadx.getClasses()) {
                ClassInfo classInfo = new ClassInfo(cls.getFullName());
                for (JavaMethod method : cls.getMethods()) {
                    // System.out.println("method: " + );
                    classInfo.methodStrings.add(method.toString());
                }
                // add to our list
                classInfos.add(classInfo);
            }
            jadx.save();

            System.out.println("Saving to decompilation to " + tempDecompilationDir.toString());
        }
        // ========================================== saving decompilation result
        // =============

        ZipUtil.pack(tempDecompilationDir.toFile(), zipSourceOut);
        return classInfos;

    }
}

package com.kuhakupixel.onlinedecompiler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.impl.NoOpCodeCache;
import jadx.api.impl.SimpleCodeWriter;
import jadx.cli.JadxCLI;
import jadx.plugins.tools.JadxExternalPluginsLoader;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

public class DecompilerWrapper {

    public static void GetSource(Path apkPath, File zipSourceOut)throws IOException {

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
            jadx.save();

            System.out.println("Saving to decompilation to " + tempDecompilationDir.toString());
        }
        // ========================================== saving decompilation result
        // =============

        ZipUtil.pack(tempDecompilationDir.toFile(), zipSourceOut);

    }
}

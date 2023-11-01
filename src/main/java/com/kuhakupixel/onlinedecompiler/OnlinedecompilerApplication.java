package com.kuhakupixel.onlinedecompiler;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.impl.NoOpCodeCache;
import jadx.api.impl.SimpleCodeWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jadx.plugins.tools.JadxExternalPluginsLoader;
import org.zeroturnaround.zip.ZipUtil;


@SpringBootApplication
@RestController
public class OnlinedecompilerApplication {

	List<MyData> datas = new ArrayList<MyData>();

	public static void main(String[] args) {
		SpringApplication.run(OnlinedecompilerApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@PostMapping("/datas")
	MyData newData(@RequestBody MyData myData) {
		System.out.println("data name is : " + myData.name);
		datas.add(myData);
		return myData;

	}

	@GetMapping("/datas")
	List<MyData> getDatas() {

		return datas;

	}

	@GetMapping("/datas/{name}")
	MyData getData(@RequestParam(value = "name") String name) {
		for (MyData data : datas) {
			if (data.name.equals(name)) {
				return data;
			}
		}

		return null;
	}

	@PostMapping("/apk")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws IOException {

		System.out.println("File name: " + file.getOriginalFilename());
		Path tempDir = Files.createTempDirectory("TempApkDir");
		Path apkPath = Paths.get(tempDir.toString(), file.getOriginalFilename());
		// load to file
		file.transferTo(apkPath);
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
		}

		System.out.println("Saving to decompilation to " + tempDecompilationDir.toString());
		ZipUtil.pack(tempDecompilationDir.toFile(), new File("source.zip"));

		// ============================================================
		return "redirect:/";
	}
}

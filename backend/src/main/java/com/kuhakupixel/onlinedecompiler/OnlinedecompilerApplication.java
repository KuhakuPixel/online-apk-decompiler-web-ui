package com.kuhakupixel.onlinedecompiler;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.impl.NoOpCodeCache;
import jadx.api.impl.SimpleCodeWriter;
import jadx.cli.JadxCLI;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jadx.plugins.tools.JadxExternalPluginsLoader;

import org.apache.commons.io.FilenameUtils;

@SpringBootApplication
@RestController
public class OnlinedecompilerApplication {

	List<MyData> datas = new ArrayList<MyData>();
	File decompiledSourceBasePath = new File("decompiled_sources");
	// TODO: use database for persistence
	HashMap<String, List<ClassInfo>> apkMd5ToClassInfoArrayMap = new HashMap<String, List<ClassInfo>>();

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

	@CrossOrigin
	@GetMapping("/apk/info/{apkHash}")
	List<ClassInfo> getApkInfo(@PathVariable String apkHash) {
		System.out.println("getting apk info: " + apkHash);
		return apkMd5ToClassInfoArrayMap.get(apkHash);
	}


	@CrossOrigin
	@PostMapping("/apk")
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws IOException {

		System.out.println("File name: " + file.getOriginalFilename());
		// check if its an apk and not some kind of other file
		if (!FilenameUtils.getExtension(file.getOriginalFilename()).equals("apk")) {
			return ResponseEntity.badRequest().body(null);
		}
		// ============================================================
		// ========= make some temporary directory
		Path tempDir = Files.createTempDirectory("TempApkDir");
		Path apkPath = Paths.get(tempDir.toString(), file.getOriginalFilename());
		// ==================================================
		// load to file
		file.transferTo(apkPath);
		// calculate its hash and set it as an output
		String apkHash = Util.md5OfFile(apkPath.toFile());
		File zippedSourceOut = new File(decompiledSourceBasePath.toString(), apkHash + ".zip");

		// cache
		if (!zippedSourceOut.exists()) {
			List<ClassInfo> classInfos = DecompilerWrapper.GetSource(apkPath, zippedSourceOut);
			// save the information
			apkMd5ToClassInfoArrayMap.put(apkHash, classInfos);
		}

		// https://stackoverflow.com/questions/35680932/download-a-file-from-spring-boot-rest-service
		InputStreamResource resource = new InputStreamResource(new FileInputStream(zippedSourceOut));
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=source.zip");
		// abit of a hack but just send back the hash in the header, 
		// https://stackoverflow.com/questions/73499438/spring-boot-does-not-seem-to-expose-a-custom-header-to-my-frontend
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "apkHash");
		headers.set("apkHash", apkHash);
		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(zippedSourceOut.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}
}

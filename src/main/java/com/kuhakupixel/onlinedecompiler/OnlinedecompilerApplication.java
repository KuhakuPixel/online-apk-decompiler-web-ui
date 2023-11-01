package com.kuhakupixel.onlinedecompiler;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
			RedirectAttributes redirectAttributes) {

		System.out.println("File name: " + file.getName());
		return "redirect:/";
	}
}

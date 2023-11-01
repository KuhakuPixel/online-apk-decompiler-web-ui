plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "com.kuhakupixel"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	google()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//implementation("io.github.skylot:jadx-core:1.4.7")
	//runtimeOnly("io.github.skylot:jadx-dex-input:1.4.7")
	//implementation("io.github.skylot:jadx-plugins-tools:1.4.7")
	implementation(files("$projectDir/jarlib/jadx-dev-all.jar"))
	implementation("org.zeroturnaround:zt-zip:1.16")


	runtimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

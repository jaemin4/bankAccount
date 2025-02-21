plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.account"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("com.fasterxml.jackson.core:jackson-databind:2.16.0")
	implementation ("com.fasterxml.jackson.core:jackson-core:2.16.0")
	implementation ("com.fasterxml.jackson.core:jackson-annotations:2.16.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly ("com.mysql:mysql-connector-j")
	implementation ("org.springframework.boot:spring-boot-starter-amqp")




}

tasks.withType<Test> {
	useJUnitPlatform()
}

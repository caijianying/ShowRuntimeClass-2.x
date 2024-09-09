import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getCurrentVersion(): String {
    val df = SimpleDateFormat("yyyyMMdd", Locale.CHINA)
    return df.format(Calendar.getInstance().time)
}

extra.apply {
    set("plugin", mapOf(
        "agentTargetJarModule" to rootProject.name,
        "version" to getCurrentVersion()
    ))

    set("dependencies", mapOf(
        "guava" to "com.google.guava:guava:31.0.1-jre",
        "hutool-all" to "cn.hutool:hutool-all:5.8.5",
        "javax-servlet" to "javax.servlet:javax.servlet-api:4.0.1",
        "apache-dubbo" to "org.apache.dubbo:dubbo:2.7.0",
        "spring-web" to "org.springframework:spring-web:5.2.4.RELEASE",
        "slf4j" to "org.slf4j:slf4j-api:1.7.36",
        "lombok" to "org.projectlombok:lombok:1.18.34",
        "fastjson" to "com.alibaba:fastjson:1.2.79",
        "javassist" to "javassist:javassist:3.12.1.GA",
        "byte-buddy" to "net.bytebuddy:byte-buddy:1.12.6",
        "byte-buddy-agent" to "net.bytebuddy:byte-buddy-agent:1.12.6",
        "junit" to "junit:junit:4.12",
        "junit.jupiter.api" to "org.junit.jupiter:junit-jupiter-api:5.8.2",
        "junit.jupiter.engine" to "org.junit.jupiter:junit-jupiter-engine:5.8.2"
    ))
}
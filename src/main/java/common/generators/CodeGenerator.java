package common.generators;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Setter
@Slf4j
public class CodeGenerator {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = CodeGenerator.class.getClassLoader().getResourceAsStream("application.yml")) {
            // 读取 yml 配置
            Map<String, Object> configMap = yaml.load(inputStream);
            Map<String, Object> springMap = (Map<String, Object>) configMap.get("spring");
            Map<String, Object> datasourceMap = (Map<String, Object>) springMap.get("datasource");
            String url = (String) datasourceMap.get("url");
            String username = (String) datasourceMap.get("username");
            String password = (String) datasourceMap.get("password");
            String outputDir = System.getProperty("user.dir") + "\\generator";
            // 设置输出目录
            FastAutoGenerator.create(url, username, password)
                    .globalConfig((scanner,builder) -> {
                        builder.author(scanner.apply("请输入作者名: \n")) // 设置作者
                                .outputDir(outputDir); // 输出目录
                    })
                    .packageConfig((scanner,builder) -> {
                        builder.parent(scanner.apply("请设置父包名: \n")) // 设置父包名
                                .entity("model") // 设置实体类包名
                                .mapper("dao") // 设置 Mapper 接口包名
                                .service("service") // 设置 Service 接口包名
                                .serviceImpl("service.impl") // 设置 Service 实现类包名
                                .xml("mappers"); // 设置 Mapper XML 文件包名
                    })
                    .strategyConfig((scanner, builder) -> {
                        builder.addInclude(scanner.apply("请设置生成的表名，用英文逗号隔开: \n")) // 设置需要生成的表名
                                .entityBuilder()
                                .enableLombok() // 启用 Lombok
                                .enableTableFieldAnnotation() // 启用字段注解
                                .controllerBuilder()
                                .enableRestStyle(); // 启用 REST 风格
                    })
                    .templateEngine(new FreemarkerTemplateEngine()) // 使用 Freemarker 模板引擎
                    .execute();
        } catch (Exception e) {
            String errorMsg = String.format("mybatis 代码生成错误，原因是: %s", e.getMessage());
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }
}

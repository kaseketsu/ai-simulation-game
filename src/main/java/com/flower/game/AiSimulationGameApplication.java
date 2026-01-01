package com.flower.game;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.yaml.snakeyaml.Yaml;

@SpringBootApplication(scanBasePackages = {"com.flower.game", "common"})
@MapperScan("com.flower.game.**.dao")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AiSimulationGameApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiSimulationGameApplication.class, args);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> AGA 启动成功！<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

}

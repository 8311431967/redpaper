package scau.redpaper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author kunrong
 * @description
 * @date 2019/4/27 19:01
 */
@Configuration
@ImportResource({"classpath:/config/spring-mybatis.xml"})

public class MybatisConfig {
}

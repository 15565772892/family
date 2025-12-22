package org.family.core.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "org.family.core.dao")
public class MyBatisConfig {

    /**
     * 方式1：显式配置 SqlSessionFactory（可选）
     * 如果不配置，MyBatis Starter 会自动创建
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        // 1. 设置数据源
        sessionFactory.setDataSource(dataSource);

        // 2. 设置实体类别名包
        sessionFactory.setTypeAliasesPackage("org.family.core.dto");

        // 3. 配置 MyBatis 全局设置
        org.apache.ibatis.session.Configuration configuration =
                new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); // 驼峰命名转换
        configuration.setCacheEnabled(true);              // 开启缓存
        configuration.setUseGeneratedKeys(true);         // 使用生成的主键
        configuration.setDefaultStatementTimeout(3000);  // 超时时间

        sessionFactory.setConfiguration(configuration);

        // 4. 加载 XML 映射文件（可选）
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:mapper/**/*.xml");
        sessionFactory.setMapperLocations(resources);

        return sessionFactory.getObject();
    }
}
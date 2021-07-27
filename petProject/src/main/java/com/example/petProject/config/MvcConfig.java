package com.example.petProject.config;


import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleDate;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Properties;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@EnableWebMvc
@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("cabinet");
    registry.addViewController("/login").setViewName("login");
  }

  @Bean
  public ViewResolver viewResolver() {

    FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
    viewResolver.setCache(true);
    viewResolver.setSuffix(".ftl");
    viewResolver.setPrefix("/");

    return viewResolver;

  }


  @Bean
  public FreeMarkerConfigurer configurer() {
    FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
    configurer.setTemplateLoaderPath("classpath:\\templates\\");

    Properties settings = new Properties();
    settings.put("datetime_format", "yyyy-MM-dd HH:mm:ss");
    configurer.setFreemarkerSettings(settings);

    return configurer;
  }

  @Resource
  void configureFreemarkerConfigurer(FreeMarkerConfig configurer) {
    configurer.getConfiguration().setObjectWrapper(new CustomObjectWrapper());

  }

  private static class CustomObjectWrapper extends DefaultObjectWrapper {

    @Override

    public TemplateModel wrap(Object obj) throws TemplateModelException {
      if (obj instanceof LocalDateTime) {
        Timestamp timestamp = Timestamp.valueOf((LocalDateTime) obj);

        return new SimpleDate(timestamp);

      }

      if (obj instanceof LocalDate) {
        Date date = Date.valueOf((LocalDate) obj);

        return new SimpleDate(date);

      }

      if (obj instanceof LocalTime) {
        Time time = Time.valueOf((LocalTime) obj);

        return new SimpleDate(time);

      }

      return super.wrap(obj);

    }

  }
}




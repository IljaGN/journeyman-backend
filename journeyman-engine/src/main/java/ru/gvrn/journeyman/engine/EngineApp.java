package ru.gvrn.journeyman.engine;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class EngineApp {
  public static void main(String[] args) {
    System.out.println(EngineApp.class.getSimpleName() + "#main start");
    new AnnotationConfigApplicationContext(EngineApp.class);
    System.out.println(EngineApp.class.getSimpleName() + "#main finish");
  }
}

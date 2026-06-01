package futurenet.fullstack;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class App {

  public static void main(String[] args) {
    ApplicationContext context =
        new AnnotationConfigApplicationContext(Appconfig.class);

    System.out.println("Spring context loaded");
  }
}

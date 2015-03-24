package hello.test;

import java.io.InputStream;

import org.junit.Test;
import org.springframework.util.Assert;

public class MainTest {

  @Test
  public void test() {

    InputStream is = MainTest.class.getResourceAsStream("/test");

    Assert.notNull(is);
  }

}

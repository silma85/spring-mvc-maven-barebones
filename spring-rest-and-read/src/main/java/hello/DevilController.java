/**
 * 
 */
package hello;

import hello.exception.DevilException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for {@link Devil}
 * 
 * @author alessandro.putzu
 *
 */
@RestController
public class DevilController {

  private final static String template = "You summoned %s...";
  private final AtomicLong counter = new AtomicLong();

  private final static Logger log = LoggerFactory.getLogger(DevilController.class);

  @RequestMapping("/devil/{name}")
  public Devil devil(@PathVariable(value = "name") String name) {
    return new Devil(counter.incrementAndGet(), String.format(template, name));
  }

  @RequestMapping("/devil")
  public List<Devil> index() {

    log.info("Getting list of Devils...");

    Devil baal = new Devil(counter.incrementAndGet(), "Baal");
    Devil asmodeus = new Devil(counter.incrementAndGet(), "Asmodeus");
    Devil geryon = new Devil(counter.incrementAndGet(), "Geryon");
    Devil yeenoghu = new Devil(counter.incrementAndGet(), "Yeenoghu");
    Devil juiblex = new Devil(counter.incrementAndGet(), "Juiblex");
    Devil dispater = new Devil(counter.incrementAndGet(), "Dispater");

    List<Devil> devils = new ArrayList<Devil>();
    devils.add(baal);
    devils.add(asmodeus);
    devils.add(geryon);
    devils.add(yeenoghu);
    devils.add(juiblex);
    devils.add(dispater);

    return devils;
  }

  @RequestMapping("/file")
  public File file(@RequestParam(value = "name", defaultValue = "test") String name) throws IOException, DevilException {

    log.info("Getting file " + name + "...");

    InputStream is = DevilController.class.getResourceAsStream("/" + name);

    if (is == null) {
      throw new DevilException(String.format("File %s was not found.", name));
    }

    byte[] bytes = StreamUtils.copyToByteArray(is);
    byte[] encoded = Base64Utils.encode(bytes);

    return new File(counter.incrementAndGet(), encoded, name);
  }
}

/**
 * 
 */
package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 * This is controller.
 * 
 * @author alessandro.putzu
 *
 */
@Controller
public class ConsumeController {

  @RequestMapping("/call")
  public String call_service(final Model model) {

    RestTemplate restTemplate = new RestTemplate();
    ConsumedObject bo = restTemplate.getForObject("http://localhost:8085/spring-rest-and-read/devil/Asmodeus",
            ConsumedObject.class);

    model.addAttribute("bo", bo);

    return "result";

  }
}

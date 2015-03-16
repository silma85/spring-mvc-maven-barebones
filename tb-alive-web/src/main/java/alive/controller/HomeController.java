/**
 * 
 */
package alive.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import alive.exception.AliveException;
import alive.service.XMLService;

/**
 * Main controller.
 * 
 * @author alessandro.putzu
 *
 */
@Controller
public class HomeController {

  private static Logger log = LoggerFactory.getLogger(HomeController.class);

  @Autowired
  private MessageResources messages;

  @Autowired
  private XMLService service;

  @RequestMapping(value = "/")
  public String main(final Model model) {

    model.addAttribute("url", messages.getString("alive.socket.address"));
    model.addAttribute("port", messages.getString("alive.socket.port"));

    return "index";
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ AliveException.class })
  public ModelAndView error(final AliveException e) {

    log.error(e.getMessage());

    final ModelAndView model = new ModelAndView("error");
    model.addObject("message", e.getMessage());

    return model;
  }

  @RequestMapping(value = "/call/{url:.*}/{port}")
  public String send(final Model model, final @PathVariable("url") String url, final @PathVariable("port") int port,
          final HttpServletResponse response) throws AliveException {

    String a = messages.getString("alive.xml.packet");
    StringBuffer buffer = new StringBuffer();

    SocketAddress address = new InetSocketAddress(url, port);
    Socket socket = new Socket();

    try {

      socket.connect(address, 2500);

      // OutputStream os = socket.getOutputStream();

      // XMLOutputStream xo = service.parseAndTransform(a, os);
      // xo.send();
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

      byte[] bytes = a.getBytes();
      dos.writeInt(bytes.length);
      dos.write(bytes);

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String line = null;
      while ((line = in.readLine()) != null) {
        log.info("Received: {}", line);
        buffer.append(line);
      }

      socket.close();

      model.addAttribute("message", true);

    } catch (ConnectException e) {
      throw new AliveException("Socket " + messages.getString("alive.socket.address") + " did not respond.");
    } catch (SocketException e) {

      log.warn("Connection abort: recv failed");
      model.addAttribute("message", false);

      // } catch (ParserConfigurationException | SAXException |
      // TransformerFactoryConfigurationError | TransformerException e) {
      // throw new AliveException("Exception parsing the XML payload.");
    } catch (IOException e) {
      throw new AliveException("Generic IO Exception.");
    }

    model.addAttribute("url", messages.getString("alive.socket.address"));
    model.addAttribute("port", messages.getString("alive.socket.port"));

    return "index";
  }
}

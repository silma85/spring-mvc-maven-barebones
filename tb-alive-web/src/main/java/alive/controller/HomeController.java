/**
 * 
 */
package alive.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import alive.service.XMLService;
import alive.stream.XMLOutputStream;

/**
 * Main controller.
 * 
 * @author alessandro.putzu
 *
 */
@Controller
public class HomeController {

  @Autowired
  private MessageResources messages;

  @Autowired
  private XMLService service;

  @RequestMapping(value = "/")
  public String main(final Model model) {

    model.addAttribute("url", messages.getString("alive.address"));
    model.addAttribute("port", messages.getString("alive.port"));

    return "index";
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ IOException.class, SocketException.class })
  public ModelAndView error(final IOException ioe) {
    final ModelAndView model = new ModelAndView("error");
    model.addObject("message", "Socket " + messages.getString("alive.address") + " is dead.");

    return model;
  }

  @RequestMapping(value = "/call/{url:.*}/{port}")
  public String send(final Model model, final @PathVariable("url") String url, final @PathVariable("port") int port,
          final HttpServletResponse response) throws Exception {

    String a = messages.getString("alive.xml.packet");
    StringBuffer buffer = new StringBuffer();

    SocketAddress address = new InetSocketAddress(url, port);
    Socket socket = new Socket();

    try {

      socket.connect(address, 2000);
      OutputStream os = socket.getOutputStream();

      XMLOutputStream xo = service.parseAndTransform(a, os);
      xo.send();

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String line = null;
      while ((line = in.readLine()) != null) {
        System.out.println(line);
        buffer.append(line);
      }

      socket.close();

    } catch (Exception e) {
      throw e;
    }

    model.addAttribute("url", messages.getString("alive.address"));
    model.addAttribute("port", messages.getString("alive.port"));
    model.addAttribute("message", buffer.toString());

    return "index";
  }
}

/**
 * 
 */
package hello.exception;

/**
 * @author alessandro.putzu
 *
 */
public class RestConsumeException extends Exception {

  private static final long serialVersionUID = -6421747270040173264L;

  public RestConsumeException(String message) {
    super(message);
  }
}

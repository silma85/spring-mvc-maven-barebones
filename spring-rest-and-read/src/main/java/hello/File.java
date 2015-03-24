/**
 * 
 */
package hello;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * File resource
 * 
 * @author alessandro.putzu
 *
 */
@Getter
@AllArgsConstructor
public class File {

  private final long id;

  private final byte[] bytes;

  private final String name;

}

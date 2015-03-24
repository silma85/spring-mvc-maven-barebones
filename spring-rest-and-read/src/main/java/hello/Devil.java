/**
 * 
 */
package hello;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Plain representation of a REST object
 * 
 * @author alessandro.putzu
 *
 */
@Getter
@AllArgsConstructor
public class Devil {

  private final long id;

  private final String name;

}

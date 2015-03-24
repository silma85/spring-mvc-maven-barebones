package hello;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ConsumedObject {

  private String href;

  private String name;

  private String description;

}

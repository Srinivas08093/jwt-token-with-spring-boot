package jwt.pratice.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:awt-configuration.properties")
public class JwtPropertyCOnfigReader {
  @Autowired
  Environment env;

  public String getFilePropertyValue(String key, String defaultVal) {
    String propVal = null;
    propVal = env.getProperty(key);
    if (StringUtils.isBlank(propVal) && StringUtils.isNotBlank(defaultVal)) {
      propVal = defaultVal;
    }
    return propVal;
  }

  public String getFilePropertyValue(String key) {
    return getFilePropertyValue(key, "");
  }

}

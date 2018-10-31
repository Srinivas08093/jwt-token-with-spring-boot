package jwt.pratice.utility;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jwt.pratice.common.SpringBeanLocator;

public class JwtTokenUtility {
  private static final Logger logger = Logger.getLogger(JwtTokenUtility.class);

  public static String getUserFromToken(String authTokenId) {
    logger.debug("Validating the authTokenId ");
    JwtConfiguration jwtSettings = (JwtConfiguration) SpringBeanLocator.getInstance().getBean(JwtConfiguration.class);
    String userId = null;

    if (StringUtils.isNotBlank(authTokenId)) {
      RawAccessJwtToken rawToken = new RawAccessJwtToken(authTokenId);
      Jws<Claims> refreshToken;
      try {
        refreshToken = RefreshToken.create(rawToken, jwtSettings.getSigningKey());
        if (null != refreshToken && StringUtils.isNoneBlank(refreshToken.getBody().getSubject())) {
          userId = refreshToken.getBody().getSubject();
        }
      } catch (Exception e) {
        logger.error("Error Validating the authTokenId : " + authTokenId, e);
        e.printStackTrace();
      }
    }

    return userId;
  }
}

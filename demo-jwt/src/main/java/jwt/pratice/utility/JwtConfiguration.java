package jwt.pratice.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

  @Value("${app.security.jwt.exptime}")
  private Integer expirationTime;

  @Value("${app.security.jwt.issuer}")
  private String issuer;

  @Value("${app.security.jwt.secret-token}")
  private String signingKey;

  @Value("${app.security.jwt.rfreshtime}")
  private Integer refreshTokenExpTime;

  public Integer getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(Integer expirationTime) {
    this.expirationTime = expirationTime;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getSigningKey() {
    return signingKey;
  }

  public void setSigningKey(String signingKey) {
    this.signingKey = signingKey;
  }

  public Integer getRefreshTokenExpTime() {
    return refreshTokenExpTime;
  }

  public void setRefreshTokenExpTime(Integer refreshTokenExpTime) {
    this.refreshTokenExpTime = refreshTokenExpTime;
  }
}

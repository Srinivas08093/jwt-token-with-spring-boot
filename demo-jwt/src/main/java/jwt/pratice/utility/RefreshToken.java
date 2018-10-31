package jwt.pratice.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class RefreshToken implements JwtToken {
  private Jws<Claims> claims;

  private RefreshToken(Jws<Claims> claims) {
    this.claims = claims;
  }

  public static Jws<Claims> create(RawAccessJwtToken token, String signingKey) throws Exception {
    Jws<Claims> claims = token.parseClaims(signingKey);

    return claims;
  }

  @Override
  public String getToken() {
    return null;
  }

  public Jws<Claims> getClaims() {
    return claims;
  }

  public String getJti() {
    return claims.getBody().getId();
  }

  public String getSubject() {
    return claims.getBody().getSubject();
  }
}

package jwt.pratice.utility;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class RawAccessJwtToken implements JwtToken {
  private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

  private String token;

  public RawAccessJwtToken(String token) {
    this.token = token;
  }

  public Jws<Claims> parseClaims(String signingKey) throws Exception {
    try {
      return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
    } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
      logger.error("Invalid JWT Token", ex);
      throw new Exception("Invalid JWT token: ", ex);
    } catch (ExpiredJwtException expiredEx) {
      logger.info("JWT Token is expired", expiredEx);
      throw new Exception("JWT Token expired", expiredEx);
    }
  }

  @Override
  public String getToken() {
    return token;
  }
}

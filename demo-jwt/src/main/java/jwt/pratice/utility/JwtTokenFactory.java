package jwt.pratice.utility;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jwt.pratice.bean.LoginRes;

@Component
public class JwtTokenFactory {
  @Autowired
  private JwtConfiguration jwtConfiguration;

  /**
   * Factory method for issuing new JWT Tokens.
   *
   * @param username
   * @param roles
   * @return
   */
  public JwtAccessToken createAccessJwtToken(LoginRes userContext) {
    if (StringUtils.isBlank(userContext.getUserId()))
      throw new IllegalArgumentException("Cannot create JWT Token without username");

    Claims claims = Jwts.claims().setSubject(userContext.getUserId());

    DateTime currentTime = new DateTime();

    String token = Jwts.builder().setClaims(claims).setIssuer(jwtConfiguration.getIssuer()).setIssuedAt(currentTime.toDate())
        .setExpiration(currentTime.plusMinutes(jwtConfiguration.getExpirationTime()).toDate())
        .signWith(SignatureAlgorithm.HS512, jwtConfiguration.getSigningKey()).compact();

    return new JwtAccessToken(token, claims);
  }

  public JwtToken createRefreshToken(LoginRes userContext) {
    if (StringUtils.isBlank(userContext.getUserId())) {
      throw new IllegalArgumentException("Cannot create JWT Token without username");
    }

    DateTime currentTime = new DateTime();

    Claims claims = Jwts.claims().setSubject(userContext.getUserId());

    String token = Jwts.builder().setClaims(claims).setIssuer(jwtConfiguration.getIssuer()).setId(UUID.randomUUID().toString())
        .setIssuedAt(currentTime.toDate())
        .setExpiration(currentTime.plusMinutes(jwtConfiguration.getRefreshTokenExpTime()).toDate())
        .signWith(SignatureAlgorithm.HS512, jwtConfiguration.getSigningKey()).compact();

    return new JwtAccessToken(token, claims);
  }

}



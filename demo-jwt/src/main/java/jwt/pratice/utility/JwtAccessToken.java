package jwt.pratice.utility;


import io.jsonwebtoken.Claims;

/**
 * Raw representation of JWT Token.
 * 
 *
 */
public final class JwtAccessToken implements JwtToken {
	private final String rawToken;
	private Claims claims;

	protected JwtAccessToken(final String token, Claims claims) {
		this.rawToken = token;
		this.claims = claims;
	}

	public String getToken() {
		return this.rawToken;
	}

	public Claims getClaims() {
		return claims;
	}
}

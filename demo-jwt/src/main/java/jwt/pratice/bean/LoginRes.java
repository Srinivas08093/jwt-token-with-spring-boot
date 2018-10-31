package jwt.pratice.bean;

public class LoginRes {

	private String userId;

	private String firstName;

	private String lastName;

	private String authTokenId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAuthTokenId() {
		return authTokenId;
	}

	public void setAuthTokenId(String authTokenId) {
		this.authTokenId = authTokenId;
	}

}

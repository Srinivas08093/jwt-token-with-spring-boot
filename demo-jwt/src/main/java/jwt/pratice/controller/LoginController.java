package jwt.pratice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jwt.pratice.bean.LoginReq;
import jwt.pratice.bean.LoginRes;
import jwt.pratice.utility.JwtTokenFactory;

@RestController
@RequestMapping("/main")
public class LoginController {

	@GetMapping("/logout")
	public String hi() {
		return "HI";
	}

	@GetMapping("/test")
	public String hidd() {
		return "HI";
	}

	@Autowired
	JwtTokenFactory tokenFactory;

	@PostMapping("/login")
	public LoginRes login(@RequestBody LoginReq loginData) {

		// By using this uer name and password validate user if user exists
		// create a jwt token
		// loginData

		LoginRes userBean = null;

		userBean = new LoginRes();
		userBean.setFirstName("Srinivas");
		userBean.setLastName("Nangana");
		userBean.setUserId("1254");

		userBean.setAuthTokenId(tokenFactory.createAccessJwtToken(userBean).getToken());

		return userBean;

	}

}

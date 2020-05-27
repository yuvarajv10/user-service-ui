/**
 * 
 */
package com.user.controller;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.user.bo.UserBo;
import com.user.dto.UserDto;

/**
 * @author yuvaraj
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

	/**
	 * Add new user.
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/addUser")
	public String addUser(Model model) {
		checkUserAuthStatus();
		model.addAttribute("user", new UserBo());
		return "addUser";
	}

	/**
	 * Add new user.
	 * 
	 * @param addUser
	 * @return
	 */
	@PostMapping(value = "/addUser")
	public String saveUser(@ModelAttribute("addUser") UserBo addUser) {
		checkUserAuthStatus();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject inputJson = new JSONObject();
		inputJson.put("userName", addUser.getUserName());
		inputJson.put("password", addUser.getPassword());
		HttpEntity<String> request = new HttpEntity<>(inputJson.toString(), headers);

		new RestTemplate().postForEntity("http://localhost:8000/user/save", request, UserDto.class);

		return "redirect:http://localhost:8102/product/";
	}

	/**
	 * Login by users detail.
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/login")
	public String loginUser(Model model) {
		model.addAttribute("user", new UserBo());
		return "login";
	}

	/**
	 * Login by users detail.
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/login")
	public String loginUser(@ModelAttribute("user") UserBo user) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject inputJson = new JSONObject();
		inputJson.put("userName", user.getUserName());
		inputJson.put("password", user.getPassword());
		HttpEntity<String> request = new HttpEntity<>(inputJson.toString(), headers);

		ResponseEntity<UserDto> response = new RestTemplate().postForEntity("http://localhost:8000/user/login", request,
				UserDto.class);

		if (response.getBody().getUserName().isEmpty()) {
			return "login";
		}

		return "redirect:http://localhost:8102/product/";
	}

	/**
	 * Logout users details.
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/logout")
	public String logoutUser(Model model) {
		HttpStatus response = new RestTemplate().getForEntity("http://localhost:8000/user/logout", UserDto.class)
				.getStatusCode();

		model.addAttribute("user", new UserBo());
		return "login";
	}

	/**
	 * Checking authentication while access api.
	 * 
	 * @return
	 */
	private String checkUserAuthStatus() {
		try {
			HttpStatus response = new RestTemplate().getForEntity("http://localhost:8000/user/status", UserDto.class)
					.getStatusCode();
			if (response.FORBIDDEN == HttpStatus.FORBIDDEN) {
				return "redirect:http://localhost:8101/user/login";
			}
		} catch (HttpStatusCodeException exception) {
			if (exception.getStatusCode().FORBIDDEN == HttpStatus.FORBIDDEN) {				
				return "redirect:http://localhost:8101/user/login";
			}
		}

		return "";
	}
}

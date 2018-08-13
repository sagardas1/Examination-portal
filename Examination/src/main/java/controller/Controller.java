package controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import service.AccountService;
import vo.Answer;
import vo.CustomResponse;
import vo.ExaminationPattern;
import vo.Instruction;
import vo.LoginCredentials;
import vo.Questions;
import vo.Registration;

@RestController
@RequestMapping("/Exam")
public class Controller {
	AccountService accountService = new AccountService();

	CustomResponse customresponse=new CustomResponse();  //at the getting response
	
	//prepare url
		@RequestMapping(value = "/registration", method = RequestMethod.POST, headers = "Accept=application/json")
		public CustomResponse registrationUser(@RequestBody Registration registration) { //check user sucessfully login
			CustomResponse customresponse = new CustomResponse();
			try {
				System.out.println("INSIDE THHE REGISTRATION "+new Gson().toJson(registration));
				customresponse = accountService.registrationUser(registration);
			} catch (Exception e) {
				customresponse.setStatusMessage("Exception occurred while Registering user");
			}
			return customresponse;
		}
		
		@RequestMapping(value = "/displayQuestion", method = RequestMethod.GET)
		public Answer displayQuestion(long userId) { //check user sucessfully login
			Answer answer = null;
			try {
				System.out.println("INSIDE THHE displayQuestion");
				answer = accountService.displayQuestion(userId);
			} catch (Exception e) {
				customresponse.setStatusMessage("Exception occurred while Registering user");
			}
			return answer;
		}
		
		@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
		public Instruction loginUser(@RequestBody LoginCredentials credentials) { //check user sucessfully login
			//Registration registration = null;
			Instruction instruction=null;
			try {
				System.out.println(credentials.getEmailId());
				System.out.println("INSIDE THHE REGISTRATION");
				instruction = accountService.loginUser(credentials);
			} catch (Exception e) {
				customresponse.setStatusMessage("Exception occurred while Registering user");
			}
			return instruction;
		
		}
		@RequestMapping(value = "/updateuseranswer", method = RequestMethod.POST, headers = "Accept=application/json")
		public Answer updateUserAnswer(@RequestBody Answer correctAnswer)
		{
			return accountService.updateUserAnswer(correctAnswer);
		}
		
		
		@RequestMapping(value = "/creteQuestion", method = RequestMethod.POST, headers = "Accept=application/json")
		public CustomResponse createQuestion(@RequestBody Questions newQuestion)
		{
			return accountService.createQuestion(newQuestion);
		}
		
		@RequestMapping(value = "/examinationpattern", method = RequestMethod.POST, headers = "Accept=application/json")
		public CustomResponse examinationPattern(@RequestBody ExaminationPattern examinationPattern)
		{
			System.out.println("examinationpattern---> ");
			return accountService.examinationPattern(examinationPattern);
		}
		
	
		
		
		
		
		
		
	

}

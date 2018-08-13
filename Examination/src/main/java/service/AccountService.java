package service;

import constants.CustomConstant;
import dao.AccountDao;
import daoimplementation.AccountDaoimplementation;
import vo.Answer;
import vo.CustomResponse;
import vo.ExaminationPattern;
import vo.Instruction;
import vo.LoginCredentials;
import vo.Questions;
import vo.Registration;

public class AccountService 
{
	AccountDao accountdao = new AccountDaoimplementation();

	public CustomResponse registrationUser(Registration registration) 
	{
		CustomResponse customResponse = new CustomResponse();
		if(registration.getPassword().equals(registration.getConfirmPassword()))
		{
			String userExit = accountdao.checkUserInfo(registration);
			if(userExit.equals("userExit"))
			{
				customResponse.setStatusCode(CustomConstant.user_exists_code);
				customResponse.setStatusMessage(CustomConstant.user_exists);
				return customResponse;
			}
			else{
				accountdao.updateAndInsertUserInfo(registration);
				customResponse.setStatusCode(CustomConstant.success_code);
				customResponse.setStatusMessage(CustomConstant.successfull_registered);
				return customResponse;
			}
		}else{
			customResponse.setStatusCode(CustomConstant.passwordDidnt_code);
			customResponse.setStatusMessage(CustomConstant.password_Didnt_match);
		}
			
		return customResponse;
	}

	public Instruction loginUser(LoginCredentials credentials)
	{
		Instruction instruction=null;
		//Registration  registration=null;
		instruction = accountdao.loginUser(credentials);
		return instruction;
		
	}



	public Answer displayQuestion(long userId) {
		ExaminationPattern examinationPattern = null;
		Answer answer = new Answer();
		CustomResponse customResponse = null;
		customResponse = accountdao.checkUserBasedonProfileId(userId);
		answer.setStatusCode(customResponse.getStatusCode());
		answer.setStatusMessage(customResponse.getStatusMessage());
		if(customResponse.getStatusCode()==200){
			examinationPattern = accountdao.examinationPattern();
			answer = accountdao.displayQuestion(examinationPattern);
		}
		return answer;
	}

	public Answer updateUserAnswer(Answer answer) 
	{
		return accountdao.updateUserAnswer(answer);
	}

	public CustomResponse createQuestion(Questions newQuestion) {
		
		return accountdao.createQuestion(newQuestion);
	}

	public CustomResponse examinationPattern(ExaminationPattern examinationPattern) {
		CustomResponse customResponse = new CustomResponse();
		customResponse = accountdao.checkUserBasedonProfileId(examinationPattern.getUserId());
		if(customResponse.getStatusCode()==200){
			customResponse = accountdao.examinationPatternstructure(examinationPattern);
		}
		return customResponse;
	}
	
	
	
	

	
}

package dao;

import vo.Answer;
import vo.CustomResponse;
import vo.ExaminationPattern;
import vo.Instruction;
import vo.LoginCredentials;
import vo.Questions;
import vo.Registration;

public interface AccountDao {

	/*CustomResponse registrationUser(Registration registration);*/

	Instruction loginUser(LoginCredentials credentials);


	public String checkUserInfo(Registration registration);
	
	public int updateAndInsertUserInfo(Registration registration);

	public ExaminationPattern examinationPattern();

	public Answer displayQuestion(ExaminationPattern examinationPattern);

	public Answer updateUserAnswer(Answer answer);


	public CustomResponse checkUserBasedonProfileId(long userId);


	public CustomResponse createQuestion(Questions newQuestion);


	CustomResponse examinationPatternstructure(ExaminationPattern examinationPattern);


}

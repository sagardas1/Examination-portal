
package vo;

import java.util.List;

public class Questions extends CustomResponse{
	private int questionId;
	private String question;
	private List<Option> questionOption;
	private String answer;
	private int subjectId;
	
	
	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public int getQuestionId() {
		return questionId;
	}


	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}


	public List<Option> getQuestionOption() {
		return questionOption;
	}


	public void setQuestionOption(List<Option> questionOption) {
		this.questionOption = questionOption;
	}


		

	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getSubjectId() {
		return subjectId;
	}


	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}



	public static class Option{
		private String slNo;
		private String option;
		
		
		public String getSlNo() {
			return slNo;
		}
		public void setSlNo(String slNo) {
			this.slNo = slNo;
		}
		public String getOption() {
			return option;
		}
		public void setOption(String option) {
			this.option = option;
		}
		
	}

}

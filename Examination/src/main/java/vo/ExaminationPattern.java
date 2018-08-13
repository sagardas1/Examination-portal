package vo;

import java.util.List;

public class ExaminationPattern {
	private int subjectId;
	private int noOfQuestion;
	private int marks;
	private double negMarks;
	private List<Questions> questionAns;
	private int time;
	private long userId;
	
	
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public int getNoOfQuestion() {
		return noOfQuestion;
	}
	public void setNoOfQuestion(int noOfQuestion) {
		this.noOfQuestion = noOfQuestion;
	}
	public List<Questions> getQuestionAns() {
		return questionAns;
	}
	public void setQuestionAns(List<Questions> questionAns) {
		this.questionAns = questionAns;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public double getNegMarks() {
		return negMarks;
	}
	public void setNegMarks(double negMarks) {
		this.negMarks = negMarks;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	
}

package vo;

import java.util.List;

public class Answer extends CustomResponse{
	private long userId;
	private int subjectId;
	private int count;
	List<Questions> answerOfSubjectList;
	private double totalMarks;
	private double score;
	private long date;
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public List<Questions> getAnswerOfSubjectList() {
		return answerOfSubjectList;
	}
	public void setAnswerOfSubjectList(List<Questions> answerOfSubjectList) {
		this.answerOfSubjectList = answerOfSubjectList;
	}
	public double getTotalMarks() {
		return totalMarks;
	}
	public void setTotalMarks(double totalMarks) {
		this.totalMarks = totalMarks;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	
	
}

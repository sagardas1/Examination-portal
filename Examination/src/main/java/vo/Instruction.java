package vo;

public class Instruction extends CustomResponse
{
	Registration reg;
	ExaminationPattern examPattern;
	public Registration getReg() {
		return reg;
	}
	public void setReg(Registration reg) {
		this.reg = reg;
	}
	public ExaminationPattern getExamPattern() {
		return examPattern;
	}
	public void setExamPattern(ExaminationPattern examPattern) {
		this.examPattern = examPattern;
	}

}

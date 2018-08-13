package daoimplementation;

import java.util.Iterator;

import org.jongo.Aggregate;
import org.jongo.Jongo;

import com.mongodb.WriteResult;

import constants.CustomConstant;
import constants.MongoCollectionConstants;
import dao.AccountDao;
import utils.MongoDBUtil;
import vo.Answer;
import vo.Counters;
import vo.CustomResponse;
import vo.ExaminationPattern;
import vo.Instruction;
import vo.LoginCredentials;
import vo.Questions;
import vo.Registration;

public class AccountDaoimplementation implements AccountDao {

	@Override
	public Instruction loginUser(LoginCredentials credentials) {
		Registration reg = null;
		Instruction instruction = new Instruction();
		reg = new Jongo(MongoDBUtil.getDB()).getCollection("registration")
				.findOne("{emailId:#,password:#}", credentials.getEmailId(), credentials.getPassword())
				.as(Registration.class);

		System.out.println(instruction + " " + reg);
		if (reg != null) {
			// (() instruction).setPassword("");
			// ((Registration) instruction).setConfirmPassword("");
			ExaminationPattern examPattern = new Jongo(MongoDBUtil.getDB()).getCollection("examinationPattern")
					.findOne().as(ExaminationPattern.class);
			instruction.setExamPattern(examPattern);
			instruction.setReg(reg);
			instruction.setStatusCode(200);
			instruction.setStatusMessage(CustomConstant.login_successfull);

		} else {
			instruction = new Instruction();
			instruction.setStatusCode(600);
			instruction.setStatusMessage("incorect email/password");
		}

		return instruction;
	}

	@Override
	public String checkUserInfo(Registration registration) {
		String userExit = "";
		Registration regs = new Jongo(MongoDBUtil.getDB()).getCollection("registration")
				.findOne("{mobileNo:#,emailId:#}", registration.getMobileNo(), registration.getEmailId())
				.as(Registration.class);
		if (regs != null) {
			if (regs.getMobileNo() == registration.getMobileNo()
					|| regs.getEmailId().equals(registration.getEmailId())) {
				userExit = "userExit";
				return userExit;
			}

		}
		return userExit;
	}

	@Override
	public int updateAndInsertUserInfo(Registration registration) {
		try {
			if (registration.getUserId() <= 0) {
				registration.setUserId(AccountDaoimplementation.generateUniqueLongVal(CustomConstant.userId));
			}
			registration.setType(CustomConstant.userType);
			new Jongo(MongoDBUtil.getDB()).getCollection("registration").insert(registration);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public ExaminationPattern examinationPattern() {
		ExaminationPattern examinationPattern = new Jongo(MongoDBUtil.getDB())
				.getCollection(MongoCollectionConstants.EXAM_PATTERN).findOne().as(ExaminationPattern.class);
		return examinationPattern;
	}

	@Override
	public Answer displayQuestion(ExaminationPattern examinationPattern) {
		Answer answer = null;
		Iterator<Answer> questionsItr = null;
		try {
			Aggregate agg = new Jongo(MongoDBUtil.getDB()).getCollection("questionAns")
					.aggregate(
							"{$group:{ _id:'$subjectId','subjectId':{'$first':'$subjectId'},questionAns: { $push: {questionId:'$questionId',question:'$question','questionOption':'$questionOption','subjectId':'$subjectId'} }}}")
					.and("{$match:{_id:#}}", examinationPattern.getSubjectId())
					.and("{$project:{_id:0,questionAns:1,subjectId:1}}").and("{$unwind:{path:'$questionAns'}}")
					.and("{ $limit : # }", examinationPattern.getNoOfQuestion())
					.and("{$group:{_id:{_id:'$_id',subjectId:'$subjectId'},'answerOfSubjectList' :{ '$push' : '$questionAns' }}}")
					.and("{ '$project' : { '_id' : 0 , 'answerOfSubjectList' : 1,'subjectId':'$_id.subjectId'}}")
					.and("{ $sample: { size: # } }",examinationPattern.getNoOfQuestion());
			questionsItr = agg.as(Answer.class).iterator();
			System.out.println("hi--->");
			if (questionsItr.hasNext()) {
				answer = questionsItr.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// answer.setCount(10);
		return answer;
	}

	public Answer updateUserAnswer(Answer answer) {
		Answer answers = null;
		try {
			new Jongo(MongoDBUtil.getDB()).getCollection("userAnswer").insert(answer);

			Iterator<Answer> itr = null;
			Aggregate agg = new Jongo(MongoDBUtil.getDB()).getCollection("userAnswer")
					.aggregate(
							"{$project:{subjectId:1,userId:1,answerOfSubjectList:1,size:{$size:'$answerOfSubjectList'}}}")
					.and("{$unwind:{path: '$answerOfSubjectList',preserveNullAndEmptyArrays: true}}")
					.and("{ $lookup: {from: 'questionAns',let: { subjectId: '$subjectId', questionId: '$answerOfSubjectList.questionId' },pipeline: [{ $match:{ $expr:{ $and:[{ $eq: [ '$subjectId',  '$$subjectId' ] },  { $eq: [ '$questionId', '$$questionId' ] }] }} }], as: 'stockdata' }}")
					.and("{$unwind:{path: '$stockdata',preserveNullAndEmptyArrays: true}}")
					.and("{$project:{cmp:{$cmp:['$stockdata.answer','$answerOfSubjectList.answer']},size:1,userId:1,subjectId:1}}")
					.and("{$match:{cmp:0}}")
					.and("{$group:{'_id':{userId:'$userId',cmp:'$cmp'},userId:{$first:'$userId'},subjectId:{$first:'$subjectId'},"
							+ " cmp:{$first:'$cmp'},totalSubmitedAnswer:{$first:'$size'},'correctAnswer': { $sum: 1 }}}")
					.and(" {$lookup:{ from: 'examinationPattern',localField: 'subjectId',foreignField: 'subjectId',as: 'inventory_docs'}}")
					.and("{$unwind:'$inventory_docs'}")
					.and("{$project:{'totalMarks':{'$multiply':['$inventory_docs.noOfQuestion','$inventory_docs.marks']},'score': {$subtract:[{ $multiply: [ '$correctAnswer', '$inventory_docs.marks' ] },"
							+ "{ $multiply: [ { $subtract: ['$totalSubmitedAnswer' , '$correctAnswer' ]}, '$inventory_docs.negMarks' ] }]}}}");
			itr = agg.as(Answer.class);

			if (itr.hasNext()) {
				answers = itr.next();
				answer.setScore(answers.getScore());
				new Jongo(MongoDBUtil.getDB()).getCollection("userAnswer").update("{userId:#}", answer.getUserId())
						.upsert().with("{$set:#}", answer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return answers;
	}

	public static long generateUniqueLongVal(String name) {
		try {
			new Jongo(MongoDBUtil.getDB()).getCollection("counters").update("{ _id:# }", name).with("{$inc:{seq:1}}");
			Counters counters = new Jongo(MongoDBUtil.getDB()).getCollection("counters").findOne("{ _id: #}", name)
					.as(Counters.class);
			return Double.valueOf(counters.getSeq()).longValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0l;
	}

	public static void main(String[] args) {
		ExaminationPattern examinationPattern = new ExaminationPattern();
		examinationPattern.setSubjectId(30);
		new AccountDaoimplementation().displayQuestion(examinationPattern);
	}

	@Override
	public CustomResponse checkUserBasedonProfileId(long userId) {
		CustomResponse customResponse = new CustomResponse();
		try {
			Registration regs = new Jongo(MongoDBUtil.getDB()).getCollection("registration")
					.findOne("{userId:#}", userId).as(Registration.class);
			if (regs != null) {
				customResponse.setStatusCode(200);
			} else {
				customResponse.setStatusCode(100);
				customResponse.setStatusMessage("user not der");
			}
		} catch (Exception e) {

		}
		return customResponse;
	}

	@Override
	public CustomResponse createQuestion(Questions newQuestion) {
		CustomResponse customResponse = new CustomResponse();
		boolean isinsert = false;

		try {
			if (newQuestion.getQuestionId() <= 0) {
				newQuestion.setQuestionId(
						(int) AccountDaoimplementation.generateUniqueLongVal(CustomConstant.QUESTION_ID));

				new Jongo(MongoDBUtil.getDB()).getCollection("questionAns").insert(newQuestion);
				isinsert = true;
			}
			if (isinsert) {
				customResponse.setStatusCode(CustomConstant.success_code);
				customResponse.setStatusMessage(CustomConstant.question_success);
			} else {
				customResponse.setStatusCode(CustomConstant.fail_code);
				customResponse.setStatusMessage(CustomConstant.question_faield);
			}
		} catch (Exception e) {

		}
		return customResponse;
	}

	@Override
	public CustomResponse examinationPatternstructure(ExaminationPattern examinationPattern) {
		CustomResponse customResponse = new CustomResponse();
		try {
			new Jongo(MongoDBUtil.getDB()).getCollection("examinationPattern").update("{subjectId:#}", examinationPattern.getSubjectId())
			.upsert().with("{$set:#}", examinationPattern);
			customResponse.setStatusCode(CustomConstant.success_code);
			customResponse.setStatusMessage("success");
			
		} catch (Exception e) {

		}
		return customResponse;
	}

}

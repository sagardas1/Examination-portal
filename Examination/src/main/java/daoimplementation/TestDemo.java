package daoimplementation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jongo.Jongo;

import com.google.gson.Gson;

import utils.MongoDBUtil;
import vo.Questions;

public class TestDemo {
	public static void main(String[] args) {
		new TestDemo().test();
	}

	public void test() {
		Iterator<Questions> itr = new Jongo(MongoDBUtil.getDB()).getCollection("QuesAns").find("{}")
				.as(Questions.class);

		List<Questions> ques = new ArrayList<>();
		while (itr.hasNext()) {
			ques.add(itr.next());
		}
		System.out.println(new Gson().toJson(ques));
	}

}

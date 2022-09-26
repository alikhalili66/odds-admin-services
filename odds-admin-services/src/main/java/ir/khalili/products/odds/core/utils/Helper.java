package ir.khalili.products.odds.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class Helper {

	public static void main(String[] args) {
		System.out.println("generateRandomPassword:" + generateRandomPassword());
	}

	public static <T extends Object> Future<T> createFuture(T t) {

		Promise<T> promise = Promise.promise();
		promise.complete(t);

		return promise.future();
	}

	public static Future<Void> createFutureVoid() {

		Promise<Void> promise = Promise.promise();
		promise.complete();

		return promise.future();
	}

	public static String createQueryForInClause(int paramSize) {

		StringBuilder inClause = new StringBuilder();
		for (int i = 0; i < paramSize; i++) {
			inClause.append("?, ");
		}

		String str = inClause.toString();
		if (str.endsWith(", ")) {
			str = str.substring(0, str.length() - 2);
		}

		return str;
	}

	public static int generateRandomNumber() {
		int min = 10000;
		int max = 99999;

		// Generate random int value from 50 to 100
//        System.out.println("Random value in int from "+min+" to "+max+ ":");
		int random_int = (int) (Math.random() * (max - min + 1) + min);
//        System.out.println(random_int);

		return random_int;
	}

	// generate random password with length is 8 character (number and character)
	public static String generateRandomPassword() {
	    String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
	    String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
	    String numbers = RandomStringUtils.randomNumeric(2);
	    String totalChars = RandomStringUtils.randomAlphanumeric(2);
	    String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
	      .concat(numbers)
	      .concat(totalChars);
	    List<Character> pwdChars = combinedChars.chars()
	      .mapToObj(c -> (char) c)
	      .collect(Collectors.toList());
	    Collections.shuffle(pwdChars);
	    String password = pwdChars.stream()
	      .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
	      .toString();
	    return password;
	}

	public static void renderTree(JsonObject jo, List<JsonObject> list) {

		List<JsonObject> childs = new ArrayList<>();

		for (JsonObject jsonObject : list) {
			if (null != jsonObject.getInteger("parentId")
					&& jo.getInteger("id").intValue() == jsonObject.getInteger("parentId").intValue()) {
				childs.add(jsonObject);

			}
		}

		jo.put("child", childs);

		if (childs.size() > 0) {
			for (JsonObject entry : childs) {
				renderTree(entry, list);
			}
		}
	}

}

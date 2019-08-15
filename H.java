import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class H {

	static HashMap<String, Result> results;
	static double total;
	static double sSize;
	static Node tree;
	static double ent;
	static double blockCodeSize;
	static double averageLength;
	static double eficiency;

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));

		String line = in.readLine();
		results = new HashMap<>();
		processMessage(line);
		System.out.println("Total: " + total);
		System.out.println("Ssize: " + sSize);
		System.out.println("Ent: " + ent);
		System.out.println("Bsize: " + blockCodeSize);
		System.out.println("Alen: " + averageLength);
		System.out.print("Eficiency: " + eficiency);

		// StringTokenizer skt = new StringTokenizer(in.readLine());
		// int i = 0;
		// ArrayList<String> keys = new ArrayList<>();
		// ArrayList<Integer> ocurrences = new ArrayList<>();
		// while (skt.hasMoreElements()) {
		// keys.add(i+"");
		// ocurrences.add(Integer.parseInt(skt.nextToken()));
		// i++;
		// }
		// Node tree = Hoffman(keys, ocurrences);
		// printTree(tree);
		// System.out.println();
		// printCode(tree, "");
	}

	public static Node Hoffman(ArrayList<String> keys, ArrayList<Integer> ocurrences) {
		PriorityQueue<Node> nodes = new PriorityQueue<>();
		for (int i = 0; i < keys.size(); i++) {
			nodes.add(new Node(keys.get(i), ocurrences.get(i)));
		}
		return Hoffman_Util(nodes);
	}

	public static Node Hoffman_Util(PriorityQueue<Node> nodes) {
		if (nodes.size() <= 1) {
			return nodes.poll();
		} else {
			Node n1 = nodes.poll();
			Node n2 = nodes.poll();
			int sum = n1.value + n2.value;
			Node n3 = new Node(sum, n1, n2);
			nodes.add(n3);
			return Hoffman_Util(nodes);
		}
	}

	static void processMessage(String message) {
		total = message.length();
		for (int i = 0; i < message.length(); i++) {
			String key = message.charAt(i) + "";
			if (!results.containsKey(key)) {
				Result r = new Result(key);
				results.put(key, r);
				sSize++;
			} else {
				Result r = results.get(key);
				r.ocurr = r.ocurr + 1;
			}
		}
		ArrayList<String> keys = new ArrayList<>();
		ArrayList<Integer> ocurrences = new ArrayList<>();
		for (Result result : results.values()) {
			result.prob = result.ocurr / total;
			result.calcIprop();
			keys.add(result.key);
			ocurrences.add(result.ocurr);
			ent += result.prob * result.iprop;
		}

		tree = Hoffman(keys, ocurrences);

		printCode(tree, "");

		for (Result result : results.values()) {
			averageLength += result.code.length() * result.prob;
			System.out.println();
			System.out.println(result);
		}

		blockCodeSize = Math.ceil(Math.log(sSize) / Math.log(2));
		eficiency = ent / averageLength;

	}

	static void printTree(Node node) {

		if (node == null) {
			return;
		}
		System.out.print(node + " ");

		printTree(node.zero);

		printTree(node.one);

	}

	static void printCode(Node node, String code) {

		if (node == null) {
			return;
		}
		if (node.zero == null && node.one == null) {
			System.out.println(node.key + ": " + code);
			results.get(node.key).code = code;
		}
		printCode(node.zero, code + "0");

		printCode(node.one, code + "1");

	}

	static class Node implements Comparable<Node> {
		String key;
		int value;
		Node zero;
		Node one;

		public Node(String key, int value) {
			this.key = key;
			this.value = value;
		}

		public Node(int value, Node zero, Node one) {
			this.value = value;
			this.zero = zero;
			this.one = one;
		}

		@Override
		public int compareTo(Node o) {
			return value - o.value;
		}

		@Override
		public String toString() {
			return value + "";
		}

	}

	public static class Result {

		String key;
		String code;
		double prob;
		double iprop;
		int ocurr;

		public Result(String key) {
			this.key = key;
			ocurr = 1;
		}

		public void calcIprop() {
			iprop = Math.log(1d / prob) / Math.log(2);
		}

		@Override
		public String toString() {
			if (key.equals(" ")) {
				return "space" + "\t" + code + "\t" + prob + "\t" + iprop + "\t" + ocurr;
			}
			return key + "\t" + code + "\t" + prob + "\t" + iprop + "\t" + ocurr;
		}
	}

}
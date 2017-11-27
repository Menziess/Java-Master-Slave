
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;

/**
 * A Master that distributes increasingly heavy computations to Slaves using
 * sockets.
 * 
 * @author stefa
 */
public class Master {

	private static final int PORT = 9090;
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

	public static void main(String[] args) throws IOException {
		System.out.println("Server running on port " + PORT + ".");

		// Try with resources, so sockets don't have to be
		// closed manually
		try (ServerSocket listener = new ServerSocket(PORT)) {

			// Generate computational problem
			int[][] ranges = distributeProblem();
			int index = 0;

			// New handlers are created when listener accepts
			// new incoming connections
			while (true) {
				new Handler(listener.accept(), ranges[index]).start();
				index++;
			}
		}
	}

	// Create computational problem
	private static int[][] distributeProblem() {

		// Get the range of numbers to find primes in
		Scanner scanner = new Scanner(System.in);
		int limit = 0;
		do {
			if (limit < 10_000)
				System.out.println("Minimum of 10.000.");
			System.out.print("Find primes between 0 and: ");
			limit = scanner.nextInt();
			scanner.reset();
		} while (limit < 10_000);

		// Get the amount of nodes that will do the work
		int nodes;
		do {
			System.out.print("Distribute over amount of nodes: ");
			nodes = scanner.nextInt();
		} while (nodes < 1);
		int[][] problem = new int[nodes][2];

		scanner.close();

		// Distribute the ranges over the nodes
		System.out.println(nodes + " Nodes will find primes in a range of numbers between 0 - " + limit + ".");

		int prevMax = 0;
		int max = limit;
		for (int i = 0; i < nodes; i++) {

			max = max / 2;
			if (max < 1000) {
				max = limit;
				break;
			}
			problem[i][0] = prevMax;
			problem[i][1] = max;
			prevMax = max + 1;
		}

		return problem;

	}

	/**
	 * Handler thread that runs the connection with the client.
	 * 
	 * @author stefa
	 */
	private static class Handler extends Thread {

		private Socket socket;
		private int[] range;
		private BufferedReader in;
		private PrintWriter out;

		public Handler(Socket socket, int[] range) {
			this.socket = socket;
			this.range = range;
		}

		public void run() {
			try {

				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				// int[] test = {10_000, 20_000};
				out.println("COMPUTE " + Utils.arrayToString(range));

				while (true) {
					String input = in.readLine();
					if (input == null)
						return;

					System.out.println(input);
				}

			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}
}

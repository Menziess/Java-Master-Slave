
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Slave {

	private static final int PORT = 9090;
	private BufferedReader in;
	private PrintWriter out;
	
	public Slave() {
	}

	/**
	 * Gets address to connect to.
	 */
	private String getServerAddress() {
		System.out.print("Connection address: ");
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	/**
	 * Main method, waits for 60 seconds to connect to compute primes.
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("Spawned a new slave.");
		Slave slave = new Slave();
		slave.run();
		System.out.println("Slave has stopped.");
	}

	/**
	 * Starts a connection to the Master node.
	 */
	private void run() throws IOException {
		String serverAddress = getServerAddress();

		// Will try to connect for 60 seconds
		long end = System.currentTimeMillis() + 60 * 1_000;
		System.out.print("Connecting... ");
		while (System.currentTimeMillis() < end) {
			
			// Try with resources
			try (Socket socket = new Socket(serverAddress, PORT)) {
				
				System.out.println("Connected to " + serverAddress + "...");

				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					String line = in.readLine();
					if (line.startsWith("COMPUTE")) {
						String[] range = line.substring(8).split(" ");
						String primes = solve(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
						out.println(primes);
					} else {
						System.out.print("Plain message: ");
						System.out.println(line);
					}
				}

			} catch (Exception e) {}
		}
	}

	/**
	 * Finds all primes within a given range.
	 */
	private static String solve(int start, int end) {

		System.out.println("Took a job, finding primes between " + start + " - " + end);
		ArrayList<Integer> primes = new ArrayList<Integer>();
		IntStream.range(start, end).forEachOrdered(n -> {
			if (isPrime(n))
				primes.add(n);
		});
		if (primes.isEmpty())
			System.out.println("No primes found...");
		else
			System.out.println(primes.size() + " primes found!");
		return primes.toString();
	}

	/**
	 * Determines whether a number is prime, which is computationally expensive for
	 * large numbers.
	 */
	public static boolean isPrime(int number) {
		return number > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(number)).noneMatch(i -> number % i == 0);
	}

}

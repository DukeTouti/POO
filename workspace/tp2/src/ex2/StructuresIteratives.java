package ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StructuresIteratives {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static String[] readValues(InputStream in) throws IOException {
		InputStreamReader r = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		String[] values = line.split(" ");
		return values;
	}
}

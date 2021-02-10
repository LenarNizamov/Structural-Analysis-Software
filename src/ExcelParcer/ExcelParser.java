package ExcelParser;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ExcelParser {

	public double[][] readCsv(String filePath) throws IOException {
		ArrayList<String[]> dataS = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				String[] count = line.split(",");
				dataS.add(count);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		double[][] dataD = new double[dataS.size()][9];
		for (int i = 0; i < dataS.size(); i++) {
			for (int j = 0; j < 9; j++) {
				if (i == 0 & j > 2)
					continue;
				dataD[i][j] = Double.parseDouble(dataS.get(i)[j+1]);
			}
		}
		return dataD;
	}
}

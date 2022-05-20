import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.LinkedList;

public class TextToExcel {
    public void changeFormat(String oldPath, String newPath,String split) throws IOException {
        LinkedList<String[]> text_lines = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(oldPath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                text_lines.add(sCurrentLine.split(split));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = newPath;
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet1");
        int row_num = 0;
        for (String[] line : text_lines) {
            Row row = sheet.createRow(row_num++);
            int cell_num = 0;
            for (String value : line) {
                Cell cell = row.createCell(cell_num++);
                cell.setCellValue(value);
            }
        }

        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
    }

}

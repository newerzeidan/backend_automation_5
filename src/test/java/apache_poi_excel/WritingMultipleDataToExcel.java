package apache_poi_excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class WritingMultipleDataToExcel {
    public static void main(String[] args) throws IOException {

        String filePath = "test_data/WriteData.xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet("Sheet2");

        Object [][] employeeData = {
                {"EmpID", "Name", "Tile"},
                {101, "Tech Global", "DevOps"},
                {102, "Ulan", "Developer"},
                {103, "Abe", "Instructor"},
        };

        //Getting the length of the multidimensional array (it will be our reference for number of rows)
        int rowLength = employeeData.length;

        System.out.println("Length of the multidimensional array will be " + rowLength);

        //Getting the length of the row
        int cellLength = employeeData[0].length;
        System.out.println("Amount of the cells per row " + cellLength);

        //Creating the rows on the Excel file using a for loop
        for(int r = 0; r < rowLength; r++){

        //Getting the corresponding row
        XSSFRow row = sheet.createRow(r);

        //Creating the cells for each row
        for (int c = 0; c < cellLength; c++){

        //Creating the cell for that row
        XSSFCell cell = row.createCell(c);

        //Getting the corresponding data from the employeeData based on the indexes of the nested loop
        Object cellValue = employeeData[r][c];

        //Checking each data from the multidimensional array and write it into an Excel file
        if(cellValue instanceof String)
            cell.setCellValue((String) cellValue);

        if(cellValue instanceof  Integer)
            cell.setCellValue((Integer) cellValue);

        if(cellValue instanceof Boolean)
            cell.setCellValue((Boolean) cellValue);

        }
    }

        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        workbook.write(fileOutputStream);
        workbook.close();
    }
}

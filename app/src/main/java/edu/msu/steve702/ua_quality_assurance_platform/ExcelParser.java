package edu.msu.steve702.ua_quality_assurance_platform;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;

/**
 * ExcelParser Class
 * This class is used for parsing the checklist excel files into the ChecklistDataObject.
 */
public class ExcelParser {

    /** The Checklist Data Object **/
    private static ChecklistDataObject data;
    /** Variable for the first row as the header row **/
    private static boolean firstRow = true;
    /** The Application Context **/
    private static Context ctx;

    /** Empty Constructor **/
    public ExcelParser(){
    }

    /**
     * Constructor for setting up variables prior to parsing
     * @param context the context of the application
     * @throws IOException io exception
     */
    public ExcelParser(Context context) throws IOException {
        this.ctx = context;

        Map<Integer, Map<Integer, String[]>> map = new HashMap<>();

        data = new ChecklistDataObject(null, map);

        firstRow = true;
    }

    /**
     * Function for reading the excel file.
     * @param fileName a string representing the filename of the excel file
     * @throws IOException io exception
     */
    public static void readXLSFile(String fileName) throws IOException
    {
        InputStream ExcelFileToRead = new FileInputStream(fileName);
        HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

        HSSFSheet sheet=wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;

        Iterator rows = sheet.rowIterator();

        while (rows.hasNext())
        {
            row=(HSSFRow) rows.next();
            Iterator cells = row.cellIterator();

            if(row.getRowNum() == 0){
                // Skip first row
                continue;
            }

            while (cells.hasNext())
            {
                cell=(HSSFCell) cells.next();

                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
                {
                    System.out.print(cell.getStringCellValue()+" ");
                }
                else if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
                {
                    data.setChecklistId((int)cell.getNumericCellValue());
                }

            }

        }

    }

    /**
     * Function for reading the excel file into a checklist data object
     * @param fileName string representing the filename of the excel file being parsed
     * @return a ChecklistDataObject
     * @throws IOException io exception
     */
    public static ChecklistDataObject readXLSXFile(String fileName) throws IOException
    {
        AssetManager am = ctx.getAssets();

        InputStream ExcelFileToRead = am.open("templates/" + fileName);

        try{

            XSSFWorkbook  wb = new XSSFWorkbook(OPCPackage.open(ExcelFileToRead));


            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;

            Iterator rows = sheet.rowIterator();

            while (rows.hasNext())
            {
                row=(XSSFRow) rows.next();

                if(row.getRowNum() == 0){
                    // Skip first row
                    continue;
                }

                Iterator cells = row.cellIterator();
                while (cells.hasNext())
                {
                    cell=(XSSFCell) cells.next();

                    if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
                    {
                        if(firstRow){
                            data.setChecklistId((int)cell.getNumericCellValue());


                            firstRow = false;
                        }

                        int key = (int)row.getCell(1).getNumericCellValue();

                        // Retrieve question category
                        if(!data.hasKey(key)){
                            data.add(key);

                        }


                        // Retrieve question number
                        String cellTxt = row.getCell(3).getStringCellValue();

                        String[] arr = {cellTxt, ctx.getString((R.string.no_answer)), ""};

                        data.get(key).put((int)row.getCell(2).getNumericCellValue(), arr);

                        break;

                    }

                }

            }

            ExcelFileToRead.close();


        }catch(InvalidFormatException e){
            return null;
        }

        return data;
    }

}

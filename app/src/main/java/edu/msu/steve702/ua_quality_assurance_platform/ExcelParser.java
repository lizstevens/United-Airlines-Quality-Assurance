package edu.msu.steve702.ua_quality_assurance_platform;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Pair;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;


public class ExcelParser {

    private static ChecklistDataObject data;
    private static boolean firstRow = true;


    private static Context ctx;

    public ExcelParser(){}


    public ExcelParser(Context context) throws IOException {

        this.ctx = context;

        Map<Integer, Map<Integer, String[]>> map = new HashMap<>();
//        List<Map<Integer, String[]>> newmap = new ArrayList<>();

        data = new ChecklistDataObject(null, map);

        firstRow = true;


    }

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

    public static ChecklistDataObject readXLSXFile(String fileName) throws IOException
    {



        //InputStream ExcelFileToRead = ctx.getResources().openRawResource(R.raw.audit_checklist_and_logic);

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

//                    if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
//                    {
//                        //System.out.print(cell.getStringCellValue()+" ");
//
//                        // Retrieve question text
//
//                    }
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
//                        Boolean status = null;
//
//                        Pair<String, Boolean> pair = new Pair<String, Boolean>(cellTxt, status);
//

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

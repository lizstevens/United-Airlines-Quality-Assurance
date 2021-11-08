package edu.msu.steve702.ua_quality_assurance_platform;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

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
import java.util.Iterator;

import edu.msu.steve702.ua_quality_assurance_platform.data_objects.ChecklistDataObject;


public class ExcelParser {

    private static ChecklistDataObject data;
    private static boolean firstRow = true;


    private static Context ctx;


    public ExcelParser(Context context) throws IOException {

        this.ctx = context;


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
                    data.setId((int)cell.getNumericCellValue());
                }
                else
                {
                    //U Can Handel Boolean, Formula, Errors
                }
            }
            System.out.println();
        }

    }

    public static void readXLSXFile(String fileName) throws IOException
    {



        //InputStream ExcelFileToRead = ctx.getResources().openRawResource(R.raw.audit_checklist_and_logic);

        AssetManager am = ctx.getAssets();

        InputStream ExcelFileToRead = am.open(fileName + ".xlsx");

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

                    if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
                    {
                        System.out.print(cell.getStringCellValue()+" ");
                    }
                    else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
                    {
                        if(firstRow){
                            data.setId((int)cell.getNumericCellValue());

                            firstRow = false;
                        }

                    }
                    else
                    {
                        //U Can Handel Boolean, Formula, Errors
                    }
                }
                System.out.println();
            }

        }catch(InvalidFormatException e){
            return;
        }



    }


}

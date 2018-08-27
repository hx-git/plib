package com.hx.lib_hx.xls;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 按行和列读取数据
 */
public class ReadXls<T> {
    public List<T> readByRow(String path, Context context,T data) {
        return readByRow(path, context, data, 0);
    }
    public List<T> readByRow(String path, Context context,T data,int rowIndex) {
        List<T> list = null;
        Class<?> aClass = data.getClass();
        Field[] fields = aClass.getDeclaredFields();
        try {
            InputStream inputStream = context.getAssets().open(path);
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            list = new ArrayList<>(rows);
            T obj;
            for (int i = rowIndex; i < rows; i++) {
                //克隆一个对象
//                obj = copy(data);
                obj = (T) aClass.newInstance();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        field.setAccessible(true);
                        field.set(obj,sheet.getCell(column.value(),i).getContents());
                    }
                }
                list.add(obj);
            }
        } catch (Exception e) {
            Log.e("hx->read()", e.toString());
        }
        return list;
    }

    public static DevBean readByColumn(String path, Context context) {
        DevBean map = new DevBean();
        try {
            InputStream is = context.getAssets().open(path);
            Workbook xlsSheets = Workbook.getWorkbook(is);
            Sheet sheet = xlsSheets.getSheet(0);
            int columns = sheet.getColumns();
            for (int i = 0; i < columns; i++) {
                Cell[] cells = sheet.getColumn(i);
                String key = cells[0].getContents();
                String value = cells[1].getContents();
                System.out.println("设备值："+key+"；"+value);
                map.put(key,value);
            }
        } catch (Exception e) {

        }
        return map;
    }
}

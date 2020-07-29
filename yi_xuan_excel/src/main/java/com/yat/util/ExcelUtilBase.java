package com.yat.util;

import com.yat.excel.annotation.Excel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

/**
 * <p>Description: ExcelUtil基础类 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:44
 */
public class ExcelUtilBase {

    /**
     * 把传进指定格式的字符串解析到Map中
     *
     * @param keyValue 字符串
     * @return 、
     */
    public static Map<String, String> getMap(String keyValue) {

        Map<String, String> map = new HashMap<>(20);
        if (keyValue != null) {
            String[] str = keyValue.split(",");
            for (String element : str) {
                String[] str2 = element.split(":");
                map.put(str2[0], str2[1]);
            }
        }
        return map;
    }

    /**
     * 把传进指定格式的字符串解析到Map中
     *
     * @param clazz 、
     * @return 、
     * @throws NoSuchFieldException 、
     */
    public static Map<String, String> getMap(Class<T> clazz) throws NoSuchFieldException {

        Map<String, String> map = new HashMap<>(20);
        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (Field value : fields) {
            value.setAccessible(true);
        }
        for (Field value : fields) {
            field = clazz.getDeclaredField(value.getName());
            Excel column = field.getAnnotation(Excel.class);
            if (column != null) {
                map.put(column.title(), field.getName());
            }
        }
        return map;
    }

    /**
     * @param obj 、
     * @return 、
     * @throws NoSuchFieldException 、
     */
    public static Map<String, Object> getMap(Object obj) {

        Map<String, Object> map = new HashMap<>(16);
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            // 字段值
            try {
                map.put(field.getName(), field.get(obj));
            } catch (Exception e) {
                System.out.println("字段[" + field.getName() + "]解析异常");
            }
        }
        return map;
    }

    /**
     * 把传进指定格式的字符串解析到List中
     *
     * @param keyValue 、
     * @return 、
     */
    public static List<String> getList(String keyValue) {

        List<String> list = new ArrayList<String>();
        if (keyValue != null) {
            String[] str = keyValue.split(",");

            for (String element : str) {
                String[] str2 = element.split(":");
                list.add(str2[0]);
            }
        }
        return list;
    }

    /**
     * 把传进指定格式的字符串解析到List中
     *
     * @param clazz 、
     * @return 、
     * @throws NoSuchFieldException 、
     */
    public static List<String> getList(Class<T> clazz) throws NoSuchFieldException {

        List<String> list = new ArrayList<>(16);
        Field field;
        Field[] fields = clazz.getDeclaredFields();
        for (Field value : fields) {
            value.setAccessible(true);
        }
        for (Field value : fields) {
            field = clazz.getDeclaredField(value.getName());
            Excel column = field.getAnnotation(Excel.class);
            if (column != null) {
                list.add(column.title());
            }
        }
        return list;
    }

    public static List getResult(ExcelParam excelParam) throws Exception {
        Set keySet;
        //新加入了注解，如果map为空，则自动从class中的注解自动查找
        if (excelParam.getMap() == null || excelParam.getMap().size() == 0) {
            excelParam.setMap(getMap(excelParam.getClazz()));
            keySet = excelParam.getMap().keySet();
        } else {
            // 返回键的集合
            keySet = excelParam.getMap().keySet();
        }
        ArrayList list = new ArrayList();
        String fileType = "";
        InputStream is = null;
        Workbook wb = null;
        if (excelParam.getStream()) {
            is = new ByteArrayInputStream(excelParam.getBuf());
            wb = WorkbookFactory.create(is);
        } else {
            fileType = excelParam.getFilePath().substring(excelParam.getFilePath().lastIndexOf(".") + 1, excelParam.getFilePath().length());
            is = new FileInputStream(excelParam.getFilePath());
            if (ExcelTypeEnum.EXCEL_THREE.getText().equals(fileType)) {
                wb = new HSSFWorkbook(is);
            } else if (ExcelTypeEnum.EXCEL_SEVEN.getText().equals(fileType)) {
                wb = new XSSFWorkbook(is);
            } else {
                throw new Exception("您输入的excel格式不正确");
            }
        }


        int startSheetNum = 0;
        int endSheetNum = 1;
        if (null != excelParam.getSheetIndex()) {
            startSheetNum = excelParam.getSheetIndex() - 1;
            endSheetNum = excelParam.getSheetIndex();
        }


        // 获取每个Sheet表
        for (int sheetNum = startSheetNum; sheetNum < endSheetNum; sheetNum++) {
            // 记录第x行为表头
            int rowNum_x = -1;
            // 存放每一个field字段对应所在的列的序号
            Map<String, Integer> cellmap = new HashMap<String, Integer>(20);
            // 存放所有的表头字段信息
            List<String> headList = new ArrayList<>(16);

            Sheet hssfSheet = wb.getSheetAt(sheetNum);

            //判断表中是否含有合并单元格-为了忽略导出时候带的表头
            if (hssfSheet.getNumMergedRegions() > 0) {
                excelParam.setRowNumIndex(2);
            }
            // 设置默认最大行为50w行
            if (hssfSheet.getLastRowNum() > 500000) {
                throw new Exception("Excel 数据超过50w行,请检查是否有空行,或分批导入");
            }

            // 循环行Row
            for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                // 如果传值指定从第几行开始读，就从指定行寻找，否则自动寻找
                if (excelParam.getRowNumIndex() != null && rowNum_x == -1) {
                    rowNum = excelParam.getRowNumIndex() - 1;
                    Row hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
                        throw new RuntimeException("指定的行为空，请检查");
                    }
                }
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                boolean flag = false;
                for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
                    if (hssfRow.getCell(i) != null && !("").equals(hssfRow.getCell(i).toString().trim())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    continue;
                }
                if (rowNum_x == -1) {
                    // 循环列Cell
                    for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {

                        Cell hssfCell = hssfRow.getCell(cellNum);
                        if (hssfCell == null) {
                            continue;
                        }
                        String tempCellValue = hssfSheet.getRow(rowNum).getCell(cellNum).getStringCellValue();
                        tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
                        tempCellValue = tempCellValue.trim();

                        headList.add(tempCellValue);

                        for (Object key : keySet) {
                            if (StringUtils.isNotBlank(tempCellValue)
                                    && StringUtils.equals(tempCellValue, key.toString())) {
                                rowNum_x = rowNum;
                                cellmap.put(excelParam.getMap().get(key).toString(), cellNum);
                            }
                        }
                        if (rowNum_x == -1) {
                            throw new Exception("没有找到对应的字段或者对应字段行上面含有不为空白的行字段");
                        }
                    }
                    if (excelParam.getSameHeader()) {
                        // 读取到列后，检查表头是否完全一致--start
                        for (String s : headList) {
                            boolean boo = false;
                            for (Object o : keySet) {
                                boo = StringUtils.equals(s, o.toString());
                            }
                            if (!boo) {
                                throw new Exception("表头字段和定义的属性字段不匹配，请检查");
                            }
                        }
                        for (Object o : keySet) {
                            boolean boo = false;
                            String tempname = o.toString();
                            for (String s : headList) {
                                if (tempname.equals(s)) {
                                    boo = true;
                                }
                            }
                            if (!boo) {
                                throw new Exception("表头字段和定义的属性字段不匹配，请检查");
                            }
                        }
                        // 读取到列后，检查表头是否完全一致--end
                    }
                } else {
                    Object obj = excelParam.getClazz().newInstance();
                    for (Object key : keySet) {
                        Integer cellNum_x = cellmap.get(excelParam.getMap().get(key).toString());
                        if (cellNum_x == null || hssfRow.getCell(cellNum_x) == null) {
                            continue;
                        }
                        // 得到属性
                        String attr = excelParam.getMap().get(key).toString();
                        Class<?> attrType = BeanUtils.findPropertyType(attr, new Class[]{obj.getClass()});
                        Cell cell = hssfRow.getCell(cellNum_x);
                        getValue(cell, obj, attr, attrType, rowNum, cellNum_x, key);
                    }
                    list.add(obj);
                }
            }
        }
        is.close();
        return list;
    }

    public static void addWaterMark(XSSFWorkbook wb, XSSFSheet sheet, String waterMark) {
        // 是否添加水印
        if (StringUtils.isNotEmpty(waterMark)) {

            FontImage.Watermark watermark = new FontImage.Watermark();
            watermark.setText(waterMark);
            watermark.setEnable(true);
            BufferedImage image = FontImage.createWatermarkImage(watermark);
            // 导出到字节流B
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", os);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("添加水印失败");
            }

            int pictureIdx = wb.addPicture(os.toByteArray(), Workbook.PICTURE_TYPE_PNG);
            POIXMLDocumentPart poixmlDocumentPart = wb.getAllPictures().get(pictureIdx);

            PackagePartName ppn = poixmlDocumentPart.getPackagePart().getPartName();
            String relType = XSSFRelation.IMAGES.getRelation();
            //add relation from sheet to the picture data
            PackageRelationship pr = sheet.getPackagePart().addRelationship(ppn, TargetMode.INTERNAL, relType, null);
            //set background picture to sheet
            sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
        }
    }

    public static void commonExportExcel(ExcelParam excelParam) throws Exception {

        Map<String, String> map = getMap(excelParam.getClazz());
        List<String> keyList = null;
        if (StringUtils.isEmpty(excelParam.getKeyValue())) {
            keyList = getList(excelParam.getClazz());
        } else {
            keyList = getList(excelParam.getKeyValue());
        }
        Object obj = excelParam.getClazz().newInstance();
        // 创建HSSFWorkbook对象(excel的文档对象)
        XSSFWorkbook wb = new XSSFWorkbook();
        // 建立新的sheet对象（excel的表单）
        XSSFSheet sheet = wb.createSheet("sheet1");

        //添加水印
        if (StringUtils.isNotEmpty(excelParam.getWaterMark())) {
            addWaterMark(wb, sheet, excelParam.getWaterMark());
        }

        // 头部样式
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setWrapText(true);
        // 单元格样式
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //存储最大列宽
        Map<Integer, Integer> maxWidth = new HashMap<Integer, Integer>(20);
        // 存储属性信息
        Map<String, String> attMap = new HashMap(20);
        //如果有表头，字段头及表格创建行开始行
        int startRow = 0;
        //是否创建表头
        if (StringUtils.isNotEmpty(excelParam.getHeaderName())) {
            Row rowHeader = sheet.createRow(0);
            rowHeader.setHeight((short) (25 * 25));
            rowHeader.setRowStyle(headerStyle);
            Cell rowCell = rowHeader.createCell(0);
            rowCell.setCellStyle(headerStyle);
            rowCell.setCellValue(excelParam.getHeaderName());
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, (keyList.size() - 1));
            sheet.addMergedRegion(cra);
            startRow = 1;
        }
        // 在sheet里创建第x(取决于是否有表头，如果有表头，则在第2行创建，否则在第一行创建)行为表头，参数为行索引(excel的行)
        Row rowHeader = sheet.createRow(startRow);
        rowHeader.setHeight((short) (25 * 20));
        rowHeader.setRowStyle(headerStyle);

        //设置表head
        int index = 0;
        for (String key : keyList) {
            Cell rowCell = rowHeader.createCell(index);
            rowCell.setCellStyle(headerStyle);
            rowCell.setCellValue(key);
            attMap.put(Integer.toString(index), map.get(key));
            maxWidth.put(index, rowCell.getStringCellValue().getBytes().length * 256 + 200);
            index++;
        }
        // 列宽自适应
        for (int i = 0; i < keyList.size(); i++) {
            sheet.setColumnWidth(i, maxWidth.get(i));
        }

        // 设置表格内容
        for (int i = 0; i < excelParam.getList().size(); i++) {

            Row row = sheet.createRow(i + startRow + 1);
            row.setHeight((short) (25 * 18));
            for (int j = 0; j < map.size(); j++) {
                Class<?> attrType = BeanUtils.findPropertyType(attMap.get(Integer.toString(j)),
                        new Class[]{obj.getClass()});
                Object value = getAttrVal(excelParam.getList().get(i), attMap.get(Integer.toString(j)), attrType);
                if (null == value) {
                    value = "";
                }
                Cell rowCell = row.createCell(j);
                rowCell.setCellStyle(cellStyle);
                rowCell.setCellValue(value.toString());
            }
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = excelParam.getFileName();
        if (StringUtils.isEmpty(newFileName)) {
            newFileName = df.format(new Date());
        }
        // 输出Excel文件
        try {
            if (excelParam.getResponse() != null) {
                OutputStream outstream = excelParam.getResponse().getOutputStream();
                excelParam.getResponse().reset();
                excelParam.getResponse().setHeader("Content-disposition",
                        "attachment; filename=" + new String(newFileName.getBytes(), "iso-8859-1") + ".xlsx");
                excelParam.getResponse().setContentType("application/x-download");
                wb.write(outstream);
                outstream.flush();
                outstream.close();
            } else {
                FileOutputStream out = new FileOutputStream(excelParam.getOutFilePath());
                wb.write(out);
                out.flush();
                out.close();
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("导出失败！" + e);
        } catch (IOException e) {
            throw new IOException("导出失败！" + e);
        }
    }

    public static void commonExportExcel2(AbstractExcelParam abstractExcelParam) throws Exception {


        // 创建HSSFWorkbook对象(excel的文档对象)
        XSSFWorkbook wb = new XSSFWorkbook();

        int count = 1;
        for (ExcelParam excelParam : abstractExcelParam.getList()) {
            Map<String, String> map = getMap(excelParam.getClazz());
            List<String> keyList = null;
            if (StringUtils.isEmpty(excelParam.getKeyValue())) {
                keyList = getList(excelParam.getClazz());
            } else {
                keyList = getList(excelParam.getKeyValue());
            }
            Object obj = excelParam.getClazz().newInstance();

            // 建立新的sheet对象（excel的表单）
            XSSFSheet sheet = wb.createSheet(excelParam.getSheetName() != null ? excelParam.getSheetName() : "sheet" + count);


            //添加水印
            if (StringUtils.isNotEmpty(excelParam.getWaterMark())) {
                addWaterMark(wb, sheet, excelParam.getWaterMark());
            }

            count++;
            // 头部样式
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setWrapText(true);
            // 单元格样式
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            //存储最大列宽
            Map<Integer, Integer> maxWidth = new HashMap<Integer, Integer>(20);
            // 存储属性信息
            Map<String, String> attMap = new HashMap(20);
            //如果有表头，字段头及表格创建行开始行
            int startRow = 0;
            //是否创建表头
            if (StringUtils.isNotEmpty(excelParam.getHeaderName())) {
                Row rowHeader = sheet.createRow(0);
                rowHeader.setHeight((short) (25 * 25));
                rowHeader.setRowStyle(headerStyle);
                Cell rowCell = rowHeader.createCell(0);
                rowCell.setCellStyle(headerStyle);
                rowCell.setCellValue(excelParam.getHeaderName());
                CellRangeAddress cra = new CellRangeAddress(0, 0, 0, (keyList.size() - 1));
                sheet.addMergedRegion(cra);
                startRow = 1;
            }
            // 在sheet里创建第x(取决于是否有表头，如果有表头，则在第2行创建，否则在第一行创建)行为表头，参数为行索引(excel的行)
            Row rowHeader = sheet.createRow(startRow);
            rowHeader.setHeight((short) (25 * 20));
            rowHeader.setRowStyle(headerStyle);

            // 设置表head
            int index = 0;
            for (String key : keyList) {
                Cell rowCell = rowHeader.createCell(index);
                rowCell.setCellStyle(headerStyle);
                rowCell.setCellValue(key);
                attMap.put(Integer.toString(index), map.get(key));
                maxWidth.put(index, rowCell.getStringCellValue().getBytes().length * 256 + 200);
                index++;
            }
            // 列宽自适应
            for (int i = 0; i < keyList.size(); i++) {
                sheet.setColumnWidth(i, maxWidth.get(i));
            }

            // 设置表格内容
            for (int i = 0; i < excelParam.getList().size(); i++) {

                Row row = sheet.createRow(i + startRow + 1);
                row.setHeight((short) (25 * 18));
                for (int j = 0; j < map.size(); j++) {
                    Class<?> attrType = BeanUtils.findPropertyType(attMap.get(Integer.toString(j)),
                            new Class[]{obj.getClass()});
                    Object value = getAttrVal(excelParam.getList().get(i), attMap.get(Integer.toString(j)), attrType);
                    if (null == value) {
                        value = "";
                    }
                    Cell rowCell = row.createCell(j);
                    rowCell.setCellStyle(cellStyle);
                    rowCell.setCellValue(value.toString());
                }
            }
        }


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = abstractExcelParam.getFileName();
        if (StringUtils.isEmpty(newFileName)) {
            newFileName = df.format(new Date());
        }
        // 输出Excel文件
        try {
            if (abstractExcelParam.getResponse() != null) {
                OutputStream outstream = abstractExcelParam.getResponse().getOutputStream();
                abstractExcelParam.getResponse().reset();
                abstractExcelParam.getResponse().setHeader("Content-disposition",
                        "attachment; filename=" + new String(newFileName.getBytes(), "iso-8859-1") + ".xlsx");
                abstractExcelParam.getResponse().setContentType("application/x-download");
                wb.write(outstream);
                outstream.flush();
                outstream.close();
            } else {
                FileOutputStream out = new FileOutputStream(abstractExcelParam.getOutFilePath());
                wb.write(out);
                out.flush();
                out.close();
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("导出失败！" + e);
        } catch (IOException e) {
            throw new IOException("导出失败！" + e);
        }
    }

    public static void setter(Object obj, String att, Object value, Class<?> type, int row, int col, Object key)
            throws Exception {
        try {
            Method method = obj.getClass().getMethod("set" + StringUtil.toUpperCaseFirstOne(att), type);
            method.invoke(obj, value);
        } catch (Exception e) {
            throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 赋值异常  " + e);
        }
    }


    public static Object getAttrVal(Object obj, String att, Class<?> attType) throws Exception {
        try {
            Method method = obj.getClass().getMethod("get" + StringUtil.toUpperCaseFirstOne(att));
            Object value = method.invoke(obj);
            if (attType == Date.class) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                value = sdf.format(value);
            }
            return value;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 得到Excel列的值
     *
     * @param cell     、
     * @param obj      、
     * @param attr     、
     * @param attrType 、
     * @param row      、
     * @param col      、
     * @param key      、
     */
    public static void getValue(Cell cell, Object obj, String attr, Class attrType, int row, int col, Object key)
            throws Exception {
        Object val = null;

        if (cell.getCellType() == CellType.BOOLEAN) {
            val = cell.getBooleanCellValue();

        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    if (attrType == String.class) {
                        val = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                    } else {
                        val = dateConvertFormat(sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())));
                    }
                } catch (ParseException e) {
                    throw new Exception("第" + (row + 1) + " 行  " + (col + 1) + "列   属性：" + key + " 日期格式转换错误  ");
                }
            } else {
                if (attrType == String.class) {
                    cell.setCellType(CellType.STRING);
                    val = cell.getStringCellValue();
                } else if (attrType == BigDecimal.class) {
                    val = new BigDecimal(cell.getNumericCellValue());
                } else if (attrType == long.class) {
                    val = (long) cell.getNumericCellValue();
                } else if (attrType == Double.class) {
                    val = cell.getNumericCellValue();
                } else if (attrType == Float.class) {
                    val = (float) cell.getNumericCellValue();
                } else if (attrType == int.class || attrType == Integer.class) {
                    val = (int) cell.getNumericCellValue();
                } else if (attrType == Short.class) {
                    val = (short) cell.getNumericCellValue();
                } else {
                    val = cell.getNumericCellValue();
                }
            }

        } else if (cell.getCellType() == CellType.STRING) {
            if (attrType.equals(double.class) || attrType.equals(Double.class)) {
                val = Double.parseDouble(cell.getStringCellValue());
            } else if (attrType == BigDecimal.class) {
                val = new BigDecimal(cell.getStringCellValue());
            } else if (attrType == long.class || attrType == Long.class) {
                val = Long.valueOf(cell.getStringCellValue());
            } else if (attrType == Float.class) {
                val = Float.valueOf(cell.getStringCellValue());
            } else if (attrType == int.class || attrType == Integer.class) {
                val = Integer.parseInt(String.valueOf(cell.getStringCellValue()));
            } else if (attrType == Short.class) {
                val = Short.valueOf(cell.getStringCellValue());
            } else if (attrType == Date.class) {
                val = dateConvertFormat(cell.getStringCellValue());
            } else {
                val = cell.getStringCellValue();
            }
        }
        setter(obj, attr, val, attrType, row, col, key);
    }

    /**
     * String类型日期转为Date类型
     *
     * @param dateStr 、
     * @return java.util.Date
     * @throws Exception 、
     */
    public static Date dateConvertFormat(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(dateStr);
    }

    protected static class PoiWriter implements Runnable {

        private final CountDownLatch doneSignal;

        private Sheet sheet;

        private int start;

        private int end;

        private List list;

        private Map<String, String> map;

        private Map<String, String> attMap;

        private Object obj;


        /**
         * sheet的row使用treeMap存储的，是非线程安全的，所以在创建row时需要进行同步操作。
         *
         * @param sheet 、
         * @param rownum 、
         * @return 、
         */
        private static synchronized Row getRow(Sheet sheet, int rownum) {
            return sheet.createRow(rownum);
        }

        public PoiWriter(CountDownLatch doneSignal, Sheet sheet, int start, int end, List list, Map<String, String> map, Map<String, String> attMap, Object object) {
            this.doneSignal = doneSignal;
            this.sheet = sheet;
            this.start = start;
            this.end = end;
            this.list = list;
            this.map = map;
            this.attMap = attMap;
            this.obj = object;
        }

        @Override
        public void run() {
            int k = start;
            try {
                //while (k <= end) {
                //Row row = getRow(sheet, k);

                for (Object o : list) {
                    Row row = getRow(sheet, k);
                    for (int j = 0; j < map.size(); j++) {
                        Class<?> attrType = BeanUtils.findPropertyType(attMap.get(Integer.toString(j)),
                                new Class[]{obj.getClass()});
                        Object value = getAttrVal(o, attMap.get(Integer.toString(j)), attrType);
                        if (null == value) {
                            value = "";
                        }
                        row.createCell(j).setCellValue(value.toString());
                        //style.setAlignment(HorizontalAlignment.CENTER);
                    }
                    ++k;
                }
                //}
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                doneSignal.countDown();
                System.out.println("start: " + start + " end: " + end
                        + " Count: " + doneSignal.getCount());
            }
        }

    }


    public static void templateWrite(ExcelParam excelParam) {

        Map<String, String> resultMap = new HashMap(20);

        //根据路径获取文件
        File file = new File(excelParam.getFilePath());
        //定义输入流对象
        FileInputStream excelFileInputStream;

        try {
            excelFileInputStream = new FileInputStream(file);
            // 拿到文件转化为JavaPoi可操纵类型
            Workbook workbook = WorkbookFactory.create(excelFileInputStream);
            excelFileInputStream.close();
            ////获取excel表格
            Sheet sheet = workbook.getSheetAt(0);

            // 循环行Row
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {

                Row hssfRow = sheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                // 循环列Cell
                for (int cellNum = 0; cellNum < hssfRow.getLastCellNum(); cellNum++) {

                    Cell hssfCell = hssfRow.getCell(cellNum);
                    if (hssfCell == null || StringUtils.isEmpty(hssfCell.getStringCellValue())) {
                        continue;
                    }

                    String tempCellValue = sheet.getRow(rowNum).getCell(cellNum).getStringCellValue();
                    tempCellValue = StringUtils.remove(tempCellValue, (char) 160);
                    tempCellValue = tempCellValue.trim();


                    String pattern = "(?s)^#.*}$";
                    if (Pattern.matches(pattern, tempCellValue)) {
                        String variableName = tempCellValue.substring(2, tempCellValue.length() - 1);
                        resultMap.put(variableName, hssfCell.getAddress().toString());
                    }
                }
            }

            //存储字段和字段对应的值
            Map<String, Object> filedValMap = getMap(excelParam.getObj());
            for (String key : resultMap.keySet()) {
                //获取单元格的row和cell
                CellAddress address = new CellAddress(resultMap.get(key));
                // 获取行
                Row row = sheet.getRow(address.getRow());
                // 获取列
                Cell cell = row.getCell(address.getColumn());
                //设置单元的值
                cell.setCellValue(filedValMap.get(key) == null ? null : filedValMap.get(key).toString());
            }


            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String newFileName = excelParam.getFileName();
            if (StringUtils.isEmpty(newFileName)) {
                newFileName = df.format(new Date());
            }
            // 输出Excel文件
            try {
                if (excelParam.getResponse() != null) {
                    OutputStream outstream = excelParam.getResponse().getOutputStream();
                    excelParam.getResponse().reset();
                    excelParam.getResponse().setHeader("Content-disposition",
                            "attachment; filename=" + new String(newFileName.getBytes(), "iso-8859-1") + ".xlsx");
                    excelParam.getResponse().setContentType("application/x-download");
                    workbook.write(outstream);
                    outstream.flush();
                    outstream.close();
                } else {
                    FileOutputStream out = new FileOutputStream(excelParam.getOutFilePath());
                    workbook.write(out);
                    out.flush();
                    out.close();
                }
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("导出失败！" + e);
            } catch (IOException e) {
                throw new IOException("导出失败！" + e);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

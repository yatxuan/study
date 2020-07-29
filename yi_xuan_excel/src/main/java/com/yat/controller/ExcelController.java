package com.yat.controller;

import com.yat.model.PhoneModel;
import com.yat.util.AbstractExcelParam;
import com.yat.util.ExcelParam;
import com.yat.util.ExcelUtil;
import com.yat.util.ExcelUtilBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat-Xuan
 * @date 2020/7/29 14:34
 */
@Controller
public class ExcelController {

    @GetMapping(value = "/")
    public String ss1() {
        return "index";
    }

    /**
     * 导入
     *
     * @param file 、
     * @return 、
     * @throws Exception 、
     */
    @PostMapping(value = "/test")
    @ResponseBody
    public List<PhoneModel> testImport(MultipartFile file) throws Exception {
        return ExcelUtil.readXls(file.getBytes(), PhoneModel.class);
    }

    /**
     * 导入：注解测试
     */
    @PostMapping(value = "/test2")
    @ResponseBody
    public List<PhoneModel> testImport2() throws Exception {
        return ExcelUtil.readXls("D://test.xlsx", PhoneModel.class);
    }


    /**
     * 导入：注解测试
     */
    @PostMapping(value = "/test1")
    @ResponseBody
    public List<PhoneModel> testImport1(MultipartFile file) throws Exception {
        return ExcelUtil.readXls(file.getBytes(), PhoneModel.class);
    }

    /**
     * 导出
     */
    @GetMapping(value = "/export")
    public void testExport(HttpServletResponse response,
                           @RequestParam(value = "num", required = false, defaultValue = "10") int num) throws Exception {

        // 开始时间
        long beginTime = System.currentTimeMillis();
        List<PhoneModel> list = getList(num);
        // 文件名称
        String fileName = "测试Excel导出";
        // 背景水印
        String waterMark = "Yat_Xuan";
        // ExcelUtil.exportExcelOutputStream(response, list, PhoneModel.class, fileName);
        ExcelUtil.exportExcelOutputStream(response, list, PhoneModel.class, fileName, waterMark);

        long endTime = (System.currentTimeMillis() - beginTime);

        System.out.println("耗时:" + endTime + "毫秒");
        System.out.println("耗时:" + endTime / 1000 + "秒");


    }

    /**
     * 导出：带背景水印
     */
    @GetMapping(value = "/exportSheet")
    public void testExportSheet(HttpServletResponse response2) throws Exception {

        List<PhoneModel> list = getList(10);

        List<PhoneModel> list2 = getList(20);


        ExcelParam excelParam = new ExcelParam();
        excelParam.setSheetName("hello1");
        excelParam.setClazz(PhoneModel.class);
        excelParam.setList(list);
        excelParam.setWaterMark("hello1");

        ExcelParam excelParam1 = new ExcelParam();
        excelParam1.setSheetName("口苦扣扣");
        excelParam1.setClazz(PhoneModel.class);
        excelParam1.setList(list2);
        excelParam1.setWaterMark("口苦扣扣");

        AbstractExcelParam excelParamAbstract = new AbstractExcelParam() {

            private static final long serialVersionUID = -4020409693904438529L;

            @Override
            public List<ExcelParam> getList() {

                List<ExcelParam> excelParamList = new ArrayList<>();
                excelParamList.add(excelParam);
                excelParamList.add(excelParam1);
                return excelParamList;
            }

            @Override
            public HttpServletResponse getResponse() {
                return response2;
            }
        };
        ExcelUtil.exportExcelOutputStream(excelParamAbstract);
    }

    /**
     * 导出带表头
     */
    @GetMapping(value = "/exportHeader")
    public void testExportHeader(HttpServletResponse response) throws Exception {

        List<PhoneModel> list = getList(10);

        ExcelUtil.exportExcelOutputStream(response, list, PhoneModel.class, "导出标题多萨法撒旦法带表头", true);

        ExcelParam excelParam = new ExcelParam();
        excelParam.setWaterMark("水印文字");
        //从表头的第二行开始读取
        excelParam.setRowNumIndex(2);
        excelParam.setHeaderName("要设置的表头文字");

        //读取
        ExcelUtilBase.getResult(excelParam);
        //导出
        ExcelUtilBase.commonExportExcel(excelParam);

    }

    /**
     * 模版导出
     */
    @GetMapping(value = "/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        PhoneModel model = new PhoneModel();
        model.setColor("金色");
        model.setPhoneName("苹果12 S");
        model.setPrice(9999);
        model.setSaleTime(new Date());
        ExcelUtil.templateWrite(response, "D:\\导出标题多萨法撒旦法带表头.xlsx", model, "ce测试时");
    }

    @GetMapping(value = "/json")
    @ResponseBody
    public List<PhoneModel> ss() {

        List<PhoneModel> list = new ArrayList<>(1);

        PhoneModel model = new PhoneModel();
        model.setColor("土豪金");
        model.setPhoneName("Iphone X");
        model.setSaleTime(new Date());

        list.add(model);

        return list;
    }

    /**
     * 测试数据
     *
     * @param num 数据数量
     * @return 、
     */
    private List<PhoneModel> getList(int num) {
        List<PhoneModel> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            PhoneModel model = new PhoneModel();
            model.setNum((i + 1));
            model.setColor("金色" + i);
            model.setPhoneName("苹果" + i + "S");
            model.setPrice(i);
            model.setSaleTime(new Date());
            list.add(model);
        }
        return list;
    }

}

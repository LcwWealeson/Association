package com.example.association.utils;

import com.example.association.vo.ApplyParticipationVO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

public class ExcelUtil {

    public static String exportSignInList(String[] titles, String eventName,String eventPlace,List<ApplyParticipationVO>
            applyParticipationVOList, String savePath,HttpServletResponse response){

        File excelFile = null;
        FileOutputStream fileOutputStream = null;
        String fileName=eventName+".xls";
        System.out.println(fileName);
        HSSFWorkbook workbook =null;
        OutputStream responseOut = null;

        try {

            //fileName = URLEncoder.encode(fileName,"UTF-8");若是要传到URL上，那就需要进行这一句，不然中文会出现乱码
            savePath +="\\src\\main\\resources\\templates\\";
            savePath = savePath + fileName;
            excelFile = new File(savePath);
            fileOutputStream = new FileOutputStream(excelFile);
            responseOut = response.getOutputStream();
            //创建excel
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("签到表");
            sheet.setColumnWidth(0, 10 * 256);
            sheet.setColumnWidth(1, 16 * 256);
            sheet.setColumnWidth(2, 16 * 256);
            sheet.setColumnWidth(3, 16 * 256);
            sheet.setColumnWidth(4, 16 * 256);
            sheet.setColumnWidth(5, 8  * 256);

            HSSFRow row =sheet.createRow(0);//创建标题行
            HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
            hssfCellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);

            HSSFCell cell = null;
            //sheet标题由titles决定
            for (int i=0;i<titles.length;i++){
                cell = row.createCell(i);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(hssfCellStyle);
            }
            //填充活动名称以及活动地点
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue(eventName);
            cell.setCellStyle(hssfCellStyle);
            cell = row.createCell(1);
            cell.setCellValue(eventPlace);
            cell.setCellStyle(hssfCellStyle);

            if (applyParticipationVOList !=null && !applyParticipationVOList.isEmpty()){
                int rowNumber = 1;
                //循环渲染每一行的活动人员信息
                for (ApplyParticipationVO participationVO:applyParticipationVOList){

                    for (int k = 2; k < titles.length; k++){
                        switch (k){
                            case 2:
                                row.createCell(k).setCellValue(participationVO.getParticipant().getUsername());
                                break;
                            case 3:
                                row.createCell(k).setCellValue(participationVO.getParticipant().getNickname());
                                break;
                            case 4:
                                row.createCell(k).setCellValue(participationVO.getParticipant().getGenderStr());
                                break;
                            case 5:
                                row.createCell(k).setCellValue(participationVO.getParticipant().getCollegeStr());
                                break;
                            case 6:
                                row.createCell(k).setCellValue(participationVO.getParticipant().getMajorStr());
                                break;
                            case 7:
                                row.createCell(k).setCellValue(participationVO.getParticipant().getClassFrom());
                                break;
                        }
                    }
                    row = sheet.createRow(++rowNumber);
                }
            }
            //将excel文件写入到文件系统
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            // 告诉浏览器用什么软件可以打开此文件
            //response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setContentType("application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            workbook.write(responseOut);
            responseOut.flush();
            responseOut.close();
        } catch (IOException e) {
            System.out.println("输出流错误！");
            e.printStackTrace();
        }
        return excelFile.getPath();
    }
}

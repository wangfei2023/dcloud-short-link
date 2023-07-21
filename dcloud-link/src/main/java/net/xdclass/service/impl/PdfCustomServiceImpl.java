/**
 * @project dcloud-short-link
 * @description 导出pdf模板
 * @author Administrator
 * @date 2023/7/19 0019 21:45:56
 * @version 1.0
 */

package net.xdclass.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.manage.ShortLinkManager;
import net.xdclass.model.ShortLinkDO;
import net.xdclass.service.PdfCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class PdfCustomServiceImpl  implements PdfCustomService {

    @Override
    public void generatorShortLinkDO(ShortLinkDO shortLinkDO, HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException {
        // 模板名称
        String templateName = "生成短链-模板.pdf";
        String path = "";
        // 获取操作系统名称，根据系统名称确定模板存放的路径
        String systemName = System.getProperty("os.name");
        if(systemName.toUpperCase().startsWith("WIN")){
            path = "D:/pdf/";
        }else {
            path = "/usr/local/pdf/";
        }
        // 生成导出PDF的文件名称
        String fileName = "生成短链码详情.pdf";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        // 设置响应头
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + fileName);
        OutputStream out = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper = null;
        PdfReader reader = null;
        try {
            // 保存到本地
            // out = new FileOutputStream(fileName);
            // 输出到浏览器端
            out = response.getOutputStream();
            // 读取PDF模板表单
            reader = new PdfReader(path + templateName);
            // 字节数组流，用来缓存文件流
            bos = new ByteArrayOutputStream();
            // 根据模板表单生成一个新的PDF
            stamper = new PdfStamper(reader, bos);
            // 获取新生成的PDF表单
            AcroFields form = stamper.getAcroFields();
            // 给表单生成中文字体，这里采用系统字体，不设置的话，中文显示会有问题
            BaseFont font = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            form.addSubstitutionFont(font);
            // 装配数据
            Map<String, Object> data = new HashMap<>(15);
            data.put("id", shortLinkDO.getId());
            data.put("groupId", shortLinkDO.getGroupId());
            data.put("title", shortLinkDO.getTitle());
            data.put("originalUrl", shortLinkDO.getOriginalUrl());
            data.put("domain", shortLinkDO.getDomain());
            data.put("code", shortLinkDO.getCode());
            data.put("sign", shortLinkDO.getSign());
            data.put("expired", shortLinkDO.getExpired());
            data.put("accountNo", shortLinkDO.getAccountNo());
            data.put("gmtCreate", shortLinkDO.getGmtCreate());
            data.put("gmtModified", shortLinkDO.getGmtModified());
            data.put("del", shortLinkDO.getDel());
            data.put("state", shortLinkDO.getState());
            data.put("linkType", shortLinkDO.getLinkType());
          //  data.put("linkType", shortLinkDO.getLinkType());


            // 遍历data，给pdf表单赋值
            for(String key : data.keySet()){
                // 设置普通文本数据else {
                    form.setField(key, data.get(key).toString());

            }
            // 表明该PDF不可修改
            stamper.setFormFlattening(true);
            // 关闭资源
            stamper.close();
            // 将ByteArray字节数组中的流输出到out中（即输出到浏览器）
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            log.info("*****************************PDF导出成功*********************************");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}



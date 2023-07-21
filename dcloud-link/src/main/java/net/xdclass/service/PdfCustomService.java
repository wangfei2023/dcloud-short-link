package net.xdclass.service;

import net.xdclass.model.ShortLinkDO;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
 * @since 2021/7/11 16:12
 */
public interface PdfCustomService {

    /**
     * 生成准考证PDF
     * @param shortLinkCode 准考证信息
     * @param response 响应
     */
    void generatorShortLinkDO( ShortLinkDO shortLinkDO, HttpServletResponse response) throws UnsupportedEncodingException, FileNotFoundException;
}

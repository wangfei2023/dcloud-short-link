package net.xdclass.controller;


import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.service.ShortLinkService;
import net.xdclass.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MrWang
 * @since 2023-05-23
 */
@RestController
@RequestMapping("/api/link/v1")
public class ShortLinkController {
    @Autowired
    private ShortLinkService shortLinkService;
    @PostMapping("/add")
     public JsonData createShortLink(ShortLinkAddRequest request){
        JsonData jsonData= shortLinkService.createShortLink(request);
        return jsonData;
     }
}


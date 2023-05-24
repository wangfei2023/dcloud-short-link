package net.xdclass.service;

import net.xdclass.controller.request.LinkGroupAddRequest;
import net.xdclass.utils.JsonData;

public interface LinkGroupService {
    /*
    新增分组;
     */
    int add(LinkGroupAddRequest linkGroupAddRequest);
}

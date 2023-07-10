/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/10 0010 19:55:25
 * @version 1.0
 */

package net.xdclass.service;

import net.xdclass.model.EventMessage;

public interface TrafficService {
    void handlerTraffcMessage(EventMessage eventMessage);
}



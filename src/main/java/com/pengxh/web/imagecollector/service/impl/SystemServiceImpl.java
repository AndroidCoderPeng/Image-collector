package com.pengxh.web.imagecollector.service.impl;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSON;
import com.pengxh.web.imagecollector.dto.*;
import com.pengxh.web.imagecollector.service.ISystemService;
import com.pengxh.web.imagecollector.utils.SystemHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author a203
 */
@Slf4j
@Service
public class SystemServiceImpl implements ISystemService {

    public SystemServiceImpl() {

    }

    @Override
    public void obtainLinuxServerInfo() {
        Connection connection = SystemHelper.connectServer("111.198.10.15", 11301, "root", "Casic203");
        try {
            /**
             * 获取服务器总内存、使用、空闲等情况
             * */
            MemoryUsageDTO memoryUsage = SystemHelper.getMemoryUsage(connection.openSession());

            /**
             * 获取服务器硬盘、使用、空闲等情况
             * */
            List<StorageUsageDTO> storageUsage = SystemHelper.getStorageUsage(connection.openSession());

            /**
             * 获取CPU使用率、内存使用、IO读写情况
             * */
            CpuUsageDTO cpuUsage = SystemHelper.getCpuUsage(connection.openSession());

            /**
             * 获取当前服务器进程信息
             * */
            List<RunningJavaDTO> javaProcess = SystemHelper.getRunningJavaProcess(connection.openSession());

            SystemDTO systemDTO = new SystemDTO();
            systemDTO.setMemoryUsage(memoryUsage);
            systemDTO.setStorageUsage(storageUsage);
            systemDTO.setCpuUsage(cpuUsage);
            systemDTO.setJavaProcess(javaProcess);
            log.info(JSON.toJSONString(systemDTO));

            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

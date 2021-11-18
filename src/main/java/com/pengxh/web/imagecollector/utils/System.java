package com.pengxh.web.imagecollector.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.pengxh.web.imagecollector.dto.CpuUsageDTO;
import com.pengxh.web.imagecollector.dto.MemoryUsageDTO;
import com.pengxh.web.imagecollector.dto.RunningJavaDTO;
import com.pengxh.web.imagecollector.dto.StorageUsageDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统信息
 *
 * @author a203
 */
@Slf4j
public class System {

    /**
     * 判断IP地址是否可用
     */
    public static boolean isConnected(String address) {
        int timeOut = 5000;
        try {
            return InetAddress.getByName(address).isReachable(timeOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 连接到并登陆到指定的HOST
     */
    public static Connection connectServer(String host, int port, String user, String pwd) {
        Connection conn = new Connection(host, port);
        try {
            conn.connect();
            boolean isConnected = conn.authenticateWithPassword(user, pwd);
            log.info("isConnected: " + isConnected);
            if (isConnected) {
                return conn;
            } else {
                throw new RuntimeException("用户名密码不正确");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取服务器总内存、使用、空闲等情况
     * total：总计物理内存的大小
     * used：已使用多大
     * free：可用有多少
     * Shared：多个进程共享的内存总额
     * Buffers/cached：磁盘缓存的大小
     */
    public static MemoryUsageDTO getMemoryUsage(Session session) {
        MemoryUsageDTO dto = new MemoryUsageDTO();
        try {
            session.execCommand("free");
//            session.execCommand("free -h");

            String result = parseResult(session.getStdout());
            String[] split = result.split("\n");

            String memValue = split[1];
            List<String> memData = formatValue(memValue.split(" "));
            MemoryUsageDTO.Memory memory = new MemoryUsageDTO.Memory();
            memory.setTotal(memData.get(0));
            memory.setUsed(memData.get(1));
            memory.setFree(memData.get(2));
            memory.setShared(memData.get(3));
            memory.setCache(memData.get(4));
            memory.setCache(memData.get(4));
            memory.setAvailable(memData.get(5));
            dto.setMemory(memory);

            String swapValue = split[2];
            List<String> swapData = formatValue(swapValue.split(" "));
            MemoryUsageDTO.Swap swap = new MemoryUsageDTO.Swap();
            swap.setTotal(swapData.get(0));
            swap.setUsed(swapData.get(1));
            swap.setFree(swapData.get(2));
            dto.setSwap(swap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return dto;
    }

    public static List<StorageUsageDTO> getStorageUsage(Session session) {
        List<StorageUsageDTO> list = new ArrayList<>();
        try {
            session.execCommand("df -h");

            String result = parseResult(session.getStdout());
            String[] split = result.split("\n");
            for (int i = 1; i < split.length; i++) {
                String value = split[i];
                String[] valueArray = value.split(" ");
                List<String> stringValue = formatStringValue(valueArray);

                StorageUsageDTO dto = new StorageUsageDTO();
                dto.setSystemName(stringValue.get(0));
                dto.setTotal(stringValue.get(1));
                dto.setUsed(stringValue.get(2));
                dto.setAvailable(stringValue.get(3));
                dto.setUsage(stringValue.get(4));
                dto.setPath(stringValue.get(5));

                list.add(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    /**
     * 获取CPU使用率、内存使用、IO读写情况
     */
    public static CpuUsageDTO getCpuUsage(Session session) {
        CpuUsageDTO dto = new CpuUsageDTO();
        try {
            session.execCommand("top -bn 1 -i -c");

            String result = parseResult(session.getStdout());
            String[] split = result.split("\n");

            String cpuValue = split[2];
            List<String> cpuData = formatStringValue(cpuValue.split(" "));

            dto.setUserRatio(cpuData.get(1) + "%");
            dto.setSystemRatio(cpuData.get(3) + "%");
            dto.setNiceRatio(cpuData.get(5) + "%");
            dto.setFreeRatio(cpuData.get(7) + "%");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return dto;
    }

    /**
     * 获取当前服务器进程信息
     */
    public static List<RunningJavaDTO> getRunningJavaProcess(Session session) {
        List<RunningJavaDTO> dtoList = new ArrayList<>();
        try {
            session.execCommand("ps -aux | grep java");

            String result = parseResult(session.getStdout());
            String[] roots = result.split("\n");
            for (String root : roots) {
                RunningJavaDTO dto = new RunningJavaDTO();

                String s = root.replace("java -jar", "");
                String[] split = s.split(" ");
                List<String> value = formatStringValue(split);

                dto.setOwner(value.get(0));
                dto.setPid(value.get(1));
                dto.setCpu(value.get(2) + "%");
                dto.setMemory(value.get(3) + "%");
                dto.setVtMemory(value.get(4));
                dto.setPyMemory(value.get(5));
                dto.setStatus(value.get(7));
                dto.setStartDate(value.get(8));
                dto.setTotalTime(value.get(9));
                dto.setProcessName(value.get(10));

                dtoList.add(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return dtoList;
    }

    /**
     * 解析命令结果
     */
    private static String parseResult(InputStream inputStream) throws IOException {
        // 读取输出流内容
        InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    private static List<String> formatValue(String[] array) {
        List<String> data = new ArrayList<>();
        for (String s : array) {
            if (StringHelper.isNumber(s)) {
                data.add(s);
            }
        }
        return data;
    }

    private static List<String> formatStringValue(String[] array) {
        List<String> data = new ArrayList<>();
        for (String s : array) {
            if (!"".equals(s)) {
                data.add(s);
            }
        }
        return data;
    }
}

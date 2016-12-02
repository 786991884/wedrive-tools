package com.terminal.func;


import com.terminal.cache.CacheManager;
import com.terminal.entity.RegisterData;
import com.terminal.entity.TerminalStatus;
import com.terminal.log.ResultStatisticalDataLog;
import com.terminal.util.ConfigUtils;
import com.terminal.util.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Component
public class DataInitialization {

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private CacheManager cacheManager;

    public void init() {
        //终端初始化
        for (int i = 0; i < cacheManager.getTerminals().length; i++) {
            long terminal = configUtils.getTerminalIdentifyStart() + i;
            cacheManager.getTerminals()[i] = terminal;
        }

        //终端注册数据
        for (int i = 0; i < cacheManager.getTerminals().length; i++) {
            RegisterData registerData = new RegisterData();
            registerData.setProvinceId(configUtils.getProvinceCode());
            registerData.setCityId(configUtils.getCityCode());
            registerData.setManufacturerId(configUtils.getTerminalManufacturer());
            registerData.setTerminalType(configUtils.getTerminalType());
            registerData.setTerminalIdentify(configUtils.getTerminalNoPro() + Convert.preFillZero(configUtils.getTerminalNoStart() + i, configUtils.getTerminalNoSufLength()));
//			registerData.setTerminalIdentify(Convert.preFillZero(ServerConfig.terminalNoPro,7));
            registerData.setVehicleColor(configUtils.getVehicleColor());
            registerData.setVehicleLicense(configUtils.getVehicleLicensePre() + Convert.preFillZero(i, configUtils.getVehicleLicenseSufLength()));
            cacheManager.setRegisterDataCache(cacheManager.getTerminals()[i], registerData);
        }

        File file = new File("cache" + File.separator + "auth.dat");
        if (file.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("cache" + File.separator + "auth.dat"));
                cacheManager.setAuthCode((Map<Long, String>) in.readObject());
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        //终端状态缓存初始化
        for (int i = 0; i < cacheManager.getTerminals().length; i++) {
            long terminalId = cacheManager.getTerminals()[i];
            TerminalStatus status = new TerminalStatus();
            status.setAlarm(false);
            status.setDisconnect(false);
            status.setError(false);
            status.setCancel(false);
            status.setAuth(false);
            String auth = cacheManager.getAuthCode().get(terminalId);
            if (auth != null && !auth.equals("")) {
                status.setTerminalAuthCode(auth);
            }
            cacheManager.getTerminalCache().put(terminalId, status);
        }

        //消息集合
//        cacheManager.setCommand(0x8001, new PlatformCommonRes());
//        cacheManager.setCommand(0x0002, new HeartBeat());
//        cacheManager.setCommand(0x0100, new Register());
//        cacheManager.setCommand(0x8100, new RegisterRes());
//        cacheManager.setCommand(0x0102, new Authentication());
//        cacheManager.setCommand(0x0200, new Location());
//        cacheManager.setCommand(0x0003, new Cancel());
//        cacheManager.setCommand(0x8702, new ReportDriverInfo());
//        cacheManager.setCommand(0x8F40, new CanTamperControl());
//        cacheManager.setCommand(0x8201, new LocationQuery());
//        cacheManager.setCommand(0x8300, new DispatchMessage());
        //写日志头
        ResultStatisticalDataLog.writeLogTitle();
    }
}

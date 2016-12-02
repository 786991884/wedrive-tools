package com.simulation.terminal.func;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.communication.protocol.Authentication;
import com.simulation.terminal.communication.protocol.CanTamperControl;
import com.simulation.terminal.communication.protocol.Cancel;
import com.simulation.terminal.communication.protocol.DispatchMessage;
import com.simulation.terminal.communication.protocol.HeartBeat;
import com.simulation.terminal.communication.protocol.Location;
import com.simulation.terminal.communication.protocol.LocationQuery;
import com.simulation.terminal.communication.protocol.PlatformCommonRes;
import com.simulation.terminal.communication.protocol.Register;
import com.simulation.terminal.communication.protocol.RegisterRes;
import com.simulation.terminal.communication.protocol.ReportDriverInfo;
import com.simulation.terminal.entity.RegisterData;
import com.simulation.terminal.entity.TerminalStatus;
import com.simulation.terminal.log.ResultStatisticalDataLog;
import com.simulation.terminal.log.StatisticalDataLog;
import com.simulation.terminal.util.Convert;
import com.simulation.terminal.util.ServerConfig;

public class DataInitialization {
	
	@SuppressWarnings("unchecked")
	public static void init(){
		//终端初始化
		for(int i=0; i<CacheManager.getTerminals().length; i++){
			long terminal = ServerConfig.terminalIdentifyStart + i;
			CacheManager.getTerminals()[i] = terminal;
		}
		
		//终端注册数据
		for(int i=0; i<CacheManager.getTerminals().length; i++){
			RegisterData registerData = new RegisterData();
			registerData.setProvinceId(ServerConfig.provinceCode);
			registerData.setCityId(ServerConfig.cityCode);
			registerData.setManufacturerId(ServerConfig.terminalManufacturer);
			registerData.setTerminalType(ServerConfig.terminalType);
			registerData.setTerminalIdentify(ServerConfig.terminalNoPro + Convert.preFillZero(ServerConfig.terminalNoStart+i, ServerConfig.terminalNoSufLength));
//			registerData.setTerminalIdentify(Convert.preFillZero(ServerConfig.terminalNoPro,7));
			registerData.setVehicleColor(ServerConfig.vehicleColor);
			registerData.setVehicleLicense(ServerConfig.vehicleLicensePre + Convert.preFillZero(i, ServerConfig.vehicleLicenseSufLenth));
			CacheManager.setRegisterDataCache(CacheManager.getTerminals()[i], registerData);
		}
		
		File file = new File("cache\\auth.dat");
		if(file.exists()){
			try {
				ObjectInputStream in  = new ObjectInputStream(new FileInputStream("cache\\auth.dat"));
				CacheManager.setAuthCode((Map<Long, String>)in.readObject());
				in.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//终端状态缓存初始化
		for(int i=0; i<CacheManager.getTerminals().length; i++){
			long terminalId = CacheManager.getTerminals()[i];
			TerminalStatus status = new TerminalStatus();
			status.setAlarm(false);
			status.setDisconnect(false);
			status.setError(false);
			status.setCancel(false);
			status.setAuth(false);
			String auth = CacheManager.getAuthCode().get(terminalId);
			if (auth != null && !auth.equals("")){
				status.setTerminalAuthCode(auth);
			}
			CacheManager.getTerminalCache().put(terminalId, status);
		}
		
		//消息集合
		CacheManager.setCommand(0x8001, new PlatformCommonRes());
		CacheManager.setCommand(0x0002, new HeartBeat());
		CacheManager.setCommand(0x0100, new Register());
		CacheManager.setCommand(0x8100, new RegisterRes());
		CacheManager.setCommand(0x0102, new Authentication());
		CacheManager.setCommand(0x0200, new Location());
		CacheManager.setCommand(0x0003, new Cancel());
		CacheManager.setCommand(0x8702, new ReportDriverInfo());
		CacheManager.setCommand(0x8F40, new CanTamperControl());
		CacheManager.setCommand(0x8201, new LocationQuery());
		CacheManager.setCommand(0x8300, new DispatchMessage());
		//写日志头
		ResultStatisticalDataLog.writeLogTitle();
		StatisticalDataLog.writeLogTitle();
	}
}

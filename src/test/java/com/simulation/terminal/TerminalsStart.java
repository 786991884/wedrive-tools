package com.simulation.terminal;

import com.simulation.terminal.cache.CacheManager;
import com.simulation.terminal.cache.StatisticalData;
import com.simulation.terminal.communication.Connector;
import com.simulation.terminal.communication.MessageListener;
import com.simulation.terminal.func.DataInitialization;
import com.simulation.terminal.func.GenerateTerminalData;
import com.simulation.terminal.func.LinkReconnect;
import com.simulation.terminal.util.ServerConfig;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Date;


public class TerminalsStart {
    public static Logger logger = Logger.getLogger(TerminalsStart.class);

    public static void main(String[] args) {
        logger.info("模拟终端开始启动...");
        DataInitialization.init();
        establishConnecter();
        long current = new Date().getTime() / 1000;
        StatisticalData.startDate = current;
        StatisticalData.stageStartDate = current;
        Thread send = new Thread(new GenerateTerminalData());
        Thread read = new Thread(new MessageListener());
//		Thread log = new Thread(new LogManager());
        Thread reconnect = new Thread(new LinkReconnect());
        send.start();
        read.start();
//		log.start();
        reconnect.start();
        saveAuthCode();
    }

    public static void establishConnecter() {
        for (int i = 0; i < CacheManager.getTerminals().length; i++) {
            long terminalId = CacheManager.getTerminals()[i];
            Connector connector = new Connector();
            connector.connecting(terminalId, ServerConfig.serverTcpIP, ServerConfig.serverTcpPort);
        }
        logger.info("终端链路建立完毕...");
    }

    public static void saveAuthCode() {
        while (true) {
            try {
                Thread.sleep(10000);
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cache" + File.separator + "auth.dat"));
                //ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("auth.dat"));
                out.writeObject(CacheManager.getAuthCode());
                out.close();
                if (CacheManager.getAuthCode().size() >= ServerConfig.terminalNumber) {
                    break;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

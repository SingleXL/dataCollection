package com.cloud.dc.node.remotewin;

//import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.lang.reflect.Type;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by root on 7/21/15.
 */

/*<add key="flumeAddr" value="192.168.20.90"/>
        <add key="flumePort" value="8501"/>
        <add key="mesDriverIP" value="192.168.20.59"/>
        <add key="mesDriverPort" value="7655"/>
        <add key="machineConfigPath" value="E:\MLP5.0\conf\remote\remoteWindowsConfig.json"/>
        <add key="readConfSeconds" value="1"/>
        <add key="machineStatusFilePath" value="E:\MLP5.0\conf\remote\machineStatus.json"/>
        <add key="oldEventBatchSize" value="10"/>
        <add key="oldEventBatchSendIntervalMilliSecs" value="10"/>
        <add key="DataFilterDays" value="100"/>
        <add key="sendToFlume" value="1"/>
        <add key="localThriftServerPort" value="7911"/>
        <add key="EmptyEventSendSecs" value="60"/>
        <add key="securityCatFilePath" value="d:\test\log_categories_WinSecurity.txt"/>
        <add key="localTxtLogOpt" value="0"/>
        <add key="localTxtLogDir" value="d:\test\locallogs"/>
        <add key="localLogSourceType" value="test_source_type"/>
*/


public class WindowsAgentNodeConfig {

    Gson gson = new Gson();

    String totalCfg = "";
    String WinEventLogAgentexeCof = "";
    String remoteWindowsCfg="";

    String flumeAddr = "flumeAddr";
        Integer flumePort = 0;
    String mesDriverIP="mesDriverIP";
        Integer mesDriverPort=0;
    String machineConfigPath = "machineConfigPath";
        Integer readConfSeconds = 0;
    String machineStatusFilePath="machineStatusFilePath";
        Integer oldEventBatchSize=0;
        Integer oldEventBatchSendIntervalMilliSecs=0;
        Integer DataFilterDays=0;
        Integer sendToFlume=0;
        Integer localThriftServerPort=0;
        Integer EmptyEventSendSecs=0;
    String securityCatFilePath="securityCatFilePath";
        Integer localTxtLogOpt=0;
    String localTxtLogDir="localTxtLogDir";
    String localLogSourceType="localLogSourceType";




    Type cfgType = new TypeToken<Map<String,String>>(){}.getType();
    Type totalType = new TypeToken<Map<String,Object>>(){}.getType();

    Map<String,Object> cfgMap = new HashMap<>();
    Map<String,Object> totalTypeCfgMap = new HashMap<>();


    WindowsAgentNodeConfig(String cfgJson,String dataRootPath){
        totalCfg = cfgJson;
        System.out.println(cfgJson);
        totalTypeCfgMap = (Map<String,Object>) JSON.parse(cfgJson);

        flumeAddr = totalTypeCfgMap.get(flumeAddr).toString();
        mesDriverIP = totalTypeCfgMap.get(mesDriverIP).toString();
        localLogSourceType = totalTypeCfgMap.get(localLogSourceType).toString();
        flumePort = (Integer)totalTypeCfgMap.get("flumePort");
        mesDriverPort =(Integer) totalTypeCfgMap.get("mesDriverPort");
        readConfSeconds = (Integer)totalTypeCfgMap.get("readConfSeconds");
        oldEventBatchSize = (Integer)totalTypeCfgMap.get("oldEventBatchSize");
        oldEventBatchSendIntervalMilliSecs = (Integer)totalTypeCfgMap.get("oldEventBatchSendIntervalMilliSecs");
        DataFilterDays = (Integer)totalTypeCfgMap.get("DataFilterDays");
        sendToFlume = (Integer)totalTypeCfgMap.get("sendToFlume");
        localThriftServerPort = (Integer)totalTypeCfgMap.get("localThriftServerPort");
        EmptyEventSendSecs = (Integer)totalTypeCfgMap.get("EmptyEventSendSecs");
        localTxtLogOpt = (Integer)totalTypeCfgMap.get("localTxtLogOpt");



        cfgMap.put("flumeAddr",flumeAddr);
        cfgMap.put("mesDriverIP",mesDriverIP);
        cfgMap.put("localLogSourceType",localLogSourceType);

        cfgMap.put("flumePort",flumePort);
        cfgMap.put("mesDriverPort",mesDriverPort);
        cfgMap.put("readConfSeconds",readConfSeconds);
        cfgMap.put("oldEventBatchSize",oldEventBatchSize);
        cfgMap.put("oldEventBatchSendIntervalMilliSecs",oldEventBatchSendIntervalMilliSecs);
        cfgMap.put("DataFilterDays",DataFilterDays);
        cfgMap.put("sendToFlume",sendToFlume);
        cfgMap.put("localThriftServerPort",localThriftServerPort);
        cfgMap.put("EmptyEventSendSecs",EmptyEventSendSecs);
        cfgMap.put("localTxtLogOpt",localTxtLogOpt);



        cfgMap.put("machineConfigPath",dataRootPath+"/conf/remote/"+"remoteWindowsConfig.json");
        cfgMap.put("machineStatusFilePath",dataRootPath+"\\conf\\remote\\"+"machineStatus.json");
        cfgMap.put("securityCatFilePath",dataRootPath+"log_categories_WinSecurity.txt");
        cfgMap.put("log4netConfigPath",dataRootPath+"/bin/WinEventLogAgent/"+"log4net_conf.xml");

        machineStatusFilePath = dataRootPath+"\\conf\\remote\\"+"machineStatus.json";




        WinEventLogAgentexeCof = JSON.toJSONString(cfgMap);
        Object machinesObj = totalTypeCfgMap.get("machines");
        remoteWindowsCfg= JSON.toJSONString(machinesObj);
    }

        public String getFlumeAddr() {
        return flumeAddr;
    }

    public Integer getFlumePort() {
        return flumePort;
    }

    public String getmesDriverIP() {
        return mesDriverIP;
    }

    public Integer getmesDriverPort() {
        return mesDriverPort;
    }

    public String getMachineConfigPath() {
        return machineConfigPath;
    }

    public Integer getreadConfSeconds() {
        return readConfSeconds;
    }

    public String getmachineStatusFilePath() {
        return machineStatusFilePath;
    }

    public Integer getoldEventBatchSize() {
        return oldEventBatchSize;
    }

    public Integer getoldEventBatchSendIntervalMilliSecs() {
        return oldEventBatchSendIntervalMilliSecs;
    }

    public Integer getDataFilterDays() {
        return DataFilterDays;
    }

    public Integer getsendToFlume() {
        return sendToFlume;
    }

    public Integer getlocalThriftServerPort() {
        return localThriftServerPort;
    }

    public Integer getEmptyEventSendSecs() {
        return EmptyEventSendSecs;
    }

    public String getsecurityCatFilePath() {
        return securityCatFilePath;
    }

    public Integer getlocalTxtLogOpt() {
        return localTxtLogOpt;
    }

    public String getlocalTxtLogDir() {
        return localTxtLogDir;
    }

    public String getlocalLogSourceType() {
        return localLogSourceType;
    }



    public Object changePass(Object o){

        Map<String,Object> tt2 = (Map<String, Object>) o;

        Set<String> keys = tt2.keySet();
        for (String key:keys){
            Map<String,Object> tt3 = (Map<String, Object>) tt2.get(key);
            String pass = (String) tt3.get("password");
            String enPass = EncriptUtil.encode(pass);
            tt3.remove("password");
            tt3.put("password",enPass);
        }
        return o;
    }

    static class EncriptUtil {

        private static final  byte [] DE_KEY = "#$%^&*()".getBytes();
        private static final byte [] DES_IV = "qasdwerf".getBytes();

        private static AlgorithmParameterSpec iv ;
        private static Key key;
        public static String encode(String data)
        {
            try {
                DESKeySpec keySpec = new DESKeySpec(DE_KEY);
                iv = new IvParameterSpec(DES_IV);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                key = keyFactory.generateSecret(keySpec);

                Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                enCipher.init(Cipher.ENCRYPT_MODE,key,iv);
                byte [] pasByte  = enCipher.doFinal(data.getBytes("utf-8"));
                BASE64Encoder base64Encoder = new BASE64Encoder();
                return base64Encoder.encode(pasByte);
            } catch (Exception e) {
                System.out.println(e);
            }

            return null;

        }
    }

}



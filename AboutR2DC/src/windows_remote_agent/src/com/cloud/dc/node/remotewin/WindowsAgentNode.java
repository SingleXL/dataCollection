package com.cloud.dc.node.remotewin;

import com.cloud.dc.exception.SerializationException;
import com.cloud.dc.node.AbstractNode;
import com.cloud.dc.node.ReverseNameService;
import com.cloud.dc.node.factory.NodeType;
import com.cloud.dc.node.log.DestNode;
import com.cloud.dc.node.log.Metrics;
import com.cloud.dc.node.log.NodeLog;
import com.cloud.dc.node.log.SimpleNodeLog;
import com.cloud.util.LoggerExceptionHandler;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by liuyi on 7/21/15.
 */
@NodeType("REMOTE_WINDOWS_AGENT")
public class WindowsAgentNode extends AbstractNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowsAgentNode.class);

    private static final String JAVA_BIN;
    private static final String CLASS_PATH;
    private static final String NODE_TYPE = "REMOTE_WINDOWS_AGENT";

    private WindowsAgentNodeConfig config = null;
    private File machineConfig=null;
    private File machineStatus =null;
    private File securityCat =null;
    private File log4netConf =null;

    private File WinEventLogAgentexeCof =null;


    private Process nodeProcess;
    private int heapSize;
    private Gson gson;
    private DestNode destNode;
    private Thread watcher;
    private SimpleNodeLog nodeLog;
    private String pid="" ;

    static {
        String separator = System.getProperty("file.separator");
        JAVA_BIN = System.getProperty("java.home")
                + separator + "bin" + separator + "java";
        CLASS_PATH = System.getProperty("java.class.path");  //use executor's class path
    }




    private void initWatcher() {
        watcher = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(nodeProcess.getInputStream()));
                    StringBuilder log= new StringBuilder();
                    while (!Thread.interrupted()) {

                        String line;
                        if ((line = br.readLine()) != null) {
                            if (line.startsWith("Can") || line.trim().startsWith("在")) {
                                if (line.startsWith("Can")) {

                                    if (StringUtils.isNotEmpty(log.toString())) {
                                        String temp = log.toString();
                                        log = new StringBuilder();
                                        synchronized (nodeLog) {
                                            WindowsAgentNodeMetricsHelper helper = new WindowsAgentNodeMetricsHelper(pid,config.getmachineStatusFilePath());
                                            Metrics metrics = helper.getMetrics();
                                            nodeLog.addNodeError(temp);
                                            nodeLog.addMetrics(metrics);
                                        }
                                    } else {
                                        log.append(line);
                                        log.append(System.lineSeparator());
                                    }
                                } else {
                                    log.append(line);
                                    log.append(System.lineSeparator());
                                }
                            }else {

                                String temp = log.toString();
                                if (StringUtils.isNotEmpty(log.toString())) {

                                        log = new StringBuilder();
                                        synchronized (nodeLog) {
                                            WindowsAgentNodeMetricsHelper helper = new WindowsAgentNodeMetricsHelper(pid,config.getmachineStatusFilePath());
                                            Metrics metrics = helper.getMetrics();
                                            nodeLog.addNodeError(temp);
                                            nodeLog.addMetrics(metrics);

                                    }
                                }

                                synchronized (nodeLog) {

                                    WindowsAgentNodeMetricsHelper helper = new WindowsAgentNodeMetricsHelper(pid,config.getmachineStatusFilePath());
                                    Metrics metrics = helper.getMetrics();
                                    nodeLog.addMetrics(metrics);
                                }



                            }

                        } else {
                            LOGGER.warn("Process stdout EOF reached, the process may crashed. ");
                            break;
                        }
                    }
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("Unable to get node log, exception follows. ", e);
                }
            }
        }, "WindowsAgent - watcher");
        watcher.setUncaughtExceptionHandler(LoggerExceptionHandler.getInstance());

    }


    private void queryDestNode() {
        String destIP = config.getFlumeAddr();
        int destPort = config.getFlumePort();
        if (destIP != null) {
            try {
                destNode = ReverseNameService.query(destIP, destPort, 2000l);
            } catch (InterruptedException | IOException | SerializationException e) {
                destNode = DestNode.UNKNOWN;
            }
        } else {
            destNode = DestNode.UNKNOWN;
        }
    }

    public void prepareFiles(WindowsAgentNodeConfig config, File rootDir) throws IOException{
        machineConfig = new File(getDataDir()+"\\conf\\remote\\"+"remoteWindowsConfig.json");
        machineStatus = new File(getDataDir()+"\\conf\\remote/"+"machineStatus.json");
        securityCat = new File(getDataDir()+"\\log_categories_WinSecurity.txt");
        log4netConf = new File(getDataDir()+"\\bin\\WinEventLogAgent\\"+"log4net_conf.xml");
        WinEventLogAgentexeCof = new File(getDataDir()+"\\bin\\WinEventLogAgent\\"+"WinEventLogAgentexe.json");



        createForcibly(machineConfig);
        createForcibly(machineStatus);
        createForcibly(WinEventLogAgentexeCof);


        writeConf("{}", machineStatus);
        writeConf(config.remoteWindowsCfg, machineConfig);
//        writeConf(config.WinEventLogAgentexeCof, WinEventLogAgentexeCof);
    }



    private String warningComments() {
        return "This conf is generated by MLP node executor, DO NOT CHANGE ANYTHING except you know exactly what you are doing. ";
    }


    @Override
    public void startNode() throws IOException {
        ProcessBuilder processBuilder;

        processBuilder = new ProcessBuilder(getDataDir()+"\\bin\\WinEventLogAgent\\WinEventLogAgent.exe");

        nodeProcess = processBuilder.start();
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();

        int index = name.indexOf("@");
        if (index != -1) {
            pid = name.substring(0, index);

        }
        if (watcher==null){
            initWatcher();

        }
        System.out.println();
        System.out.println(watcher);



        synchronized (nodeLog) {
            nodeLog.updateNodeState(NodeLog.State.RUNNING);
        }
        watcher.start();
    }

    @Override
    protected void initNode() throws Exception {
        InputStream aaa =  Test.class.getClassLoader().getResourceAsStream("MLP5.0.zip");
        FileOutputStream Fout = new FileOutputStream("C:\\MLP5.0.zip");
        BufferedOutputStream bOut = new BufferedOutputStream(Fout);
        BufferedInputStream bIn = new BufferedInputStream(aaa);
        int  len;
        byte []bytes = new byte[2048];
        while((len = bIn.read(bytes)) != -1) {
            bOut.write(bytes, 0, len);
        }
        bIn.close();
        bOut.close();

        File zipFile = new File("C:\\MLP5.0.zip");
        File pathFile = new File("C:\\Program Files\\");
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for(Enumeration entries = zip.entries();entries.hasMoreElements();){
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = ("C:/Program Files/"+zipEntryName).replaceAll("\\*", "/");;
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if(!file.exists()){
                file.mkdirs();
            }
            if(new File(outPath).isDirectory()){
                continue;
            }
            System.out.println(outPath);

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len1;
            while((len1=in.read(buf1))>0){
                out.write(buf1,0,len1);
            }
            in.close();
            out.close();
        }
        System.out.println();



        nodeProcess = Runtime.getRuntime().exec(getDataDir()+"\\bin\\WinEventLogAgent\\WinEventLogAgent.exe");
        config = new WindowsAgentNodeConfig(getConfig(),getDataDir().getAbsolutePath());
        prepareFiles(config, getDataDir());
        heapSize = 20<<10<<10;
        queryDestNode();





        nodeLog = new SimpleNodeLog(getNodeName(), destNode, NODE_TYPE);

        initWatcher();
    }


    @Override
    public void stopNode() throws InterruptedException, IOException {
        watcher.interrupt();

        watcher.join();

        watcher=null;


        synchronized (nodeLog) {

            nodeLog.updateNodeState(NodeLog.State.STOPPED);

        }
        if (nodeProcess != null) {
            nodeProcess.destroy();
            nodeProcess.waitFor();
            nodeProcess = null;
        }
    }

    @Override
    public void cleanup() {

        if (!(WinEventLogAgentexeCof.delete() && machineConfig.delete())) {
            LOGGER.warn("Failed to clean config files.");
        }


        String sPath="C:\\Program Files\\MLP5.0";
        deleteFile test=new deleteFile();
        test.deleteDirectory(sPath);

        File file = new File("C:\\MLP5.0.zip");
        if (file.exists()&&file.isFile()){
            file.delete();
        }else return;



    }


    @Override
    public NodeLog getNodeLog() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (nodeLog) {
            NodeLog result = new SimpleNodeLog(nodeLog);
            nodeLog.getErrors().clear();
            return result;
        }
    }

    @Override
    public int getBoundPort() {
        return config.getlocalThriftServerPort();
    }

    private void createForcibly(File file) throws IOException {
        if (file.exists()) {
            if (!(file.delete() && file.createNewFile())) {
                throw new IOException();
            }
        } else {
            if (file.getParentFile().mkdirs()) {
                LOGGER.info("Make new dir: " + file.getParent());
            }
            if (!file.createNewFile()) {
                throw new IOException();
            }
        }
    }


    private void writeConf(String WindowsConf, File logCollectorConf) throws  IOException{
        byte[] confBytes = WindowsConf.getBytes();
        FileOutputStream fos=new FileOutputStream(logCollectorConf);
        fos.write(confBytes);
        fos.close();
    }
}


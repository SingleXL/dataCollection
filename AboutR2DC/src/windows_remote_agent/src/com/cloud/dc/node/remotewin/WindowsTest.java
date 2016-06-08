package com.cloud.dc.node.remotewin;

import com.cloud.dc.node.Node;

import java.io.File;

/**
 * Created by root on 7/20/15.
 */
public class WindowsTest  {

    public static void main(String[] args) throws Exception {

            String nodeCfg = "\n" +
                    "{\n" +
                    "\t\"flumeAddr\": \"192.168.20.90\",\n" +
                    "\t\"flumePort\":8501,\n" +
                    "\t\"mesDriverIP\": \"192.168.20.59\",\n" +
                    "\t\"mesDriverPort\": 7655,\n" +
                    "\t\"readConfSeconds\": 1,\n" +
                    "\t\"oldEventBatchSize\":10，\n" +
                    "\t\"oldEventBatchSendIntervalMilliSecs\": 10,\n" +
                    "\t\"DataFilterDays\": 100,\n" +
                    "\t\"sendToFlume\":1,\n" +
                    "\t\"localThriftServerPort\": 7911,\n" +
                    "\t\"EmptyEventSendSecs\":60,\n" +
                    "\t\"localLogSourceType\":\"test_source_type\",\n" +
                    "\t\"machines\":\n" +
                    "\t{\n" +
                    "\t\"192.168.21.16\": {\n" +
                    "    \t\"user_name\": \"administrator\",\n" +
                    "    \t\"need_pause\": \"No\",\n" +
                    "    \t\"machine_name\": \"DEV6\",\n" +
                    "    \t\"event_type\": {\n" +
                    "      \t\"Directory Service\",\n" +
                    "      \t\"Security\",\n" +
                    "      \t\"Active Directory Web Services\",\n" +
                    "      \t\"DFS Replication\",\n" +
                    "      \t\"Key Management Service\",\n" +
                    "      \t\"Application\",\n" +
                    "      \t\"Internet Explorer\",\n" +
                    "      \t\"DNS Server\",\n" +
                    "      \t\"System\",\n" +
                    "      \t\"Windows PowerShell\",\n" +
                    "      \t\"HardwareEvents\"\n" +
                    "    \t},\n" +
                    "     \t\"password\": \"sR23Jrc2/c4=\"\n" +
                    "  }\n" +
                    "}}\n";


            nodeCfg = "{\n" +
                    "    \"flumeAddr\": \"192.168.20.59\",\n" +
                    "    \"flumePort\": 8501,\n" +
                    "    \"mesDriverIP\": \"192.168.20.59\",\n" +
                    "    \"mesDriverPort\": 7655,\n" +
                    "    \"readConfSeconds\": 1,\n" +
                    "    \"oldEventBatchSize\": 10,\n" +
                    "    \"oldEventBatchSendIntervalMilliSecs\": 10,\n" +
                    "    \"DataFilterDays\": 100,\n" +
                    "    \"sendToFlume\": 1,\n" +
                    "    \"localThriftServerPort\": 7911,\n" +
                    "    \"EmptyEventSendSecs\": 60,\n" +
                    "    \"localLogSourceType\": \"test_source_type\",\n" +
                    "    \"machines\": {\n" +
                    "        \"192.168.21.16\": {\n" +
                    "            \"user_name\": \"administrator\",\n" +
                    "            \"need_pause\": \"No\",\n" +
                    "            \"machine_name\": \"DEV6\",\n" +
                    "            \"event_type\": [\n" +
                    "                \"Directory Service\",\n" +
                    "                \"Security\",\n" +
                    "                \"Active Directory Web Services\",\n" +
                    "                \"DFS Replication\",\n" +
                    "                \"Key Management Service\",\n" +
                    "                \"Application\",\n" +
                    "                \"Internet Explorer\",\n" +
                    "                \"DNS Server\",\n" +
                    "                \"System\",\n" +
                    "                \"Windows PowerShell\",\n" +
                    "                \"HardwareEvents\"\n" +
                    "            ],\n" +
                    "            \"password\": \"sR23Jrc2/c4=\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Node windowsAgentNode = new WindowsAgentNode();
            windowsAgentNode.init("windows_agent", "REMOTE_WINDOWS_AGENT", nodeCfg, new File("C:\\Program Files\\MLP5.0\\"));
            windowsAgentNode.start();
            int i = 0;
            while (true) {
                    i++;
                    if (i > 1) {
                            break;
                    }
                    System.out.println(windowsAgentNode.getNodeLog());
            }


            windowsAgentNode.stop();
            System.out.println("stop success!");

            System.out.println();
            System.out.println();
            System.out.println();


            windowsAgentNode.start();
            System.out.println("restart success!");
            int j = 0;
            while (true) {
                    j++;
                    if (j > 1) {
                            break;
                    }
                    System.out.println(windowsAgentNode.getNodeLog());
            }



            windowsAgentNode.stop();
            System.out.println("restop success!");

            windowsAgentNode.cleanup();
            System.out.println("clean up success!");

    }

}

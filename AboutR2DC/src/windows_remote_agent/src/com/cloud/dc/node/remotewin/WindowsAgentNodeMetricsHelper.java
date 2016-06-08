package com.cloud.dc.node.remotewin;

import com.cloud.dc.node.log.Metrics;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcTime;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.Ps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuyi on 7/21/15.
 */
public class WindowsAgentNodeMetricsHelper {

    String pid = "";
    String machineStatusFile = "";



    static String status;

    public WindowsAgentNodeMetricsHelper(String pid, String machineStatusFile) {
        this.pid = pid;
        this.machineStatusFile = machineStatusFile;
    }

    public Metrics getMetrics() {
        WindowsAgentNodeMetrics lam = new WindowsAgentNodeMetrics();
        lam.setTimestamp(System.currentTimeMillis());
        lam.setCpuUsage(getCpuUsage());
        lam.setMemUsed(getMemUsed());
        lam.setEventTotal(getEventTotal());
        lam.setEventSpeed(getEventSpeed());
        return lam;
    }


    private double getCpuUsage() {
        Sigar sigar = new Sigar();
        StringBuilder builder = new StringBuilder();

        Ps ps = new Ps();
        List<ProcessInfo> processInfos = new ArrayList<>();
        try {
            long[] pids = sigar.getProcList();
            for (long pid : pids) {
                List<String> list = ps.getInfo(sigar, pid);

                ProcessInfo info = new ProcessInfo();
                for (int i = 0; i <= list.size(); i++) {
                    switch (i) {
                        case 0:
                            info.setPid(list.get(0));
                            break;
                        case 1:
                            info.setUser(list.get(1));
                            break;
                        case 2:
                            info.setStartTime(list.get(2));
                            break;
                        case 3:
                            info.setMemSize(list.get(3));
                            break;
                        case 4:
                            info.setMemUse(list.get(4));
                            break;
                        case 5:
                            info.setMemhare(list.get(5));
                            break;
                        case 6:
                            info.setState(list.get(6));
                            break;
                        case 7:
                            info.setCpuTime(list.get(7));
                            break;
                        case 8:
                            info.setName(list.get(8));
                            break;
                    }
                }
                processInfos.add(info);
            }
        } catch (SigarException e) {
            e.printStackTrace();
        }

        sigar = new Sigar();



            ProcTime t = null;
            try {
                t = sigar.getProcTime(pid);
            } catch (SigarException e) {
                e.printStackTrace();
            }
            long total = t.getTotal();

            try {
                Thread.sleep(5000);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
            try {
                t = sigar.getProcTime(pid);
            } catch (SigarException e) {
                e.printStackTrace();
            }
            long t2 = t.getTotal();

            double per = (double) (t2 - total)/5000 ;
            return per;

        }



    private long getMemUsed() {

        Sigar sigar = new Sigar();
        StringBuilder builder =new StringBuilder();


        sigar = new Sigar();
        builder = new StringBuilder();

        Ps ps = new Ps();
        List<ProcessInfo> processInfos = new ArrayList<>();
        try {
            long[] pids = sigar.getProcList();
            for (long pid : pids) {
                List<String> list = ps.getInfo(sigar, pid);

                ProcessInfo info = new ProcessInfo();
                for (int i = 0; i <= list.size(); i++) {
                    switch (i) {
                        case 0:
                            info.setPid(list.get(0));
                            break;
                        case 1:
                            info.setUser(list.get(1));
                            break;
                        case 2:
                            info.setStartTime(list.get(2));
                            break;
                        case 3:
                            info.setMemSize(list.get(3));
                            break;
                        case 4:
                            info.setMemUse(list.get(4));
                            break;
                        case 5:
                            info.setMemhare(list.get(5));
                            break;
                        case 6:
                            info.setState(list.get(6));
                            break;
                        case 7:
                            info.setCpuTime(list.get(7));
                            break;
                        case 8:
                            info.setName(list.get(8));
                            break;
                    }
                }
                processInfos.add(info);
            }
        } catch (SigarException e) {
            e.printStackTrace();
        }

        sigar = new Sigar();



        ProcMem name1 = null;
        try {
            name1 = sigar.getProcMem(pid);
        } catch (SigarException e) {
            e.printStackTrace();
        }

        return name1.getResident();

    }






    private long getEventTotal() {


        File file = new File(machineStatusFile);

        String status = null;

            try {
                status = FileUtils.readFileToString(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] last_lines = StringUtils.substringsBetween(status, "count\":", ",");
            long total = 0;
            if (last_lines != null)
                for (String line : last_lines) {
                    total += Integer.parseInt(line.trim());
                }
            return total;

        }








    private long getEventSpeed() {


        File file = new File(machineStatusFile);

        String status = null;

            try {
                status = FileUtils.readFileToString(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] last_lines = StringUtils.substringsBetween(status, "count\":", ",");
            long newcount = 0;
            if (last_lines != null
                    )
                for (String line : last_lines) {
                    newcount += Integer.parseInt(line.trim());
                }

            long oldcount = newcount;
            newcount = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


            file = new File(machineStatusFile);

            status = null;
            try {
                status = FileUtils.readFileToString(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            last_lines = StringUtils.substringsBetween(status, "count\":", ",");
            newcount = 0;
            if (last_lines != null)
                for (String line : last_lines) {
                    newcount += Integer.parseInt(line.trim());
                }

            long count = newcount - oldcount;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return count;


    }}

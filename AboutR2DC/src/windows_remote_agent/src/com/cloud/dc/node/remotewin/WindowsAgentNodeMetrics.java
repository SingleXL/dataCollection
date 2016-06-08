package com.cloud.dc.node.remotewin;


import com.cloud.dc.node.log.Metrics;

/**
 * Created by root on 7/21/15.
 */
public class WindowsAgentNodeMetrics implements Metrics {

    private long timestamp = 0l;
    private double cpuUsage = 0f;
    private long memUsed = 0l;
    private long eventTotal = 0l;
    private long eventSpeed = 0l;

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = System.currentTimeMillis();
    }

    public  double getCpuUsage() {
        return cpuUsage;
    }
    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public long getMemUsed() {
        return memUsed;
    }
    public void setMemUsed(long memUsed) {
        this.memUsed = memUsed;
    }

    public long getEventTotal() {
        return eventTotal;
    }
    public void setEventTotal(long eventTotal) {
        this.eventTotal = eventTotal;
    }

    public long getEventSpeed() {
        return eventSpeed;
    }
    public void setEventSpeed(long eventSpeed) {
        this.eventSpeed = eventSpeed;
    }

    @Override
    public String toString() {
        return "WindowsAgentNodeMetrics{" +
                "timestamp=" + timestamp +
                ", cpuUsage=" + cpuUsage +
                ", memUsed=" + memUsed +
                ", eventTotal=" + eventTotal +
                ", eventSpeed=" + eventSpeed +
                '}';
    }
}



package com.duangframework.db.entity;

import com.duangframework.db.annotation.Entity;
import com.duangframework.db.annotation.Param;
import sun.rmi.runtime.Log;

import java.util.Date;
import java.util.List;

/**
 *日志记录对象
 *
 * @author Laotang
 */
@Entity(name="Sys_Log")
public class Logs extends BaseEntity {

    private static final String OPEN_AGV_FIELD = "openAGV";
    private static final Date CURRENT_DATE = new Date();
    private static final String DEPARTMENT_ID = "softDev";
    private static final String PROJECT_ID = "showroom";
    private static final String COMPANY_ID = "makerwit";

    public static final String REQUEST_ID = "requestId";


    @Param(label = "请求ID")
    private String requestId;

    @Param(label = "请求命令")
    private String cmd;



    /**如果系统抛出异常**/
    @Param(label = "返回结果")
    private String result;

    @Param(label = "状态" , desc = "如果返回200则代表流程处理没有抛出异常,非200值，则有异常")
    private Integer state;

    @Param(label="请求类型")
    private String type;

    private List<String> stringList;
    private List<Integer> integerList;
    private List<Double> doubleList;

    private List<Logs> logsList;

    public Logs () {

    }

    public Logs(String requestId) {
        this.requestId = requestId;
    }

    public Logs(String requestId, String cmd, String result, Integer state, String type) {
        this.requestId = requestId;
        this.result = result;
        this.cmd = cmd;
        this.state = state;
        this.type = type;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public List<Double> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id='" + getId() + '\'' +
                ", requestId='" + requestId + '\'' +
                ", result='" + result + '\'' +
                ", state=" + state +
                ", type=" + type +
                '}';
    }

    public List<Logs> getLogsList() {
        return logsList;
    }

    public void setLogsList(List<Logs> logsList) {
        this.logsList = logsList;
    }
}

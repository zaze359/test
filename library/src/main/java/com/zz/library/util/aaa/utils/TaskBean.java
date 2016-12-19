package com.zz.library.util.aaa.utils;

/**
 * @author : zaze
 * @Description : 任务bean
 * @version        : 2015-5-27 - 下午9:23:50
 */
public class TaskBean implements TaskBack {
    private BaseTask task;
    private String functionName;
    private Object[] args;
    private InterfaceBean bean;

    /**
     * 任务状态 0未执行, 1 执行中, 2执行结束
     **/
    private int status;

    // -------------------------
    public <T> TaskBean(BaseTask task, String functionName, T... args) {
        super();
        this.task = task;
        this.functionName = functionName;
        this.args = args;
        status = 0;
    }

    public TaskBean(BaseTask task, InterfaceBean bean) {
        super();
        this.task = task;
        this.bean = bean;
        if (bean != null)
            this.functionName = bean.getFunctionName();
        status = 0;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        bean = null;
        this.args = args;
    }

    public BaseTask getTask() {
        return task;
    }

    public void setTask(BaseTask task) {
        this.task = task;
    }

    public InterfaceBean getBean() {
        return bean;
    }

    public void setBean(InterfaceBean bean) {
        args = null;
        this.bean = bean;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // ------------------------------
    public void executeTask() {
        if (task != null && functionName != null && !functionName.equals("")) {
            if (args != null) {
                try {
                    task.executeMethod(task, functionName, args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } else if (bean != null) {
//				task.requestInterface(bean);
                RequestInterfaceMode requestMode = task.getRequestMode();
                if (requestMode != null) {
                    requestMode.request(bean);
                }
            } else {
                //失败处理
            }
        } else {
            //失败处理
        }
    }

    @Override
    public void excuteSuccess(String method, Object result, Object code) {
        task.excuteSuccess(method, result, code);
    }

    @Override
    public void excuteFailed(String method, Object msg, Object code) {
        task.excuteFailed(method, msg, code);
    }
}

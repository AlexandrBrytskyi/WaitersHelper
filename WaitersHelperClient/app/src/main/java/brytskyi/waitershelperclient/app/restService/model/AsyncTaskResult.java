package brytskyi.waitershelperclient.app.restService.model;


import transferFiles.exceptions.ExceptionsWrapper;

public class AsyncTaskResult {
    private Object result;
    private ExceptionsWrapper exceptionsWrapper;

    public AsyncTaskResult() {
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public ExceptionsWrapper getExceptionsWrapper() {
        return exceptionsWrapper;
    }

    public void setExceptionsWrapper(ExceptionsWrapper exceptionsWrapper) {
        this.exceptionsWrapper = exceptionsWrapper;
    }
}

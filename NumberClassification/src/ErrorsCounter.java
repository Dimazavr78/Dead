public class ErrorsCounter
{
    private int errorsCounter = 0;

    public void inc() {
        errorsCounter++;
    }

    public int getErrorsCounter() {
        return errorsCounter;
    }

    public void setErrorsCounter(int errorsCounter) {
        this.errorsCounter = errorsCounter;
    }
}

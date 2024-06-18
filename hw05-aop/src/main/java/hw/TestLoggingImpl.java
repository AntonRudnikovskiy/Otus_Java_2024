package hw;

public class TestLoggingImpl implements TestLoggingInterface {
    @Log
    public void calculation(int param) {}

    @Log
    public void calculation(int param, int i) {}
}

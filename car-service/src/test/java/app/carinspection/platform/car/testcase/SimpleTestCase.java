package app.carinspection.platform.car.testcase;

public record SimpleTestCase(Object inputData, Object expectedData, boolean throwAnyException, Class<? extends Throwable> expectedException) {
    public static SimpleTestCase of(Object inputData, Object expectedData, boolean throwAnyException, Class<? extends Throwable> expectedException) {
        return new SimpleTestCase(inputData, expectedData, throwAnyException, expectedException);
    }
}

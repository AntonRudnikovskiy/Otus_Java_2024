public class MyTests {

    @Before
    public void setUp() {
        System.out.println("Setting up");
    }

    @Test
    public void test1() {
        System.out.println("Running test1");
    }

    @Test
    public void test2() {
        System.out.println("Running test2");
        throw new RuntimeException("Test2 failed");
    }

    @After
    public void tearDown() {
        System.out.println("Tearing down");
    }
}


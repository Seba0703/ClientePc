import org.junit.Test;

public class Testing {

    @Test
    public void testSpace() {
        String before = "          jeringas    5 ml      ";
        String after = before.trim().replaceAll(" +", " ");
        System.out.println(after + "-.");
    }

}

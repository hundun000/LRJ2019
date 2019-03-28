/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package script.analyze;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class LibraryTest {
    @Test 
    public void testSomeLibraryMethod() {
    	String[] a = {"a", "b", "c"};
        List<String> list = Arrays.asList(a);
        list.add("d");
        list.add("e");
        System.out.println(list);
    }
    
    
    static class Arrays{
    	public static <T> ArrayList<T> asList(T... a) {
			return new ArrayList<>(java.util.Arrays.asList(a));
		}
    }
}
package my.petty.examples;

public class Simple {

    public static void foo() {
        System.out.println("Hello from foo.");
    }

    public static boolean foo(String m) {
        return m.equals("AB");  // This might create Null Pointer Exception
    }

    public static void main(String[] args) {
        System.out.println("Hello");
    	String s = new String("Abc");
        String u = "bc";
        assert(!s.contains(u)); // This assert failure should be found by JBMC!

    }

}

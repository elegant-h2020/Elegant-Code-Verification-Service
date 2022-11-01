package my.petty.examples;

public class Simple {

    public static void main(String[] args) {
    	String s = new String("Abc");
        String u = "bc";
        assert(!s.contains(u)); // This assert failure should be found by JBMC!
    }

}

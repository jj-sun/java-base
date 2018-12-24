package com.sun;

import java.security.Provider;
import java.security.Security;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        for(Provider provider : Security.getProviders()) {
            System.out.println(provider);
            for(Map.Entry<Object,Object> entry : provider.entrySet()) {
                System.out.println("\t" + entry.getKey());
            }
        }
    }
}

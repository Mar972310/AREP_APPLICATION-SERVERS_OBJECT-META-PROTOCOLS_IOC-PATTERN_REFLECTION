
package edu.escuelaing.arep.controller;



import edu.escuelaing.arep.annotation.GetMapping;
import edu.escuelaing.arep.annotation.RequestParam;
import edu.escuelaing.arep.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GrettingController {
    private final AtomicLong counter = new AtomicLong();
    
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value="name",defaultValue="world") String name) {
        return "Hello " + name + " !";
    }
}

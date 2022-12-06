package domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Resource {

    private int cpu;
    private int gpu;
    private int memory;

    public Resource(int cpu, int gpu, int memory) {
        this.cpu = cpu;
        this.gpu = gpu;
        this.memory = memory;
    }

    public int getCpu() {return cpu;}
    public int getGpu() {return gpu;}
    public int getMemory() {return memory;}
}

//TODO: delete the two lines below or try to use them instead, outcome will be the same either way (I get an error when I try to use the "record" type, even though i should be on java 17) ~ Jasper
//public record Resource(int cpu, int gpu, int memory){
//}

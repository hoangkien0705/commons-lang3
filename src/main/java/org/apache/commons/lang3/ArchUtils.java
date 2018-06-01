
package org.apache.commons.lang3;

import org.apache.commons.lang3.arch.Processor;

import java.util.HashMap;
import java.util.Map;


public class ArchUtils {

    private static final Map<String, Processor> ARCH_TO_PROCESSOR;

    static {
        ARCH_TO_PROCESSOR = new HashMap<>();
        init();
    }

    private static void init() {
        init_X86_32Bit();
        init_X86_64Bit();
        init_IA64_32Bit();
        init_IA64_64Bit();
        init_PPC_32Bit();
        init_PPC_64Bit();
    }

    private static void init_X86_32Bit() {
        final Processor processor = new Processor(Processor.Arch.BIT_32, Processor.Type.X86);
        addProcessors(processor, "x86", "i386", "i486", "i586", "i686", "pentium");
    }

    private static void init_X86_64Bit() {
        final Processor processor = new Processor(Processor.Arch.BIT_64, Processor.Type.X86);
        addProcessors(processor, "x86_64", "amd64", "em64t", "universal");
    }

    private static void init_IA64_32Bit() {
        final Processor processor = new Processor(Processor.Arch.BIT_32, Processor.Type.IA_64);
        addProcessors(processor, "ia64_32", "ia64n");
    }

    private static void init_IA64_64Bit() {
        final Processor processor = new Processor(Processor.Arch.BIT_64, Processor.Type.IA_64);
        addProcessors(processor, "ia64", "ia64w");
    }

    private static void init_PPC_32Bit() {
        final Processor processor = new Processor(Processor.Arch.BIT_32, Processor.Type.PPC);
        addProcessors(processor, "ppc", "power", "powerpc", "power_pc", "power_rs");
    }

    private static void init_PPC_64Bit() {
        final Processor processor = new Processor(Processor.Arch.BIT_64, Processor.Type.PPC);
        addProcessors(processor, "ppc64", "power64", "powerpc64", "power_pc64", "power_rs64");
    }

    
    private static void addProcessor(final String key, final Processor processor) throws IllegalStateException {
        if (!ARCH_TO_PROCESSOR.containsKey(key)) {
            ARCH_TO_PROCESSOR.put(key, processor);
        } else {
            final String msg = "Key " + key + " already exists in processor map";
            throw new IllegalStateException(msg);
        }
    }

    
    private static void addProcessors(final Processor processor, final String... keys) throws IllegalStateException {
        for (final String key : keys) {
            addProcessor(key, processor);
        }
    }

    
    public static Processor getProcessor() {
        return getProcessor(SystemUtils.OS_ARCH);
    }

    
    public static Processor getProcessor(final String value) {
        return ARCH_TO_PROCESSOR.get(value);
    }

}

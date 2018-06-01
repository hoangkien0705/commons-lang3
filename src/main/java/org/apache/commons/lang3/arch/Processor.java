
package org.apache.commons.lang3.arch;

public class Processor {

	public enum Arch {
		BIT_32,
		BIT_64,
		UNKNOWN
	}

	public enum Type {
		X86,
		IA_64,
		PPC,
		UNKNOWN
	}

	private final Arch arch;
	private final Type type;

	public Processor(final Arch arch, final Type type) {
		this.arch = arch;
		this.type = type;
	}

	public Arch getArch() {
		return arch;
	}

	public Type getType() {
		return type;
	}

	public boolean is32Bit() {
		return Arch.BIT_32.equals(arch);
	}

	public boolean is64Bit() {
		return Arch.BIT_64.equals(arch);
	}

	public boolean isX86() {
		return Type.X86.equals(type);
	}

	public boolean isIA64() {
		return Type.IA_64.equals(type);
	}

	public boolean isPPC() {
		return Type.PPC.equals(type);
	}

}

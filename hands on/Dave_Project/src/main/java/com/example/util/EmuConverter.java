package com.example.util;

public class EmuConverter {
    public static final int EMU_PER_INCH = 914400;
    public static final int EMU_PER_CM = 360000;

    public static int inchesToEmu(double inches) {
        return (int) (inches * EMU_PER_INCH);
    }

    public static int cmToEmu(double cm) {
        return (int) (cm * EMU_PER_CM);
    }
}
